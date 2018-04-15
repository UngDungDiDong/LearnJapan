package com.japan.jav.learnjapan.download_nguyen.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.japan.jav.learnjapan.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thanh on 4/13/2018.
 */

public class DetailTopicAdapter extends RecyclerView.Adapter<DetailTopicAdapter.TopicHolder> {
    private Context mContext;
    private List<String> mList;

    public DetailTopicAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public DetailTopicAdapter.TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kanji_thanh, parent, false);
        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailTopicAdapter.TopicHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class TopicHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_kanji)
        TextView tvKanji;

        @BindView(R.id.tv_amhan)
        TextView tvAmHan;

        @BindView(R.id.tv_tuvung)
        TextView tvTuVung;

        public TopicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int pos) {
            tvKanji.setText(mList.get(pos));
        }
    }
}

