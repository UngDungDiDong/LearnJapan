package com.japan.jav.learnjapan.complete_profile_bang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.japan.jav.learnjapan.R;

import java.util.ArrayList;


public class CompleteProfileActivity extends AppCompatActivity {

    Spinner spProvince, spDistrict, spCommune;
    ArrayList<String> provices, districs, communes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile_bang);

        addControls();
        addEvents();
    }

    private void addControls() {

        addSpinnerProvince();
        addSpinnerDistrict();
        addSpinnerCommune();
    }

    private void addSpinnerCommune() {
        spCommune = findViewById(R.id.spCommune);
        spCommune.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Phường/Xã"}
        ));
    }

    private void addSpinnerDistrict() {
        spDistrict = findViewById(R.id.spDistrict);
        spDistrict.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Quận/H"}
        ));
    }

    private void addSpinnerProvince() {
        spProvince = findViewById(R.id.spProvince);
        spProvince.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Tỉnh/Thành phố"}
        ));
    }

    private void addEvents() {

    }
}
