package com.example.fstha.quanlysinhviendemo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtName,edtMail,edtPhone;
    RadioButton rbtnMale, rbtnFeMale;
    ListView lvSinhVien;
    SinhVienAdapter adapter;
    Toolbar toolbar;
    DatabaseReference mData;
    ArrayList<String> ListID;
    int position = -1;
    SinhVien svEdited;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mData = FirebaseDatabase.getInstance().getReference();
        edtName = (EditText)findViewById(R.id.edtName);
        edtMail = (EditText)findViewById(R.id.edtEmail);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        rbtnMale = (RadioButton)findViewById(R.id.rbtnMale);
        rbtnFeMale = (RadioButton)findViewById(R.id.rbtnFeMale);
        lvSinhVien = (ListView)findViewById(R.id.lvSinhVien);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ListID = new ArrayList<>();

        getData();

        adapter = new SinhVienAdapter(this, 0, Data.getData().arrSV);
        lvSinhVien.setAdapter(adapter);

        mData.child("SinhVien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ListID.add(dataSnapshot.getKey());
                SinhVien sv = dataSnapshot.getValue(SinhVien.class);
                Data.getData().arrSV.add(sv);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(position >= 0) {
                    Data.getData().arrSV.set(position, svEdited);
                    adapter.notifyDataSetChanged();
                }
        }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(position >= 0) {
                    ListID.remove(position);
                    Data.getData().arrSV.remove(position);
                    adapter.notifyDataSetChanged();
                    SetEmptyInfo();
                    position = -1;
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        lvSinhVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                SinhVien sv = (SinhVien)lvSinhVien.getItemAtPosition(position);
                edtName.setText(sv.getName());
                edtMail.setText(sv.getEmail());
                edtPhone.setText(sv.getPhone());
                if(sv.getGender()){
                    rbtnMale.setChecked(true);
                }
                else{
                    rbtnFeMale.setChecked(true);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.addMenu:
                Toast.makeText(MainActivity.this, "Add item", Toast.LENGTH_SHORT).show();
                AddSinhVien();
                return true;
            case R.id.deleteMenu:
                Toast.makeText(MainActivity.this, "Delete item", Toast.LENGTH_SHORT).show();
                DeleteSinhVien();
                return true;
            case R.id.editMenu:
                EditSinhVien();
                Toast.makeText(MainActivity.this, "Edit item", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getData(){
        Data.getData().arrSV = new ArrayList<>();
    }

    public void AddSinhVien(){
        SinhVien sv = new SinhVien("Your Name", "Your phone number", "Your Email", true);
        mData.child("SinhVien").push().setValue(sv);
    }

    public void DeleteSinhVien(){
        if(position >= 0) {
            mData.child("SinhVien").child(ListID.get(position)).removeValue();
        }
    }

    public void EditSinhVien(){
        if(position >= 0) {
            svEdited = new SinhVien(edtName.getText().toString(), edtPhone.getText().toString(), edtMail.getText().toString(), true);
            if (rbtnFeMale.isChecked())
                svEdited.SetGender(false);
            mData.child("SinhVien").child(ListID.get(position)).setValue(svEdited);
        }

    }

    public void SetEmptyInfo(){
        edtPhone.setText("");
        edtMail.setText("");
        edtName.setText("");
        rbtnMale.setChecked(true);
    }

}
