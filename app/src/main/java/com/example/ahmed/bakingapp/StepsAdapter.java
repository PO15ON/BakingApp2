package com.example.ahmed.bakingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmed.bakingapp.data.Step;

import java.util.ArrayList;

public class StepsAdapter extends BaseAdapter {
    Context context;
    ArrayList<Step> modelList;

    public StepsAdapter(Context context, ArrayList<Step> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
//        Log.d("count", "getCount: " + modelListAll.size());
        if (modelList == null) return 0;
        return modelList.size();
    }

    public void updateList(ArrayList<Step> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView;
        if (view == null) {
            textView = new TextView(context);
        } else {
            textView = (TextView) view;
        }

        String text = modelList.get(i).getShortDescription() + "\n...................";
        textView.setText(text);
        textView.setTextSize(18);
        return textView;
    }
}
