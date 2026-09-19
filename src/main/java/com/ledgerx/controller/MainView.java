package com.ledgerx.controller;
/**
 *
 * @author gabrielpc
 */
import com.ledgerx.dao.TransaccionDAO;
import com.ledgerx.model.Transaccion;
import com.ledgerx.model.TipoTransaccion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.LocalDate;


public class MainView extends  BorderPane{
    private final TransaccionDAO dao = new TransaccionDAO();
    private final ObservableList<Transaccion> datosTabla = FXCollections.observableArrayList();
    
    //componentees form
    private ComboBox<TipoTransaccion> comboTipo;
    private TextField campoMonto;
    private TextField campoCategoria;
    private DatePicker campoFecha;
    private TextField campoDescripcion;
    
    //Tabla y label de balance
    private TableView<Transaccion> tabla;
    private Label labelBalance;
    
    public MainView(){
        setPadding(new Insets(15));
        setTop(crearFormulario());
        setCenter(crearPanelTabla());
        setBottom(crearPanelBalance());
        
        cargarDatos();
        
    }
    
    private GridPane crearFormulario(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        comboTipo = new ComboBox<>(FXCollections.observableArrayList(TipoTransaccion.values()));
        comboTipo.setPromptText("Tipo");
        
        campoMonto = new TextField();
        campoMonto.setPromptText("Monto");
        
        campoCategoria = new TextField();
        campoCategoria.setPromptText("Categoria");
        
        campoFecha = new DatePicker(LocalDate.now());
        
        campoDescripcion = new TextField();
        campoDescripcion.setPromptText("Descripcion");
        
        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> guardarTransaccion());
        
        grid.add(new Label("Tipo:"), 0, 0);
        grid.add(comboTipo, 1, 0);
        grid.add(new Label("Monto:"), 2, 0);
        grid.add(campoMonto, 3, 0);
        
        grid.add(new Label("Categoria:"), 0, 1);
        grid.add(campoCategoria, 1 ,1);
        grid.add(new Label("Fecha"), 2, 1);
        grid.add(campoFecha, 3, 1);
        
        grid.add(new Label("Descripcion"), 0, 2);
        grid.add(campoDescripcion, 1, 2, 3, 1);
        
        grid.add(btnGuardar, 3, 3);
        
                
        return grid;
    }
    
    private BorderPane crearPanelTabla() {
        tabla = new TableView<>(datosTabla);

        TableColumn<Transaccion, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaccion, TipoTransaccion> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Transaccion, Double> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));

        TableColumn<Transaccion, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Transaccion, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Transaccion, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tabla.getColumns().addAll(colId, colTipo, colMonto, colCategoria, colFecha, colDescripcion);

        Button btnEliminar = new Button("Eliminar seleccionada");
        btnEliminar.setOnAction(e -> eliminarSeleccionada());

        HBox panelBotones = new HBox(10, btnEliminar);
        panelBotones.setPadding(new Insets(10, 0, 0, 0));
        panelBotones.setAlignment(Pos.CENTER_RIGHT);

        BorderPane contenedor = new BorderPane();
        contenedor.setCenter(tabla);
        contenedor.setBottom(panelBotones);

        return contenedor;
    }
    
    private HBox crearPanelBalance(){
        labelBalance = new Label("Balance: $0.00");
        labelBalance.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        HBox box = new HBox(labelBalance);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        
        return box;
    }
    
    private void guardarTransaccion(){
        try{
            TipoTransaccion tipo = comboTipo.getValue();
            String montoTexto = campoMonto.getText();
            String categoria = campoCategoria.getText();
            LocalDate fecha = campoFecha.getValue();
            String descripcion = campoDescripcion.getText();
            
            if(tipo == null|| montoTexto.isBlank() || categoria.isBlank() || fecha == null){
                mostrarAlerta("Por favor completa todos los campos obligatorios");
                
            }
            double monto = Double.parseDouble(montoTexto);
            Transaccion nueva = new Transaccion(tipo, monto, categoria, fecha, descripcion);
            dao.insertar(nueva);
            
            limpiarFormulario();
            cargarDatos();
            
            
        } catch (NumberFormatException e){
            mostrarAlerta("El monto debe ser un número valido");
        } catch (SQLException e){
            mostrarAlerta("Error al guardar en la base de datos");
        }
    }
    
    private void eliminarSeleccionada(){
        Transaccion seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null){
            mostrarAlerta("Selecciona una transaccion de la tabla para eliminar");
            return;
        }
        
        try{
            dao.eliminar(seleccionada.getId());
            cargarDatos();
        } catch (SQLException e){
            mostrarAlerta("Error al eliminar: "+e.getMessage());
        }
    }
    
    private void cargarDatos(){
        try{
            datosTabla.setAll(dao.listarTodas());
            double balance = dao.calcularBalance();
            labelBalance.setText(String.format("Balance: $%.2f", balance));
        } catch (SQLException e){
            mostrarAlerta("Error al cargar datos: "+e.getMessage());
        }
    }
    
    private void limpiarFormulario(){
        comboTipo.setValue(null);
        campoMonto.clear();
        campoCategoria.clear();
        campoFecha.setValue(LocalDate.now());
        campoDescripcion.clear();
    }
    
    private void mostrarAlerta(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.WARNING, mensaje);
        alerta.showAndWait();
    }
}
