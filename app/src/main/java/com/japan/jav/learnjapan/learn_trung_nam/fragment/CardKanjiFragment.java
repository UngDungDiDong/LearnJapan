package com.japan.jav.learnjapan.learn_trung_nam.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.service.Constants;
import com.japan.jav.learnjapan.service.DatabaseService;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trungnguyeen on 1/14/18.
 */

public class CardKanjiFragment extends Fragment {

    private DatabaseService mData = DatabaseService.getInstance();
    private Context context;
    private Kanji mKanji;
    private CardView mFontCardView;
    private CardView mBackCardView;
    private EasyFlipView mEasyFlipView;

    private TextView tvKanji;
    private TextView tvAmHan;
    private TextView tvTuVung;
    private ImageView btnCamera;
    private ImageView imgKanji;
    private float mCardElevationValue;
    private AlertDialog dialog;
    private String setId;

    public static final int REQUEST_CAMERA = 10;
    public static final int REQUEST_GALLERY = 11;
    private Uri filePath = null;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    String mCurrentPhotoPath;

    private final static String TAG = CardKanjiFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_kanji_item, container, false);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        initControls(view);
        setEvents();
        return view;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setItem(Kanji mKanji) {
        this.mKanji = mKanji;
    }


    public void initControls(View view) {
        mEasyFlipView = view.findViewById(R.id.flip_view);
        mFontCardView = view.findViewById(R.id.font_card_view);
        mBackCardView = view.findViewById(R.id.back_card_view);

        mCardElevationValue = mFontCardView.getCardElevation();

        tvKanji = view.findViewById(R.id.tv_kanji);
        tvAmHan = view.findViewById(R.id.tv_am_han);
        tvTuVung = view.findViewById(R.id.tv_tu_vung);

        btnCamera = view.findViewById(R.id.btnCamera);

        imgKanji = view.findViewById(R.id.img_kanji);
        if (!TextUtils.isEmpty(mKanji.getPhoto())){
            Picasso.with(this.context)
                    .load(mKanji.getPhoto())
                    .into(imgKanji);
            Log.e(TAG, "initControls: " + mKanji.getPhoto());
            imgKanji.setVisibility(View.VISIBLE);
        }

        tvKanji.setText(this.mKanji.getKanji());
        tvAmHan.setText(this.mKanji.getAmhan());
        tvTuVung.setText(this.mKanji.getTuvung());

    }

    public void setEvents() {

        mBackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animRemoveElevationAndFlip();
            }
        });

        mFontCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animRemoveElevationAndFlip();
            }
        });

        mEasyFlipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                mBackCardView.setCardElevation(mCardElevationValue);
                mFontCardView.setCardElevation(mCardElevationValue);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseOption();
            }
        });
    }

    public void animRemoveElevationAndFlip() {
        mFontCardView.setCardElevation(0f);
        mBackCardView.setCardElevation(0f);
        mEasyFlipView.flipTheView();
    }

    public void showDialogChooseOption() {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        final View dialogView = layoutInflater.inflate(R.layout.dialog_choose_option, null);
        final TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        final TextView txtCamera = dialogView.findViewById(R.id.camera);
        final TextView txtGallery = dialogView.findViewById(R.id.gallery);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        builder.setView(dialogView);
        final Boolean result = ContextCompat.checkSelfPermission(this.context,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;

        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: choose camera");
                if (isCheckCameraPermission() && isCheckGalleryPermission()) {
                    cameraIntent();
                } else {
                }
            }
        });

        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"onClick: choose gallery");
                if (isCheckGalleryPermission())
                    galleryIntent();
            }
        });

        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private Boolean isCheckCameraPermission() {
        if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return false;
        } else {
            return true;
        }
    }

    private Boolean isCheckGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (dialog != null) {
            dialog.cancel();
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            filePath = data.getData();
            try {
                bm = MediaStore.Images.Media
                        .getBitmap(getActivity().getContentResolver(),
                                data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgKanji.setImageBitmap(rotateImage(bm, 180));
        imgKanji.setVisibility(View.VISIBLE);
        updateImage();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        File destination = null;
        try {
            destination = createImageFile();
            filePath = Uri.fromFile(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(destination);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, fo);
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgKanji.setImageBitmap(thumbnail);
        imgKanji.setVisibility(View.VISIBLE);
        updateImage();
    }

    private void updateImage() {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this.context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //TODO xoa anh cu
            if (!TextUtils.isEmpty(mKanji.getPhoto())){
                StorageReference deletePhotoRef = firebaseStorage.getReferenceFromUrl(mKanji.getPhoto())  ;;
                deletePhotoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: deleted file");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "onFailure: did not delete file");
                    }
                });
            }

            //TODO them anh moi vao storage firebase vao setbyuser
            StorageReference riversRef = storageReference.child("images/" + filePath.getLastPathSegment());
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.e("TAG", "onSuccess: " + downloadUrl.toString());

                            //import image to SetByUser
                            FirebaseDatabase.getInstance()
                                    .getReference(Constants.SET_BY_USER)
                                    .child(mData.getUserID())
                                    .child(setId)
                                    .child(mKanji.getId())
                                    .child("photo")
                                    .setValue(downloadUrl.toString());

                            mKanji.setPhoto(downloadUrl.toString());


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.e("TAG", "onFailure: " + e.getMessage());
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraIntent();
                    }
                } else {
                    Toast.makeText(this.context, "Can't get camera because of permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                    Toast.makeText(this.context, "Can't get location because of permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);
    }


    public static Bitmap realBitmap(Bitmap bitmap, String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
