package model;

public class Erosketa {

    private int id;
    private int hornitzaileaId;
    private String hornitzaileIzena;
    private Integer osagaiaId;
    private String osagaiaIzena;
    private Integer materialaId;
    private String materialaIzena;
    private double prezioa;
    private int kantitatea;

    public Erosketa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHornitzaileaId() {
        return hornitzaileaId;
    }

    public void setHornitzaileaId(int hornitzaileaId) {
        this.hornitzaileaId = hornitzaileaId;
    }

    public String getHornitzaileIzena() {
        return hornitzaileIzena;
    }

    public void setHornitzaileIzena(String hornitzaileIzena) {
        this.hornitzaileIzena = hornitzaileIzena;
    }

    public Integer getOsagaiaId() {
        return osagaiaId;
    }

    public void setOsagaiaId(Integer osagaiaId) {
        this.osagaiaId = osagaiaId;
    }

    public String getOsagaiaIzena() {
        return osagaiaIzena;
    }

    public void setOsagaiaIzena(String osagaiaIzena) {
        this.osagaiaIzena = osagaiaIzena;
    }

    public Integer getMaterialaId() {
        return materialaId;
    }

    public void setMaterialaId(Integer materialaId) {
        this.materialaId = materialaId;
    }

    public String getMaterialaIzena() {
        return materialaIzena;
    }

    public void setMaterialaIzena(String materialaIzena) {
        this.materialaIzena = materialaIzena;
    }

    public double getPrezioa() {
        return prezioa;
    }

    public void setPrezioa(double prezioa) {
        this.prezioa = prezioa;
    }

    public int getKantitatea() {
        return kantitatea;
    }

    public void setKantitatea(int kantitatea) {
        this.kantitatea = kantitatea;
    }

    public String getErosketaElementua() {
        if (osagaiaId != null) return osagaiaIzena;
        if (materialaId != null) return materialaIzena;
        return "";
    }
}
