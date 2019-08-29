
package org.natancastaneda.bean;


public class EmailCliente {
    private int codigoEmailCliente;
    private String descripcion;
    private String email;
    private int codigoCliente;

    public EmailCliente(int codigoEmailCliente, String descripcion, String email, int codigoCliente) {
        this.codigoEmailCliente = codigoEmailCliente;
        this.descripcion = descripcion;
        this.email = email;
        this.codigoCliente = codigoCliente;
    }

    public EmailCliente() {
    }

    public int getCodigoEmailCliente() {
        return codigoEmailCliente;
    }

    public void setCodigoEmailCliente(int codigoEmailCliente) {
        this.codigoEmailCliente = codigoEmailCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    
    public String toString(){
        return getCodigoEmailCliente() +" | " +getDescripcion();
    }
    
}
