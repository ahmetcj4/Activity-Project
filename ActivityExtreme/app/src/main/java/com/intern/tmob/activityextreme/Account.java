package com.intern.tmob.activityextreme;

import android.net.Uri;

/**
 * Created by ahmet on 03.09.2015.
 */
public class Account {
    private String name;
    private String surname;
    private String id;
    private String location;
    private String gender;
    private Uri photoUri;


    Account(String id){
        this.id = id;
    }
    Account(String name,String surname,String id,String location,String gender,Uri photoUri){
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.location = location;
        this.gender = gender;
        this.photoUri = photoUri;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }
}
