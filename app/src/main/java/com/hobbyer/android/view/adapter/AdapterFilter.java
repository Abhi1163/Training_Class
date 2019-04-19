package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.filter.CategoryListContentList;
import com.hobbyer.android.databinding.RowFilterBinding;
import com.hobbyer.android.interfaces.OnClickInterFace;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFilter extends RecyclerView.Adapter<AdapterFilter.MyViewHolder> {
    private static SparseBooleanArray sSelectedItems;
    private final LayoutInflater mInflator;
    private ArrayList<CategoryListContentList> studiosList;
    private Activity mActivity;
    private OnClickInterFace listner;
    private int flag = 0;

    public AdapterFilter(Activity mActivity, ArrayList<CategoryListContentList> list, OnClickInterFace onClick) {
        this.mActivity = mActivity;
        this.studiosList = list;
        this.listner = onClick;
        mInflator = LayoutInflater.from(mActivity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowFilterBinding rowFilterBinding = DataBindingUtil.inflate(mInflator, R.layout.row_filter, parent, false);
        return new MyViewHolder(rowFilterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if (studiosList != null && studiosList.size() > 0) {
            holder.bindData(studiosList.get(position));


            if (studiosList.get(position).isSelected()) {
                holder.myRowFilterBinding.flOuter.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.bg_yellow_filter));

            } else {
                holder.myRowFilterBinding.flOuter.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.bg_rect));

            }


            holder.myRowFilterBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (!studiosList.get(position).isSelected()) {
                        studiosList.get(position).setSelected(true);
                        listner.onItemClick(holder.myRowFilterBinding.flOuter, holder.getAdapterPosition(), studiosList.get(position));


                    }
                    else {
                        studiosList.get(position).setSelected(false);

                    }
                    notifyItemChanged(position);
                }
            });
        } else {
            holder.myRowFilterBinding.flOuter.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.bg_rect));
        }

    }


    @Override
    public int getItemCount() {
        return studiosList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowFilterBinding myRowFilterBinding;

        public MyViewHolder(RowFilterBinding rowFindClassTodayBinding) {
            super(rowFindClassTodayBinding.getRoot());
            this.myRowFilterBinding = rowFindClassTodayBinding;
        }

        public RowFilterBinding getBinding() {
            return myRowFilterBinding;
        }


        public void bindData(CategoryListContentList videosContentList) {
            myRowFilterBinding.tvFilterTitle.setText(videosContentList.getName());


            if (videosContentList.getImage() != null && !videosContentList.getImage().equalsIgnoreCase("")) {
                Picasso.with(mActivity).load(videosContentList.getImage()).placeholder(R.mipmap.ic_business).fit().into(myRowFilterBinding.ivFilter, new Callback() {
                    @Override
                    public void onSuccess() {
                        myRowFilterBinding.pbFilter.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        myRowFilterBinding.pbFilter.setVisibility(View.GONE);
                    }
                });
            } else {
                myRowFilterBinding.ivFilter.setImageResource(R.mipmap.ic_business);
                myRowFilterBinding.pbFilter.setVisibility(View.GONE);
            }


        }


        @Override
        public void onClick(View v) {
            if (sSelectedItems.get(getAdapterPosition(), false)) {
                sSelectedItems.delete(getAdapterPosition());
                v.setSelected(false);
            } else {
                sSelectedItems.put(getAdapterPosition(), true);
                v.setSelected(true);

            }
        }

    }


}