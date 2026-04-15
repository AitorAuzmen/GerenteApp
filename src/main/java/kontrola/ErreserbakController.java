package kontrola;

import DatuBasea.ErreserbakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import model.Erreserba;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ErreserbakController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML private TableView<Erreserba> erreserbakTable;
    @FXML private TableColumn<Erreserba, String> colBezeroa;
    @FXML private TableColumn<Erreserba, String> colTelefonoa;
    @FXML private TableColumn<Erreserba, String> colPertsonak;
    @FXML private TableColumn<Erreserba, String> colData;
    @FXML private TableColumn<Erreserba, String> colPrezioa;
    @FXML private TableColumn<Erreserba, String> colFaktura;
    @FXML private TableColumn<Erreserba, String> colMahaia;
    @FXML private TableColumn<Erreserba, String> colLangilea;
    @FXML private TableColumn<Erreserba, String> colOrdainduta;
    @FXML private TextField txtBuscar;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

    private ObservableList<Erreserba> erreserbak;
    private FilteredList<Erreserba> filtratua;

    @FXML
    public void initialize() {
        colBezeroa.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getBezeroIzena()));
        colTelefonoa.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTelefonoa()));
        colPertsonak.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().getPertsonaKopurua())));
        colData.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEgunaOrdua().format(DATE_FORMAT)));
        colPrezioa.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getPrezioTotala() == null ? "-" : String.format("%.2f €", cell.getValue().getPrezioTotala())));
        colFaktura.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getFakturaRuta() == null || cell.getValue().getFakturaRuta().isBlank() ? "-" : cell.getValue().getFakturaRuta()));
        colMahaia.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMahaiaLabel()));
        colLangilea.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLangileaIzena()));
        colOrdainduta.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().isOrdainduta() ? "Bai" : "Ez"));

        erreserbak = FXCollections.observableArrayList(ErreserbakDB.lortuGuztiak());
        filtratua = new FilteredList<>(erreserbak, p -> true);
        erreserbakTable.setItems(filtratua);

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> aplikatuFiltro());

        erreserbakTable.setRowFactory(tv -> {
            TableRow<Erreserba> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    editatuErreserba();
                }
            });
            return row;
        });

        btnEdit.disableProperty().bind(erreserbakTable.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(erreserbakTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void aplikatuFiltro() {
        String bilaketa = txtBuscar.getText() == null ? "" : txtBuscar.getText().toLowerCase().trim();

        filtratua.setPredicate(erreserba -> {
            if (bilaketa.isEmpty()) {
                return true;
            }

            return erreserba.getBezeroIzena().toLowerCase().contains(bilaketa)
                    || erreserba.getTelefonoa().toLowerCase().contains(bilaketa)
                    || String.valueOf(erreserba.getPertsonaKopurua()).contains(bilaketa)
                    || (erreserba.getPrezioTotala() != null && String.valueOf(erreserba.getPrezioTotala()).contains(bilaketa))
                    || (erreserba.getFakturaRuta() != null && erreserba.getFakturaRuta().toLowerCase().contains(bilaketa))
                    || erreserba.getMahaiaLabel().toLowerCase().contains(bilaketa)
                    || erreserba.getLangileaIzena().toLowerCase().contains(bilaketa);
        });
    }

    @FXML
    private void gehituErreserba() {
        Erreserba erreserba = ErreserbaFormController.openForm(null);
        if (erreserba != null && ErreserbakDB.insert(erreserba)) {
            berritu();
        }
    }

    @FXML
    private void editatuErreserba() {
        Erreserba selected = erreserbakTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Erreserba emaitza = ErreserbaFormController.openForm(selected);
        if (emaitza != null && ErreserbakDB.update(emaitza)) {
            berritu();
        }
    }

    @FXML
    private void ezabatuErreserba() {
        Erreserba selected = erreserbakTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        if (ErreserbakDB.dagoErabilita(selected.getId())) {
            new Alert(Alert.AlertType.WARNING, "Erreserba honek eskariak ditu lotuta eta ezin da ezabatu.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Erreserba ezabatu");
        confirm.setHeaderText("Ziur zaude erreserba hau ezabatu nahi duzula?");
        confirm.setContentText(selected.getBezeroIzena());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && ErreserbakDB.delete(selected.getId())) {
            berritu();
        }
    }

    private void berritu() {
        erreserbak.setAll(ErreserbakDB.lortuGuztiak());
    }
}