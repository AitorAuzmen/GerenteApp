package kontrola;

import DatuBasea.OsagaiakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Osagaiak;

import java.util.Optional;

public class OsagaiakController {

    @FXML private TableView<Osagaiak> table;
    @FXML private TableColumn<Osagaiak, String> colIzena;
    @FXML private TableColumn<Osagaiak, String> colUnitatea;
    @FXML private TableColumn<Osagaiak, Double> colStock;
    @FXML private TextField txtBuscar;

    private ObservableList<Osagaiak> masterData;
    private FilteredList<Osagaiak> filteredData;

    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

    @FXML
    public void initialize() {
        
        colIzena.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIzena()));
        colUnitatea.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUnitatea()));
        colStock.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getStock_aktuala()));

        
        masterData = FXCollections.observableArrayList(OsagaiakDB.lortuGuztiak());
        filteredData = new FilteredList<>(masterData, p -> true);

        
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(osagaia -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return osagaia.getIzena().toLowerCase().contains(lower);
            });
        });

        
        SortedList<Osagaiak> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        
        table.setRowFactory(tv -> {
            TableRow<Osagaiak> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    editatuOsagaia();
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
    private void gehituOsagaia() {
        Osagaiak o = OsagaiakFormController.openForm(null);
        if (o != null) {
            OsagaiakDB.insert(o);
            masterData.add(o);
        }
    }

    @FXML
    private void editatuOsagaia() {
        Osagaiak seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Osagaiak o = OsagaiakFormController.openForm(seleccionado);
            if (o != null) {
                OsagaiakDB.update(o);
                table.refresh();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Mesedez, hautatu osagaia editatzeko.").showAndWait();
        }
    }

    @FXML
    private void ezabatuOsagaia() {
        Osagaiak seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Ziur zaude ezabatu nahi duzula?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                OsagaiakDB.delete(seleccionado.getId());
                masterData.remove(seleccionado);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Mesedez, hautatu osagaia ezabatzeko.").showAndWait();
        }
    }
}
