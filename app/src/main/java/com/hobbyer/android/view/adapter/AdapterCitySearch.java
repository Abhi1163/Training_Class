package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hobbyer.android.R;

import com.hobbyer.android.api.response.auth.searchcity.CityContentList;

import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.RowCityBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.findyourcity.FindYourCityActivity;

import java.util.ArrayList;

public class AdapterCitySearch extends RecyclerView.Adapter<AdapterCitySearch.MyViewHolder> {


    private ArrayList<CityContentList> cityList;

    private Activity mActivity;
    private final LayoutInflater mInflator;

    public AdapterCitySearch(Activity mActivity, ArrayList<CityContentList> list) {
        this.mActivity = mActivity;
        this.cityList = list;
        mInflator = LayoutInflater.from(mActivity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCityBinding cityBinding = DataBindingUtil.inflate(mInflator, R.layout.row_city, parent, false);
        return new AdapterCitySearch.MyViewHolder(cityBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (cityList != null && cityList.size() > 0) {
            holder.bindData(cityList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowCityBinding myRowCityBinding;

        public MyViewHolder(RowCityBinding rowCityBinding) {
            super(rowCityBinding.getRoot());
            this.myRowCityBinding = rowCityBinding;
        }

        public RowCityBinding getBinding() {
            return myRowCityBinding;
        }


        public void bindData(CityContentList cityContentList) {
            myRowCityBinding.mRestName.setText(cityContentList.getName());

            myRowCityBinding.mItemName.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstant.BUNDLE_KEY.CITY_NAME, cityContentList.getName());
                    bundle.putString(AppConstant.BUNDLE_KEY.CITY_LATITUDE, cityContentList.getLatitude());
                    bundle.putString(AppConstant.BUNDLE_KEY.CITY_LONGITUDE, cityContentList.getLongitude());
                    ActivityController.startActivityForResult(mActivity, FindYourCityActivity.class, 1,bundle,true);

                }
            } );

        }

    }
    public void deleteItem(int index) {
        cityList.remove(index);
        notifyItemRemoved(index);
    }


    public void setCountries(ArrayList<CityContentList> mCountriesModel) {
        this.cityList = mCountriesModel;
        notifyDataSetChanged();
    }
}


















































