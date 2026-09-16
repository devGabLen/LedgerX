package com.ledgerx.util;
/**
 *
 * @author gabrielpc
 */
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[]args){
        try  (Connection conexion = ConexionDB.obtenerConexion()){
            System.out.println("¡Conexion exitosa a la base de datos!");
            System.out.println("Detalles: "+ conexion.getMetaData().getURL());
        }catch (SQLException e){
            System.out.println("Error al conectar a la DB");
            e.printStackTrace();
        }
        
    }
}
