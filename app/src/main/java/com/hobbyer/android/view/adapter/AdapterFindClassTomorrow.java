package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.findclass.FindClassContentList;
import com.hobbyer.android.api.response.auth.findclasstomorrow.FindClassTomorrowContentList;
import com.hobbyer.android.utils.StringUtils;

import java.util.ArrayList;

public class AdapterFindClassTomorrow extends RecyclerView.Adapter<AdapterFindClassTomorrow.DataObjectHolder> {


    private ArrayList<FindClassTomorrowContentList> studiosList;
    Activity main;
    private Activity context;

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar pbHome;
        TextView tvGymName,tvTime,tvGymDescription,tvMin;
        Button btGymPoint;
        public DataObjectHolder(View itemView) {
            super(itemView);
            tvGymName = (TextView) itemView.findViewById(R.id.tvGymName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvGymDescription = (TextView) itemView.findViewById(R.id.tvGymDescription);
            tvMin = (TextView) itemView.findViewById(R.id.tvMin);
            btGymPoint=(Button)itemView.findViewById(R.id.btGymPoint);
            pbHome = (ProgressBar) itemView.findViewById(R.id.pbFind);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public AdapterFindClassTomorrow(Activity context, ArrayList<FindClassTomorrowContentList> list) {


        this.context = context;
        this.studiosList = list;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_find_class_tomorrow, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {



        String timeStart = StringUtils.timeFormat(studiosList.get(position).getStartTime());
       /* String timeEnd = StringUtils.timeFormat(studiosList.get(position).getEndTime());
        String todayAsString = StringUtils.dateFormat(studiosList.get(position).getDays());*/
        holder.tvTime.setText(timeStart.toString());

        holder.tvMin.setText(studiosList.get(position).getDuration() + " min");
        holder.tvGymName.setText(studiosList.get(position).getStudioName());
        holder.tvGymDescription.setText(studiosList.get(position).getDescription());
        //holder.btGymPoint.setText(studiosList.get(position).getPoints());


    }


    public void deleteItem(int index) {
        studiosList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return studiosList.size();
    }

    public void setCountries(ArrayList<FindClassTomorrowContentList> findClassModel) {
        this.studiosList = findClassModel;
        notifyDataSetChanged();
    }


}
