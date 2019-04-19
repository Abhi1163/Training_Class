package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.filter.CategoryListContentList;
import com.hobbyer.android.api.response.auth.planlist.PlanListData;
import com.hobbyer.android.databinding.RowAddpointsBinding;
import com.hobbyer.android.databinding.RowTopRatingBinding;
import com.hobbyer.android.interfaces.OnItemClickListenerr;
import com.hobbyer.android.interfaces.OnItemClickListners;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopRatingClassAdapter extends RecyclerView.Adapter<TopRatingClassAdapter.MyViewHolder> {

    private ArrayList<CategoryListContentList> list;
    private Activity activity;
    private OnItemClickListenerr listner;


    public TopRatingClassAdapter(Activity context, ArrayList<CategoryListContentList> list, OnItemClickListenerr listner) {
        this.list = list;
        this.activity = context;
        this.listner = listner;
    }


    @NonNull
    @Override
    public TopRatingClassAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_top_rating, viewGroup, false);
        return new TopRatingClassAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopRatingClassAdapter.MyViewHolder myViewHolder, int i) {
        if (list != null) {


            myViewHolder.rowAddpointsBinding.tvClassName.setText(list.get(i).getName());
            myViewHolder.rowAddpointsBinding.llTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    listner.onItemClick(view, myViewHolder.getAdapterPosition());
                }
            });
           /* if (list.get(i).getImage() != null && !list.get(i).getImage().equalsIgnoreCase("")) {
                Picasso.with(activity).load(list.get(i).getImage()).placeholder(R.mipmap.ic_business).fit().into(myViewHolder.rowAddpointsBinding.ivClass, new Callback() {
                    @Override
                    public void onSuccess() {
                        myViewHolder.rowAddpointsBinding.ivClass.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        myViewHolder.rowAddpointsBinding.ivClass.setVisibility(View.GONE);
                    }
                });
            } else {
                myViewHolder.rowAddpointsBinding.ivClass.setImageResource(R.mipmap.ic_business);
                myViewHolder.rowAddpointsBinding.ivClass.setVisibility(View.GONE);
            }*/



            Picasso.with(activity).load(list.get(i).getImage()).placeholder(R.mipmap.placeholder).fit().into(myViewHolder.rowAddpointsBinding.ivClass, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowTopRatingBinding rowAddpointsBinding;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rowAddpointsBinding = DataBindingUtil.bind(itemView);

        }
    }
}


