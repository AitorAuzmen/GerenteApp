package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {

   
     //private static final String URL = "jdbc:mysql://localhost:3306/tpv?useSSL=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://192.168.1.10:3306/tpv?useSSL=false&serverTimezone=UTC";
    private static final String USER = "admin";
    private static final String PASSWORD = "Taldea4";
    // Para conexión remota, descomenta la siguiente línea:
    // private static final String URL = "jdbc:mysql://192.168.1.10:3306/tpv?useSSL=false&serverTimezone=UTC";
    //private static final String URL = "jdbc:mysql://localhost:3306/tpv?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    //private static final String USER = "root";
    //private static final String PASSWORD = "1MG2024";
    //private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
