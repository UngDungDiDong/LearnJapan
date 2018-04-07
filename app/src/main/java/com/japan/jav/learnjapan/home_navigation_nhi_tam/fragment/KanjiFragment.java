package com.japan.jav.learnjapan.home_navigation_nhi_tam.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.databinding.FragmentMojiTamBinding;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.Constants;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.model.DataTypeEnum;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.model.Set;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.adapter.RecyclerViewAdapter;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.view.HomeActivity;

import java.util.ArrayList;

/**
 * Created by tamlv on 4/4/18.
 */

public class KanjiFragment extends Fragment{
    private DatabaseReference mDatabase;
    private ArrayList<Set> kanjiSetList;
    private FragmentMojiTamBinding binding;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private boolean isStable = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_moji_tam, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_moji_tam, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadMojiData();

        initRecyclerView(binding.getRoot());
        setEvents();

        return binding.getRoot();
    }

    private void setEvents() {
        binding.fabKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "onClick: floatButton");
                if(!binding .fabCreate.isShown()){
                    binding.fabAdd.show();
                    binding.fabCreate.show();  //required
                    float deg = binding.fabKanji.getRotation() + 45F;
                    binding.fabKanji.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = false;
                }
                else{
                    binding.fabAdd.hide();
                    binding.fabCreate.hide();
                    float deg = binding.fabKanji.getRotation() + 45F;
                    binding.fabKanji.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    isStable = true;
                }
            }
        });
        binding.fabCreate.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(getContext(), setName, Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(getContext(), CreateVocabActivity.class);
//                            intent.putExtra(Constants.CREATE, Constants.CREATE_KANJI);
//                            intent.putExtra(Constants.NAME, setName);
//                            intent.putExtra(Constants.USER_ID, mUserID);
//                            startActivity(intent);
                        }else{
                            Toast.makeText(getContext(), "Cannot create a new set.\nSet name field is required", Toast.LENGTH_SHORT).show();
                        }
                        binding.fabAdd.hide();
                        binding.fabCreate.hide();
                        binding.fabKanji.setRotation(binding.fabKanji.getRotation()+45F);
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
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    //chuyen add topic
//                    Intent intent = new Intent(getContext(), TopicKanjiActivity.class);
//                    startActivity(intent);
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

    private void initRecyclerView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = view.findViewById(R.id.recyclerViewTam);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(kanjiSetList);
        recyclerView.setAdapter(adapter);


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

//                        new CountItemTask(set).execute();
//                        break;
                    case 2:
                        Toast.makeText(getContext(), "To chart activity", Toast.LENGTH_SHORT).show();

                        //chuyen qua chart
//                        new LoadDataForChart(mData.getUserID(), set.getId()).execute();
//                        break;
                    case 3:
                        //chuyen qua edit tu vung
                        Toast.makeText(getContext(), "To edit activity", Toast.LENGTH_SHORT).show();

//                        Intent editIntent = new Intent(getContext(), EditVocabActivity.class);
//                        editIntent.putExtra(Constants.SET_BY_USER, set);
//                        editIntent.putExtra(Constants.DATA_TYPE, FRAGMENT_TAG);
//                        editIntent.putExtra(Constants.USER_ID, mUserID);
//                        startActivity(editIntent);
                        break;
                    case 4:
                        //xoa item voi position
                        showRemoveDialog(set, position);
                       break;
                }
            }
        });
    }

    public void showRemoveDialog(final Set set, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.remove_warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeData(set.getId(), position);
                Log.i("TAG", "onClick: id " + set.getId());
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void removeData(String id, int position) {
        mDatabase.child(Constants.KANJI_SET_NODE).child(HomeActivity.getUserID()).child(id).removeValue();
        mDatabase.child(Constants.SET_BY_USER).child(HomeActivity.getUserID()).child(id).removeValue();
        kanjiSetList.remove(position);
        adapter.notifyDataSetChanged();

        //  data local
        //        Map myMap = mLocalData.readAllData();
        //        Map kanjiMap = mLocalData.readData(Constants.KANJI_SET_NODE);
        //        Map setByUserMap = mLocalData.readData(Constants.SET_BY_USER_NODE);
        //        kanjiMap.remove(id);
        //        setByUserMap.remove(id);
        //        myMap.put(Constants.KANJI_SET_NODE, kanjiMap);
        //        myMap.put(Constants.SET_BY_USER_NODE, setByUserMap);
        //        String str = new Gson().toJson(myMap);
        //        mLocalData.writeToFile(Constants.DATA_FILE+mUserID, str, getContext());

    }


    private void loadMojiData() {
        kanjiSetList = new ArrayList<>();
        new LoadKanjiDataTask().execute();
    }



    public class LoadKanjiDataTask  extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            //"XjeTdoRw0XYHIgDfFVKVyabyOcw2"
            //HomeActivity.getUserID()
            mDatabase.child(Constants.KANJI_SET_NODE).child(HomeActivity.getUserID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    kanjiSetList.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        kanjiSetList.add(ds.getValue(Set.class));
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

}
