package kontrola;

import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Mahaia;

public class MahaiaFormController {

    @FXML private TextField txtZenbakia;
    @FXML private ComboBox<String> cmbEgoera; 
    @FXML private Button btnGorde;
    @FXML private Button btnUtzi;

    private Mahaia editatzen;

    @FXML
    public void initialize() {
        
        cmbEgoera.setItems(FXCollections.observableArrayList(
                "libre", "okupatuta", "ordainketa_pendiente"
        ));
    }

    public void setMahai(Mahaia m) {
        this.editatzen = m;
        if (m != null) {
            txtZenbakia.setText(String.valueOf(m.getZenbakia()));
            cmbEgoera.setValue(m.getEgoera()); 
        }
    }

    @FXML
    private void gordeMahai() {
        String zenbStr = txtZenbakia.getText().trim();
        String egoera = cmbEgoera.getValue();

        if (zenbStr.isEmpty() || egoera == null || egoera.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Sartu zenbakia eta egoera.").showAndWait();
            return;
        }

        int zenbakia;
        try {
            zenbakia = Integer.parseInt(zenbStr);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Zenbakia osatua ez da baliozko zenbakia.").showAndWait();
            return;
        }

        if (editatzen == null) {
            int id = MahaiakDB.gehituMahai(new Mahaia(0, zenbakia, egoera));
            if (id == -1) {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da mahai gorde.").showAndWait();
                return;
            }
        } else {
            editatzen.setZenbakia(zenbakia);
            editatzen.setEgoera(egoera);
            MahaiakDB.eguneratuMahai(editatzen);
        }

        itxi();
    }

    @FXML
    private void itxi() {
        Stage stage = (Stage) txtZenbakia.getScene().getWindow();
        stage.close();
    }
}
