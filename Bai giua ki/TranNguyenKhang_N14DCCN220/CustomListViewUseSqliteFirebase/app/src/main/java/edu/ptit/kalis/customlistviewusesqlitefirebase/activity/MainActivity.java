package edu.ptit.kalis.customlistviewusesqlitefirebase.activity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Random;

import edu.ptit.kalis.customlistviewusesqlitefirebase.R;
import edu.ptit.kalis.customlistviewusesqlitefirebase.adapter.MyAdapter;
import edu.ptit.kalis.customlistviewusesqlitefirebase.model.FakeObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "1";
    private static final String tableName = "App";
    private ArrayList<FakeObject> datas = new ArrayList<>();
    private MyAdapter mApdater = null;
    private ListView mListView;
    private SQLiteDatabase sqLiteDB;
    private Button btnFireBase;
    private Button btnSqlite;
    private Button btnInsert;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDB;
    private boolean isSqlite = true;
    private EditText txtAppName, txtOwner;
    private ConstraintLayout mLayout;
    private DatabaseReference myRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add Firebase
        firebaseDB = FirebaseDatabase.getInstance();
        sqLiteDB = openOrCreateDatabase("androidapps.s3db", MODE_PRIVATE, null);


        addFireBaseEvent();
        addControls();
        addEvents();
        loadSqliteDatabase();
