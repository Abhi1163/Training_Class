package com.hobbyer.android.view.fragments.profile;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.AcceptFriendRequest;
import com.hobbyer.android.api.request.BlockFriendRequest;
import com.hobbyer.android.api.request.BlockFriendViewRequest;
import com.hobbyer.android.api.request.CancelFriendRequest;
import com.hobbyer.android.api.request.ConfirmFriendViewRequest;
import com.hobbyer.android.api.request.GetUserFriendViewRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.acceptfriendrequest.ConfirmFriendViewResponse;
import com.hobbyer.android.api.response.auth.acceptfriendrequest.ConfirmFriendViewResponseContentList;
import com.hobbyer.android.api.response.auth.blockfriendlist.BlockFriendViewResponse;
import com.hobbyer.android.api.response.auth.blockfriendlist.BlockFriendViewResponseContentList;
import com.hobbyer.android.api.response.auth.getfriends.GetUserFriendViewResponse;
import com.hobbyer.android.api.response.auth.getfriends.GetUserFriendViewResponseContentList;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.FragmentFriendBinding;
import com.hobbyer.android.interfaces.OnConfirmFriendItemClickListners;
import com.hobbyer.android.interfaces.OnFriendItemClickListners;
import com.hobbyer.android.interfaces.OnUnblockFriendItemClickListners;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.view.activities.addfriendfrom.AddFriendActivity;
import com.hobbyer.android.view.adapter.BlockUserListAdapter;
import com.hobbyer.android.view.adapter.FriendListAdapter;
import com.hobbyer.android.view.adapter.UserListAdapter;

import java.util.ArrayList;

import retrofit2.Call;

public class FriendFragment extends Fragment implements View.OnClickListener{

    private View rootView;

    private android.support.v4.app.Fragment fragment;

    private TextView mFriends, mBlockUser,tvNoFiend;
    private Activity activity;
    private ProgressBar progress_bar;
    private RecyclerView  mBlockRecyclerView, mRequestListRecyclerView;
    private LinearLayout mLayAddFriend;
private FragmentFriendBinding binding;
    private FriendListAdapter friendListAdapter;
    private BlockUserListAdapter blockUserListAdapter;
    private UserListAdapter userListAdapter;

    public ArrayList<GetUserFriendViewResponseContentList> friendsList;
    public ArrayList<ConfirmFriendViewResponseContentList> confirmFriendsList;
    public ArrayList<BlockFriendViewResponseContentList> blockFriendsList;

    private int pastVisiblesItemsConfirm, visibleItemCountConfirm, totalItemCountConfirm;
    private int pastVisiblesItemsFriend, visibleItemCountFriend, totalItemCountFriend;
    private int pastVisiblesItemsBlock, visibleItemCountBlock, totalItemCountBlock;

    private boolean loadingForConfirmList = true, loadingForFriendsList = true, loadingForBlockList = true;
    private int pageCountConfirm = 1, pageCountFriends = 1, pageCountBlock = 1;
    private LinearLayoutManager mFriendsLayoutManager, mBlockLayoutManager, mConfirmLayoutManager;

    boolean clickOnBlock = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.activity = getActivity();

