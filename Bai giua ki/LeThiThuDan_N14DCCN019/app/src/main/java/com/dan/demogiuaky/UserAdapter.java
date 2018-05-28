package com.dan.demogiuaky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 16-04-18.
 */

public class UserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> arrUser;

    public UserAdapter(Context context, int layout, List<User> arrUser) {
        this.context = context;
        this.layout = layout;
        this.arrUser = arrUser;
    }


    @Override
    public int getCount() {
        return arrUser.size();
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

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();

            viewHolder.tvName = (TextView) view.findViewById(R.id.textviewName);
            viewHolder.tvEmail = (TextView) view.findViewById(R.id.textviewEmail);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();

        }

        User user = arrUser.get(i);

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvEmail.setText(user.getEmail());


        return view;
    }
    public class ViewHolder {
        TextView tvName, tvEmail ;
    }
}
