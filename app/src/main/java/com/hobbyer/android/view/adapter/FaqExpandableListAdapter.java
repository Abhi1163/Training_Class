package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;


import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.faq.FaqContentList;


import java.util.ArrayList;


public class FaqExpandableListAdapter extends RecyclerView.Adapter<FaqExpandableListAdapter.DataObjectHolder> {
    private ArrayList<FaqContentList> faqTitle;
    Activity main;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button btHowDo,btHowDoGon;
        private TextView txAnswer;
        LinearLayout linearLayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
           // cityName = (TextView) itemView.findViewById(R.id.mRestName);
            btHowDo = (Button) itemView.findViewById(R.id.btHowDo);
            btHowDoGon = (Button) itemView.findViewById(R.id.btHowDoGon);
            txAnswer =(TextView)itemView.findViewById(R.id.txAnswer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public FaqExpandableListAdapter(Activity activity, Context context, ArrayList<FaqContentList> faqList) {

        this.main = activity;
        this.context = context;
        this.faqTitle = faqList;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_faq_group, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        holder.btHowDo.setText(faqTitle.get(position).getTitle());
        holder.txAnswer.setText(faqTitle.get(position).getAnswer());

        holder.btHowDo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btHowDoGon.setText(faqTitle.get(position).getTitle());
                holder.txAnswer.setVisibility(View.VISIBLE);
                holder.btHowDoGon.setVisibility(View.VISIBLE);
                holder.btHowDo.setVisibility(View.GONE);
            }
        } );
        holder.btHowDoGon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.btHowDoGon.setVisibility(View.GONE);
                holder.txAnswer.setVisibility(View.GONE);
                holder.btHowDo.setVisibility(View.VISIBLE);

            }
        } );


    }


    public void deleteItem(int index) {
        faqTitle.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return faqTitle.size();
    }
    public void setCountries(ArrayList<FaqContentList> mCountriesModel) {
        this.faqTitle = mCountriesModel;
        notifyDataSetChanged();
    }


}
