package com.example.huuduc.firebasedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuduc.firebasedemo.R;
import com.example.huuduc.firebasedemo.model.Employee;

import java.util.ArrayList;

/**
 * Created by huuduc on 10/04/2018.
 */

public class ListViewAdapter extends ArrayAdapter<Employee> {

    private Context context;
    private ArrayList<Employee> listEmploy;
    private int resouceID;

    public ListViewAdapter(Context context, int resource, ArrayList<Employee> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resouceID = resource;
        this.listEmploy = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        convertView = layoutInflater.inflate(this.resouceID, null);

        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvName = convertView.findViewById(R.id.tvName);
        ImageView ivIcon = convertView.findViewById(R.id.ivIcon);

        Employee employee = this.listEmploy.get(position);
        tvId.setText(employee.getId());
        tvName.setText(employee.getName());
        if (employee.isGender()){
            ivIcon.setImageResource(R.drawable.boyicon);
        }else{
            ivIcon.setImageResource(R.drawable.girlicon);
        }


        return convertView;
    }
}
