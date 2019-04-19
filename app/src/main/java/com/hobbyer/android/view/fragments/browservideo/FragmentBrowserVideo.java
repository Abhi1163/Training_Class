package com.hobbyer.android.view.fragments.browservideo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.request.HomePageRequest;
import com.hobbyer.android.api.request.VideosRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.videos.VideosCategoryVideo;
import com.hobbyer.android.api.response.auth.videos.VideosContentList;
import com.hobbyer.android.api.response.auth.videos.VideosResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.FragmentBrowserVideoBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.adapter.AdapterBrowserVideo;
import com.hobbyer.android.view.adapter.AdapterTopLiveUser;

import java.util.ArrayList;
import retrofit2.Call;

public class FragmentBrowserVideo extends Fragment {
    private Fragment fragment;
    public FragmentBrowserVideo() {
        // Required empty public constructor
    }
    FragmentBrowserVideoBinding fragmentBrowserVideoBinding;
    private Activity mContext;
    private AdapterBrowserVideo browserVideoAdapter;
    private AdapterTopLiveUser  adapterTopLiveUser;
    private RecyclerView rvMindandBody;
    private ImageView ivBack,ivSearch,ivLocation;
    private TextView tvTitle,tvRight;
    private DrawerLayout drawerLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentBrowserVideoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_browser_video, container, false);
        mContext = getActivity();


        fragment= new FragmentBrowserVideo();
        init();
        setToolbar();
        return fragmentBrowserVideoBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        countPointConnection();

    }
    private void initViews() {
        videoConnection();
        LinearLayoutManager rvTopLiveLayoutManager = new LinearLayoutManager(fragmentBrowserVideoBinding.rvTopLiveUsers.getContext(), LinearLayoutManager.VERTICAL,false);
        fragmentBrowserVideoBinding.rvTopLiveUsers.setLayoutManager(rvTopLiveLayoutManager);

    }
    private void init() {
        ivBack=getActivity().findViewById(R.id.ivBack);
        ivLocation=getActivity().findViewById(R.id.ivLocation);
        ivSearch=getActivity().findViewById(R.id.ivSearch);
        tvTitle=getActivity().findViewById(R.id.tvTitle);
        tvRight=getActivity().findViewById(R.id.tvRight);
        drawerLayout=getActivity().findViewById(R.id.drawer_Main_layout);
    }

    private void setToolbar() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setImageResource(R.mipmap.ic_menu);
        tvRight.setVisibility(View.VISIBLE);
        ivLocation.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Browse Video");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }


    private void videoConnection() {

        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                UserModel userModel = PreferenceUtils.getUserModel();
                if(userModel == null) {
                    return;
                }
                VideosRequest videosRequest = new VideosRequest();
                videosRequest.setUserId(""+userModel.getId());
                videosRequest.setPage_number(1);
                videosRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.videos_list(videosRequest).enqueue(new BaseCallback<VideosResponse>(fragment) {
                    @Override
                    public void onSuccess(VideosResponse response) {

                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ArrayList<VideosContentList> videosCategoryVideo =response.getResult().getContentList();
                                ProgressDialogUtils.show(mContext);
                                adapterTopLiveUser = new AdapterTopLiveUser(mContext,videosCategoryVideo);
                                fragmentBrowserVideoBinding.rvTopLiveUsers.setAdapter(adapterTopLiveUser);
                                ProgressDialogUtils.dismiss();


                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<VideosResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }



    private void countPointConnection() {

        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
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
                                pointData();
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
            Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
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
    private void pointData(){
        CountPointData point = PreferenceUtils.getPoint();

        tvRight.setText(point.getUser_point() + " Points ");
    }


}
