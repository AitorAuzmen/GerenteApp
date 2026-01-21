package kontrola;

import DatuBasea.RolakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import model.Rolak;

import java.util.List;

public class RolakController {

    @FXML
    private TableView<Rolak> rolakTable;
    @FXML
    private TableColumn<Rolak, String> colIzena;
    @FXML
    private TextField txtBuscar;

    private ObservableList<Rolak> rolakList;
    private FilteredList<Rolak> rolakFiltratuak;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    @FXML
    public void initialize() {
        
        colIzena.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIzena()));

        
        List<Rolak> lista = RolakDB.lortuGuztiak();
        rolakList = FXCollections.observableArrayList(lista);
        rolakFiltratuak = new FilteredList<>(rolakList, p -> true);
        rolakTable.setItems(rolakFiltratuak);

        
        txtBuscar.textProperty().addListener((obs, oldV, newV) -> aplikatuFiltro());

        
        rolakTable.setRowFactory(tv -> {
            TableRow<Rolak> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    editatuRol();
                }
            });
            return row;
        });

        
        btnEdit.disableProperty().bind(rolakTable.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(rolakTable.getSelectionModel().selectedItemProperty().isNull());
    }


    private void aplikatuFiltro() {
        String bilaketa = txtBuscar.getText() == null ? "" : txtBuscar.getText().toLowerCase();

        rolakFiltratuak.setPredicate(r -> {
            return r.getIzena().toLowerCase().contains(bilaketa);
        });
    }

    @FXML
    private void gehituRol() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gehitu Rol");
        dialog.setHeaderText("Sartu rolaren izena:");
        dialog.setContentText("Izena:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                Rolak r = new Rolak();
                r.setIzena(name);
                if (RolakDB.insert(r)) {
                    rolakList.add(r);
                }
            }
        });
    }

    @FXML
    private void editatuRol() {
        Rolak selected = rolakTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog(selected.getIzena());
        dialog.setTitle("Editatu Rol");
        dialog.setHeaderText("Aldatu rolaren izena:");
        dialog.setContentText("Izena:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                selected.setIzena(name);
                if (RolakDB.update(selected)) {
                    rolakTable.refresh();
                }
            }
        });
    }

    @FXML
    private void ezabatuRol() {
        Rolak selected = rolakTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ezabatu Rol");
        alert.setHeaderText("Ziur zaude rol hau ezabatu nahi duzula?");
        alert.setContentText(selected.getIzena());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (RolakDB.delete(selected.getId())) {
                    rolakList.remove(selected);
                }
            }
        });
    }
}
