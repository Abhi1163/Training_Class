package com.hobbyer.android.api.response.auth.review;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

 public class ReviewResult {

    private ArrayList<ReviewContentList>contentList;

     public ArrayList<ReviewContentList> getContentList() {
         return contentList;
     }

     public void setContentList(ArrayList<ReviewContentList> contentList) {
         this.contentList = contentList;
     }
 }
