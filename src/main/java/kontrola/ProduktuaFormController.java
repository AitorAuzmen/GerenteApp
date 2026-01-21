package kontrola;

import DatuBasea.KategoriakDB;
import DatuBasea.OsagaiakDB;
import DatuBasea.ProduktuOsagaiakDB;
import DatuBasea.ProduktuakDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import model.Osagaiak;
import model.ProduktuOsagaia;
import model.Produktuak;

import java.util.List;
import java.util.Map;

public class ProduktuaFormController {

    @FXML private TextField txtIzena, txtPrezioa, txtStock, txtBilatuOsagaia;
    @FXML private ComboBox<String> cmbKategoria;
    @FXML private TableView<ProduktuOsagaia> osagaiTaula;
    @FXML private TableColumn<ProduktuOsagaia, String> colOsagaiaIzena, colUnitatea;
    @FXML private TableColumn<ProduktuOsagaia, Double> colKantitatea;

    private final ObservableList<ProduktuOsagaia> osagaiak = FXCollections.observableArrayList();
    private FilteredList<ProduktuOsagaia> filtratua;
    private Produktuak editatzen;

    public void setProduktua(Produktuak p) {
        this.editatzen = p;
        if (p != null) {
            txtIzena.setText(p.getIzena());
            txtPrezioa.setText(String.valueOf(p.getPrezioa()));
            txtStock.setText(String.valueOf(p.getStockAktuala()));
            osagaiak.setAll(ProduktuOsagaiakDB.lortuProduktukoOsagaiak(p.getId()));

            Map<Integer, String> mapa = KategoriakDB.lortuKategoriakMap();
            cmbKategoria.setValue(mapa.get(p.getKategoriaId()));
        }
    }

    @FXML
    public void initialize() {
        osagaiTaula.setEditable(true);

        
        
        
        cmbKategoria.setItems(FXCollections.observableArrayList(KategoriakDB.lortuKategoriakMap().values()));


        
        
        
        List<Osagaiak> guztiak = OsagaiakDB.lortuGuztiak();
        ObservableList<String> osagaiIzenaLista = FXCollections.observableArrayList();
        for (Osagaiak o : guztiak) osagaiIzenaLista.add(o.getIzena());

        colOsagaiaIzena.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIzena()));
        colOsagaiaIzena.setCellFactory(ComboBoxTableCell.forTableColumn(osagaiIzenaLista));
        colOsagaiaIzena.setOnEditCommit(event -> {
            ProduktuOsagaia po = event.getRowValue();
            String izenaBerria = event.getNewValue();
            po.setIzena(izenaBerria);

            for (Osagaiak o : guztiak) {
                if (o.getIzena().equalsIgnoreCase(izenaBerria)) {
                    po.setUnitatea(o.getUnitatea());
                    po.setOsagaiaId(o.getId());
                    break;
                }
            }
            osagaiTaula.refresh();
        });

        colKantitatea.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getKantitatea()));
        colKantitatea.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colKantitatea.setOnEditCommit(e -> e.getRowValue().setKantitatea(e.getNewValue()));

        colUnitatea.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUnitatea()));

        
        
        
        filtratua = new FilteredList<>(osagaiak, po -> true);
        osagaiTaula.setItems(filtratua);

        txtBilatuOsagaia.textProperty().addListener((obs, oldVal, newVal) -> {
            String filtro = newVal.toLowerCase().trim();

            filtratua.setPredicate(po -> {
                if (filtro.isEmpty()) return true;
                return po.getIzena().toLowerCase().contains(filtro);
            });
        });
    }

    @FXML
    private void gehituOsagaia() {
        
        osagaiak.add(new ProduktuOsagaia(0, 0, "", 1.0, ""));
    }

    @FXML
    private void kenduOsagaia() {
        ProduktuOsagaia o = osagaiTaula.getSelectionModel().getSelectedItem();
        if (o != null) osagaiak.remove(o);
    }

    @FXML
    private void gordeProduktua() {

        if (osagaiTaula.getEditingCell() != null) {
            osagaiTaula.edit(-1, null);
        }

        String izenaKategoria = cmbKategoria.getValue();
        int kategoriaId = -1;

        for (Map.Entry<Integer, String> e : KategoriakDB.lortuKategoriakMap().entrySet()) {
            if (e.getValue().trim().equalsIgnoreCase(izenaKategoria.trim())) {
                kategoriaId = e.getKey();
                break;
            }
        }

        if (kategoriaId == -1) {
            new Alert(Alert.AlertType.ERROR, "Kategoria ez da existitzen: " + izenaKategoria).showAndWait();
            return;
        }

        Produktuak p = new Produktuak(
                editatzen == null ? 0 : editatzen.getId(),
                txtIzena.getText(),
                kategoriaId,
                Double.parseDouble(txtPrezioa.getText()),
                Integer.parseInt(txtStock.getText())
        );

        int productoId;

        if (editatzen == null) {
            productoId = ProduktuakDB.gehituProduktua(p);
            if (productoId == -1) {
                new Alert(Alert.AlertType.ERROR, "Ezin izan da produktua gorde.").showAndWait();
                return;
            }
            p.setId(productoId);
        } else {
            ProduktuakDB.eguneratuProduktua(p);
            productoId = p.getId();
            ProduktuOsagaiakDB.ezabatuProduktukoOsagaiak(productoId);
        }

        for (ProduktuOsagaia o : osagaiak) {
            o.setProduktuaId(productoId);
            ProduktuOsagaiakDB.gehituProduktukoOsagaia(productoId, o);
        }

        itxi();
    }

    @FXML
    private void itxi() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
