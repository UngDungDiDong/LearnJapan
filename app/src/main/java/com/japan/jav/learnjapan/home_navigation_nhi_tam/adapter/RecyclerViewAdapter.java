package com.japan.jav.learnjapan.home_navigation_nhi_tam.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.BuilderManager;
import com.japan.jav.learnjapan.model.DataTypeEnum;
import com.japan.jav.learnjapan.model.Set;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;


/**
 * Created by tamlv on 3/29/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Set> list;
    private OnBoomMenuItemClicked mListener;
    private String FRAGMENT_TAG;

    public RecyclerViewAdapter(String FRAGMENT_TAG, ArrayList<Set> list) {
        this.FRAGMENT_TAG = FRAGMENT_TAG;
        this.list = list;
    }

    public void setOnBoomMenuItemClick(OnBoomMenuItemClicked mListener) {
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_tam, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.txtName.setText(list.get(position).getName());
        viewHolder.txtDatetime.setText(list.get(position).getDatetime());
        viewHolder.set = this.list.get(position);
        viewHolder.bmb.clearBuilders();

        if(this.FRAGMENT_TAG == "KANJI"){
            viewHolder.dataType = DataTypeEnum.KANJI;
        }else{
            viewHolder.dataType = DataTypeEnum.MOJI;
        }

        int stringIndex = 0;
        for (int i = 0; i < viewHolder.bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            switch (i) {
                case 0:
                    stringIndex = R.string.text_outside_button_buider_learn;
                    break;
                case 1:
                    stringIndex = R.string.text_outside_button_buider_test;
                    break;
                case 2:
                    stringIndex = R.string.text_outside_button_buider_chart;
                    break;
                case 3:
                    stringIndex = R.string.text_outside_button_buider_edit;
                    break;
                case 4:
                    stringIndex = R.string.text_outside_button_buider_delete;
                    break;
            }
            addBuilder(viewHolder, stringIndex, position);
        }

    }

    void addBuilder(final ItemViewHolder viewHolder, int stringIndex, final int position) {
        viewHolder.bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(BuilderManager.getImageResource())
                .normalTextRes(stringIndex)
                .textSize(14)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        mListener.OnMenuItemClicked(index, viewHolder.dataType, viewHolder.set, position);
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtDatetime;
        BoomMenuButton bmb;
        CardView cardView;
        Set set;
        DataTypeEnum dataType;

        public ItemViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txt_name_tam);
            txtDatetime = (TextView) itemView.findViewById(R.id.txt_datetime_tam);
            bmb = (BoomMenuButton) itemView.findViewById(R.id.bmb_tam);
            cardView = (CardView) itemView.findViewById(R.id.card_view_tam);
        }
    }
    public interface OnBoomMenuItemClicked {
        void OnMenuItemClicked(int classIndex, DataTypeEnum dataTypeEnum, Set set, int position);
    }
}

