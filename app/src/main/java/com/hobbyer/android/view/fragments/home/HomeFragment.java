package com.hobbyer.android.view.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.request.HomePageRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.homepage.HomePageContentList;
import com.hobbyer.android.api.response.auth.homepage.HomePageResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.FragmentHomeBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;
import com.hobbyer.android.view.activities.signup.SignUpActivity;
import com.hobbyer.android.view.fragments.findaclass.FragmentFindAClass;
import com.hobbyer.android.view.fragments.profile.ProfileFragment;
import com.hobbyer.android.view.fragments.upcoming.UpComingFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import retrofit2.Call;


public class HomeFragment  extends Fragment implements View.OnClickListener {
    private Context context;
    private Fragment fragment;
    private AdapterHomePage homeAdapter;
    FragmentHomeBinding  fragmentHomeBinding;
    private ImageView ivBack,ivSearch,ivLocation;
    private TextView tvTitle,tvRight;
    private DrawerLayout drawerLayout;
   private FragmentActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       fragmentHomeBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
       context=this.getContext();
       this.activity=activity;
        init();
        setToolbar();



        fragmentHomeBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homePageConnection();
            }
        });
        fragmentHomeBinding.swipeContainer.setColorSchemeResources( R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorPrimaryDark);
        fragment = new UpComingFragment();

        fragmentHomeBinding.tvUpcoming.setOnClickListener(this);
        fragmentHomeBinding.tvCompleted.setOnClickListener(this);

       return fragmentHomeBinding.getRoot();
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
        tvRight.setVisibility(View.INVISIBLE);
        ivLocation.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.home);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }
    private void initViews() {
        countPointConnection();
        homePageConnection();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        fragmentHomeBinding.rvHomePage.setLayoutManager(mLayoutManager);
        fragmentHomeBinding.rvHomePage.setLayoutManager(new GridLayoutManager(context, numberOfColumns));


    }
    private void homePageConnection() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if(userModel == null) {
                    return;
                }
                HomePageRequest homeRequest = new HomePageRequest();
                homeRequest.setUserId(""+userModel.getId());
                homeRequest.setPage_number(1);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.home_page(homeRequest).enqueue(new BaseCallback<HomePageResponse>(fragment) {
                    @Override
                    public void onSuccess(HomePageResponse response) {
                        fragmentHomeBinding.swipeContainer.setRefreshing(false);
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                if (response.getResult().getContentList().size() > 0) {
                                    fragmentHomeBinding.llRecy.setVisibility(View.VISIBLE);
                                    fragmentHomeBinding.llNoData.setVisibility(View.GONE);

                                    ArrayList<HomePageContentList> studiosList = response.getResult().getContentList();
                                    homeAdapter = new AdapterHomePage((Activity) context, studiosList);
                                    fragmentHomeBinding.rvHomePage.setAdapter(homeAdapter);
                                    countPointConnection();
                                }
                                else {

                                    fragmentHomeBinding.llRecy.setVisibility(View.GONE);
                                    fragmentHomeBinding.llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<HomePageResponse> call, BaseResponse baseResponse) {
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

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tvUpcoming:

                CommonUtils.setFragment(new UpComingFragment(), false, (FragmentActivity) context, R.id.flHome);

                break;
            case  R.id.tvCompleted:
                CommonUtils.setFragment(new ProfileFragment(), false, (FragmentActivity) context, R.id.flHome);

                break;

        }

    }

    public class AdapterHomePage extends RecyclerView.Adapter<AdapterHomePage.DataObjectHolder> {


        private ArrayList<HomePageContentList> studiosList;
        Activity main;
        private Activity context;
        public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ProgressBar pbHome;
            TextView tvStudioName,tvStudioDays,tvStudioDescription;
            ImageView imStudioImage;
            LinearLayout llFindClass;
            public DataObjectHolder(View itemView) {
                super(itemView);


                tvStudioName = (TextView) itemView.findViewById(R.id.tvStudioName);
                tvStudioDays = (TextView) itemView.findViewById(R.id.tvStudioDays);
                tvStudioDescription = (TextView) itemView.findViewById(R.id.tvStudioDescription);
                imStudioImage = (ImageView) itemView.findViewById(R.id.imStudioImage);
                llFindClass = (LinearLayout) itemView.findViewById(R.id.llFindClass);
                pbHome = (ProgressBar) itemView.findViewById(R.id.pbHome);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
            }
        }

        public AdapterHomePage(Activity context, ArrayList<HomePageContentList> list) {
            this.context = context;
            this.studiosList = list;
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_home, parent, false);
            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            return dataObjectHolder;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {

            if (studiosList.get(position).getStudioImage() != null && !studiosList.get(position).getStudioImage().equalsIgnoreCase("")) {
                Picasso.with(context).load(studiosList.get(position).getStudioImage()).placeholder(R.drawable.ic_image).fit().into(holder.imStudioImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.pbHome.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.pbHome.setVisibility(View.GONE);
                    }
                });
            } else {
                holder.imStudioImage.setImageResource(R.drawable.ic_image);
            }
            String todayAsString = StringUtils.dateFormat(studiosList.get(position).getDays());
            String timeStart = StringUtils.timeFormat(studiosList.get(position).getStartTime());
            String timeEnd = StringUtils.timeFormat(studiosList.get(position).getEndTime());
            holder.tvStudioDays.setText(studiosList.get(position).getDay()+"," +todayAsString+","+timeStart.toString()+"-"+timeEnd.toString());
            holder.tvStudioName.setText(studiosList.get(position).getStudioName());
            holder.tvStudioDescription.setText(studiosList.get(position).getAddress());
            holder.llFindClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    fragment = new FragmentFindAClass();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack();
                    bundle.putString(AppConstant.BUNDLE_KEY.CLASS_TYPE,"home");
                    HomePageContentList homePositionData = studiosList.get(position);
                    HomePageContentList userModel = createUserModel(homePositionData);
                    PreferenceUtils.saveHome(userModel);
                    ActivityController.setFragments(fragment, true, fragmentManager, R.id.flHome);
                }
            });

        }


        public void deleteItem(int index) {
            studiosList.remove(index);
            notifyItemRemoved(index);
        }

        @Override
        public int getItemCount() {
            return studiosList.size();
        }

        public void setCountries(ArrayList<HomePageContentList> mCountriesModel) {
            this.studiosList = mCountriesModel;
            notifyDataSetChanged();
        }


    }

    private HomePageContentList createUserModel(HomePageContentList homeData) {
        HomePageContentList homePageList = new HomePageContentList();
        homePageList.setId(homeData.getId());
        homePageList.setStudioId(homeData.getStudioId());
        homePageList.setUserId(homeData.getUserId());
        homePageList.setClassName(homeData.getClassName());
        homePageList.setStartDate(homeData.getStartDate());
        homePageList.setEndDate(homeData.getEndDate());
        homePageList.setStartTime(homeData.getStartTime());
        homePageList.setEndTime(homeData.getEndTime());
        homePageList.setDay(homeData.getDay());

        return homePageList;
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

    private void pointData(){
        CountPointData point = PreferenceUtils.getPoint();
        fragmentHomeBinding.tvUpcoming.setText(point.getUpcoming_class() + " Upcoming");
        fragmentHomeBinding.tvCompleted.setText(point.getCompleted_class() + " Completed ");
    }
}
