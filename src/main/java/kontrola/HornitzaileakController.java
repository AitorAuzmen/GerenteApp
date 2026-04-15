package kontrola;

import DatuBasea.HornitzaileakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import model.Hornitzailea;

import java.util.Optional;

public class HornitzaileakController {

    @FXML private TableView<Hornitzailea> hornitzaileakTable;
    @FXML private TableColumn<Hornitzailea, String> colIzena;
    @FXML private TableColumn<Hornitzailea, String> colKontaktua;
    @FXML private TableColumn<Hornitzailea, String> colHelbidea;
    @FXML private TextField txtBuscar;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

    private ObservableList<Hornitzailea> hornitzaileak;
    private FilteredList<Hornitzailea> filtratua;

    @FXML
    public void initialize() {
        colIzena.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIzena()));
        colKontaktua.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getKontaktua()));
        colHelbidea.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getHelbidea()));

        hornitzaileak = FXCollections.observableArrayList(HornitzaileakDB.lortuGuztiak());
        filtratua = new FilteredList<>(hornitzaileak, p -> true);
        hornitzaileakTable.setItems(filtratua);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> aplikatuFiltro());

        hornitzaileakTable.setRowFactory(tv -> {
            TableRow<Hornitzailea> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    editatuHornitzailea();
                }
            });
            return row;
        });

        btnEdit.disableProperty().bind(hornitzaileakTable.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(hornitzaileakTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void aplikatuFiltro() {
        String bilaketa = txtBuscar.getText() == null ? "" : txtBuscar.getText().toLowerCase().trim();

        filtratua.setPredicate(hornitzailea -> {
            if (bilaketa.isEmpty()) {
                return true;
            }

            return hornitzailea.getIzena().toLowerCase().contains(bilaketa)
                    || hornitzailea.getKontaktua().toLowerCase().contains(bilaketa)
                    || hornitzailea.getHelbidea().toLowerCase().contains(bilaketa);
        });
    }

    @FXML
    private void gehituHornitzailea() {
        Hornitzailea hornitzailea = HornitzaileFormController.openForm(null);
        if (hornitzailea != null && HornitzaileakDB.insert(hornitzailea)) {
            berritu();
        }
    }

    @FXML
    private void editatuHornitzailea() {
        Hornitzailea selected = hornitzaileakTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Hornitzailea emaitza = HornitzaileFormController.openForm(selected);
        if (emaitza != null && HornitzaileakDB.update(emaitza)) {
            berritu();
        }
    }

    @FXML
    private void ezabatuHornitzailea() {
        Hornitzailea selected = hornitzaileakTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        if (HornitzaileakDB.dagoErabilita(selected.getId())) {
            new Alert(Alert.AlertType.WARNING, "Hornitzaile hau ezin da ezabatu, osagai edo materialekin lotuta dagoelako.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Hornitzailea ezabatu");
        confirm.setHeaderText("Ziur zaude hornitzaile hau ezabatu nahi duzula?");
        confirm.setContentText(selected.getIzena());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && HornitzaileakDB.delete(selected.getId())) {
            berritu();
        }
    }

    private void berritu() {
        hornitzaileak.setAll(HornitzaileakDB.lortuGuztiak());
    }
}