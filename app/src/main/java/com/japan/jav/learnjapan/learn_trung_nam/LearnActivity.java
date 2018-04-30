package com.japan.jav.learnjapan.learn_trung_nam;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.Constants;
import com.japan.jav.learnjapan.learn_trung_nam.adapter.CardFragmentPagerAdapter;
import com.japan.jav.learnjapan.model.DataTypeEnum;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;
import com.japan.jav.learnjapan.model.Set;
import com.japan.jav.learnjapan.service.DatabaseService;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    private final static String TAG = LearnActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private CardFragmentPagerAdapter mPagerAdapter;
    private static boolean isFront = true;
    private DataTypeEnum dataTypeEnum;
    private ArrayList<Moji> mojiList;
    private ArrayList<Kanji> kanjiList;
    private Set set, dateSet;
    private DatabaseReference mSetByUserRef;
    private DatabaseReference mDateSetRef;
    private Toolbar mToolbar;
    private TextView txtNext, txtForward, txtNumber, txtNumberTotal;
    private DatabaseService mData = DatabaseService.getInstance();
    //private LocalDatabase mLocalData = LocalDatabase.getInstance();
//    private DatabaseReference mDateSet = mData.createDatabase("DateSet");
    private String mUserID = "";
    public String type;
    public int cardNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_trung_nam);

        Intent intent = getIntent();
        if (intent != null) {
            dataTypeEnum = (DataTypeEnum) intent.getSerializableExtra(Constants.DATA_TYPE);
            set = (Set) intent.getSerializableExtra(Constants.SET_BY_USER);
            mUserID = intent.getStringExtra(Constants.USER_ID);
            Log.d(TAG, "onCreate: dataTypeEnum: " + dataTypeEnum);
            Log.d(TAG, "onCreate: Set: " + set);
            if (dataTypeEnum == DataTypeEnum.KANJI) {
                kanjiList = new ArrayList<>();
                type = "Kanji";
            } else {
                mojiList = new ArrayList<>();
                type = "Moji";
            }
        }
        initParam();
        initControls();
        new fetchData().execute();
        setEvents();
    }

    private void initParam() {
        mSetByUserRef = mData.getDatabase()
                .child(Constants.SET_BY_USER)
                .child(mUserID)
                .child(set.getId());
        Log.e(TAG, "initParam: " + mSetByUserRef.getKey());
    }

    public static boolean isFront() {
        return isFront;
    }

    private void initControls() {
        txtNext = findViewById(R.id.txt_next);
        txtForward = findViewById(R.id.txt_forward);
        txtNumber = findViewById(R.id.txt_item_number);
        txtNumberTotal = findViewById(R.id.txt_item_number_learn);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Learn");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar = findViewById(R.id.progress_bar_learn);
        mViewPager = findViewById(R.id.viewPager_learn);
        mPagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), dataTypeEnum);
        if (type == "Kanji") {
            mPagerAdapter.setKanList(kanjiList);
        } else {
            mPagerAdapter.setMojiList(mojiList);
        }
        if (cardNumber != 0) {
            mProgressBar.setProgress(100 / cardNumber);
        }
        mViewPager.setAdapter(mPagerAdapter);
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

    private void setEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int progressValue = 0;
                if (cardNumber != 0) {
                    progressValue = ((position + 1) * 100) / cardNumber;
                }
                mProgressBar.setProgress(progressValue);
                txtNumber.setText("" + (position + 1) + "/");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txtForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });
    }

    public class fetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... aVoids) {
            mSetByUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataTypeEnum == DataTypeEnum.MOJI) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mojiList.add(ds.getValue(Moji.class));
                        }
                        cardNumber = mojiList.size();
                    } else {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            kanjiList.add(ds.getValue(Kanji.class));
                        }
                        cardNumber = kanjiList.size();
                    }
                    onProgressUpdate();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (type == "Kanji") {
                mPagerAdapter.setKanList(kanjiList);
                cardNumber = kanjiList.size();
            } else {
                mPagerAdapter.setMojiList(mojiList);
                cardNumber = mojiList.size();
            }
            txtNumberTotal.setText("" + cardNumber);
            mPagerAdapter.createCardList();
            mPagerAdapter.notifyDataSetChanged();
        }
    }


}
