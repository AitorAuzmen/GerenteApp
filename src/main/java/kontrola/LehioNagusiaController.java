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
    private void kargatuProduktuak() {
        cargarVista("/fxml/produktuak.fxml", "Produktuak");
    }

    @FXML
    private void kargatuErabiltzaileak() {
        cargarVista("/fxml/erabiltzaileak.fxml", "Erabiltzaileak");
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
    }}

