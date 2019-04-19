package com.hobbyer.android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.filter.CategoryListContentList;
import com.hobbyer.android.api.response.auth.filter.CategoryListResponse;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanResponse;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanResult;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.FragmentTopRatingStudiosBinding;
import com.hobbyer.android.interfaces.OnClickInterFace;
import com.hobbyer.android.interfaces.OnItemClickListenerr;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.filter.FilterActivity;
import com.hobbyer.android.view.adapter.AdapterFilter;
import com.hobbyer.android.view.adapter.TopRatingClassAdapter;
import com.hobbyer.android.view.fragments.findaclass.FragmentFindAClass;
import com.hobbyer.android.view.fragments.findaclass.FragmentFindClassToday;
import com.hobbyer.android.view.fragments.refer.FragmentRefer;
import com.hobbyer.android.widget.CustomDialog;

import java.util.ArrayList;

import retrofit2.Call;

import static io.fabric.sdk.android.services.concurrency.AsyncTask.init;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatingStudiosFragment extends Fragment implements View.OnClickListener {

    private FragmentTopRatingStudiosBinding binding;
    private Context context;
    private Fragment fragment;
    private TextView tvTitle, tvRight;
    private ImageView ivBack, ivSearch, ivLocation;
    private CustomDialog dialog;
    private TextView tvOk, tvMessage;
    private DrawerLayout drawerLayout;
    private TopRatingClassAdapter adapterFilter;
private   String id;
    private ArrayList<String> list = new ArrayList();
    private ArrayList<CategoryListContentList> studiosList = new ArrayList<>();

    public TopRatingStudiosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_rating_studios, container, false);
        context = this.getContext();
        init();
        setToolbar();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();


    }

    private void setToolbar() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setImageResource(R.mipmap.ic_menu);
        tvRight.setVisibility(View.GONE);
        ivLocation.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        ivBack.setOnClickListener(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.toprating);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }  private void init() {
        ivBack = getActivity().findViewById(R.id.ivBack);
        ivLocation = getActivity().findViewById(R.id.ivLocation);
        ivSearch = getActivity().findViewById(R.id.ivSearch);
        tvTitle = getActivity().findViewById(R.id.tvTitle);
        tvRight = getActivity().findViewById(R.id.tvRight);

        drawerLayout = getActivity().findViewById(R.id.drawer_Main_layout);
    }
    private void initViews() {
        filterConnection();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        binding.rvTopRating.setLayoutManager(mLayoutManager);
        binding.rvTopRating.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
    }

    private void filterConnection() {

        if (CommonUtils.isOnline(context)) try {
            ProgressDialogUtils.show(context);
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
            webServices.category_List().enqueue(new BaseCallback<CategoryListResponse>((AppCompatActivity) context) {
                @Override
                public void onSuccess(CategoryListResponse response) {
                    if (response != null) {
                        if (response.getStatus() == 1) {
                            studiosList = response.getResult().getContentList();
                            adapterFilter = new TopRatingClassAdapter((Activity) context, studiosList, new OnItemClickListenerr() {

                                @Override
                                public void onItemClick(View view, int position) {

                                    list = new ArrayList<>();
                                    list.add(studiosList.get(position).getId());


                                    Bundle bundle = new Bundle();

                                    fragment = new FragmentFindAClass();
                                    bundle.putInt("pos", 0);
                                    bundle.putSerializable("categoryList",list);
                                    bundle.putString("isFrom", "top");

                                    fragment.setArguments(bundle);
                                    CommonUtils.setFragment(fragment, true, getActivity(), bundle, R.id.flHome);
                                           /* bundle.putInt("pos", 0);
                                            bundle.putString("id", "" + id);

                                            fragment.setArguments(bundle);
                                        CommonUtils.setFragment(fragment, true, getActivity(), bundle, R.id.flHome);*/


                                }
                            });
                            binding.rvTopRating.setAdapter(adapterFilter);
                        }
                    }
                    ProgressDialogUtils.dismiss();
                }

                @Override
                public void onFail(Call<CategoryListResponse> call, BaseResponse baseResponse) {
                    ProgressDialogUtils.dismiss();
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View view) {

    }
}
