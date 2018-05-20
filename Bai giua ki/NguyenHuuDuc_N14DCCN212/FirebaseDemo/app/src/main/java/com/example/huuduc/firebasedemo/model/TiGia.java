package com.example.huuduc.firebasedemo.model;

/**
 * Created by huuduc on 09/04/2018.
 */

public class TiGia {

    private String USD;
    private String Euro;

    public TiGia() {
    }

    public TiGia(String USD, String euro) {
        this.USD = USD;
        Euro = euro;
    }

    public String getUSD() {
        return USD;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public String getEuro() {
        return Euro;
    }

    public void setEuro(String euro) {
        Euro = euro;
    }
}
