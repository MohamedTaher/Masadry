package com.example.root.newsapp.ui.adapter;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.News;
import com.example.root.newsapp.ui.listener.ListenerHandler;
import com.example.root.newsapp.ui.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 4/9/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private HashMap<String, ArrayList<News>> mDataset;
    private ArrayList<String> categories;

    private static final int ITEM_ONE = 1;
    private static final int ITEM_ONE_TWO = 2;
    private static final int OTHER_ITEM = 3;

    private ListenerHandler listenerHandler;

    public NewsAdapter(HashMap<String,  ArrayList<News>> myDataset, ArrayList<String> categories, ListenerHandler listenerHandler) {
        mDataset = myDataset;
        this.listenerHandler= listenerHandler;
        this.categories = categories;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = viewType  == ITEM_ONE ? R.layout.news_item : viewType  == ITEM_ONE_TWO ? R.layout.news_item:R.layout.news_item;

            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.mTextView.setText(mDataset[position]);
        // Span the item if active

        final int pos = position;
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerHandler.doAction(pos);
            }
        });

        holder.myViewPagerAdapter = new ViewPagerAdapter(MyApplication.getContext()
                                                        , R.layout.slide_one
                                                        , mDataset.get(categories.get(position)));

        int sz = mDataset.get(categories.get(position)).size();
        if(sz > 4) sz = 4;
        holder.viewPager.setAdapter(holder.myViewPagerAdapter);
        holder.mTitle.setText(categories.get(position).split("-")[1]);
        holder.mPageNumber.setText("< " + sz + " / " + 1 + " >");
        holder.data = mDataset.get(categories.get(position));
        holder.addBottomDots(0);



        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
            sglp.setFullSpan(/*position == 1 || position == 2 ? false : */true);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle, mPageNumber;
        public TextView[] dots = new TextView[4];
        public ViewPager viewPager;
        public ViewPagerAdapter myViewPagerAdapter;
        public LinearLayout container;
        public ArrayList<News> data;
        public LinearLayout dotsLayout;

        //  viewpager change listener
        public ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mPageNumber.setText("< " + (data.size() < 4 ? data.size() : 4 ) + " / " + (position+1) + " >");
                addBottomDots(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };


        public ViewHolder(View v) {
            super(v);

            viewPager = (ViewPager) v.findViewById(R.id.view_pager);
            /*
            int[] layouts = new int[]{
                    R.layout.slide_one,
                    R.layout.slide_one,
                    R.layout.slide_one,
                    R.layout.slide_one};*/

            // data is a element at mdataset 2d array

            data = new ArrayList<>();
            data.add(null);
            data.add(null);
            data.add(null);
            data.add(null);

            myViewPagerAdapter = new ViewPagerAdapter(MyApplication.getContext(), R.layout.slide_one,data);
            viewPager.setAdapter(myViewPagerAdapter);
            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

            mTitle = (TextView) v.findViewById(R.id.title);
            mPageNumber  = (TextView) v.findViewById(R.id.number);
            mPageNumber.setText("< " + 4 + " / " + 1 + " >");

            container = (LinearLayout) v.findViewById(R.id.content);
            dotsLayout = (LinearLayout) v.findViewById(R.id.dots_item);
            addBottomDots(0);



        }

        public void addBottomDots(int currentPage) {

            int[] colorsActive = MyApplication.getContext().getResources().getIntArray(R.array.array_dot_active);
            int[] colorsInactive = MyApplication.getContext().getResources().getIntArray(R.array.array_dot_inactive);

            dotsLayout.removeAllViews();
            for (int i = 0; i < (data.size() < 4 ? data.size() : 4)  ; i++) {
                dots[i] = new TextView(MyApplication.getContext());
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorsInactive[3]);
                dotsLayout.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(colorsActive[3]);
        }
    }



}
