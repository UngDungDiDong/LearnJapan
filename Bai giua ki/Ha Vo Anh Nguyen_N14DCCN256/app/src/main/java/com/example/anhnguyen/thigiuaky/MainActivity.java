package com.example.anhnguyen.thigiuaky;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edt_add;
    Button btn_add;
    ListView listView;
    Button btn_update;
    Button btn_remove;

    int pos = -1;

    private DatabaseReference mDatabase;

    ArrayList<Contact> arrContact = new ArrayList<>();
    CustomAdapter customAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValue();
        setEven();
        configListView();
    }

    void setEven(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_add.getText().toString();
                if (name.equals("")){
                    Toast.makeText(MainActivity.this, "Field không có giá trị", Toast.LENGTH_SHORT).show();
                }else {
                    mDatabase.child("user").push().child("name").setValue(name);
                }
            }
        });

        mDatabase.child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrContact.add(new Contact(dataSnapshot.child("name").getValue().toString(), R.color.colorPrimary));
                customAdaper.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                arrContact.set(pos, new Contact(dataSnapshot.child("name").getValue().toString(), R.color.colorPrimary));
                customAdaper.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                arrContact.remove(pos);
                customAdaper.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edt_add.setText(arrContact.get(position).getName());
                pos = position;
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos != -1){
                    mDatabase.child("user").orderByChild("name").equalTo(arrContact.get(pos).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                mDatabase.child("user").child(child.getKey()).child("name").setValue(edt_add.getText().toString());
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Chưa chọn dòng để cập nhập lại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos != -1){
                    mDatabase.child("user").orderByChild("name").equalTo(arrContact.get(pos).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                mDatabase.child("user").child(child.getKey()).removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Chưa chọn dòng để xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void configListView(){
        customAdaper = new CustomAdapter(this,R.layout.item,arrContact);
        listView.setAdapter(customAdaper);
    }

    void initValue(){
        edt_add = findViewById(R.id.edt_add);
        btn_add = findViewById(R.id.btn_add);
        listView = findViewById(R.id.listview);
        btn_update = findViewById(R.id.btn_update);
        btn_remove = findViewById(R.id.btn_remove);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

}
