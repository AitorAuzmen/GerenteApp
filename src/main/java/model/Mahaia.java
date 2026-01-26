package model;

public class Mahaia {

    private int id;
    private int zenbakia;
    private int kapazitatea;
    private String egoera; 

    public Mahaia(int id, int zenbakia, int kapazitatea, String egoera) {
        this.id = id;
        this.zenbakia = zenbakia;
        this.kapazitatea = kapazitatea;
        this.egoera = egoera;
    }

    public int getId() { return id; }
    public int getZenbakia() { return zenbakia; }
    public int getKapazitatea() { return kapazitatea; }
    public String getEgoera() { return egoera; }

    public void setId(int id) { this.id = id; }
    public void setZenbakia(int zenbakia) { this.zenbakia = zenbakia; }
    public void setKapazitatea(int kapazitatea) { this.kapazitatea = kapazitatea; }
    public void setEgoera(String egoera) { this.egoera = egoera; }
}
