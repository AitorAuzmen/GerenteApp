package DatuBasea;

import model.Produktuak;
import Util.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduktuakDB {

    public static List<Produktuak> lortuProduktuak() {
        List<Produktuak> lista = new ArrayList<>();
        String sql = "SELECT id, izena, mota, prezioa, stock FROM produktuak";

        try (Connection conn = Conn.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Produktuak(
                        rs.getInt("id"),
                        rs.getString("izena"),
                    rs.getString("mota"),
                        rs.getDouble("prezioa"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static int gehituProduktua(Produktuak p) {
        String sql = "INSERT INTO produktuak (izena, mota, prezioa, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getIzena());
            ps.setString(2, p.getMota());
            ps.setDouble(3, p.getPrezioa());
            ps.setInt(4, p.getStock());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el producto, no se afectaron filas.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    p.setId(idGenerado); 
                    return idGenerado;   
                } else {
                    throw new SQLException("No se pudo obtener el ID del producto insertado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void eguneratuProduktua(Produktuak p) {
        String sql = "UPDATE produktuak SET izena=?, mota=?, prezioa=?, stock=? WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getIzena());
            ps.setString(2, p.getMota());
            ps.setDouble(3, p.getPrezioa());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ezabatuProduktua(int id) {
        String sql = "DELETE FROM produktuak WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> lortuMotak() {
        List<String> motak = new ArrayList<>();
        String sql = "SELECT DISTINCT mota FROM produktuak WHERE mota IS NOT NULL AND mota <> '' ORDER BY mota";

        try (Connection conn = Conn.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                motak.add(rs.getString("mota"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return motak;
    }
}
