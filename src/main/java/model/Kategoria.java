package model;

public class Kategoria {

    private int id;
    private String izena;

    public Kategoria(int id, String izena) {
        this.id = id;
        this.izena = izena;
    }

    public int getId() { return id; }
    public String getIzena() { return izena; }

    public void setId(int id) { this.id = id; }
    public void setIzena(String izena) { this.izena = izena; }
}
