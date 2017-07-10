package com.example.root.newsapp.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.ui.activity.NewsDetails;
import com.example.root.newsapp.ui.adapter.CategoryListAdapter;
import com.example.root.newsapp.ui.listener.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by root on 4/19/17.
 */

public class Topic extends Fragment {

    private ArrayList<String> data;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public Topic() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Bundle getBundle (ArrayList<String> data) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("DATA", data);
        return bundle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.topic, container, false);

        data = getArguments().getStringArrayList("DATA");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.topics_list);
        mLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CategoryListAdapter(data, R.layout.dialog_row);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MyApplication.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                })
        );

        return v;
    }

}