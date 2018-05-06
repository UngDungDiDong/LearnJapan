package com.japan.jav.learnjapan.setting_khang;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.base.BaseActivity;
import com.japan.jav.learnjapan.service.Constants;
import com.japan.jav.learnjapan.service.DatabaseService;
import com.japan.jav.learnjapan.setting_khang.model.SettingNotification;

import java.util.Calendar;

public class SettingActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView txtSelectTime;
    private EditText txtMessage;
    private CheckBox cb2,cb3,cb4,cb5,cb6,cb7,cbCN;
    private Button btnSettingSave;
    private ConstraintLayout mLayout;

    private String mUserID = "";
    private String token = "";
    private DatabaseService mData = DatabaseService.getInstance();
    private DatabaseReference snRef = mData.createDatabase(Constants.SETTING_NOTIFICATION);
    private static final String TAG = SettingActivity.class.getSimpleName();

    private SettingNotification obj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_khang);

        setUserID();

        addFireBaseMessaging();
        addControls();
        addEvents();
        loadDataFromFireBase();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
//        finish();
        super.onBackPressed();
    }

    private void loadDataFromFireBase() {
        // load obj
        obj = new SettingNotification();
        snRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    obj = dataSnapshot.child(mUserID).getValue(SettingNotification.class);

                    txtMessage.setText(obj.getMsg()+"");
                    String[] splitTime = obj.getTime().split(" ");

                    int hour = Integer.parseInt(splitTime[2]);
                    hour = hour + 7 > 24 ? (hour+7-24) : hour+7;

                    int min = Integer.parseInt(splitTime[1]);
                    String _hour = hour > 10 ? (hour+"") : ( "0" + hour);
                    String _min = min > 10 ? (min+"") : ( "0" + min);
                    txtSelectTime.setText(_hour+":"+_min);

                    String repeatWeekly = splitTime[5];

                    if (repeatWeekly.contains("1")) cb2.setChecked(true);
                    if (repeatWeekly.contains("2")) cb3.setChecked(true);
                    if (repeatWeekly.contains("3")) cb4.setChecked(true);
                    if (repeatWeekly.contains("4")) cb5.setChecked(true);
                    if (repeatWeekly.contains("5")) cb6.setChecked(true);
                    if (repeatWeekly.contains("6")) cb7.setChecked(true);
                    if (repeatWeekly.contains("7")) cbCN.setChecked(true);
                } catch (Exception e) {


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void setUserID(){
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.USER_ID)){
            mUserID = intent.getStringExtra(Constants.USER_ID);
            Log.i(TAG, "getUserID: " + mUserID);
        }
    }

    private void addFireBaseMessaging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.notification_channel_id);
            String channelName = getString(R.string.notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", token);
    }

    private void addControls() {
        txtSelectTime = findViewById(R.id.txtSelectTime);
        txtMessage = findViewById(R.id.txtMessage);
        btnSettingSave = findViewById(R.id.btnSettingSave);
        mLayout = findViewById(R.id.settingNotificationLayout);

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
                mcurrentTime.add(Calendar.MINUTE, 1);

                int hour = mcurrentTime.get(mcurrentTime.HOUR_OF_DAY);
                int minute = mcurrentTime.get(mcurrentTime.MINUTE);
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
                View v = SettingActivity.this.getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String [] time = txtSelectTime.getText().toString().split(":");
                if (txtMessage.getText().toString().equalsIgnoreCase("")) {
                    txtMessage.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    showSnackBar("Vui lòng điền nội dung cần nhắc nhở", android.R.color.holo_red_dark);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txtMessage.getBackground().clearColorFilter();
                        }
                    }, 1500);
                    return;
                }
                int hour,minute;
                try {
                    hour = Integer.parseInt(time[0]);
                    hour = hour - 7 < 0 ? (24 - 7 - hour) : hour-7;
                    minute = Integer.parseInt(time[1]);
                } catch (Exception e) {
                    showSnackBar("Vui lòng chọn thời gian", android.R.color.holo_red_dark);
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
                    showSnackBar("Vui lòng chọn ngày", android.R.color.holo_red_dark);
                    return;
                }
                dayOfWeek = dayOfWeek.substring(0, dayOfWeek.length()-1);

                String _time = "01 " + minute + " "  + hour + " * * " + dayOfWeek;
                obj = new SettingNotification(token, _time, txtMessage.getText().toString().trim());
                snRef.child(mUserID).setValue(obj);
                showSnackBar("Lưu thành công", android.R.color.holo_green_dark);
            }
        });
    }

    private void showSnackBar(String msg, int source) {
        Snackbar snackbar = Snackbar
                .make(mLayout, msg, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(source));
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }
}
