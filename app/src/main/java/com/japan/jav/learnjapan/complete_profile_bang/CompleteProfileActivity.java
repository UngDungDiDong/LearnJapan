package com.japan.jav.learnjapan.complete_profile_bang;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.base.BaseActivity;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.view.HomeActivity;
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


public class CompleteProfileActivity extends BaseActivity {
    // constants


    private static final String TAG = "CompleteProfileActivity";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int SELECT_FILE = 1338;
    private static final int RC_SIGN_IN = 123;
    private static final String FIREBASE_URL_STORE = "gs://learnjapan-4b5b5.appspot.com";
    private static final String FIREBASE_STORE_PATH = "avatar/";
    private static final Object lock = new Object();
    // views
    private TextInputEditText txtFullName, txtPhone, txtEmail, txtDateOfBirth, txtAddress;

    private Button btnUpdate, btnGetDate;

    private RadioButton rbMale, rbFemale;

    private ImageView imgAvatar;

    // temp available
    private String avatarUrl = "";

    private Bitmap avatar;

    private User user;


    // firebase available

    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseStorage mFirebaseStorage;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile_bang);


        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance(FIREBASE_URL_STORE);

        mAuth = FirebaseAuth.getInstance();

        addControls();

        addEvents();
    }

    private void addControls() {

        txtFullName = findViewById(R.id.txtFullName);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtDateOfBirth = findViewById(R.id.txtDateOfBirth);
        txtAddress = findViewById(R.id.txtAddress);

        RadioGroup rgGender = findViewById(R.id.rgGender);
        rbFemale = findViewById(R.id.rbFemale);
        rbMale = findViewById(R.id.rbMale);

        imgAvatar = findViewById(R.id.imgAvatar);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnGetDate = findViewById(R.id.btnGetDate);

        attachData();

    }

    private String getUserId() {

        return mAuth.getCurrentUser().getUid();
    }

    private void attachData() {
        String userId = getUserId();
        mFirebaseDatabase
                .getReference()
                .child("User")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        setDataIntoInput(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setDataIntoInput(User user) {
        try {

            txtFullName.setText(user.getUsername());

            txtDateOfBirth.setText(user.getDateOfBirth());

            if (user.isGender()) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }

            txtPhone.setText(user.getPhone());

            txtEmail.setText(user.getEmail());

            txtAddress.setText(user.getAddress());

            avatarUrl = user.getLinkPhoto() == null ? "" : user.getLinkPhoto();

            Picasso.with(this)
                    .load(user.getLinkPhoto())
                    .placeholder(R.drawable.noimage)
                    .into(imgAvatar);

        } catch (Exception e) {
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

    public static void getDate(Activity activity, final TextView txt) {

        final Calendar calendar = Calendar.getInstance();

        if (!txt.getText().toString().trim().isEmpty()) {

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

        if (avatarUrl.isEmpty() && avatar == null) {
            showMessage("Please select your avatar");
            return;
        }

        if (fullName.isEmpty()) {
            txtFullName.requestFocus();
            showMessage("Please enter your full name");
            return;
        }

        if (dateOfBirth.isEmpty()) {
            showMessage("Please select your birthday");
            return;
        }

        if (phone.isEmpty()) {
            txtPhone.requestFocus();
            showMessage("Please enter your phone");
            return;
        }

        if (email.isEmpty()) {
            txtEmail.requestFocus();
            showMessage("Please enter your email");
            return;
        }

        if (address.isEmpty()) {
            txtAddress.requestFocus();
            showMessage("Please enter your address");
            return;
        }
        upLoadAvatar();
        user = new User(getUserId(), fullName, email, avatarUrl, gender, phone, dateOfBirth, address);
        storeUser(user);
    }

    private void upLoadAvatar() {
       if(avatarUrl.length() == 0){
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           avatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
           byte[] data = baos.toByteArray();

           Calendar calendar = Calendar.getInstance();
           StorageReference mStorageReference = mFirebaseStorage
                   .getReference(FIREBASE_STORE_PATH + calendar.getTimeInMillis() + ".png");

           UploadTask uploadTask = mStorageReference.putBytes(data);

           showDialog();
           uploadTask
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {
                           dismissDialog();
                           showMessage("Upload failed");
                       }
                   })
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           avatarUrl = taskSnapshot.getDownloadUrl().toString();
                           mFirebaseDatabase.getReference()
                                   .child("User")
                                   .child(user.getId())
                                   .child("linkPhoto")
                                   .setValue(avatarUrl);
                           dismissDialog();
                           finish();
                       }
                   });
       }
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void storeUser(User user) {

        mFirebaseDatabase.getReference()
                .child("User")
                .child(user.getId())
                .setValue(user);
        mFirebaseDatabase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showMessage("Added");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showMessage("Failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    avatar = (Bitmap) bundle.get("data");
                }
                imgAvatar.setImageBitmap(avatar);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                try {
                    avatar = getBitmapFromUri(this, selectedImageUri);
                } catch (IOException e) {
                    avatar = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                }
                imgAvatar.setImageBitmap(avatar);
                avatarUrl = "";
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PIC_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraIntent(this);
                    }
                } else {
                    Toast.makeText(this, "Can't get camera because of permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            case SELECT_FILE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent(this);
                } else {
                    Toast.makeText(this, "Can't get location because of permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor = activity
                .getContentResolver()
                .openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor ==
                null ? null : parcelFileDescriptor.getFileDescriptor();

        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        if (parcelFileDescriptor != null) {
            parcelFileDescriptor.close();
        }

        return image;
    }

    private void cameraIntent(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void galleryIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        activity.startActivityForResult(Intent.createChooser(intent, "Select avatar..."), SELECT_FILE);
    }

    private void openDialogChooseImage(final Activity activity) {

        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Add picture");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    if (checkCamPermission())
                        cameraIntent(activity);
                } else if (items[i].equals("Gallery")) {
                    if (checkGaleryPermission())
                        galleryIntent(activity);
                }
            }
        });

        builder.show();
    }

    private Boolean checkCamPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PIC_REQUEST);
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkGaleryPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
            return false;
        } else {
            return true;
        }
    }
}