
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
import org.natancastaneda.bean.Proveedor;
import org.natancastaneda.bean.TelefonoProveedor;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;


public class TelefonoProveedorController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList <TelefonoProveedor> listaTelefonoProveedor;
    private ObservableList <Proveedor> listaProveedor;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    @FXML private ComboBox cmbProveedor;
    @FXML private ComboBox cmbCodigo;
    @FXML private TextField txtNumero;
    @FXML private TextField txtDescripcion;
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
       cmbProveedor.setItems(getProveedores());
    }

    public void ventanaProveedores(){
        escenarioPrincipal.ventanaProveedores();
    }
    
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public ObservableList<TelefonoProveedor> getTelefonoProveedor(){
        ArrayList <TelefonoProveedor> lista = new ArrayList<TelefonoProveedor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonoProveedor}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoProveedor(resultado.getInt("codigoProveedor"),
                                                resultado.getInt("codigoTelefonoProveedor"),
                                                resultado.getString("numero"), 
                                                resultado.getString("descripcion")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaTelefonoProveedor = FXCollections.observableArrayList(lista);
    } 
    
    public void cargarDatos(){
        tblTelefonos.setItems(getTelefonoProveedor());
        colNumero.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor,String>("numero"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor,String>("descripcion"));
    }
    
    public void seleccionarElemento(){
        if(tblTelefonos.getSelectionModel().getSelectedItem() != null){
            cmbProveedor.setValue(((TelefonoProveedor)tblTelefonos.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            cmbCodigo.setValue(((TelefonoProveedor)tblTelefonos.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
            txtNumero.setText(((TelefonoProveedor)tblTelefonos.getSelectionModel().getSelectedItem()).getNumero());
            txtDescripcion.setText(((TelefonoProveedor)tblTelefonos.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "No puede seleccionar casillas vacias");
        }
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
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                tipoDeOperacion = operaciones.GUARDAR;
                btnReporte.setDisable(true);
                btnEditar.setDisable(true);
            break;
            
            case GUARDAR:
                agregar();
                activarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                tipoDeOperacion = operaciones.NINGUNO;
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                cargarDatos();
            break;
        }
    }
    
    public void agregar(){
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setNumero(txtNumero.getText());
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoProveedor(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNumero());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.execute();
            listaTelefonoProveedor.add(registro);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTelefonos.getSelectionModel().getSelectedItem() != null){
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                tipoDeOperacion = operaciones.ACTUALIZAR;
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un registro");
                }
            break;
            
            case ACTUALIZAR:
                actualizar();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                desactivarControles();
                limpiarControles();
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoProveedor(?,?)}");
            TelefonoProveedor registro = (TelefonoProveedor)tblTelefonos.getSelectionModel().getSelectedItem();
            registro.setCodigoTelefonoProveedor(((TelefonoProveedor)tblTelefonos.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
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
                if(tblTelefonos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Telefono Proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta==JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoProveedor(?)}");
                            procedimiento.setInt(1, (((TelefonoProveedor)tblTelefonos.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
                            procedimiento.execute();
                            limpiarControles();
                            cargarDatos();
                            
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
    
    public void limpiarControles(){
        txtDescripcion.setText("");
        txtNumero.setText("");
        cmbProveedor.setValue("");
        cmbCodigo.setValue("");
    }
    
    public void activarControles(){
        txtDescripcion.setDisable(false);
        txtNumero.setDisable(false);
        cmbProveedor.setDisable(false);
        cmbCodigo.setDisable(false);
    }
    
    public void desactivarControles(){
        txtDescripcion.setDisable(true);
        txtNumero.setDisable(true);
        cmbProveedor.setDisable(true);
        cmbCodigo.setDisable(true);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
