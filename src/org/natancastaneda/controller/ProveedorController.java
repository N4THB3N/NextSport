
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
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;


public class ProveedorController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<Proveedor> listaProveedor;
    public enum operaciones{NUEVO, GUARDAR, ELIMINAR, NINGUNO, ACTUALIZAR, CANCELAR, EDITAR}
    public operaciones tipoDeOperacion = operaciones.NINGUNO;
    @FXML private TableView tblProveedores;
    @FXML private TableColumn colCodProveedor;
    @FXML private TableColumn colPaginaWeb;
    @FXML private TableColumn colContactoPrincipal;
    @FXML private TableColumn colRazonSocial;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colDireccion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ComboBox cmbProveedor;
    @FXML private TextField txtPagina;
    @FXML private TextField txtContacto;
    @FXML private TextField txtRazon;
    @FXML private TextField txtNit;
    @FXML private TextField txtDireccion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            cargarDatos();
    }
    
    public void cargarDatos(){
        tblProveedores.setItems(getProveedores());
        colCodProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor,Integer>("codigoProveedor"));
        colPaginaWeb.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("paginaWeb"));
        colContactoPrincipal.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("contactoPrincipal"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("razonSocial"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("nit"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor,String>("direccion"));
    }
    
    public void ventanaTelefonoProveedor(){
        escenarioPrincipal.ventanaTelefonoProveedor();
    }
    
    public void ventanaEmailProveedor(){
        escenarioPrincipal.ventanaEmailProveedor();
    }
    
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
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
    
    public void seleccionarElemento(){
        if(tblProveedores.getSelectionModel().getSelectedItem() != null){
            cmbProveedor.getSelectionModel().select(buscarProveedor(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txtPagina.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());
            txtContacto.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
            txtRazon.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
            txtNit.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getNit());
            txtDireccion.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
        }
    }
    
     public Proveedor buscarProveedor(int codigoProveedor){
        Proveedor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarProveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
                ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Proveedor(registro.getInt("codigoProveedor"),
                                            registro.getString("paginaWeb"), 
                                            registro.getString("contactoPrincipal"),
                                            registro.getString("razonSocial"),
                                            registro.getString("nit"),
                                            registro.getString("direccion"));               
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
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?");
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProveedor(?)}");
                            procedimiento.setInt(1, ((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedor.remove(tblProveedores.getSelectionModel().getSelectedIndex());
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
            Proveedor registro = new Proveedor();
            registro.setPaginaWeb(txtPagina.getText());
            registro.setContactoPrincipal(txtContacto.getText());
            registro.setRazonSocial(txtRazon.getText());
            registro.setNit(txtNit.getText());
            registro.setDireccion(txtDireccion.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProveedor(?,?,?,?,?)}");
                procedimiento.setString(1, registro.getPaginaWeb());
                procedimiento.setString(2, registro.getContactoPrincipal());
                procedimiento.setString(3, registro.getRazonSocial());
                procedimiento.setString(4, registro.getNit());
                procedimiento.setString(5, registro.getDireccion());
                procedimiento.execute();
                listaProveedor.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        txtDireccion.setEditable(false);
                        txtNit.setEditable(true);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProveedor(?,?)}");
            Proveedor registro = (Proveedor)tblProveedores.getSelectionModel().getSelectedItem();
            registro.setNit(txtNit.getText());
            registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNit());
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
        txtContacto.setEditable(false);
        txtNit.setEditable(false);
        txtPagina.setEditable(false);
        cmbProveedor.setDisable(true);
        txtDireccion.setEditable(false);
        txtRazon.setEditable(false);
    }
    
    public void activarControles(){
        txtContacto.setEditable(true);
        txtNit.setEditable(true);
        txtPagina.setEditable(true);
        cmbProveedor.setDisable(true);
        txtDireccion.setEditable(true);
        txtRazon.setEditable(true);
    }
    
    public void limpiarControles(){
        txtContacto.setText("");
        txtNit.setText("");
        txtPagina.setText("");
        cmbProveedor.setValue("");
        txtDireccion.setText("");
        txtRazon.setText("");
    }
    
     public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}

