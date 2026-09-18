package controller;
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
    private DatePicker caompoFecha;
    private TextField campoDescripcion;
    
    //Tabla y label de balance
    private TableView<Transaccion> tabla;
    private Label labelBalance;
    
    public MainView(){
        setPadding(new Insets(15));
        setTop(crearFormulario());
        setCenter(crearTabla());
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
        btnGuardar.setOnAction(e -> gurardarTransaccion());
        
        grid.add(new Label("Tipo"), 0, 0);
        
        
        
        return grid;
    }
    
    
}
