
package org.natancastaneda.bean;


public class TelefonoCliente {
    private int codigoTelefonoCliente;
    private String numero;
    private String descripcion;
    private int codigoCliente;

    public TelefonoCliente(int codigoTelefonoCliente, String numero, String descripcion, int codigoCliente) {
        this.codigoTelefonoCliente = codigoTelefonoCliente;
        this.numero = numero;
        this.descripcion = descripcion;
        this.codigoCliente = codigoCliente;
    }

    public TelefonoCliente() {
    }

    public int getCodigoTelefonoCliente() {
        return codigoTelefonoCliente;
    }

    public void setCodigoTelefonoCliente(int codigoTelefonoCliente) {
        this.codigoTelefonoCliente = codigoTelefonoCliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    
    public String toString(){
        return getCodigoTelefonoCliente()+ " | "+getDescripcion();
    }
}
