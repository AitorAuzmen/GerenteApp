module com.example.erronka4taldea {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.erronka4taldea to javafx.fxml;
    exports com.example.erronka4taldea;
}