package com.example.root.newsapp.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.asha.nightowllib.NightOwl;
import com.example.root.newsapp.Date.connection.Connection;
import com.example.root.newsapp.helper.Utility;

import org.json.JSONException;

/**
 * Created by root on 4/11/17.
 */

public class MyApplication extends Application {

    private static Context context;



    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        Utility.doNightMode();
        NightOwl.builder().defaultMode(0).create();



    }

    public static Context getContext() {
        return context;
    }
}
