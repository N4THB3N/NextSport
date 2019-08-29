
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
import javax.swing.JOptionPane;
import javafx.scene.control.cell.PropertyValueFactory;
import org.natancastaneda.bean.DetalleFactura;
import org.natancastaneda.bean.Factura;
import org.natancastaneda.bean.Producto;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;


public class DetalleFacturaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, ACTUALIZAR, GUARDAR, NINGUNO, CANCELAR, ELIMINAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleFactura> listaDetalleFactura;
    private ObservableList<Factura> listaFactura;
    private ObservableList<Producto> listaProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox cmbDetalle;
    @FXML private ComboBox cmbProducto;
    @FXML private ComboBox cmbFactura;
    @FXML private TableView tblDetalles;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colPrecio;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbProducto.setItems(getProductos());
        cmbFactura.setItems(getFacturas());
    }

    public void cargarDatos(){
            tblDetalles.setItems(getDetalleFactura());
            colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleFactura,Integer>("cantidad"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<DetalleFactura,Double>("precio"));
    }
    
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public ObservableList<Factura>getFacturas(){
        ArrayList<Factura> lista = new ArrayList<Factura>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarFacturas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Factura(resultado.getInt("numeroFactura"),
                                      resultado.getString("estado"),
                                      resultado.getDate("fecha"),
                                      resultado.getString("nit"),
                                      resultado.getDouble("total"),
                                      resultado.getInt("codigoCliente")));
          }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaFactura = FXCollections.observableArrayList(lista);
    }
    
       public ObservableList<Producto>getProductos(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"), 
                                        resultado.getInt("existencia"), 
                                        resultado.getDouble("precioUnitario"), 
                                        resultado.getDouble("precioDocena"), 
                                        resultado.getString("imagen"), 
                                        resultado.getInt("codigoCategoria"), 
                                        resultado.getString("categoria"), 
                                        resultado.getInt("codigoMarca"), 
                                        resultado.getString("marca"), 
                                        resultado.getInt("codigoTalla"), 
                                        resultado.getString("talla"), 
                                        resultado.getString("descripcion"), 
                                        resultado.getInt("precioMayor")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
       
    public ObservableList<DetalleFactura>getDetalleFactura(){
    ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarDetalleFactura}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new DetalleFactura(resultado.getInt("codigoDetalleFactura"),
                                         resultado.getInt("cantidad"),
                                         resultado.getInt("numeroFactura"),
                                         resultado.getDouble("precio"),
                                         resultado.getInt("codigoProducto")));
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
    return listaDetalleFactura = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblDetalles.getSelectionModel().getSelectedItem() != null){
            cmbDetalle.setValue(((DetalleFactura)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
            cmbFactura.setValue(((DetalleFactura)tblDetalles.getSelectionModel().getSelectedItem()).getNumeroFactura());
            cmbProducto.setValue(((DetalleFactura)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoProducto());  
        }
        else{
            JOptionPane.showMessageDialog(null, "Por favor, no seleccione casillas en blanco.","",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public DetalleFactura buscarDetalleFactura(int codigoDetalleFactura){
        DetalleFactura resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarDetalleFactura(?)}");
            procedimiento.setInt(1, codigoDetalleFactura);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new DetalleFactura(registro.getInt("codigoDetalleFactura"),
                                         registro.getInt("cantidad"),
                                         registro.getInt("numeroDocumento"),
                                         registro.getDouble("precio"),
                                         registro.getInt("codigoProducto"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultado;
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
        DetalleFactura registro = new DetalleFactura();
        registro.setCodigoProducto(((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setNumeroFactura(((Factura)cmbFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setPrecio(Double.parseDouble(txtPrecio.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleFactura(?,?,?,?)}");
                procedimiento.setInt(1, registro.getNumeroFactura());
                procedimiento.setInt(2, registro.getCodigoProducto());
                procedimiento.setInt(3, registro.getCantidad());
                procedimiento.setDouble(4, registro.getPrecio());
                procedimiento.execute();
                listaDetalleFactura.add(registro);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
     
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblDetalles.getSelectionModel().getSelectedItem() != null){
                txtCantidad.setEditable(true);
                txtPrecio.setEditable(false);
                cmbDetalle.setDisable(false);
                cmbFactura.setDisable(false);
                cmbProducto.setDisable(false);
                tipoDeOperacion = operaciones.ACTUALIZAR;
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
            
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione una categoria primero");
                }
            break;
        
            case ACTUALIZAR:
                actualizar();
                btnReporte.setText("Editar");
                btnEditar.setText("Editar");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                limpiarControles();
                desactivarControles();
                cargarDatos();
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarDetalleFactura(?,?)}");
            DetalleFactura registro = (DetalleFactura)tblDetalles.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleFactura(((DetalleFactura)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setInt(2, registro.getCodigoDetalleFactura());
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
                if(tblDetalles.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleFactura(?)}");
                            procedimiento.setInt(1, ((DetalleFactura)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                            procedimiento.execute();
                            listaDetalleFactura.remove(tblDetalles.getSelectionModel().getSelectedIndex());
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
    public void desactivarControles(){
        txtCantidad.setEditable(true);
        cmbDetalle.setDisable(true);
        cmbFactura.setDisable(true);
        cmbProducto.setDisable(true);
        txtPrecio.setEditable(true);
    }
    public void activarControles(){
        txtCantidad.setEditable(true);
        cmbDetalle.setDisable(true);
        cmbFactura.setDisable(false);
        cmbProducto.setDisable(false);
        txtPrecio.setEditable(true);
    }
    public void limpiarControles(){
        txtCantidad.setText("");
        cmbDetalle.setValue("");
        cmbFactura.setValue("");
        cmbProducto.setValue("");
        txtPrecio.setText("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
