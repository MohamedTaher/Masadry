package com.example.root.newsapp.model;

import java.io.Serializable;

/**
 * Created by root on 4/9/17.
 */

public class News implements Serializable{
    private String ID;
    private String Date;
    private String Title;
    private String Image;
    private String Author;
    private String postlink;
    private String posttype;
    private String categoryurl;
    private String adtype;
    private String countryurl;
    private String adwidth;
    private String adheight;
    private String term_id;
    private String name;
    private String slug;
    private String post_author;

    private String Content;
    private String PostContent;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPostContent() {
        return PostContent;
    }

    public void setPostContent(String postContent) {
        PostContent = postContent;
    }

    public News(String ID, String date, String title, String image, String author, String postlink, String posttype, String categoryurl, String adtype, String countryurl, String adwidth, String adheight, String term_id, String name, String slug, String post_author, String content, String postContent) {
        this.ID = ID;
        Date = date;
        Title = title;
        Image = image;
        Author = author;
        this.postlink = postlink;
        this.posttype = posttype;
        this.categoryurl = categoryurl;
        this.adtype = adtype;
        this.countryurl = countryurl;
        this.adwidth = adwidth;
        this.adheight = adheight;
        this.term_id = term_id;
        this.name = name;
        this.slug = slug;
        this.post_author = post_author;
        Content = content;
        PostContent = postContent;
    }

    public News(String ID, String date, String title, String image, String author, String postlink, String posttype, String categoryurl, String adtype, String countryurl, String adwidth, String adheight, String term_id, String name, String slug, String post_author) {
        this.ID = ID;
        Date = date;
        Title = title;
        Image = image;
        Author = author;
        this.postlink = postlink;
        this.posttype = posttype;
        this.categoryurl = categoryurl;
        this.adtype = adtype;
        this.countryurl = countryurl;
        this.adwidth = adwidth;
        this.adheight = adheight;
        this.term_id = term_id;
        this.name = name;
        this.slug = slug;
        this.post_author = post_author;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTitle() {
        return Title.trim();
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image.equals("") ? "http://masadry.net/wp-content/uploads/2017/appresources/defaultimage.jpg" : Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getPostlink() {
        return postlink;
    }

    public void setPostlink(String postlink) {
        this.postlink = postlink;
    }

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    public String getCategoryurl() {
        return categoryurl;
    }

    public void setCategoryurl(String categoryurl) {
        this.categoryurl = categoryurl;
    }

    public String getAdtype() {
        return adtype;
    }

    public void setAdtype(String adtype) {
        this.adtype = adtype;
    }

    public String getCountryurl() {
        return countryurl;
    }

    public void setCountryurl(String countryurl) {
        this.countryurl = countryurl;
    }

    public String getAdwidth() {
        return adwidth;
    }

    public void setAdwidth(String adwidth) {
        this.adwidth = adwidth;
    }

    public String getAdheight() {
        return adheight;
    }

    public void setAdheight(String adheight) {
        this.adheight = adheight;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }
}
