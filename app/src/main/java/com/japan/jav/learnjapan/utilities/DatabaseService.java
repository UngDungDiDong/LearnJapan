package com.japan.jav.learnjapan.utilities;

/**
 * Created by lana on 18/01/2018.
 */

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by TrungNguyeen on 30/03/2018.
 */

public class DatabaseService {
    private static DatabaseService mInstance;
    private static FirebaseAuth mAuth;
    private static FirebaseDatabase mDatabase;
    private static FirebaseUser mUser;

    protected DatabaseService()
    {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
    }
    public FirebaseAuth getFirebaseAuth(){
        Log.d("TAG: ", mAuth.toString());
        return mAuth;
    }
    public static DatabaseService getInstance(){
        if(mInstance == null){
            mInstance = new DatabaseService();
        }
        return mInstance;
    }
    public static DatabaseReference createDatabase(String databaseName){
        return mDatabase.getInstance().getReference(databaseName);
    }
    public static DatabaseReference getDatabase(){
        return mDatabase.getInstance().getReference();
    }
    public static boolean isSignIn(){
        if(mUser == null)
            return false;
        else
            return true;
    }
    public void signOut(){
        mUser = null;
        mAuth.signOut();
    }

    public static String getUserID(){
        if(!isSignIn())
            return "";
        else
            return mUser.getUid().toString();
    }

    public static String getPhotoUrl(){
        if(!isSignIn())
            return "";
        else{
            if(mUser.getPhotoUrl() == null){
                return "";
            }
            else
                return mUser.getPhotoUrl().toString();
        }
    }
    public static String getDisplayName(){
        if(!isSignIn())
            return "";
        else
        {
            if(mUser.getDisplayName() == null)
                return "";
            else
                return mUser.getDisplayName().toString();
        }
    }
    public static String getEmail(){
        if(!isSignIn())
            return "";
        else
            return mUser.getEmail().toString();
    }
}
