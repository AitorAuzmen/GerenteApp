package model;

public class Materiala {

    private int id;
    private String izena;
    private double prezioa;
    private int stock;
    private int hornitzaileId;
    private String hornitzaileIzena;

    public Materiala() {
    }

    public Materiala(int id, String izena, double prezioa, int stock, int hornitzaileId, String hornitzaileIzena) {
        this.id = id;
        this.izena = izena;
        this.prezioa = prezioa;
        this.stock = stock;
        this.hornitzaileId = hornitzaileId;
        this.hornitzaileIzena = hornitzaileIzena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public double getPrezioa() {
        return prezioa;
    }

    public void setPrezioa(double prezioa) {
        this.prezioa = prezioa;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getHornitzaileId() {
        return hornitzaileId;
    }

    public void setHornitzaileId(int hornitzaileId) {
        this.hornitzaileId = hornitzaileId;
    }

    public String getHornitzaileIzena() {
        return hornitzaileIzena;
    }

    public void setHornitzaileIzena(String hornitzaileIzena) {
        this.hornitzaileIzena = hornitzaileIzena;
    }

    @Override
    public String toString() {
        return izena;
    }
}
