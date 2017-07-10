package com.example.root.newsapp.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.root.newsapp.Date.connection.Connection;
import com.example.root.newsapp.Date.connection.ConnectionDetector;
import com.example.root.newsapp.R;
import com.example.root.newsapp.app.MyApplication;
import com.example.root.newsapp.helper.GetScreenSize;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.News;
import com.example.root.newsapp.ui.adapter.NewsListAdapter;
import com.example.root.newsapp.ui.adapter.ViewPagerAdapter;
import com.example.root.newsapp.ui.listener.ListenerHandler;
import com.example.root.newsapp.ui.listener.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;



public class NewsList extends AppCompatActivity {

    ///GetAllPosts/countyid/categoryid/startpage/newscount/language
    private ArrayList<News> data = new ArrayList<>();
    private int font_sz = 0,width,height, startpage = 0;
    private final int newscount = 10;
    private String url, countyid, category, language, categoryId;
    private Dialog dialog;
    private boolean loading = true;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ScrollView contentView;
    private ViewPager viewPager;
    private ViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int newsAtSlider = 0;
    //private int[] layouts;

    private Handler handler;
    private Runnable update;

    private Connection connection;
    private ConnectionDetector connectionDetector;

    //  viewpager change listener
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == newsAtSlider - 1) {
                // last page. make button text to GOT IT
                //btnNext.setText(getString(R.string.start));
                //btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                //btnNext.setText(getString(R.string.next));
                //btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    public static Intent getIntent(Context context, String url, String countyid, String category, String language) {
        Intent intent = new Intent(context, NewsList.class);
        intent.putExtra("url", url);
        intent.putExtra("countyid", countyid);
        intent.putExtra("category", category);
        intent.putExtra("language", language);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        //overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        GetScreenSize getScreenSize=new GetScreenSize(this);
        getScreenSize.getImageSize();
        height=getScreenSize.getHeight();
        width=getScreenSize.getWidth();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
    }

    private void init() {

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        countyid = intent.getStringExtra("countyid");
        category = intent.getStringExtra("category");
        language = intent.getStringExtra("language");

        String[] temp = category.split("-");
        categoryId = temp[0];

        setTitle(temp[1]);

        //load first set of data
        getData();

        contentView = (ScrollView) findViewById(R.id.content_home);

        //slider_start
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        contentView.requestChildFocus(viewPager,viewPager);

        // adding bottom dots
        addBottomDots(0);

        //slider_end


        //recycle_view_start
        mRecyclerView = (RecyclerView) findViewById(R.id.news_list);

        //recycle_view_end

        //set font size
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getString("font_size","").equals(""))
            font_sz = Integer.parseInt(prefs.getString("font_size",""));
        Log.e("FOUNT_SIZE : " , prefs + "");

    }

    private void getData(){

        connectionDetector=new ConnectionDetector(this);
        if(connectionDetector.isConnectingToInternet()) {
            connection = new Connection(this, "/GetAllPosts/" + countyid + "/" + categoryId + "/" + startpage + "/" + newscount + "/" + language , "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);
                    startpage++;
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray(category);

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

    private void setData() {

        //slider start
        ArrayList<News> _data = new ArrayList<>();
        for (int i = 0; i < data.size(); i++){
            if (i == 4)break;
            _data.add(data.get(i));
        }
        Collections.shuffle(_data);

        newsAtSlider = _data.size();
        myViewPagerAdapter = new ViewPagerAdapter(this, R.layout.slide_one,_data);

        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        viewPager.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams=viewPager.getLayoutParams();
                layoutParams.height=height/3;
                viewPager.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams1.addRule(RelativeLayout.BELOW,R.id.view_pager);
                layoutParams1.height=height/17;
                dotsLayout.setLayoutParams(layoutParams1);
                dotsLayout.setVisibility(View.GONE);
            }
        });

        handler = new Handler();

        update = new Runnable() {
            public void run() {
                int currentPage = viewPager.getCurrentItem();
                if (currentPage == newsAtSlider-1) {
                    currentPage = 0;
                    viewPager.setCurrentItem(currentPage, true);
                } else {
                    viewPager.setCurrentItem(++currentPage, true);
                }
            }
        };


        new Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.e("view_pager","switch");
                handler.post(update);
            }
        }, 3000, 1500);

        dotsLayout.setVisibility(View.VISIBLE);

        //slider_end

        //list start
        mRecyclerView.setHasFixedSize(true);

        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);

        final Context context = getApplicationContext();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (data.get(position).getPosttype().equals("ad")){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getPostlink()));
                            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(browserIntent);

                        } else {
                            ArrayList<News> related = new ArrayList<>();
                            for (int i = 0; i < data.size(); i++){
                                if (i == 4)break;
                                related.add(data.get(i));
                            }
                            Collections.shuffle(related);

                            Intent intent = NewsDetails.getIntent(MyApplication.getContext(), data.get(position), related);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                })
        );


        mLayoutManager = new LinearLayoutManager(this);
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
                            //Do pagination.. i.e. fetch new data
                            getData();

                        }


                    }

                }
            }
        });

        contentView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                Log.e("TRACE", i + " " + i1 + " " + i2 + " " + i3 + " " + contentView.getMaxScrollAmount()  );
                View _view = (View) contentView.getChildAt(contentView.getChildCount()-1);
                int diff = (_view.getBottom()-(contentView.getHeight()+ contentView.getScrollY()));
                if (diff == 0) {
                    Log.e("ENNNNNNNNNNNNNNNNNNND", "-_-");
                    getData();
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

    private void addBottomDots(int currentPage) {
        dots = new TextView[newsAtSlider];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[1]);
            dots[i].setVisibility(View.GONE);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[1]);
        dotsLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

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
