package kontrola;

import javafx.scene.control.Alert;
import Util.Conn;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginManager {

    @FXML
    private TextField txtErabiltzailea;

    @FXML
    private PasswordField txtPasahitza;

    @FXML
    private void saioaHasi() {

        String erabiltzailea = txtErabiltzailea.getText().trim();
        String pasahitza = txtPasahitza.getText().trim();

        if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            alertaErakutsi("Errorea", "Erabiltzailea eta pasahitza sartu behar dituzu", Alert.AlertType.ERROR);
            return;
        }

        if (erabiltzaileaBalidatu(erabiltzailea, pasahitza)) {
            alertaErakutsi("Saioa ondo hasi da", "Ongi etorri, " + erabiltzailea + "!", Alert.AlertType.INFORMATION);
            leihoNagusiaIreki();
        } else {
            alertaErakutsi("Saioa hasteko errorea", "Erabiltzailea edo pasahitza okerra", Alert.AlertType.ERROR);
        }
    }

    private boolean erabiltzaileaBalidatu(String erabiltzailea, String pasahitza) {
        String sql = "SELECT * FROM erabiltzaileak WHERE izena = ? AND pasahitza = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, erabiltzailea);
            stmt.setString(2, pasahitza);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            alertaErakutsi("Errorea", "Datu-basearekin konektatzean errorea", Alert.AlertType.ERROR);
            return false;
        }
    }

    private void alertaErakutsi(String izenburua, String mezua, Alert.AlertType mota) {
        Alert alerta = new Alert(mota);
        alerta.setTitle(izenburua);
        alerta.setHeaderText(null);
        alerta.setContentText(mezua);
        alerta.showAndWait();
    }

    private void leihoNagusiaIreki() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Principal.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("JAUS - Leiho Nagusia");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

            // Login leihoa itxi
            Stage loginStage = (Stage) txtErabiltzailea.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
