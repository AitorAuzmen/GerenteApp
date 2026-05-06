package DatuBasea;

import Util.Conn;
import model.Kategoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KategoriakDB {

    private static int motaId(String mota) {
        if (mota == null) return -1;
        String normalized = mota.trim().toLowerCase();
        if (normalized.isEmpty()) return -1;
        return normalized.hashCode() & 0x7fffffff;
    }

    private static String lortuMotaById(int id) {
        String sql = "SELECT DISTINCT mota FROM produktuak WHERE mota IS NOT NULL AND mota <> ''";
        try (Connection c = Conn.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String mota = rs.getString("mota");
                if (motaId(mota) == id) {
                    return mota;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int produktuKopuruaMota(String mota) {
        if (mota == null || mota.isBlank()) return 0;
        String sql = "SELECT COUNT(*) FROM produktuak WHERE mota = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, mota);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    
    public static List<Kategoria> lortuKategoriak() {
        List<Kategoria> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT mota FROM produktuak WHERE mota IS NOT NULL AND mota <> '' ORDER BY mota";

        try (Connection c = Conn.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String mota = rs.getString("mota");
                lista.add(new Kategoria(motaId(mota), mota));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    
    public static Map<Integer, String> lortuKategoriakMap() {
        Map<Integer, String> mapa = new HashMap<>();
        for (Kategoria k : lortuKategoriak()) {
            mapa.put(k.getId(), k.getIzena());
        }
        return mapa;
    }

    
    public static int gehituKategoria(Kategoria k) {
        return motaId(k == null ? null : k.getIzena());
    }

    
    public static void eguneratuKategoria(Kategoria k) {
        String motaZaharra = lortuMotaById(k.getId());
        if (motaZaharra == null) return;
        String sql = "UPDATE produktuak SET mota = ? WHERE mota = ?";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, k.getIzena());
            ps.setString(2, motaZaharra);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static boolean dagoProdukturik(int kategoriaId) {
        String mota = lortuMotaById(kategoriaId);
        return produktuKopuruaMota(mota) > 0;
    }

    
    public static boolean ezabatuKategoria(int id) {
        String mota = lortuMotaById(id);
        if (mota == null) return true;
        return produktuKopuruaMota(mota) == 0;
    }

    
    public static List<Kategoria> lortuKategoriak(String filtro) {
        List<Kategoria> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT mota FROM produktuak WHERE mota LIKE ? ORDER BY mota";

        try (Connection c = Conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mota = rs.getString("mota");
                lista.add(new Kategoria(motaId(mota), mota));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
