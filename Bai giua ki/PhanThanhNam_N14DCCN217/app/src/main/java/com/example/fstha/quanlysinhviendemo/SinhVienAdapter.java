package com.example.fstha.quanlysinhviendemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by fstha on 13/04/2018.
 */

public class SinhVienAdapter extends ArrayAdapter<SinhVien>{
    ArrayList<SinhVien> arr;
    Context ct;

    public SinhVienAdapter(@NonNull Context context, int resource, ArrayList<SinhVien> o) {
        super(context, resource, o);
        this.ct = context;
        this.arr = o;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder v;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_sinh_vien, null);
            v = new ViewHolder(row);
            row.setTag(v);
        }
        else{
            v = (ViewHolder)row.getTag();
        }
        if(arr.size() > 0){
            System.out.println(arr.size());
            v.setView(arr.get(position));
        }
        return row;
    }

    class ViewHolder{
        TextView txvName, txvPhone, txvEmail;
        ImageView imgGender;

        public ViewHolder(View v) {
            txvName = (TextView) v.findViewById(R.id.txvName);
            txvPhone = (TextView) v.findViewById(R.id.txvPhone);
            txvEmail = (TextView) v.findViewById(R.id.txvEmail);
            imgGender = (ImageView) v.findViewById(R.id.imgGender);
        }

        public void setView(SinhVien sv){
            txvName.setText(sv.getName());
            txvEmail.setText(sv.getEmail());
            txvPhone.setText(sv.getPhone());

            if(sv.getGender() == true){
                imgGender.setImageResource(R.drawable.nam);
            }
            else {
                imgGender.setImageResource(R.drawable.nu);
            }

        }
    }

    public interface OnClickedListenner{
        void OnSelectedItem(int position);
    }
}
