package com.japan.jav.learnjapan.download_nguyen.topic;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.base.BaseActivity;
import com.japan.jav.learnjapan.download_nguyen.adapter.MojiAdater;
import com.japan.jav.learnjapan.model.Moji;
import com.japan.jav.learnjapan.model.Set;
import com.japan.jav.learnjapan.service.DatabaseService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MojiExploresActivity extends BaseActivity {

    public static final String TAG = MojiExploresActivity.class.getSimpleName();

    public static final String MOJI_SOUMATOME_KEY = "Soumatome";

    private Toolbar mToolbar;
    private String mTopic;
    private ArrayList<Moji> mojiList = new ArrayList<Moji>();
    private RecyclerView mMojiRecycler;
    private MojiAdater mojiAdater;
    private DatabaseReference mMojiRef;
    private String mSetName = "";
    final Context context = this;
    Date currentTime;
    ImageView ivAdd;
    String userID;
    String id;
    Boolean isAdded;

    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mMojiSet = mData.createDatabase("MojiSet");
    private DatabaseReference mSetByUser = mData.createDatabase("SetByUser");

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moji_explores);
        mAuth = FirebaseAuth.getInstance();
        currentTime = Calendar.getInstance().getTime();
        userID = mAuth.getCurrentUser().getUid();
        isAdded = false;

        //Declare database reference
        Intent intent = getIntent();
        mTopic = intent.getStringExtra("Moji_Key");
        mSetName = mTopic;
        setReference(mTopic);
        changeButtonAdd();
        checkStatus();
        addControl();

        new LoadDataTask().execute();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        
}
    private void addControl() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMojiRecycler = findViewById(R.id.mojiRecyclerView);
        mojiAdater = new MojiAdater();
        mojiAdater.setmMojiList(mojiList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMojiRecycler.setLayoutManager(linearLayoutManager);
        mMojiRecycler.setAdapter(mojiAdater);
        ivAdd = findViewById(R.id.iv_add_MojiSet);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlAddButton();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setReference(String topic) {
        mMojiRef = mData.getDatabase().child("Moji").child(MOJI_SOUMATOME_KEY).child(topic);
      Log.d(TAG, "setReference: Mojiref: " + mMojiRef);
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getMoji();
            return null;
        }
    }

    private void getMoji() {
        mMojiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Moji moji = ds.getValue(Moji.class);
            moji.setId(ds.getKey());
            Log.e(TAG, "showData: " + ds.getKey());
            mojiList.add(moji);
        }
        mojiAdater.notifyDataSetChanged();
    }

    private void checkStatus() {
        Log.d(TAG, "checkStatus: begin " + isAdded);
        mMojiSet.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Set mojiSet = new Set();
                    mojiSet.setName(ds.getValue(Set.class).getName());

                    if (mojiSet.getName().equals(mTopic)) {
                        isAdded = true;
                        changeButtonAdd();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void controlAddButton() {
        if (isAdded == false) {
            id = mMojiSet.push().getKey();
            Set set = new Set(id, mSetName, String.valueOf(currentTime));
            //Set set = new Set(id, mSetName, currentTime);
            mMojiSet.child(userID).child(id).setValue(set);
            for (int i = 0; i < mojiList.size(); i++){
                mSetByUser.child(userID).child(id).child(mojiList.get(i).getId()).setValue(mojiList.get(i));
            }
            isAdded = true;
            changeButtonAdd();
            Toast.makeText(MojiExploresActivity.this, "Added to your data", Toast.LENGTH_LONG).show();
        } else if (isAdded == true) {
            showRemoveDialog();
        }
    }

    private void changeButtonAdd() {
        if (isAdded) {
            ivAdd.setBackgroundResource(R.drawable.ic_add_set);
        } else {
//            ivAdd.setBackgroundResource(R.drawable.ic_remove_set);
        }
    }

    void showRemoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete this topic from your data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFromDatabase();
                Toast.makeText(MojiExploresActivity.this, "Deleted", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void deleteFromDatabase() {
        mMojiSet.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAdded == true) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getValue(Set.class).getName();
                        if (name.equals(mTopic)) {
                            id = ds.getKey();
                            Log.d(TAG, "onDataChange: key: " + id);
                            mMojiSet.child(userID).child(id).removeValue();
                            mSetByUser.child(userID).child(id).removeValue();
                            isAdded = false;
                            ivAdd.setBackgroundResource(R.drawable.ic_remove_set);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
