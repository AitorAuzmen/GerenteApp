
package kontrola;

import DatuBasea.ErabiltzaileakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Erabiltzailea;

public class ErabiltzaileakController {

    @FXML private TableView<Erabiltzailea> erabiltzaileTable;
    @FXML private TableColumn<Erabiltzailea, String> colErabiltzailea;
    @FXML private TableColumn<Erabiltzailea, String> colEmail;
    @FXML private TableColumn<Erabiltzailea, String> colPasahitza;
    @FXML private TableColumn<Erabiltzailea, Boolean> colEzabatua;
    @FXML private TableColumn<Erabiltzailea, Boolean> colChat;

    @FXML private ComboBox<String> cmbFiltro;
    @FXML private ComboBox<String> cmbRola; 
    @FXML private TextField txtBuscar;
    @FXML private Button btnAdd, btnEdit, btnDelete;

    private final ErabiltzaileakDB erabiltzaileakDB = new ErabiltzaileakDB();
    private ObservableList<Erabiltzailea> erabiltzaileak;
    private FilteredList<Erabiltzailea> erabiltzaileakFiltratuak;

    @FXML
    private void initialize() {

        
        colErabiltzailea.setCellValueFactory(new PropertyValueFactory<>("erabiltzailea"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPasahitza.setCellValueFactory(new PropertyValueFactory<>("pasahitza"));
        colEzabatua.setCellValueFactory(new PropertyValueFactory<>("ezabatua"));
        colChat.setCellValueFactory(new PropertyValueFactory<>("chat"));

        
        colEzabatua.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Boolean v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : (v ? "✔" : "✖"));
                setStyle("-fx-alignment: CENTER;");
            }
        });

        colChat.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Boolean v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : (v ? "✔" : "✖"));
                setStyle("-fx-alignment: CENTER;");
            }
        });

        
        cmbFiltro.getItems().addAll("Aktiboak", "Borratuak", "Dena");
        cmbFiltro.setValue("Aktiboak");

        
        cmbRola.getItems().addAll("Denak", "Admin", "Langilea");
        cmbRola.setValue("Denak");

        
        erabiltzaileak = FXCollections.observableArrayList(erabiltzaileakDB.getAll());
        erabiltzaileakFiltratuak = new FilteredList<>(erabiltzaileak);
        erabiltzaileTable.setItems(erabiltzaileakFiltratuak);

        
        cmbFiltro.setOnAction(e -> {
            aplikatuFiltro();
            botoiakEguneratu();
        });

        cmbRola.setOnAction(e -> aplikatuFiltro()); 

        txtBuscar.textProperty().addListener((obs, old, val) -> aplikatuFiltro());

        erabiltzaileTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, val) -> botoiakEguneratu());

        
        erabiltzaileTable.setRowFactory(tv -> {
            TableRow<Erabiltzailea> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    erabiltzaileTable.getSelectionModel().select(row.getItem());
                    editatuErabiltzailea();
                }
            });
            return row;
        });

        aplikatuFiltro();
        botoiakEguneratu();
    }

    
    private void aplikatuFiltro() {
        String filtro = cmbFiltro.getValue();
        String rolFiltro = cmbRola.getValue();
        String bilaketa = txtBuscar.getText() == null ? "" : txtBuscar.getText().toLowerCase();

        erabiltzaileakFiltratuak.setPredicate(e -> {
            
            boolean status;
            if ("Aktiboak".equals(filtro)) status = !e.isEzabatua();
            else if ("Borratuak".equals(filtro)) status = e.isEzabatua();
            else status = true;

            
            boolean text = e.getErabiltzailea().toLowerCase().contains(bilaketa)
                    || e.getEmail().toLowerCase().contains(bilaketa);

            
            boolean rol = true;
            if (!"Denak".equals(rolFiltro)) {
                rol = e.getRola().equals(rolFiltro); 
            }

            return status && text && rol;
        });
    }

    private void botoiakEguneratu() {
        String filtro = cmbFiltro.getValue();
        Erabiltzailea sel = erabiltzaileTable.getSelectionModel().getSelectedItem();

        btnAdd.setDisable(true);
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        if (filtro == null) return;

        switch (filtro) {
            case "Aktiboak":
                btnAdd.setDisable(false);
                btnEdit.setDisable(sel == null);
                btnDelete.setDisable(sel == null);
                btnDelete.setText("Ezabatu");
                break;

            case "Borratuak":
                btnEdit.setDisable(sel == null);
                btnDelete.setDisable(sel == null);
                btnDelete.setText("Berreskuratu");
                break;

            case "Dena":
                btnEdit.setDisable(sel == null);
                btnDelete.setDisable(sel == null);
                btnDelete.setText(sel != null && sel.isEzabatua() ? "Berreskuratu" : "Ezabatu");
                break;
        }
    }

    @FXML private void gehituErabiltzailea() {
        Erabiltzailea e = dialog(null);
        if (e != null && erabiltzaileakDB.insert(e)) freskatu();
    }

    @FXML private void editatuErabiltzailea() {
        Erabiltzailea sel = erabiltzaileTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Erabiltzailea e = dialog(sel);
        if (e != null && erabiltzaileakDB.update(e)) freskatu();
    }

    @FXML private void ezabatuErabiltzailea() {
        Erabiltzailea sel = erabiltzaileTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        boolean rec = sel.isEzabatua();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                rec ? "Berreskuratu erabiltzailea?" : "Ezabatu erabiltzailea?");
        if (a.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        if (rec) erabiltzaileakDB.berreskuratu(sel.getId());
        else erabiltzaileakDB.delete(sel.getId());

        freskatu();
    }

    private void freskatu() {
        erabiltzaileak.setAll(erabiltzaileakDB.getAll());
        aplikatuFiltro();
        botoiakEguneratu();
    }

    private Erabiltzailea dialog(Erabiltzailea e) {
        Dialog<Erabiltzailea> d = new Dialog<>();
        d.setTitle(e == null ? "Gehitu" : "Editatu");

        ButtonType save = new ButtonType("Gorde", ButtonBar.ButtonData.OK_DONE);
        d.getDialogPane().getButtonTypes().addAll(save, ButtonType.CANCEL);

        TextField t1 = new TextField(), t2 = new TextField();
        CheckBox chk = new CheckBox("Chat");

        if (e != null) {
            t1.setText(e.getErabiltzailea());
            t2.setText(e.getEmail());
            chk.setSelected(e.isChat());
        }

        GridPane g = new GridPane();
        g.setVgap(10); g.setHgap(10);
        g.addRow(0, new Label("Erabiltzailea:"), t1);
        g.addRow(1, new Label("Email:"), t2);
        g.addRow(2, chk);

        d.getDialogPane().setContent(g);

        d.setResultConverter(btn -> {
            if (btn == save) {
                Erabiltzailea u = e == null ? new Erabiltzailea() : e;
                u.setErabiltzailea(t1.getText());
                u.setEmail(t2.getText());
                u.setChat(chk.isSelected());
                return u;
            }
            return null;
        });

        return d.showAndWait().orElse(null);
    }
}
