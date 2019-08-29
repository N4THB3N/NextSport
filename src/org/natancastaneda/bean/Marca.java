/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.natancastaneda.bean;

/**
 *
 * @author Nathan Castañeda
 */
public class Marca {
    private int codigoMarca;
    private String descripcion;

    public Marca(int codigoMarca, String descripcion) {
        this.codigoMarca = codigoMarca;
        this.descripcion = descripcion;
    }
    
    public Marca(){
    }
    public int getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(int codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String toString(){
        return getCodigoMarca() + " | " +getDescripcion();
    }
}
