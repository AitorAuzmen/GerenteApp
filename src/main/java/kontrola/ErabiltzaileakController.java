
package kontrola;

import DatuBasea.ErabiltzaileakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import DatuBasea.RolakDB;
import model.Rolak;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Erabiltzailea;

public class ErabiltzaileakController {

    @FXML private TableView<Erabiltzailea> erabiltzaileTable;
    @FXML private TableColumn<Erabiltzailea, String> colIzena;
    @FXML private TableColumn<Erabiltzailea, String> colAbizena;
    @FXML private TableColumn<Erabiltzailea, String> colErabiltzailea;
    @FXML private TableColumn<Erabiltzailea, Integer> colLangileKodea;
    @FXML private TableColumn<Erabiltzailea, String> colRola;
    @FXML private TableColumn<Erabiltzailea, String> colPasahitza;
    @FXML private TableColumn<Erabiltzailea, Boolean> colEzabatua;
    @FXML private TableColumn<Erabiltzailea, Boolean> colChat;

    @FXML private ComboBox<String> cmbFiltro;
    @FXML private ComboBox<Rolak> cmbRola; 
    @FXML private TextField txtBuscar;
    @FXML private Button btnAdd, btnEdit, btnDelete;

    private final ErabiltzaileakDB erabiltzaileakDB = new ErabiltzaileakDB();
    private ObservableList<Erabiltzailea> erabiltzaileak;
    private FilteredList<Erabiltzailea> erabiltzaileakFiltratuak;

    @FXML
    private void initialize() {

        colIzena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        colAbizena.setCellValueFactory(new PropertyValueFactory<>("abizena"));
        colErabiltzailea.setCellValueFactory(new PropertyValueFactory<>("erabiltzailea"));
        colLangileKodea.setCellValueFactory(new PropertyValueFactory<>("langileKodea"));
        colRola.setCellValueFactory(new PropertyValueFactory<>("rola"));
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
            private final ToggleButton toggle = new ToggleButton();

            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setAlignment(Pos.CENTER);
                toggle.getStyleClass().add("chat-toggle");
                toggle.setMinWidth(54);
                toggle.setPrefWidth(54);
                toggle.setFocusTraversable(false);
                toggle.setOnAction(event -> {
                    Erabiltzailea erabiltzailea = getTableRow() == null ? null : getTableRow().getItem();
                    if (erabiltzailea == null) {
                        return;
                    }

                    boolean egoeraBerria = toggle.isSelected();
                    if (erabiltzaileakDB.eguneratuChatEgoera(erabiltzailea.getId(), egoeraBerria)) {
                        erabiltzailea.setChat(egoeraBerria);
                    } else {
                        toggle.setSelected(!egoeraBerria);
                        new Alert(Alert.AlertType.ERROR, "Ezin izan da chat egoera eguneratu.").show();
                    }
                    eguneratuChatToggleTestua(toggle, toggle.isSelected());
                });
            }

            @Override
            protected void updateItem(Boolean v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null || v == null) {
                    setGraphic(null);
                    return;
                }

                Erabiltzailea erabiltzailea = getTableRow().getItem();
                toggle.setDisable(erabiltzailea.isEzabatua());
                toggle.setSelected(v);
                eguneratuChatToggleTestua(toggle, v);
                setGraphic(toggle);
            }
        });

        
        cmbFiltro.getItems().addAll("Aktiboak", "Borratuak", "Dena");

        cmbFiltro.setValue("Aktiboak");

        cmbRola.getItems().clear();
        cmbRola.getItems().add(null); // "Denak" aukera
        for (Rolak r : RolakDB.lortuGuztiak()) {
            cmbRola.getItems().add(r);
        }
        cmbRola.setPromptText("Denak");
        cmbRola.setValue(null);

        
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
        Rolak rolFiltro = cmbRola.getValue();
        String bilaketa = txtBuscar.getText() == null ? "" : txtBuscar.getText().toLowerCase();

        erabiltzaileakFiltratuak.setPredicate(e -> {
            boolean status;
            if ("Aktiboak".equals(filtro)) status = !e.isEzabatua();
            else if ("Borratuak".equals(filtro)) status = e.isEzabatua();
            else status = true;

            boolean text = e.getErabiltzailea().toLowerCase().contains(bilaketa)
                    || e.getIzena().toLowerCase().contains(bilaketa)
                    || e.getAbizena().toLowerCase().contains(bilaketa);

            boolean rol = true;
            if (rolFiltro != null) {
                rol = e.getRolaId() == rolFiltro.getId();
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

    private void eguneratuChatToggleTestua(ToggleButton toggle, boolean aktibo) {
        toggle.setText(aktibo ? "ON" : "OFF");
    }

    private Erabiltzailea dialog(Erabiltzailea e) {
        Dialog<Erabiltzailea> d = new Dialog<>();
        d.setTitle(e == null ? "Gehitu erabiltzailea" : "Editatu erabiltzailea");

        ButtonType save = new ButtonType("Gorde", ButtonBar.ButtonData.OK_DONE);
        d.getDialogPane().getButtonTypes().addAll(save, ButtonType.CANCEL);

        TextField tIzena = new TextField();
        TextField tAbizena = new TextField();
        TextField t1 = new TextField();
        TextField tKodea = new TextField();
        PasswordField t3 = new PasswordField();
        ComboBox<Rolak> cmbRol = new ComboBox<>();
        CheckBox chk = new CheckBox("Txata aktibatu");

        cmbRol.getItems().addAll(RolakDB.lortuGuztiak());
        cmbRol.setPromptText("Aukeratu rola");

        if (e != null) {
            tIzena.setText(e.getIzena());
            tAbizena.setText(e.getAbizena());
            t1.setText(e.getErabiltzailea());
            tKodea.setText(String.valueOf(e.getLangileKodea()));
            t3.setText(e.getPasahitza());
            chk.setSelected(e.isChat());
            for (Rolak r : cmbRol.getItems()) {
                if (r.getId() == e.getRolaId()) {
                    cmbRol.setValue(r);
                    break;
                }
            }
        }

        GridPane g = new GridPane();
        g.setVgap(10); g.setHgap(10);
        g.addRow(0, new Label("Izena:"), tIzena);
        g.addRow(1, new Label("Abizena:"), tAbizena);
        g.addRow(2, new Label("Erabiltzailea:"), t1);
        g.addRow(3, new Label("Langile kodea:"), tKodea);
        g.addRow(4, new Label("Pasahitza:"), t3);
        g.addRow(5, new Label("Rola:"), cmbRol);
        g.addRow(6, chk);

        d.getDialogPane().setContent(g);

        d.setResultConverter(btn -> {
            if (btn == save) {
                if (tIzena.getText().isBlank() || tAbizena.getText().isBlank()
                        || t1.getText().isBlank() || tKodea.getText().isBlank()
                        || t3.getText().isBlank() || cmbRol.getValue() == null) {
                    return null;
                }

                int kodea;
                try {
                    kodea = Integer.parseInt(tKodea.getText().trim());
                } catch (NumberFormatException ex) {
                    return null;
                }

                Erabiltzailea u = e == null ? new Erabiltzailea() : e;
                u.setIzena(tIzena.getText().trim());
                u.setAbizena(tAbizena.getText().trim());
                u.setErabiltzailea(t1.getText().trim());
                u.setLangileKodea(kodea);
                u.setPasahitza(t3.getText().trim());
                Rolak rol = cmbRol.getValue();
                u.setRolaId(rol != null ? rol.getId() : 0);
                u.setChat(chk.isSelected());
                return u;
            }
            return null;
        });

        return d.showAndWait().orElse(null);
    }
}
