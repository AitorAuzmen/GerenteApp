package kontrola;

import DatuBasea.HornitzaileakDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Hornitzailea;
import model.Osagaiak;

import java.io.IOException;
import java.util.List;

public class OsagaiakFormController {
    @FXML private TextField txtIzena;
    @FXML private TextField txtStock;
    @FXML private ComboBox<Hornitzailea> cmbHornitzailea;
    @FXML private Button btnGorde;
    @FXML private Button btnUtzi;

    private Osagaiak osagaia;

    @FXML
    private void initialize() {
        cmbHornitzailea.setConverter(new StringConverter<>() {
            @Override
            public String toString(Hornitzailea hornitzailea) {
                return hornitzailea == null ? "" : hornitzailea.getIzena();
            }

            @Override
            public Hornitzailea fromString(String string) {
                return null;
            }
        });

        List<Hornitzailea> hornitzaileak = HornitzaileakDB.lortuGuztiak();
        cmbHornitzailea.getItems().setAll(hornitzaileak);
        if (!hornitzaileak.isEmpty()) {
            cmbHornitzailea.getSelectionModel().selectFirst();
        }
    }

    public static Osagaiak openForm(Osagaiak o) {
        try {
            FXMLLoader loader = new FXMLLoader(OsagaiakFormController.class.getResource("/fxml/OsagaiForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle(o == null ? "Gehitu Osagaia" : "Editatu Osagaia");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);

            OsagaiakFormController controller = loader.getController();
            controller.setOsagaia(o);

            stage.showAndWait();
            return controller.getOsagaia();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setOsagaia(Osagaiak o) {
        this.osagaia = o;
        if (o != null) {
            txtIzena.setText(o.getIzena());
            txtStock.setText(String.valueOf(o.getStock()));
            hautatuHornitzailea(o.getHornitzaileId());
        }
    }

    public Osagaiak getOsagaia() {
        return osagaia;
    }

    @FXML
    private void gordeOsagaia() {
        if (osagaia == null) osagaia = new Osagaiak();

        Hornitzailea hornitzailea = cmbHornitzailea.getSelectionModel().getSelectedItem();
        if (hornitzailea == null) {
            new Alert(Alert.AlertType.WARNING, "Lehenengo hornitzaile bat sortu edo hautatu behar duzu.").showAndWait();
            return;
        }

        osagaia.setIzena(txtIzena.getText());
        try {
            osagaia.setStock(Double.parseDouble(txtStock.getText()));
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Stock zenbaki balioduna izan behar da.").showAndWait();
            return;
        }
        osagaia.setHornitzaileId(hornitzailea.getId());
        osagaia.setHornitzaileIzena(hornitzailea.getIzena());

        btnGorde.getScene().getWindow().hide();
    }

    @FXML
    private void itxi() {
        btnUtzi.getScene().getWindow().hide();
    }

    private void hautatuHornitzailea(int hornitzaileId) {
        if (hornitzaileId <= 0) {
            return;
        }

        for (Hornitzailea hornitzailea : cmbHornitzailea.getItems()) {
            if (hornitzailea.getId() == hornitzaileId) {
                cmbHornitzailea.getSelectionModel().select(hornitzailea);
                return;
            }
        }
    }
}
