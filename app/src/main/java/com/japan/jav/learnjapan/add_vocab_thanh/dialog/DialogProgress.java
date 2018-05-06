package com.japan.jav.learnjapan.add_vocab_thanh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.japan.jav.learnjapan.R;

/**
 * Created by thanh on 5/6/2018.
 */

public class DialogProgress extends Dialog {

    public DialogProgress(Context context) {
        super(context, R.style.FullscreenDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress_thanh);

        FrameLayout frmContainer = (FrameLayout) findViewById(R.id.frm_container);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        frmContainer.startAnimation(fadeIn);

        setCancelable(false);
    }
}
