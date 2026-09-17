/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ledgerx.util;
/**
 *
 * @author gabrielpc
 */
import com.ledgerx.dao.TransaccionDAO;
import com.ledgerx.model.Transaccion;
import com.ledgerx.model.TipoTransaccion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;


public class TestDAO {
    public static void main(String[]args){
        TransaccionDAO dao = new TransaccionDAO();
        try {
            System.out.println("Insertando Transacciones");
            Transaccion ingreso = new Transaccion(
                    TipoTransaccion.INGRESO, 1500.00, "salario", LocalDate.of(2026, 9, 1), "Nomina de septiembre"
            );
            dao.insertar(ingreso);
            System.out.println("Ingreso insertado");
            
            Transaccion gasto = new Transaccion(
                    TipoTransaccion.GASTO, 45.00, "Comida",
                    LocalDate.of(2026, 9, 3), "Almuerzo con amigos"
            );
            dao.insertar(gasto);
            System.out.println("Gasto insertado");
            
            
        }catch (SQLException e){
            System.out.println("Ocurrio un error con la DB");
            e.printStackTrace();
        }
    
    }
}