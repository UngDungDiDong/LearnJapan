package com.japan.jav.learnjapan.create_account_dan;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.japan.jav.learnjapan.R;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    public static String TAG = SignupActivity.class.getSimpleName();
    EditText mUsername;
    EditText mPassword;
    EditText mEmail;
    EditText mConfirmPass;
    TextView txtUsername;
    TextView txtPass;
    TextView txtEmail;
    TextView txtConfirmPass;
    Button mBtnSignUp;
    FirebaseAuth DatabaseService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_dan);


        mUsername = (EditText) findViewById(R.id.edt_username);
        mEmail = (EditText) findViewById(R.id.edt_email);
        mPassword = (EditText) findViewById(R.id.edt_password);
        mConfirmPass = (EditText) findViewById(R.id.edt_confirm_password);
        mBtnSignUp = (Button) findViewById(R.id.btnSignup);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPass = (TextView) findViewById(R.id.txtPassword);
        txtConfirmPass = (TextView) findViewById(R.id.txtConfirmPass);
        // final DatabaseService mDatabase = DatabaseService.getInstance();
        textChangeListener();
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim(), email = mEmail.getText().toString().trim(),
                        password = mPassword.getText().toString().trim(), comfirmPass = mConfirmPass.getText().toString().trim();
                Log.d("username", username);
                Log.d("email", email);
                Log.d("password", password);
                Log.d("comfirmPass", comfirmPass);

                if (isValidUsername(username) && isValidEmail(email) && isValidPass(password) && isPassMatching(password, comfirmPass)) {
                    Log.d(TAG, "register");
                    hideKeyboard(view);
                    mBtnSignUp.setEnabled(false);
                    mBtnSignUp.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.com_facebook_button_background_color_disabled));
                    registerAccount(email, password, username);
                }
                if (!isConnected()) {
                    Toast.makeText(SignupActivity.this, "Failed to connected account!", Toast.LENGTH_SHORT).show() ;
                    Log.d(TAG, "faill");
                }

            }
        });
    }

    // hỗ trợ điều hướng lên
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // quay lại nhấn
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void textChangeListener() {
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidUsername(editable.toString())) {
                    txtUsername.setText("Can't ");////////////////////////////////
                    Log.d(TAG, "fail2");
                } else
                    txtUsername.setText("");
            }
        });
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidEmail(editable.toString())) {
                    //txtEmail.setText(R.string.invalid_email);
                    txtEmail.setText("can't....");
                    Log.d(TAG, "fail3");
                } else {
                    txtEmail.setText("");
                }
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidPass(editable.toString())) {
                    //txtPass.setText(R.string.invalid_password);
                    txtPass.setText("can't .....");
                    Log.d(TAG, "fail4");
                } else {
                    txtPass.setText("");
                }
            }
        });
        mConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isPassMatching(mPassword.getEditableText().toString(), editable.toString())) {
                    //txtConfirmPass.setText(R.string.pass_not_matching);
                    txtConfirmPass.setText("aasdfghjkl......");
                    Log.d(TAG, "fail5");
                } else {
                    txtConfirmPass.setText("");
                }
            }
        });
    }


    private boolean isValidUsername(String username) {

        if (username.length() >= 6 && username.length() <= 20) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private boolean isValidPass(String pass) {
        if (pass.length() < 6) {
            return false;
        }
        return true;
    }

    private boolean isPassMatching(String pass, String confirm) {
        if (pass.equals(confirm))
            return true;
        return false;
    }


    private void registerAccount(final String email, final String password, final String username) {

    }

    // ẩn bàn phím
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}