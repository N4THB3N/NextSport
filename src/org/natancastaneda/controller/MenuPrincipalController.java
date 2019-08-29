package org.natancastaneda.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.natancastaneda.sistema.Principal;

/**
 *
 * @author informatica
 */
public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;

     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaCategorias(){
        escenarioPrincipal.ventanaCategorias();
    }
    
    public void ventanaMarcas(){
        escenarioPrincipal.ventanaMarcas();
    }
    
    public void ventanaTallas(){
        escenarioPrincipal.ventanaTallas();
    }
    
    public void ventanaClientes(){
        escenarioPrincipal.ventanaClientes();
    }
    
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
     
    public void ventanaProveedores(){
        escenarioPrincipal.ventanaProveedores();
    }
    
    public void ventanaCompras(){
        escenarioPrincipal.ventanaCompras();
    }
    
    public void ventanaFacturas(){
        escenarioPrincipal.ventanaFacturas();
    }
    
    public void ventanaEmailCliente(){
        escenarioPrincipal.ventanaEmailCliente();
    }
    
    public void ventanaEmailProveedor(){
        escenarioPrincipal.ventanaEmailProveedor();
    }
    
    public void ventanaAbout(){
        escenarioPrincipal.ventanaAbout();
    }
    
    public void ventanaTelefonoCliente(){
        escenarioPrincipal.ventanaTelefonoCliente();
    }
    
    public void ventanaTelefonoProveedor(){
        escenarioPrincipal.ventanaTelefonoProveedor();
    }
}
