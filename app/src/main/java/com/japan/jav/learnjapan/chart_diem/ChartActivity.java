package com.japan.jav.learnjapan.chart_diem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.chart_diem.model.DataChart;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;


import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    PieChart pieChart;
    private SQLiteDatabase sqLiteDB;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private List<DataChart> mListDataChart;

    private String userID;
    private String setID;

    private ArrayList<Kanji> kanjiList = new ArrayList<>();
    private ArrayList<Moji> mojiList = new ArrayList<>();
    private static boolean isKanji = false;

    private int correctAnswer;
    private int allAnswer;
    private int testTimes;
    private int wrongAnswer;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_diem);

        Intent intent = getIntent();
        if (intent.hasExtra("SetByUser")) {
            String fragmentTag = intent.getStringExtra("DATA_TYPE");
            if (!TextUtils.isEmpty(fragmentTag)){
                if (fragmentTag.equals("KANJI")) {
                    isKanji = true;
                    kanjiList = (ArrayList<Kanji>) intent.getSerializableExtra("SetByUser");

                } else {
                    isKanji = false;
                    mojiList = (ArrayList<Moji>) intent.getSerializableExtra("SetByUser");


                }
            }
            userID = intent.getExtras().getString("userID");
            setID = intent.getExtras().getString("SetId");

        }

        pieChart = findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(8.99f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.BLUE);
        pieChart.setTransparentCircleRadius(61f);


        initDataChart();
        //getChartDataSuccess();
    }


    private void initDataChart() {

        mListDataChart = new ArrayList<>();
        //new LoadChartDataTask().execute();
        //getChartDataSuccess();

        LoadChartDataTask task = new LoadChartDataTask();
        task.execute(new DataChart(userID, setID, 1
                , allAnswer, correctAnswer));

    }

    public class LoadChartDataTask extends AsyncTask<DataChart, Void, Void> {
        private SQLiteDatabase sqLiteDB;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sqLiteDB = openOrCreateDatabase(getString(R.string.test_result_database), MODE_PRIVATE, null);
        }

        @Override
        protected Void doInBackground(DataChart... chartResults) {
            Cursor cursor = null;

            try {
                String selectSql = "Select * from TestResult where ";
                selectSql += "UserId = '" + userID + "' and ";
                selectSql += "SetId = '" + setID + "'";

                DataChart chartRes = chartResults[0];
                cursor = sqLiteDB.rawQuery(selectSql,null);

                for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor
                        .moveToNext()) {
                    testTimes = (cursor.getInt(2) + chartRes.getSoLanTest());
                    allAnswer = testTimes * (cursor.getInt(3) + chartRes.getTongSocau());
                    correctAnswer = (cursor.getInt(4) + chartRes.getSoCauDung());
                    wrongAnswer = allAnswer - correctAnswer;

                    ArrayList<PieEntry> yValues = new ArrayList<>();


                    yValues.add(new PieEntry( correctAnswer , correctAnswer+ " Số từ đã thuộc"));
                    yValues.add(new PieEntry( wrongAnswer   , wrongAnswer+ " Số từ chưa thuộc"));


                    pieChart.setCenterText("Số lần Test: " + testTimes +'\n' + "Tổng số từ: "+ allAnswer);
                    pieChart.setCenterTextSize(24);
                    pieChart.setEntryLabelTextSize(16);

                    PieDataSet dataSet = new PieDataSet(yValues, "THỐNG KÊ");
                    dataSet.setValueTextSize(30);
                    dataSet.setSliceSpace(5f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                    // ---------------- chart set data --------------
                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(20f);
                    data.setValueTextColor(Color.YELLOW);

                    pieChart.setData(data);

                    // refresh the chart
                    pieChart.invalidate();

                }
            } catch (Exception ex) {
                Log.e("Sqlite", ex.toString());
            } finally {
                cursor.close();
                if (sqLiteDB != null || sqLiteDB.isOpen()) {
                    sqLiteDB.close();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            getChartDataSuccess();
        }
    }

//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            mDatabase.child("chart").child(HomeActivity.getUserID()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    mListDataChart.clear();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        mListDataChart.add(ds.getValue(DataChart.class));
//                        Log.d("TAMHome: ", ds.getKey() + "/" + String.valueOf(ds.getValue()));
//                    }
//                    publishProgress();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//            getChartDataSuccess();
//        }
//    }


    private void getChartDataSuccess() {
//        ArrayList<PieEntry> yValues = new ArrayList<>();
//
////        sqLiteDB = openOrCreateDatabase(getString(R.string.test_result_database), MODE_PRIVATE, null);
////        String selectSql = "Select * from TestResult where ";
////        selectSql += "UserId = '" + userID + "' and ";
////        selectSql += "SetId = '" + setID + "'";
////
////
////        Cursor res = sqLiteDB.rawQuery(selectSql,null);
////
////        if (res.moveToFirst()){
////            do {
////                // Passing values
////                String correctAnswer = res.getString(4);
////                String allAnswer = res.getString(3);
////                //String column3 = res.getString(2);
////                // Do something Here with values
////
////
////        for (DataChart item : mListDataChart){
////            correctAnswer +=item.getSoCauDung();
////             allAnswer +=item.getTongSocau();
////
////        }
//
//        yValues.add(new PieEntry( correctAnswer,"Số từ đã thuộc"));
//        yValues.add(new PieEntry( allAnswer - correctAnswer  ,"Số từ chưa thuộc"));
//
//
//        pieChart.setCenterText("Trung bình số câu đúng qua các lần Test");
//        pieChart.setCenterTextSize(24);
//
//        PieDataSet dataSet = new PieDataSet(yValues, "THỐNG KÊ");
//        dataSet.setSliceSpace(5f);
//        dataSet.setSelectionShift(5f);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//
//        PieData data = new PieData(dataSet);
//        data.setValueTextSize(20f);
//        data.setValueTextColor(Color.YELLOW);
//
//        pieChart.setData(data);
//        pieChart.invalidate();
    }

}
