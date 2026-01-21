package kontrola;

import DatuBasea.KategoriakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Kategoria;

public class KategoriakController {

    @FXML private TableView<Kategoria> kategoriakTable;
    @FXML private TableColumn<Kategoria, String> colIzena;

    @FXML private Button btnAdd, btnEdit, btnDelete;

    private ObservableList<Kategoria> kategoriak;

    @FXML
    public void initialize() {
        
        colIzena.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIzena()));

        cargarDatos();

        
        kategoriakTable.setRowFactory(tv -> {
            TableRow<Kategoria> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Kategoria clicked = row.getItem();
                    abrirFormulario(clicked);
                }
            });
            return row;
        });

        
        kategoriakTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, val) -> actualizarBotones());

        actualizarBotones(); 
    }

    private void cargarDatos() {
        kategoriak = FXCollections.observableArrayList(KategoriakDB.lortuKategoriak());
        kategoriakTable.setItems(kategoriak);
    }

    private void actualizarBotones() {
        Kategoria sel = kategoriakTable.getSelectionModel().getSelectedItem();
        btnEdit.setDisable(sel == null);
        btnDelete.setDisable(sel == null);
    }

    @FXML
    private void gehituKategoria() {
        abrirFormulario(null);
    }

    @FXML
    private void editatuKategoria() {
        Kategoria k = kategoriakTable.getSelectionModel().getSelectedItem();
        if (k != null) abrirFormulario(k);
        else alerta("Hautatu kategoria bat editatzeko.");
    }

    @FXML
    private void ezabatuKategoria() {
        Kategoria k = kategoriakTable.getSelectionModel().getSelectedItem();
        if (k == null) {
            alerta("Hautatu kategoria bat ezabatzeko.");
            return;
        }

        if (KategoriakDB.dagoProdukturik(k.getId())) {
            alerta("Errorea: Kategoria honek produktuak ditu, ezin da ezabatu.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Ziur zaude kategoria hau ezabatu nahi duzula?",
                ButtonType.YES, ButtonType.NO
        );

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            boolean eliminado = KategoriakDB.ezabatuKategoria(k.getId());
            if (eliminado) cargarDatos();
            else alerta("Errorea: Ezin izan da kategoria ezabatu.");
        }
    }

    private void abrirFormulario(Kategoria kategoria) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/KategoriaForm.fxml"));
            Parent root = loader.load();

            KategoriaFormController controller = loader.getController();
            controller.setKategoria(kategoria);

            Stage stage = new Stage();
            stage.setTitle(kategoria == null ? "Gehitu Kategoria" : "Editatu Kategoria");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea: ezin izan da formularioa ireki.");
        }
    }

    private void alerta(String mezua) {
        new Alert(Alert.AlertType.WARNING, mezua).showAndWait();
    }
}
