package DatuBasea;

import Util.Conn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Produktuak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProduktuakDB {

    public static ObservableList<Produktuak> lortuGuztiak() {
        ObservableList<Produktuak> lista = FXCollections.observableArrayList();
        String sql = "SELECT id, izena, kategoria_id, prezioa, stock_aktuala FROM produktuak";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Produktuak p = new Produktuak();
                p.setId(rs.getInt("id"));
                p.setIzena(rs.getString("izena"));
                p.setKategoria_id(rs.getInt("kategoria_id"));
                p.setPrezioa(rs.getDouble("prezioa"));
                p.setStock_aktuala(rs.getInt("stock_aktuala"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean insert(Produktuak p) {
        String sql = "INSERT INTO produktuak (izena, kategoria_id, prezioa, stock_aktuala) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, p.getIzena());
            pst.setInt(2, p.getKategoria_id());
            pst.setDouble(3, p.getPrezioa());
            pst.setInt(4, p.getStock_aktuala());

            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Produktuak p) {
        String sql = "UPDATE produktuak SET izena = ?, kategoria_id = ?, prezioa = ?, stock_aktuala = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, p.getIzena());
            pst.setInt(2, p.getKategoria_id());
            pst.setDouble(3, p.getPrezioa());
            pst.setInt(4, p.getStock_aktuala());
            pst.setInt(5, p.getId());

            return pst.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM produktuak WHERE id = ?";
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
