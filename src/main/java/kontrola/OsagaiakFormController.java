package kontrola;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Osagaiak;

import java.io.IOException;

public class OsagaiakFormController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtUnitatea;
    @FXML private TextField txtStock;
    @FXML private Button btnGorde;
    @FXML private Button btnUtzi;

    private Osagaiak osagaia;

    public static Osagaiak openForm(Osagaiak o) {
        try {
            FXMLLoader loader = new FXMLLoader(OsagaiakFormController.class.getResource("/fxml/OsagaiForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle(o == null ? "Gehitu Osagaia" : "Editatu Osagaia");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

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
            txtUnitatea.setText(o.getUnitatea());
            txtStock.setText(String.valueOf(o.getStock_aktuala()));
        }
    }

    public Osagaiak getOsagaia() {
        return osagaia;
    }

    @FXML
    private void gordeOsagaia() {
        if (osagaia == null) osagaia = new Osagaiak();

        osagaia.setIzena(txtIzena.getText());
        osagaia.setUnitatea(txtUnitatea.getText());
        try {
            osagaia.setStock_aktuala(Double.parseDouble(txtStock.getText()));
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Stock aktuala zenbaki balioduna izan behar da.").showAndWait();
            return;
        }

        btnGorde.getScene().getWindow().hide();
    }

    @FXML
    private void itxi() {
        btnUtzi.getScene().getWindow().hide();
    }
}
