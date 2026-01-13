package kontrola;

import DatuBasea.KategoriakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Kategoria;

public class KategoriakController {

    @FXML private TextField txtIzena;
    @FXML private TableView<Kategoria> kategoriakTable;
    @FXML private TableColumn<Kategoria, Integer> colId;
    @FXML private TableColumn<Kategoria, String> colIzena;

    private ObservableList<Kategoria> kategoriak;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
        colIzena.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIzena()));

        kategoriak = FXCollections.observableArrayList(KategoriakDB.lortuKategoriak());
        kategoriakTable.setItems(kategoriak);
    }

    @FXML
    private void gehituKategoria() {
        String izena = txtIzena.getText().trim();
        if (!izena.isEmpty()) {
            Kategoria k = new Kategoria(0, izena);
            int id = KategoriakDB.gehituKategoria(k);
            if (id != -1) {
                k.setId(id);
                kategoriak.add(k);
                txtIzena.clear();
            }
        }
    }

    @FXML
    private void editatuKategoria() {
        Kategoria k = kategoriakTable.getSelectionModel().getSelectedItem();
        if (k != null) {
            String izenaBerria = txtIzena.getText().trim();
            if (!izenaBerria.isEmpty()) {
                k.setIzena(izenaBerria);
                KategoriakDB.eguneratuKategoria(k);
                kategoriakTable.refresh();
            }
        }
    }

    @FXML
    private void ezabatuKategoria() {
        Kategoria k = kategoriakTable.getSelectionModel().getSelectedItem();
        if (k != null) {
            KategoriakDB.ezabatuKategoria(k.getId());
            kategoriak.remove(k);
        }
    }
}
