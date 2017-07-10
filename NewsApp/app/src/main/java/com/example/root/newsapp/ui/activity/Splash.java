package com.example.root.newsapp.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.root.newsapp.Date.Constants;
import com.example.root.newsapp.Date.connection.Connection;
import com.example.root.newsapp.Date.connection.ConnectionDetector;
import com.example.root.newsapp.R;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.Country;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Splash extends AppCompatActivity {

    private Connection connection;
    private ConnectionDetector connectionDetector;
    private int language = Constants.LANGUAGE_ENGLISH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);

        getData();
    }

    private void getData(){
        connectionDetector=new ConnectionDetector(this);
        if(connectionDetector.isConnectingToInternet()) {
            connection = new Connection(this, "/GetAllCounteries/" + language, "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("1-countries");

                    for(int i = 0; i < jsonArray.length(); i++){
                        Gson gson = new GsonBuilder().create();
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Country country = gson.fromJson(jo.toString(), Country.class);
                        Constants.addCountry(country);
                    }

                    Intent mainIntent = new Intent(Splash.this,Home.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            });

        }
        else {
            Utility.connectionToast(this);
        }
    }
}
