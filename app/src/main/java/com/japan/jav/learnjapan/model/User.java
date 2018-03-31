package com.japan.jav.learnjapan.model;

/**
 * Created by trungnguyeen on 3/31/18.
 */

public class User {

    private String id;
    private String username;
    private String email;
    private String linkPhoto;

    public User() {
    }

    public User(String id, String username, String email, String linkPhoto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.linkPhoto = linkPhoto;
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