package com.exam.baobang.androidmidtermexam;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AddActivity extends AppCompatActivity {

    ImageView img;
    TextInputEditText txtName, txtPrice, txtDescription;
    Button btnSave, btnCancel;
    Food mFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mFood = (Food) bundle.getSerializable("DATA");
        }

        addControls();
        setData();
        addEvents();
    }

    private void setData() {
        if(mFood != null){

            img.setEnabled(false);

            txtName.setText(mFood.getName());
            txtPrice.setText(mFood.getPrice() + "");
            txtDescription.setText(mFood.getDescription());
            Picasso.get()
                    .load(mFood.getImageUrl())
                    .placeholder(R.drawable.no_image)
                    .into(img);
        }
    }

    private void addEvents() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                ;
            }
        });
    }

    private void saveData() {
        mFood = new Food();
        mFood.setDescription(txtDescription.getText().toString());
        mFood.setName(txtName.getText().toString());
        mFood.setPrice(Double.parseDouble(txtPrice.getText().toString()));
        mFood.setImageUrl("https://firebasestorage.googleapis.com/v0/b/androidmidtermexam.appspot.com/o/1.png?alt=media&token=dd6f619b-c175-4fa6-9f59-d6de45b278dc");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
        String key =  databaseReference.push().getKey();
        mFood.setId(key);
        databaseReference.child(key).setValue(mFood);
        Toast.makeText(this, "Save success", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addControls() {
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescriptions);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        img = findViewById(R.id.img);
    }
}
