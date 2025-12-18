package DatuBasea;

import model.Erabiltzailea;
import Util.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErabiltzaileakDB {

    public Erabiltzailea login(String email, String pasahitza) {
        String sql = """
            SELECT * FROM erabiltzaileak
            WHERE email = ? AND pasahitza = ? AND ezabatua = 0
        """;

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, pasahitza);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Erabiltzailea> getAll() {
        List<Erabiltzailea> lista = new ArrayList<>();
        String sql = "SELECT * FROM erabiltzaileak";

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insert(Erabiltzailea e) {
        String sql = """
            INSERT INTO erabiltzaileak
            (erabiltzailea, email, pasahitza, rola_id, ezabatua, chat)
            VALUES (?, ?, ?, ?, 0, ?)
        """;

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, e.getErabiltzailea());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getPasahitza());
            ps.setInt(4, e.getRolaId());
            ps.setBoolean(5, e.isChat());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Erabiltzailea e) {
        String sql = """
            UPDATE erabiltzaileak SET
            erabiltzailea = ?, email = ?, pasahitza = ?,
            rola_id = ?, chat = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, e.getErabiltzailea());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getPasahitza());
            ps.setInt(4, e.getRolaId());
            ps.setBoolean(5, e.isChat());
            ps.setInt(6, e.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "UPDATE erabiltzaileak SET ezabatua = 1 WHERE id = ?";

        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean berreskuratu(int id) {
        String sql = "UPDATE erabiltzaileak SET ezabatua = 0 WHERE id = ?";
        try (PreparedStatement ps = Conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Erabiltzailea mapResultSet(ResultSet rs) throws SQLException {
        Erabiltzailea e = new Erabiltzailea();
        e.setId(rs.getInt("id"));
        e.setErabiltzailea(rs.getString("erabiltzailea"));
        e.setEmail(rs.getString("email"));
        e.setPasahitza(rs.getString("pasahitza"));
        e.setRolaId(rs.getInt("rola_id"));
        e.setEzabatua(rs.getBoolean("ezabatua"));
        e.setChat(rs.getBoolean("chat"));
        return e;
    }
}
