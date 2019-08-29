package org.natancastaneda.bean;

public class Producto {
    private int codigoProducto;
    private int existencia;
    private double precioUnitario;
    private double precioDocena;
    private String imagen;
    private int codigoCategoria;
    private String categoria;
    private int codigoMarca;
    private String marca;
    private int codigoTalla;
    private String talla;
    private String descripcion;  
    private int precioMayor;

    public Producto(int codigoProducto, int existencia, double precioUnitario, double precioDocena, String imagen, int codigoCategoria, String categoria, int codigoMarca, String marca, int codigoTalla, String talla, String descripcion, int precioMayor) {
        this.codigoProducto = codigoProducto;
        this.existencia = existencia;
        this.precioUnitario = precioUnitario;
        this.precioDocena = precioDocena;
        this.imagen = imagen;
        this.codigoCategoria = codigoCategoria;
        this.categoria = categoria;
        this.codigoMarca = codigoMarca;
        this.marca = marca;
        this.codigoTalla = codigoTalla;
        this.talla = talla;
        this.descripcion = descripcion;
        this.precioMayor = precioMayor;
    }

    public Producto() {
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioDocena() {
        return precioDocena;
    }

    public void setPrecioDocena(double precioDocena) {
        this.precioDocena = precioDocena;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(int codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(int codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getCodigoTalla() {
        return codigoTalla;
    }

    public void setCodigoTalla(int codigoTalla) {
        this.codigoTalla = codigoTalla;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecioMayor() {
        return precioMayor;
    }

    public void setPrecioMayor(int precioMayor) {
        this.precioMayor = precioMayor;
    }
    
    public String toString(){
        return getCodigoProducto()+ " | " + getDescripcion();
    }
    
}


