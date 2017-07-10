package com.example.root.newsapp.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaFormat;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.newsapp.Date.Constants;
import com.example.root.newsapp.Date.connection.Connection;
import com.example.root.newsapp.Date.connection.ConnectionDetector;
import com.example.root.newsapp.helper.GetScreenSize;
import com.example.root.newsapp.helper.Utility;
import com.example.root.newsapp.model.Country;
import com.example.root.newsapp.model.News;
import com.example.root.newsapp.ui.Fragment.*;
import com.example.root.newsapp.ui.adapter.CategoryAdapter;
import com.example.root.newsapp.ui.adapter.coverFlow.CoverFlow;
import com.example.root.newsapp.ui.adapter.coverFlow.ImageAdapter;
import com.example.root.newsapp.ui.adapter.NewsAdapter;
import com.example.root.newsapp.R;
import com.example.root.newsapp.ui.adapter.ViewPagerAdapter;
import com.example.root.newsapp.ui.listener.ListenerHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private HashMap<String, ArrayList<News>> data; // category -> List of news
    private ArrayList<String> categories = new ArrayList<>();
    private int language = Constants.LANGUAGE_ENGLISH;
    private int font_sz = 0,width,height;
    private Dialog dialog;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ScrollView contentView;
    private ViewPager viewPager;
    private ViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int newsAtSlider = 4;

    private Connection connection;
    private ConnectionDetector connectionDetector;

    private Handler handler;
    private Runnable update;

    private ViewPager viewPagerCategory;
    private TextView tabOne_Topic;
    private TextView tabTwo_Country;
    private NavigationView navigationView;
    private DrawerLayout drawer;

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

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener2 = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position != newsAtSlider - 1) {
                // last page. make button text to GOT IT
                tabTwo_Country.setBackground(getDrawable(R.drawable.tap1_selected_shape));
                tabTwo_Country.setTextColor(getColor(R.color.colorPrimary));

                tabOne_Topic.setBackground(null);
                tabOne_Topic.setTextColor(getColor(R.color.white));

            } else {
                // still pages are left tabOne_Topic.setBackground(getDrawable(R.drawable.tap2_selected_shape));
                tabOne_Topic.setTextColor(getColor(R.color.colorPrimary));

                tabTwo_Country.setBackground(null);
                tabTwo_Country.setTextColor(getColor(R.color.white));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GetScreenSize getScreenSize=new GetScreenSize(this);
        getScreenSize.getImageSize();
        height=getScreenSize.getHeight();
        width=getScreenSize.getWidth();
        //container_relative=(RelativeLayout)findViewById(R.id.Home_container_relative);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //custom toolbar
        ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.toolbar,
                null);

        // Set up your ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        Toolbar toolbar=(Toolbar)actionBar.getCustomView().getParent();
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        ImageView menuIcon = (ImageView) findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo "if English language the gravity mast be LEFT"
                if (drawer.isDrawerOpen(Gravity.RIGHT))
                    drawer.closeDrawer(Gravity.RIGHT);
                else
                    drawer.openDrawer(Gravity.RIGHT);
            }
        });

        ImageView searchIcon = (ImageView) findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo search
                startActivity(SearchActivity.getIntent(getApplicationContext(), "", "313",language+""));
            }
        });

        ImageView settingIcon = (ImageView) findViewById(R.id.settingIcon);
        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Settings.class));
                overridePendingTransition(R.anim.slide_in_top, R.anim.no_thing);
            }
        });
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);


        init();

    }

    private void menuInit(){
        viewPagerCategory = (ViewPager) findViewById(R.id.viewpagerCategory);
        setupViewPager(viewPagerCategory);

        viewPagerCategory.setOnPageChangeListener(viewPagerPageChangeListener2);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.menu_content);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("-_-", "enter linear");
            }
        });

        tabOne_Topic = (TextView) findViewById(R.id.tap1_topic);
        tabTwo_Country = (TextView) findViewById(R.id.tap2_country);

        tabOne_Topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerCategory.setCurrentItem(0);
                Log.e("-_-", "enter0");

                tabOne_Topic.setBackground(getDrawable(R.drawable.tap2_selected_shape));
                tabOne_Topic.setTextColor(getColor(R.color.colorPrimary));

                tabTwo_Country.setBackground(null);
                tabTwo_Country.setTextColor(getColor(R.color.white));

            }
        });

        tabTwo_Country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerCategory.setCurrentItem(1);
                Log.e("-_-", "enter1");

                tabTwo_Country.setBackground(getDrawable(R.drawable.tap1_selected_shape));
                tabTwo_Country.setTextColor(getColor(R.color.colorPrimary));

                tabOne_Topic.setBackground(null);
                tabOne_Topic.setTextColor(getColor(R.color.white));
            }
        });

        viewPagerCategory.setCurrentItem(1);

    }

    private void init() {
        connectionDetector=new ConnectionDetector(this);
        contentView = (ScrollView) findViewById(R.id.content_home);

        //getData
        //getData();
        getNews(Constants.getCountry(0).getCountrysourcefilterid());

        //slider_start
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        contentView.requestChildFocus(viewPager,viewPager);

        // adding bottom dots
        addBottomDots(0);


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
            }
        });
        //slider_end


        //coverFlow_start data_parse_done
        /*
        CoverFlow coverFlow;
        coverFlow = (CoverFlow) findViewById(R.id.coverFlow);

        coverFlow.setAdapter(new ImageAdapter(this));

        ImageAdapter coverImageAdapter =  new ImageAdapter(this);

        coverImageAdapter.createReflectedImages();

        coverFlow.setAdapter(coverImageAdapter);

        coverFlow.setSpacing(-30);
        coverFlow.setSelection(5, true);*/
        //coverFlow_end


        //recycle_view_start
        mRecyclerView = (RecyclerView) findViewById(R.id.news_list);

        //mRecyclerView.setHasFixedSize(true);


        //recycle_view_end

        //set font size
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getString("font_size","").equals(""))
            font_sz = Integer.parseInt(prefs.getString("font_size",""));
        Log.e("FOUNT_SIZE : " , prefs + "");


    }

    private void getData(){
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

                    getNews(Constants.getCountry(0).getCountrysourcefilterid());
                }
            });

        }
        else {
            Utility.connectionToast(this);
        }
    }

    private void getNews(int country) {
        data = new HashMap<>();
        if(connectionDetector.isConnectingToInternet()) {
            connection = new Connection(this, "/GetAllCounteryPosts/" + country + "/" + language , "Get");
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.e("checkresponseresult",str);

                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray _categories = jsonObject.names();

                    for (int i = 0; i < _categories.length(); i++) {
                        categories.add(_categories.get(i).toString());
                        JSONArray ja = jsonObject.getJSONArray(_categories.get(i).toString());
                        ArrayList<News> temp = new ArrayList<>();
                        for (int j = 0; j < ja.length(); j++) {
                            Gson gson = new GsonBuilder().create();
                            JSONObject jo = ja.getJSONObject(j);
                            News news = gson.fromJson(jo.toString(), News.class);
                            temp.add(news);
                        }
                        data.put(_categories.get(i).toString(), temp);
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

        //slider Add Data Start
        ArrayList<News> _data = new ArrayList<>();
        _data.add(this.data.get(categories.get(0)).get(0));
        _data.add(this.data.get(categories.get(1)).get(0));
        _data.add(this.data.get(categories.get(2)).get(0));
        _data.add(this.data.get(categories.get(3)).get(0));

        myViewPagerAdapter = new ViewPagerAdapter(this, R.layout.slide_one, _data);

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
        //slider add data _end

        //recycler_start
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final int lan = language;

        mAdapter = new NewsAdapter(data, categories, new ListenerHandler() {
            @Override
            public void doAction(int position) {
                //Bundle bundleAnimation =
                //        ActivityOptions.makeCustomAnimation(Home.this, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(NewsList.getIntent(getApplicationContext(),"/GetAllPosts/","313",categories.get(position), lan + ""));
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                //overridePendingTransition(R.anim.slide_in_top, R.anim.no_thing);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        //recycler_end

        menuInit();

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
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[1]);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*
        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),Settings.class));
            overridePendingTransition(R.anim.slide_in_top, R.anim.no_thing);
            return true;

        } else if (id == R.id.action_search) {

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_favorite) {

        } else if (id == R.id.nav_topic) {
            //startActivityForResult(DialogListActivity.getIntent(this, R.array.topic_names, R.array.topic_flags, R.string.select_topic), 1);
        } else if (id == R.id.nav_country) {
            //startActivityForResult(DialogListActivity.getIntent(this, R.array.country_names, R.array.country_flags, R.string.select_country), 1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String country = data.getStringExtra(DialogListActivity.RESULT_CONTRYCODE);
            Toast.makeText(this, country, Toast.LENGTH_LONG).show();
        }
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

    private void setupViewPager(ViewPager viewPager) {
        CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager());

        Fragment topic = new Topic();
        topic.setArguments(Topic.getBundle(categories));
        adapter.addFragment(topic, "ONE");
        adapter.addFragment(new CountryCategory(), "TWO");
        viewPager.setAdapter(adapter);
    }

}
