package com.ledgerx.util;
/**
 *
 * @author gabrielpc
 */
import java.sql.Connection;
import java.sql.DriverManager;
 import java.sql.SQLException;
public class ConexionDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/ledgerx";
    private static final String USUARIO = "ledgerx_user";
    private static final String PASSWORD = "tu_password_aqui";
    
    private ConexionDB(){
    }
    
    public static Connection obtenerConexion()throws SQLException{
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
