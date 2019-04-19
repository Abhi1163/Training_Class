package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.findclasstomorrow.FindClassTomorrowContentList;
import com.hobbyer.android.api.response.auth.videos.VideosCategoryVideo;
import com.hobbyer.android.api.response.auth.videos.VideosContentList;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.RowOfMindbodyBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.BitmapUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;
import com.hobbyer.android.view.activities.intro.IntroSeriesActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AdapterBrowserVideo extends RecyclerView.Adapter<AdapterBrowserVideo.MyViewHolder> {

    private ArrayList<VideosCategoryVideo> studiosList;
    private Activity mActivity;
    private Bitmap videoUrl;
    private String imageLink;
    private final LayoutInflater mInflator;
    public AdapterBrowserVideo(Activity mActivity, ArrayList<VideosCategoryVideo> list) {
        this.mActivity = mActivity;
        this.studiosList = list;
        mInflator = LayoutInflater.from(mActivity);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowOfMindbodyBinding rowOfMindbodyBinding= DataBindingUtil.inflate(mInflator, R.layout.row_of_mindbody, parent, false);
        return new MyViewHolder(rowOfMindbodyBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (studiosList != null && studiosList.size() > 0) {
            holder.bindData(studiosList.get(position));

        }
    }


    @Override
    public int getItemCount() {
        return studiosList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowOfMindbodyBinding myRowOfMindbodyBinding;

        public MyViewHolder(RowOfMindbodyBinding rowFindClassTodayBinding) {
            super(rowFindClassTodayBinding.getRoot());
            this.myRowOfMindbodyBinding = rowFindClassTodayBinding;
        }

        public RowOfMindbodyBinding getBinding() {
            return myRowOfMindbodyBinding;
        }



        public void bindData(VideosCategoryVideo videosContentList) {
            ProgressDialogUtils.show(mActivity);
            myRowOfMindbodyBinding.tvVideoIntroSeries.setText(Html.fromHtml(videosContentList.getVideoName().replace("<>h1","h6")));
            myRowOfMindbodyBinding.tvVideoIntro.setText(Html.fromHtml(videosContentList.getDescription().replace("<h1>","<h6>")));
            try {

                videoUrl=BitmapUtils.retriveVideoFrameFromVideo(videosContentList.getVideoUrl());

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            myRowOfMindbodyBinding.ivVideoImage.setImageBitmap(videoUrl);
            ProgressDialogUtils.dismiss();
            myRowOfMindbodyBinding.llVideoItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     Bundle bundle = new Bundle();
                      bundle.putParcelable(AppConstant.BUNDLE_KEY.VIDEOS_LIST,videosContentList);
                    ActivityController.startActivity(mActivity, IntroSeriesActivity.class,bundle);
                }
            });
        }
    }



}
