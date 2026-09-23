/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ledgerx.model;

/**
 *
 * @author gabrielpc
 */
public class Categoria {
    private int id;
    private String nombre;
    private TipoTransaccion tipo;

    public Categoria() {
    }

    public Categoria(int id, String nombre, TipoTransaccion tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    
    
    
}
