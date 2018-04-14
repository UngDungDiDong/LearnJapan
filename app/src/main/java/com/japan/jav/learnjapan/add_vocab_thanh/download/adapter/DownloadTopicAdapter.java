package com.japan.jav.learnjapan.add_vocab_thanh.download.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japan.jav.learnjapan.R;

import java.util.List;

/**
 * Created by thanh on 4/13/2018.
 */

public class DownloadTopicAdapter extends RecyclerView.Adapter<DownloadTopicAdapter.TopicHolder> {
    private Context mContext;
    private List<String> mList;

    public DownloadTopicAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public DownloadTopicAdapter.TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_tam, parent, false);
        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadTopicAdapter.TopicHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class TopicHolder extends RecyclerView.ViewHolder {
        public TopicHolder(View itemView) {
            super(itemView);


        }
    }
}

