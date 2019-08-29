package org.natancastaneda.bean;

/**
 *
 * @author Nathan Castañeda
 */
public class Talla {
    private int codigoTalla;
    private String descripcion;

    public Talla(int codigoTalla, String descripcion) {
        this.codigoTalla = codigoTalla;
        this.descripcion = descripcion;
    }
    
    public Talla() {
    }

    public int getCodigoTalla() {
        return codigoTalla;
    }

    public void setCodigoTalla(int codigoTalla) {
        this.codigoTalla = codigoTalla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
     public String toString(){
        return getCodigoTalla() + " | " +getDescripcion();
    }
}
