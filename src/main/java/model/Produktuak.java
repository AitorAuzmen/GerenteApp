package model;

public class Produktuak {

    private int id;
    private String izena;
    private int kategoriaId;
    private double prezioa;
    private int stockAktuala;

    public Produktuak(int id, String izena, int kategoriaId, double prezioa, int stockAktuala) {
        this.id = id;
        this.izena = izena;
        this.kategoriaId = kategoriaId;
        this.prezioa = prezioa;
        this.stockAktuala = stockAktuala;
    }

    
    public int getId() { return id; }
    public String getIzena() { return izena; }
    public int getKategoriaId() { return kategoriaId; }
    public double getPrezioa() { return prezioa; }
    public int getStockAktuala() { return stockAktuala; }

    
    public void setId(int id) { this.id = id; } 
    public void setIzena(String izena) { this.izena = izena; }
    public void setKategoriaId(int kategoriaId) { this.kategoriaId = kategoriaId; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }
    public void setStockAktuala(int stockAktuala) { this.stockAktuala = stockAktuala; }
}
