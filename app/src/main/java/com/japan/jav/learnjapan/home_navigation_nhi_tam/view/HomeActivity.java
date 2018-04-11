package com.japan.jav.learnjapan.home_navigation_nhi_tam.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.adapter.HomeFragmentPagerAdapter;
import com.japan.jav.learnjapan.login_trung_nam.LoginActivity;
import com.japan.jav.learnjapan.profile_tung.ProfileActivity;
import com.japan.jav.learnjapan.utilities.ConnectivityChangeReceiver;
import com.japan.jav.learnjapan.utilities.Constants;
import com.japan.jav.learnjapan.utilities.NetworkListener;

/**
 * Created by matas on 3/19/18.
 */

public class HomeActivity extends AppCompatActivity implements NetworkListener{
    private Toolbar mToolBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private final String TAG = HomeActivity.class.getSimpleName();
    private static String mUserID = "";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    private TextView tvName;
    private TextView tvGmail;

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

        registerReceiver(new ConnectivityChangeReceiver(this), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        setUserID();
        setupToolbar();

        addControls();
        setupDrawerLayout();
        addEvent();

        //Toast.makeText(this, mUserID, Toast.LENGTH_SHORT).show();

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

        mDatabase.getReference().child("User").child(mUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();
                tvName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        tvName.setText(firebaseUser.getPhoneNumber());
        tvGmail.setText(firebaseUser.getEmail());
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolBar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(true);

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
                    case R.id.about_us:
                        Toast.makeText(getApplicationContext(), "About_us ai lam", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sent_mail:
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        firebaseUser = null;
                        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
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
    private void setControl() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }


    public static String getUserID() {
        //return mUserID;
        //return "Cth8UD56A7VzeehZnDsPCfiCrBS2";
        return "XjeTdoRw0XYHIgDfFVKVyabyOcw2";
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
