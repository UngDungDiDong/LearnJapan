package com.example.fstha.quanlysinhviendemo;

import java.util.ArrayList;

/**
 * Created by fstha on 13/04/2018.
 */

public class Data {
    public static Data data;
    ArrayList<SinhVien> arrSV;
    static {
        data = new Data();
    }
    public static Data getData(){
        return data;
    }
}
