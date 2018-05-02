package com.japan.jav.learnjapan.model;

import java.io.Serializable;

public class Moji implements Serializable {

    private String id;
    private String AmHan;
    private String CachDocHira;
    private String NghiaTiengViet;
    private String TuTiengNhat;
    private String photo;

    public Moji() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmHan() {
        return AmHan;
    }

    public void setAmHan(String amHan) {
        AmHan = amHan;
    }

    public String getCachDocHira() {
        return CachDocHira;
    }

    public void setCachDocHira(String cachDocHira) {
        CachDocHira = cachDocHira;
    }

    public String getNghiaTiengViet() {
        return NghiaTiengViet;
    }

    public void setNghiaTiengViet(String nghiaTiengViet) {
        NghiaTiengViet = nghiaTiengViet;
    }

    public String getTuTiengNhat() {
        return TuTiengNhat;
    }

    public void setTuTiengNhat(String tuTiengNhat) {
        TuTiengNhat = tuTiengNhat;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Moji{" +
                "AmHan='" + AmHan + '\'' +
                ", CachDocHira='" + CachDocHira + '\'' +
                ", NghiaTiengViet='" + NghiaTiengViet + '\'' +
                ", TuTiengNhat='" + TuTiengNhat + '\'' +
                '}';
    }
}
