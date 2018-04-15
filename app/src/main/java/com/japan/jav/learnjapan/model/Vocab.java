package com.japan.jav.learnjapan.model;

/**
 * Created by thanh on 4/14/2018.
 */

public class Vocab {
    private String tuVung;
    private String hiragana ;
    private String amHan;
    private String nghia;

    public Vocab() {
    }

    public Vocab(String tuVung, String hiragana, String amHan, String nghia) {
        this.tuVung = tuVung;
        this.hiragana = hiragana;
        this.amHan = amHan;
        this.nghia = nghia;
    }

    public String getTuVung() {
        return tuVung;
    }

    public void setTuVung(String tuVung) {
        this.tuVung = tuVung;
    }

    public String getHiragana() {
        return hiragana;
    }

    public void setHiragana(String hiragana) {
        this.hiragana = hiragana;
    }

    public String getAmHan() {
        return amHan;
    }

    public void setAmHan(String amHan) {
        this.amHan = amHan;
    }

    public String getNghia() {
        return nghia;
    }

    public void setNghia(String nghia) {
        this.nghia = nghia;
    }
}
