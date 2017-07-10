package com.example.root.newsapp.Date;

import com.example.root.newsapp.model.Country;

import java.util.ArrayList;

/**
 * Created by root on 4/18/17.
 */

public class Constants {
    public static final int LANGUAGE_ENGLISH = 1;
    public static final int LANGUAGE_ARABIC = 2;

    private static ArrayList<Country> countries = new ArrayList<>();

    public static void addCountry (Country country) {
        countries.add(country);
    }

    public static Country getCountry (int index) {
        return countries.get(index);
    }

    public static ArrayList<Country> getCountries() {
        return countries;
    }
}
