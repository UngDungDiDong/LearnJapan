package com.japan.jav.learnjapan.login_trung_nam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.complete_profile_bang.CompleteProfileActivity;
import com.japan.jav.learnjapan.create_account_dan.SignupActivity;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.HomeActivity;
import com.japan.jav.learnjapan.reset_pass_hao.ResetPasswordActivity;
import com.japan.jav.learnjapan.utilities_trung.Constants;
import com.japan.jav.learnjapan.utilities_trung.DatabaseService;

/**
 * Created by matas on 3/17/18.
 */

public class LoginActivity  extends AppCompatActivity{

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private ImageView imgFacebook;
    private ImageView imgGoogle;
    private TextView txtCreateAcount;
    private TextView txtForgotPass;
    private ProgressBar progressBar;
    private ImageView imgBottom;

    private FirebaseAuth mAuth;
    private DatabaseService mData = DatabaseService.getInstance();;
    private final String TAG = LoginActivity.class.getSimpleName();
    private LoginGoogle loginGoogle;
    //-------------

    //---NAM----------
    private LoginFacebook loginFacebook;
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    //----------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_trung_nam);
        getControls();
        setEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this, CompleteProfileActivity.class);
            Log.i(TAG, "Userid: " + mData.getUserID());
            intent.putExtra(Constants.USER_ID, mData.getUserID());
            startActivity(intent);
            finish();
        }{
            Log.e(TAG, "onStart: " + "null usercom");
        }
    }

    private void getControls() {
        imgBottom = (ImageView) findViewById(R.id.img_bottom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook);
        imgGoogle = (ImageView) findViewById(R.id.img_google);
        txtCreateAcount = (TextView) findViewById(R.id.tv_create_an_account);
        txtForgotPass = (TextView) findViewById(R.id.tv_forgot_password);
        loginGoogle = new LoginGoogle(getString(R.string.default_web_client_id),this);
        mAuth = mData.getFirebaseAuth();

        //---NAM----------
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginFacebook = new LoginFacebook(loginManager, callbackManager, this);
        //----------------
    }

    private void setEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(!username.isEmpty() && !password.isEmpty()){
                    hideKeyboard(view);
                    progressBar.setVisibility(View.VISIBLE);
                    imgBottom.setVisibility(View.GONE);
                    requestSignIn(username, password);

                }else{
                    Toast.makeText(LoginActivity.this, "Please fill in username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFacebook.loginFacebook();
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = loginGoogle.getmGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, LoginGoogle.RC_SIGN_IN);
            }
        });

        txtCreateAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private void requestSignIn(String email, String pass){
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "User_id:" + mData.getUserID());
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorDisable));

                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, CompleteProfileActivity.class);
                    intent.putExtra(Constants.USER_ID, task.getResult().getUser().getUid());

                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                imgBottom.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
                btnLogin.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == LoginGoogle.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            LoginGoogle.firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
