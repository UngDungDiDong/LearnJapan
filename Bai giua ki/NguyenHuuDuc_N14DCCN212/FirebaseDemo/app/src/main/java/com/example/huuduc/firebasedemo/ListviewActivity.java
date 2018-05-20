package com.example.huuduc.firebasedemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.huuduc.firebasedemo.adapter.ListViewAdapter;
import com.example.huuduc.firebasedemo.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by huuduc on 10/04/2018.
 */

public class ListviewActivity extends AppCompatActivity {

    private ListView lvList;
    private ArrayList<Employee> listEmploy;
    private ListViewAdapter adapter;

    private ImageView ivIcon;
    private TextView tvId;
    private TextView tvName;

    private DatabaseReference mReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        ivIcon = findViewById(R.id.ivIcon);
        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        lvList = findViewById(R.id.lvList);

        mReference = FirebaseDatabase.getInstance().getReference();

        listEmploy = new ArrayList<>();
        adapter = new ListViewAdapter(this, R.layout.item_list, listEmploy);
        lvList.setAdapter(adapter);

        new LoadData().execute();

    }

    private void getData() {
        mReference.child("Employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Employee employee = ds.getValue(Employee.class);
            listEmploy.add(employee);
        }
        if (listEmploy.size() > 0)
            adapter.notifyDataSetChanged();
    }

    public class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getData();
            return null;
        }
    }
}
