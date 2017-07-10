package com.example.root.newsapp.model;

/**
 * Created by root on 4/18/17.
 */

public class Country {
    private String countrylatname;
    private String countryflagurl;
    private int countrysourcefilterid;
    private int id_currency;

    public Country(String countrylatname, String countryflagurl, int countrysourcefilterid, int id_currency) {
        this.countrylatname = countrylatname;
        this.countryflagurl = countryflagurl;
        this.countrysourcefilterid = countrysourcefilterid;
        this.id_currency = id_currency;
    }

    public String getCountrylatname() {
        return countrylatname;
    }

    public void setCountrylatname(String countrylatname) {
        this.countrylatname = countrylatname;
    }

    public String getCountryflagurl() {
        return countryflagurl;
    }

    public void setCountryflagurl(String countryflagurl) {
        this.countryflagurl = countryflagurl;
    }

    public int getCountrysourcefilterid() {
        return countrysourcefilterid;
    }

    public void setCountrysourcefilterid(int countrysourcefilterid) {
        this.countrysourcefilterid = countrysourcefilterid;
    }

    public int getId_currency() {
        return id_currency;
    }

    public void setId_currency(int id_currency) {
        this.id_currency = id_currency;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countrylatname='" + countrylatname + '\'' +
                ", countryflagurl='" + countryflagurl + '\'' +
                ", countrysourcefilterid=" + countrysourcefilterid +
                ", id_currency=" + id_currency +
                '}';
    }
}
