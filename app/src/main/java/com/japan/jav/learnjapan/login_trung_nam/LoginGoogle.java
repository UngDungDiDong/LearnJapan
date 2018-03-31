package com.japan.jav.learnjapan.login_trung_nam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.complete_profile_bang.CompleteProfileActivity;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.HomeActivity;
import com.japan.jav.learnjapan.model.User;
import com.japan.jav.learnjapan.utilities_trung.Constants;
import com.japan.jav.learnjapan.utilities_trung.DatabaseService;

/**
 * Created by trungnguyeen on 3/31/18.
 */

public class LoginGoogle {

    private static final String TAG = LoginGoogle.class.getSimpleName();
    public static final int RC_SIGN_IN = 9001;
    private static FirebaseAuth mAuth;
    private static Activity mActivity;
    private GoogleSignInClient mGoogleSignInClient;
    private static DatabaseService mData = DatabaseService.getInstance();
    private static ProgressDialog progressDialog;
    private static String email = "";
    private static String userName = "";
    private static String avatar = "";
    private static String idUser;


    public LoginGoogle(String defaultWebClientID, LoginActivity loginActivity) {
        this.mActivity = loginActivity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(defaultWebClientID)
                .requestEmail()
                .build();
        mAuth = mData.getFirebaseAuth();
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
        progressDialog = new ProgressDialog(mActivity);
    }

    public GoogleSignInClient getmGoogleSignInClient(){
        return mGoogleSignInClient;
    }

    public static void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //Display Progress Dialog
        showProgress();

        //tao chung chi dc google xac thuc. dung de xin lay thong tin tu google account
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            email = task.getResult().getUser().getEmail();
                            userName = task.getResult().getUser().getDisplayName();
                            avatar = task.getResult().getUser().getPhotoUrl().toString();
                            idUser = task.getResult().getUser().getUid();
                            User user = new User(idUser, userName, email,avatar);
                            createUserOnFireBase(user);
                            Toast.makeText(mActivity, R.string.login_success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mActivity, CompleteProfileActivity.class);
                            intent.putExtra(Constants.USER_ID, idUser);
                            mActivity.startActivity(intent);
                            if(progressDialog.isShowing()){
                                hideProgress();
                                mActivity.finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mActivity, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgress();
                        // [END_EXCLUDE]
                    }
                });
    }

    private static void createUserOnFireBase(final User user){
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

    private static void showProgress(){
        progressDialog.show();
    }
    private static void hideProgress(){
        progressDialog.dismiss();
        progressDialog.hide();
    }
}
