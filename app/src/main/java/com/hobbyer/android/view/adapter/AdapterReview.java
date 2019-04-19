package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.review.ReviewContentList;
import com.hobbyer.android.databinding.RowReviewBinding;


import java.util.ArrayList;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.MyViewHolder> {
    private ArrayList<ReviewContentList> studiosList;
    private Activity mActivity;
    private final LayoutInflater mInflator;
    public AdapterReview(Activity mActivity, ArrayList<ReviewContentList> list) {
        this.mActivity = mActivity;
        this.studiosList = list;
        mInflator = LayoutInflater.from(mActivity);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowReviewBinding rowReviewBinding=DataBindingUtil.inflate(mInflator, R.layout.row_review, parent, false);
        return new MyViewHolder(rowReviewBinding);
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
        RowReviewBinding myRowReviewBinding;

        public MyViewHolder(RowReviewBinding rowReviewBinding) {
            super(rowReviewBinding.getRoot());
            this.myRowReviewBinding = rowReviewBinding;
        }

        public RowReviewBinding getBinding() {
            return myRowReviewBinding;
        }



        public void bindData(ReviewContentList reviewContentList) {

            myRowReviewBinding.tvStudioName.setText(reviewContentList.getStudioName());
            myRowReviewBinding.tvStudioFeedback.setText(reviewContentList.getFeedback());
            if (!TextUtils.isEmpty(""+reviewContentList.getRating())&&reviewContentList.getRating()!=0){
                myRowReviewBinding.rbStudioReview.setRating(reviewContentList.getRating());

            }else {
                myRowReviewBinding.rbStudioReview.setVisibility(View.GONE);

            }


        }
    }



}
