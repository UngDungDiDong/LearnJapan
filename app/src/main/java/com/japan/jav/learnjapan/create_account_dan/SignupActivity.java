package com.japan.jav.learnjapan.create_account_dan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.login_trung_nam.LoginActivity;
import com.japan.jav.learnjapan.model.User;
import com.japan.jav.learnjapan.service.DatabaseService;
import com.japan.jav.learnjapan.service.Constants;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText tv_username , tv_email, tv_pass, tv_confirmpass;
    Button bt_Signup;

    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_dan);

        mData = FirebaseDatabase.getInstance().getReference();

        tv_username = findViewById(R.id.tvUsername);
        tv_email = findViewById(R.id.tvEmail);
        tv_pass = findViewById(R.id.tvpass);
        tv_confirmpass = findViewById(R.id.tvconfirmPass);
        bt_Signup = findViewById(R.id.btSignUp);
        bt_Signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == bt_Signup){
            String username = tv_username.getText().toString(),
                    email = tv_email.getText().toString(),
                    pass = tv_pass.getText().toString(),
                    confirmPass = tv_confirmpass.getText().toString();
            if(username.isEmpty() || email.isEmpty()|| pass.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(SignupActivity.this, "Please fill all username, email, pass and confirmPass", Toast.LENGTH_SHORT).show();
            }
            else{
                User user = new User (username, email,pass);
                mData.child("User").push().setValue(user).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SignupActivity.this, "Create accout is false ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }
    }
    // ẩn bàn phím
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
