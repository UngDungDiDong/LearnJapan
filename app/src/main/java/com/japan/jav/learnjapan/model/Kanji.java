package com.japan.jav.learnjapan.model;

import java.io.Serializable;

public class Kanji implements Serializable {
    private String id;
    private String amhan;
    private String kanji;
    private String tuvung;
    private String photo;

    public Kanji(String id, String amhan, String kanji, String tuvung, String photo) {
        this.id = id;
        this.amhan = amhan;
        this.kanji = kanji;
        this.tuvung = tuvung;
        this.photo = photo;
    }
    public Kanji(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmhan() {
        return amhan;
    }

    public void setAmhan(String amhan) {
        this.amhan = amhan;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getTuvung() {
        return tuvung;
    }

    public void setTuvung(String tuvung) {
        this.tuvung = tuvung;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
