package com.example.root.newsapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.newsapp.Date.Constants;
import com.example.root.newsapp.Date.connection.Connection;
import com.example.root.newsapp.Date.connection.ConnectionDetector;
import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.Country;
import com.example.root.newsapp.model.News;
import com.example.root.newsapp.ui.adapter.NewsListAdapter;
import com.example.root.newsapp.ui.listener.ListenerHandler;
import com.example.root.newsapp.ui.listener.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NewsDetails extends AppCompatActivity {

    private static final String DATA_KEY = "NEWS";
    private static final String RELATED_KEY = "RELATED";

    private TextView date;
    private WebView content;
    private AppBarLayout appBarLayout;
    private Connection connection;
    private ConnectionDetector connectionDetector;
    private ArrayList<News> related;
    private int language = Constants.LANGUAGE_ENGLISH;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private News news;

    public static Intent getIntent(Context context, News news, ArrayList<News> related) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_KEY, news);
        bundle.putSerializable(RELATED_KEY,related);
        Intent intent= new Intent(context,NewsDetails.class);
        intent.putExtras(bundle);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        news = (News) bundle.getSerializable(DATA_KEY);
        related = (ArrayList<News>) bundle.getSerializable(RELATED_KEY);
        setTitle(news.getTitle() + "");

        connectionDetector=new ConnectionDetector(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Hello");
                sendIntent.setType("text/plain");
                Intent.createChooser(sendIntent,"Share via");
                startActivity(sendIntent);
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        getData();

        Picasso.with(this)
                .load(news.getImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        appBarLayout.setBackground(drawable);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


    }

    private void init() {
        setTitle(news.getTitle() + "");
        content = (WebView) findViewById(R.id.content);
        date = (TextView) findViewById(R.id.date);

        date.setText(news.getDate() + "");


        content.loadDataWithBaseURL("", "<html dir=\"rtl\" lang=\"\"><body>" + news.getPostContent() + "</body></html>", "text/html", "UTF-8",null);
        content.getSettings().setJavaScriptEnabled(true);

        Utility.resizeFont(date);
        setRelated ();
    }

    private void setRelated (){
        mRecyclerView = (RecyclerView) findViewById(R.id.related_news);
        mRecyclerView.setHasFixedSize(true);

        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        ArrayList<News> _related = new ArrayList<News>(related);
                        _related.remove(position);
                        _related.add(news);
                        Collections.shuffle(_related);

                        Intent intent = NewsDetails.getIntent(MyApplication.getContext(), related.get(position), _related);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
        );


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new NewsListAdapter(related, new ListenerHandler() {
            @Override
            public void doAction(int position) {

            }
        });


        mRecyclerView.setAdapter(mAdapter);
        //list end
    }

    private void getData() {
        if(connectionDetector.isConnectingToInternet()) {
            connection = new Connection(this, "/GetAllCountryPostData/" + "313" + "/" + news.getID() + "/" + language, "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("postdata");

                    for(int i = 0; i < jsonArray.length(); i++){
                        Gson gson = new GsonBuilder().create();
                        JSONObject jo = jsonArray.getJSONObject(i);
                        news = gson.fromJson(jo.toString(), News.class);
                     }

                    init();

                }
            });

        }
        else {
            Utility.connectionToast(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

}
