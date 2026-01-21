package model;

public class Mahaia {

    private int id;
    private int zenbakia;
    private String egoera; 

    public Mahaia(int id, int zenbakia, String egoera) {
        this.id = id;
        this.zenbakia = zenbakia;
        this.egoera = egoera;
    }

    public int getId() { return id; }
    public int getZenbakia() { return zenbakia; }
    public String getEgoera() { return egoera; }

    public void setId(int id) { this.id = id; }
    public void setZenbakia(int zenbakia) { this.zenbakia = zenbakia; }
    public void setEgoera(String egoera) { this.egoera = egoera; }
}
