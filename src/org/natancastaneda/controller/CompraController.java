
package org.natancastaneda.controller;

import java.util.ArrayList;
import java.util.Date;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.cell.PropertyValueFactory;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Label;
import javax.swing.JOptionPane;
import org.natancastaneda.bean.Compra;
import org.natancastaneda.bean.Proveedor;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.report.GenerarReporte;
import org.natancastaneda.sistema.Principal;

public class CompraController implements Initializable {
    private enum operaciones {NUEVO,GUARDAR,EDITAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Compra>listaCompra;
    private ObservableList<Proveedor>listaProveedor;
    private DatePicker fecha;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbProveedor;
    @FXML private ComboBox cmbNumero;
    @FXML private TableView tblCompras;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtDescripcion;
    @FXML private TableColumn colNumeroDocumento;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colTotal;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       cmbProveedor.setItems(getProveedores());
       fecha = new DatePicker(Locale.ENGLISH);
       fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
       fecha.getCalendarView().todayButtonTextProperty().set("Today");
       fecha.getCalendarView().setShowWeeks(false);
       fecha.getStylesheets().add("/org/natancastaneda/resource/DatePicker.css");
       grpFecha.add(fecha, 0, 0);
       
    }
    
    public void cargarDatos(){
        tblCompras.setItems(getCompras());
        colTotal.setCellValueFactory(new PropertyValueFactory<Compra, Double>("total"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Compra, String>("direccion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Compra, Date>("fecha"));
    }
    
    public void seleccionarElemento(){
        if(tblCompras.getSelectionModel().getSelectedItem() != null){
            cmbNumero.setValue(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            cmbProveedor.setValue(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            txtDireccion.setText(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getDireccion());
            txtDescripcion.setText(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        }else{
            JOptionPane.showMessageDialog(null, "No seleccione casillas vacias", "", JOptionPane.ERROR_MESSAGE);
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
                if(tblCompras.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta==JOptionPane.YES_NO_OPTION)
                    {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCompra(?)}");
                            procedimiento.setInt(1, ((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompra.remove(tblCompras.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                if(tblCompras.getSelectionModel().getSelectedItem()!= null)
                {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtDireccion.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCompra(?,?)}");
            Compra registro = (Compra)tblCompras.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            registro.setNumeroDocumento(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:              
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                guardar();
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        try{
            Compra nuevaCompra = new Compra();
            nuevaCompra.setFecha(fecha.getSelectedDate());
            nuevaCompra.setDescripcion(txtDescripcion.getText());
            Proveedor proveedor =(Proveedor) cmbProveedor.getSelectionModel().getSelectedItem();
            nuevaCompra.setCodigoProveedor(proveedor.getCodigoProveedor());
            nuevaCompra.setContactoPrincipal(proveedor.getContactoPrincipal());
            nuevaCompra.setDireccion(proveedor.getDireccion());
            nuevaCompra.setNit(proveedor.getNit());
            nuevaCompra.setPaginaWeb(proveedor.getPaginaWeb());
            nuevaCompra.setRazonSocial(proveedor.getRazonSocial());
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCompra(?,?,?)}");
            procedimiento.setInt(1, nuevaCompra.getCodigoProveedor());
            procedimiento.setDate(2, new java.sql.Date(nuevaCompra.getFecha().getTime()));
            procedimiento.setString(3, nuevaCompra.getDescripcion());
            procedimiento.execute();
            listaCompra.add(nuevaCompra);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void generarReporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(true);
                desactivarControles();
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put(" NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteCompra.jasper", "Reporte de Compras", parametros);
    }
    
    public void limpiarControles(){
        txtDescripcion.setText("");
        cmbProveedor.setValue("");
        cmbNumero.setValue("");
        txtDireccion.setText("");
    }
    
    public void activarControles(){
        txtDescripcion.setEditable(true);
        cmbProveedor.setDisable(false);
        cmbNumero.setDisable(false);
        txtDireccion.setEditable(true);
        grpFecha.setDisable(false);
    }
    
    public void desactivarControles(){
        txtDescripcion.setEditable(false);
        cmbProveedor.setDisable(true);
        cmbNumero.setDisable(true);
        txtDireccion.setEditable(false);
        grpFecha.setDisable(true);
    }
    
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void detalleCompra(){
        if(tblCompras.getSelectionModel().getSelectedItem()!= null){
            escenarioPrincipal.ventanaDetalleCompra((Compra)tblCompras.getSelectionModel().getSelectedItem());
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar una compra");
        }
    }
    
}
