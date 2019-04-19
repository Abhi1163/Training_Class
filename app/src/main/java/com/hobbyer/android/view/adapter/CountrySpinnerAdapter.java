package com.hobbyer.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.countrylist.CountryContentList;
import com.hobbyer.android.databinding.RowCountryCodeBinding;
import com.hobbyer.android.interfaces.ProfileInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CountrySpinnerAdapter extends RecyclerView.Adapter<CountrySpinnerAdapter.MyViewHolder> {
    public List<CountryContentList> contentList=new ArrayList<>();
    private Context context;
    private Date dateTime;
    private ProfileInterface profileInterface;


    public CountrySpinnerAdapter(Context context, List<CountryContentList> contentList, ProfileInterface profileInterface) {
        this.context = context;
        this.contentList = contentList;
        this.profileInterface = profileInterface;
    }

    @NonNull
    @Override
    public CountrySpinnerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_country_code, viewGroup, false);

        return new CountrySpinnerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountrySpinnerAdapter.MyViewHolder myViewHolder, int i) {
        if (contentList != null) {
            String code =contentList.get(i).getPhonecode();

            myViewHolder.binding.tvCountryCode.setText(""+code);
            myViewHolder.binding.tvName.setText(contentList.get(i).getCountry());
            myViewHolder.binding.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    profileInterface.onItemClick(view,i);
                }
            });


        }

    }

    public void updatedList(List<CountryContentList> filteresNames){
        contentList = filteresNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowCountryCodeBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
