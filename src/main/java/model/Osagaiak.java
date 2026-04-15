package model;

public class Osagaiak {
    private int id;
    private String izena;
    private double stock;
    private int hornitzaileId;
    private String hornitzaileIzena;

    public Osagaiak() {}

    public Osagaiak(int id, String izena, double stock) {
        this.id = id;
        this.izena = izena;
        this.stock = stock;
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

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getStock_aktuala() {
        return stock;
    }

    public void setStock_aktuala(double stock_aktuala) {
        this.stock = stock_aktuala;
    }

    public String getUnitatea() {
        return "unitate";
    }

    public void setUnitatea(String unitatea) {
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
        return "Osagaiak{" +
                "id=" + id +
                ", izena='" + izena + '\'' +
                ", stock=" + stock +
                ", hornitzaileId=" + hornitzaileId +
                ", hornitzaileIzena='" + hornitzaileIzena + '\'' +
                '}';
    }
}

