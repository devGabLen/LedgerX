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

        Scene scene = new Scene(tabPane, 900, 600);

        primaryStage.setTitle("LedgerX - Personal Finance Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}