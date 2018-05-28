package com.dan.demogiuaky;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ArrayList<User> arrUser;
    private ListView lvUser;
    private UserAdapter adapter;

    private Button btnAdd;
    private EditText edtName, edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        adapter = new UserAdapter(MainActivity.this, R.layout.custom_listview, arrUser);
        lvUser.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(edtName.getText().toString(), edtEmail.getText().toString());
                mDatabase.push().setValue(user);
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user1 = dataSnapshot.getValue(User.class);
                arrUser.add(user1);
                adapter.notifyDataSetChanged();
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

    }



    public void addControls(){
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        edtName = (EditText) findViewById(R.id.editTextName);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        lvUser = (ListView) findViewById(R.id.database_list);
        arrUser = new ArrayList<>();
    }
}
