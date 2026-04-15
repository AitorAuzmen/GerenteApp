package kontrola;

import DatuBasea.ErabiltzaileakDB;
import DatuBasea.MahaiakDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Erabiltzailea;
import model.Erreserba;
import model.Mahaia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class ErreserbaFormController {

    @FXML private TextField txtBezeroIzena;
    @FXML private TextField txtTelefonoa;
    @FXML private TextField txtPertsonaKopurua;
    @FXML private DatePicker dpEguna;
    @FXML private TextField txtOrdua;
    @FXML private TextField txtPrezioTotala;
    @FXML private TextField txtFakturaRuta;
    @FXML private CheckBox chkOrdainduta;
    @FXML private ComboBox<Erabiltzailea> cmbLangilea;
    @FXML private ComboBox<Mahaia> cmbMahaia;

    private Erreserba erreserba;

    public static Erreserba openForm(Erreserba erreserba) {
        try {
            FXMLLoader loader = new FXMLLoader(ErreserbaFormController.class.getResource("/fxml/ErreserbaForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle(erreserba == null ? "Erreserba gehitu" : "Erreserba editatu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(ErreserbaFormController.class.getResource("/css/estilo.css").toExternalForm());
            stage.setScene(scene);

            ErreserbaFormController controller = loader.getController();
            controller.kargatuAukerak();
            controller.setErreserba(erreserba);

            stage.showAndWait();
            return controller.getErreserba();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void kargatuAukerak() {
        ErabiltzaileakDB db = new ErabiltzaileakDB();
        List<Erabiltzailea> langileak = db.getAll().stream()
                .filter(langilea -> !langilea.isEzabatua())
                .collect(Collectors.toList());

        List<Mahaia> mahaiak = MahaiakDB.lortuMahaiak();

        cmbLangilea.setItems(FXCollections.observableArrayList(langileak));
        cmbMahaia.setItems(FXCollections.observableArrayList(mahaiak));

        cmbLangilea.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Erabiltzailea item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIzena() + " " + item.getAbizena());
            }
        });
        cmbLangilea.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Erabiltzailea item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIzena() + " " + item.getAbizena());
            }
        });

        cmbMahaia.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Mahaia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "Mahaia " + item.getZenbakia() + " - " + item.getEgoera());
            }
        });
        cmbMahaia.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Mahaia item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "Mahaia " + item.getZenbakia() + " - " + item.getEgoera());
            }
        });
    }

    public void setErreserba(Erreserba erreserba) {
        if (erreserba == null) {
            this.erreserba = null;
            dpEguna.setValue(LocalDate.now());
            txtOrdua.setText("14:00");
            return;
        }

        this.erreserba = new Erreserba();
        this.erreserba.setId(erreserba.getId());
        this.erreserba.setBezeroIzena(erreserba.getBezeroIzena());
        this.erreserba.setTelefonoa(erreserba.getTelefonoa());
        this.erreserba.setPertsonaKopurua(erreserba.getPertsonaKopurua());
        this.erreserba.setEgunaOrdua(erreserba.getEgunaOrdua());
        this.erreserba.setPrezioTotala(erreserba.getPrezioTotala());
        this.erreserba.setOrdainduta(erreserba.isOrdainduta());
        this.erreserba.setFakturaRuta(erreserba.getFakturaRuta());
        this.erreserba.setLangileaId(erreserba.getLangileaId());
        this.erreserba.setMahaiaId(erreserba.getMahaiaId());

        txtBezeroIzena.setText(this.erreserba.getBezeroIzena());
        txtTelefonoa.setText(this.erreserba.getTelefonoa());
        txtPertsonaKopurua.setText(String.valueOf(this.erreserba.getPertsonaKopurua()));
        dpEguna.setValue(this.erreserba.getEgunaOrdua().toLocalDate());
        txtOrdua.setText(this.erreserba.getEgunaOrdua().toLocalTime().toString());
        txtPrezioTotala.setText(this.erreserba.getPrezioTotala() == null ? "" : String.valueOf(this.erreserba.getPrezioTotala()));
        txtFakturaRuta.setText(this.erreserba.getFakturaRuta() == null ? "" : this.erreserba.getFakturaRuta());
        chkOrdainduta.setSelected(this.erreserba.isOrdainduta());

        cmbLangilea.getItems().stream()
                .filter(langilea -> langilea.getId() == this.erreserba.getLangileaId())
                .findFirst()
                .ifPresent(cmbLangilea::setValue);

        cmbMahaia.getItems().stream()
                .filter(mahaia -> mahaia.getId() == this.erreserba.getMahaiaId())
                .findFirst()
                .ifPresent(cmbMahaia::setValue);
    }

    public Erreserba getErreserba() {
        return erreserba;
    }

    @FXML
    private void gordeErreserba() {
        String bezeroIzena = txtBezeroIzena.getText().trim();
        String telefonoa = txtTelefonoa.getText().trim();
        String pertsonaText = txtPertsonaKopurua.getText().trim();
        String orduaText = txtOrdua.getText().trim();
        LocalDate eguna = dpEguna.getValue();

        if (bezeroIzena.isEmpty() || telefonoa.isEmpty() || pertsonaText.isEmpty() || orduaText.isEmpty()
                || eguna == null || cmbLangilea.getValue() == null || cmbMahaia.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Bete beharrezko eremu guztiak.").showAndWait();
            return;
        }

        int pertsonaKopurua;
        LocalTime ordua;
        Double prezioTotala = null;

        try {
            pertsonaKopurua = Integer.parseInt(pertsonaText);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Pertsona kopurua zenbaki balioduna izan behar da.").showAndWait();
            return;
        }

        try {
            ordua = LocalTime.parse(orduaText);
        } catch (DateTimeParseException e) {
            new Alert(Alert.AlertType.WARNING, "Orduak HH:mm formatua izan behar du.").showAndWait();
            return;
        }

        if (!txtPrezioTotala.getText().trim().isEmpty()) {
            try {
                prezioTotala = Double.parseDouble(txtPrezioTotala.getText().trim());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Prezio totala zenbaki balioduna izan behar da.").showAndWait();
                return;
            }
        }

        if (erreserba == null) {
            erreserba = new Erreserba();
        }

        erreserba.setBezeroIzena(bezeroIzena);
        erreserba.setTelefonoa(telefonoa);
        erreserba.setPertsonaKopurua(pertsonaKopurua);
        erreserba.setEgunaOrdua(LocalDateTime.of(eguna, ordua));
        erreserba.setPrezioTotala(prezioTotala);
        erreserba.setOrdainduta(chkOrdainduta.isSelected());
        erreserba.setFakturaRuta(txtFakturaRuta.getText().trim().isEmpty() ? null : txtFakturaRuta.getText().trim());
        erreserba.setLangileaId(cmbLangilea.getValue().getId());
        erreserba.setMahaiaId(cmbMahaia.getValue().getId());
        itxi();
    }

    @FXML
    private void itxi() {
        Stage stage = (Stage) txtBezeroIzena.getScene().getWindow();
        stage.close();
    }
}