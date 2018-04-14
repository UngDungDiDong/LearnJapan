package com.japan.jav.learnjapan.setting_khang;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.japan.jav.learnjapan.R;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView txtSelectTime;
    private RadioButton rb2,rb3,rb4,rb5,rb6,rb7,rbCN;

    private static final String TAG = SettingActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_khang);


        addControls();
        addEvents();
    }

    private void addControls() {
        txtSelectTime = findViewById(R.id.txtSelectTime);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addEvents() {
        txtSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = selectedHour < 9 ? "0"+selectedHour : selectedHour+"";
                        String minute = selectedMinute < 9 ? "0"+selectedMinute : selectedMinute+"";
                        txtSelectTime.setText( hour + ":" + minute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

}
