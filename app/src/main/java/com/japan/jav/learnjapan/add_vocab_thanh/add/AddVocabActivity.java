package com.japan.jav.learnjapan.add_vocab_thanh.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.add_vocab_thanh.customView.VocabView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by matas on 3/17/18.
 */

public class AddVocabActivity extends AppCompatActivity {

    @BindView(R.id.ll_data)
    LinearLayout llData;

    @BindView(R.id.imv_add)
    ImageView imvAdd;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_done)
    TextView tvDone;

    @BindView(R.id.tv_vocab)
    TextView tvVocab;

    private VocabView vocabView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vocab_thanh);
        ButterKnife.bind(this);
        addControls();
    }

    private void addControls() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addData() {
        vocabView = new VocabView(AddVocabActivity.this);
        llData.addView(vocabView);
    }
}
