package edu.ptit.kalis.customlistviewusesqlitefirebase.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import edu.ptit.kalis.customlistviewusesqlitefirebase.R;
import edu.ptit.kalis.customlistviewusesqlitefirebase.model.FakeObject;

/**
 * Created by Kalis on 04/09/2018.
 */

public class MyAdapter extends ArrayAdapter<FakeObject> {
    Activity activity;
    int layout;
    List<FakeObject> objects;

    public MyAdapter(@NonNull Activity activity, int layout, @NonNull List<FakeObject> objects) {
        super(activity, layout, objects);
        this.activity = activity;
        this.layout = layout;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FakeObject obj = getItem(position);
        if(objects.size()>0 && position>=0)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            final TextView txtName= convertView.findViewById(R.id.txtName);
            final TextView txtOwner = convertView.findViewById(R.id.txtOwner);
            final RatingBar ratingBar = convertView.findViewById(R.id.rating);
            final ImageView imgIcon = convertView.findViewById(R.id.imgIcon);

            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(activity.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(activity.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(activity.getResources().getColor(android.R.color.background_light), PorterDuff.Mode.SRC_ATOP);

            Bitmap bm = BitmapFactory.decodeByteArray(obj.getImgBytes(), 0 ,obj.getImgBytes().length);
            imgIcon.setImageBitmap(bm);

            txtName.setText(obj.getName());
            txtOwner.setText(obj.getOwner());
            ratingBar.setRating(obj.getRating());
            DrawableCompat.setTint(ratingBar.getProgressDrawable(), Color.GRAY);
        }

        return convertView;
    }
}
