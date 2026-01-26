
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
        String sql = "SELECT id, izena, unitatea, stock_aktuala FROM osagaiak";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Osagaiak o = new Osagaiak();
                o.setId(rs.getInt("id"));
                o.setIzena(rs.getString("izena"));
                o.setUnitatea(rs.getString("unitatea"));
                o.setStock_aktuala(rs.getDouble("stock_aktuala"));
                lista.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Osagaiak lortuById(int id) {
        String sql = "SELECT id, izena, unitatea, stock_aktuala FROM osagaiak WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Osagaiak o = new Osagaiak();
                    o.setId(rs.getInt("id"));
                    o.setIzena(rs.getString("izena"));
                    o.setUnitatea(rs.getString("unitatea"));
                    o.setStock_aktuala(rs.getDouble("stock_aktuala"));
                    return o;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insert(Osagaiak o) {
        String sql = "INSERT INTO osagaiak (izena, unitatea, stock_aktuala) VALUES (?, ?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, o.getIzena());
            pst.setString(2, o.getUnitatea());
            pst.setDouble(3, o.getStock_aktuala());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Osagaiak o) {
        String sql = "UPDATE osagaiak SET izena = ?, unitatea = ?, stock_aktuala = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, o.getIzena());
            pst.setString(2, o.getUnitatea());
            pst.setDouble(3, o.getStock_aktuala());
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
}
