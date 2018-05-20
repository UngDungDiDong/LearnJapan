package com.example.huuduc.firebasedemo.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuduc.firebasedemo.R;
import com.example.huuduc.firebasedemo.model.Employee;

import java.util.ArrayList;

/**
 * Created by huuduc on 09/04/2018.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private ArrayList<Employee> listEmploy;
    private Context context;

    public EmployeeAdapter(Context context, ArrayList<Employee> listEmploy) {
        this.context = context;
        this.listEmploy = listEmploy;
    }

    public void setListEmploy(ArrayList<Employee> listEmploy) {
        this.listEmploy = listEmploy;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.item_list, parent, false);

        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        Employee employee = this.listEmploy.get(position);
        holder.bindView(employee);
    }

    @Override
    public int getItemCount() {
        return this.listEmploy.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivIcon;
        private TextView tvId;
        private TextView tvName;
        private ImageView ivDelete;
        public ConstraintLayout viewForeground;
        public ConstraintLayout viewBackground;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            viewForeground = itemView.findViewById(R.id.foreground);
            viewBackground = itemView.findViewById(R.id.background);
        }

        public void bindView (Employee employee){
            tvId.setText(employee.getId());
            tvName.setText(employee.getName());
            if (employee.isGender()){
                ivIcon.setImageResource(R.drawable.boyicon);
            }else{
                ivIcon.setImageResource(R.drawable.girlicon);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public void removeItem(int position) {
        listEmploy.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Employee item, int position) {
        listEmploy.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
