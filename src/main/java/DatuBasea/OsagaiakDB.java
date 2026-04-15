
package DatuBasea;

import Util.Conn;
import model.Osagaiak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OsagaiakDB {
    public static List<Osagaiak> lortuGuztiak() {
        List<Osagaiak> lista = new ArrayList<>();
        String sql = """
                SELECT o.id, o.izena, o.stock, o.hornitzaileak_id, h.izena AS hornitzaile_izena
                FROM osagaiak o
                LEFT JOIN hornitzaileak h ON h.id = o.hornitzaileak_id
                ORDER BY o.izena
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

    public static Osagaiak lortuById(int id) {
        String sql = """
                SELECT o.id, o.izena, o.stock, o.hornitzaileak_id, h.izena AS hornitzaile_izena
                FROM osagaiak o
                LEFT JOIN hornitzaileak h ON h.id = o.hornitzaileak_id
                WHERE o.id = ?
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

    public static boolean insert(Osagaiak o) {
        String sql = "INSERT INTO osagaiak (izena, prezioa, stock, hornitzaileak_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, o.getIzena());
            pst.setDouble(2, 0.0);
            pst.setDouble(3, o.getStock());
            pst.setInt(4, lortuHornitzaileId(o, conn));
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Osagaiak o) {
        String sql = "UPDATE osagaiak SET izena = ?, stock = ?, hornitzaileak_id = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, o.getIzena());
            pst.setDouble(2, o.getStock());
            pst.setInt(3, lortuHornitzaileId(o, conn));
            pst.setInt(4, o.getId());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM osagaiak WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
        throw new IllegalStateException("Ez dago hornitzaile erregistraturik osagai berriak sortzeko.");
    }

    private static int lortuHornitzaileId(Osagaiak osagaia, Connection conn) throws Exception {
        if (osagaia.getHornitzaileId() > 0) {
            return osagaia.getHornitzaileId();
        }
        return lortuHornitzaileLehenetsia(conn);
    }

    private static Osagaiak mapRow(ResultSet rs) throws Exception {
        Osagaiak o = new Osagaiak();
        o.setId(rs.getInt("id"));
        o.setIzena(rs.getString("izena"));
        o.setStock(rs.getDouble("stock"));
        o.setHornitzaileId(rs.getInt("hornitzaileak_id"));
        o.setHornitzaileIzena(rs.getString("hornitzaile_izena"));
        return o;
    }
}
