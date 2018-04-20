package com.japan.jav.learnjapan.chart_diem.model;

public class DataChart {

    String setId;
    String userId;
    int soCauDung;
    int tongSocau;
    int soLanTest;

    public DataChart() {

    }

    public DataChart(String userId, String setId, int soLanTest, int tongSocau, int soCauDung) {
        this.setId = setId;
        this.userId = userId;
        this.soCauDung = soCauDung;
        this.tongSocau = tongSocau;
        this.soLanTest = soLanTest;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(int soCauDung) {
        this.soCauDung = soCauDung;
    }

    public int getTongSocau() {
        return tongSocau;
    }

    public void setTongSocau(int tongSocau) {
        this.tongSocau = tongSocau;
    }

    public int getSoLanTest() {
        return soLanTest;
    }

    public void setSoLanTest(int soLanTest) {
        this.soLanTest = soLanTest;
    }
}

