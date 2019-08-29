package org.natancastaneda.bean;

public class Cliente {
    private int codigoCliente;
    private String direccion;
    private String nit;
    private String nombre;

    public Cliente(int codigoCliente, String direccion, String nit, String nombre) {
        this.codigoCliente = codigoCliente;
        this.direccion = direccion;
        this.nit = nit;
        this.nombre = nombre;
    }
    
    public Cliente(){
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String toString(){
        return getCodigoCliente() +" | "+ getNombre();
    }
    

}
