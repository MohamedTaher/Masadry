package com.example.root.newsapp.ui.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.root.newsapp.ui.adapter.DialogListArrayAdapter;

import java.util.ArrayList;

/**
 * Created by root on 4/10/17.
 */

public class DialogListActivity extends ListActivity {

    public static String RESULT_CONTRYCODE = "selected_country";
    public String[] countrynames;
    private TypedArray imgs;
    private ArrayList<Country> countryList;
    private int name;
    private int flag;

    public final static String TITLE = "TITLE";
    public final static String NAME = "NAME";
    public final static String FLAG = "FLAG";

    public static Intent getIntent(Context context, int name, int flag, int title) {
        Intent intent = new Intent(context, DialogListActivity.class);
        intent.putExtra(NAME, name);
        intent.putExtra(FLAG, flag);
        intent.putExtra(TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        name = intent.getIntExtra(NAME,0);
        flag = intent.getIntExtra(FLAG,0);
        setTitle(getString(intent.getIntExtra(TITLE,0)));

        populateCountryList();
        ArrayAdapter<Country> adapter = new DialogListArrayAdapter(this, countryList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country c = countryList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_CONTRYCODE, c.getName());
                setResult(RESULT_OK, returnIntent);
                //imgs.recycle(); //recycle images
                finish();
            }
        });
    }

    private void populateCountryList() {
        countryList = new ArrayList<>();
        countrynames = getResources().getStringArray(name);
        imgs = getResources().obtainTypedArray(flag);
        for(int i = 0; i < countrynames.length; i++){
            countryList.add(new Country(countrynames[i], imgs.getDrawable(i)));
        }
    }

    public class Country {
        private String name;
        private Drawable flag;
        public Country(String name, Drawable flag){
            this.name = name;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }
    }
}
