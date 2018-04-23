package com.japan.jav.learnjapan.home_navigation_nhi_tam.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.add_vocab_thanh.add.AddVocabActivity;
import com.japan.jav.learnjapan.chart_diem.ChartActivity;
import com.japan.jav.learnjapan.download_nguyen.topic.TopicMojiActivity;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.Constants;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.adapter.RecyclerViewAdapter;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.model.DataTypeEnum;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.model.Set;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.view.HomeActivity;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;
import com.japan.jav.learnjapan.service.DatabaseService;
import com.japan.jav.learnjapan.test_feature_khang_duc.view.TestActivity;

import java.util.ArrayList;

/**
 * Created by tamlv on 3/29/18.
 */

public class MojiFragment extends Fragment {
    private DatabaseReference mDatabase;
    private ArrayList<Set> mojiSetList;

    private FloatingActionButton fabKanji;
    private FloatingActionButton fabCreate;
    private FloatingActionButton fabAdd;

    private DatabaseService mData = DatabaseService.getInstance();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private boolean isStable = true;

    private ArrayList<Moji> listMoji = new ArrayList<>();
    public String FRAGMENT_TAG = "MOJI";
    private FirebaseUser user;
    public MojiFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moji_tam, container, false);

        setControll(view);
        loadMojiData();
        initRecyclerView(view);
        setEvents();
        return view;
    }

    private void setControll(View view) {
        fabKanji = (FloatingActionButton) view.findViewById(R.id.fabKanji);
        fabCreate = (FloatingActionButton) view.findViewById(R.id.fabCreate);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void initRecyclerView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = view.findViewById(R.id.recyclerViewTam);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(mojiSetList);
        recyclerView.setAdapter(adapter);
        user = FirebaseAuth.getInstance().getCurrentUser();
        adapter.setOnBoomMenuItemClick(new RecyclerViewAdapter.OnBoomMenuItemClicked() {
            @Override
            public void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum, Set set, int position) {
                switch (classIndex) {
                    case 0:
                        Toast.makeText(getContext(), "To learn activity", Toast.LENGTH_SHORT).show();
                        //chuyen qua man hinh LEARN
//                        Intent intent = new Intent(getContext(), LearnActivity.class);
//                        intent.putExtra(Constants.SET_BY_USER, set);
//                        intent.putExtra(Constants.DATA_TYPE, dataTypeEnum);
//                        intent.putExtra(Constants.USER_ID, HomeActivity.getUserID());
//                        startActivity(intent);
                        break;
                    case 1:
                        //chuyen qua test
                        Toast.makeText(getContext(), "To test activity", Toast.LENGTH_SHORT).show();

                        new LoadMojiTask(set).execute();
                        break;
                    case 2:
                        Toast.makeText(getContext(), "To chart activity", Toast.LENGTH_SHORT).show();

                        // chuyen qua man hinh Chart
                        Intent intent = new Intent(getContext(), ChartActivity.class);
                        intent.putExtra(Constants.SET_BY_USER, set);
                        intent.putExtra(Constants.DATA_TYPE, dataTypeEnum);
                        intent.putExtra(Constants.SET_ID, set.getId());
                        intent.putExtra(Constants.USER_ID, HomeActivity.getUserID());
                        startActivity(intent);

                        //chuyen qua chart
//                        new LoadDataForChart(mData.getUserID(), set.getId()).execute();
                        break;
                    case 3:
                        //chuyen qua edit tu vung
                        Intent editIntent = new Intent(getContext(), AddVocabActivity.class);
                        editIntent.putExtra(Constants.SET_BY_USER, set);
                        editIntent.putExtra(Constants.CREATE, Constants.MOJI);
                        startActivityForResult(editIntent, HomeActivity.REQUEST_ADD_VOCAB);
                        break;
                    case 4:
                        //xoa item voi position
                        showRemoveDialog(set, position);
                        break;
                }
            }
        });
    }

    private void setEvents() {
        fabKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: floatButton");
                if(!fabCreate.isShown()){
                    fabAdd.show();
                    fabCreate.show();  //required
                    float deg = fabKanji.getRotation() + 45F;
                    fabKanji.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = false;
                }
                else{
                    fabAdd.hide();
                    fabCreate.hide();
                    float deg = fabKanji.getRotation() + 45F;
                    fabKanji.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = true;
                }
            }
        });
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                final View dialogView = layoutInflater.inflate(R.layout.dialog_create_list, null);
                final TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
                final EditText edtSetName = dialogView.findViewById(R.id.edtSetName);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String setName = edtSetName.getText().toString().trim();
                        if(!setName.isEmpty()){
                            Intent intent = new Intent(getContext(), AddVocabActivity.class);
                            intent.putExtra(Constants.CREATE, Constants.MOJI);
                            intent.putExtra(Constants.NAME, setName);
                            startActivityForResult(intent, HomeActivity.REQUEST_ADD_VOCAB);
                        }else{
                            Toast.makeText(getContext(), "Cannot create a new set.\nSet name field is required", Toast.LENGTH_SHORT).show();
                        }
                        fabAdd.hide();
                        fabCreate.hide();
                        fabKanji.setRotation(fabKanji.getRotation()+45F);
                        isStable = true;

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    //chuyen add topic
                    Intent intent = new Intent(getContext(), TopicMojiActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void showRemoveDialog(final Set set, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.remove_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.i("TAG", "onClick: Set " + set.getName() + " position" + position);
                removeData(set.getId(), position);

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void removeData(String setId, int position) {
        mData.getDatabase().child(Constants.MOJI_SET_NODE).child(mData.getUserID()).child(setId).removeValue();
        mData.getDatabase().child(Constants.SET_BY_USER).child(mData.getUserID()).child(setId).removeValue();
        mojiSetList.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void loadMojiData() {
        mojiSetList = new ArrayList<>();
        new LoadMojiDataTask().execute();
    }


    //Load moji set
    public class LoadMojiDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mDatabase.child("MojiSet").child(HomeActivity.getUserID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mojiSetList.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        mojiSetList.add(ds.getValue(Set.class));

                        Log.d("TAMHome: ", ds.getKey()+"/"+String.valueOf(ds.getValue()));
                    }

                    publishProgress();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }
    }

    class LoadMojiTask extends AsyncTask<Void, Void, Void> {

        Set mSet = new Set();

        public LoadMojiTask(Set mSet) {
            this.mSet = mSet;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listMoji.clear();

            mDatabase.child(Constants.SET_BY_USER).child(HomeActivity.getUserID()).child(mSet.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        listMoji.add(data.getValue(Moji.class));
                        Log.d("kanji" , data.getValue(Kanji.class).toString());
                    }
                    publishProgress();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if(listMoji.size() >= 5){
                Intent intentTest = new Intent(getContext(), TestActivity.class);
                intentTest.putExtra(Constants.SET_BY_USER, listMoji);
                intentTest.putExtra(Constants.DATA_TYPE, FRAGMENT_TAG);
                if (user != null) {
                    intentTest.putExtra(Constants.USER_ID, user.getUid());
                    intentTest.putExtra(Constants.KANJI_SET_NODE, mSet.getId());
                }
                startActivity(intentTest);
            }
            else{
                Toast.makeText(getContext(), "Cannot create test.\nLess than 5 items in the set.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
