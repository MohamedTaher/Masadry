package com.example.root.newsapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.newsapp.R;
import com.example.root.newsapp.model.News;

import java.util.ArrayList;

/**
 * Created by root on 4/20/17.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder>  {

    private ArrayList<String> mDataset;
    private int layout;

    public CategoryListAdapter(ArrayList<String> mDataset, int layout) {
        this.mDataset = mDataset;
        this.layout = layout;
    }

    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        CategoryListAdapter.ViewHolder vh = new CategoryListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String temp = mDataset.get(position);
        if (mDataset.get(position).contains("-"))
            temp = mDataset.get(position).split("-")[1];

        holder.title.setText(temp);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.name);
            imageView = (ImageView) v.findViewById(R.id.flag);
            imageView.setVisibility(View.GONE);

        }
    }
}
