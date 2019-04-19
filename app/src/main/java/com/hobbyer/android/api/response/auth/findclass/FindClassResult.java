package com.hobbyer.android.api.response.auth.findclass;

import java.util.ArrayList;

public class FindClassResult {


    private ArrayList<FindClassContentList>contentList;


    public ArrayList<FindClassContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<FindClassContentList> contentList) {
        this.contentList = contentList;
    }
}
