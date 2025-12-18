// java
package DatuBasea;

import Util.Conn;
import model.Osagaiak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProduktuOsagaiakDB {

    public static List<Osagaiak> lortuForProduktua(int produktuId) {
        List<Osagaiak> lista = new ArrayList<>();
        String sql = "SELECT o.id, o.izena, o.unitatea, o.stock_aktuala, po.kantitatea " +
                "FROM osagaiak o " +
                "JOIN produktu_osagaiak po ON o.id = po.osagarri_id " +
                "WHERE po.produktua_id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, produktuId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Osagaiak o = new Osagaiak();
                    o.setId(rs.getInt("id"));
                    o.setIzena(rs.getString("izena"));
                    o.setUnitatea(rs.getString("unitatea"));
                    o.setStock_aktuala(rs.getInt("stock_aktuala"));

                    lista.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }


    public static boolean saveForProduktua(Connection conn, int produktuId, List<Osagaiak> list) throws Exception {
        String deleteSql = "DELETE FROM produktu_osagaiak WHERE produktua_id = ?";
        String insertSql = "INSERT INTO produktu_osagaiak (produktua_id, osagarri_id, kantitatea) VALUES (?, ?, ?)";
        try (PreparedStatement del = conn.prepareStatement(deleteSql)) {
            del.setInt(1, produktuId);
            del.executeUpdate();
        }
        try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
            for (Osagaiak o : list) {
                ins.setInt(1, produktuId);
                ins.setInt(2, o.getId());
                ins.setDouble(3, 1.0);
                ins.addBatch();
            }
            ins.executeBatch();
        }
        return true;
    }


    public static boolean saveForProduktua(int produktuId, List<Osagaiak> list) {
        Connection conn = null;
        boolean originalAuto = true;
        try {
            conn = Conn.getConnection();
            originalAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            saveForProduktua(conn, produktuId, list);
            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ignored) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(originalAuto); conn.close(); } catch (Exception ignored) {}
            }
        }
    }
}
