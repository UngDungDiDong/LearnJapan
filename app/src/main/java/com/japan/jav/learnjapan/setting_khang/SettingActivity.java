package com.japan.jav.learnjapan.setting_khang;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.japan.jav.learnjapan.R;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView txtSelectTime;
    private CheckBox cb2,cb3,cb4,cb5,cb6,cb7,cbCN;
    private Button btnSettingSave;

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
        btnSettingSave = findViewById(R.id.btnSettingSave);

        cb2 = findViewById(R.id.cbSetting2);
        cb3 = findViewById(R.id.cbSetting3);
        cb4 = findViewById(R.id.cbSetting4);
        cb5 = findViewById(R.id.cbSetting5);
        cb6 = findViewById(R.id.cbSetting6);
        cb7 = findViewById(R.id.cbSetting7);
        cbCN = findViewById(R.id.cbSettingCN);

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
        btnSettingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String [] time = txtSelectTime.getText().toString().split(":");
                int hour,minute;
                try {
                    hour = Integer.parseInt(time[0]);
                    minute = Integer.parseInt(time[1]);
                } catch (Exception e) {
                    Toast.makeText(SettingActivity.this, "Vui lòng chọn thời gian", Toast.LENGTH_LONG);
                    return;
                }
                String dayOfWeek = "";
                if (cb2.isChecked()) dayOfWeek+="1,";
                if (cb3.isChecked()) dayOfWeek+="2,";
                if (cb4.isChecked()) dayOfWeek+="3,";
                if (cb5.isChecked()) dayOfWeek+="4,";
                if (cb6.isChecked()) dayOfWeek+="5,";
                if (cb7.isChecked()) dayOfWeek+="6,";
                if (cbCN.isChecked()) dayOfWeek+="7,";

                if (dayOfWeek == "") {
                    Toast.makeText(SettingActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_LONG);
                    return;
                }
                dayOfWeek = dayOfWeek.substring(0, dayOfWeek.length()-1);

                Toast.makeText(SettingActivity.this, dayOfWeek, Toast.LENGTH_LONG);
            }
        });
    }

}
