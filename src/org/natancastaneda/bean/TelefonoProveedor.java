
package org.natancastaneda.bean;


public class TelefonoProveedor {
    private int codigoProveedor;
    private int codigoTelefonoProveedor;
    private String numero;
    private String descripcion;

    public TelefonoProveedor(int codigoProveedor, int codigoTelefonoProveedor, String numero, String descripcion) {
        this.codigoProveedor = codigoProveedor;
        this.codigoTelefonoProveedor = codigoTelefonoProveedor;
        this.numero = numero;
        this.descripcion = descripcion;
    }

    public TelefonoProveedor() {
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoTelefonoProveedor() {
        return codigoTelefonoProveedor;
    }

    public void setCodigoTelefonoProveedor(int codigoTelefonoProveedor) {
        this.codigoTelefonoProveedor = codigoTelefonoProveedor;
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
    
    public String toString(){
        return getCodigoTelefonoProveedor()+" | "+getDescripcion();
    }
}
