package com.example.tungtu.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewCar;
    ArrayList<Car> arrayListCar;
    CarAdapter carAdapter;
    ArrayList<String> arrayListId;
    Button btnSubmit;
    EditText txtName, txtYear;
    Car car_edit;
    String car_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCar = findViewById(R.id.list_view);

        arrayListCar = new ArrayList<>();
        arrayListId = new ArrayList<>();
        carAdapter = new CarAdapter(this, R.layout.layout_car, arrayListCar);
        listViewCar.setAdapter(carAdapter);

//        arrayListCar.add(new Car("toyota 2017", R.drawable.car, "2017"));
//        arrayListCar.add(new Car("innova 2017", R.drawable.car, "2015"));
//        arrayListCar.add(new Car("mazda", R.drawable.car, "2018"));
//        arrayListCar.add(new Car("hyundai", R.drawable.car, "2016"));
//        arrayListCar.add(new Car("mitsubishi", R.drawable.car, "2017"));
//        arrayListCar.add(new Car("honda", R.drawable.car, "2017"));
//        arrayListCar.add(new Car("bus 34 chỗ", R.drawable.f683, "2017"));
//        arrayListCar.add(new Car("bus 52 chỗ", R.drawable.f683, "2016"));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("car");

//        for (Car car : arrayListCar){
//            myRef.push().setValue(car);
//        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                arrayListCar.clear();
                arrayListId.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    arrayListCar.add(data.getValue(Car.class));
                    arrayListId.add(data.getKey());
                }
                carAdapter.setCarList(arrayListCar);
                carAdapter.notifyDataSetChanged();
//                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        btnSubmit = findViewById(R.id.btnSubmit);
        txtName = findViewById(R.id.txtName);
        txtYear = findViewById(R.id.txtYear);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(txtName.getText().toString().trim()) && !TextUtils.isEmpty(txtYear.getText().toString().trim()))
                {
                    if(car_edit != null){
                        String name = String.valueOf(txtName.getText());
                        String year = String.valueOf(txtYear.getText());
                        Log.d("tung", txtName.getText() + "");
                        Log.d("tung", txtYear.getText() + "");
                        Log.d("tung", car_id + "");
                        car_edit.setName(name);
                        car_edit.setYear(year);
                        myRef.child(car_id).setValue(car_edit);
                        resetForm();
                    }
                    else{
                        String name = String.valueOf(txtName.getText());
                        String year = String.valueOf(txtYear.getText());
                        Log.d("tung", txtName.getText() + "");
                        Log.d("tung", txtYear.getText() + "");
                        Car car = new Car(name, R.drawable.car, year);
                        myRef.push().setValue(car);
                        resetForm();
                    }
                }
            }
        });


        listViewCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("tung", arrayListId.get(i) + "");
                car_id = arrayListId.get(i);
                car_edit = arrayListCar.get(i);
                txtName.setText(car_edit.getName());
                txtYear.setText(car_edit.getYear());
            }
        });

        listViewCar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("tung", arrayListId.get(i) + "");
                car_id = arrayListId.get(i);
                myRef.child(car_id).removeValue();
                resetForm();
                return false;
            }
        });
    }

    private void resetForm(){
        car_edit = null;
        txtName.setText("");
        txtYear.setText("");
    }
}
