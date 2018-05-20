package com.example.huuduc.firebasedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.huuduc.firebasedemo.model.TiGia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestActivity extends AppCompatActivity {

    EditText txtUSD, txtYEN, txtEuro, txt18k;
    Button btnGet;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReference = mDatabase.child("TiGia");

        txtUSD = findViewById(R.id.txtUSD);
        txtEuro = findViewById(R.id.txtEuro);


        btnGet = findViewById(R.id.btnGet);


        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewTiGia(txtUSD.getText().toString(),
                        txtEuro.getText().toString());
            };
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TiGia tiGia1 = dataSnapshot.getValue(TiGia.class);
                txtUSD.setText(tiGia1.getUSD());
                txtEuro.setText(tiGia1.getEuro());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TiGia tiGia1 = dataSnapshot.getValue(TiGia.class);
                txtUSD.setText(tiGia1.getUSD());
                txtEuro.setText(tiGia1.getEuro());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void writeNewTiGia(String USD, String euro) {
        final TiGia tiGia = new TiGia(USD, euro);
        databaseReference.setValue(tiGia);
    }
}
