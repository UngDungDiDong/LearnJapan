package com.japan.jav.learnjapan.test_feature_khang_duc.view.model;

public class TestResult {
    String userId;
    int soCauDung;
    int soCauSai;

    public TestResult() {
    }

    public TestResult(String userId, int soCauDung, int soCauSai) {
        this.userId = userId;
        this.soCauDung = soCauDung;
        this.soCauSai = soCauSai;
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

    public int getSoCauSai() {
        return soCauSai;
    }

    public void setSoCauSai(int soCauSai) {
        this.soCauSai = soCauSai;
    }
}
