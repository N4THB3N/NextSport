package org.natancastaneda.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.natancastaneda.sistema.Principal;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import org.natancastaneda.bean.Categoria;
import org.natancastaneda.bean.Marca;
import org.natancastaneda.bean.Producto;
import org.natancastaneda.bean.Talla;
import org.natancastaneda.db.Conexion;
import javafx.fxml.Initializable;
import org.natancastaneda.report.GenerarReporte;
import org.natancastaneda.sistema.Principal;

public class ProductoController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<Marca> listaMarca;
    private ObservableList<Categoria> listaCategoria;
    private ObservableList<Talla> listaTalla;
    private ObservableList<Producto> listaProducto;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    @FXML private TextField txtImagen;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox cmbCodigoPro;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colPreDocena;
    @FXML private TableColumn colPreUnitario;
    @FXML private TableColumn colPreMayor;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnImage;
    @FXML private ComboBox cmbCodTalla;
    @FXML private ComboBox cmbCodigo;
    @FXML private ComboBox cmbCodCategoria;
    @FXML private ImageView imgProducto;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodCategoria.setItems(getCategorias());
        cmbCodigo.setItems(getMarcas());
        cmbCodTalla.setItems(getTallas());
    }

       public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colExistencia.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("existencia"));
        colPreDocena.setCellValueFactory(new PropertyValueFactory<Producto,Double>("precioDocena"));
        colPreUnitario.setCellValueFactory(new PropertyValueFactory<Producto,Double>("precioUnitario"));
        colPreMayor.setCellValueFactory(new PropertyValueFactory<Producto,Double>("precioMayor"));
    }
     
    public void ventanaPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public ObservableList<Marca>getMarcas(){
        ArrayList<Marca> lista = new ArrayList<Marca>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMarcas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Marca(resultado.getInt("codigoMarca"),resultado.getString("descripcion")));
            }
        }catch(Exception e){
                    e.printStackTrace();
                    }
            return listaMarca = FXCollections.observableArrayList(lista);    
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

    public ObservableList<Categoria>getCategorias(){
        ArrayList<Categoria> lista = new ArrayList<Categoria>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCategoria}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Categoria(resultado.getInt("codigoCategoria"), resultado.getString("descripcion")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaCategoria = FXCollections.observableArrayList(lista);
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
        
     public void seleccionarElemento(){
         if(tblProductos.getSelectionModel().getSelectedItem() != null){
        cmbCodigoPro.setValue(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
        cmbCodCategoria.setValue(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCategoria());
        cmbCodigo.setValue(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getMarca());
        cmbCodTalla.setValue(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getTalla());
        txtDescripcion.setText((((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDescripcion()));
        txtImagen.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getImagen());
         }else{
            JOptionPane.showMessageDialog(null, "¡Cuidado! No puede seleccionar casillas vacías","", JOptionPane.ERROR_MESSAGE);
         }
    }    
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"), registro.getInt("existencia"), 
                                            registro.getDouble("precioUnitario"), registro.getDouble("precioDocena"), 
                                            registro.getString("imagen"), registro.getInt("codigoCategoria"), 
                                            registro.getString("categoria"), registro.getInt("codigoMarca"), 
                                            registro.getString("marca"), registro.getInt("codigoTalla"), 
                                            registro.getString("talla"), 
                                            registro.getString("descripcion"), registro.getInt("precioMayor"));
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
                if(tblProductos.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta==JOptionPane.YES_NO_OPTION)
                    {
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                            procedimiento.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
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
        Producto registro = new Producto();
        registro.setCodigoCategoria(((Categoria)cmbCodCategoria.getSelectionModel().getSelectedItem()).getCodigoCategoria());
        registro.setCodigoMarca(((Marca)cmbCodigo.getSelectionModel().getSelectedItem()).getCodigoMarca());
        registro.setCodigoTalla(((Talla)cmbCodTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
        registro.setImagen(txtImagen.getText());
        registro.setDescripcion(txtDescripcion.getText());
        try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProductos(?,?,?,?,?)}");
                procedimiento.setInt(1, registro.getCodigoCategoria());
                procedimiento.setInt(2, registro.getCodigoMarca());
                procedimiento.setInt(3, registro.getCodigoTalla());
                procedimiento.setString(4, registro.getImagen());
                procedimiento.setString(5, registro.getDescripcion());
                procedimiento.execute();
                listaProducto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem()!= null)
                {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtImagen.setEditable(false);
                    activarControles();
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProductos(?,?)}");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
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
                txtDescripcion.setEditable(false);
                cmbCodCategoria.setDisable(true);
                cmbCodigo.setDisable(true);
                cmbCodTalla.setDisable(true);
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put(" NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteProducto.jasper", "Reporte de Productos", parametros);
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
        txtDescripcion.setDisable(true);
        cmbCodCategoria.setDisable(true);
        cmbCodigo.setDisable(true);
        cmbCodTalla.setDisable(true);
        txtImagen.setDisable(true);
    }
    public void activarControles(){
        txtDescripcion.setDisable(false);
        cmbCodCategoria.setDisable(false);
        cmbCodigo.setDisable(false);
        cmbCodTalla.setDisable(false);
        txtImagen.setDisable(false);
    }
    public void limpiarControles(){
        txtDescripcion.setText("");
        cmbCodigo.setValue("");
        txtImagen.setText("");
        cmbCodigoPro.setValue("");
        cmbCodCategoria.setValue("");
        cmbCodTalla.setValue("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
