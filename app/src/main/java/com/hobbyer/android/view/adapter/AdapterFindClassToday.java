package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.findclass.FindClassContentList;
import android.view.LayoutInflater;

import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.RowFindClassTodayBinding;
import com.hobbyer.android.interfaces.HorizontalListItemClickListener;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFindClassToday extends RecyclerView.Adapter<AdapterFindClassToday.MyViewHolder> {
    private ArrayList<FindClassContentList> studiosList;
    private Activity mActivity;
    private final LayoutInflater mInflator;
    float rating;
    String ratingCount;
    String dateCurrent;
    public AdapterFindClassToday(Activity mActivity, ArrayList<FindClassContentList> list,String date ) {
        this.mActivity = mActivity;
        this.studiosList = list;
        this.dateCurrent=date;
        mInflator = LayoutInflater.from(mActivity);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFindClassTodayBinding findClassTodayBinding=DataBindingUtil.inflate(mInflator, R.layout.row_find_class_today, parent, false);
        return new MyViewHolder(findClassTodayBinding);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (studiosList != null && studiosList.size() > 0) {
            holder.bindData(studiosList.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return studiosList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowFindClassTodayBinding myFindClassListBinding;

        public MyViewHolder(RowFindClassTodayBinding rowFindClassTodayBinding) {
            super(rowFindClassTodayBinding.getRoot());
            this.myFindClassListBinding = rowFindClassTodayBinding;
        }

        public RowFindClassTodayBinding getBinding() {
            return myFindClassListBinding;
        }



        public void bindData(FindClassContentList findClassList) {
            String timeStart = StringUtils.timeFormat(findClassList.getStartTime());
            myFindClassListBinding.tvTime.setText(timeStart.toString());
            myFindClassListBinding.tvMin.setText(findClassList.getDuration() + " min");
            myFindClassListBinding.tvGymName.setText(findClassList.getStudioName());
            myFindClassListBinding.btGymPoints.setText(""+findClassList.getPoints()+ " Points");
            myFindClassListBinding.tvGymDescription.setText(findClassList.getDescription());
            if (!TextUtils.isEmpty(""+findClassList.getRating_count())&&findClassList.getRating_count()!=0){
                rating = Float.parseFloat(findClassList.getRating());
                myFindClassListBinding.ratingBar.setRating(rating);
                ratingCount = Integer.toString(findClassList.getRating_count());
                myFindClassListBinding.tvRating.setText(""+rating);
            }else {
                myFindClassListBinding.ratingBar.setVisibility(View.GONE);
                myFindClassListBinding.tvRating.setVisibility(View.GONE);
            }

            myFindClassListBinding.llFindClassItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID, findClassList.getClass_schedule_id());
                    bundle.putString(AppConstant.BUNDLE_KEY.DATE, dateCurrent);
                    ActivityController.startActivity(mActivity,GymTimeActivity.class,bundle);
                }
            });
        }
    }



}
