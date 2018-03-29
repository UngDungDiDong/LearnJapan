package com.japan.jav.learnjapan.login_diem_nguyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.japan.jav.learnjapan.R;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by matas on 3/17/18.
 */

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private LoginButton imgFacebook;
    private SignInButton imgGoogle;
    private TextView txtCreateAcount;
    private TextView txtForgotPass;
    private GoogleApiClient mGoogleSignInClient;
    CallbackManager callbackManager;
    int RC_SIGN_IN =001;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /*
    sharedPreferences:
    name: user_infor
    userName: name of user
    passWord: pass of user

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login_nguyen_diem);

        //yeu cau nguoi dung cung cap thong tin co ban
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        SignInButton imgGoogle =(SignInButton) findViewById(R.id.img_google);
        imgGoogle.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.img_google).setOnClickListener(this);

        getControls();
        setEvens();
        keyHashFB();

        imgFacebook.setReadPermissions(Arrays.asList("public_profile","email"));
        setLoginFB();
    }

    private void setLoginFB() {
        imgFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // login sucessfull
                imgFacebook.setVisibility(View.INVISIBLE);
                result();
            }

            @Override
            public void onCancel() {
                // return if login sucessfull
            }

            @Override
            public void onError(FacebookException error) {
                // login fail
            }
        });
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("status",response.getJSONObject().toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    private void getControls() {
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        imgFacebook = (LoginButton) findViewById(R.id.img_facebook);
        imgGoogle =(SignInButton) findViewById(R.id.img_google);
        txtCreateAcount = (TextView) findViewById(R.id.tv_create_an_account);
        txtForgotPass = (TextView) findViewById(R.id.tv_forgot_password);
    }

    private void setEvens(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("user_infor",MODE_PRIVATE);
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(!username.isEmpty() && !password.isEmpty()){
                    editor = sharedPreferences.edit();
                    editor.putString("userName",username);
                    editor.putString("passWord",password);
                    editor.commit();
                }else{
                    // no data input
                    Toast.makeText(LoginActivity.this, "Please fill in username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCreateAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go create Acount
            }
        });


        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go forgot Password
            }
        });
    }

    private void keyHashFB(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Failed",connectionResult+ "");
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d("Success",mGoogleSignInClient.isConnected()+ "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_google:
                signIn();
                break;
        }
    }
}
