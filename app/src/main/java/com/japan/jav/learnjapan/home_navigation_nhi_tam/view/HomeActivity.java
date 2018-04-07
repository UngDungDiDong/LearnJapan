package com.japan.jav.learnjapan.home_navigation_nhi_tam.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.databinding.ActivityHomeTamNhiBinding;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.adapter.HomeFragmentPagerAdapter;
import com.japan.jav.learnjapan.utilities.ConnectivityChangeReceiver;
import com.japan.jav.learnjapan.utilities.Constants;
import com.japan.jav.learnjapan.utilities.NetworkListener;

/**
 * Created by matas on 3/19/18.
 */

public class HomeActivity extends AppCompatActivity implements NetworkListener{
    private ActivityHomeTamNhiBinding binding;
    private DatabaseReference mDatabase;
    private final String TAG = HomeActivity.class.getSimpleName();
    private static String mUserID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_tam_nhi);

        setSupportActionBar(binding.toolbar);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager()));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerReceiver(new ConnectivityChangeReceiver(this), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        setUserID();

        //Toast.makeText(this, mUserID, Toast.LENGTH_SHORT).show();
    }



    public static String getUserID() {
        return mUserID;
    }

    private void setUserID(){
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_ID)){
            mUserID = intent.getStringExtra(Constants.USER_ID);
            Log.i(TAG, "getUserID: " + mUserID);
        }
    }

    @Override
    public void connected() {

    }

    @Override
    public void notConnected() {
        Toast.makeText(this, getResources().getText(R.string.not_connected), Toast.LENGTH_SHORT).show();

    }
}
