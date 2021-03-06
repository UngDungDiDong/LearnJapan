package com.japan.jav.learnjapan.download_nguyen.topic;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.base.BaseActivity;
import com.japan.jav.learnjapan.download_nguyen.adapter.KanjiAdapter;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Set;
import com.japan.jav.learnjapan.service.DatabaseService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class KanjiExploresActivity extends BaseActivity {

    public static final String TAG = KanjiExploresActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private ArrayList<Kanji> kanjiList = new ArrayList<Kanji>();
    private KanjiAdapter kanjiAdapter;
    private RecyclerView mKanjiRecycler;
    private String Topic;
    private boolean isAdded;
    private DatabaseReference mKanjiRef;
    private String mSetName = "";
    final Context context = this;
    Date currentTime;
    ImageView ivAdd;
    String userID;
    String id;

    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mKanjiSet = mData.createDatabase("KanjiSet");
    private DatabaseReference mSetByUser = mData.createDatabase("SetByUser");
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_explores);

        currentTime = Calendar.getInstance().getTime();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        isAdded = false;

        //Declare database references
        Intent intent = getIntent();
        Topic = intent.getStringExtra("Kanji_Key");
        mSetName = Topic;
        setReference(Topic);
        changeButtonAdd();
        checkStatus();
        addControl();

        //Load data
        new LoadDataTask().execute();
    }

    private void addControl() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mKanjiRecycler = findViewById(R.id.kanjiRecyclerView);
        kanjiAdapter = new KanjiAdapter();
        kanjiAdapter.setmKanjiList(kanjiList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mKanjiRecycler.setLayoutManager(linearLayoutManager);
        mKanjiRecycler.setAdapter(kanjiAdapter);
        ivAdd = findViewById(R.id.iv_add_KanjiSet);
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


    private void setReference(String Topic) {
        mKanjiRef = mData.getDatabase().child("Kanji").child(Topic);
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getKanji();
            return null;
        }
    }

    public void getKanji() {
        mKanjiRef.addValueEventListener(new ValueEventListener() {
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
            Kanji kanji = ds.getValue(Kanji.class);
            kanjiList.add(kanji);
        }
        if (kanjiList.size() > 0)
            kanjiAdapter.notifyDataSetChanged();
    }

    private void checkStatus() {
        Log.d(TAG, "checkStatus: begin " + isAdded);
        mKanjiSet.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Set kanjiSet = new Set();
                    kanjiSet.setName(ds.getValue(Set.class).getName());

                    if (kanjiSet.getName().equals(Topic)) {
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

    @SuppressLint("ResourceAsColor")
    private void controlAddButton() {
        if (isAdded == false) {
            id = mKanjiSet.push().getKey();
            Set set = new Set(id, mSetName, String.valueOf(currentTime));
            //Set set = new Set(id, mSetName, currentTime);
            mKanjiSet.child(userID).child(id).setValue(set);
            for (int i = 0; i < kanjiList.size(); i++){
                mSetByUser.child(userID).child(id).child(kanjiList.get(i).getId()).setValue(kanjiList.get(i));
            }
            isAdded = true;
            checkStatus();
            Toast.makeText(KanjiExploresActivity.this, "Added to your data", Toast.LENGTH_LONG).show();
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
                Toast.makeText(KanjiExploresActivity.this, "Deleted", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void deleteFromDatabase() {
        mKanjiSet.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: isAdded: " + isAdded);
                if (isAdded == true) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getValue(Set.class).getName();

                        if (name.equals(Topic)) {
                            id = ds.getKey();
                            mKanjiSet.child(userID).child(id).removeValue();
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
