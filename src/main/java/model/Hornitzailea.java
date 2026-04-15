package model;

public class Hornitzailea {
    private int id;
    private String izena;
    private String kontaktua;
    private String helbidea;

    public Hornitzailea() {}

    public Hornitzailea(int id, String izena, String kontaktua, String helbidea) {
        this.id = id;
        this.izena = izena;
        this.kontaktua = kontaktua;
        this.helbidea = helbidea;
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

    public String getKontaktua() {
        return kontaktua;
    }

    public void setKontaktua(String kontaktua) {
        this.kontaktua = kontaktua;
    }

    public String getHelbidea() {
        return helbidea;
    }

    public void setHelbidea(String helbidea) {
        this.helbidea = helbidea;
    }
}