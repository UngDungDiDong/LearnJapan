package com.thudiem.ntt2.newfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    DatabaseReference mData;
    TextView txtHoTen;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextDiaChi;
    String ID;

    Button btSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtHoTen = findViewById(R.id.textViewHoTen);

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("HoTen").setValue("Nguyễn Thị Thu Điểm");

        mData.child("HoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtHoTen.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDiaChi = findViewById(R.id.editTextDiaChi);

        mData.child("SinhVien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ID = dataSnapshot.getKey();
                SinhVien sinhVien = dataSnapshot.getValue(SinhVien.class);
                editTextEmail.setText(sinhVien.getEmail().toString());
                editTextPhone.setText(sinhVien.getPhone().toString());
                editTextDiaChi.setText(sinhVien.getDiaChi().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinhVien sv = new SinhVien(editTextEmail.getText().toString(),editTextPhone.getText().toString(),editTextDiaChi.getText().toString());
                mData.child("SinhVien").push().setValue(sv, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null){
                            Toast.makeText(MainActivity.this,"Lưu Thành công",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"Lưu Thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



//
//        Map<String,Integer> myMap = new HashMap<String, Integer>();
//        myMap.put("XeMay",2);
//        mData.child("PhuongTien").setValue(myMap);
//
//        SinhVien sinhVien = new SinhVien("Thu Diem","Hà Nội", 2006);
//        mData.child("HocVien").push().setValue(sinhVien);
//
//        mData.child("BaiTap").setValue("Số 1", new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if (databaseError == null){
//                    Toast.makeText(MainActivity.this,"Lưu Thành công",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this,"Lưu Thất bại",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
