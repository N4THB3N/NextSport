
package org.natancastaneda.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.natancastaneda.bean.Compra;
import org.natancastaneda.bean.DetalleCompra;
import org.natancastaneda.bean.Producto;
import org.natancastaneda.bean.Proveedor;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;

public class DetalleCompraController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;
    private ObservableList<Compra> listaCompra;
    private ObservableList<Proveedor> listaProveedor;
    private ObservableList<DetalleCompra> listaDetalleCompra;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtCostoU;
    @FXML private ComboBox cmbProducto;
    @FXML private TableView tblDetalles;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNumero;
    @FXML private TableColumn colSubTotal;
    @FXML private TableColumn colCostoU;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ComboBox cmbProveedor;
    @FXML private ComboBox cmbNumero;
    @FXML private ComboBox cmbCodigo;
    @FXML private Label lblTotal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbProducto.setItems(getProductos());
        cmbProveedor.setItems(getProveedores());
        cmbNumero.setItems(getCompras());
    }

    public void cargarDatos(){
        
        tblDetalles.setItems(getDetalleCompras());
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra,Integer>("cantidad"));
        colNumero.setCellValueFactory(new PropertyValueFactory<DetalleCompra,Integer>("numeroDocumento"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<DetalleCompra,Integer>("subtotal"));
        colCostoU.setCellValueFactory(new PropertyValueFactory<DetalleCompra,Integer>("costoUnitario"));
    }
    
    public void ventanaCompra(){
        escenarioPrincipal.ventanaCompras();
    }
    
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public ObservableList<DetalleCompra>getDetalleCompras(){
    ArrayList<DetalleCompra> lista = new ArrayList<DetalleCompra>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarDetalleCompras}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new DetalleCompra(resultado.getInt("codigoProveedor"),
                                        resultado.getInt("codigoProducto"),
                                        resultado.getInt("cantidad"),
                                        resultado.getInt("codigoDetalleCompra"),
                                        resultado.getInt("numeroDocumento"),
                                        resultado.getDouble("subtotal"),
                                        resultado.getDouble("costoUnitario")));
        }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaDetalleCompra = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Compra>getCompras(){
        ArrayList<Compra> lista = new ArrayList<Compra>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCompras}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Compra(resultado.getInt("numeroDocumento"),
                                     resultado.getInt("codigoProveedor"),
                                     resultado.getDouble("total"),
                                     resultado.getDate("fecha"),
                                     resultado.getString("descripcion"),
                                     resultado.getString("paginaWeb"),
                                     resultado.getString("contactoPrincipal"),
                                     resultado.getString("razonSocial"),
                                     resultado.getString("nit"),
                                     resultado.getString("direccion")));
        }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaCompra = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblDetalles.getSelectionModel().getSelectedItem() != null){
            cmbNumero.setValue(((DetalleCompra)tblDetalles.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            cmbCodigo.setValue(((DetalleCompra)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
            cmbProducto.setValue(((DetalleCompra)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoProducto());
            cmbProveedor.setValue(((DetalleCompra)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        }else{
            JOptionPane.showMessageDialog(null, "Por favor, no seleccione casillas en blanco.","",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public DetalleCompra buscarCodigoDetalleCompra(int codigoDetalleCompra){
        DetalleCompra resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarDetalleCompra(?)}");
            procedimiento.setInt(1, codigoDetalleCompra);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new DetalleCompra(registro.getInt("codigoProveedor"),
                                        registro.getInt("codigoProducto"),
                                        registro.getInt("cantidad"),
                                        registro.getInt("codigoDetalleCompra"),
                                        registro.getInt("numeroDocumento"),
                                        registro.getDouble("subtotal"),
                                        registro.getDouble("costoUnitario"));
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
                if(tblDetalles.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Detalle Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta==JOptionPane.YES_NO_OPTION)
                    {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleCompra(?)}");
                            procedimiento.setInt(1,(((DetalleCompra)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
                            procedimiento.execute();
                            listaDetalleCompra.remove(tblDetalles.getSelectionModel().getSelectedIndex());
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    
    public void agregar(){
        DetalleCompra registro = new DetalleCompra();
        registro.setCodigoProducto(((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setNumeroDocumento(((Compra)cmbNumero.getSelectionModel().getSelectedItem()).getNumeroDocumento());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setCostoUnitario(Double.parseDouble(txtCostoU.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleCompra(?,?,?,?,?)}");
                procedimiento.setInt(1, registro.getCodigoProducto());
                procedimiento.setInt(2, registro.getCodigoProveedor());
                procedimiento.setInt(3, registro.getCantidad());
                procedimiento.setInt(4, registro.getNumeroDocumento());
                procedimiento.setDouble(5, registro.getCostoUnitario());
                procedimiento.execute();
                listaDetalleCompra.add(registro);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                if(tblDetalles.getSelectionModel().getSelectedItem()!= null)
                {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtCantidad.setEditable(true);
                    txtCostoU.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarDetalleCompra(?,?)}");
            DetalleCompra registro = (DetalleCompra)tblDetalles.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleCompra(((DetalleCompra)tblDetalles.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCantidad.setEditable(false);
        cmbNumero.setDisable(true);
        cmbCodigo.setDisable(true);
        cmbProducto.setDisable(true);
        cmbProveedor.setDisable(true);
        txtCostoU.setEditable(false);
    }
    public void activarControles(){
        txtCantidad.setEditable(true);
        cmbNumero.setDisable(false);
        cmbCodigo.setDisable(true);
        cmbProducto.setDisable(false);
        cmbProveedor.setDisable(false);
        txtCostoU.setEditable(true);
    }
    public void limpiarControles(){
        txtCantidad.setText("");
        cmbNumero.setValue("");
        cmbCodigo.setValue("");
        cmbProducto.setValue("");
        cmbProveedor.setValue("");
        txtCostoU.setText("");
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}

