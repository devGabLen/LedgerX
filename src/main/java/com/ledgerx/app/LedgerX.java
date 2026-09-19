package com.ledgerx.app;
/**
 *
 * @author gabrielpc
 */
import com.ledgerx.controller.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LedgerX extends Application{

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 900, 600);
        
        primaryStage.setTitle("LedgerX - Personal Financec Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    public static void main (String[]args){
        launch(args);
    }
}
