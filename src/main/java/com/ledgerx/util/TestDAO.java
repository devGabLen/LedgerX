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
import java.util.Map;

public class TestDAO {
    public static void main(String[] args) {
        TransaccionDAO dao = new TransaccionDAO();

        try {
            System.out.println("Insertando transacciones");

            Transaccion ingreso = new Transaccion(
                    TipoTransaccion.INGRESO, 1500.00, "salario",
                    LocalDate.of(2026, 9, 1), "Pago de nómina de septiembre"
            );
            dao.insertar(ingreso);
            System.out.println("Ingreso insertado");

            Transaccion gasto = new Transaccion(
                    TipoTransaccion.GASTO, 45.50, "comida",
                    LocalDate.of(2026, 9, 3), "Almuerzo con amigos"
            );
            dao.insertar(gasto);
            System.out.println("Gasto insertado.");

            System.out.println("Listado de transacciones");
            List<Transaccion> lista = dao.listarTodas();
            for (Transaccion t : lista) {
                System.out.println(t);
            }

            if (!lista.isEmpty()) {
                int idPrueba = lista.get(0).getId();
                System.out.println("Buscando transaccion por ID");
                Transaccion encontrada = dao.buscarPorId(idPrueba);
                System.out.println(encontrada);

                System.out.println("Actualizando transaccion");
                encontrada.setMonto(999.99);
                encontrada.setDescripcion("Descripcion actualizada desde TestDAO");
                dao.actualizar(encontrada);
                System.out.println("Actualizada. Nuevo estaado: " + dao.buscarPorId(idPrueba));
            }

            System.out.println("Balance actual");
            double balance = dao.calcularBalance();
            System.out.println("Balance" + balance);

            if (!lista.isEmpty()) {
                int idAEliminar = lista.get(lista.size() - 1).getId();
                System.out.println("Eliminando transacccion con id" + idAEliminar);
                dao.eliminar(idAEliminar);
                System.out.println("ELiminada");
            }

            System.out.println("Listado final");
            for (Transaccion t : dao.listarTodas()) {
                System.out.println(t);
            }
            
            System.out.println("\n=== Gastos por categoría ===");
            Map<String, Double> gastosPorCategoria = dao.obtenerGastosPorCategoria();
            for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
                System.out.println(entry.getKey() + ": $" + entry.getValue());
            }

            System.out.println("\n=== Balance por mes ===");
            Map<String, Double> balancePorMes = dao.obtenerBalancePorMes();
            for (Map.Entry<String, Double> entry : balancePorMes.entrySet()) {
                System.out.println(entry.getKey() + ": $" + entry.getValue());
            }

        } catch (SQLException e) {
            System.out.println("Ocurrio un error con la DB");
            e.printStackTrace();
        }
    }
}