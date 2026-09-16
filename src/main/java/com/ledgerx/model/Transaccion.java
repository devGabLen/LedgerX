package com.ledgerx.model;

/**
 *
 * @author gabrielpc
 */
import java.time.LocalDate;

public class Transaccion {
    private int id;
    private TipoTransaccion tipo;
    private double monto;
    private String categoria;
    private LocalDate fecha;
    private String descripcion;
    
    
    // Contructor vacio
    public Transaccion(){
    }
    //constructor para toda la DB
    public Transaccion(int id, TipoTransaccion tipo, double monto, String categoria, LocalDate fecha, String descripcion){
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
    //constructor sin id
    public Transaccion(TipoTransaccion tipo, double monto, String categoria, LocalDate fecha, String descripcion){
        this.tipo = tipo;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
    
    //getters y setters
    
    public int getId(){
        return id;
    }
    public void setId(){
        this.id = id;
    }
    public TipoTransaccion getTipo(){
        return tipo;
    }
    public void setTipo(TipoTransaccion tipo){
        this.tipo = tipo;
    }
    public double getMonto(){
        return monto;
    }
    public void setMonto(double monto){
        this.monto = monto;
    }
    public String getCategoria(){
        return categoria;
    }
    public void setCategoria(String categoria){
        this.categoria = categoria;
    }
    public LocalDate getFecha(){
        return fecha;
    }
    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    
    public String toString(){
        return "Transacción{"+
                "id= "+ id +
                ", tipo= " + tipo +
                ", monto= "+ monto +
                ", categoria= '"+ categoria +'\''+
                ", fecha= "+ fecha +
                ", descripción= '"+ descripcion + '\''+
                '}';
    }
}
