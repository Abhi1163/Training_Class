package com.hobbyer.android.model;

import java.util.List;

public class FilterModel {
    private String address;
    private String minPoint;

    private int minSeekbar;
    private int maxSeekbar;

    public int getMinSeekbar() {
        return minSeekbar;
    }

    public void setMinSeekbar(int minSeekbar) {
        this.minSeekbar = minSeekbar;
    }

    public int getMaxSeekbar() {
        return maxSeekbar;
    }

    public void setMaxSeekbar(int maxSeekbar) {
        this.maxSeekbar = maxSeekbar;
    }

    public String getMinPoint() {
        return minPoint;
    }

    public void setMinPoint(String minPoint) {
        this.minPoint = minPoint;
    }

    public String getMaxpoint() {
        return maxpoint;
    }

    public void setMaxpoint(String maxpoint) {
        this.maxpoint = maxpoint;
    }

    private String maxpoint;
    private String startTime;
    private String endTime;




    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
