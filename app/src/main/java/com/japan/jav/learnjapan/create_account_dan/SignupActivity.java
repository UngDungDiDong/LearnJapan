package com.japan.jav.learnjapan.create_account_dan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.login_trung_nam.LoginActivity;
import com.japan.jav.learnjapan.model.User;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText tv_username , tv_email, tv_pass, tv_confirmpass;
    Button bt_Signup;
    String username = "",email = "", pass = "", confirmPass = "";
    ProgressBar progressbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_dan);

        mAuth = FirebaseAuth.getInstance();

        tv_username = findViewById(R.id.tvUsername);
        tv_email = findViewById(R.id.tvEmail);
        tv_pass = findViewById(R.id.tvpass);
        tv_confirmpass = findViewById(R.id.tvconfirmPass);
        bt_Signup = findViewById(R.id.btSignUp);
        bt_Signup.setOnClickListener(this);
        progressbar = findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        if(view == bt_Signup){
                    username = tv_username.getText().toString();
                    email = tv_email.getText().toString();
                    pass = tv_pass.getText().toString();
                    confirmPass = tv_confirmpass.getText().toString();
            if(username.isEmpty() || email.isEmpty()|| pass.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(SignupActivity.this, "Please fill all username, email, pass and confirmPass", Toast.LENGTH_SHORT).show();
            }
            else{
                if(!pass.equals(confirmPass)){
                    Toast.makeText(SignupActivity.this, "Enter confirmPass failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressbar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        sendEmailVercation();
                                    } else {

                                        Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    progressbar.setVisibility(View.GONE);
                                }
                            });
                }
            }
        }
    }

    private void sendEmailVercation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(SignupActivity.this, " successfully register acccout", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(SignupActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private void sendUserData() {
        FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = firebasedatabase.getReference().child("User");
        User userAccout = new User(mAuth.getUid(), username, email);
        myRef1.child(mAuth.getUid()).setValue(userAccout);

    }


    // ẩn bàn phím
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
