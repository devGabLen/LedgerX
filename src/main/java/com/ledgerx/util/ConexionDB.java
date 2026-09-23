package com.ledgerx.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConexionDB.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException(
                    "config.properties not found. Copy config.properties.example to "
                    + "config.properties and fill in your own database credentials."
                );
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties", e);
        }
    }

    private ConexionDB() {
    }

    public static Connection obtenerConexion() throws SQLException {
        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        return DriverManager.getConnection(url, usuario, password);
    }
}