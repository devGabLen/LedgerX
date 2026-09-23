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
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Map;

public class ReportesView extends VBox {

    private final TransaccionDAO dao = new TransaccionDAO();
    private final HBox contenedorGraficas = new HBox(24);

    public ReportesView() {
        setSpacing(24);
        setPadding(new Insets(20));
        getStyleClass().add("reportes-root");

        Button btnActualizar = new Button("Actualizar reportes");
        btnActualizar.getStyleClass().add("button-secondary");
        btnActualizar.setOnAction(e -> cargarGraficas());

        HBox panelBoton = new HBox(btnActualizar);
        panelBoton.setAlignment(Pos.CENTER_RIGHT);

        contenedorGraficas.setFillHeight(true);

        getChildren().addAll(panelBoton, contenedorGraficas);

        cargarGraficas();
    }

    private void cargarGraficas() {
        contenedorGraficas.getChildren().clear();

        VBox pieCard = crearTarjetaGrafica("Gastos por categoría", crearGraficaGastosPorCategoria());
        VBox barCard = crearTarjetaGrafica("Balance por mes", crearGraficaBalancePorMes());

        HBox.setHgrow(pieCard, Priority.ALWAYS);
        HBox.setHgrow(barCard, Priority.ALWAYS);

        contenedorGraficas.getChildren().addAll(pieCard, barCard);
    }

    private VBox crearTarjetaGrafica(String titulo, javafx.scene.Node grafica) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("card-title");

        VBox card = new VBox(16, lblTitulo, grafica);
        card.getStyleClass().add("card");
        VBox.setVgrow(grafica, Priority.ALWAYS);

        return card;
    }

    private PieChart crearGraficaGastosPorCategoria() {
        PieChart grafica = new PieChart();
        grafica.setLegendVisible(true);
        grafica.setLabelsVisible(true);
        grafica.setPrefHeight(320);

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
        grafica.setLegendVisible(false);
        grafica.setPrefHeight(320);
        grafica.setBarGap(8);
        grafica.setCategoryGap(30);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();

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