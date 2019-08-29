package org.natancastaneda.bean;

import java.util.Date;


public class Factura {
    private int numeroFactura;
    private String estado;
    private Date fecha;
    private String nit;
    private Double total;
    private int codigoCliente; 

    public Factura(int numeroFactura, String estado, Date fecha, String nit, Double total, int codigoCliente) {
        this.numeroFactura = numeroFactura;
        this.estado = estado;
        this.fecha = fecha;
        this.nit = nit;
        this.total = total;
        this.codigoCliente = codigoCliente;
    }

    public Factura() {
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    
    public String toString(){
        return getNumeroFactura() + " | " + getEstado();
    }
}
