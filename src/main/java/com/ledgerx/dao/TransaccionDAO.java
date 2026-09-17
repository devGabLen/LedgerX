package com.ledgerx.dao;
/**
 *
 * @author gabrielpc
 */
import com.ledgerx.model.TipoTransaccion;
import com.ledgerx.model.Transaccion;
import com.ledgerx.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {
    public void insertar(Transaccion transaccion) throws SQLException{
        String sql = "INSERT INTO transacciones (tipo, monto, categoria, fecha, descripcion)"+
                    "VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conexion.prepareStatement(sql)){
            stmt.setString(1, transaccion.getTipo().name());
            stmt.setDouble(2, transaccion.getMonto());
            stmt.setString(3, transaccion.getCategoria());
            stmt.setDate(4, java.sql.Date.valueOf(transaccion.getFecha()));
            stmt.setString(5, transaccion.getDescripcion());
            
            stmt.executeUpdate();
        }
    }
    
    public List<Transaccion> listarTodas() throws SQLException{
        List<Transaccion> transacciones = new ArrayList<>();
        String sql = "SELECT id, tipo, monto, categoria, fecha, descripcion "+
                    "FROM transacciones ORDER BY fecha DESC";
        try(Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conexion.prepareCall(sql);
                ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()){
                Transaccion t = new Transaccion(
                        rs.getInt("id"),
                        TipoTransaccion.valueOf(rs.getString("tipo")),
                        rs.getDouble("monto"),
                        rs.getString("categoria"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("descripcion")
                );
                transacciones.add(t);
            
            }
        }
        return transacciones;
    }
    
    public void actualizar(Transaccion transaccion) throws SQLException{
        String sql = "UPDATE transacciones SET tipo = ?, monto = ?, categoria = ?, "
                   + "fecha = ?, descripcion = ? WHERE id = ?";
        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conexion.prepareCall(sql)){
            stmt.setString(1, transaccion.getTipo().name());
            stmt.setDouble(2, transaccion.getMonto());
            stmt.setString(3, transaccion.getCategoria());
            stmt.setDate(4, java.sql.Date.valueOf(transaccion.getFecha()));
            stmt.setString(5, transaccion.getDescripcion());
            stmt.setInt(6, transaccion.getId());
        
            stmt.executeUpdate();
        }
    }
    
    public void eliminar(int id) throws SQLException{
        String sql = "DELETE FROM transacciones WHERE id = ?";
        
        try(Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conexion.prepareCall(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }        
    }
    
    public Transaccion buscarPorId(int id) throws SQLException{
        String sql = "SELECT id, tipo, monto, categoria, fecha, descripcion " + "FROM transacciones WHERE id = ?";
        
        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conexion.prepareStatement(sql)){
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return new Transaccion(
                            rs.getInt("id"),
                            TipoTransaccion.valueOf(rs.getString("tipo")),
                            rs.getDouble("monto"),
                            rs.getString("categoria"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getString("descripcion")
                    );
                }
            
            }
        }
        
        return null; //no se encontró transaccion con ID
        
    }
    
    public double calcularBalance()throws SQLException{
        String sql = "SELECT "
           + "COALESCE(SUM(CASE WHEN tipo = 'INGRESO' THEN monto ELSE 0 END), 0) - "
           + "COALESCE(SUM(CASE WHEN tipo = 'GASTO' THEN monto ELSE 0 END), 0) AS balance "
           + "FROM transacciones";
        try (Connection conexion = ConexionDB.obtenerConexion();
                PreparedStatement stmt = conexion.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()){
            if(rs.next()){
                return rs.getDouble("balance");
            }
        
        }
        return 0.0;
    }
    
}
