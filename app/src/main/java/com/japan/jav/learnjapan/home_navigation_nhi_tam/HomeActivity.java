package com.japan.jav.learnjapan.home_navigation_nhi_tam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.utilities_trung.Constants;

/**
 * Created by matas on 3/19/18.
 */

public class HomeActivity extends AppCompatActivity {

    private final String TAG = HomeActivity.class.getSimpleName();
    private String mUserID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tam_nhi);

        getUserID();
    }

    private void getUserID(){
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_ID)){
            mUserID = intent.getStringExtra(Constants.USER_ID);
            Log.i(TAG, "getUserID: " + mUserID);
        }
    }
}
