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

import java.util.Optional;

public class ErabiltzaileakController {

    // ===================== FXML =====================

    @FXML
    private TableView<Erabiltzailea> erabiltzaileTable;

    @FXML
    private TableColumn<Erabiltzailea, Integer> colId;
    @FXML
    private TableColumn<Erabiltzailea, String> colErabiltzailea;
    @FXML
    private TableColumn<Erabiltzailea, String> colEmail;
    @FXML
    private TableColumn<Erabiltzailea, String> colPasahitza;
    @FXML
    private TableColumn<Erabiltzailea, Integer> colRola;
    @FXML
    private TableColumn<Erabiltzailea, Boolean> colEzabatua;
    @FXML
    private TableColumn<Erabiltzailea, Boolean> colChat;

    @FXML
    private ComboBox<String> cmbFiltro;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnAdd, btnEdit, btnDelete;

    // ===================== DATUAK =====================

    private final ErabiltzaileakDB erabiltzaileakDB = new ErabiltzaileakDB();

    private ObservableList<Erabiltzailea> erabiltzaileak;
    private FilteredList<Erabiltzailea> erabiltzaileakFiltratuak;

    // ===================== HASIERATZEA =====================

    @FXML
    private void initialize() {

        // Taulako zutabeak
        colErabiltzailea.setCellValueFactory(new PropertyValueFactory<>("erabiltzailea"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPasahitza.setCellValueFactory(new PropertyValueFactory<>("pasahitza"));
        colRola.setCellValueFactory(new PropertyValueFactory<>("rolaId"));
        colEzabatua.setCellValueFactory(new PropertyValueFactory<>("ezabatua"));
        colChat.setCellValueFactory(new PropertyValueFactory<>("chat"));

        // Filtroa
        cmbFiltro.getItems().addAll("Aktiboak", "Borratuak", "Dena");
        cmbFiltro.setValue("Aktiboak");

        // Datuak kargatu
        erabiltzaileak = FXCollections.observableArrayList(erabiltzaileakDB.getAll());
        erabiltzaileakFiltratuak = new FilteredList<>(erabiltzaileak);
        erabiltzaileTable.setItems(erabiltzaileakFiltratuak);

        // Filtroa aldatu denean
        cmbFiltro.setOnAction(e -> {
            aplikatuFiltro();
            botoiakEguneratu();
        });

        // TextField bilaketa
        txtBuscar.textProperty().addListener((obs, zaharra, berria) -> {
            aplikatuFiltro();
        });

        // Aukeraketa aldatu denean
        erabiltzaileTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, zaharra, berria) -> botoiakEguneratu());

        // Hasierako egoera
        aplikatuFiltro();
        botoiakEguneratu();
    }

    // ===================== FILTROA =====================

    @FXML
    private void aplikatuFiltro() {
        String aukera = cmbFiltro.getValue();
        String bilaketa = txtBuscar.getText() == null ? "" : txtBuscar.getText().toLowerCase().trim();

        erabiltzaileakFiltratuak.setPredicate(e -> {
            // Filtratu status
            boolean statusMatch;
            switch (aukera) {
                case "Aktiboak":
                    statusMatch = !e.isEzabatua();
                    break;
                case "Borratuak":
                    statusMatch = e.isEzabatua();
                    break;
                default:
                    statusMatch = true;
            }

            // Filtratu bilaketa (erabiltzailea edo emaila)
            boolean searchMatch = e.getErabiltzailea().toLowerCase().contains(bilaketa)
                    || e.getEmail().toLowerCase().contains(bilaketa);

            return statusMatch && searchMatch;
        });
    }

    // ===================== BOTOIAK =====================

