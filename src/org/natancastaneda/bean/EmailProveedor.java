
package org.natancastaneda.bean;


public class EmailProveedor {
    private int codigoEmailProveedor;
    private int codigoProveedor;
    private String contactoPrincipal;
    private String descripcion;
    private String email;

    public EmailProveedor(int codigoEmailProveedor, int codigoProveedor, String contactoPrincipal, String descripcion, String email) {
        this.codigoEmailProveedor = codigoEmailProveedor;
        this.codigoProveedor = codigoProveedor;
        this.contactoPrincipal = contactoPrincipal;
        this.descripcion = descripcion;
        this.email = email;
    }

    public EmailProveedor() {
    }

    public int getCodigoEmailProveedor() {
        return codigoEmailProveedor;
    }

    public void setCodigoEmailProveedor(int codigoEmailProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(String contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
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
    
    public String toString(){
        return getCodigoEmailProveedor() +" | "+getContactoPrincipal();
    }
}
