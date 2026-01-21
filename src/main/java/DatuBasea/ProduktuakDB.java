package DatuBasea;

import model.Produktuak;
import Util.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduktuakDB {

    public static List<Produktuak> lortuProduktuak() {
        List<Produktuak> lista = new ArrayList<>();
        String sql = "SELECT * FROM produktuak";

        try (Connection conn = Conn.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Produktuak(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getInt("kategoria_id"),
                        rs.getDouble("prezioa"),
                        rs.getInt("stock_aktuala")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    
    public static int gehituProduktua(Produktuak p) {
        String sql = "INSERT INTO produktuak (izena, kategoria_id, prezioa, stock_aktuala) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getIzena());
            ps.setInt(2, p.getKategoriaId());
            ps.setDouble(3, p.getPrezioa());
            ps.setInt(4, p.getStockAktuala());

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
        String sql = "UPDATE produktuak SET izena=?, kategoria_id=?, prezioa=?, stock_aktuala=? WHERE id=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getIzena());
            ps.setInt(2, p.getKategoriaId());
            ps.setDouble(3, p.getPrezioa());
            ps.setInt(4, p.getStockAktuala());
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
}
