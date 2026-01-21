package model;

public class ProduktuOsagaia {

    private int produktuaId; 
    private int osagaiaId;
    private String izena;
    private double kantitatea;
    private String unitatea;

    public ProduktuOsagaia(int produktuaId, int osagaiaId, String izena, double kantitatea, String unitatea) {
        this.produktuaId = produktuaId;
        this.osagaiaId = osagaiaId;
        this.izena = izena;
        this.kantitatea = kantitatea;
        this.unitatea = unitatea;
    }

    
    public int getProduktuaId() { return produktuaId; }
    public int getOsagaiaId() { return osagaiaId; }
    public String getIzena() { return izena; }
    public double getKantitatea() { return kantitatea; }
    public String getUnitatea() { return unitatea; }

    
    public void setProduktuaId(int produktuaId) { this.produktuaId = produktuaId; }
    public void setOsagaiaId(int osagaiaId) { this.osagaiaId = osagaiaId; }
    public void setIzena(String izena) { this.izena = izena; }
    public void setKantitatea(double kantitatea) { this.kantitatea = kantitatea; }
    public void setUnitatea(String unitatea) { this.unitatea = unitatea; }
}
