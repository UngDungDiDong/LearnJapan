package com.japan.jav.learnjapan.test_feature_khang_duc.view;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.base.BaseActivity;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.Constants;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;
import com.japan.jav.learnjapan.model.QuestionAnswer;
import com.japan.jav.learnjapan.test_feature_khang_duc.view.model.ReviewItem;
import com.japan.jav.learnjapan.test_feature_khang_duc.view.model.TestResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by huuduc on 15/03/2018.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView txtRightCount, txtQuestion, txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD;
    private TextView txtNumberQuestion;
    private ConstraintLayout layout_test;
    private TextView txtNotification;
    private Button btnMain;
    private Button btnRetry;
    private Button btnReview;

    private ArrayList<Kanji> kanjiList = new ArrayList<>();
    private ArrayList<Moji> mojiList = new ArrayList<>();
    private static boolean isKanji = false;
    private ArrayList<String> answerList = new ArrayList<>();
    private ArrayList<QuestionAnswer> QAList = new ArrayList<>();
    private ArrayList<Kanji> oldKanjiList = new ArrayList<>();
    private ArrayList<Moji> oldMojiList = new ArrayList<>();

    private ArrayList<ReviewItem> listReviews = new ArrayList<>();
    private String userID;
    private String setID;

    Dialog settingsDialog = null;
    public static int NUMBER_OF_QUESTION = 0;

    private int number_of_right_answer = 0;
    private int index_question = 0;
    public boolean check;
    private int tongSoCau = 0;

    private static final String TAG = TestActivity.class.getSimpleName();

    ViewGroup.MarginLayoutParams defaultMarginLayoutParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_khang_duc);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SET_BY_USER)) {
            String fragmentTag = intent.getStringExtra(Constants.DATA_TYPE);
            if (fragmentTag.equals("KANJI")) {
                isKanji = true;
                kanjiList = (ArrayList<Kanji>) intent.getSerializableExtra(Constants.SET_BY_USER);
                tongSoCau = kanjiList.size();
            } else {
                isKanji = false;
                mojiList = (ArrayList<Moji>) intent.getSerializableExtra(Constants.SET_BY_USER);
                tongSoCau = mojiList.size();
            }
            userID = intent.getExtras().getString(Constants.USER_ID);
            setID = intent.getExtras().getString(Constants.KANJI_SET_NODE);

        }

        Log.d("KanjiList", kanjiList.toString());
        Log.d("MOJI_LIst", mojiList.toString());

        addControls();
        initData(isKanji);
        addEvents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void addEvents() {
        txtAnswerA.setOnClickListener(this);
        txtAnswerB.setOnClickListener(this);
        txtAnswerC.setOnClickListener(this);
        txtAnswerD.setOnClickListener(this);

        btnMain.setOnClickListener(this);
        btnRetry.setOnClickListener(this);
        btnReview.setOnClickListener(this);
    }

    private void addControls() {
        check = false;
        txtRightCount = findViewById(R.id.txtRightCount);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtAnswerA = findViewById(R.id.txtAnswerA);
        txtAnswerB = findViewById(R.id.txtAnswerB);
        txtAnswerC = findViewById(R.id.txtAnswerC);
        txtAnswerD = findViewById(R.id.txtAnswerD);
        txtNumberQuestion = findViewById(R.id.txtNumberOfQuestion);
        btnMain = findViewById(R.id.btnMain);
        btnRetry = findViewById(R.id.btnRetry);
        btnReview = findViewById(R.id.btnReview);
        txtNotification = findViewById(R.id.txtNotification);

        layout_test = findViewById(R.id.layout_test_activity);


        btnMain.setVisibility(View.GONE);
        btnRetry.setVisibility(View.GONE);
        btnReview.setVisibility(View.GONE);
        txtNotification.setVisibility(View.GONE);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData(boolean isKanji) {
        if (isKanji) {
            for (int i = 0; i < kanjiList.size(); i++) {
                oldKanjiList.add(kanjiList.get(i));
                answerList.add(kanjiList.get(i).getAmhan());
                QAList.add(new QuestionAnswer(kanjiList.get(i).getKanji(),
                        kanjiList.get(i).getAmhan()));
            }
            NUMBER_OF_QUESTION = kanjiList.size();
            updateQuestionKanji(kanjiList, answerList);
        } else {
            for (int i = 0; i < mojiList.size(); i++) {
                oldMojiList.add(mojiList.get(i));
                answerList.add(mojiList.get(i).getNghiaTiengViet());
                QAList.add(new QuestionAnswer(mojiList.get(i).getTuTiengNhat(),
                        mojiList.get(i).getNghiaTiengViet()));
            }
//            NUMBER_OF_QUESTION = 5; // fortest
            NUMBER_OF_QUESTION = mojiList.size();
            updateQuestionMoji(mojiList, answerList);
        }
        txtNumberQuestion.setText("QUESTION: " + String.valueOf(index_question + 1) + "/"
                + String.valueOf(NUMBER_OF_QUESTION));
        txtRightCount.setText(String.valueOf(number_of_right_answer));
    }

    public void updateQuestionMoji(ArrayList<Moji> listMoji, ArrayList<String> listAnswer) {
        ArrayList<String> listCurrentAnswer = new ArrayList<>(listAnswer);

        Log.d("ANSWER_SIZE", String.valueOf(listCurrentAnswer.size()));
        int index_moji = 0;
        index_moji = new Random().nextInt(listMoji.size());

        Log.d("INDEX_MOJI", String.valueOf(index_moji));
        int index_one;
        int index_two;
        int index_three;

        String answer_mojiRemove = "";
        String answerRemove_1 = "";
        String answerRemove_2 = "";

        txtQuestion.setText(listMoji.get(index_moji).getTuTiengNhat());


        int index_one_location = new Random().nextInt(4);
        switch (index_one_location) {
            case 0:
                // set dap an vao cau A
                txtAnswerA.setText(listMoji.get(index_moji).getNghiaTiengViet());
                // đánh dấu câu trả lời đúng của câu hỏi
                answer_mojiRemove = listMoji.get(index_moji).getNghiaTiengViet();
                // remove khỏi list answer
                listCurrentAnswer.remove(answer_mojiRemove);


                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerB.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);
                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);

                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);
                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 1:
                txtAnswerB.setText(listMoji.get(index_moji).getNghiaTiengViet());
                answer_mojiRemove = listMoji.get(index_moji).getNghiaTiengViet();
                listCurrentAnswer.remove(answer_mojiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
            case 2:
                txtAnswerC.setText(listMoji.get(index_moji).getNghiaTiengViet());
                answer_mojiRemove = listMoji.get(index_moji).getNghiaTiengViet();
                listCurrentAnswer.remove(answer_mojiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 3:
                txtAnswerD.setText(listMoji.get(index_moji).getNghiaTiengViet());
                answer_mojiRemove = listMoji.get(index_moji).getNghiaTiengViet();
                listCurrentAnswer.remove(answer_mojiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerC.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
        }
//        listMoji.remove(listMoji.get(index_moji));
    }

    public void updateQuestionKanji(ArrayList<Kanji> listKanji, ArrayList<String> listAnswer) {
        ArrayList<String> listCurrentAnswer = new ArrayList<>(listAnswer);


        Log.d("ANSWER_SIZE", String.valueOf(listCurrentAnswer.size()));
        int index_kanji = 0;
        index_kanji = new Random().nextInt(listKanji.size());

        Log.d("INDEX_MOJI", String.valueOf(index_kanji));
        int index_one;
        int index_two;
        int index_three;

        String answer_kanjiRemove = "";
        String answerRemove_1 = "";
        String answerRemove_2 = "";

        txtQuestion.setText(listKanji.get(index_kanji).getKanji());


        int index_one_location = new Random().nextInt(4);
        switch (index_one_location) {
            case 0:
                // set dap an vao cau A
                txtAnswerA.setText(listKanji.get(index_kanji).getAmhan());
                // đánh dấu câu trả lời đúng của câu hỏi
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                // remove khỏi list answer
                listCurrentAnswer.remove(answer_kanjiRemove);


                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerB.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);
                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);

                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);
                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 1:
                txtAnswerB.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listCurrentAnswer.remove(answer_kanjiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerC.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));

                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
            case 2:
                txtAnswerC.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listCurrentAnswer.remove(answer_kanjiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerD.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));

                break;
            case 3:
                txtAnswerD.setText(listKanji.get(index_kanji).getAmhan());
                answer_kanjiRemove = listKanji.get(index_kanji).getAmhan();
                listCurrentAnswer.remove(answer_kanjiRemove);

                index_one = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_1 = listCurrentAnswer.get(index_one);
                txtAnswerA.setText(answerRemove_1);
                listCurrentAnswer.remove(answerRemove_1);

                index_two = new Random().nextInt(listCurrentAnswer.size());
                answerRemove_2 = listCurrentAnswer.get(index_two);
                txtAnswerB.setText(answerRemove_2);
                listCurrentAnswer.remove(answerRemove_2);

                index_three = new Random().nextInt(listCurrentAnswer.size());
                txtAnswerC.setText(listCurrentAnswer.get(index_three));
                Log.d("INDEX_ONE", String.valueOf(index_one));
                Log.d("INDEX_TWO", String.valueOf(index_two));
                Log.d("INDEX_THREE", String.valueOf(index_three));
                break;
        }
