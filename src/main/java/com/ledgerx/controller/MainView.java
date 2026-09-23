package com.ledgerx.controller;

import com.ledgerx.dao.CategoriaDAO;
import com.ledgerx.dao.TransaccionDAO;
import com.ledgerx.model.Categoria;
import com.ledgerx.model.Transaccion;
import com.ledgerx.model.TipoTransaccion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.LocalDate;

public class MainView extends BorderPane {

    private final TransaccionDAO dao = new TransaccionDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ObservableList<Transaccion> datosTabla = FXCollections.observableArrayList();

    // Componentes del formulario
    private ComboBox<TipoTransaccion> comboTipo;
    private TextField campoMonto;
    private ComboBox<Categoria> comboCategoria;
    private DatePicker campoFecha;
    private TextField campoDescripcion;
    private Button btnGuardar;
    private Button btnCancelarEdicion;

    // Tabla y label de balance
    private TableView<Transaccion> tabla;
    private Label labelBalance;

    // Estado de edición: si es null, estamos creando; si tiene un id, estamos editando esa transacción
    private Transaccion transaccionEnEdicion = null;

    public MainView() {
        setPadding(new Insets(15));
        setTop(crearFormulario());
        setCenter(crearPanelTabla());
        setBottom(crearPanelBalance());

        cargarDatos();
    }

    // ================= FORMULARIO =================
    private GridPane crearFormulario() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("card");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        comboTipo = new ComboBox<>(FXCollections.observableArrayList(TipoTransaccion.values()));
        comboTipo.setPromptText("Tipo");
        comboTipo.setOnAction(e -> actualizarCategoriasSegunTipo());

        campoMonto = new TextField();
        campoMonto.setPromptText("Monto");

        comboCategoria = new ComboBox<>();
        comboCategoria.setPromptText("Categoría");

        campoFecha = new DatePicker(LocalDate.now());

        campoDescripcion = new TextField();
        campoDescripcion.setPromptText("Descripcion");

        btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> guardarOActualizarTransaccion());

        btnCancelarEdicion = new Button("Cancelar edición");
        btnCancelarEdicion.getStyleClass().add("button-secondary");
        btnCancelarEdicion.setOnAction(e -> cancelarEdicion());
        btnCancelarEdicion.setVisible(false);
        btnCancelarEdicion.setManaged(false); // no ocupa espacio cuando está oculto

        grid.add(new Label("Tipo:"), 0, 0);
        grid.add(comboTipo, 1, 0);
        grid.add(new Label("Monto:"), 2, 0);
        grid.add(campoMonto, 3, 0);

        grid.add(new Label("Categoria:"), 0, 1);
        grid.add(comboCategoria, 1, 1);
        grid.add(new Label("Fecha"), 2, 1);
        grid.add(campoFecha, 3, 1);

        grid.add(new Label("Descripcion"), 0, 2);
        grid.add(campoDescripcion, 1, 2, 3, 1);

        HBox panelBotonesForm = new HBox(10, btnCancelarEdicion, btnGuardar);
        panelBotonesForm.setAlignment(Pos.CENTER_RIGHT);
        grid.add(panelBotonesForm, 3, 3);

        return grid;
    }

    // ================= TABLA =================
    private BorderPane crearPanelTabla() {
        tabla = new TableView<>(datosTabla);

        TableColumn<Transaccion, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaccion, TipoTransaccion> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(TipoTransaccion tipo, boolean empty) {
                super.updateItem(tipo, empty);
                if (empty || tipo == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(tipo.name());
                    badge.getStyleClass().add(tipo == TipoTransaccion.INGRESO ? "badge-ingreso" : "badge-gasto");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        TableColumn<Transaccion, Double> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));

        TableColumn<Transaccion, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Transaccion, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Transaccion, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tabla.getColumns().addAll(colId, colTipo, colMonto, colCategoria, colFecha, colDescripcion);

        // Doble-click en una fila para editarla
        tabla.setRowFactory(tv -> {
            TableRow<Transaccion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY && !row.isEmpty()) {
                    cargarParaEditar(row.getItem());
                }
            });
            return row;
        });

        Button btnEliminar = new Button("Eliminar seleccionada");
        btnEliminar.getStyleClass().add("button-secondary");
        btnEliminar.setOnAction(e -> eliminarSeleccionada());

        HBox panelBotones = new HBox(10, btnEliminar);
        panelBotones.setPadding(new Insets(10, 0, 0, 0));
        panelBotones.setAlignment(Pos.CENTER_RIGHT);

        BorderPane contenedor = new BorderPane();
        contenedor.getStyleClass().add("card");
        contenedor.setCenter(tabla);
        contenedor.setBottom(panelBotones);

        return contenedor;
    }

    // ================= BALANCE =================
    private HBox crearPanelBalance() {
        labelBalance = new Label("Balance: $0.00");
        labelBalance.getStyleClass().add("balance-value");

        HBox box = new HBox(labelBalance);
        box.getStyleClass().add("balance-chip");
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    // ================= CATEGORÍAS DINÁMICAS =================
    private void actualizarCategoriasSegunTipo() {
        TipoTransaccion tipo = comboTipo.getValue();
        comboCategoria.getItems().clear();
        comboCategoria.setValue(null);
        if (tipo != null) {
            try {
                comboCategoria.getItems().addAll(categoriaDAO.listarPorTipo(tipo));
            } catch (SQLException e) {
                mostrarAlerta("Error al cargar categorías: " + e.getMessage());
            }
        }
    }

    // ================= LÓGICA DE EDICIÓN =================
    private void cargarParaEditar(Transaccion transaccion) {
        transaccionEnEdicion = transaccion;

        comboTipo.setValue(transaccion.getTipo());
        actualizarCategoriasSegunTipo();

        campoMonto.setText(String.valueOf(transaccion.getMonto()));

        // Buscar y seleccionar la categoría que coincida por nombre
        comboCategoria.getItems().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(transaccion.getCategoria()))
                .findFirst()
                .ifPresent(comboCategoria::setValue);

        campoFecha.setValue(transaccion.getFecha());
        campoDescripcion.setText(transaccion.getDescripcion());

        btnGuardar.setText("Actualizar");
        btnCancelarEdicion.setVisible(true);
        btnCancelarEdicion.setManaged(true);
    }

    private void cancelarEdicion() {
        transaccionEnEdicion = null;
        limpiarFormulario();
        btnGuardar.setText("Guardar");
        btnCancelarEdicion.setVisible(false);
        btnCancelarEdicion.setManaged(false);
    }

    // ================= LÓGICA CRUD =================
    private void guardarOActualizarTransaccion() {
        try {
            TipoTransaccion tipo = comboTipo.getValue();
            String montoTexto = campoMonto.getText().trim();
            Categoria categoriaSeleccionada = comboCategoria.getValue();
            LocalDate fecha = campoFecha.getValue();
            String descripcion = campoDescripcion.getText().trim();

            // Validación de campos obligatorios
            if (tipo == null || montoTexto.isBlank() || categoriaSeleccionada == null || fecha == null) {
                mostrarAlerta("Por favor completa todos los campos obligatorios");
                return;
            }

            // Validación de formato numérico
            double monto;
            try {
                monto = Double.parseDouble(montoTexto);
            } catch (NumberFormatException e) {
                mostrarAlerta("El monto debe ser un número válido (ej. 150.50)");
                return;
            }

            // Validación de rango del monto
            if (monto <= 0) {
                mostrarAlerta("El monto debe ser mayor a cero");
                return;
            }

            if (monto > 99_999_999.99) {
                mostrarAlerta("El monto ingresado es demasiado grande");
                return;
            }

            // Validación de fecha (no futura)
            if (fecha.isAfter(LocalDate.now())) {
                mostrarAlerta("La fecha no puede ser en el futuro");
                return;
            }

            // Validación de longitud de descripción
            if (descripcion.length() > 255) {
                mostrarAlerta("La descripción no puede superar los 255 caracteres");
                return;
            }

            String categoria = categoriaSeleccionada.getNombre();

            if (transaccionEnEdicion == null) {
                Transaccion nueva = new Transaccion(tipo, monto, categoria, fecha, descripcion);
                dao.insertar(nueva);
            } else {
                transaccionEnEdicion.setTipo(tipo);
                transaccionEnEdicion.setMonto(monto);
                transaccionEnEdicion.setCategoria(categoria);
                transaccionEnEdicion.setFecha(fecha);
                transaccionEnEdicion.setDescripcion(descripcion);
                dao.actualizar(transaccionEnEdicion);
            }

            cancelarEdicion();
            cargarDatos();

        } catch (SQLException e) {
            mostrarAlerta("Error al guardar en la base de datos: " + e.getMessage());
        }
    }

    private void eliminarSeleccionada() {
        Transaccion seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una transaccion de la tabla para eliminar");
            return;
        }

        try {
            dao.eliminar(seleccionada.getId());
            if (transaccionEnEdicion != null && transaccionEnEdicion.getId() == seleccionada.getId()) {
                cancelarEdicion();
            }
            cargarDatos();
        } catch (SQLException e) {
            mostrarAlerta("Error al eliminar: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            datosTabla.setAll(dao.listarTodas());
            double balance = dao.calcularBalance();
            labelBalance.setText(String.format("Balance: $%.2f", balance));
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar datos: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        comboTipo.setValue(null);
        campoMonto.clear();
        comboCategoria.getItems().clear();
        comboCategoria.setValue(null);
        campoFecha.setValue(LocalDate.now());
        campoDescripcion.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING, mensaje);
        alerta.showAndWait();
    }
}
