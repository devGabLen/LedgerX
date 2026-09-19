package com.ledgerx.controller;

import com.ledgerx.dao.TransaccionDAO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Map;

public class ReportesView extends VBox {

    private final TransaccionDAO dao = new TransaccionDAO();
    private final VBox contenedorGraficas = new VBox(20);

    public ReportesView() {
        setSpacing(20);
        setPadding(new Insets(15));

        Button btnActualizar = new Button("Actualizar reportes");
        btnActualizar.setOnAction(e -> cargarGraficas());

        VBox panelBoton = new VBox(btnActualizar);
        panelBoton.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(panelBoton, contenedorGraficas);

        cargarGraficas();
    }

    private void cargarGraficas() {
        contenedorGraficas.getChildren().clear();
        contenedorGraficas.getChildren().add(crearGraficaGastosPorCategoria());
        contenedorGraficas.getChildren().add(crearGraficaBalancePorMes());
    }

    private PieChart crearGraficaGastosPorCategoria() {
        PieChart grafica = new PieChart();
        grafica.setTitle("Gastos por categoría");

        try {
            Map<String, Double> datos = dao.obtenerGastosPorCategoria();
            for (Map.Entry<String, Double> entry : datos.entrySet()) {
                grafica.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar gráfica de categorías: " + e.getMessage());
        }

        return grafica;
    }

    private BarChart<String, Number> crearGraficaBalancePorMes() {
        CategoryAxis ejeX = new CategoryAxis();
        ejeX.setLabel("Mes");

        NumberAxis ejeY = new NumberAxis();
        ejeY.setLabel("Balance ($)");

        BarChart<String, Number> grafica = new BarChart<>(ejeX, ejeY);
        grafica.setTitle("Balance por mes");

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Balance");

        try {
            Map<String, Double> datos = dao.obtenerBalancePorMes();
            for (Map.Entry<String, Double> entry : datos.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar gráfica de balance: " + e.getMessage());
        }

        grafica.getData().add(serie);
        return grafica;
    }
}