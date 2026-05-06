package DatuBasea;

import Util.Conn;
import model.Materiala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MaterialakDB {

    public static List<Materiala> lortuGuztiak() {
        List<Materiala> lista = new ArrayList<>();
        String sql = """
                SELECT m.id, m.izena, m.prezioa, m.stock, m.hornitzaileak_id, h.izena AS hornitzaile_izena
                FROM materialak m
                LEFT JOIN hornitzaileak h ON h.id = m.hornitzaileak_id
                ORDER BY m.izena
                """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Materiala lortuById(int id) {
        String sql = """
                SELECT m.id, m.izena, m.prezioa, m.stock, m.hornitzaileak_id, h.izena AS hornitzaile_izena
                FROM materialak m
                LEFT JOIN hornitzaileak h ON h.id = m.hornitzaileak_id
                WHERE m.id = ?
                """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insert(Materiala materiala) {
        String sql = "INSERT INTO materialak (izena, prezioa, stock, hornitzaileak_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, materiala.getIzena());
            pst.setDouble(2, materiala.getPrezioa());
            pst.setInt(3, materiala.getStock());
            pst.setInt(4, lortuHornitzaileId(materiala, conn));
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Materiala materiala) {
        String sql = "UPDATE materialak SET izena = ?, prezioa = ?, stock = ?, hornitzaileak_id = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, materiala.getIzena());
            pst.setDouble(2, materiala.getPrezioa());
            pst.setInt(3, materiala.getStock());
            pst.setInt(4, lortuHornitzaileId(materiala, conn));
            pst.setInt(5, materiala.getId());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM materialak WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean dagoErabilita(int id) {
        String sql = "SELECT COUNT(*) AS guztira FROM erosketa WHERE materiala_id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt("guztira") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static int lortuHornitzaileLehenetsia(Connection conn) throws Exception {
        String sql = "SELECT id FROM hornitzaileak ORDER BY id LIMIT 1";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new IllegalStateException("Ez dago hornitzaile erregistraturik material berriak sortzeko.");
    }

    private static int lortuHornitzaileId(Materiala materiala, Connection conn) throws Exception {
        if (materiala.getHornitzaileId() > 0) {
            return materiala.getHornitzaileId();
        }
        return lortuHornitzaileLehenetsia(conn);
    }

    private static Materiala mapRow(ResultSet rs) throws Exception {
        Materiala materiala = new Materiala();
        materiala.setId(rs.getInt("id"));
        materiala.setIzena(rs.getString("izena"));
        materiala.setPrezioa(rs.getDouble("prezioa"));
        materiala.setStock(rs.getInt("stock"));
        materiala.setHornitzaileId(rs.getInt("hornitzaileak_id"));
        materiala.setHornitzaileIzena(rs.getString("hornitzaile_izena"));
        return materiala;
    }
}
