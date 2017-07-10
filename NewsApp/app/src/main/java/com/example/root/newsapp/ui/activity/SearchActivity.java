package com.example.root.newsapp.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.newsapp.Date.connection.Connection;
import com.example.root.newsapp.Date.connection.ConnectionDetector;
import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.News;
import com.example.root.newsapp.ui.adapter.NewsListAdapter;
import com.example.root.newsapp.ui.listener.ListenerHandler;
import com.example.root.newsapp.ui.listener.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private TextView noResult;
    private Dialog dialog;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private Connection connection;
    private ConnectionDetector connectionDetector;

    private int font_sz = 0,width,height, startpage = 0;
    private final int newscount = 10;
    private String url, countyid, language, queue = "";
    private boolean loading = true;

    private ArrayList<News> data = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle(getString(R.string.search));

        connectionDetector=new ConnectionDetector(this);
        init();
    }

    public static Intent getIntent(Context context, String url, String countyid, String language) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("countyid", countyid);
        intent.putExtra("language", language);
        return intent;
    }

    public void init() {

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        countyid = intent.getStringExtra("countyid");
        language = intent.getStringExtra("language");

        mRecyclerView = (RecyclerView) findViewById(R.id.search_list);
        noResult = (TextView) findViewById(R.id.no_result);
        noResult.setVisibility(View.INVISIBLE);
    }

    private void searchAndSetResult (String text) {

        noResult.setVisibility(View.INVISIBLE);

        connectionDetector=new ConnectionDetector(this);
        if(connectionDetector.isConnectingToInternet()) {
            connection = new Connection(this, "/GetAllPostsBySearch/" + countyid + "/" + text + "/" + newscount + "/" + language , "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);
                    startpage++;

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("1-PostsBySearch");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Gson gson = new GsonBuilder().create();
                        News news = gson.fromJson(jo.toString(), News.class);
                        data.add(news);
                    }

                    setData();

                }
            });

        }
        else {
            Utility.connectionToast(this);
        }
    }

    private void setData(){
        if (data.size() > 0) noResult.setVisibility(View.INVISIBLE);
        else noResult.setVisibility(View.VISIBLE);
        //list start
        mRecyclerView.setHasFixedSize(true);

        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        ArrayList<News> related = new ArrayList<News>();
                        for (int i = 0; i < data.size(); i++) {
                            if (i == 4)break;
                            related.add(data.get(i));
                        }

                        Collections.shuffle(related);


                        Intent intent = NewsDetails.getIntent(MyApplication.getContext(), data.get(position), related);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
        );


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()  {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            searchAndSetResult(queue);
                        }


                    }

                }
            }
        });


        mAdapter = new NewsListAdapter(data, new ListenerHandler() {
            @Override
            public void doAction(int position) {

            }
        });


        mRecyclerView.setAdapter(mAdapter);
        //list end
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //noResult.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        data = new ArrayList<>();
        if(query != null || !query.equals("")) {

            this.queue = query;
            data = new ArrayList<>();
            searchAndSetResult(query);


            return true;
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //noResult.setVisibility(View.INVISIBLE);
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                dialog = new Dialog(this, R.style.CircularProgress);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_loading_item);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                LinearLayout relativeLayout;
                relativeLayout=(LinearLayout) dialog.findViewById(R.id.rel_loder);
                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // The user just touched the screen
                                //   Toast.makeText(SplashActivity.this,"ended", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                break;
                            case MotionEvent.ACTION_UP:
                                // The touch just ended
                                // Toast.makeText(SplashActivity.this,"ended", Toast.LENGTH_SHORT).show();

                                break;
                        }

                        return false;
                    }
                });


                return dialog;
            default:
                return null;
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            System.out.println("TOuch outside the dialog ******************** ");
            dialog.dismiss();
        }
        return false;
    }

}
