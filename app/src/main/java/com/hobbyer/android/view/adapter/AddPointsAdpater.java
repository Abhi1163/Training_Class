package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.planlist.PlanListData;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.DialogReviewDetailsBinding;
import com.hobbyer.android.databinding.RowAddpointsBinding;
import com.hobbyer.android.interfaces.OnClickInterFace;
import com.hobbyer.android.interfaces.OnItemClickListners;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.view.activities.intro.IntroSeriesActivity;

import java.util.ArrayList;

public class AddPointsAdpater extends RecyclerView.Adapter<AddPointsAdpater.MyViewHolder> {

    private ArrayList<PlanListData> list;
    private Activity activity;
    private OnItemClickListners listner;


    public AddPointsAdpater(Activity context, ArrayList<PlanListData> list, OnItemClickListners listner) {
        this.list = list;
        this.activity = context;
        this.listner=listner;
    }



    @NonNull
    @Override
    public AddPointsAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.row_addpoints,viewGroup,false);
        return new AddPointsAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPointsAdpater.MyViewHolder myViewHolder, int i) {
        if(list!=null){


            myViewHolder.rowAddpointsBinding.tvPlans.setText("Buy  "+list.get(i).getPoints()+" Points Pack");
            myViewHolder.rowAddpointsBinding.tvPoints.setText("$"+list.get(i).getPlans()+".00");
            myViewHolder.rowAddpointsBinding.llPoints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    listner.onItemClick(view,myViewHolder.getAdapterPosition(),list.get(i));
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private RowAddpointsBinding rowAddpointsBinding;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rowAddpointsBinding= DataBindingUtil.bind(itemView);

        }
    }




}