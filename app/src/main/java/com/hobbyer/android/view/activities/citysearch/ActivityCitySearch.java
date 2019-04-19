package com.hobbyer.android.view.activities.citysearch;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.searchcity.CityContentList;
import com.hobbyer.android.api.response.auth.searchcity.CityResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.SearchCityActivityBinding;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.view.adapter.AdapterCitySearch;

import java.util.ArrayList;

import retrofit2.Call;

public class ActivityCitySearch extends AppCompatActivity implements View.OnClickListener {


    RecyclerView counrtriesList;
    private SearchCityActivityBinding binding;
    private EditText autoCompView;
    private AppCompatImageView mClear_btn_search_view, mClose_btn_search_view;
    private String mLat, mLng, mEntity_id;
    private Context mContext;
    private ArrayList<CityContentList> cityList;
    private ProgressBar mmProgressBar;
    private AdapterCitySearch countriesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.search_city_activity);
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(this, mContext.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        autoCompView = (EditText) findViewById(R.id.search_input);
        counrtriesList = (RecyclerView) findViewById(R.id.CounrtriesList);
        mClear_btn_search_view = (AppCompatImageView) findViewById(R.id.clear_btn_search_view);
        // mClose_btn_search_view = (AppCompatImageView)findViewById( R.id.close_btn_search_view );
        if (autoCompView != null) {
            mClear_btn_search_view.setVisibility(View.GONE);
        } else {
            mClear_btn_search_view.setVisibility(View.VISIBLE);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        counrtriesList.setLayoutManager(mLayoutManager);
        counrtriesList.setLayoutManager(new GridLayoutManager(mContext, numberOfColumns));

        mmProgressBar = (ProgressBar) findViewById(R.id.mProgressBarImages);
        mClear_btn_search_view.setOnClickListener(view -> {

            if (autoCompView == null) {

            } else {
                clearSearchView();
            }

        });

      /*  mClose_btn_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        initializerSearchView(autoCompView, mClear_btn_search_view);


    }

    public void initializerSearchView(EditText searchInput, ImageView clearSearchBtn) {
        searchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                clearSearchBtn.setVisibility(View.VISIBLE);
                //countriesAdapter.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                clearSearchBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                citySearchConnection(s.toString().trim());
                clearSearchBtn.setVisibility(View.VISIBLE);

            }


        });
    }


    private void setToolbar() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.toolbar.tvTitle.setText("Address or City");
    }

    private void citySearchConnection(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (CommonUtils.isOnline(mContext)) {
                try {
                    /*ProgressDialogUtils.show(mContext);*/
                    AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                    webServices.searchCity(text).enqueue(new BaseCallback<CityResponse>(ActivityCitySearch.this) {
                        @Override
                        public void onSuccess(CityResponse response) {
                            if (response != null) {
                                if (response.getStatus() == 1) {
                                    ArrayList<CityContentList> contentList = response.getResult().getContentList();
                                    countriesAdapter = new AdapterCitySearch(ActivityCitySearch.this, contentList);
                                    counrtriesList.setAdapter(countriesAdapter);

                                }
                            }

                            /*ProgressDialogUtils.dismiss();*/
                        }

                        @Override
                        public void onFail(Call<CityResponse> call, BaseResponse baseResponse) {
                            /*ProgressDialogUtils.dismiss();*/
                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            /*ProgressDialogUtils.dismiss();*/
        }
    }

    public void clearSearchView() {
        if (autoCompView.getText() != null) {
            autoCompView.setText("");

            ArrayList<CityContentList> list = new ArrayList<CityContentList>() {
            };
            countriesAdapter.setCountries(list);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }

    }
}
