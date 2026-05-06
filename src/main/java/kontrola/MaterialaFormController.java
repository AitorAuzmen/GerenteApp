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
import model.Materiala;

import java.io.IOException;
import java.util.List;

public class MaterialaFormController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtStock;
    @FXML private ComboBox<Hornitzailea> cmbHornitzailea;
    @FXML private Button btnGorde;
    @FXML private Button btnUtzi;

    private Materiala materiala;

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

    public static Materiala openForm(Materiala m) {
        try {
            FXMLLoader loader = new FXMLLoader(MaterialaFormController.class.getResource("/fxml/MaterialaForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle(m == null ? "Gehitu Materiala" : "Editatu Materiala");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);

            MaterialaFormController controller = loader.getController();
            controller.setMateriala(m);

            stage.showAndWait();
            return controller.getMateriala();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMateriala(Materiala m) {
        this.materiala = m;
        if (m != null) {
            txtIzena.setText(m.getIzena());
            txtPrezioa.setText(String.valueOf(m.getPrezioa()));
            txtStock.setText(String.valueOf(m.getStock()));
            hautatuHornitzailea(m.getHornitzaileId());
        }
    }

    public Materiala getMateriala() {
        return materiala;
    }

    @FXML
    private void gordeMateriala() {
        if (materiala == null) materiala = new Materiala();

        Hornitzailea hornitzailea = cmbHornitzailea.getSelectionModel().getSelectedItem();
        if (hornitzailea == null) {
            new Alert(Alert.AlertType.WARNING, "Lehenengo hornitzaile bat sortu edo hautatu behar duzu.").showAndWait();
            return;
        }

        String izena = txtIzena.getText().trim();
        if (izena.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Izena sartu behar duzu.").showAndWait();
            return;
        }

        double prezioa;
        try {
            prezioa = Double.parseDouble(txtPrezioa.getText().trim());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Prezioa zenbaki balioduna izan behar da.").showAndWait();
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Stock zenbaki balioduna izan behar da.").showAndWait();
            return;
        }

        materiala.setIzena(izena);
        materiala.setPrezioa(prezioa);
        materiala.setStock(stock);
        materiala.setHornitzaileId(hornitzailea.getId());
        materiala.setHornitzaileIzena(hornitzailea.getIzena());

        btnGorde.getScene().getWindow().hide();
    }

    @FXML
    private void itxi() {
        btnUtzi.getScene().getWindow().hide();
    }

    private void hautatuHornitzailea(int hornitzaileId) {
        if (hornitzaileId <= 0) return;
        for (Hornitzailea hornitzailea : cmbHornitzailea.getItems()) {
            if (hornitzailea.getId() == hornitzaileId) {
                cmbHornitzailea.getSelectionModel().select(hornitzailea);
                return;
            }
        }
    }
}
