package com.japan.jav.learnjapan.download_nguyen.list.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
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

public class DownloadTopicAdapter extends RecyclerView.Adapter<DownloadTopicAdapter.TopicHolder> {
    private Context mContext;
    private List<String> mList;

    public DownloadTopicAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public DownloadTopicAdapter.TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_thanh, parent, false);
        return new TopicHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadTopicAdapter.TopicHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class TopicHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_Topic)
        CardView cvTopic;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public TopicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cvTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick();
                }
            });
        }

        public void bind(int pos) {
            tvTitle.setText(mList.get(pos));
        }
    }

    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener callBack){
        this.listener = callBack;
    }
    public interface OnItemClickListener{
        void onClick();
    }
}

