package com.japan.jav.learnjapan.complete_profile_bang;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;


public class CompleteProfileActivity extends AppCompatActivity {
    // constants


    private static final String TAG = "CompleteProfileActivity";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private static int CAMERA_PIC_REQUEST = 1337;
    private static int SELECT_FILE = 1338;
    private static final int RC_SIGN_IN = 123;
    private static final String FIREBASE_URL_STORE = "gs://learnjapan-4b5b5.appspot.com";
    private static final String FIREBASE_STORE_PATH = "avatar/";
    private static final Object lock = new Object();
    // views
    private ExtendedEditText txtFullName, txtPhone, txtEmail, txtDateOfBirth, txtAddress;

    private Button btnUpdate, banCancel, btnGetDate;

    private RadioButton rbMale, rbFemale;

    private RadioGroup rgGender;

    private ImageView imgAvatar;

    // temp available
    private String avatarUrl = "";

    private Bitmap avatar;

    private User user;


    // firebase available
    private FirebaseAuth mAuth;

    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseUser currentUser = null;

    private FirebaseStorage mFirebaseStorage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile_bang);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance(FIREBASE_URL_STORE);

        addControls();

        addEvents();
    }

    private void addControls() {

        txtFullName = findViewById(R.id.txtFullName);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtDateOfBirth = findViewById(R.id.txtDateOfBirth);
        txtAddress = findViewById(R.id.txtAddress);

        rgGender = findViewById(R.id.rgGender);
        rbFemale = findViewById(R.id.rbFemale);
        rbMale = findViewById(R.id.rbMale);

        imgAvatar = findViewById(R.id.imgAvatar);
        banCancel = findViewById(R.id.btnCancle);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnGetDate = findViewById(R.id.btnGetDate);

        attachData();

    }

    private void attachData() {
        if(currentUser != null){
            String userUID = currentUser.getUid();
            mFirebaseDatabase
                    .getReference()
                    .child("User")
                    .child(userUID)
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            if(user == null){

                                txtFullName.setText(currentUser.getDisplayName());
                                txtEmail.setText(currentUser.getEmail());
                                txtPhone.setText(currentUser.getPhoneNumber());

                                Uri photo = currentUser.getPhotoUrl();

                                Picasso.with(CompleteProfileActivity.this)
                                        .load(photo)
                                        .placeholder(R.drawable.noimage)
                                        .into(imgAvatar);

                                avatarUrl = photo == null ? "" : photo.toString();

                            }else{
                                setDataIntoInput(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void setDataIntoInput(User user){
        try{

            txtFullName.setText(user.getUsername());

            txtDateOfBirth.setText(user.getDateOfBirth());

            if(user.isGender()){
                rbMale.setChecked(true);
            }else{
                rbFemale.setChecked(true);
            }

            txtPhone.setText(user.getPhone());

            txtEmail.setText(user.getEmail());

            txtAddress.setText(user.getAddress());

            avatarUrl = user.getLinkPhoto();

            Picasso.with(this)
                    .load(user.getLinkPhoto())
                    .placeholder(R.drawable.noimage)
                    .into(imgAvatar);

        }catch (Exception e){
            Log.e("SET", e.getMessage());
        }
    }

    private void addEvents() {

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialogChooseImage(CompleteProfileActivity.this);

            }
        });

        banCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateCurrentUser();

            }
        });

        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDate(CompleteProfileActivity.this, txtDateOfBirth);

            }
        });
    }

    public static void getDate(Activity activity, final TextView txt){

        final Calendar calendar = Calendar.getInstance();

        if(!txt.getText().toString().trim().isEmpty()){

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

            try {
                Date date = sdf.parse(txt.getText().toString().trim());
                calendar.setTime(date);// all done
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        txt.setText(new StringBuilder(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
        datePickerDialog.show();
    }

    private void updateCurrentUser() {

        String fullName = txtFullName.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String dateOfBirth = txtDateOfBirth.getText().toString().trim();
        boolean gender = rbMale.isChecked();
        String address = txtAddress.getText().toString().trim();

        if(avatarUrl.isEmpty() && avatar == null ){
            openDialog(this, "Please select your avatar");
            return;
        }

        if(fullName.isEmpty()){
            txtFullName.requestFocus();
            txtFullName.setError("Please enter your full name");
            return;
        }

        if(dateOfBirth.isEmpty()){
            openDialog(this, "Please select your birthday");
            return;
        }

        if(phone.isEmpty()){
            txtPhone.requestFocus();
            txtPhone.setError("Please enter your phone");
            return;
        }

        if(email.isEmpty()){
            txtEmail.requestFocus();
            txtEmail.setError("Please enter your email");
            return;
        }

        if(address.isEmpty()){
            txtAddress.requestFocus();
            txtAddress.setError("Please enter your address");
            return;
        }

        synchronized (lock){
            if(avatar != null){
                UploadHandler uploadHandler = new UploadHandler(this);
                uploadHandler.execute(avatar);
            }
        }

        synchronized (lock){
            String uid = mAuth.getCurrentUser().getUid();
            user = new User(uid, fullName, email, avatarUrl, gender, phone, dateOfBirth, address);
            storeUser(user);
        }
    }


    private void storeUser(User user){

        mFirebaseDatabase.getReference()
                .child("User")
                .child(user.getId())
                .setValue(user);
        mFirebaseDatabase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(CompleteProfileActivity.this, "added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void openDialog(Activity context, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                context);
        dialog.setTitle("Message");
        dialog.setMessage(message);
        dialog.setPositiveButton("Ok",null);
        dialog.show();
    }

    private void backToHomeActivity() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_PIC_REQUEST) {
                Bundle bundle =  data.getExtras();
                if(bundle != null){
                    avatar = (Bitmap) bundle.get("data");
                }
                imgAvatar.setImageBitmap(avatar);
            }else if(requestCode == SELECT_FILE){
                Uri selectedImageUri = data.getData();
                try {
                    avatar = getBitmapFromUri(this,selectedImageUri);
                } catch (IOException e) {
                    avatar = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                }
                imgAvatar.setImageBitmap(avatar);
            }
        }
    }

    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor = activity
                .getContentResolver()
                .openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor ==
                null ? null : parcelFileDescriptor.getFileDescriptor();

        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        if(parcelFileDescriptor != null){
            parcelFileDescriptor.close();
        }

        return image;
    }

    private  void cameraIntent(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private  void galleryIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        activity.startActivityForResult(Intent.createChooser(intent, "Select avatar..."),SELECT_FILE);
    }

    private  void openDialogChooseImage(final Activity activity) {

        final CharSequence[] items = { "Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Add picture");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    cameraIntent(activity);
                } else if (items[i].equals("Gallery")) {
                    galleryIntent(activity);
                }
            }
        });

        builder.show();
    }

    class UploadHandler extends AsyncTask<Bitmap, Void, Void>{

        Activity context;
        ProgressDialog progressDialog;

        public UploadHandler(Activity context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            Calendar calendar = Calendar.getInstance();
            StorageReference mStorageReference = mFirebaseStorage
                    .getReference(FIREBASE_STORE_PATH + calendar.getTimeInMillis() + ".png");

            UploadTask uploadTask = mStorageReference.putBytes(data);


            uploadTask
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }})
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            avatarUrl = taskSnapshot.getDownloadUrl().toString();
                        }});

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

}