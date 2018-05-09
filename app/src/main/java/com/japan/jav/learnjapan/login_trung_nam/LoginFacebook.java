package com.japan.jav.learnjapan.login_trung_nam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.view.HomeActivity;
import com.japan.jav.learnjapan.model.User;
import com.japan.jav.learnjapan.service.Constants;
import com.japan.jav.learnjapan.service.DatabaseService;

import java.util.Arrays;

/**
 * Created by fstha on 31/03/2018.
 */

public class LoginFacebook {
    private LoginManager loginManager;
    public CallbackManager callbackManager;
    private Activity mActivity;
    private FirebaseAuth mAuth;

    private DatabaseService mData = DatabaseService.getInstance();
    private ProgressDialog progressDialog;
    private static String email = "";
    private static String userName = "";
    private static String avatar = "";
    private static String idUser;

    public LoginFacebook(LoginManager loginManager, CallbackManager callbackManager, Activity mActivity) {
        this.loginManager = loginManager;
        this.callbackManager = callbackManager;
        this.mActivity = mActivity;
        progressDialog = new ProgressDialog(mActivity);
        mAuth = mData.getFirebaseAuth();
    }

    public void loginFacebook (){
//        String [] permission = {"public_profile", "email", "user_friends"};
        loginManager.logInWithReadPermissions(mActivity, Arrays.asList("email", "public_profile", "user_friends"));
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                //Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(mActivity, R.string.login_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(mActivity, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        showProgress();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.e("loginfacebook", "handleFacebookAccessToken: " + token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_SHORT).show();
                            email = task.getResult().getUser().getEmail();
                            userName = task.getResult().getUser().getDisplayName();
                            avatar = task.getResult().getUser().getPhotoUrl().toString();
                            idUser = task.getResult().getUser().getUid();
                            User user = new User(idUser, userName, email,avatar);
                            createUserOnFireBase(user);
                            Intent intent = new Intent(mActivity, HomeActivity.class);
                            intent.putExtra(Constants.USER_ID, idUser);
                            mActivity.startActivity(intent);
                            if(progressDialog.isShowing()){
                                hideProgress();
                                mActivity.finish();
                            }
                        } else {
                            hideProgress();
                            Toast.makeText(mActivity, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                        Log.d("UNSUCCESSFUL", "SignInError");
                        Toast.makeText(mActivity, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + e.getMessage().toString());
                    }
        });
    }

    private void createUserOnFireBase(final User user){
        final DatabaseReference userNode = mData.createDatabase(Constants.USER_NODE).child(user.getId());
        userNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    userNode.setValue(user);
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.login_success),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    hideProgress();
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.login_success),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgress (){
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress (){
        progressDialog.hide();
    }
}
