package com.example.tungtu.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CarAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Car> carList;

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public CarAdapter(Context context, int layout, List<Car> carList) {
        this.context = context;
        this.layout = layout;
        this.carList = carList;
    }

    private class ViewHolder{
        ImageView imgCar;
        TextView txtName, txtYear;
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);

            viewHolder.txtName = view.findViewById(R.id.txtName);
            viewHolder.txtYear = view.findViewById(R.id.txtYear);
            viewHolder.imgCar = view.findViewById(R.id.imgCar);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }


        Car car = carList.get(i);

        viewHolder.txtName.setText(car.getName());
        viewHolder.txtYear.setText(car.getYear() + "");
        viewHolder.imgCar.setImageResource(car.getImage());

        return view;
    }
}
