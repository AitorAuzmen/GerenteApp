package kontrola;

import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Mahaia;

public class MahaiakController {

    @FXML private TableView<Mahaia> mahaiakTable;
    @FXML private TableColumn<Mahaia, String> colZenbakia;
    @FXML private TableColumn<Mahaia, String> colKapazitatea;
    @FXML private TableColumn<Mahaia, String> colEgoera;
    @FXML private Button btnAdd, btnEdit, btnDelete;

    private ObservableList<Mahaia> mahaiak;

    @FXML
    public void initialize() {
        colZenbakia.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getZenbakia())));
        colKapazitatea.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getKapazitatea())));
        colEgoera.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEgoera()));

        kargatuDatuak();

        mahaiakTable.setRowFactory(tv -> {
            TableRow<Mahaia> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Mahaia clicked = row.getItem();
                    irekiFormularioa(clicked);
                }
            });
            return row;
        });

        mahaiakTable.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> botoiakAktualizatu());
        botoiakAktualizatu();
    }

    private void kargatuDatuak() {
        mahaiak = FXCollections.observableArrayList(MahaiakDB.lortuMahaiak());
        mahaiakTable.setItems(mahaiak);
    }

    private void botoiakAktualizatu() {
        Mahaia sel = mahaiakTable.getSelectionModel().getSelectedItem();
        btnEdit.setDisable(sel == null);
        btnDelete.setDisable(sel == null);
    }

    @FXML
    private void gehituMahai() { irekiFormularioa(null); }

    @FXML
    private void editatuMahai() {
        Mahaia m = mahaiakTable.getSelectionModel().getSelectedItem();
        if (m != null) irekiFormularioa(m);
        else alerta("Hautatu mahai bat editatzeko.");
    }

    @FXML
    private void ezabatuMahai() {
        Mahaia m = mahaiakTable.getSelectionModel().getSelectedItem();
        if (m == null) { alerta("Hautatu mahai bat ezabatzeko."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Ziur zaude mahai hau ezabatu nahi duzula?", ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            boolean deleted = MahaiakDB.ezabatuMahai(m.getId());
            if (deleted) kargatuDatuak();
            else alerta("Errorea: ezin izan da mahai ezabatu.");
        }
    }

    private void irekiFormularioa(Mahaia m) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MahaiaForm.fxml"));
            Parent root = loader.load();

            MahaiaFormController controller = loader.getController();
            controller.setMahai(m);

            Stage stage = new Stage();
            stage.setTitle(m == null ? "Mahaia gehitu" : "Mahaia editatu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            kargatuDatuak();
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea: ezin izan da formularioa ireki.");
        }
    }

    private void alerta(String mezua) {
        new Alert(Alert.AlertType.WARNING, mezua).showAndWait();
    }
}
