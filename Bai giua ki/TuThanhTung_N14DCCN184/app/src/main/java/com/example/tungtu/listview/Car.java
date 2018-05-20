package com.example.tungtu.listview;

/**
 * Created by tungtu on 16/01/2018.
 */

public class Car {
    private String name;
    private int image;
    private String year;

    public Car() {
    }

    public Car(String name, int image, String year) {
        this.name = name;
        this.image = image;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