//        listKanji.remove(listKanji.get(index_kanji));
    }

    public void updateNumberOfRightAnswer() {
        txtRightCount.setText(String.valueOf(number_of_right_answer));
    }

    public void checkTrueOrFalseAnswer(boolean check, final TextView answer_view) {
        if (check == true) {
            new CountDownTimer(1100, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d("TAG", "onTick: " + "seconds remaining: " + millisUntilFinished / 1000);

                    answer_view.setBackgroundColor(Color.parseColor("#4CAF50"));
                    answer_view.setTextColor(Color.WHITE);
                    txtAnswerA.setEnabled(false);
                    txtAnswerB.setEnabled(false);
                    txtAnswerC.setEnabled(false);
                    txtAnswerD.setEnabled(false);

                    //next question if touch on screen
                    layout_test.setEnabled(true);
                    layout_test.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: clicked layout_test");
                            onFinish();
                            cancel();
                        }
                    });
                }

                public void onFinish() {
                    Log.d(TAG, "onFinish: countdowntimer");
                    layout_test.setEnabled(false);
                    finishCheckAnswer(answer_view);
                }
            }.start();
        } else {
            new CountDownTimer(1100, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, "onTick");
                    Log.d("TAG", "onTick: " + "seconds remaining: " + millisUntilFinished / 1000);
                    answer_view.setBackgroundColor(Color.parseColor("#C62828"));
                    answer_view.setTextColor(Color.WHITE);
                    txtAnswerA.setEnabled(false);
                    txtAnswerB.setEnabled(false);
                    txtAnswerC.setEnabled(false);
                    txtAnswerD.setEnabled(false);

                    //next question if touch on screen
                    layout_test.setEnabled(true);
                    layout_test.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: clicked layout_test");
                            onFinish();
                            cancel();
                        }
                    });
                }

                public void onFinish() {
                    Log.d(TAG, "onFinish: countdowntimer");
                    layout_test.setEnabled(false);
                    finishCheckAnswer(answer_view);
                }
            }.start();
        }
    }

    public void finishCheckAnswer(TextView answer_view) {
        Log.d(TAG, "finishCheckAnswer: isKanji" + isKanji);
        answer_view.setTextColor(Color.parseColor("#AB5E4F"));
        answer_view.setBackgroundResource(R.drawable.border);
        txtAnswerA.setEnabled(true);
        txtAnswerB.setEnabled(true);
        txtAnswerC.setEnabled(true);
        txtAnswerD.setEnabled(true);
        index_question++;
        String numberQuestion = "Question: " + String.valueOf(index_question + 1) + "/"
                + String.valueOf(NUMBER_OF_QUESTION);
        if (index_question != NUMBER_OF_QUESTION) txtNumberQuestion.setText(numberQuestion);
        txtRightCount.setText(String.valueOf(number_of_right_answer));
        if (isKanji) {
            if (index_question == NUMBER_OF_QUESTION) {
                Log.d("KANJIlist_EMPTY", "KANJIlist_EMPTY");
                updateNumberOfRightAnswer();
                showResult();
            } else {
                updateQuestionKanji(kanjiList, answerList);
            }
        } else {
            if (index_question == NUMBER_OF_QUESTION) {
                Log.d("MOJIlist_EMPTY", "MOJIlist_EMPTY");
                updateNumberOfRightAnswer();
                showResult();
            } else {
                updateQuestionMoji(mojiList, answerList);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (settingsDialog != null) {
            settingsDialog.dismiss();
        }
    }

    public void showResult() {
        if (settingsDialog != null) {
            settingsDialog.dismiss();
        }
        txtQuestion.setVisibility(View.GONE);
        txtAnswerA.setVisibility(View.GONE);
        txtAnswerB.setVisibility(View.GONE);
        txtAnswerC.setVisibility(View.GONE);
        txtAnswerD.setVisibility(View.GONE);

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) txtNumberQuestion.getLayoutParams();
        defaultMarginLayoutParams = new ViewGroup.MarginLayoutParams(marginLayoutParams);
        marginLayoutParams.setMargins(0, 100, 0, 0);

        txtRightCount.setTextSize(100f);

        txtNotification.setVisibility(View.VISIBLE);
        btnMain.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.VISIBLE);
        btnReview.setVisibility(View.VISIBLE);
//        Save Data To Sqlite

        SqliteLoadTask task = new SqliteLoadTask();
        task.execute(new TestResult(userID, setID, 1
                , tongSoCau, number_of_right_answer));
    }

    @Override
    public void onClick(View view) {
        String question = txtQuestion.getText().toString();

        String answer = "";

        switch (view.getId()) {
            case R.id.txtAnswerA:
                answer = txtAnswerA.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        ReviewItem reviewItem = new ReviewItem(question, item.getAnswer(), answer);
                        listReviews.add(reviewItem);
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {

                            check = false;
                        }
                    }
                }
                checkTrueOrFalseAnswer(check, txtAnswerA);

                break;
            case R.id.txtAnswerB:

                answer = txtAnswerB.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        ReviewItem reviewItem = new ReviewItem(question, item.getAnswer(), answer);
                        listReviews.add(reviewItem);
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {
                            check = false;
                        }
                    }
                }
                checkTrueOrFalseAnswer(check, txtAnswerB);
                break;
            case R.id.txtAnswerC:
                answer = txtAnswerC.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        ReviewItem reviewItem = new ReviewItem(question, item.getAnswer(), answer);
                        listReviews.add(reviewItem);
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {
                            check = false;
                        }
                    }
                }
                checkTrueOrFalseAnswer(check, txtAnswerC);
                break;
            case R.id.txtAnswerD:
                answer = txtAnswerD.getText().toString();
                for (QuestionAnswer item : QAList) {
                    if (item.getQuestion().equalsIgnoreCase(question)) {
                        ReviewItem reviewItem = new ReviewItem(question, item.getAnswer(), answer);
                        listReviews.add(reviewItem);
                        if (item.getAnswer().equalsIgnoreCase(answer)) {
                            check = true;
                            number_of_right_answer++;
                        } else {
                            check = false;
                        }
                    }
                }
                checkTrueOrFalseAnswer(check, txtAnswerD);

                break;

            case R.id.btnMain:
                finish();
                break;

            case R.id.btnRetry:
                answerList.clear();
                QAList.clear();

                NUMBER_OF_QUESTION = 0;

                number_of_right_answer = 0;
                index_question = 0;

                txtRightCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) txtNumberQuestion.getLayoutParams();
                marginLayoutParams.setMargins(defaultMarginLayoutParams.leftMargin,
                        defaultMarginLayoutParams.topMargin,
                        defaultMarginLayoutParams.rightMargin, defaultMarginLayoutParams.bottomMargin);

                txtRightCount.setText(number_of_right_answer + "");


                initData(isKanji);

                txtRightCount.setVisibility(View.VISIBLE);
                txtQuestion.setVisibility(View.VISIBLE);
                txtAnswerA.setVisibility(View.VISIBLE);
                txtAnswerB.setVisibility(View.VISIBLE);
                txtAnswerC.setVisibility(View.VISIBLE);
                txtAnswerD.setVisibility(View.VISIBLE);
                txtNumberQuestion.setVisibility(View.VISIBLE);

                btnMain.setVisibility(View.GONE);
                btnRetry.setVisibility(View.GONE);
                txtNotification.setVisibility(View.GONE);
                btnReview.setVisibility(View.GONE);
                break;
