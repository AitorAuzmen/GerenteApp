package DatuBasea;

import model.Erabiltzailea;
import Util.Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginManager {

    private String azkenErrorea;

    public String getAzkenErrorea() {
        return azkenErrorea;
    }

    public boolean saioaHasi(Erabiltzailea erabiltzailea) {
        azkenErrorea = null;
        String identifikatzailea = erabiltzailea.getErabiltzailea();
        boolean kodeaDa = false;
        int langileKodea = 0;

        try {
            langileKodea = Integer.parseInt(identifikatzailea);
            kodeaDa = true;
        } catch (NumberFormatException ignored) {
        }

        String sql = """
            SELECT id, izena, abizena, erabiltzaile_izena, langile_kodea, pasahitza, rola_id, ezabatua, chat
            FROM langileak
            WHERE %s AND pasahitza = ? AND ezabatua = 0
        """.formatted(kodeaDa ? "langile_kodea = ?" : "erabiltzaile_izena = ?");

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (kodeaDa) {
                stmt.setInt(1, langileKodea);
            } else {
                stmt.setString(1, identifikatzailea);
            }
            stmt.setString(2, erabiltzailea.getPasahitza());

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    azkenErrorea = "Erabiltzailea edo pasahitza okerra";
                    return false;
                }
                erabiltzailea.setId(rs.getInt("id"));
                erabiltzailea.setIzena(rs.getString("izena"));
                erabiltzailea.setAbizena(rs.getString("abizena"));
                erabiltzailea.setErabiltzailea(rs.getString("erabiltzaile_izena"));
                erabiltzailea.setLangileKodea(rs.getInt("langile_kodea"));
                erabiltzailea.setPasahitza(rs.getString("pasahitza"));
                erabiltzailea.setRolaId(rs.getInt("rola_id"));
                erabiltzailea.setEzabatua(rs.getBoolean("ezabatua"));
                erabiltzailea.setChat(rs.getBoolean("chat"));
                return true;
            }
        } catch (SQLException e) {
            azkenErrorea = e.getMessage();
            return false;
        }
    }

    public boolean login(Erabiltzailea erabiltzailea) {
        return saioaHasi(erabiltzailea);
    }
}
