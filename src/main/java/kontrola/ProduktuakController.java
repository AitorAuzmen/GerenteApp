package kontrola;

import DatuBasea.KategoriakDB;
import DatuBasea.ProduktuakDB;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Produktuak;

import java.util.Map;

public class ProduktuakController {

    @FXML private TableView<Produktuak> produktuTable;
    @FXML private TableColumn<Produktuak, String> colIzena;
    @FXML private TableColumn<Produktuak, Integer> colKategoria;
    @FXML private TableColumn<Produktuak, Double> colPrezioa;
    @FXML private TableColumn<Produktuak, Integer> colStock;

    @FXML private TextField txtBilatu;
    @FXML private ComboBox<String> cmbKategoriak;

    @FXML private Button btnAdd, btnEdit, btnDelete;

    private ObservableList<Produktuak> produktuak;
    private FilteredList<Produktuak> filtratua;
    private Map<Integer, String> kategoriakMapa;

    @FXML
    public void initialize() {

        
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzena()));
        colKategoria.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getKategoriaId()));
        colPrezioa.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrezioa()));
        colStock.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStockAktuala()));

        
        produktuak = FXCollections.observableArrayList(ProduktuakDB.lortuProduktuak());
        filtratua = new FilteredList<>(produktuak, p -> true);
        produktuTable.setItems(filtratua);

        
        kategoriakMapa = KategoriakDB.lortuKategoriakMap();
        cmbKategoriak.getItems().setAll(kategoriakMapa.values());

        
        txtBilatu.textProperty().addListener((obs, old, val) -> aplikatuFiltro());
        cmbKategoriak.valueProperty().addListener((obs, old, val) -> aplikatuFiltro());

        
        produktuTable.setRowFactory(tv -> {
            TableRow<Produktuak> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    produktuTable.getSelectionModel().select(row.getItem());
                    editatuProduktua();
                }
            });
            return row;
        });

        
        produktuTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, val) -> actualizarBotones());

        actualizarBotones(); 
    }

    private void aplikatuFiltro() {
        String testua = txtBilatu.getText().toLowerCase();
        String kategoriaIzena = cmbKategoriak.getValue();

        filtratua.setPredicate(p -> {
            boolean testuaOndo = testua.isEmpty() || p.getIzena().toLowerCase().contains(testua);
            boolean kategoriaOndo = true;

            if (kategoriaIzena != null) {
                String produktuaKategoria = kategoriakMapa.get(p.getKategoriaId());
                kategoriaOndo = kategoriaIzena.equals(produktuaKategoria);
            }

            return testuaOndo && kategoriaOndo;
        });
    }

    private void actualizarBotones() {
        Produktuak sel = produktuTable.getSelectionModel().getSelectedItem();
        btnEdit.setDisable(sel == null);
        btnDelete.setDisable(sel == null);
    }

    @FXML
    private void gehituProduktua() {
        irekiFormulario(null);
    }

    @FXML
    private void editatuProduktua() {
        Produktuak p = produktuTable.getSelectionModel().getSelectedItem();
        if (p != null) irekiFormulario(p);
        else alerta("Aukeratu produktu bat editatzeko.");
    }

    @FXML
    private void ezabatuProduktua() {
        Produktuak p = produktuTable.getSelectionModel().getSelectedItem();
        if (p != null) {
            ProduktuakDB.ezabatuProduktua(p.getId());
            berritu();
        } else {
            alerta("Aukeratu produktu bat ezabatzeko.");
        }
    }

    private void irekiFormulario(Produktuak p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/produktua_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ProduktuaFormController ctrl = loader.getController();
            ctrl.setProduktua(p);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(p == null ? "Produktua gehitu" : "Produktua editatu");
            stage.showAndWait();
            berritu();
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea: ezin izan da formularioa ireki.");
        }
    }

    private void berritu() {
        produktuak.setAll(ProduktuakDB.lortuProduktuak());
    }

    private void alerta(String mezua) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(mezua);
        a.showAndWait();
    }
}
