package model;

public class Produktuak {

    private int id;
    private String izena;
    private String mota;
    private double prezioa;
    private int stock;

    public Produktuak(int id, String izena, String mota, double prezioa, int stock) {
        this.id = id;
        this.izena = izena;
        this.mota = mota;
        this.prezioa = prezioa;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getIzena() { return izena; }
    public String getMota() { return mota; }
    public double getPrezioa() { return prezioa; }
    public int getStock() { return stock; }

    public void setId(int id) { this.id = id; } 
    public void setIzena(String izena) { this.izena = izena; }
    public void setMota(String mota) { this.mota = mota; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }
    public void setStock(int stock) { this.stock = stock; }
}
