package com.intern.tmob.activityextreme;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

public class WallItem implements Serializable{
    private String name;
    private String sent;
    private String detail;
    private String header;
    private int image;
    private String imageLink;
    private String fid;


    // image == -1 => wall
    public WallItem(String image, String name, String sent,
                           String detail, String header,String fid) {
        this.name = name;
        this.sent = sent;
        this.detail = detail;
        this.header = header;
        this.imageLink = image;
        this.image = -1;
        this.fid = fid;
    }

    // image != -1 => favorites
    public WallItem(int image, String name) {
        this.name = name;
        this.image = image;
        this.imageLink = "";
    }

    public String getFid(){ return fid;}
    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getsent() {
        return sent;
    }

    public void setsent(String sent) {
        this.sent = sent;
    }

    public String getdetail() {
        return detail;
    }

    public void setdetail(String detail) {
        this.detail = detail;
    }

    public int getimage() {
        return image;
    }

    public void setimage(int image) {
        this.image = image;
    }

    public String getheader() {
        return header;
    }

    public void setheader(String header) {
        this.header = header;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}
