package model;

public class Osagaiak {
    private int id;
    private String izena;
    private String unitatea;
    private double stock_aktuala;

    public Osagaiak() {}

    public Osagaiak(int id, String izena, String unitatea, double stock_aktuala) {
        this.id = id;
        this.izena = izena;
        this.unitatea = unitatea;
        this.stock_aktuala = stock_aktuala;
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

    public String getUnitatea() {
        return unitatea;
    }

    public void setUnitatea(String unitatea) {
        this.unitatea = unitatea;
    }

    public double getStock_aktuala() {
        return stock_aktuala;
    }

    public void setStock_aktuala(double stock_aktuala) {
        this.stock_aktuala = stock_aktuala;
    }

    @Override
    public String toString() {
        return "Osagaiak{" +
                "id=" + id +
                ", izena='" + izena + '\'' +
                ", unitatea='" + unitatea + '\'' +
                ", stock_aktuala=" + stock_aktuala +
                '}';
    }
}

