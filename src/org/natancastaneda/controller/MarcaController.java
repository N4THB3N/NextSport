/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.natancastaneda.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.bean.Marca;
import org.natancastaneda.sistema.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;


/**
 *
 * @author Nathan Castañeda
 */
public class MarcaController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Marca> listaMarca;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox cmbCodigo;
    @FXML private TableView tblMarcas;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void cargarDatos(){
        tblMarcas.setItems(getMarcas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Marca,Integer>("codigoMarca"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Marca,String>("descripcion"));
    }
    public ObservableList<Marca>getMarcas(){
        ArrayList<Marca> lista = new ArrayList<Marca>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMarcas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Marca(resultado.getInt("codigoMarca"),resultado.getString("descripcion")));
            }
        }catch(Exception e){
                    e.printStackTrace();
                    }
            return listaMarca = FXCollections.observableArrayList(lista);    
    }
    
    public void seleccionarElemento(){
         if(tblMarcas.getSelectionModel().getSelectedItem() != null){
        cmbCodigo.getSelectionModel().select(buscarMarca(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca()));
        txtDescripcion.setText(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getDescripcion());
         }else{
            JOptionPane.showMessageDialog(null, "¡Cuidado! No puede seleccionar casillas vacías","", JOptionPane.ERROR_MESSAGE);
         }
    }
    public Marca buscarMarca(int codigoMarca){
        Marca resultado = null;
        try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMarcas(?)}");
            procedimiento.setInt(1, codigoMarca);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Marca(registro.getInt("codigoMarca"),registro.getString("descripcion"));
            }
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        return resultado;
    }
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblMarcas.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta==JOptionPane.YES_NO_OPTION)
                    {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMarca(?)}");
                            procedimiento.setInt(1, ((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca());
                            procedimiento.execute();
                            listaMarca.remove(tblMarcas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    public void agregar(){
        Marca registro = new Marca();
        registro.setDescripcion(txtDescripcion.getText());
        try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMarca(?)}");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                listaMarca.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                if(tblMarcas.getSelectionModel().getSelectedItem()!= null)
                {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria");
                }
                break;
            
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                limpiarControles();
                desactivarControles();
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarMarca(?,?)}");
            Marca registro = (Marca)tblMarcas.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoMarca(((Marca)cmbCodigo.getSelectionModel().getSelectedItem()).getCodigoMarca());
            procedimiento.setInt(1, registro.getCodigoMarca());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    public void cancelar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.ELIMINAR;
                
                break;
        }
    }
    public void desactivarControles(){
        txtDescripcion.setDisable(false);
        cmbCodigo.setDisable(true);
    }
    public void activarControles(){
        txtDescripcion.setEditable(true);
        cmbCodigo.setDisable(true);
    }
    public void limpiarControles(){
        txtDescripcion.setText("");
        cmbCodigo.setValue("");
    }
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
