/*
package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.homepage.HomePageContentList;

import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.view.fragments.findaclass.FragmentFindAClass;
import com.hobbyer.android.view.fragments.home.HomeFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdapterHomePage extends RecyclerView.Adapter<AdapterHomePage.DataObjectHolder> {


    private ArrayList<HomePageContentList> studiosList;
    Activity main;
    private Activity context;
    private FragmentTransaction mFragmentTransaction;
    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar pbHome;
        TextView tvStudioName,tvStudioDays,tvStudioDescription;
        ImageView imStudioImage;
        LinearLayout llFindClass;
        String sDate,todayTime;

        public DataObjectHolder(View itemView) {
            super(itemView);


            tvStudioName = (TextView) itemView.findViewById(R.id.tvStudioName);
            tvStudioDays = (TextView) itemView.findViewById(R.id.tvStudioDays);
            tvStudioDescription = (TextView) itemView.findViewById(R.id.tvStudioDescription);
            imStudioImage = (ImageView) itemView.findViewById(R.id.imStudioImage);
            llFindClass = (LinearLayout) itemView.findViewById(R.id.llFindClass);
            pbHome = (ProgressBar) itemView.findViewById(R.id.pbHome);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public AdapterHomePage(Activity context, ArrayList<HomePageContentList> list) {


        this.context = context;
        this.studiosList = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        if (studiosList.get(position).getStudioImage() != null && !studiosList.get(position).getStudioImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(studiosList.get(position).getStudioImage()).placeholder(R.drawable.ic_image).fit().into(holder.imStudioImage, new Callback() {
                @Override
                public void onSuccess() {
                    holder.pbHome.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.pbHome.setVisibility(View.GONE);
                }
            });
        } else {
            holder.imStudioImage.setImageResource(R.drawable.ic_image);
        }
        String todayAsString = StringUtils.dateFormat(studiosList.get(position).getDays());
        String timeStart = StringUtils.timeFormat(studiosList.get(position).getStartTime());
        String timeEnd = StringUtils.timeFormat(studiosList.get(position).getEndTime());
        holder.tvStudioDays.setText(studiosList.get(position).getDay()+"," +todayAsString+","+timeStart.toString()+"-"+timeEnd.toString());
        holder.tvStudioName.setText(studiosList.get(position).getStudioName());
        holder.tvStudioDescription.setText(studiosList.get(position).getDescription());
        holder.llFindClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.BUNDLE_KEY.FIND_CLASS_DAY, studiosList.get(position).getDay());
                //mFragmentTransaction.replace( R.id.mMainActivitysr, new FragmentFindAClass()).addToBackStack( "123" ).commit();
            }
        });

    }


    public void deleteItem(int index) {
        studiosList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return studiosList.size();
    }

    public void setCountries(ArrayList<HomePageContentList> mCountriesModel) {
        this.studiosList = mCountriesModel;
        notifyDataSetChanged();
    }


}
*/
