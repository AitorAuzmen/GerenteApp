package kontrola;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Hornitzailea;

import java.io.IOException;

public class HornitzaileFormController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtKontaktua;
    @FXML private TextField txtHelbidea;

    private Hornitzailea hornitzailea;

    public static Hornitzailea openForm(Hornitzailea hornitzailea) {
        try {
            FXMLLoader loader = new FXMLLoader(HornitzaileFormController.class.getResource("/fxml/HornitzaileForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle(hornitzailea == null ? "Hornitzailea gehitu" : "Hornitzailea editatu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(loader.load()));

            HornitzaileFormController controller = loader.getController();
            controller.setHornitzailea(hornitzailea);

            stage.showAndWait();
            return controller.getHornitzailea();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setHornitzailea(Hornitzailea hornitzailea) {
        if (hornitzailea == null) {
            this.hornitzailea = null;
            return;
        }

        this.hornitzailea = new Hornitzailea(
                hornitzailea.getId(),
                hornitzailea.getIzena(),
                hornitzailea.getKontaktua(),
                hornitzailea.getHelbidea()
        );

        txtIzena.setText(this.hornitzailea.getIzena());
        txtKontaktua.setText(this.hornitzailea.getKontaktua());
        txtHelbidea.setText(this.hornitzailea.getHelbidea());
    }

    public Hornitzailea getHornitzailea() {
        return hornitzailea;
    }

    @FXML
    private void gordeHornitzailea() {
        String izena = txtIzena.getText().trim();
        String kontaktua = txtKontaktua.getText().trim();
        String helbidea = txtHelbidea.getText().trim();

        if (izena.isEmpty() || kontaktua.isEmpty() || helbidea.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Sartu izena, kontaktua eta helbidea.").showAndWait();
            return;
        }

        if (hornitzailea == null) {
            hornitzailea = new Hornitzailea();
        }

        hornitzailea.setIzena(izena);
        hornitzailea.setKontaktua(kontaktua);
        hornitzailea.setHelbidea(helbidea);
        itxi();
    }

    @FXML
    private void itxi() {
        Stage stage = (Stage) txtIzena.getScene().getWindow();
        stage.close();
    }
}