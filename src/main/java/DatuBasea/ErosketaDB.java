package DatuBasea;

import Util.Conn;
import model.Erosketa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ErosketaDB {

    public static List<Erosketa> lortuGuztiak() {
        List<Erosketa> lista = new ArrayList<>();
        String sql = """
                SELECT e.id,
                       e.hornitzailea_id, h.izena AS hornitzaile_izena,
                       e.osagaia_id, o.izena AS osagaia_izena,
                       e.materiala_id, m.izena AS materiala_izena,
                       e.prezioa, e.kantitatea
                FROM erosketa e
                JOIN hornitzaileak h ON h.id = e.hornitzailea_id
                LEFT JOIN osagaiak o ON o.id = e.osagaia_id
                LEFT JOIN materialak m ON m.id = e.materiala_id
                ORDER BY e.id DESC
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

    public static boolean insert(Erosketa erosketa) {
        boolean osagaiaDa = erosketa.getOsagaiaId() != null;
        boolean materialaDa = erosketa.getMaterialaId() != null;
        if (osagaiaDa == materialaDa) {
            return false;
        }

        String insertSql = "INSERT INTO erosketa (hornitzailea_id, osagaia_id, prezioa, kantitatea, materiala_id) VALUES (?, ?, ?, ?, ?)";
        String stockSql = osagaiaDa
                ? "UPDATE osagaiak SET stock = stock + ? WHERE id = ?"
                : "UPDATE materialak SET stock = stock + ? WHERE id = ?";

        try (Connection conn = Conn.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pst = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pstStock = conn.prepareStatement(stockSql)) {

                pst.setInt(1, erosketa.getHornitzaileaId());
                if (osagaiaDa) {
                    pst.setInt(2, erosketa.getOsagaiaId());
                } else {
                    pst.setNull(2, Types.INTEGER);
                }
                pst.setDouble(3, erosketa.getPrezioa());
                pst.setInt(4, erosketa.getKantitatea());
                if (materialaDa) {
                    pst.setInt(5, erosketa.getMaterialaId());
                } else {
                    pst.setNull(5, Types.INTEGER);
                }

                if (pst.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet keys = pst.getGeneratedKeys()) {
                    if (keys.next()) {
                        erosketa.setId(keys.getInt(1));
                    }
                }

                pstStock.setInt(1, erosketa.getKantitatea());
                pstStock.setInt(2, osagaiaDa ? erosketa.getOsagaiaId() : erosketa.getMaterialaId());
                pstStock.executeUpdate();

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        Erosketa erosketa = lortuById(id);
        if (erosketa == null) return false;

        boolean osagaiaDa = erosketa.getOsagaiaId() != null;
        boolean materialaDa = erosketa.getMaterialaId() != null;
        if (osagaiaDa == materialaDa) return false;

        String deleteSql = "DELETE FROM erosketa WHERE id = ?";
        String stockSql = osagaiaDa
                ? "UPDATE osagaiak SET stock = stock - ? WHERE id = ?"
                : "UPDATE materialak SET stock = stock - ? WHERE id = ?";

        try (Connection conn = Conn.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstDel = conn.prepareStatement(deleteSql);
                 PreparedStatement pstStock = conn.prepareStatement(stockSql)) {

                pstDel.setInt(1, id);
                if (pstDel.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }

                pstStock.setInt(1, erosketa.getKantitatea());
                pstStock.setInt(2, osagaiaDa ? erosketa.getOsagaiaId() : erosketa.getMaterialaId());
                pstStock.executeUpdate();

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Erosketa lortuById(int id) {
        String sql = """
                SELECT e.id,
                       e.hornitzailea_id, h.izena AS hornitzaile_izena,
                       e.osagaia_id, o.izena AS osagaia_izena,
                       e.materiala_id, m.izena AS materiala_izena,
                       e.prezioa, e.kantitatea
                FROM erosketa e
                JOIN hornitzaileak h ON h.id = e.hornitzailea_id
                LEFT JOIN osagaiak o ON o.id = e.osagaia_id
                LEFT JOIN materialak m ON m.id = e.materiala_id
                WHERE e.id = ?
                """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Erosketa mapRow(ResultSet rs) throws Exception {
        Erosketa erosketa = new Erosketa();
        erosketa.setId(rs.getInt("id"));
        erosketa.setHornitzaileaId(rs.getInt("hornitzailea_id"));
        erosketa.setHornitzaileIzena(rs.getString("hornitzaile_izena"));

        Integer osagaiaId = rs.getObject("osagaia_id") == null ? null : rs.getInt("osagaia_id");
        erosketa.setOsagaiaId(osagaiaId);
        erosketa.setOsagaiaIzena(rs.getString("osagaia_izena"));

        Integer materialaId = rs.getObject("materiala_id") == null ? null : rs.getInt("materiala_id");
        erosketa.setMaterialaId(materialaId);
        erosketa.setMaterialaIzena(rs.getString("materiala_izena"));

        erosketa.setPrezioa(rs.getDouble("prezioa"));
        erosketa.setKantitatea(rs.getInt("kantitatea"));
        return erosketa;
    }
}
