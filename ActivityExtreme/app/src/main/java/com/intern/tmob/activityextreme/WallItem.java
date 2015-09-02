package com.intern.tmob.activityextreme;


public class WallItem {
    private String name;
    private String sent;
    private String detail;
    private String header;
    private int image;

    public WallItem(int image, String name, String sent,
                           String detail, String header) {
        this.name = name;
        this.sent = sent;
        this.detail = detail;
        this.header = header;
        this.image = image;
    }

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
}
