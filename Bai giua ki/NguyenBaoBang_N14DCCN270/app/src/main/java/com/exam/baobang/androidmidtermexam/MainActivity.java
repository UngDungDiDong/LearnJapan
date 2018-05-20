package com.exam.baobang.androidmidtermexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String DATABASE_REFERENCE = "Foods";
    private FoodAdapter mFoodAdapter;
    private ArrayList<Food> mFoods;
    private RecyclerView mRVFood;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference(DATABASE_REFERENCE);

//        mFoods = new ArrayList<>();
//        mFoods.add(new Food("", "Chocolate Glazed Donut With Sprinkles", "https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/1.png?alt=media&token=dd6f619b-c175-4fa6-9f59-d6de45b278dc", 100000 , "This single serving mug cake tastes just like baked old fashioned donuts or the baked donut muffins that became popular a few years ago. "));
//        mFoods.add(new Food("", "Chocolate Glazed Donut With Sprinkles", "https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/10.jpg?alt=media&token=82e3daa4-65ca-491f-ae6e-a175d87afe82", 100000 , "This single serving mug cake tastes just like baked old fashioned donuts or the baked donut muffins that became popular a few years ago. "));
//        mFoods.add(new Food("", "Chocolate Glazed Donut With Sprinkles", "https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/11.png?alt=media&token=a83fd152-bdda-4052-8eee-afd93552a57f", 100000 , "This single serving mug cake tastes just like baked old fashioned donuts or the baked donut muffins that became popular a few years ago. "));
//        mFoods.add(new Food("", "Chocolate Glazed Donut With Sprinkles", "https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/11.png?alt=media&token=a83fd152-bdda-4052-8eee-afd93552a57f", 100000 , "This single serving mug cake tastes just like baked old fashioned donuts or the baked donut muffins that became popular a few years ago. "));
//        mFoods.add(new Food("", "Chocolate Glazed Donut With Sprinkles", "https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/3.jpg?alt=media&token=e00e2dbd-3d94-40da-a6da-8abc8565c4fa", 100000 , "This single serving mug cake tastes just like baked old fashioned donuts or the baked donut muffins that became popular a few years ago. "));
//
//        for(Food food : mFoods){
//            mDatabaseReference.push().setValue(food);
//        }
        addControls();
        getData();
    }

    private void getData() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFoods.clear();
                for(DataSnapshot snapshotPost : dataSnapshot.getChildren()){
                    Food food = snapshotPost.getValue(Food.class);
                    mFoods.add(food);
                }
                mFoodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });

        mRVFood.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return false;
            }
        });
    }

    private void write() {
        ArrayList<Food> foods = new ArrayList<>();
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/1.png?alt=media&token=dd6f619b-c175-4fa6-9f59-d6de45b278dc",100000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/10.jpg?alt=media&token=82e3daa4-65ca-491f-ae6e-a175d87afe82",900000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/2.jpg?alt=media&token=93358d11-c2ff-4b79-b51f-63cd89e4f8ce",800000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/3.jpg?alt=media&token=e00e2dbd-3d94-40da-a6da-8abc8565c4fa",300000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/4.jpg?alt=media&token=8e1136cd-5a22-4e04-ac38-be9e4c422af3",400000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/6.jpg?alt=media&token=88942ec7-bd86-4a7b-b86a-f3a65c8d141f",500000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/7.jpg?alt=media&token=6c9eda69-885f-46c6-91c5-651c39e2d1b6",600000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/8.jpg?alt=media&token=ad9c7fc5-7b2c-4cfd-b53a-edb4a8b59b0f",200000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/9.jpg?alt=media&token=b9e312bc-7847-49cc-baad-15f0f9825d0a",100000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));
        foods.add(new Food(mDatabaseReference.push().getKey(), "Chocolate Caramel Duo","https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/5.jpg?alt=media&token=6194eba6-eb7d-45b4-97f3-c3bf95b9274d",1000000,"The sweet and subtle salty combo of chocolate meets caramel. Introduce the Caramel Duo to your mouth"));

        mDatabaseReference.setValue(foods);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addControls() {
        mFoods = new ArrayList<>();
        mFoodAdapter = new FoodAdapter(this, mFoods);
        mRVFood = findViewById(R.id.rvFood);
        mRVFood.setLayoutManager(new LinearLayoutManager(this));
        mRVFood.setAdapter(mFoodAdapter);
        mRVFood.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }
}
