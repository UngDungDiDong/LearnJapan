package edu.ptit.kalis.customlistviewusesqlitefirebase.model;

import java.util.Arrays;

/**
 * Created by Kalis on 04/09/2018.
 */

public class FakeObject {
    private String name;
    private String owner;
    private float rating;
    private byte[] imgBytes = null;
    private String imgBase64 = "";

    public FakeObject() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public byte[] getImgBytes() {
        return imgBytes;
    }

    public void setImgBytes(byte[] imgBytes) {
        this.imgBytes = imgBytes;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public FakeObject(String name, String owner, float rating, String imgBase64) {
        this.name = name;
        this.owner = owner;
        this.rating = rating;
        this.imgBase64 = imgBase64;
    }

    @Override
    public String toString() {
        return "FakeObject{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", rating=" + rating +
                ", imgBytes=" + Arrays.toString(imgBytes) +
                '}';
    }

    public FakeObject(String name, String owner, float rating, byte[] imgBytes) {

        this.name = name;
        this.owner = owner;
        this.rating = rating;
        this.imgBytes = imgBytes;
    }
}
