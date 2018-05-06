package com.japan.jav.learnjapan.home_navigation_nhi_tam.view;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.base.BaseActivity;
import com.japan.jav.learnjapan.complete_profile_bang.CompleteProfileActivity;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.adapter.HomeFragmentPagerAdapter;
import com.japan.jav.learnjapan.login_trung_nam.LoginActivity;
import com.japan.jav.learnjapan.profile_tung.ProfileActivity;
import com.japan.jav.learnjapan.service.ConnectivityChangeReceiver;
import com.japan.jav.learnjapan.service.Constants;
import com.japan.jav.learnjapan.service.DatabaseService;
import com.japan.jav.learnjapan.service.NetworkListener;
import com.japan.jav.learnjapan.setting_khang.SettingActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by matas on 3/19/18.
 */

public class HomeActivity extends BaseActivity implements NetworkListener{
    public static final int REQUEST_ADD_VOCAB = 1;
    private Toolbar mToolBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DatabaseService databaseService = DatabaseService.getInstance();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private final String TAG = HomeActivity.class.getSimpleName();
    private static String mUserID = "";
    BroadcastReceiver receiver;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    private TextView tvName;
    private TextView tvGmail;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tam_nhi);
        setControl();

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager()));

        mDatabase = FirebaseDatabase.getInstance().getReference().getDatabase();

        receiver = new ConnectivityChangeReceiver(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        setUserID();
        setupToolbar();

        addControls();
        setupDrawerLayout();
        addEvent();

    }

    // ===== start. TamLV ======
    private void setControl() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager_learn);
    }

    public static String getUserID() {
        //return "XjeTdoRw0XYHIgDfFVKVyabyOcw2";
        return mUserID;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
        }
    }

    @Override
    public void connected() {}

    @Override
    public void notConnected() {
        Toast.makeText(this, getResources().getText(R.string.not_connected), Toast.LENGTH_SHORT).show();
    }
    // ===== end. TamLV =====


    private void setUserID(){
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_ID)){
            mUserID = intent.getStringExtra(Constants.USER_ID);
            Log.i(TAG, "getUserID: " + mUserID);
        }
    }

    private void setupToolbar() {
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolBar.setTitle(R.string.app_name);
    }
    private void setupDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        View hView = navigationView.getHeaderView(0);
        tvName = hView.findViewById(R.id.txtTen);
        tvGmail = hView.findViewById(R.id.txtGmail);
        imgAvatar = hView.findViewById(R.id.profile_image);

        mDatabase.getReference().child("User").child(mUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("username").getValue() == null || dataSnapshot.child("username").getValue().toString().length() == 0 ||
                        dataSnapshot.child("linkPhoto").getValue() == null || dataSnapshot.child("linkPhoto").getValue().toString().length() == 0){
                    goToCompeleteProfileActivities();
                    return;
                }

                String name = dataSnapshot.child("username").getValue().toString();
                String urlPhoto = dataSnapshot.child("linkPhoto").getValue().toString();
                Picasso.with(HomeActivity.this)
                        .load(urlPhoto)
                        .placeholder(R.drawable.placeholder)
                        .into(imgAvatar);
                tvName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tvGmail.setText(firebaseUser.getEmail());
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolBar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }
    private void goToCompeleteProfileActivities() {
        Intent intent = new Intent(HomeActivity.this, CompleteProfileActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }

    private void addEvent() {

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.profile:
//                        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
//                    case R.id.about_us:
//                        Toast.makeText(getApplicationContext(), "About_us ai lam", Toast.LENGTH_SHORT).show();
//                        return true;
                    case R.id.sent_mail:
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        firebaseUser = null;
                        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        return true;
                    case R.id.menu:
                        Intent settingIntent = new Intent(HomeActivity.this, SettingActivity.class);
                        if (mUserID != "") {
                            settingIntent.putExtra(Constants.USER_ID, mUserID);
                        }
                        startActivity(settingIntent);
//                        finish();
                        menuItem.setChecked(false);
                        return true;
                    default:

                        return false;
                }
            }
        });


    }

    private void addControls() {
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
    }
}
