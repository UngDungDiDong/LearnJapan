package com.japan.jav.learnjapan.model;

/**
 * Created by trungnguyeen on 3/31/18.
 */

public class User {

    private String id;
    private String password; // dan cmt
    private String username;
    private String email;
    private String linkPhoto;
    private boolean gender;
    private String phone;
    private String dateOfBirth;
    private String address;

    public User() {
    }

    // dan cmt
    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
//
    public User(String id, String username, String email, String linkPhoto, boolean gender, String phone, String dateOfBirth, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.linkPhoto = linkPhoto;
        this.gender = gender;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public User(String id, String username, String email, String linkPhoto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.linkPhoto = linkPhoto;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }
}
