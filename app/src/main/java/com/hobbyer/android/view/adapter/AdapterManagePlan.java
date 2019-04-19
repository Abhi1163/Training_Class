package com.hobbyer.android.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanData;
import com.hobbyer.android.databinding.RowManagePlanBinding;
import com.hobbyer.android.interfaces.OnItemClickListenerr;
import com.hobbyer.android.view.activities.manageplan.ManagePlanActivity;

import java.util.List;

public class AdapterManagePlan extends RecyclerView.Adapter<AdapterManagePlan.MyViewHolder> {
    private ManagePlanActivity activity;
    private List<ManagePlanData> planDataList;
    private OnItemClickListenerr onItemClick;


    public AdapterManagePlan(ManagePlanActivity activity, List<ManagePlanData> data, OnItemClickListenerr onItemClickListenerr) {
        this.activity = activity;
        this.planDataList = data;
        this.onItemClick = onItemClickListenerr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_manage_plan, viewGroup, false);
        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (planDataList != null) {


            if (planDataList.get(i).getIsCurrent() == 1) {

                    myViewHolder.binding.rbManage.setChecked(true);



            } else {


                    myViewHolder.binding.rbManage.setChecked(false);


            }

            ManagePlanData managePlanData = planDataList.get(i);

                if (managePlanData.getDuration()==1) {
                    if (managePlanData.getMembershipType().equalsIgnoreCase("Free")) {
                        myViewHolder.binding.tvPlan.setText("$ 0 Free week");

                    } else {
                        myViewHolder.binding.tvPlan.setText("$ " + planDataList.get(i).getMembership() + ".00 per week");

                    }
                }
             else    if (managePlanData.getDuration()==2)
             {
                 if (managePlanData.getMembershipType().equalsIgnoreCase("Free"))
                 {
                        myViewHolder.binding.tvPlan.setText("$ 0 Free month");
                    }
                    else {


                     myViewHolder.binding.tvPlan.setText("$ " + planDataList.get(i).getMembership() + ".00 per month");
                 }

                }
                else
                {
                    if (managePlanData.getMembershipType().equalsIgnoreCase("Free"))
                    {
                        myViewHolder.binding.tvPlan.setText("$ 0 Free year");
                    }
                    else {


                        myViewHolder.binding.tvPlan.setText("$ " + planDataList.get(i).getMembership() + ".00 per year");

                    }
                }


                 //   myViewHolder.binding.tvPlan.setText("$ " + planDataList.get(i).getMembership() + ".00 per month");
                myViewHolder.binding.tvPlanPoints.setText("Includes " + planDataList.get(i).getPoints() + " Points to book " + planDataList.get(i).getClasses() + " classes");

            }





    }

    @Override
    public int getItemCount() {
        return planDataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowManagePlanBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.rbManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
