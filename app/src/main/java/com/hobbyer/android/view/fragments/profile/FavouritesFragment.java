package com.hobbyer.android.view.fragments.profile;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.request.FavouriteStudioRequest;
import com.hobbyer.android.api.request.FavouriteUserViewRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.favouriteuserview.FavouriteUserViewResponse;
import com.hobbyer.android.api.response.auth.favouriteuserview.FavouriteUserViewResponseContentList;
import com.hobbyer.android.api.response.auth.homepage.HomePageContentList;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.FragmentFavouriteBinding;
import com.hobbyer.android.databinding.RowFavLikeBinding;
import com.hobbyer.android.databinding.RowFavProfileBinding;
import com.hobbyer.android.interfaces.LogInNavigator;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import android.support.annotation.NonNull;

import com.hobbyer.android.view.fragments.findaclass.FragmentFindAClass;
import com.hobbyer.android.widget.CustomDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import retrofit2.Call;

public class FavouritesFragment extends Fragment {
    private Context context;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransection;
    private android.support.v4.app.Fragment fragment;
    FragmentFavouriteBinding fragmentFavouriteBinding;
    AdapterFavouriteUserView adapterFavouriteUserView;
    private ProfileFragment mFragment;
    private Activity activity;
    private TextView favouriteText;

   public FavouritesFragment( ) {

    }

    @SuppressLint("ValidFragment")
    public FavouritesFragment(ProfileFragment fragment) {
        mFragment=fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentFavouriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false);
        context = this.getActivity();
        this.activity = getActivity();
        fragment = new android.support.v4.app.Fragment();
        return fragmentFavouriteBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {

        favouritesPageConnection();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        fragmentFavouriteBinding.rvFavourites.setLayoutManager(mLayoutManager);
        fragmentFavouriteBinding.rvFavourites.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
    }


    private void favouritesPageConnection() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if(userModel == null) {
                    return;
                }
                FavouriteUserViewRequest homeRequest = new FavouriteUserViewRequest();
                homeRequest.setUserId(""+userModel.getId());
                homeRequest.setPage_number(1);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.favouriteUserView(homeRequest).enqueue(new BaseCallback<FavouriteUserViewResponse>(fragment) {
                    @Override
                    public void onSuccess(FavouriteUserViewResponse response) {
                        //fragmentHomeBinding.swipeContainer.setRefreshing(false);
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ArrayList<FavouriteUserViewResponseContentList> studiosList = response.getResult().getContentList();
                                adapterFavouriteUserView = new AdapterFavouriteUserView((Activity) context, studiosList);
                                fragmentFavouriteBinding.rvFavourites.setAdapter(adapterFavouriteUserView);
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<FavouriteUserViewResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }





    public class AdapterFavouriteUserView extends RecyclerView.Adapter<AdapterFavouriteUserView.MyViewHolder> {

        private ArrayList<FavouriteUserViewResponseContentList>contentLists;
        private Activity mActivity;
        private final LayoutInflater mInflator;
        String favourite_key=null;
        private CustomDialog dialog;
        private TextView tvOk,tvMessage;
        private LogInNavigator mLogInNavigator;
        public AdapterFavouriteUserView(Activity mActivity, ArrayList<FavouriteUserViewResponseContentList> list) {
            this.mActivity = mActivity;
            this.contentLists = list;
            this.mLogInNavigator = mLogInNavigator;
            mInflator = LayoutInflater.from(mActivity);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RowFavLikeBinding rowFavProfileBinding=DataBindingUtil.inflate(mInflator, R.layout.row_fav_like, parent, false);
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
            RowFavLikeBinding myRowFavProfileBinding;
            public MyViewHolder(RowFavLikeBinding rowFavProfileBinding) {
                super(rowFavProfileBinding.getRoot());
                this.myRowFavProfileBinding=rowFavProfileBinding;
            }

            public RowFavLikeBinding getBinding() {
                return myRowFavProfileBinding;
            }

            public void bindData(FavouriteUserViewResponseContentList upComingClassesList) {

                myRowFavProfileBinding.tvGymName.setText(upComingClassesList.getStudio_name());
              //  myRowFavProfileBinding.tvGym.setText(upComingClassesList.getDescription());
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
                       // myRowFavProfileBinding.ivUnLikeFav.setVisibility(View.VISIBLE);
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


        /*favouriteStudioConnection*/
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

                    AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                    webServices.favouriteStudio(favouriteStudioRequest).enqueue(new BaseCallback<BaseResponse>((AppCompatActivity) mActivity) {
                        @Override
                        public void onSuccess(BaseResponse response) {
                            if (response != null) {
                                if (response.getStatus() == 1) {
                                    favouritesPageConnection();
                                    mFragment.viewModel.countPointConnection();
                                   //favouriteText.setText(""+(Integer.valueOf(favouriteText.getText().toString().trim())-1));
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


    private void countPointConnection() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if(userModel == null) {
                    return;
                }
                CountPointRequest countPointRequest = new CountPointRequest();
                countPointRequest.setUserId(""+userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.count_Point(countPointRequest).enqueue(new BaseCallback<CountPointResponse>(fragment) {
                    @Override
                    public void onSuccess(CountPointResponse response) {

                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CountPointData countPointData = response.getResult().getData();
                                CountPointData   countPoint=   createCountPoint(countPointData);
                                PreferenceUtils.savePoint(countPoint);

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<CountPointResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }
    private CountPointData createCountPoint(CountPointData countPointData) {
        CountPointData countPointData1 = new CountPointData();
        countPointData1.setFriend_count(countPointData.getFriend_count());
        countPointData1.setFavourite_studios(countPointData.getFavourite_studios());
        countPointData1.setCompleted_class(countPointData.getCompleted_class());
        countPointData1.setUpcoming_class(countPointData.getUpcoming_class());
        countPointData1.setUser_point(countPointData.getUser_point());
        return countPointData1;
    }

}
