package com.example.root.newsapp.ui.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.helper.GetScreenSize;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.News;
import com.example.root.newsapp.ui.listener.ListenerHandler;
import com.example.root.newsapp.ui.listener.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 4/9/17.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private ArrayList<News> mDataset;

    private static final int ITEM_ONE = 1;
    private static final int ITEM_ONE_TWO = 2;
    private static final int OTHER_ITEM = 3;

    private int height, width;

    private ListenerHandler listenerHandler;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Boolean isLoading=false;

    public NewsListAdapter(ArrayList<News> data, ListenerHandler listenerHandler) {
        GetScreenSize getScreenSize=new GetScreenSize(MyApplication.getContext());
        getScreenSize.getImageSize();
        height=getScreenSize.getHeight();
        width=getScreenSize.getWidth();

        mDataset = data;
        this.listenerHandler= listenerHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType  == ITEM_ONE ? R.layout.news_item_row : viewType  == ITEM_ONE_TWO ? R.layout.news_item_row:R.layout.news_item_row;

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.mTextView.setText(mDataset[position]);

        final News temp = mDataset.get(position);

        if (temp.getPosttype().equals("ad")){
            holder.title.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
            holder.sorce.setVisibility(View.GONE);

            Picasso.with(MyApplication.getContext())
                    .load(temp.getImage())
                    .placeholder(R.drawable.defaultimage)
                    .resize(width,height)
                    .centerInside()
                    .into(holder.imageView);

        } else {
            holder.title.setText(temp.getTitle());
            holder.date.setText(temp.getDate());
            holder.sorce.setText(temp.getAuthor());

            Picasso.with(MyApplication.getContext())
                    .load(temp.getImage())
                    .resize(width/4,height/6)
                    .centerCrop()
                    .error(MyApplication.getContext().getDrawable(R.drawable.defaultimage))
                    .into(holder.imageView);
        }

        // Span the item if active
        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
            sglp.setFullSpan(position == 1 || position == 2 ? true : true);
            holder.itemView.setLayoutParams(sglp);
        }
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_ONE;
        } else if (position == 1 || position == 2) {
            return ITEM_ONE_TWO;
        } else {
            return OTHER_ITEM;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView date, sorce, title;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
            sorce = (TextView) v.findViewById(R.id.source);
            imageView = (ImageView) v.findViewById(R.id.news_pic);

        }
    }



}
