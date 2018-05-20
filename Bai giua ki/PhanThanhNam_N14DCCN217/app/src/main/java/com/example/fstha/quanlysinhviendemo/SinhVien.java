package com.example.fstha.quanlysinhviendemo;

/**
 * Created by fstha on 13/04/2018.
 */

public class SinhVien {
    private String name;
    private String phone;
    private String email;
    private Boolean gender;

    public SinhVien() {
    }

    public SinhVien(String name, String phone, String email, Boolean gender) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }

    public Boolean getGender(){
        return gender;
    }

    public void SetGender(Boolean gender){
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