//        addFakeDatas();
        loadFireBaseDb();

    }

    private void addFireBaseEvent() {
        myRef = firebaseDB.getReference("App");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!isSqlite) {
                    FakeObject obj = dataSnapshot.getValue(FakeObject.class);
                    obj.setImgBytes(Base64.decode(obj.getImgBase64(),Base64.DEFAULT));
                    mApdater.add(obj);
                    mApdater.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (!isSqlite) {
                    mApdater.clear();
                    FakeObject obj = dataSnapshot.getValue(FakeObject.class);

                    for(int i=0;i<mApdater.getCount();i++) {
                        FakeObject o = mApdater.getItem(i);
                        if (o.getName().equalsIgnoreCase(obj.getName()) && o.getOwner().equalsIgnoreCase(obj.getOwner())
                                && o.getRating() == obj.getRating()) {
                            mApdater.remove(o);
                            break;
                        }
                    }

                    mApdater.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadFireBaseDb() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!isSqlite) {
                    mApdater.clear();
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        FakeObject obj = child.getValue(FakeObject.class);
                        obj.setImgBytes(Base64.decode(obj.getImgBase64(),Base64.DEFAULT));
                        mApdater.add(obj);
                    }

                    mApdater.notifyDataSetChanged();
                    mListView.setAlpha(1f);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadSqliteDatabase() {
        isSqlite = true;
        SqliteLoadTask _task = new SqliteLoadTask(mApdater);
        _task.execute();
    }

    private void addFakeDatas() {
        int size = datas.size();
        for(int i=0;i<size;i++) {
            datas.add(datas.get(i));
//            fakeDatas.add(new FakeObject("Khang", "Khang version:"+i, i%6));
        }
    }

    private void addControls() {
        progressBar = findViewById(R.id.progressBar);

        txtAppName = findViewById(R.id.txtAppName);
        txtOwner = findViewById(R.id.txtOwner);
        btnFireBase = findViewById(R.id.btnLoadFireBase);
        btnSqlite = findViewById(R.id.btnLoadSqlite);
        btnInsert = findViewById(R.id.btnInsert);
        mListView = findViewById(R.id.mListView);
        mListView.requestFocus();
        mApdater = new MyAdapter(this, R.layout.my_custom_layout, datas);
        mListView.setAdapter(mApdater);
        mLayout = findViewById(R.id.mainLayout);
    }

    private void addEvents() {
        btnSqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqliteLoadTask _task = new SqliteLoadTask(mApdater);
                _task.execute();
            }
        });
        btnFireBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSqlite = false;
                progressBar.setVisibility(View.VISIBLE);
                mApdater.clear();
                mListView.setAlpha(0f);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadFireBaseDb();
                    }
                }, 1500);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FakeObject obj = datas.get(position);
                String text = datas.get(position).toString();
                showSnackBar(text, android.R.color.holo_green_dark);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = datas.get(position).toString();
                final FakeObject obj = datas.get(position);
                Log.d(TAG, text);
                AlertDialog dialog =new AlertDialog.Builder(MainActivity.this)
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you want to delete " + obj.getName() + " - owner : " + obj.getOwner())
                        .setIcon(android.R.drawable.ic_delete)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (isSqlite) {
                                    try {
                                        String sql = "DELETE FROM App WHERE Name = '" + obj.getName() + "'";
                                        sql += " and Owner = '" + obj.getOwner() + "'";
                                        sql += " and Rating = '" + obj.getRating() + "'";
                                        sqLiteDB.execSQL(sql);
                                        showSnackBar("Delete success!", android.R.color.holo_green_dark);
                                        loadSqliteDatabase();
                                    } catch (Exception e) {
                                        showSnackBar("Error when delete!", android.R.color.holo_red_dark);
                                    }
                                } else {
                                    try {
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Iterable<DataSnapshot> list = dataSnapshot.getChildren();
                                                for (DataSnapshot ds:list) {
                                                    FakeObject o = ds.getValue(FakeObject.class);
                                                    if (o.getName().equalsIgnoreCase(obj.getName())
                                                            && o.getOwner().equalsIgnoreCase(obj.getOwner())
                                                            && o.getRating() == obj.getRating()) {
                                                        ds.getRef().removeValue();
                                                        loadFireBaseDb();
                                                        showSnackBar("Delete success!", android.R.color.holo_green_dark);
                                                        break;
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                showSnackBar("Error when delete!", android.R.color.holo_red_dark);
                                            }
                                        });
                                    } catch (Exception e) {
                                        showSnackBar("Error when delete!", android.R.color.holo_red_dark);
                                    }
                                }
                                dialog.dismiss();
                            }

                        })



                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                return false;
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appName = txtAppName.getText().toString().trim();
                String owner = txtOwner.getText().toString().trim();
                if (appName.equalsIgnoreCase("") || owner.equalsIgnoreCase("")) {
                    showSnackBar("Please fill the form!", android.R.color.holo_red_dark);
                    return;
                }
                int rating = new Random().nextInt(5);
                try {
                    FakeObject o = ((FakeObject)mListView.getAdapter().getItem(0));
                    if (isSqlite) {
                        byte[] imgBytes = o.getImgBytes();
                        ContentValues initialValues = new ContentValues();
                        initialValues.put("Name", appName);
                        initialValues.put("Owner",owner);
                        initialValues.put("Rating", rating);
                        initialValues.put("Image", imgBytes);
                        sqLiteDB.insert("App", null, initialValues);
                        showSnackBar("Insert to Sqltie Successful!", android.R.color.holo_green_dark);
                        loadSqliteDatabase();
                    } else {
                        String key = myRef.child("_id").push().getKey();
                        String imageFile = Base64.encodeToString(o.getImgBytes(), Base64.DEFAULT);
                        myRef.child(key).setValue(new FakeObject(appName + " - from FireBase", owner, rating, imageFile));
                        showSnackBar("Insert to FireBase Successful!", android.R.color.holo_green_dark);
//                        loadFireBaseDb();
                    }
                    View view = MainActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    txtOwner.setText("");
                    txtAppName.setText("");
                } catch (Exception e) {

                }
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

    private class SqliteLoadTask extends AsyncTask<Void, Void, ArrayList<FakeObject>> {

        MyAdapter _adapter = null;
        public SqliteLoadTask(MyAdapter adapter) {
            _adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            mListView.setAlpha(0f);
            _adapter.clear();
        }

        @Override
        protected ArrayList<FakeObject> doInBackground(Void... params) {
            final ArrayList<FakeObject> data = new ArrayList<>();
            try {
                Thread.sleep(1500);
                Cursor c = sqLiteDB.query(tableName, null, null, null, null, null, null);
                while(c.moveToNext()) {
                    data.add(new FakeObject(c.getString(0), c.getString(1), c.getFloat(2), c.getBlob(3)));
                }
                c.close();
                return data;
            } catch (Exception e) {
//                e.printStackTrace();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                final ArrayList<FakeObject> data2 = new ArrayList<>();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS `App` (" +
                                "`Name` TEXT NOT NULL," +
                                "`Owner` TEXT NOT NULL," +
                                "`Rating` REAL," +
                                "`Image` BLOB" +
                                ")";
                        sqLiteDB.execSQL(sqlCreateTable);

                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            FakeObject obj = child.getValue(FakeObject.class);
                            obj.setImgBytes(Base64.decode(obj.getImgBase64(),Base64.DEFAULT));


                            byte[] imgBytes = obj.getImgBytes();
                            ContentValues initialValues = new ContentValues();
                            String name = obj.getName().replaceFirst(" - from FireBase", "");

                            obj.setName(name);
//                            data2.add(obj);
                            initialValues.put("Name", obj.getName());
                            initialValues.put("Owner", obj.getOwner());
                            initialValues.put("Rating", obj.getRating());
                            initialValues.put("Image", imgBytes);
                            sqLiteDB.insert("App", null, initialValues);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                return data2;
            }
        }


        @Override
        protected void onPostExecute(ArrayList<FakeObject> data) {
            _adapter.addAll(data);
            _adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            mListView.setAlpha(1f);
            loadFireBaseDb();
        }

    }
}
