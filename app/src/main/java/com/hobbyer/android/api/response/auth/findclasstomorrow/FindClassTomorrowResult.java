package com.hobbyer.android.api.response.auth.findclasstomorrow;

import java.util.ArrayList;

public class FindClassTomorrowResult {

    private ArrayList<FindClassTomorrowContentList>contentList;


    public ArrayList<FindClassTomorrowContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<FindClassTomorrowContentList> contentList) {
        this.contentList = contentList;
    }
}
