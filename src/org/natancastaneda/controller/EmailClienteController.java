
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
import org.natancastaneda.bean.EmailCliente;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;

public class EmailClienteController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<EmailCliente> listaEmailCliente;
    private ObservableList<Cliente> listaCliente;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtEmail;
    @FXML private ComboBox cmbEmail;
    @FXML private ComboBox cmbCliente;
    @FXML private TableView tblEmails;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colEmail;
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
        tblEmails.setItems(getEmailClientes());
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailCliente,String>("descripcion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EmailCliente,String>("email"));
    }
    
    public ObservableList<EmailCliente>getEmailClientes(){
        ArrayList<EmailCliente> lista = new ArrayList<EmailCliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmailCliente}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new EmailCliente(resultado.getInt("codigoEmailCliente"), resultado.getString("descripcion"),
                                           resultado.getString("email"), resultado.getInt("codigoCliente")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaEmailCliente = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblEmails.getSelectionModel().getSelectedItem() != null){
           cmbEmail.getSelectionModel().select(buscarEmailCliente(((EmailCliente)tblEmails.getSelectionModel().getSelectedItem()).getCodigoEmailCliente()));
           cmbCliente.getSelectionModel().select(buscarEmailCliente(((EmailCliente)tblEmails.getSelectionModel().getSelectedItem()).getCodigoCliente()));
           txtEmail.setText(((EmailCliente)tblEmails.getSelectionModel().getSelectedItem()).getEmail());
           txtDescripcion.setText(((EmailCliente)tblEmails.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "¡Cuidado! No puede seleccionar casillas vacías","", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public EmailCliente buscarEmailCliente(int codigoEmailCliente){
        EmailCliente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEmailCliente(?)}");
            procedimiento.setInt(1, codigoEmailCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new EmailCliente(registro.getInt("codigoEmailCliente"), registro.getString("descripcion"), registro.getString("email"),
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
        EmailCliente registro = new EmailCliente();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEmailCliente(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getEmail());
            procedimiento.execute();
            listaEmailCliente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblEmails.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtEmail.setEditable(false);
                    
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailCliente(?,?)}");
            EmailCliente registro = (EmailCliente)tblEmails.getSelectionModel().getSelectedItem();
            registro.setCodigoEmailCliente(((EmailCliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoEmailCliente());
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoEmailCliente());
            procedimiento.setString(2, registro.getDescripcion());
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
                if(tblEmails.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailCliente(?)}");
                            procedimiento.setInt(1, ((EmailCliente)tblEmails.getSelectionModel().getSelectedItem()).getCodigoEmailCliente());
                            procedimiento.execute();
                            listaEmailCliente.remove(tblEmails.getSelectionModel().getSelectedIndex());
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
        txtEmail.setEditable(false);
        cmbEmail.setEditable(false);
        cmbCliente.setDisable(true);
    }
    
    public void activarControles(){
        txtDescripcion.setEditable(true);
        txtEmail.setEditable(true);
        cmbEmail.setEditable(false);
        cmbCliente.setDisable(false);
    }
    
    public void limpiarControles(){
        txtDescripcion.setText("");
        txtEmail.setText("");
        cmbEmail.setValue("");
        cmbCliente.setValue("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
