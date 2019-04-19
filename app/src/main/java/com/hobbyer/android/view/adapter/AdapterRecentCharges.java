package com.hobbyer.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.recentcharges.RecentChargeList;
import com.hobbyer.android.databinding.RowRecentChargesBinding;
import com.hobbyer.android.prefs.PreferenceUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterRecentCharges extends RecyclerView.Adapter<AdapterRecentCharges.MyViewHolder> {
    private List<RecentChargeList> recentChargeLists;
    private Context context;
    private Date dateTime;


    public AdapterRecentCharges(Context context, List<RecentChargeList> recentChargeLists) {

        this.context=context;
        this.recentChargeLists=recentChargeLists;


    }

    @NonNull
    @Override
    public AdapterRecentCharges.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recent_charges,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecentCharges.MyViewHolder myViewHolder, int i) {
        if ( recentChargeLists!=null)
        {
            UserModel userModel = PreferenceUtils.getUserModel();
           String firstName= userModel.getFirstName();
           String lastName= userModel.getLastName();
           String name=firstName+" "+lastName;

            SimpleDateFormat month_date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String actualDate = recentChargeLists.get(i).getCreatedAt();
            try {
                dateTime = sdf.parse(actualDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String monthDate = month_date.format(dateTime);
            System.out.println("Month :" + monthDate);



            myViewHolder.binding.tvName.setText(name);
            myViewHolder.binding.tvDateFormat.setText(monthDate);
            myViewHolder.binding.tvRecentChargeFormat.setText(recentChargeLists.get(i).getDeductionType());

        }

    }

    @Override
    public int getItemCount() {
        return recentChargeLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowRecentChargesBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
