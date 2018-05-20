package learnjapan.japan.com.demofirebase;

/**
 * Created by tamlv on 3/31/18.
 */

public class Item {
    private String id;
    private String amHan;
    private String cachDocHira;
    private String nghiaTiengViet;
    private String tuTiengNhat;

    public Item() {
    }

    public Item(String id, String amHan, String cachDocHira, String nghiaTiengViet, String tuTiengNhat) {
        this.id = id;
        this.amHan = amHan;
        this.cachDocHira = cachDocHira;
        this.nghiaTiengViet = nghiaTiengViet;
        this.tuTiengNhat = tuTiengNhat;
    }

    @Override
    public String toString() {
        return "Item(id=" + this.id + ", amHan=" + this.amHan + ", cachDocHira=" + this.cachDocHira + ", nghiaTiengViet=" + this.nghiaTiengViet + ", tuTiengNhat=" + this.tuTiengNhat +")";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmHan() {
        return amHan;
    }

    public void setAmHan(String amHan) {
        this.amHan = amHan;
    }

    public String getCachDocHira() {
        return cachDocHira;
    }

    public void setCachDocHira(String cachDocHira) {
        this.cachDocHira = cachDocHira;
    }

    public String getNghiaTiengViet() {
        return nghiaTiengViet;
    }

    public void setNghiaTiengViet(String nghiaTiengViet) {
        this.nghiaTiengViet = nghiaTiengViet;
    }

    public String getTuTiengNhat() {
        return tuTiengNhat;
    }

    public void setTuTiengNhat(String tuTiengNhat) {
        this.tuTiengNhat = tuTiengNhat;
    }
}
