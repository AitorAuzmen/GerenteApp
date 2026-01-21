package kontrola;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Rolak;

import java.io.IOException;

public class RolakFormController {

    @FXML
    private TextField txtIzena;
    @FXML
    private Button btnGorde, btnUtzi;

    private Rolak rol;

    public static Rolak openForm(Rolak r) {
        try {
            FXMLLoader loader = new FXMLLoader(RolakFormController.class.getResource("/fxml/RolakForm.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            RolakFormController controller = loader.getController();
            controller.setRol(r);
            stage.showAndWait();
            return controller.getRol();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setRol(Rolak r) {
        this.rol = r;
        if (r != null) txtIzena.setText(r.getIzena());
    }

    public Rolak getRol() {
        return rol;
    }

    @FXML
    private void gorde() {
        String izena = txtIzena.getText();
        if (izena != null && !izena.isBlank()) {
            if (rol == null) rol = new Rolak();
            rol.setIzena(izena);
            ((Stage) btnGorde.getScene().getWindow()).close();
        }
    }

    @FXML
    private void itxi() {
        rol = null; 
        ((Stage) btnUtzi.getScene().getWindow()).close();
    }
}

