package model;

import java.time.LocalDateTime;

public class Erreserba {
    private int id;
    private String bezeroIzena;
    private String telefonoa;
    private int pertsonaKopurua;
    private LocalDateTime egunaOrdua;
    private Double prezioTotala;
    private boolean ordainduta;
    private String fakturaRuta;
    private int langileaId;
    private int mahaiaId;
    private String langileaIzena;
    private String mahaiaLabel;

    public Erreserba() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeroIzena() {
        return bezeroIzena;
    }

    public void setBezeroIzena(String bezeroIzena) {
        this.bezeroIzena = bezeroIzena;
    }

    public String getTelefonoa() {
        return telefonoa;
    }

    public void setTelefonoa(String telefonoa) {
        this.telefonoa = telefonoa;
    }

    public int getPertsonaKopurua() {
        return pertsonaKopurua;
    }

    public void setPertsonaKopurua(int pertsonaKopurua) {
        this.pertsonaKopurua = pertsonaKopurua;
    }

    public LocalDateTime getEgunaOrdua() {
        return egunaOrdua;
    }

    public void setEgunaOrdua(LocalDateTime egunaOrdua) {
        this.egunaOrdua = egunaOrdua;
    }

    public Double getPrezioTotala() {
        return prezioTotala;
    }

    public void setPrezioTotala(Double prezioTotala) {
        this.prezioTotala = prezioTotala;
    }

    public boolean isOrdainduta() {
        return ordainduta;
    }

    public void setOrdainduta(boolean ordainduta) {
        this.ordainduta = ordainduta;
    }

    public String getFakturaRuta() {
        return fakturaRuta;
    }

    public void setFakturaRuta(String fakturaRuta) {
        this.fakturaRuta = fakturaRuta;
    }

    public int getLangileaId() {
        return langileaId;
    }

    public void setLangileaId(int langileaId) {
        this.langileaId = langileaId;
    }

    public int getMahaiaId() {
        return mahaiaId;
    }

    public void setMahaiaId(int mahaiaId) {
        this.mahaiaId = mahaiaId;
    }

    public String getLangileaIzena() {
        return langileaIzena;
    }

    public void setLangileaIzena(String langileaIzena) {
        this.langileaIzena = langileaIzena;
    }

    public String getMahaiaLabel() {
        return mahaiaLabel;
    }

    public void setMahaiaLabel(String mahaiaLabel) {
        this.mahaiaLabel = mahaiaLabel;
    }
}