//                Intent refresh = new Intent(this, TestActivity.class);
//                if (isKanji) {
//                    Log.d("test", String.valueOf(kanjiList.size()));
//
//                    refresh.putExtra(Constants.SET_BY_USER, oldKanjiList);
//                    refresh.putExtra(Constants.DATA_TYPE, "KANJI");
//
//                } else {
//
//                    refresh.putExtra(Constants.SET_BY_USER, oldMojiList);
//                    refresh.putExtra(Constants.DATA_TYPE, "MOJI");
//                }
//                refresh.putExtra(Constants.USER_ID, userID);
//                refresh.putExtra(Constants.KANJI_SET_NODE, setID);
//                startActivity(refresh);
//                this.finish();
            case R.id.btnReview:

                if (isKanji) {
                    HashSet<ReviewItem> withoutDuplicate = new HashSet<>(listReviews);
                    listReviews.clear();
                    listReviews.addAll(withoutDuplicate);
                    Log.e(TAG, "onC: " + listReviews.size());
                    Intent intent = new Intent(TestActivity.this, TestReviewActivity.class);
                    intent.putExtra(Constants.LIST_REVIEW, listReviews);
                    startActivity(intent);
                } else {
                    HashSet<ReviewItem> withoutDuplicate = new HashSet<>(listReviews);
                    listReviews.clear();
                    listReviews.addAll(withoutDuplicate);
                    Log.e(TAG, "onC: " + listReviews.size());
                    Intent intent = new Intent(TestActivity.this, TestReviewActivity.class);
                    intent.putExtra(Constants.LIST_REVIEW, listReviews);
                    startActivity(intent);
                }
                break;
        }
    }

    private class SqliteLoadTask extends AsyncTask<TestResult, Void, Void> {
        private SQLiteDatabase sqLiteDB;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
            sqLiteDB = openOrCreateDatabase(getString(R.string.test_result_database), MODE_PRIVATE, null);
        }

        @Override
        protected Void doInBackground(TestResult... testResults) {
            Cursor cursor = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String time = sdf.format(new Date());
                String sqlCreateTable = "CREATE TABLE IF NOT EXISTS `TestResult` (" +
                        "`UserId` TEXT NOT NULL," +
                        "`SetId` TEXT NOT NULL," +
                        "`SoLanTest` INTEGER NOT NULL," +
                        "`TongSoCau` INTEGER NOT NULL," +
                        "`SoCauDung` INTEGER NOT NULL" +
                        ")";
                sqLiteDB.execSQL(sqlCreateTable);

                // save to db
                String selectSql = "Select * from TestResult where ";
                selectSql += "UserId = '" + userID + "' and ";
                selectSql += "SetId = '" + setID + "'";

                TestResult testRes = testResults[0];
                cursor = sqLiteDB.rawQuery(selectSql, null);

                String sql =
                        "INSERT or replace INTO " + getString(R.string.test_result_table)
                                + " (UserId, SetId, SoLanTest, TongSoCau, SoCauDung) VALUES('"
                                + testRes.getUserId() + "','"
                                + testRes.getSetId() + "','"
                                + testRes.getSoLanTest() + "','"
                                + testRes.getTongSocau() + "','"
                                + testRes.getSoCauDung() + "')";

                if (cursor != null && cursor.moveToFirst()) {
                    sql = "UPDATE " + getString(R.string.test_result_table)
                            + " SET "
                            + "SoLanTest='" + (cursor.getInt(2) + 1) + "'"
                            + ", SoCauDung='" + (cursor.getInt(4) + testRes.getSoCauDung()) + "'"
                            + " WHERE UserId='" + testRes.getUserId() + "'"
                            + " and SetId='" + testRes.getSetId() + "'";
                }

                sqLiteDB.execSQL(sql);
            } catch (Exception e) {
                Log.e("Sqlite", e.toString());
            } finally {
                dismissDialog();
                cursor.close();
            }
            return null;
        }
    }
}
