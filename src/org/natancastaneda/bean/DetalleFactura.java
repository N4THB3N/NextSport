
package org.natancastaneda.bean;


public class DetalleFactura {
    private int codigoDetalleFactura;
    private int cantidad;
    private int numeroFactura;
    private Double precio;
    private int codigoProducto;

    public DetalleFactura(int codigoDetalleFactura, int cantidad, int numeroFactura, Double precio, int codigoProducto) {
        this.codigoDetalleFactura = codigoDetalleFactura;
        this.cantidad = cantidad;
        this.numeroFactura = numeroFactura;
        this.precio = precio;
        this.codigoProducto = codigoProducto;
    }

    public DetalleFactura() {
    }

    public int getCodigoDetalleFactura() {
        return codigoDetalleFactura;
    }

    public void setCodigoDetalleFactura(int codigoDetalleFactura) {
        this.codigoDetalleFactura = codigoDetalleFactura;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    public String toString(){
        return getCodigoDetalleFactura() +" | "+ getPrecio(); 
    }
}
