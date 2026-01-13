package kontrola;

import DatuBasea.KategoriakDB;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Kategoria;

public class KategoriaFormController {

    @FXML private TextField txtIzena;

    private Kategoria editatzen;

    public void setKategoria(Kategoria k) {
        this.editatzen = k;
        if (k != null) {
            txtIzena.setText(k.getIzena());
        }
    }

    @FXML
    private void gordeKategoria() {
        String izena = txtIzena.getText().trim();
        if (izena.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Sartu izen bat.").showAndWait();
            return;
        }

        if (editatzen == null) {
            int id = KategoriakDB.gehituKategoria(new Kategoria(0, izena));
            if (id == -1) {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da kategoriak gorde.").showAndWait();
            }
        } else {
            editatzen.setIzena(izena);
            KategoriakDB.eguneratuKategoria(editatzen);
        }

        itxi();
    }

    @FXML
    private void itxi() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
