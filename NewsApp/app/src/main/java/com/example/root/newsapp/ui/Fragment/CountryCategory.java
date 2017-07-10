package com.example.root.newsapp.ui.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.newsapp.Date.Constants;
import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.ui.adapter.CategoryListAdapter;
import com.example.root.newsapp.ui.listener.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by root on 4/19/17.
 */

public class CountryCategory extends Fragment {

    private ArrayList<String> data;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public CountryCategory() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.country, container, false);

        data = new ArrayList<>();

        for (int i = 0; i < Constants.getCountries().size(); i++) {
            data.add(Constants.getCountries().get(i).getCountrylatname());
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.countries_list);
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