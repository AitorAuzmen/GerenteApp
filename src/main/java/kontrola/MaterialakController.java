package kontrola;

import DatuBasea.MaterialakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Materiala;

import java.util.Optional;

public class MaterialakController {

    @FXML private TableView<Materiala> table;
    @FXML private TableColumn<Materiala, String> colIzena;
    @FXML private TableColumn<Materiala, Double> colPrezioa;
    @FXML private TableColumn<Materiala, Integer> colStock;
    @FXML private TableColumn<Materiala, String> colHornitzailea;
    @FXML private TextField txtBuscar;

    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

    private ObservableList<Materiala> masterData;
    private FilteredList<Materiala> filteredData;

    @FXML
    public void initialize() {
        colIzena.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIzena()));
        colPrezioa.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrezioa()));
        colStock.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getStock()));
        colHornitzailea.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getHornitzaileIzena()));

        masterData = FXCollections.observableArrayList(MaterialakDB.lortuGuztiak());
        filteredData = new FilteredList<>(masterData, p -> true);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(materiala -> {
                if (newVal == null || newVal.isBlank()) return true;
                String lower = newVal.toLowerCase();
                return (materiala.getIzena() != null && materiala.getIzena().toLowerCase().contains(lower))
                        || (materiala.getHornitzaileIzena() != null && materiala.getHornitzaileIzena().toLowerCase().contains(lower));
            });
        });

        SortedList<Materiala> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        table.setRowFactory(tv -> {
            TableRow<Materiala> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    editatuMateriala();
                }
            });
            return row;
        });

        btnEdit.setDisable(true);
        btnDelete.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean seleccionado = newSel != null;
            btnEdit.setDisable(!seleccionado);
            btnDelete.setDisable(!seleccionado);
        });
    }

    @FXML
    private void gehituMateriala() {
        Materiala materiala = MaterialaFormController.openForm(null);
        if (materiala != null) {
            if (MaterialakDB.insert(materiala)) {
                masterData.setAll(MaterialakDB.lortuGuztiak());
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da materiala gorde.").showAndWait();
            }
        }
    }

    @FXML
    private void editatuMateriala() {
        Materiala seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Mesedez, hautatu materiala editatzeko.").showAndWait();
            return;
        }

        Materiala materiala = MaterialaFormController.openForm(seleccionado);
        if (materiala != null) {
            if (MaterialakDB.update(materiala)) {
                masterData.setAll(MaterialakDB.lortuGuztiak());
                table.refresh();
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da materiala eguneratu.").showAndWait();
            }
        }
    }

    @FXML
    private void ezabatuMateriala() {
        Materiala seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Mesedez, hautatu materiala ezabatzeko.").showAndWait();
            return;
        }

        if (MaterialakDB.dagoErabilita(seleccionado.getId())) {
            new Alert(Alert.AlertType.WARNING, "Material hau erosketetan erabilita dago, ezin da ezabatu.").showAndWait();
            return;
        }

        Optional<javafx.scene.control.ButtonType> result = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Ziur zaude material hau ezabatu nahi duzula?"
        ).showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            if (MaterialakDB.delete(seleccionado.getId())) {
                masterData.setAll(MaterialakDB.lortuGuztiak());
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da materiala ezabatu.").showAndWait();
            }
        }
    }
}
