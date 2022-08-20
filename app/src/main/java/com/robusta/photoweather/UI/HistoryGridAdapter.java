package com.robusta.photoweather.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.robusta.Other.Config;
import com.robusta.Other.FullScreenImage;
import com.robusta.photoweather.R;

import java.util.ArrayList;
import java.util.Set;

public class HistoryGridAdapter extends BaseAdapter {

    private final HomeScreen mContext;
    private final ArrayList<String> historyList;

    public HistoryGridAdapter(HomeScreen mContext, Set<String> historyList) {
        this.mContext = mContext;
        this.historyList = new ArrayList<>(historyList);

    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        grid = inflater.inflate(R.layout.home_grid_single_item, null);

        TextView shareButton = grid.findViewById(R.id.home_grid_share_btn);
        shareButton.setOnClickListener(view1 -> Config.ShareImage(mContext, Config.decodeStringToBitmap(historyList.get(i))));

        ImageView brandImage = grid.findViewById(R.id.home_grid_history_image);
        brandImage.setImageBitmap(Config.decodeStringToBitmap(historyList.get(i)));
        brandImage.setOnClickListener(view12 -> {
            FullScreenImage fullScreenImage = new FullScreenImage(Config.decodeStringToBitmap(historyList.get(i)), mContext);
            fullScreenImage.show();
        });

        return grid;
    }

}