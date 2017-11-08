package com.muhamadarief.belajarfirestore;

import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Created by Muhamad Arief on 08/11/2017.
 */

@IgnoreExtraProperties
public class FriendsResponse {

    private String name;
    private String image;
    private String title;
    private String company;

    public FriendsResponse() {
    }

    public FriendsResponse(String name, String image, String title, String company) {
        this.name = name;
        this.image = image;
        this.title = title;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
