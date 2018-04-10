package com.japan.jav.learnjapan.test_feature_khang_duc.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.japan.jav.learnjapan.R;

/**
 * Created by huuduc on 15/03/2018.
 */

public class TestActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private TextView txtRightCount, txtQuestion, txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD;
    private TextView txtCorrect;
    private TextView txtNumberQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_khang_duc);

        addControls();
    }

    private void addControls() {
        txtRightCount = findViewById(R.id.txtRightCount);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtAnswerA = findViewById(R.id.txtAnswerA);
        txtAnswerB = findViewById(R.id.txtAnswerB);
        txtAnswerC = findViewById(R.id.txtAnswerC);
        txtAnswerD = findViewById(R.id.txtAnswerD);
        txtCorrect = findViewById(R.id.txtCorrect);
        txtNumberQuestion = findViewById(R.id.txtNumberOfQuestion);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
