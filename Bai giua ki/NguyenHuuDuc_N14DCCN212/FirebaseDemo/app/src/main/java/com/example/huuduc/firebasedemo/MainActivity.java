package com.example.huuduc.firebasedemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huuduc.firebasedemo.adapter.EmployeeAdapter;
import com.example.huuduc.firebasedemo.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView ivIcon;
    private TextView tvId;
    private TextView tvName;
    private DatabaseReference mReference;
    private EmployeeAdapter adapter;
    private ArrayList<Employee> listEmploy;
    private RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
//        saveDummyData();
        new LoadData().execute();
        Log.d("SIZE", listEmploy.size() + "");

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
    }

    private void addControls() {
        ivIcon = findViewById(R.id.ivIcon);
        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        rvList = findViewById(R.id.rvList);
        mReference = FirebaseDatabase.getInstance().getReference();
        listEmploy = new ArrayList<>();

        adapter = new EmployeeAdapter(this, listEmploy);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
    }

    private void saveDummyData() {
        Employee ee1 = new Employee("1", "Nguyen Van A", true);
        Employee ee2 = new Employee("2", "Nguyen Van B", false);
        Employee ee3 = new Employee("3", "Nguyen Van C", true);
        Employee ee4 = new Employee("4", "Nguyen Van D", false);
        Employee ee5 = new Employee("5", "Nguyen Van E", false);
        Employee ee6 = new Employee("6", "Nguyen Van F", true);
        Employee ee7 = new Employee("7", "Nguyen Van G", true);

        listEmploy.add(ee1);
        listEmploy.add(ee2);
        listEmploy.add(ee3);
        listEmploy.add(ee4);
        listEmploy.add(ee5);
        listEmploy.add(ee6);
        listEmploy.add(ee7);


        for (int i = 0; i < listEmploy.size(); i++){
            Employee employee = listEmploy.get(i);
            mReference.child("Employee").child(employee.getId()).setValue(employee);
        }
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
