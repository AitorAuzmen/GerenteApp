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
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 450);

            
            scene.getStylesheets().add(getClass().getResource("/css/estilo.css").toExternalForm());

            primaryStage.setTitle("JAUS - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            
            
            
            
            
            
            
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
