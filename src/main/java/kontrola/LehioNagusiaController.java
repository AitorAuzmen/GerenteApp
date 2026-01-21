package kontrola;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.util.Objects;

public class LehioNagusiaController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        System.out.println("LehioNagusiaController inicializado correctamente.");
    }

    
    @FXML
    public void kargatuProduktuak() {
        cargarVista("/fxml/produktuak.fxml", "Produktuak");
    }

    @FXML
    public void kargatuErabiltzaileak() {
        cargarVista("/fxml/erabiltzaileak.fxml", "Erabiltzaileak");
    }

    @FXML
    public void kargatuKategoriak() {
        cargarVista("/fxml/kategoriak.fxml", "Kategoriak");
    }

    @FXML
    public void kargatuMahaiak() {
        cargarVista("/fxml/mahaiak.fxml", "Mahaiak");
    }

    @FXML
    public void kargatuRolak() {
        cargarVista("/fxml/rolak.fxml", "Rolak");
    }

    @FXML
    public void kargatuOsagaiak() {
        cargarVista("/fxml/osagaiak.fxml", "Osagaiak");
    }

    
    private void cargarVista(String rutaFXML, String nombreVista) {
        System.out.println("Pantalla de " + nombreVista + "...");

        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(rutaFXML))
            );

            Parent pane = loader.load();

            
            pane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/css/estilo.css")).toExternalForm()
            );

            contentArea.getChildren().setAll(pane);

        } catch (Exception e) {
            System.out.println("ERROR cargando " + rutaFXML);
            e.printStackTrace();
        }
    }
}
