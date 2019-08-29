
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
import org.natancastaneda.bean.EmailProveedor;
import org.natancastaneda.bean.Proveedor;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;


public class EmailProveedorController implements Initializable{
    private Principal escenarioPrincipal;
    @FXML private ComboBox cmbCodigo;
    @FXML private ComboBox cmbProveedor;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtEmail;
    @FXML private TableView tblEmails;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colEmail;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    private ObservableList<EmailProveedor> listaEmailProveedor;
    private ObservableList<Proveedor> listaProveedor;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbProveedor.setItems(getProveedores());
    }

    public void cargarDatos(){
            tblEmails.setItems(getEmailProveedores());
            colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailProveedor,String>("descripcion"));
            colEmail.setCellValueFactory(new PropertyValueFactory<EmailProveedor,String>("email")); 
    }
    
    public void ventanaProveedores(){
        escenarioPrincipal.ventanaProveedores();
    }
    
    public ObservableList<Proveedor>getProveedores(){
    ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedor(resultado.getInt("codigoProveedor"),
                                                        resultado.getString("paginaWeb"),
                                                        resultado.getString("contactoPrincipal"),
                                                        resultado.getString("razonSocial"),
                                                        resultado.getString("nit"),
                                                        resultado.getString("direccion")));
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaProveedor = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<EmailProveedor> getEmailProveedores(){
        ArrayList<EmailProveedor> lista = new ArrayList<EmailProveedor>(); 
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmailProveedor}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new EmailProveedor(resultado.getInt("codigoEmailProveedor"), resultado.getInt("codigoProveedor"),
                                             resultado.getString("contactoPrincipal"), resultado.getString("descripcion"),
                                             resultado.getString("email")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaEmailProveedor = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Agregar");
                btnEliminar.setText("Cancelar");
                tipoDeOperacion = operaciones.GUARDAR;
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
            break;
            
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                tipoDeOperacion = operaciones.NINGUNO;
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                break;
        }
    }
    
    public void agregar(){
            EmailProveedor registro = new EmailProveedor();
            registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setEmail(txtEmail.getText());
        try{   
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEmailProveedor(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getEmail());
            procedimiento.execute();
            listaEmailProveedor.add(registro);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        if(tblEmails.getSelectionModel().getSelectedItem() != null){
            cmbCodigo.setValue(((EmailProveedor)tblEmails.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
            cmbProveedor.setValue(((EmailProveedor)tblEmails.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            txtEmail.setText(((EmailProveedor)tblEmails.getSelectionModel().getSelectedItem()).getEmail());
            txtDescripcion.setText(((EmailProveedor)tblEmails.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "No seleccione casillas vacias", "", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailProveedor(?,?)}");
            EmailProveedor registro = (EmailProveedor)tblEmails.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoEmailProveedor(((EmailProveedor)tblEmails.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getCodigoEmailProveedor());
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
                if(tblEmails.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta==JOptionPane.YES_NO_OPTION)
                    {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailProveedor(?)}");
                            procedimiento.setInt(1, ((EmailProveedor)tblEmails.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
                            procedimiento.execute();
                            listaEmailProveedor.remove(tblEmails.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
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
                break;
        }
    }
    public void editar(){
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                if(tblEmails.getSelectionModel().getSelectedItem()!= null)
                {
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
    
    public void activarControles(){
        cmbProveedor.setDisable(false);
        cmbCodigo.setDisable(false);
        txtEmail.setEditable(true);
        txtDescripcion.setEditable(true);
    }
    
    public void desactivarControles(){
        cmbProveedor.setDisable(true);
        cmbCodigo.setDisable(true);
        txtEmail.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void limpiarControles(){
        cmbProveedor.setValue("");
        cmbCodigo.setValue("");
        txtEmail.setText("");
        txtDescripcion.setText("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
