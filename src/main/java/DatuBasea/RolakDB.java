package DatuBasea;

import Util.Conn;
import model.Rolak;
import model.Rolak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RolakDB {

    public static List<Rolak> lortuGuztiak() {
        List<Rolak> lista = new ArrayList<>();
        String sql = "SELECT id, izena FROM rolak";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Rolak r = new Rolak();
                r.setId(rs.getInt("id"));
                r.setIzena(rs.getString("izena"));
                lista.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean insert(Rolak r) {
        String sql = "INSERT INTO rolak (izena) VALUES (?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, r.getIzena());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Rolak r) {
        String sql = "UPDATE rolak SET izena = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, r.getIzena());
            pst.setInt(2, r.getId());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM rolak WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