        fragment = new android.support.v4.app.Fragment();

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_friend, container, false);


        binding.progressBar.setVisibility(View.VISIBLE);
        binding.ivFriendPro.setOnClickListener(this);
        binding.ivBlockUser.setOnClickListener(this);

         /*   mFriends.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                }
            });

        mBlockUser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                BlockTab();
            }
        });*/
        binding.layAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHelpCenter = new Intent(getActivity(), AddFriendActivity.class);
                activity.startActivity(intentHelpCenter);
            }
        });


        binding.progressBar.setVisibility(View.GONE);

        getConfirmFriends();

        getFriends();

        getBlockFriends();


        binding.friendRequestRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCountConfirm = mConfirmLayoutManager.getChildCount();
                    totalItemCountConfirm = mConfirmLayoutManager.getItemCount();
                    pastVisiblesItemsConfirm = mConfirmLayoutManager.findFirstVisibleItemPosition();

                    if (loadingForConfirmList) {
                        if ((visibleItemCountConfirm + pastVisiblesItemsConfirm) >= totalItemCountConfirm) {
                            loadingForConfirmList = false;

                            pageCountConfirm = pageCountConfirm + 1;

                            loadMoreConfirmFriends();
                        }
                    }
                }
            }
        });

        binding.friendRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCountFriend = mFriendsLayoutManager.getChildCount();
                    totalItemCountFriend = mFriendsLayoutManager.getItemCount();
                    pastVisiblesItemsFriend = mFriendsLayoutManager.findFirstVisibleItemPosition();

                    if (loadingForFriendsList) {
                        if ((visibleItemCountFriend + pastVisiblesItemsFriend) >= totalItemCountFriend) {
                            loadingForFriendsList = false;

                            pageCountFriends = pageCountFriends + 1;

                            loadMoregetFriends();
                        }
                    }
                }
            }
        });


        binding.blockRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCountBlock = mBlockLayoutManager.getChildCount();
                    totalItemCountBlock = mBlockLayoutManager.getItemCount();
                    pastVisiblesItemsBlock = mBlockLayoutManager.findFirstVisibleItemPosition();

                    if (loadingForBlockList) {
                        if ((visibleItemCountBlock + pastVisiblesItemsBlock) >= totalItemCountBlock) {
                            loadingForBlockList = false;

                            pageCountBlock = pageCountBlock + 1;

                            loadMoreBlockFriendList();
                        }
                    }
                }
            }
        });

        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void FriendsTab() {

       /* mBlockUser.setBackgroundResource(R.color.white);
        mBlockUser.setTextColor(activity.getResources().getColor(R.color.black));

        mFriends.setBackgroundResource(R.color.colorPrimary);
        mFriends.setVisibility(View.VISIBLE);
        mFriends.setTextColor(activity.getResources().getColor(R.color.black));*/


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void BlockTab() {
       /* mFriends.setBackgroundResource(R.color.white);
        mFriends.setTextColor(activity.getResources().getColor(R.color.black));

        mBlockUser.setBackgroundResource(R.color.colorPrimary);
        mBlockUser.setVisibility(View.VISIBLE);
        mBlockUser.setTextColor(activity.getResources().getColor(R.color.black));*/



    }



    //    get Friends List
    private void getFriends() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                GetUserFriendViewRequest homeRequest = new GetUserFriendViewRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setPage_number(1);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.getUserFriendView(homeRequest).enqueue(new BaseCallback<GetUserFriendViewResponse>(fragment) {
                    @Override
                    public void onSuccess(GetUserFriendViewResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                Log.e("RESPONSE", response + "");

                                friendsList = response.getResult().getContentList();

                                if (friendsList.size() == 0) {

                                    binding.friendRecyclerView.setVisibility(View.GONE);
                                    binding.tvFriend.setVisibility(View.VISIBLE);
                                } else {
                                    binding.friendRecyclerView.setVisibility(View.VISIBLE);
                                    binding.tvFriend.setVisibility(View.GONE);

                                    displayFriendsList();
                                }

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<GetUserFriendViewResponse> call, BaseResponse baseResponse) {
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

    private void loadMoregetFriends() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                GetUserFriendViewRequest homeRequest = new GetUserFriendViewRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setPage_number(pageCountFriends);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.getUserFriendView(homeRequest).enqueue(new BaseCallback<GetUserFriendViewResponse>(fragment) {
                    @Override
                    public void onSuccess(GetUserFriendViewResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                Log.e("RESPONSE", response + "");

                                loadingForFriendsList = true;

                                ArrayList<GetUserFriendViewResponseContentList> contentLists = response.getResult().getContentList();

                                friendsList.addAll(contentLists);

                                friendListAdapter.notifyDataSetChanged();

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<GetUserFriendViewResponse> call, BaseResponse baseResponse) {
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



    //    get block friends List
    private void getBlockFriends() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                BlockFriendViewRequest homeRequest = new BlockFriendViewRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setPage_number(1);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.getBlockUserFriendView(homeRequest).enqueue(new BaseCallback<BlockFriendViewResponse>(fragment) {
                    @Override
                    public void onSuccess(BlockFriendViewResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                Log.e("RESPONSE", response + "");

                                blockFriendsList = response.getResult().getContentList();

                                if (blockFriendsList.size() == 0) {
                                    binding.blockRecyclerView.setVisibility(View.GONE);
                                } else {

                                    if (clickOnBlock) {
                                        binding.blockRecyclerView.setVisibility(View.VISIBLE);
                                        displayBlockFriendList();
                                    }
                                }
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<BlockFriendViewResponse> call, BaseResponse baseResponse) {
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

    public void loadMoreBlockFriendList() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                BlockFriendViewRequest homeRequest = new BlockFriendViewRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setPage_number(pageCountBlock);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.getBlockUserFriendView(homeRequest).enqueue(new BaseCallback<BlockFriendViewResponse>(fragment) {
                    @Override
                    public void onSuccess(BlockFriendViewResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                Log.e("RESPONSE", response + "");

                                loadingForBlockList = true;

                                ArrayList<BlockFriendViewResponseContentList> contentLists = response.getResult().getContentList();

                                blockFriendsList.addAll(contentLists);

                                blockUserListAdapter.notifyDataSetChanged();

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<BlockFriendViewResponse> call, BaseResponse baseResponse) {
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


    //    get friend request List
    private void getConfirmFriends() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                ConfirmFriendViewRequest homeRequest = new ConfirmFriendViewRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setPage_number(pageCountConfirm);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.confirmFriendView(homeRequest).enqueue(new BaseCallback<ConfirmFriendViewResponse>(fragment) {
                    @Override
                    public void onSuccess(ConfirmFriendViewResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                Log.e("RESPONSE", response + "");

                                confirmFriendsList = response.getResult().getContentList();

                                if (confirmFriendsList.size() == 0) {
                                    binding.friendRequestRecyclerView.setVisibility(View.GONE);
                                } else {
                                    binding.friendRequestRecyclerView.setVisibility(View.VISIBLE);

                                    displayConfirmFriend();
                                }

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ConfirmFriendViewResponse> call, BaseResponse baseResponse) {
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

    private void loadMoreConfirmFriends() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                ConfirmFriendViewRequest homeRequest = new ConfirmFriendViewRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setPage_number(pageCountConfirm);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.confirmFriendView(homeRequest).enqueue(new BaseCallback<ConfirmFriendViewResponse>(fragment) {
                    @Override
                    public void onSuccess(ConfirmFriendViewResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                loadingForFriendsList = true;

                                ArrayList<ConfirmFriendViewResponseContentList> confirmUserFriendsList = response.getResult().getContentList();

                                confirmFriendsList.addAll(confirmUserFriendsList);

                                userListAdapter.notifyDataSetChanged();

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ConfirmFriendViewResponse> call, BaseResponse baseResponse) {
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




    public void displayBlockFriendList() {

        mBlockLayoutManager = new LinearLayoutManager(activity);

        blockUserListAdapter = new BlockUserListAdapter(activity, blockFriendsList, new OnUnblockFriendItemClickListners() {
            @Override
            public void onUnBlock(int postion, BlockFriendViewResponseContentList listData) {
                UnfriendFriendList(postion, blockFriendsList);
            }
        });
        binding.blockRecyclerView.setHasFixedSize(true);
        binding.blockRecyclerView.setLayoutManager(mBlockLayoutManager);
        binding.blockRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.blockRecyclerView.setAdapter(blockUserListAdapter);
    }

    public void displayConfirmFriend() {

        mConfirmLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

        userListAdapter = new UserListAdapter(activity, confirmFriendsList, new OnConfirmFriendItemClickListners() {
            @Override
            public void onIgnore(int postion, ConfirmFriendViewResponseContentList listData) {
                cancelConfirmFriendList(postion, confirmFriendsList);
            }

            @Override
            public void onBlock(int postion, ConfirmFriendViewResponseContentList listData) {
                blockConfirmFriendList(postion, confirmFriendsList);
            }

            @Override
            public void onAccept(int postion, ConfirmFriendViewResponseContentList listData) {
                acceptFriendRequest(postion, confirmFriendsList);
            }
        });

        binding.friendRequestRecyclerView.setHasFixedSize(true);
        binding.friendRequestRecyclerView.setLayoutManager(mConfirmLayoutManager);
        binding.friendRequestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.friendRequestRecyclerView.setAdapter(userListAdapter);
    }

    public void displayFriendsList() {

        mFriendsLayoutManager = new LinearLayoutManager(activity);

        friendListAdapter = new FriendListAdapter(activity, friendsList, new OnFriendItemClickListners() {
            @Override
            public void onUnfriendClick(int postion, GetUserFriendViewResponseContentList listData) {
                cancelFriendList(postion, friendsList);
            }

            @Override
            public void onBlockClick(int postion, GetUserFriendViewResponseContentList listData) {
                blockFriendList(postion, friendsList);
            }
        });
        binding.friendRecyclerView.setHasFixedSize(true);
        binding.friendRecyclerView.setLayoutManager(mFriendsLayoutManager);
        binding.friendRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.friendRecyclerView.setAdapter(friendListAdapter);
    }



//    Take Action apis call

    // Block Request

    private void blockFriendList(int postion, ArrayList<GetUserFriendViewResponseContentList> friendsList) {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                BlockFriendRequest homeRequest = new BlockFriendRequest();
                homeRequest.setSender_id(userModel.getId() + "");
                homeRequest.setReciever_id(friendsList.get(postion).getId() + "");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);


                webServices.blockFriend(homeRequest).enqueue(new BaseCallback<BaseResponse>(fragment) {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getStatus() == 1) {

                            friendsList.remove(postion);
                            friendListAdapter.notifyItemRemoved(postion);
                            friendListAdapter.notifyDataSetChanged();

                            if (friendsList.size() == 0) {
                                binding.friendRecyclerView.setVisibility(View.GONE);
                            } else {
                                binding.friendRecyclerView.setVisibility(View.VISIBLE);
                                displayFriendsList();
                            }

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

    private void blockConfirmFriendList(int postion, ArrayList<ConfirmFriendViewResponseContentList> confirmFriendsList) {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                BlockFriendRequest homeRequest = new BlockFriendRequest();
                homeRequest.setSender_id(userModel.getId() + "");
                homeRequest.setReciever_id(confirmFriendsList.get(postion).getId() + "");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);


                webServices.blockFriend(homeRequest).enqueue(new BaseCallback<BaseResponse>(fragment) {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getStatus() == 1) {

                            confirmFriendsList.remove(postion);
                            userListAdapter.notifyItemRemoved(postion);
                            userListAdapter.notifyDataSetChanged();

                            if (confirmFriendsList.size() == 0) {
                                binding.friendRequestRecyclerView.setVisibility(View.GONE);
                            } else {
                                binding.friendRequestRecyclerView.setVisibility(View.VISIBLE);
                            }

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


    // Accept Request

    private void acceptFriendRequest(int postion, ArrayList<ConfirmFriendViewResponseContentList> confirmFriendsList) {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                AcceptFriendRequest homeRequest = new AcceptFriendRequest();
                homeRequest.setReciever_id(userModel.getId() + "");
                homeRequest.setSender_id(confirmFriendsList.get(postion).getId() + "");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);


                webServices.acceptFriend(homeRequest).enqueue(new BaseCallback<BaseResponse>(fragment) {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getStatus() == 1) {

                            confirmFriendsList.remove(postion);
                            userListAdapter.notifyItemRemoved(postion);
                            userListAdapter.notifyDataSetChanged();

                            if (confirmFriendsList.size() == 0) {
                                binding.friendRequestRecyclerView.setVisibility(View.GONE);
                            } else {
                                binding.friendRequestRecyclerView.setVisibility(View.VISIBLE);
                            }


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


    // Unfrined request

    private void cancelConfirmFriendList(int postion, ArrayList<ConfirmFriendViewResponseContentList> confirmFriendsList) {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                CancelFriendRequest homeRequest = new CancelFriendRequest();
                homeRequest.setSender_id(userModel.getId() + "");
                homeRequest.setReciever_id(confirmFriendsList.get(postion).getId() + "");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);


                webServices.cancelFriend(homeRequest).enqueue(new BaseCallback<BaseResponse>(fragment) {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getStatus() == 1) {

                            confirmFriendsList.remove(postion);
                            userListAdapter.notifyItemRemoved(postion);
                            userListAdapter.notifyDataSetChanged();

                            if (confirmFriendsList.size() == 0) {
                                binding.friendRequestRecyclerView.setVisibility(View.GONE);
                            } else {
                                binding.friendRequestRecyclerView.setVisibility(View.VISIBLE);
                            }

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

    private void cancelFriendList(int postion, ArrayList<GetUserFriendViewResponseContentList> friendsList) {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                CancelFriendRequest homeRequest = new CancelFriendRequest();
                homeRequest.setSender_id(userModel.getId() + "");
                homeRequest.setReciever_id(friendsList.get(postion).getId() + "");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);


                webServices.cancelFriend(homeRequest).enqueue(new BaseCallback<BaseResponse>(fragment) {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getStatus() == 1) {

                            friendsList.remove(postion);
                            friendListAdapter.notifyItemRemoved(postion);
                            friendListAdapter.notifyDataSetChanged();

                            if (friendsList.size() == 0) {
                                binding.friendRecyclerView.setVisibility(View.GONE);
                            } else {
                                binding.friendRecyclerView.setVisibility(View.VISIBLE);
                            }

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

    private void UnfriendFriendList(int postion, ArrayList<BlockFriendViewResponseContentList> blockFriendsList) {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                CancelFriendRequest homeRequest = new CancelFriendRequest();
                homeRequest.setSender_id(userModel.getId() + "");
                homeRequest.setReciever_id(blockFriendsList.get(postion).getId() + "");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);


                webServices.cancelFriend(homeRequest).enqueue(new BaseCallback<BaseResponse>(fragment) {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getStatus() == 1) {

                            blockFriendsList.remove(postion);
                            blockUserListAdapter.notifyItemRemoved(postion);
                            blockUserListAdapter.notifyDataSetChanged();

                            if (blockFriendsList.size() == 0) {
                                binding.blockRecyclerView.setVisibility(View.GONE);
                            } else {
                                binding.blockRecyclerView.setVisibility(View.VISIBLE);
                            }

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ivFriendPro:

                binding.ivFriendPro.setBackgroundResource(R.color.colorPrimary);
                binding.ivFriendPro.setVisibility(View.VISIBLE);
                binding.ivFriendPro.setTextColor(activity.getResources().getColor(R.color.black));
                binding.ivBlockUser.setBackgroundResource(R.color.white);
                binding.ivBlockUser.setTextColor(activity.getResources().getColor(R.color.black));

                binding.friendRecyclerView.setVisibility(View.VISIBLE);
                binding.blockRecyclerView.setVisibility(View.GONE);

                getFriends();

                break;


            case R.id.ivBlockUser:


                binding.ivBlockUser.setBackgroundResource(R.color.colorPrimary);
                binding.ivBlockUser.setVisibility(View.VISIBLE);
                binding.ivBlockUser.setTextColor(activity.getResources().getColor(R.color.black));
                binding.ivFriendPro.setBackgroundResource(R.color.white);
                binding.ivFriendPro.setTextColor(activity.getResources().getColor(R.color.black));
                binding.friendRecyclerView.setVisibility(View.GONE);
                binding.blockRecyclerView.setVisibility(View.VISIBLE);


                getBlockFriends();
                break;
        }

    }
}
