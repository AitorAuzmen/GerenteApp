package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 450);

            // Cargar CSS
            scene.getStylesheets().add(getClass().getResource("/css/estilo.css").toExternalForm());

            primaryStage.setTitle("JAUS - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            // NOTA: cuando el login sea correcto, desde el controlador de Login
            // se abrirá LehioNagusia de forma similar:
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LehioNagusia.fxml"));
            // Parent root = loader.load();
            // Scene scene = new Scene(root, 1000, 600);
            // scene.getStylesheets().add(getClass().getResource("/css/estilo.css").toExternalForm());
            // Stage stage = new Stage();
            // stage.setTitle("JAUS - Lehio Nagusia");
            // stage.setScene(scene);
            // stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
