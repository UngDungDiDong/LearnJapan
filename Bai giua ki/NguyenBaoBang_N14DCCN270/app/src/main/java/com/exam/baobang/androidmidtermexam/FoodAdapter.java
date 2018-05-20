package com.exam.baobang.androidmidtermexam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {
    private ArrayList<Food> mFoods;
    private NumberFormat mNumberFormat;
    private Currency  mCurrency;
    private Locale mLocale;
    Context mContext;

    public FoodAdapter(Context context, ArrayList<Food> mFoods) {
        this.mFoods = mFoods;
        mLocale = new Locale("vi", "VN");
        mNumberFormat = NumberFormat.getNumberInstance(mLocale);
        mCurrency = Currency.getInstance(mLocale);
        this.mContext = context;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item, parent, false);

        return new FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        holder.onBindView(mFoods.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoods == null ? 0 : mFoods.size();
    }

    public class FoodHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txtName, txtDescription, txtPrice;
        ConstraintLayout container;

        public FoodHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            container = itemView.findViewById(R.id.container);
        }

        public void onBindView(Food food) {
            txtName.setText(food.getName());
            txtDescription.setText(food.getDescription());
            txtPrice.setText(new StringBuilder(mNumberFormat.format(food.getPrice()) + mCurrency.getSymbol()));
            Picasso.get()
                    .load(food.getImageUrl())
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.no_image)
                    .into(img);
                    container.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
                            databaseReference.child(mFoods.get(getAdapterPosition()).getId()).removeValue();
                            Toast.makeText(mContext, "Delete success", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
        }
    }
}
