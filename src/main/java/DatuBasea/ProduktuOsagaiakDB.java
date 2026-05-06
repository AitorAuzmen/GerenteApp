package DatuBasea;

import model.ProduktuOsagaia;
import Util.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduktuOsagaiakDB {

    public static List<ProduktuOsagaia> lortuProduktukoOsagaiak(int produktuaId) {
        List<ProduktuOsagaia> lista = new ArrayList<>();
        String sql = """
            SELECT o.id, o.izena, o.stock, po.kantitatea
            FROM produktuak_has_osagaiak po
            JOIN osagaiak o ON po.osagaiak_id = o.id
            WHERE po.produktuak_id = ?
        """;

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, produktuaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new ProduktuOsagaia(
                        produktuaId,
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getDouble("kantitatea"),
                        rs.getDouble("stock")
                ));
            }

        } catch (SQLException e) { e.printStackTrace(); }

        return lista;
    }

    public static void ezabatuProduktukoOsagaiak(int produktuaId) {
        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM produktuak_has_osagaiak WHERE produktuak_id=?")) {

            ps.setInt(1, produktuaId);
            ps.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void gehituProduktukoOsagaia(int produktuaId, ProduktuOsagaia o) {
        String sql = "INSERT INTO produktuak_has_osagaiak (produktuak_id, osagaiak_id, kantitatea) VALUES (?, ?, ?)";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, produktuaId);
            ps.setInt(2, o.getOsagaiaId());
            ps.setDouble(3, o.getKantitatea());
            ps.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }
}
