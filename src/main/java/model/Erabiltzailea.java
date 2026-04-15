
package model;

public class Erabiltzailea {

    private int id;
    private String izena;
    private String abizena;
    private String erabiltzailea;
    private int langileKodea;
    private String pasahitza;
    private int rolaId;
    private boolean ezabatua;
    private boolean chat;

    public Erabiltzailea() {}

    public Erabiltzailea(int id, String izena, String abizena,
                         String erabiltzailea, int langileKodea,
                         String pasahitza, int rolaId,
                         boolean ezabatua, boolean chat) {
        this.id = id;
        this.izena = izena;
        this.abizena = abizena;
        this.erabiltzailea = erabiltzailea;
        this.langileKodea = langileKodea;
        this.pasahitza = pasahitza;
        this.rolaId = rolaId;
        this.ezabatua = ezabatua;
        this.chat = chat;
    }

    public Erabiltzailea(String erabiltzailea, String pasahitza) {
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
    }

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getAbizena() { return abizena; }
    public void setAbizena(String abizena) { this.abizena = abizena; }

    public String getErabiltzailea() { return erabiltzailea; }
    public void setErabiltzailea(String erabiltzailea) { this.erabiltzailea = erabiltzailea; }

    public int getLangileKodea() { return langileKodea; }
    public void setLangileKodea(int langileKodea) { this.langileKodea = langileKodea; }

    public String getPasahitza() { return pasahitza; }
    public void setPasahitza(String pasahitza) { this.pasahitza = pasahitza; }

    public int getRolaId() { return rolaId; }
    public void setRolaId(int rolaId) { this.rolaId = rolaId; }

    public boolean isEzabatua() { return ezabatua; }
    public void setEzabatua(boolean ezabatua) { this.ezabatua = ezabatua; }

    public boolean isChat() { return chat; }
    public void setChat(boolean chat) { this.chat = chat; }

    
    public String getRola() {
        switch (rolaId) {
            case 1: return "administratzailea";
            case 2: return "zerbitzaria";
            case 3: return "sukaldaria";
            default: return "Desconocido";
        }
    }
}
