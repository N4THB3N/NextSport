
package org.natancastaneda.controller;

import java.net.URL;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.natancastaneda.bean.Registro;
import org.natancastaneda.db.Conexion;
import org.natancastaneda.report.GenerarReporte;
import org.natancastaneda.sistema.Principal;


public class LoginController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, AGREGAR, ACTUALIZAR, EDITAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Registro> listaRegistro;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasena;
    @FXML private ComboBox cmbCodigo;
    @FXML private Button btnNuevo;
    @FXML private Button btnRegistro;
    @FXML private Button btnCancelar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCodigo.setItems(getRegistros());
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaRegistro(){
        escenarioPrincipal.ventanaRegistro();
    }
    
    public void ventanaMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void buscar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarUsuario(?,?)}");
            procedimiento.setString(1, txtUsuario.getText());
            procedimiento.setString(2, txtContrasena.getText());
            ResultSet resultado = procedimiento.executeQuery();
            
            if(resultado.next()){
                ventanaMenuPrincipal();
            }else{
               int answer = JOptionPane.showConfirmDialog(null, "El usuario no ha sido ingresado. ¿Desea crearlo?", "Error de registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(answer==JOptionPane.YES_NO_OPTION){
                        ventanaRegistro();
                    }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void cancelar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                txtUsuario.setText("");
                txtContrasena.setText("");
                cmbCodigo.setValue("");
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(((Registro)cmbCodigo.getSelectionModel().getSelectedItem()).getTipoUsuario().equals("admin")){
                tipoDeOperacion = operaciones.ACTUALIZAR;
                btnNuevo.setDisable(false);
                imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Usted no puede ver los registros");
                }
                break;
                
            case ACTUALIZAR:
                btnNuevo.setDisable(false);
                activarControles();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("Codigo Usuario", null);
        GenerarReporte.mostrarReporte("ReporteLogin.jasper", "Reporte Usuarios", parametros);
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
    
    public void activarControles(){
        txtUsuario.setText("");
        txtContrasena.setText("");
        cmbCodigo.setValue("");
    }
}
