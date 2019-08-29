
package org.natancastaneda.bean;

import java.util.Date;


public class Compra {
    private int numeroDocumento;
    private int codigoProveedor;
    private Double total;
    private Date fecha;
    private String descripcion;
    private String paginaWeb;
    private String contactoPrincipal;
    private String razonSocial;
    private String nit;
    private String direccion;

    public Compra() {
    }

    public Compra(int numeroDocumento, int codigoProveedor, Double total, Date fecha, String descripcion, String paginaWeb, String contactoPrincipal, String razonSocial, String nit, String direccion) {
        this.numeroDocumento = numeroDocumento;
        this.codigoProveedor = codigoProveedor;
        this.total = total;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.paginaWeb = paginaWeb;
        this.contactoPrincipal = contactoPrincipal;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.direccion = direccion;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public String getContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(String contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String toString(){
        return getNumeroDocumento()+" | "+getDescripcion();
    }
}
