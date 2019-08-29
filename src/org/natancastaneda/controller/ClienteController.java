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
import org.natancastaneda.bean.Cliente;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;

/**
 *
 * @author informatica
 */
public class ClienteController implements Initializable{
   private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
   private Principal escenarioPrincipal;
   private operaciones tipoDeOperacion = operaciones.NINGUNO;
   private ObservableList<Cliente> listaCliente;
   @FXML private TextField txtNit;
   @FXML private TextField txtNombre;
   @FXML private TextField txtDireccion;
   @FXML private ComboBox cmbCodigo;
   @FXML private TableView tblClientes;
   @FXML private TableColumn colCodigo;
   @FXML private TableColumn colDireccion;
   @FXML private TableColumn colNit;
   @FXML private TableColumn colNombre;
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
        tblClientes.setItems(getClientes());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("codigoCliente"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nit"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        
    }
    
    public void ventanaTelefonoCliente(){
        escenarioPrincipal.ventanaTelefonoCliente();
    }
    
    
    
    public ObservableList<Cliente> getClientes(){
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cliente(resultado.getInt("codigoCliente"), resultado.getString("direccion"), resultado.getString("nit"), resultado.getString("nombre")));
                
            }
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        return listaCliente = FXCollections.observableArrayList(lista);     
    }
    
    public void seleccionarElemento(){
        if(tblClientes.getSelectionModel().getSelectedItem() != null)
        {
            cmbCodigo.getSelectionModel().select(buscarCliente(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            txtDireccion.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
            txtNit.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getNit());
            txtNombre.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getNombre());
        }
        else{
            JOptionPane.showMessageDialog(null, "¡Cuidado! No puede seleccionar casillas vacías","", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Cliente buscarCliente(int codigoCliente){
        Cliente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCliente(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cliente(registro.getInt("codigoCliente"),registro.getString("direccion"), registro.getString("nit"), registro.getString("nombre"));               
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
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?");
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCliente(?)}");
                            procedimiento.setInt(1, ((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaCliente.remove(tblClientes.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar una categoría");
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void agregar(){
        Cliente registro = new Cliente();
        registro.setDireccion(txtDireccion.getText());
        registro.setNit(txtNit.getText());
        registro.setNombre(txtNombre.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarClientes(?,?,?)}");
            procedimiento.setString(1, registro.getDireccion());
            procedimiento.setString(2, registro.getNit());
            procedimiento.setString(3, registro.getNombre());
            procedimiento.execute();
            listaCliente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDireccion.setEditable(false);
                    txtNit.setEditable(false);
                    txtNombre.setEditable(true);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCliente(?,?)}");
            Cliente registro = (Cliente)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNombre(txtNombre.getText());
            registro.setCodigoCliente(((Cliente)cmbCodigo.getSelectionModel().getSelectedItem()).getCodigoCliente());
            procedimiento.setString(1, registro.getNombre());
            procedimiento.setInt(2, registro.getCodigoCliente());
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
    
    public void desactivarControles(){
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
        txtNombre.setEditable(false);
        cmbCodigo.setDisable(true);
    }
    
    public void activarControles(){
        txtDireccion.setEditable(true);
        txtNit.setEditable(true);
        txtNombre.setEditable(true);
        cmbCodigo.setDisable(true);
    }
    
    public void limpiarControles(){
        txtDireccion.setText("");
        txtNit.setText("");
        txtNombre.setText("");
        cmbCodigo.setValue("");
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
