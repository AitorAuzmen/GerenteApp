package DatuBasea;

import Util.Conn;
import model.Hornitzailea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HornitzaileakDB {

    public static List<Hornitzailea> lortuGuztiak() {
        List<Hornitzailea> lista = new ArrayList<>();
        String sql = "SELECT id, izena, kontaktua, helbidea FROM hornitzaileak ORDER BY izena";

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

    public static boolean insert(Hornitzailea hornitzailea) {
        String sql = "INSERT INTO hornitzaileak (izena, kontaktua, helbidea) VALUES (?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, hornitzailea.getIzena());
            pst.setString(2, hornitzailea.getKontaktua());
            pst.setString(3, hornitzailea.getHelbidea());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Hornitzailea hornitzailea) {
        String sql = "UPDATE hornitzaileak SET izena = ?, kontaktua = ?, helbidea = ? WHERE id = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, hornitzailea.getIzena());
            pst.setString(2, hornitzailea.getKontaktua());
            pst.setString(3, hornitzailea.getHelbidea());
            pst.setInt(4, hornitzailea.getId());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM hornitzaileak WHERE id = ?";

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
        String sql = """
            SELECT
                (SELECT COUNT(*) FROM osagaiak WHERE hornitzaileak_id = ?) +
                (SELECT COUNT(*) FROM materialak WHERE hornitzaileak_id = ?) AS guztira
        """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setInt(2, id);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt("guztira") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static Hornitzailea mapRow(ResultSet rs) throws Exception {
        Hornitzailea hornitzailea = new Hornitzailea();
        hornitzailea.setId(rs.getInt("id"));
        hornitzailea.setIzena(rs.getString("izena"));
        hornitzailea.setKontaktua(rs.getString("kontaktua"));
        hornitzailea.setHelbidea(rs.getString("helbidea"));
        return hornitzailea;
    }
}