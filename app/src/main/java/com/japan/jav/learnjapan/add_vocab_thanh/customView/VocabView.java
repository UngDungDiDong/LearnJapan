package com.japan.jav.learnjapan.add_vocab_thanh.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.add_vocab_thanh.add.AddVocabActivity;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;
import com.japan.jav.learnjapan.model.Vocab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thanh on 4/13/2018.
 */

public class VocabView extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.edt_tu_vung)
    EditText edtTuVung;

    @BindView(R.id.edt_hiragana)
    EditText edtHiragana;

    @BindView(R.id.edt_am_han)
    EditText edtAmHan;

    @BindView(R.id.edt_nghia)
    EditText edtNghia;

    @BindView(R.id.imv_delete)
    ImageView imvDelete;

    private Context mContext;
    private int type;

    public VocabView(Context context, int type, OnDeleteListener callBack) {
        super(context);
        this.mContext = context;
        View contactView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_moji_thanh, null);
        ButterKnife.bind(this, contactView);
        this.listener = callBack;
        this.type = type;
        if (type == AddVocabActivity.KANJI) {
            edtHiragana.setVisibility(GONE);
            edtTuVung.setHint(mContext.getResources().getString(R.string.kanji));
            edtAmHan.setHint(mContext.getResources().getString(R.string.am_han));
            edtNghia.setHint(mContext.getResources().getString(R.string.tu_vung_back));
        }

        imvDelete.setOnClickListener(this);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16, 0, 16, 16);
        addView(contactView, layoutParams);
    }

    public boolean checkData() {
        if (type == AddVocabActivity.MOJI) {
            if (edtTuVung.getText().toString().isEmpty() || edtHiragana.getText().toString().isEmpty()
                    || edtAmHan.getText().toString().isEmpty() || edtNghia.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "Please Input Information!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (edtTuVung.getText().toString().isEmpty() || edtAmHan.getText().toString().isEmpty()
                    || edtNghia.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "Please Input Information!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public Kanji getDataKanji() {
        Kanji kanji = new Kanji();
        kanji.setKanji(edtTuVung.getText().toString());
        kanji.setAmhan(edtAmHan.getText().toString());
        kanji.setTuvung(edtNghia.getText().toString());
        return kanji;
    }

    public Moji getDataMoji() {
        Moji moji = new Moji();
        moji.setTuTiengNhat(edtTuVung.getText().toString());
        moji.setCachDocHira(edtHiragana.getText().toString());
        moji.setAmHan(edtAmHan.getText().toString());
        moji.setNghiaTiengViet(edtNghia.getText().toString());
        return moji;
    }

    public void setData(Vocab vocab) {
        edtTuVung.setText(vocab.getTuVung());
        edtHiragana.setText(vocab.getHiragana());
        edtAmHan.setText(vocab.getAmHan());
        edtNghia.setText(vocab.getNghia());
    }

    private OnDeleteListener listener;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_delete:
                listener.onDelete(this);
                break;
        }
    }

    public interface OnDeleteListener {
        void onDelete(View view);
    }
}
