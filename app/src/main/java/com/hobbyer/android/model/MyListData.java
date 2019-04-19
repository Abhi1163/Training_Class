package com.hobbyer.android.model;

public class MyListData {
    private String name;
    private String imgUrl;
    private String location;
    private String pastClass;
    private String upconingClass;
    private String favoriteClass;
    private boolean isAdd;

    public MyListData() {
    }

    public MyListData(String name, String imgUrl, String location, String pastClass, String upconingClass, String favoriteClass, boolean isAdd) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.location = location;
        this.pastClass = pastClass;
        this.upconingClass = upconingClass;
        this.favoriteClass = favoriteClass;
        this.isAdd = isAdd;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPastClass() {
        return pastClass;
    }

    public void setPastClass(String pastClass) {
        this.pastClass = pastClass;
    }

    public String getUpconingClass() {
        return upconingClass;
    }

    public void setUpconingClass(String upconingClass) {
        this.upconingClass = upconingClass;
    }

    public String getFavoriteClass() {
        return favoriteClass;
    }

    public void setFavoriteClass(String favoriteClass) {
        this.favoriteClass = favoriteClass;
    }
}
