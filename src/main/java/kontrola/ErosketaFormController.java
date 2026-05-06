package kontrola;

import DatuBasea.HornitzaileakDB;
import DatuBasea.MaterialakDB;
import DatuBasea.OsagaiakDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Erosketa;
import model.Hornitzailea;
import model.Materiala;
import model.Osagaiak;

import java.io.IOException;
import java.util.List;

public class ErosketaFormController {

    @FXML private ComboBox<Hornitzailea> cmbHornitzailea;
    @FXML private RadioButton rbOsagaia;
    @FXML private RadioButton rbMateriala;
    @FXML private ComboBox<Osagaiak> cmbOsagaia;
    @FXML private ComboBox<Materiala> cmbMateriala;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtKantitatea;
    @FXML private Button btnGorde;
    @FXML private Button btnUtzi;

    private final ToggleGroup motaGroup = new ToggleGroup();
    private Erosketa erosketa;

    @FXML
    private void initialize() {
        rbOsagaia.setToggleGroup(motaGroup);
        rbMateriala.setToggleGroup(motaGroup);
        rbOsagaia.setSelected(true);

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

        cmbOsagaia.setConverter(new StringConverter<>() {
            @Override
            public String toString(Osagaiak osagaia) {
                return osagaia == null ? "" : osagaia.getIzena();
            }

            @Override
            public Osagaiak fromString(String string) {
                return null;
            }
        });

        List<Hornitzailea> hornitzaileak = HornitzaileakDB.lortuGuztiak();
        cmbHornitzailea.getItems().setAll(hornitzaileak);
        if (!hornitzaileak.isEmpty()) {
            cmbHornitzailea.getSelectionModel().selectFirst();
        }

        List<Osagaiak> osagaiak = OsagaiakDB.lortuGuztiak();
        cmbOsagaia.getItems().setAll(osagaiak);
        if (!osagaiak.isEmpty()) {
            cmbOsagaia.getSelectionModel().selectFirst();
        }

        List<Materiala> materialak = MaterialakDB.lortuGuztiak();
        cmbMateriala.getItems().setAll(materialak);
        if (!materialak.isEmpty()) {
            cmbMateriala.getSelectionModel().selectFirst();
        }

        eguneratuMotaAukerak();
        motaGroup.selectedToggleProperty().addListener((obs, old, val) -> eguneratuMotaAukerak());
    }

    public static Erosketa openForm() {
        try {
            FXMLLoader loader = new FXMLLoader(ErosketaFormController.class.getResource("/fxml/ErosketaForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gehitu Erosketa");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);

            ErosketaFormController controller = loader.getController();
            stage.showAndWait();
            return controller.getErosketa();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Erosketa getErosketa() {
        return erosketa;
    }

    @FXML
    private void gordeErosketa() {
        Hornitzailea hornitzailea = cmbHornitzailea.getSelectionModel().getSelectedItem();
        if (hornitzailea == null) {
            new Alert(Alert.AlertType.WARNING, "Hornitzailea hautatu behar duzu.").showAndWait();
            return;
        }

        boolean osagaiaDa = rbOsagaia.isSelected();
        Osagaiak osagaia = cmbOsagaia.getSelectionModel().getSelectedItem();
        Materiala materiala = cmbMateriala.getSelectionModel().getSelectedItem();

        if (osagaiaDa && osagaia == null) {
            new Alert(Alert.AlertType.WARNING, "Osagaia hautatu behar duzu.").showAndWait();
            return;
        }
        if (!osagaiaDa && materiala == null) {
            new Alert(Alert.AlertType.WARNING, "Materiala hautatu behar duzu.").showAndWait();
            return;
        }

        double prezioa;
        try {
            prezioa = Double.parseDouble(txtPrezioa.getText().trim());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Prezioa zenbaki balioduna izan behar da.").showAndWait();
            return;
        }

        int kantitatea;
        try {
            kantitatea = Integer.parseInt(txtKantitatea.getText().trim());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Kantitatea zenbaki balioduna izan behar da.").showAndWait();
            return;
        }

        if (kantitatea <= 0) {
            new Alert(Alert.AlertType.WARNING, "Kantitatea 0 baino handiagoa izan behar da.").showAndWait();
            return;
        }

        erosketa = new Erosketa();
        erosketa.setHornitzaileaId(hornitzailea.getId());
        erosketa.setHornitzaileIzena(hornitzailea.getIzena());
        erosketa.setPrezioa(prezioa);
        erosketa.setKantitatea(kantitatea);

        if (osagaiaDa) {
            erosketa.setOsagaiaId(osagaia.getId());
            erosketa.setOsagaiaIzena(osagaia.getIzena());
        } else {
            erosketa.setMaterialaId(materiala.getId());
            erosketa.setMaterialaIzena(materiala.getIzena());
        }

        btnGorde.getScene().getWindow().hide();
    }

    @FXML
    private void itxi() {
        btnUtzi.getScene().getWindow().hide();
    }

    private void eguneratuMotaAukerak() {
        boolean osagaiaDa = rbOsagaia.isSelected();
        cmbOsagaia.setDisable(!osagaiaDa);
        cmbMateriala.setDisable(osagaiaDa);
    }
}
