/*
package com.hobbyer.android.view.adapter;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.upcomingclasses.UpComingClassesContentList;
import com.hobbyer.android.databinding.RowUpcomingClassesBinding;
import com.hobbyer.android.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AdapterUpComingClasses extends RecyclerView.Adapter<AdapterUpComingClasses.MyViewHolder> {


    private ArrayList<UpComingClassesContentList> contentLists;
    private Activity mActivity;
    private final LayoutInflater mInflator;

    public AdapterUpComingClasses(Activity mActivity, ArrayList<UpComingClassesContentList> list) {
        this.mActivity = mActivity;
        this.contentLists = list;
        mInflator = LayoutInflater.from(mActivity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowUpcomingClassesBinding rowUpcomingClassesBinding=DataBindingUtil.inflate(mInflator, R.layout.row_upcoming_classes, parent, false);
        return new MyViewHolder(rowUpcomingClassesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (contentLists != null && contentLists.size() > 0) {
            holder.bindData(contentLists.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return contentLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowUpcomingClassesBinding myRowUpcomingClassesBinding;
        public MyViewHolder(RowUpcomingClassesBinding rowFindClassTodayBinding) {
            super(rowFindClassTodayBinding.getRoot());
            this.myRowUpcomingClassesBinding=rowFindClassTodayBinding;
        }
        public RowUpcomingClassesBinding getBinding() {
            return myRowUpcomingClassesBinding;
        }

        public void bindData(UpComingClassesContentList upComingClassesList) {
            String timeStart = StringUtils.timeFormat(upComingClassesList.getStartTime());
            myRowUpcomingClassesBinding.tvUpComingStudioDays.setText(timeStart.toString());
            myRowUpcomingClassesBinding.tvUpComingStudioName.setText(upComingClassesList.getStudioName());
            myRowUpcomingClassesBinding.tvUpComingStudioDescription.setText(upComingClassesList.getDescription());
            if (upComingClassesList.getStudioImage() != null && !upComingClassesList.getStudioImage().equalsIgnoreCase("")) {
                Picasso.with(mActivity).load(upComingClassesList.getStudioImage()).placeholder(R.drawable.ic_image).fit().into(myRowUpcomingClassesBinding.imStudioImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        myRowUpcomingClassesBinding.pbUpComing.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {
                        myRowUpcomingClassesBinding.pbUpComing.setVisibility(View.GONE);
                    }
                });
            } else {
                myRowUpcomingClassesBinding.imStudioImage.setImageResource(R.drawable.ic_image);
                myRowUpcomingClassesBinding.pbUpComing.setVisibility(View.GONE);
            }

        }
    }
}
*/
