package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.videos.VideosContentList;
import com.hobbyer.android.databinding.RowOfTopliveuserBinding;


import java.util.ArrayList;

public class AdapterTopLiveUser extends RecyclerView.Adapter<AdapterTopLiveUser.MyViewHolder> {
    private ArrayList<VideosContentList> studiosList;
    private Activity mActivity;
    private final LayoutInflater mInflator;
    public AdapterTopLiveUser(Activity mActivity, ArrayList<VideosContentList> list) {
        this.mActivity = mActivity;
        this.studiosList = list;
        mInflator = LayoutInflater.from(mActivity);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowOfTopliveuserBinding rowOfTopliveuserBinding=DataBindingUtil.inflate(mInflator, R.layout.row_of_topliveuser, parent, false);
        return new MyViewHolder(rowOfTopliveuserBinding);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (studiosList != null && studiosList.size() > 0) {
            ArrayList singleSectionItems = studiosList.get(position).getCategory_video();
            AdapterBrowserVideo itemListDataAdapter = new AdapterBrowserVideo(mActivity, singleSectionItems);
            holder.bindData(itemListDataAdapter,studiosList.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return studiosList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowOfTopliveuserBinding myRowOfTopliveuserBinding;

        public MyViewHolder(RowOfTopliveuserBinding rowFindClassTodayBinding) {
            super(rowFindClassTodayBinding.getRoot());
            this.myRowOfTopliveuserBinding = rowFindClassTodayBinding;
        }

        public RowOfTopliveuserBinding getBinding() {
            return myRowOfTopliveuserBinding;
        }



        public void bindData(AdapterBrowserVideo adapterBrowserVideo,VideosContentList videosContentList) {

            myRowOfTopliveuserBinding.tvTopTitle.setText(videosContentList.getName());
            myRowOfTopliveuserBinding.rvTopList.setHasFixedSize(true);
            myRowOfTopliveuserBinding.rvTopList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
            myRowOfTopliveuserBinding.rvTopList.setAdapter(adapterBrowserVideo);
            myRowOfTopliveuserBinding.rvTopList.setNestedScrollingEnabled(false);
            /*myRowOfTopliveuserBinding.mLiveData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*Bundle bundle = new Bundle();
                    ActivityController.startActivity(mActivity,GymTimeActivity.class,bundle);*//*
                }
            });*/
        }
    }



}
