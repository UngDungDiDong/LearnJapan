package com.example.huuduc.firebasedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.huuduc.firebasedemo.adapter.EmployeeAdapter;
import com.example.huuduc.firebasedemo.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private EditText edId;
    private EditText edName;
    private RadioGroup rdGender;
    private RadioButton rdMale;
    private RadioButton rdFemale;
    private Button btnSave;
    private ConstraintLayout newActivity;

    private ArrayList<Employee> listEmploys;
    private EmployeeAdapter adapter;
    private DatabaseReference mReference;
    private RecyclerView  rvList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_save);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edId.getText().toString().trim()) && !TextUtils.isEmpty(edName.getText().toString().trim())) {
                    Boolean gender = rdMale.isChecked();
                    Employee employee = new Employee(edId.getText().toString().trim(), edName.getText().toString().trim(), gender);
                    mReference.child(employee.getId()).setValue(employee);

                    getData();
//                    if (addItemToList(listEmploys, employee) == 0){
//                        listEmploys.add(employee);
//                        adapter.notifyItemInserted(listEmploys.indexOf(employee));
//                    }

                    edId.setText("");
                    edName.setText("");
                    edId.requestFocus();

                    Toast.makeText(NewActivity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private int addItemToList(ArrayList<Employee> listEmploy, Employee employee) {
        for (int i = 0; i < listEmploy.size(); i++) {
            if (listEmploy.get(i).getId().equalsIgnoreCase(employee.getId())) {
                listEmploy.get(i).setName(employee.getName());
                listEmploy.get(i).setGender(employee.isGender());
                return 1;
            }
        }
        return 0;
    }

    private void addControls() {
        newActivity = findViewById(R.id.newActivity);
        edId = findViewById(R.id.edMaSv);
        edName = findViewById(R.id.edName);
        rdGender = findViewById(R.id.rdGender);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        btnSave = findViewById(R.id.btnSave);
        rvList = findViewById(R.id.rvNewList);
        rdGender.check(rdMale.getId());

        listEmploys = new ArrayList<>();
        adapter = new EmployeeAdapter(NewActivity.this, listEmploys);
        mReference = FirebaseDatabase.getInstance().getReference().child("Employee");

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvList);

        getData();
    }

    private void getData() {

        adapter.setListEmploy(listEmploys);
        adapter.notifyDataSetChanged();
        mReference.addValueEventListener(new ValueEventListener() {
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
        listEmploys.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Employee employee = ds.getValue(Employee.class);
            listEmploys.add(employee);
        }
        adapter.setListEmploy(listEmploys);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof EmployeeAdapter.EmployeeViewHolder) {

            final Employee deletedItem = listEmploys.get(viewHolder.getAdapterPosition());

            mReference.child(deletedItem.getId()).removeValue();

            // remove the item from recycler view
//            adapter.removeItem(viewHolder.getAdapterPosition());
//            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//            adapter.notifyItemRangeChanged(viewHolder.getAdapterPosition(), listEmploys .size());
//            adapter.notifyDataSetChanged();


            // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(newActivity, name + " removed from list!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    adapter.restoreItem(deletedItem, deletedIndex);
//                    mReference.child(deletedItem.getId()).setValue(deletedItem);
//                    listEmploys.add(deletedItem);
//                    adapter.notifyDataSetChanged();
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
//            getData();
        }
    }

//    public class LoadData extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            getData();
//            return null;
//        }
//    }
}
