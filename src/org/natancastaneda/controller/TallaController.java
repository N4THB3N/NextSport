package org.natancastaneda.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.natancastaneda.bean.Talla;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;

/**
 *
 * @author Nathan Castañeda
 */
public class TallaController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioprincipal;
    private ObservableList<Talla>listaTalla;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox cmbCodTalla;
    @FXML private TableView tblTallas;
    @FXML private TableColumn colCodTalla;
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
        escenarioprincipal.menuPrincipal();
    }
    public void cargarDatos(){
        tblTallas.setItems(getTallas());
        colCodTalla.setCellValueFactory(new PropertyValueFactory<Talla,Integer>("codigoTalla"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Talla,Integer>("descripcion"));
    }
    public ObservableList<Talla>getTallas(){
        ArrayList<Talla> lista = new ArrayList<Talla>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarTalla}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Talla(resultado.getInt("codigoTalla"),resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTalla = FXCollections.observableArrayList(lista);
    }
        public void seleccionarElemento(){
        if(tblTallas.getSelectionModel().getSelectedItem() != null){
            cmbCodTalla.getSelectionModel().select(buscarTalla(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla()));
            txtDescripcion.setText(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "¡Cuidado! No puede seleccionar casillas vacías","", JOptionPane.ERROR_MESSAGE);
        }
    }
       public Talla buscarTalla(int codigoTalla){
        Talla resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTalla(?)}");
            procedimiento.setInt(1, codigoTalla);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Talla(registro.getInt("codigoTalla"),registro.getString("descripcion"));
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
                if(tblTallas.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTalla(?)}");
                            procedimiento.setInt(1, ((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla());
                            procedimiento.execute();
                            listaTalla.remove(tblTallas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria");
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
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    public void agregar(){
        Talla registro = new Talla();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTalla(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTalla.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void editar(){
        switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblTallas.getSelectionModel().getSelectedItem() != null)
                    {
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        txtDescripcion.setEditable(true);
                    }else{
                        JOptionPane.showMessageDialog(null,"Debe seleccionar una Categoria");
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTalla(?,?)}");
            Talla registro = (Talla)tblTallas.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoTalla(((Talla)cmbCodTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());      
            procedimiento.setInt(1,registro.getCodigoTalla());
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
                    btnEditar.setDisable(true);
                    tipoDeOperacion = operaciones.ELIMINAR;
                    break;
        }
    }
    
    public void desactivarControles(){
        txtDescripcion.setEditable(true);
        cmbCodTalla.setDisable(true);
    }
    
    public void activarControles(){
        txtDescripcion.setEditable(true);
        cmbCodTalla.setDisable(true);
    }
    
    public void limpiarControles(){
        txtDescripcion.setText("");
        cmbCodTalla.setValue("");
    }

    public Principal getEscenarioprincipal() {
        return escenarioprincipal;
    }

    public void setEscenarioprincipal(Principal escenarioprincipal) {
        this.escenarioprincipal = escenarioprincipal;
    }
    
}
