/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.natancastaneda.sistema;

import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.natancastaneda.bean.Compra;
import org.natancastaneda.bean.Factura;
import org.natancastaneda.controller.AboutController;
import org.natancastaneda.controller.CategoriaController;
import org.natancastaneda.controller.ClienteController;
import org.natancastaneda.controller.CompraController;
import org.natancastaneda.controller.DetalleCompraController;
import org.natancastaneda.controller.DetalleFacturaController;
import org.natancastaneda.controller.EmailClienteController;
import org.natancastaneda.controller.EmailProveedorController;
import org.natancastaneda.controller.FacturaController;
import org.natancastaneda.controller.LoginController;
import org.natancastaneda.controller.MarcaController;
import org.natancastaneda.controller.MenuPrincipalController;
import org.natancastaneda.controller.ProductoController;
import org.natancastaneda.controller.ProveedorController;
import org.natancastaneda.controller.RegistroController;
import org.natancastaneda.controller.TallaController;
import org.natancastaneda.controller.TelefonoClienteController;
import org.natancastaneda.controller.TelefonoProveedorController;

/**
 *
 * @author informatica
 */
public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/natancastaneda/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal)throws Exception{
       this.escenarioPrincipal = escenarioPrincipal;
       escenarioPrincipal.setTitle("NextSport Application");
       Login();
       escenarioPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",600,600);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void Login(){
        try{
            LoginController loginController = (LoginController)cambiarEscena("Login.fxml",400,400);
            loginController.setEscenarioPrincipal(this);
        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    
    public void ventanaRegistro(){
        try{
            RegistroController registroController = (RegistroController)cambiarEscena("Registro.fxml",496,459);
            registroController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCategorias(){
        try{
            CategoriaController categoriaController = (CategoriaController)cambiarEscena("CategoriaView.fxml",600,470);
            categoriaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMarcas(){
        try{
            MarcaController marcaController = (MarcaController)cambiarEscena("MarcasView.fxml",602,432);
            marcaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTallas(){
        try{
            TallaController tallaController = (TallaController)cambiarEscena("TallaView.fxml",555,478);
            tallaController.setEscenarioprincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaClientes(){
        try{
            ClienteController clienteController = (ClienteController)cambiarEscena("ClientesView.fxml",600,470);
            clienteController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos(){
        try{
            ProductoController productoController = (ProductoController)cambiarEscena("ProductoView.fxml",598,483);
            productoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProveedores(){
        try{
            ProveedorController proveedorController = (ProveedorController)cambiarEscena("ProveedorView.fxml",600,400);
            proveedorController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCompras(){
        try{
            CompraController compraController = (CompraController)cambiarEscena("Compras.fxml",525,457);
            compraController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaFacturas(){
        try{
            FacturaController facturaController = (FacturaController)cambiarEscena("FacturaView.fxml",522,465);
            facturaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmailCliente(){
        try{
            EmailClienteController emailClienteController = (EmailClienteController)cambiarEscena("EmailCliente.fxml",540,443);
            emailClienteController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleFactura(Factura factura){
        try{
            DetalleFacturaController detalleFacturaController = (DetalleFacturaController)cambiarEscena("DetalleFactura.fxml",549,441);
            detalleFacturaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmailProveedor(){
        try{
            EmailProveedorController emailProveedorController = (EmailProveedorController)cambiarEscena("EmailProveedor.fxml",513,433);
            emailProveedorController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaAbout(){
        try{
            AboutController aboutController = (AboutController)cambiarEscena("About.fxml",600,400);
            aboutController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTelefonoCliente(){
        try{
            TelefonoClienteController telefonoClienteController = (TelefonoClienteController)cambiarEscena("TelefonoClienteView.fxml",495,400);
            telefonoClienteController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTelefonoProveedor(){
        try{
            TelefonoProveedorController telefonoProveedorController = (TelefonoProveedorController)cambiarEscena("TelefonoProveedorView.fxml",548,464);
            telefonoProveedorController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleCompra(Compra compra){
        try{
            DetalleCompraController detalleCompraController = (DetalleCompraController)cambiarEscena("DetalleCompra.fxml",568,451);
            detalleCompraController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    } 
   
}