    private void botoiakEguneratu() {

        String filtroa = cmbFiltro.getValue();
        Erabiltzailea aukeratua = erabiltzaileTable.getSelectionModel().getSelectedItem();

        btnAdd.setDisable(true);
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        if (filtroa == null) return;

        switch (filtroa) {
            case "Aktiboak":
                btnAdd.setDisable(false);
                btnEdit.setDisable(aukeratua == null);
                btnDelete.setDisable(aukeratua == null);
                btnDelete.setText("Ezabatu");
                break;

            case "Borratuak":
                btnEdit.setDisable(aukeratua == null);
                btnDelete.setDisable(aukeratua == null);
                btnDelete.setText("Berreskuratu");
                break;

            case "Dena":
                btnEdit.setDisable(aukeratua == null);
                btnDelete.setDisable(aukeratua == null);

                if (aukeratua != null && aukeratua.isEzabatua()) {
                    btnDelete.setText("Berreskuratu");
                } else {
                    btnDelete.setText("Ezabatu");
                }
                break;
        }
    }

    // ===================== EKINTZAK =====================

    @FXML
    private void gehituErabiltzailea() {
        Erabiltzailea berria = mostrarDialogo(null);
        if (berria != null && erabiltzaileakDB.insert(berria)) {
            freskatu();
        }
    }

    @FXML
    private void editatuErabiltzailea() {
        Erabiltzailea aukeratua = erabiltzaileTable.getSelectionModel().getSelectedItem();
        if (aukeratua == null) {
            alerta("Errorea", "Hautatu erabiltzaile bat");
            return;
        }

        Erabiltzailea editatua = mostrarDialogo(aukeratua);
        if (editatua != null && erabiltzaileakDB.update(editatua)) {
            freskatu();
        }
    }

    @FXML
    private void ezabatuErabiltzailea() {

        Erabiltzailea aukeratua = erabiltzaileTable.getSelectionModel().getSelectedItem();
        if (aukeratua == null) return;

        boolean berreskuratu = aukeratua.isEzabatua();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Baieztatu");
        confirm.setHeaderText(null);
        confirm.setContentText(
                berreskuratu
                        ? "Ziur erabiltzailea berreskuratu nahi duzu?"
                        : "Ziur erabiltzailea ezabatu nahi duzu?"
        );

        Optional<ButtonType> erantzuna = confirm.showAndWait();
        if (erantzuna.isEmpty() || erantzuna.get() != ButtonType.OK) return;

        if (berreskuratu) {
            erabiltzaileakDB.berreskuratu(aukeratua.getId());
        } else {
            erabiltzaileakDB.delete(aukeratua.getId());
        }

        freskatu();
    }

    // ===================== LAGUNTZA =====================

    private void freskatu() {
        erabiltzaileak.setAll(erabiltzaileakDB.getAll());
        aplikatuFiltro();
        botoiakEguneratu();
    }

    private Erabiltzailea mostrarDialogo(Erabiltzailea e) {

        Dialog<Erabiltzailea> dialog = new Dialog<>();
        dialog.setTitle(e == null ? "Erabiltzailea gehitu" : "Erabiltzailea editatu");

        ButtonType saveBtn = new ButtonType("Gorde", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField txtErabiltzailea = new TextField();
        TextField txtEmail = new TextField();
        PasswordField txtPasahitza = new PasswordField();
        TextField txtRola = new TextField();
        CheckBox chkChat = new CheckBox("Chat");

        if (e != null) {
            txtErabiltzailea.setText(e.getErabiltzailea());
            txtEmail.setText(e.getEmail());
            txtPasahitza.setText(e.getPasahitza());
            txtRola.setText(String.valueOf(e.getRolaId()));
            chkChat.setSelected(e.isChat());
        }

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.addRow(0, new Label("Erabiltzailea:"), txtErabiltzailea);
        grid.addRow(1, new Label("Email:"), txtEmail);
        grid.addRow(2, new Label("Pasahitza:"), txtPasahitza);
        grid.addRow(3, new Label("Rola ID:"), txtRola);
        grid.addRow(4, chkChat);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                Erabiltzailea u = (e == null) ? new Erabiltzailea() : e;
                u.setErabiltzailea(txtErabiltzailea.getText());
                u.setEmail(txtEmail.getText());
                u.setPasahitza(txtPasahitza.getText());
                u.setRolaId(Integer.parseInt(txtRola.getText()));
                u.setChat(chkChat.isSelected());
                return u;
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private void alerta(String titulua, String mezua) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titulua);
        a.setHeaderText(null);
        a.setContentText(mezua);
        a.showAndWait();
    }
}
