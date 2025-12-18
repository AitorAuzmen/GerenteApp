package model;

public class Produktuak {
    private int id;
    private String izena;
    private int kategoria_id;
    private double prezioa;
    private int stock_aktuala;


    public Produktuak() {}


    public Produktuak(int id, String izena, int kategoria_id, double prezioa, int stock_aktuala) {
        this.id = id;
        this.izena = izena;
        this.kategoria_id = kategoria_id;
        this.prezioa = prezioa;
        this.stock_aktuala = stock_aktuala;
    }


    public int getId() { return id; }
    public String getIzena() { return izena; }
    public int getKategoria_id() { return kategoria_id; }
    public double getPrezioa() { return prezioa; }
    public int getStock_aktuala() { return stock_aktuala; }


    public void setId(int id) { this.id = id; }
    public void setIzena(String izena) { this.izena = izena; }
    public void setKategoria_id(int kategoria_id) { this.kategoria_id = kategoria_id; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }
    public void setStock_aktuala(int stock_aktuala) { this.stock_aktuala = stock_aktuala; }
}
