
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
import org.natancastaneda.bean.TelefonoCliente;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;


public class TelefonoClienteController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoCliente> listaTelefonoCliente;
    private ObservableList<Cliente> listaCliente;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtNumero;
    @FXML private ComboBox cmbTelefono;
    @FXML private ComboBox cmbCliente;
    @FXML private TableView tblTelefonos;
    @FXML private TableColumn colNumero;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCliente.setItems(getClientes());
    }

    public void cargarDatos(){
        tblTelefonos.setItems(getTelefonoCliente());
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoCliente,String>("descripcion"));
        colNumero.setCellValueFactory(new PropertyValueFactory<TelefonoCliente,String>("numero"));
    }
    
    public void ventanaClientes(){
        escenarioPrincipal.ventanaClientes();
    }
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public ObservableList<TelefonoCliente>getTelefonoCliente(){
        ArrayList<TelefonoCliente> lista = new ArrayList<TelefonoCliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonoCliente}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoCliente(resultado.getInt("codigoTelefonoCliente"), resultado.getString("numero"),
                                           resultado.getString("descripcion"), resultado.getInt("codigoCliente")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaTelefonoCliente = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblTelefonos.getSelectionModel().getSelectedItem() != null){
           cmbTelefono.getSelectionModel().select(buscarEmailCliente(((TelefonoCliente)tblTelefonos.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente()));
           cmbCliente.getSelectionModel().select(buscarEmailCliente(((TelefonoCliente)tblTelefonos.getSelectionModel().getSelectedItem()).getCodigoCliente()));
           txtNumero.setText(((TelefonoCliente)tblTelefonos.getSelectionModel().getSelectedItem()).getNumero());
           txtDescripcion.setText(((TelefonoCliente)tblTelefonos.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "¡Cuidado! No puede seleccionar casillas vacías","", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public TelefonoCliente buscarEmailCliente(int codigoTelefonoCliente){
        TelefonoCliente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoCliente(?)}");
            procedimiento.setInt(1, codigoTelefonoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TelefonoCliente(registro.getInt("codigoTelefonoCliente"), registro.getString("numero"), registro.getString("descripcion"),
                                             registro.getInt("codigoCliente"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultado;
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
        TelefonoCliente registro = new TelefonoCliente();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setNumero(txtNumero.getText());
        registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoCliente(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNumero());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.execute();
            listaTelefonoCliente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTelefonos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtNumero.setEditable(true);
                    
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoCliente(?,?,?)}");
            TelefonoCliente registro = (TelefonoCliente)tblTelefonos.getSelectionModel().getSelectedItem();
            registro.setCodigoTelefonoCliente(((TelefonoCliente)cmbTelefono.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setNumero(txtNumero.getText());
            procedimiento.setInt(1, registro.getCodigoTelefonoCliente());
            procedimiento.setString(2, registro.getNumero());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
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
                if(tblTelefonos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoCliente(?)}");
                            procedimiento.setInt(1, ((TelefonoCliente)tblTelefonos.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente());
                            procedimiento.execute();
                            listaTelefonoCliente.remove(tblTelefonos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria");
                    }
                }
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
        }
    }
    
    public void desactivarControles(){
        txtDescripcion.setEditable(false);
        txtNumero.setEditable(false);
        cmbTelefono.setEditable(false);
        cmbCliente.setDisable(true);
    }
    
    public void activarControles(){
        txtDescripcion.setEditable(true);
        txtNumero.setEditable(true);
        cmbTelefono.setEditable(false);
        cmbCliente.setDisable(false);
    }
    
    public void limpiarControles(){
        txtDescripcion.setText("");
        txtNumero.setText("");
        cmbTelefono.setValue("");
        cmbCliente.setValue("");
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
