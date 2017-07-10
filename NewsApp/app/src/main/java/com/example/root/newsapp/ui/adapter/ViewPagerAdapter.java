package com.example.root.newsapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.helper.GetScreenSize;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.News;
import com.example.root.newsapp.ui.activity.NewsDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by root on 4/12/17.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private int layout;
    private Context context;
    private ArrayList<News> data;
    private int height,width;

    public ViewPagerAdapter(Context context, int layout, ArrayList<News> data) {
        this.context = context;
        this.layout = layout;
        this.data = data;

        GetScreenSize getScreenSize=new GetScreenSize(context);
        getScreenSize.getImageSize();
        height=getScreenSize.getHeight();
        width=getScreenSize.getWidth();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout, container, false);

        final News news = data.get(position);

        ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        TextView textView = (TextView) view.findViewById(R.id.textView);

        Picasso.with(context).load(news.getImage()).placeholder(R.drawable.defaultimage).resize(width,height/3).error(R.drawable.defaultimage).into(image);
        textView.setText(news.getTitle());
        container.addView(view);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<News> related = new ArrayList<News>();
                for (int i = 0; i < data.size(); i++){
                    if (i == 4)break;
                    related.add(data.get(i));
                }
                Collections.shuffle(related);

                Intent intent = NewsDetails.getIntent(MyApplication.getContext(), news, related);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        int sz;
        if (data != null) {
            sz = data.size();
            if (sz > 4)sz = 4;
        } else
            sz = 4;
        return sz;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;

        container.removeView(view);
    }
}
