package com.n14dccn309.thanhle.demofirebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> mList;

    public UserAdapter(@NonNull Context context, int resource, List<User> list) {
        super(context, resource);
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        TextView tvUser = view.findViewById(R.id.tv_user);

        User user = mList.get(position);

        tvUser.setText(user.getName() + " - " + user.getAddress());
        return view;
    }
}
