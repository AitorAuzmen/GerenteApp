import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conn {
    private static final String URL = "jdbc:mysql://localhost:3306/tpv_restaurante";
    private static final String USER = "root";
    private static final String PASSWORD = "1MG2024";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
