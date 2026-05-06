package kontrola;

import DatuBasea.ErosketaDB;
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
import javafx.scene.input.MouseButton;
import model.Erosketa;

import java.util.Optional;

public class ErosketakController {

    @FXML private TableView<Erosketa> table;
    @FXML private TableColumn<Erosketa, String> colHornitzailea;
    @FXML private TableColumn<Erosketa, String> colElementua;
    @FXML private TableColumn<Erosketa, Integer> colKantitatea;
    @FXML private TableColumn<Erosketa, Double> colPrezioa;
    @FXML private TextField txtBuscar;

    @FXML private Button btnAdd;
    @FXML private Button btnDelete;

    private ObservableList<Erosketa> masterData;
    private FilteredList<Erosketa> filteredData;

    @FXML
    public void initialize() {
        colHornitzailea.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getHornitzaileIzena()));
        colElementua.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getErosketaElementua()));
        colKantitatea.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getKantitatea()));
        colPrezioa.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrezioa()));

        masterData = FXCollections.observableArrayList(ErosketaDB.lortuGuztiak());
        filteredData = new FilteredList<>(masterData, p -> true);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(erosketa -> {
                if (newVal == null || newVal.isBlank()) return true;
                String lower = newVal.toLowerCase();
                return (erosketa.getHornitzaileIzena() != null && erosketa.getHornitzaileIzena().toLowerCase().contains(lower))
                        || (erosketa.getErosketaElementua() != null && erosketa.getErosketaElementua().toLowerCase().contains(lower));
            });
        });

        SortedList<Erosketa> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        table.setRowFactory(tv -> {
            TableRow<Erosketa> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    new Alert(Alert.AlertType.INFORMATION, "Erosketa editatzea ez dago inplementatuta. Ezabatu eta berriro sortu.").showAndWait();
                }
            });
            return row;
        });

        btnDelete.setDisable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> btnDelete.setDisable(newSel == null));
    }

    @FXML
    private void gehituErosketa() {
        Erosketa erosketa = ErosketaFormController.openForm();
        if (erosketa != null) {
            if (ErosketaDB.insert(erosketa)) {
                masterData.setAll(ErosketaDB.lortuGuztiak());
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da erosketa gorde.").showAndWait();
            }
        }
    }

    @FXML
    private void ezabatuErosketa() {
        Erosketa seleccionado = table.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Mesedez, hautatu erosketa ezabatzeko.").showAndWait();
            return;
        }

        Optional<javafx.scene.control.ButtonType> result = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Ziur zaude erosketa hau ezabatu nahi duzula? (stock-a ere eguneratuko da)"
        ).showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            if (ErosketaDB.delete(seleccionado.getId())) {
                masterData.setAll(ErosketaDB.lortuGuztiak());
            } else {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da erosketa ezabatu.").showAndWait();
            }
        }
    }
}
