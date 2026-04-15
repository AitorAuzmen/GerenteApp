package DatuBasea;

import Util.Conn;
import model.Erreserba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErreserbakDB {

    public static List<Erreserba> lortuGuztiak() {
        List<Erreserba> lista = new ArrayList<>();
        String sql = """
            SELECT e.id, e.bezero_izena, e.telefonoa, e.pertsona_kopurua, e.eguna_ordua,
                   e.prezio_totala, e.ordainduta, e.faktura_ruta, e.langileak_id, e.mahaiak_id,
                   CONCAT(l.izena, ' ', l.abizena) AS langilea_izena,
                   CONCAT('Mahaia ', m.zenbakia, ' - ', m.kokapena) AS mahaia_label
            FROM erreserbak e
            JOIN langileak l ON l.id = e.langileak_id
            JOIN mahaiak m ON m.id = e.mahaiak_id
            ORDER BY e.eguna_ordua DESC
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

    public static boolean insert(Erreserba erreserba) {
        String sql = """
            INSERT INTO erreserbak
            (bezero_izena, telefonoa, pertsona_kopurua, eguna_ordua, prezio_totala,
             ordainduta, faktura_ruta, langileak_id, mahaiak_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setCommonParams(pst, erreserba);
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Erreserba erreserba) {
        String sql = """
            UPDATE erreserbak SET
            bezero_izena = ?, telefonoa = ?, pertsona_kopurua = ?, eguna_ordua = ?,
            prezio_totala = ?, ordainduta = ?, faktura_ruta = ?, langileak_id = ?, mahaiek_id = ?
            WHERE id = ?
        """;

        sql = sql.replace("mahaiek_id", "mahaiak_id");

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setCommonParams(pst, erreserba);
            pst.setInt(10, erreserba.getId());
            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM erreserbak WHERE id = ?";

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
        String sql = "SELECT COUNT(*) AS guztira FROM eskariak WHERE erreserbak_id = ?";

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

    private static void setCommonParams(PreparedStatement pst, Erreserba erreserba) throws Exception {
        pst.setString(1, erreserba.getBezeroIzena());
        pst.setString(2, erreserba.getTelefonoa());
        pst.setInt(3, erreserba.getPertsonaKopurua());
        pst.setTimestamp(4, Timestamp.valueOf(erreserba.getEgunaOrdua()));
        if (erreserba.getPrezioTotala() == null) {
            pst.setNull(5, java.sql.Types.DOUBLE);
        } else {
            pst.setDouble(5, erreserba.getPrezioTotala());
        }
        pst.setBoolean(6, erreserba.isOrdainduta());
        pst.setString(7, erreserba.getFakturaRuta());
        pst.setInt(8, erreserba.getLangileaId());
        pst.setInt(9, erreserba.getMahaiaId());
    }

    private static Erreserba mapRow(ResultSet rs) throws Exception {
        Erreserba erreserba = new Erreserba();
        erreserba.setId(rs.getInt("id"));
        erreserba.setBezeroIzena(rs.getString("bezero_izena"));
        erreserba.setTelefonoa(rs.getString("telefonoa"));
        erreserba.setPertsonaKopurua(rs.getInt("pertsona_kopurua"));
        Timestamp ts = rs.getTimestamp("eguna_ordua");
        erreserba.setEgunaOrdua(ts == null ? LocalDateTime.now() : ts.toLocalDateTime());
        double prezioa = rs.getDouble("prezio_totala");
        erreserba.setPrezioTotala(rs.wasNull() ? null : prezioa);
        erreserba.setOrdainduta(rs.getBoolean("ordainduta"));
        erreserba.setFakturaRuta(rs.getString("faktura_ruta"));
        erreserba.setLangileaId(rs.getInt("langileak_id"));
        erreserba.setMahaiaId(rs.getInt("mahaiak_id"));
        erreserba.setLangileaIzena(rs.getString("langilea_izena"));
        erreserba.setMahaiaLabel(rs.getString("mahaia_label"));
        return erreserba;
    }
}