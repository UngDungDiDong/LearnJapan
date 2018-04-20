package com.japan.jav.learnjapan.add_vocab_thanh.add;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.add_vocab_thanh.customView.VocabView;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.Constants;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.model.Set;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.view.HomeActivity;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;
import com.japan.jav.learnjapan.service.DatabaseService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by matas on 3/17/18.
 */

public class AddVocabActivity extends AppCompatActivity implements View.OnClickListener, VocabView.OnDeleteListener {
    public static final int MODE_CREATE = 0;
    public static final int MODE_EDIT = 1;
    int curMode = MODE_CREATE;

    public static final int KANJI = 2;
    public static final int MOJI = 3;
    int curTYPE = KANJI;

    @BindView(R.id.ll_data)
    LinearLayout llData;

    @BindView(R.id.imv_add)
    ImageView imvAdd;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_done)
    TextView tvDone;

    @BindView(R.id.tv_count_vocab)
    TextView tvCountVocab;

    private VocabView vocabView;
    private Set setByUser;
    private String userId, name, type;
    private Date currentTime;
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mMojiSet = mData.createDatabase(Constants.MOJI_SET_NODE);
    private DatabaseReference mKanjiSet = mData.createDatabase(Constants.KANJI_SET_NODE);
    private DatabaseReference mSetByUser = mData.createDatabase(Constants.SET_BY_USER);

    private List<Kanji> mListKanji = new ArrayList<>();
    private List<Moji> mListMoji = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vocab_thanh);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra(Constants.NAME);
            type = intent.getStringExtra(Constants.CREATE);
            if (name != null) {
                curMode = MODE_CREATE;
                tvCountVocab.setText(getString(R.string.new_words, 0));
                if (type.equalsIgnoreCase(Constants.KANJI))
                    curTYPE = KANJI;
                else
                    curTYPE = MOJI;
                mToolbar.setTitle(name);
            } else {
                curMode = MODE_EDIT;
                setByUser = (Set) intent.getSerializableExtra(Constants.SET_BY_USER);
                if (type.equalsIgnoreCase(Constants.KANJI)) {
                    curTYPE = KANJI;
                } else {
                    curTYPE = MOJI;
                }
            }
        }
        userId = mData.getUserID();

        currentTime = Calendar.getInstance().getTime();

        if (curMode == MODE_CREATE) {
            vocabView = new VocabView(AddVocabActivity.this, curTYPE, new VocabView.OnDeleteListener() {
                @Override
                public void onDelete(View view) {
                    llData.removeView(view);
                    tvCountVocab.setText(getString(R.string.new_words, llData.getChildCount()));
                }
            });
            llData.addView(vocabView);
        } else {
            if (curTYPE == KANJI) {
                mDatabase.child(Constants.SET_BY_USER).child(userId).child(setByUser.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            vocabView = new VocabView(AddVocabActivity.this, curTYPE, new VocabView.OnDeleteListener() {
                                @Override
                                public void onDelete(View view) {
                                    llData.removeView(view);
                                    tvCountVocab.setText(getString(R.string.new_words, llData.getChildCount()));
                                }
                            });
                            vocabView.setDataKanji(data.getValue(Kanji.class));
                            llData.addView(vocabView);
                        }
                        tvCountVocab.setText(getString(R.string.new_words, llData.getChildCount()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            } else {
                mDatabase.child(Constants.SET_BY_USER).child(HomeActivity.getUserID()).child(setByUser.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            vocabView = new VocabView(AddVocabActivity.this, curTYPE, new VocabView.OnDeleteListener() {
                                @Override
                                public void onDelete(View view) {
                                    llData.removeView(view);
                                    tvCountVocab.setText(getString(R.string.new_words, llData.getChildCount()));
                                }
                            });
                            vocabView.setDataMoji(data.getValue(Moji.class));
                            llData.addView(vocabView);
                        }
                        tvCountVocab.setText(getString(R.string.new_words, llData.getChildCount()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }

        imvAdd.setOnClickListener(this);
        tvDone.setOnClickListener(this);
    }

    private void initListener() {

    }

    //Warning if user hasn't saved vocab set.
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warning_unsave_data);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDataAndSave();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_add:
                vocabView = new VocabView(AddVocabActivity.this, curTYPE, this);
                llData.addView(vocabView);
                tvCountVocab.setText(getString(R.string.new_words, llData.getChildCount()));
                break;
            case R.id.tv_done:
                getDataAndSave();
                break;
        }
    }

    private void getDataAndSave() {
        String id = "";
//        if (isNetworkAvailable()) {
        if (curTYPE == MOJI) {
            id = mMojiSet.push().getKey();
        } else {
            id = mKanjiSet.push().getKey();
        }
        saveDatabase(id);
//        } else {
//            if (curTYPE == MOJI) {
//                id = mMojiSet.push().getKey();
//            } else {
//                id = mKanjiSet.push().getKey();
//            }
//            saveLocalDatabase(id);
//        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //save to Firebase
    private void saveDatabase(String id) {
        boolean check;
        Set set;
        if (curMode == MODE_CREATE) {
            set = new Set(id, name, String.valueOf(currentTime));
        } else {
            id = setByUser.getId();
            set = new Set(setByUser.getId(), setByUser.getName(), String.valueOf(currentTime));
        }
        if (curTYPE == MOJI) {
            if (mListMoji != null)
                mListMoji.clear();
            int vocabCount = llData.getChildCount();
            if (vocabCount > 0) {
                for (int i = 0; i < vocabCount; i++) {
                    VocabView vocabView = (VocabView) llData.getChildAt(i);
                    check = vocabView.checkData();
                    if (!check)
                        return;

                    Moji moji = vocabView.getDataMoji();
                    mListMoji.add(moji);
                }
            }
            mMojiSet.child(userId).child(id).setValue(set);
            mSetByUser.child(userId).child(id).setValue(mListMoji);
        } else {
            if (mListKanji != null)
                mListKanji.clear();
            int vocabCount = llData.getChildCount();
            if (vocabCount > 0) {
                for (int i = 0; i < vocabCount; i++) {
                    VocabView vocabView = (VocabView) llData.getChildAt(i);
                    check = vocabView.checkData();
                    if (!check)
                        return;

                    Kanji kanji = vocabView.getDataKanji();
                    mListKanji.add(kanji);
                }
            }
            mKanjiSet.child(userId).child(id).setValue(set);
            mSetByUser.child(userId).child(id).setValue(mListKanji);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (llData.getChildCount() > 0)
            showAlertDialog();
        else
            finish();
    }

    @Override
    public void onDelete(View view) {
        llData.removeView(view);
        tvCountVocab.setText(getString(R.string.new_words, llData.getChildCount()));
    }
}
