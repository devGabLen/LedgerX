package com.ledgerx.app;
/**
 *
 * @author gabrielpc
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LedgerX extends Application{

    public void start(Stage primaryStage) {
        Label welcomeLabel = new Label("Bienvenido a LedgerX");
        welcomeLabel.setStyle("-fx-font-size: 20px");
        
        StackPane root = new StackPane();
        root.getChildren().add(welcomeLabel);
        
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("LedgerX - Personal Finance Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    public static void main (String[]args){
        launch(args);
    }
}
