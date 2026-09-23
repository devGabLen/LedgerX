package com.ledgerx.dao;

/**
 *
 * @author gabrielpc
 */
import com.ledgerx.model.Categoria;
import com.ledgerx.model.TipoTransaccion;
import com.ledgerx.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public List<Categoria> listarPorTipo(TipoTransaccion tipo) throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre, tipo FROM categorias WHERE tipo = ? ORDER BY nombre ASC";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, tipo.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categorias.add(new Categoria(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            TipoTransaccion.valueOf(rs.getString("tipo"))
                    ));
                }
            }
        }

        return categorias;
    }

    public List<Categoria> listarTodas() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre, tipo FROM categorias ORDER BY nombre ASC";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        TipoTransaccion.valueOf(rs.getString("tipo"))
                ));
            }
        }

        return categorias;
    }
}
