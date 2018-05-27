package com.n14dccn309.thanhle.demofirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<User> mListUser;
    private UserAdapter mAdapter;
    private List<String> mListId;

    private ListView lvUser;
    private Button btnSave;
    private EditText edtName;
    private EditText edtAddress;

    private User userEdit;
    private String idUser;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvUser = findViewById(R.id.lv_user);
        btnSave = findViewById(R.id.btn_save);
        edtName = findViewById(R.id.edt_name);
        edtAddress = findViewById(R.id.edt_address);

        initData();
        initListener();
    }

    private void initListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mListUser != null) mListUser.clear();
                if (mListId != null) mListId.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mListUser.add(data.getValue(User.class));
                    mListId.add(data.getKey());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

            }
        });


        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idUser = mListId.get(i);
                userEdit = mListUser.get(i);
                edtName.setText(userEdit.getName());
                edtAddress.setText(userEdit.getAddress());
            }
        });

        lvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idUser = mListId.get(i);
                myRef.child(idUser).removeValue();
                edtName.setText("");
                edtAddress.setText("");
                return false;
            }
        });
    }

    private void getData() {
        if (edtName.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtAddress.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userEdit != null) {
            userEdit.setName(edtName.getText().toString());
            userEdit.setAddress(edtAddress.getText().toString());
            myRef.child(idUser).setValue(userEdit);
            edtName.setText("");
            edtAddress.setText("");
        } else {
            User user = new User(edtName.getText().toString(), edtAddress.getText().toString());
            myRef.push().setValue(user);
            edtName.setText("");
            edtAddress.setText("");
        }
    }

    private void initData() {
        mListUser = new ArrayList<>();
        mListId = new ArrayList<>();
        mAdapter = new UserAdapter(this, R.layout.item_user, mListUser);
        lvUser.setAdapter(mAdapter);
    }
}
