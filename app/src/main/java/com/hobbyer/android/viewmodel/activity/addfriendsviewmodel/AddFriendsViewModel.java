package com.hobbyer.android.viewmodel.activity.addfriendsviewmodel;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.SearchFriendViewRequest;
import com.hobbyer.android.api.request.SendFriendRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.searchfriendlist.SearchFriendViewResponse;
import com.hobbyer.android.api.response.auth.searchfriendlist.SearchFriendViewResponseContentList;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.interfaces.AddFriendItemClickListners;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.view.activities.addfriend.AddByNameActivity;
import com.hobbyer.android.view.adapter.AddFriendListAdapter;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AddFriendsViewModel extends ActivityViewModel<AddByNameActivity> {
    public ArrayList<SearchFriendViewResponseContentList> friendsList;
    AddFriendListAdapter adapter;
    private AddByNameActivity activity;
    private RecyclerView mFriendRecyclerView;
    private ImageView imBack;
    private EditText mEtSearchArea;
    private int isFb;

    public AddFriendsViewModel(AddByNameActivity activity) {
        super(activity);
        this.activity = activity;
        getIntent();
        mFriendRecyclerView = (RecyclerView) activity.findViewById(R.id.rv_addfriend);

        imBack = (ImageView) activity.findViewById(R.id.imBack);
        mEtSearchArea = (EditText) activity.findViewById(R.id.etSearchArea);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEtSearchArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                seacrhFriends(s.toString());

//                filter(s.toString());
//
//                if (noDataFound.getVisibility() == View.VISIBLE) {
//                    mFriendRecyclerView.setVisibility(View.INVISIBLE);
//                } else {
//                    mFriendRecyclerView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void getIntent() {
        Bundle i = activity.getIntent().getExtras();
        isFb = i.getInt(("isFrom"));


    }

    private void sendFriendRequest(int postion, SearchFriendViewResponseContentList data) {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                SendFriendRequest homeRequest = new SendFriendRequest();
                homeRequest.setSender_id(userModel.getId() + "");
                homeRequest.setReciever_id(data.getId() + "");

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);


                webServices.sendFriendRequest(homeRequest).enqueue(new BaseCallback<BaseResponse>(activity) {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getStatus() == 1) {

                            data.setAlready_friend(1.0);

                            adapter.notifyItemChanged(postion, data);

                        } else {
                            Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    private void seacrhFriends(String userName) {

        friendsList = new ArrayList<>();

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                SearchFriendViewRequest homeRequest = new SearchFriendViewRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setKeyword(userName);
                if (isFb == 1) {
                    homeRequest.setFacebookSearch(isFb);
                } else {
                    homeRequest.setFacebookSearch(0);
                }
                homeRequest.setPage_number(1);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.searchFriendView(homeRequest).enqueue(new BaseCallback<SearchFriendViewResponse>(activity) {
                    @Override
                    public void onSuccess(SearchFriendViewResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                Log.e("RESPONSE", response + "");

                                friendsList = response.getResult().getContentList();

                                displayFriendsList();

                            } else {

                                displayFriendsList();

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<SearchFriendViewResponse> call, BaseResponse baseResponse) {

                        displayFriendsList();

                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    public void displayFriendsList() {
        adapter = new AddFriendListAdapter(activity, friendsList, new AddFriendItemClickListners() {

            @Override
            public void onAddfriendClick(int postion, List<SearchFriendViewResponseContentList> listData) {

                SearchFriendViewResponseContentList data = listData.get(postion);

                sendFriendRequest(postion, data);

            }
        });
        mFriendRecyclerView.setHasFixedSize(true);
        mFriendRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mFriendRecyclerView.setAdapter(adapter);
    }
}
