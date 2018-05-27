package com.thudiem.ntt2.newfirebase;

public class SinhVien {

    private String Email;
    private String Phone;
    private String DiaChi;

    public SinhVien() {

    }

    public SinhVien(String email, String phone, String diaChi) {
        Email = email;
        Phone = phone;
        DiaChi = diaChi;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
}
