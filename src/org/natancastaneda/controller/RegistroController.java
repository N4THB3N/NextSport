
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
import org.natancastaneda.bean.Registro;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.sistema.Principal;


public class RegistroController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Registro> listaRegistro;
    @FXML private ComboBox cmbCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasena;
    @FXML private TextField txtTipo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private TableView tblRegistros;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colUsuario;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public ObservableList<Registro> getRegistros(){
        ArrayList<Registro> lista = new ArrayList<Registro>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Registro(resultado.getInt("codigoUsuario"), resultado.getString("nombreUsuario"), resultado.getString("email"), resultado.getString("usuario"), resultado.getString("contrasena"), resultado.getString("tipoUsuario")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listaRegistro = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblRegistros.setItems(getRegistros());
        colNombre.setCellValueFactory(new PropertyValueFactory<Registro, String>("nombreUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<Registro, String>("usuario"));
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                btnNuevo.setText("Agregar");
                btnEditar.setText("Cancelar");
                tipoDeOperacion = operaciones.GUARDAR;
                JOptionPane.showMessageDialog(null, "Coloque su usuario como \"admin\"\n" +
                                                    " si es administrador, o como \"user\" \n" +
                                                    "si es dependiente de tienda.\n" +
                                                    "\n" +
                                                    "\n" +
                                                    "Por favor, no olvide colocar su tipo\n" +
                                                    "de usuario en minúsculas");
                btnEliminar.setDisable(true);
                limpiarControles();
                activarControles();
            break;
            
            case GUARDAR:
                btnNuevo.setText("Nuevo");
                btnEditar.setText("Editar");
                tipoDeOperacion = operaciones.NINGUNO;
                btnEliminar.setDisable(false);
                agregar();
                cargarDatos();
                limpiarControles();
                desactivarControles();
        }        
    }
    
    public void agregar(){
        Registro registro = new Registro();
        registro.setNombreUsuario(txtNombre.getText());
        registro.setEmail(txtEmail.getText());
        registro.setUsuario(txtUsuario.getText());
        registro.setContrasena(txtContrasena.getText());
        registro.setTipoUsuario(txtTipo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getEmail());
            procedimiento.setString(3, registro.getUsuario());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.setString(5, registro.getTipoUsuario());
            procedimiento.execute();
            listaRegistro.add(registro);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        cmbCodigo.getSelectionModel().select(buscarRegistro(((Registro)tblRegistros.getSelectionModel().getSelectedItem()).getCodigoUsuario()));
        txtNombre.setText(((Registro)tblRegistros.getSelectionModel().getSelectedItem()).getNombreUsuario());
        txtEmail.setText(((Registro)tblRegistros.getSelectionModel().getSelectedItem()).getEmail());
        txtUsuario.setText(((Registro)tblRegistros.getSelectionModel().getSelectedItem()).getUsuario());
        txtContrasena.setText(((Registro)tblRegistros.getSelectionModel().getSelectedItem()).getContrasena());
        txtTipo.setText(((Registro)tblRegistros.getSelectionModel().getSelectedItem()).getTipoUsuario());
    }
    
    public Registro buscarRegistro(int codigoUsuario){
        Registro resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarUsuarios(?)}");
            procedimiento.setInt(1, codigoUsuario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Registro(registro.getInt("codigoUsuario"), registro.getString("nombreUsuario"), registro.getString("email"), 
                             registro.getString("usuario"), registro.getString("contrasena"), registro.getString("tipoUsuario"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblRegistros.getSelectionModel().getSelectedItem() != null){
                btnNuevo.setDisable(true);
                btnEditar.setText("Actualizar");
                tipoDeOperacion = operaciones.ACTUALIZAR;
                btnEliminar.setText("Cancelar");
                activarControles();
                }else{
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una categoria", "Error",JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case ACTUALIZAR:
                btnNuevo.setDisable(false);
                btnEditar.setText("Editar");
                tipoDeOperacion = operaciones.NINGUNO;
                btnEliminar.setText("Eliminar");
                actualizar();
                limpiarControles();
                desactivarControles();
                cargarDatos();
                break;
                
            case GUARDAR:
                btnNuevo.setText("Nuevo");
                btnEditar.setText("Editar");
                tipoDeOperacion = operaciones.NINGUNO;
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarUsuario(?,?)}");
            Registro resultado = (Registro)tblRegistros.getSelectionModel().getSelectedItem();
            resultado.setCodigoUsuario(((Registro)cmbCodigo.getSelectionModel().getSelectedItem()).getCodigoUsuario());
            resultado.setUsuario(txtUsuario.getText());
            procedimiento.setInt(1, resultado.getCodigoUsuario());
            procedimiento.setString(2, resultado.getUsuario());
            procedimiento.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.Login();
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                btnNuevo.setDisable(false);
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                activarControles();
            break;
            
            default:
                try{
                if(tblRegistros.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el registro?","Eliminar el registro",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta==JOptionPane.YES_NO_OPTION){
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuario(?)}");
                        procedimiento.setInt(1, ((Registro)tblRegistros.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                        procedimiento.execute();
                        listaRegistro.remove(tblRegistros.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                        desactivarControles();
                        cargarDatos();
                        
                    }
        }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un registro, por favor","Error eliminar",JOptionPane.ERROR_MESSAGE);
                }
    }catch(SQLException e){
        e.printStackTrace();
    }
  }
}
    
    public void limpiarControles(){
        cmbCodigo.setValue("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtUsuario.setText("");
        txtContrasena.setText("");
        txtTipo.setText("");
    }
    
    public void desactivarControles(){
        cmbCodigo.setDisable(true);
        txtNombre.setEditable(false);
        txtEmail.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasena.setEditable(false);
        txtTipo.setEditable(false);
    }
    
    public void activarControles(){
        cmbCodigo.setDisable(false);
        txtNombre.setEditable(true);
        txtEmail.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasena.setEditable(true);
        txtTipo.setEditable(true);
    }
}
