package com.japan.jav.learnjapan.test_feature_khang_duc.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.Constants;
import com.japan.jav.learnjapan.test_feature_khang_duc.view.adapter.ListReviewAdapter;
import com.japan.jav.learnjapan.test_feature_khang_duc.view.model.ReviewItem;

import java.util.ArrayList;

public class TestReviewActivity extends AppCompatActivity {

    private ListReviewAdapter adapter;
    private RecyclerView rvListReview;
    private ArrayList<ReviewItem> listReviews;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_result_review);

        addControls();
        addEvents();

    }

    private void addEvents() {

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvListReview = findViewById(R.id.rvReview);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.LIST_REVIEW)){
            listReviews = (ArrayList<ReviewItem>) intent.getSerializableExtra(Constants.LIST_REVIEW);
            adapter = new ListReviewAdapter(listReviews, this);
            rvListReview.setLayoutManager(new LinearLayoutManager(this));
            rvListReview.setAdapter(adapter);
        }else {
            finish();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
