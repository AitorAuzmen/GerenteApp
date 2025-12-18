package model;

public class Erabiltzailea {

    private int id;
    private String erabiltzailea;
    private String email;
    private String pasahitza;
    private int rolaId;
    private boolean ezabatua;
    private boolean chat;

    // Constructor vacío
    public Erabiltzailea() {
    }

    // Constructor completo
    public Erabiltzailea(int id, String erabiltzailea, String email,
                         String pasahitza, int rolaId,
                         boolean ezabatua, boolean chat) {
        this.id = id;
        this.erabiltzailea = erabiltzailea;
        this.email = email;
        this.pasahitza = pasahitza;
        this.rolaId = rolaId;
        this.ezabatua = ezabatua;
        this.chat = chat;
    }

    // Nuevo constructor solo con nombre de usuario y contraseña
    public Erabiltzailea(String erabiltzailea, String pasahitza) {
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public void setErabiltzailea(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    public int getRolaId() {
        return rolaId;
    }

    public void setRolaId(int rolaId) {
        this.rolaId = rolaId;
    }

    public boolean isEzabatua() {
        return ezabatua;
    }

    public void setEzabatua(boolean ezabatua) {
        this.ezabatua = ezabatua;
    }

    public boolean isChat() {
        return chat;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }
}
