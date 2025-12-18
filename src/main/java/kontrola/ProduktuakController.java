package kontrola;

import DatuBasea.ProduktuakDB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Produktuak;
import javafx.collections.ObservableList;

public class ProduktuakController {

    @FXML private TableView<Produktuak> tableView;
    @FXML private TableColumn<Produktuak, Integer> colId;
    @FXML private TableColumn<Produktuak, String> colIzena;
    @FXML private TableColumn<Produktuak, Integer> colKategoria;
    @FXML private TableColumn<Produktuak, Double> colPrezioa;
    @FXML private TableColumn<Produktuak, Integer> colStock;

    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colIzena.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("izena"));
        colKategoria.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("kategoria_id"));
        colPrezioa.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("prezioa"));
        colStock.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("stock_aktuala"));


        kargatuDatuak();


        btnAdd.setOnAction(e -> openAddDialog());
        btnEdit.setOnAction(e -> openEditDialog());
        btnDelete.setOnAction(e -> deleteSelected());
    }

    private void kargatuDatuak() {
        ObservableList<Produktuak> items = ProduktuakDB.lortuGuztiak();
        tableView.setItems(items);
    }

    private void openAddDialog() {
        Produktuak p = editDialog(null);
        if (p != null) {
            boolean ok = ProduktuakDB.insert(p);
            if (ok) kargatuDatuak();
            else alerta("Errorea", "Ezin izan da produktua gehitu");
        }
    }

    private void openEditDialog() {
        Produktuak sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Aukeratu", "Hautatu produktua editatzeko"); return; }

        Produktuak mod = editDialog(sel);
        if (mod != null) {
            boolean ok = ProduktuakDB.update(mod);
            if (ok) kargatuDatuak();
            else alerta("Errorea", "Ezin izan da produktua eguneratu");
        }
    }

    private void deleteSelected() {
        Produktuak sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Aukeratu", "Hautatu produktua ezabatzeko"); return; }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Ziur zaude ezabatu nahi duzula?", ButtonType.YES, ButtonType.NO);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                boolean ok = ProduktuakDB.delete(sel.getId());
                if (ok) kargatuDatuak();
                else alerta("Errorea", "Ezin izan da produktua ezabatu");
            }
        });
    }


    private Produktuak editDialog(Produktuak p) {
        Dialog<Produktuak> dialog = new Dialog<>();
        dialog.setTitle(p == null ? "Gehitu Produktua" : "Editatu Produktua");


        ButtonType saveBtn = new ButtonType("Gorde", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);


        TextField tfIzena = new TextField();
        TextField tfKategoria = new TextField();
        TextField tfPrezioa = new TextField();
        TextField tfStock = new TextField();

        if (p != null) {
            tfIzena.setText(p.getIzena());
            tfKategoria.setText(String.valueOf(p.getKategoria_id()));
            tfPrezioa.setText(String.valueOf(p.getPrezioa()));
            tfStock.setText(String.valueOf(p.getStock_aktuala()));
        }

        VBox content = new VBox(8);
        content.getChildren().addAll(
                new Label("Izena"), tfIzena,
                new Label("Kategoria ID"), tfKategoria,
                new Label("Prezioa"), tfPrezioa,
                new Label("Stock"), tfStock
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                try {
                    Produktuak np = (p == null) ? new Produktuak() : p;
                    np.setIzena(tfIzena.getText().trim());
                    np.setKategoria_id(Integer.parseInt(tfKategoria.getText().trim()));
                    np.setPrezioa(Double.parseDouble(tfPrezioa.getText().trim()));
                    np.setStock_aktuala(Integer.parseInt(tfStock.getText().trim()));
                    return np;
                } catch (Exception ex) {
                    alerta("Balio okerra", "Begiratu datuak: " + ex.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private void alerta(String title, String mezua) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, mezua, ButtonType.OK);
        a.setTitle(title);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.showAndWait();
    }
}
