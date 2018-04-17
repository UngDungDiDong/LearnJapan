package com.japan.jav.learnjapan.profile_tung;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtEmail_profile;
    TextView txtUsername;
    CircleImageView imgAvatar;
    String avatarUrl;
    private User user;
    private int total_set = 0;
    private int total_word = 0;
    private Toolbar mToolbar;
    private FirebaseDatabase database;
    private FirebaseUser user_current = null;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tung);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user_current = auth.getCurrentUser();
        mapping();
        run();
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

            Log.d("user", uid);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imvRight4:
                break;
            case R.id.imvRight2:
                break;
            case R.id.imvRight1:
                break;
        }
    }


}
