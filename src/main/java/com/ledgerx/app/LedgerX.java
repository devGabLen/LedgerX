package com.ledgerx.app;

import com.ledgerx.controller.MainView;
import com.ledgerx.controller.ReportesView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class LedgerX extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabTransacciones = new Tab("Transacciones", new MainView());
        Tab tabReportes = new Tab("Reportes", new ReportesView());

        tabPane.getTabs().addAll(tabTransacciones, tabReportes);

        Scene scene = new Scene(tabPane, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setTitle("LedgerX - Personal Finance Manager");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}