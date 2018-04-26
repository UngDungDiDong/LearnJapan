package com.japan.jav.learnjapan.test_feature_khang_duc.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.test_feature_khang_duc.view.model.ReviewItem;

import java.util.ArrayList;

public class ListReviewAdapter extends RecyclerView.Adapter<ListReviewAdapter.TestReviewHolder> {

    private ArrayList<ReviewItem> listReview;
    private Context context;

    public void setListReview(ArrayList<ReviewItem> listReview) {
        this.listReview = listReview;
    }

    public ListReviewAdapter(ArrayList<ReviewItem> listReview, Context context) {
        this.listReview = listReview;
        this.context = context;
    }

    @Override
    public TestReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View itemView = layoutInflater.inflate(R.layout.result_item, parent, false);
        return new TestReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TestReviewHolder holder, int position) {
        ReviewItem item = listReview.get(position);
        holder.bindView(item);
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    public class TestReviewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCheck;
        private TextView tvQuestion;
        private TextView tvAnswer;
        private TextView tvUserAnswer;

        public TestReviewHolder(View itemView) {
            super(itemView);
            ivCheck = itemView.findViewById(R.id.ivCheck);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvYourAnswer);
            tvUserAnswer = itemView.findViewById(R.id.tvAnswer);
        }

        public void bindView (ReviewItem item){
            if (item.getUserAnswer().equalsIgnoreCase(item.getAnswer())){
                ivCheck.setImageResource(R.drawable.ic_action_name);
            }else if (!item.getUserAnswer().equalsIgnoreCase(item.getAnswer())){
                ivCheck.setImageResource(R.drawable.wrong_review);
            }
            tvQuestion.setText(item.getQuestion());
            tvUserAnswer.setText(item.getUserAnswer());
            tvAnswer.setText(item.getAnswer());
        }
    }
}
