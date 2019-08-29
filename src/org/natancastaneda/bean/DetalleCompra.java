
package org.natancastaneda.bean;

public class DetalleCompra{
    private int codigoProducto;
    private int codigoProveedor;
    private int cantidad;
    private int codigoDetalleCompra;
    private int numeroDocumento;
    private Double subtotal;
    private Double costoUnitario;

    public DetalleCompra(int codigoProducto, int codigoProveedor, int cantidad, int codigoDetalleCompra, int numeroDocumento, Double subtotal, Double costoUnitario) {
        this.codigoProducto = codigoProducto;
        this.codigoProveedor = codigoProveedor;
        this.cantidad = cantidad;
        this.codigoDetalleCompra = codigoDetalleCompra;
        this.numeroDocumento = numeroDocumento;
        this.subtotal = subtotal;
        this.costoUnitario = costoUnitario;
    }

    public DetalleCompra() {
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCodigoDetalleCompra() {
        return codigoDetalleCompra;
    }

    public void setCodigoDetalleCompra(int codigoDetalleCompra) {
        this.codigoDetalleCompra = codigoDetalleCompra;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(Double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }
    
    public String toString(){
        return getCodigoDetalleCompra() +" | " + getCostoUnitario();
    }
}
