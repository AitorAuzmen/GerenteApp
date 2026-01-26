package kontrola;

import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Mahaia;

public class MahaiaFormController {

    @FXML private TextField txtZenbakia;
    @FXML private TextField txtKapazitatea;
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
            txtKapazitatea.setText(String.valueOf(m.getKapazitatea()));
            cmbEgoera.setValue(m.getEgoera()); 
        }
    }

    @FXML
    private void gordeMahai() {
        String zenbStr = txtZenbakia.getText().trim();
        String kapStr = txtKapazitatea.getText().trim();
        String egoera = cmbEgoera.getValue();

        if (zenbStr.isEmpty() || kapStr.isEmpty() || egoera == null || egoera.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Sartu zenbakia, kapazitatea eta egoera.").showAndWait();
            return;
        }

        int zenbakia;
        int kapazitatea;
        try {
            zenbakia = Integer.parseInt(zenbStr);
            kapazitatea = Integer.parseInt(kapStr);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Zenbakia edo kapazitatea ez da baliozko zenbakia.").showAndWait();
            return;
        }

        if (editatzen == null) {
            int id = MahaiakDB.gehituMahai(new Mahaia(0, zenbakia, kapazitatea, egoera));
            if (id == -1) {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da mahai gorde.").showAndWait();
                return;
            }
        } else {
            editatzen.setZenbakia(zenbakia);
            editatzen.setKapazitatea(kapazitatea);
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
