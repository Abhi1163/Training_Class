/*
package com.hobbyer.android.view.adapter;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.FavouriteStudioRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.favouriteuserview.FavouriteUserViewResponseContentList;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.RowFavProfileBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.widget.CustomDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;


public class AdapterFavouriteUserView extends RecyclerView.Adapter<AdapterFavouriteUserView.MyViewHolder> {

    private ArrayList<FavouriteUserViewResponseContentList>contentLists;
    private Activity mActivity;
    private final LayoutInflater mInflator;
    String favourite_key=null;
    private CustomDialog dialog;
    private TextView tvOk,tvMessage;
    public AdapterFavouriteUserView(Activity mActivity, ArrayList<FavouriteUserViewResponseContentList> list) {
        this.mActivity = mActivity;
        this.contentLists = list;
        mInflator = LayoutInflater.from(mActivity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFavProfileBinding rowFavProfileBinding=DataBindingUtil.inflate(mInflator, R.layout.row_fav_profile, parent, false);
        return new MyViewHolder(rowFavProfileBinding);
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
        RowFavProfileBinding myRowFavProfileBinding;
        public MyViewHolder(RowFavProfileBinding rowFavProfileBinding) {
            super(rowFavProfileBinding.getRoot());
            this.myRowFavProfileBinding=rowFavProfileBinding;
        }

        public RowFavProfileBinding getBinding() {
            return myRowFavProfileBinding;
        }

        public void bindData(FavouriteUserViewResponseContentList upComingClassesList) {

            myRowFavProfileBinding.tvGymName.setText(upComingClassesList.getStudio_name());
            myRowFavProfileBinding.tvGym.setText(upComingClassesList.getDescription());
            if (upComingClassesList.getFull_studio_image_url() != null && !upComingClassesList.getFull_studio_image_url().equalsIgnoreCase("")) {
                Picasso.with(mActivity).load(upComingClassesList.getFull_studio_image_url()).placeholder(R.drawable.ic_image).fit().into(myRowFavProfileBinding.ivGymImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        myRowFavProfileBinding.pbFav.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {
                        myRowFavProfileBinding.pbFav.setVisibility(View.GONE);
                    }
                });
            } else {
                myRowFavProfileBinding.ivGymImage.setImageResource(R.drawable.ic_image);
                myRowFavProfileBinding.pbFav.setVisibility(View.GONE);
            }
            myRowFavProfileBinding.ivLikeFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRowFavProfileBinding.ivLikeFav.setVisibility(View.GONE);
                    myRowFavProfileBinding.ivUnLikeFav.setVisibility(View.VISIBLE);
                    favourite_key="0";
                    favouriteStudioConnection(upComingClassesList.getStudio_id());
                }
            });
            myRowFavProfileBinding.ivUnLikeFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRowFavProfileBinding.ivLikeFav.setVisibility(View.VISIBLE);
                    myRowFavProfileBinding.ivUnLikeFav.setVisibility(View.GONE);
                    favourite_key="1";
                    favouriteStudioConnection(upComingClassesList.getStudio_id());
                }
            });
        }
    }


    */
/*favouriteStudioConnection*//*

    private void favouriteStudioConnection(String studioId) {
        if (CommonUtils.isOnline(mActivity)) {
            try {
                ProgressDialogUtils.show(mActivity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                FavouriteStudioRequest favouriteStudioRequest = new FavouriteStudioRequest();
                favouriteStudioRequest.setUser_id("" + userModel.getId());
                favouriteStudioRequest.setStudio_id(studioId);
                favouriteStudioRequest.setFavourite_key(favourite_key);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
                webServices.favouriteStudio(favouriteStudioRequest).enqueue(new BaseCallback<BaseResponse>((AppCompatActivity) mActivity) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                showDialog(response.getMessage());
                            } else {
                                ToastUtils.showToastLong(mActivity, "  " + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mActivity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }
    private void showDialog(String message) {
        dialog=new CustomDialog(mActivity,R.layout.row_message_city);
        tvOk=dialog.findViewById(R.id.btnOk);
        tvMessage=dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }
}
*/
