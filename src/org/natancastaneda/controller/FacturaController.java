
package org.natancastaneda.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.natancastaneda.bean.Cliente;
import org.natancastaneda.bean.Factura;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.report.GenerarReporte;
import org.natancastaneda.sistema.Principal;


public class FacturaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, NINGUNO, CANCELAR, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Factura> listaFactura;
    private ObservableList<Cliente> listaCliente;
    private DatePicker fecha;
    @FXML private TableView tblFacturas;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colTotal;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCliente;
    @FXML private ComboBox cmbNumero;
    @FXML private TextField txtNit;
    @FXML private TextField txtEstado;
    @FXML private TextField txtFecha;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCliente.setItems(getClientes());
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/natancastaneda/resource/DatePicker.css");
        grpFecha.add(fecha, 0, 0);
    }

    public void cargarDatos(){
        tblFacturas.setItems(getFacturas());
        colEstado.setCellValueFactory(new PropertyValueFactory<Factura,String>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Factura,Date>("fecha"));
        colNit.setCellValueFactory(new PropertyValueFactory<Factura,String>("nit"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura,Double>("total"));
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
                btnNuevo.setText("Agregar");
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
        Factura registro = new Factura();
        registro.setEstado(txtEstado.getText());
        registro.setFecha(fecha.getSelectedDate());
        registro.setNit(txtNit.getText());
        registro.setCodigoCliente(((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarFactura(?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getEstado());
            procedimiento.setDate(3, new java.sql.Date(registro.getFecha().getTime()));
            procedimiento.setString(4, registro.getNit());
            procedimiento.execute();
            listaFactura.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
  public void seleccionarElemento(){
    if(tblFacturas.getSelectionModel().getSelectedItem() != null){
        cmbCliente.getSelectionModel().select(buscarFactura(((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        cmbNumero.getSelectionModel().select(buscarFactura(((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        txtNit.setText(((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getNit());
        txtEstado.setText(((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getEstado());
    }else{
        JOptionPane.showMessageDialog(null, "¡Cuidado! No puede seleccionar casillas vacías","", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public Factura buscarFactura(int numeroFactura){
    Factura resultado = null;
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarFactura(?)}");
        procedimiento.setInt(1, numeroFactura);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Factura(registro.getInt("numeroFactura"), registro.getString("estado"),
                                    registro.getDate("fecha"), registro.getString("nit"),
                                    registro.getDouble("total"), registro.getInt("codigoCliente"));
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
              if(tblFacturas.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta== JOptionPane.YES_NO_OPTION){
                      try{
                      PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarFactura(?)}");
                      procedimiento.setInt(1, ((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getNumeroFactura());
                      procedimiento.execute();
                      listaFactura.remove(tblFacturas.getSelectionModel().getSelectedIndex());
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
  
  public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblFacturas.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarFactura(?,?)}");
            Factura registro = (Factura)tblFacturas.getSelectionModel().getSelectedItem();
            registro.setNumeroFactura(((Factura)cmbNumero.getSelectionModel().getSelectedItem()).getNumeroFactura());
            registro.setEstado(txtEstado.getText());
            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.setString(2, registro.getEstado());
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
                desactivarControles();
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put(" NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteFactura.jasper", "Reporte de Facturas", parametros);
    }
    
    public void reporte(){
        desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
    }
    
    public void detalleFactura(){
        if(tblFacturas.getSelectionModel().getSelectedItem() != null){
            escenarioPrincipal.ventanaDetalleFactura((Factura)tblFacturas.getSelectionModel().getSelectedItem());
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
        }
    }
    
    public void desactivarControles(){
        txtNit.setEditable(false);
        txtEstado.setEditable(true);
        cmbNumero.setDisable(true);
        cmbCliente.setDisable(true);
        grpFecha.setDisable(true);
    }
    
    public void activarControles(){
        txtNit.setEditable(true);
        txtEstado.setEditable(true);
        cmbNumero.setDisable(false);
        cmbCliente.setDisable(false);
        grpFecha.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNit.setText("");
        txtEstado.setText("");
        cmbNumero.setValue("");
        cmbCliente.setValue("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
