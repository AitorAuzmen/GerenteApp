package model;

public class Erabiltzailea {
    private String erabiltzailea;
    private String pasahitza;

    public Erabiltzailea(String erabiltzailea, String pasahitza) {
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
    }

    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public String getPasahitza() {
        return pasahitza;
    }
}
