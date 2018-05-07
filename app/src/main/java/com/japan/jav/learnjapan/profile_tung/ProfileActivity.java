package com.japan.jav.learnjapan.profile_tung;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.japan.jav.learnjapan.model.User;
import com.japan.jav.learnjapan.reset_pass_hao.ResetPasswordActivity;
import com.japan.jav.learnjapan.service.ConnectivityChangeReceiver;
import com.japan.jav.learnjapan.service.NetworkListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements NetworkListener {

    TextView txtEmail_profile;
    TextView txtUsername;
    CircleImageView imgAvatar;
    String avatarUrl;
    ConstraintLayout layoutChangePass;
    private User user;
    private int total_set = 0;
    private int total_word = 0;
    private Toolbar mToolbar;
    private FirebaseDatabase database;
    private FirebaseUser user_current = null;
    private FirebaseAuth auth;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tung);

        layoutChangePass = findViewById(R.id.layoutChangePass);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        receiver = new ConnectivityChangeReceiver(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user_current = auth.getCurrentUser();
        mapping();
        run();

        layoutChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ResetPasswordActivity.class));
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
        finish();
    }

    private void run() {
        if (user_current != null) {
            final String uid = user_current.getUid();

            database.getReference()
                    .child("User")
                    .child(uid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);

                            if (user == null) {
                                txtUsername.setText(user_current.getDisplayName());
                                txtEmail_profile.setText(user_current.getEmail());
                                Uri photo = user_current.getPhotoUrl();

//                            Picasso.with(this)
//                                    .load(photo)
//                                    .placeholder(R.drawable.noimage)
//                                    .into(imgAvatar);
//
//                            avatarUrl = photo == null ? "" : photo.toString();

                            } else {
                                Log.d("user", user.getUsername());
                                setDataToView(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            database.getReference()
                    .child("SetByUser")
                    .child(uid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            total_set += dataSnapshot.getChildrenCount();
                            for (DataSnapshot word : dataSnapshot.getChildren()) {
                                total_word += word.getChildrenCount();
                            }

                            TextView txtNumberOfSet = findViewById(R.id.txtNumberOfSet);
                            txtNumberOfSet.setText("Number Of Sets: " + total_set);
                            TextView txtTotalOfWord = findViewById(R.id.txtTotalOfWord);
                            txtTotalOfWord.setText("Number Of Words: " + total_word);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void mapping() {

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail_profile = findViewById(R.id.txtEmail_profile);
        imgAvatar = findViewById(R.id.imgAvatar);

    }

    private void setDataToView(User user) {
        try {
            txtUsername.setText(user.getUsername());
            txtEmail_profile.setText(user.getEmail());
            avatarUrl = user.getLinkPhoto();

            Picasso.with(this)
                    .load(user.getLinkPhoto())
                    .placeholder(R.drawable.noimage)
                    .into(imgAvatar);

        } catch (Exception e) {
            Log.e("SET", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_update:
                startActivity(new Intent(ProfileActivity.this, CompleteProfileActivity.class));
                return true;
        }
        return false;
    }


    @Override
    public void connected() {

    }

    @Override
    public void notConnected() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
