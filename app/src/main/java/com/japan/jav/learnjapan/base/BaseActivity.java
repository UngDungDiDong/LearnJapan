package com.japan.jav.learnjapan.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.japan.jav.learnjapan.add_vocab_thanh.dialog.DialogProgress;

/**
 * Created by thanh on 5/6/2018.
 */

public class BaseActivity extends AppCompatActivity {
    // progress dialog
    private DialogProgress mProgressDialog;

    protected void showDialog() {
        dismissDialog();
        mProgressDialog = showProgressDialog(this);
    }

    protected void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public DialogProgress showProgressDialog(Context context) {
        DialogProgress progressDialog = new DialogProgress(context);
        progressDialog.show();
        return progressDialog;
    }
    // end progress dialog
}
