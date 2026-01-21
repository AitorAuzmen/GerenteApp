module com.example.erronka4taldea {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens Main to javafx.fxml;
    opens kontrola to javafx.fxml;
    opens Util to javafx.fxml;
    opens DatuBasea to javafx.fxml;
    opens model to javafx.base;  

    exports Main;
    exports kontrola;
    exports Util;
    exports DatuBasea;
}
