package model;

public class ProduktuOsagaia {

    private int produktuaId;
    private int osagaiaId;
    private String izena;
    private double kantitatea;
    private double stock;

    public ProduktuOsagaia(int produktuaId, int osagaiaId, String izena, double kantitatea, double stock) {
        this.produktuaId = produktuaId;
        this.osagaiaId = osagaiaId;
        this.izena = izena;
        this.kantitatea = kantitatea;
        this.stock = stock;
    }

    public int getProduktuaId() { return produktuaId; }
    public int getOsagaiaId() { return osagaiaId; }
    public String getIzena() { return izena; }
    public double getKantitatea() { return kantitatea; }
    public double getStock() { return stock; }

    public void setProduktuaId(int produktuaId) { this.produktuaId = produktuaId; }
    public void setOsagaiaId(int osagaiaId) { this.osagaiaId = osagaiaId; }
    public void setIzena(String izena) { this.izena = izena; }
    public void setKantitatea(double kantitatea) { this.kantitatea = kantitatea; }
    public void setStock(double stock) { this.stock = stock; }
}
