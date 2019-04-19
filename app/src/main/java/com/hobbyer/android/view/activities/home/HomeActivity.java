package com.hobbyer.android.view.activities.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.hobbyer.android.R;
import com.hobbyer.android.TopRatingStudiosFragment;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.LogoutRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.ActivityHomeBinding;
import com.hobbyer.android.databinding.LogoutDialogBinding;
import com.hobbyer.android.model.ProfileImageModel;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.view.fragments.browservideo.FragmentBrowserVideo;
import com.hobbyer.android.view.fragments.findaclass.FragmentFindAClass;
import com.hobbyer.android.view.fragments.home.HomeFragment;
import com.hobbyer.android.view.fragments.profile.ProfileFragment;
import com.hobbyer.android.view.fragments.refer.FragmentRefer;
import com.hobbyer.android.view.fragments.setting.SettingsFragment;
import com.hobbyer.android.view.fragments.upcoming.UpComingFragment;
import com.hobbyer.android.widget.CustomDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawer;
    Bundle intent;
    Fragment fragment;
    View headerView;
    private ActivityHomeBinding binding;
    private TextView btYes, btNo;
    private NavigationView navigationView;
    private Context mContext;
    private CustomDialog dialog;
    private String date;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mContext = HomeActivity.this;
     //   getIntentData();
        fragment = new HomeFragment();
        CommonUtils.setFragment(fragment, true, this, R.id.flHome);
      /*  getProfile();*/
        setListener();
        Fabric.with(this, new Crashlytics());
        logUser();

    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppConstant.DATE)) {
                date = bundle.getString(AppConstant.DATE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment = getSupportFragmentManager().findFragmentById(R.id.flHome);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userProfile();
    }


    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("developernirva@gmail.com");
        Crashlytics.setUserName("saddam");
    }

    private void logOut() {
        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                LogoutRequest logout = new LogoutRequest();
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }

                logout.setUserId("" + userModel.getId());
                logout.setDeviceToken("tok");
                logout.setDeviceType(1);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.logoutUser(logout).enqueue(new BaseCallback<BaseResponse>(HomeActivity.this) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                BaseResponse changePasswordResult = response;
                                LoginManager.getInstance().logOut();
                                PreferenceUtils.deleteUserModel();
                                PreferenceUtils.deleteProfile();

                                PreferenceUtils.deleteCard();
                                Intent m = new Intent(mContext, LogInActivity.class);
                                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(m);
                                finish();
                            } else {
                                ToastUtils.showToastLong(mContext, "" + response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        //ToastUtils.showToastLong(mContext, " ....... : ");
                    }

                });

            } catch (Exception e) {

            }
        } else {
            ToastUtils.showToastShort(HomeActivity.this, "Please check your internet.");
        }
    }



    public void showAlertLogOut() {

        dialog = new CustomDialog(mContext, R.layout.logout_popup);
        btYes = (TextView) dialog.findViewById(R.id.btYes);
        btNo = (TextView) dialog.findViewById(R.id.btNo);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                dialog.dismiss();
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setListener() {
        binding.mHome.setOnClickListener(this);
        binding.mFindClass.setOnClickListener(this);
        binding.mVideo.setOnClickListener(this);
        binding.mTTopRatingClass.setOnClickListener(this);
        binding.mUpcoming.setOnClickListener(this);
        binding.mProfile.setOnClickListener(this);
        binding.mReferEarn.setOnClickListener(this);
        binding.mSetting.setOnClickListener(this);
        binding.mLogOut.setOnClickListener(this);
        binding.imUserProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        binding.drawerMainLayout.closeDrawer(Gravity.START);
        switch (v.getId()) {
            case R.id.mHome:
                fragment = new HomeFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mFindClass:
                fragment = new FragmentFindAClass();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mVideo:
                fragment = new FragmentBrowserVideo();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mTTopRatingClass:
                fragment = new TopRatingStudiosFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mUpcoming:
                fragment = new UpComingFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mProfile:
                fragment = new ProfileFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mReferEarn:
                fragment = new FragmentRefer();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mSetting:
                fragment = new SettingsFragment();

                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mLogOut:
                showAlertLogOut();
                break;
            case R.id.imUserProfile:
                fragment = new ProfileFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            default:
                break;

        }

    }


    @Override
    public void onBackPressed() {
        fragment = getSupportFragmentManager().findFragmentById(R.id.flHome);
        if (fragment instanceof HomeFragment /*|| fragment instanceof OffersFragment || fragment instanceof NotificationsFragment
|| fragment instanceof KYCFragment || fragment instanceof WalletFragment || fragment instanceof SettingsFragment || fragment instanceof ProfileFragment*/) {
            exitDialog();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                fragment = new HomeFragment();

                CommonUtils.setFragment(fragment, true, this, R.id.flHome);            } else {
                fragment = null;
                super.onBackPressed();
            }
        }
    }


    private void exitDialog() {
        LogoutDialogBinding logoutDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.logout_dialog, null, false);
        Dialog dialog = DialogUtils.createDialog(mContext, logoutDialogBinding.getRoot());
        logoutDialogBinding.tvNo.setOnClickListener(v -> dialog.dismiss());
        logoutDialogBinding.tvYes.setOnClickListener(v -> finishAffinity());

    }


    /*public void getProfile() {
        String img = SharedPref.getStringPreferences(mContext, AppConstant.SAVE__PROFILE);
        if (img!=null) {
            Glide.with(mContext).load(img).error(R.drawable.ic_avtar).placeholder(R.drawable.ic_avtar).into(binding.imUserProfile);
        }}*/





    public void userProfile() {
      //  UserModel userModel = PreferenceUtils.getUserModel();
        ProfileImageModel userModel = PreferenceUtils.getProfile();
        if(userModel != null && !TextUtils.isEmpty(userModel.getFirstName()) && !TextUtils.isEmpty(userModel.getLastName())) {
            binding.tvUserName.setText(userModel.getFirstName() + " "+ userModel.getLastName());
        }

        if (userModel.getSocialId() != null && !userModel.getSocialId().equalsIgnoreCase("")) {
            Glide.with(this).load("https://graph.facebook.com/" + userModel.getSocialId() + "/picture?type=large").fitCenter().into(binding.imUserProfile);
            Picasso.with(mContext).load("https://graph.facebook.com/" + userModel.getSocialId() + "/picture?type=large").placeholder(R.mipmap.img_568656).fit().into(binding.imUserProfile, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        else {

        }
        if (userModel.getUserImage() != null && !userModel.getUserImage().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(userModel.getUserImage()).placeholder(R.mipmap.img_568656).fit().into(binding.imUserProfile, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        } else {
            binding.imUserProfile.setImageResource(R.mipmap.img_568656);

        }

    }


}



















/*
package com.hobbyer.android.view.activities.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.LogoutRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.savefile.SaveFileData;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.ActivityHomeBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.view.fragments.browservideo.FragmentBrowserVideo;
import com.hobbyer.android.view.fragments.findaclass.FragmentFindAClass;
import com.hobbyer.android.view.fragments.home.HomeFragment;
import com.hobbyer.android.view.fragments.profile.ProfileFragment;
import com.hobbyer.android.view.fragments.refer.FragmentRefer;
import com.hobbyer.android.view.fragments.setting.SettingsFragment;
import com.hobbyer.android.view.fragments.upcoming.UpComingFragment;
import com.hobbyer.android.widget.CustomDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stripe.android.RequestOptions;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawer;
    Bundle intent;
    Fragment fragment;
    View headerView;
    private ActivityHomeBinding binding;
    private TextView btYes, btNo;
    private NavigationView navigationView;
    private Context mContext;
    private SaveFileData saveFileData;
    private CustomDialog dialog;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        mContext = HomeActivity.this;

        //getProfile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(HomeActivity.this, mContext.getResources().getColor(R.color.colorPrimary));
        }

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();



        fragment = new HomeFragment();
        CommonUtils.setFragment(fragment, true, this, bundle, R.id.flHome);
        binding.mHome.setBackgroundColor(getResources().getColor(R.color.yellow));

        //binding.imUserProfile.setImageURI(Uri.parse(saveFileData.getUrl()));

        setListener();
        Fabric.with(this, new Crashlytics());
        logUser();

    }

    public void getProfile() {
        String img = SharedPref.getStringPreferences(mContext, AppConstant.SAVE__PROFILE);
        if (!img.equals("")  &&img!=null) {
            Glide.with(mContext).load(img).error(R.drawable.ic_avtar).placeholder(R.drawable.ic_avtar).into(binding.imUserProfile);

    */
/*    Glide.with(mContext).load(img).into(binding.imUserProfile);
               else {
            binding.imUserProfile.setImageResource(R.drawable.ic_avtar);
        }*//*

        }
        else {
        }
        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.flHome);
            if (fragment != null) {

                fragment.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        */
/*getProfile();*//*

        userProfile();
    }

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_Main_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                FragmentManager fm = getSupportFragmentManager();
                for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
                    String stackName = fm.getBackStackEntryAt(entry).getName();
                    System.out.println(stackName);
                }
                System.out.println(fm.getBackStackEntryCount());
                if (fm.getBackStackEntryCount() > 0) {
                    if (fm.getBackStackEntryCount() == 1) {
                        fm.popBackStack();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.setCustomAnimations(0, 0);
                        mFragmentTransaction.add(R.id.flHome, new HomeFragment()).commit();
                        */
/*toolbar.setTitle("Home");*//*

                    } else {
                        fm.popBackStack();
                    }
                } else {
                    */
/*toolbar.setTitle("Home");*//*

                    super.onBackPressed();
                }
                */
/*toolbar.setTitle("Home");*//*

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        */
/*toolbar.setTitle("Home");*//*

    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("developernirva@gmail.com");
        Crashlytics.setUserName("saddam");
    }

    private void logOut() {
        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                LogoutRequest logout = new LogoutRequest();
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }

                logout.setUserId("" + userModel.getId());
                logout.setDeviceToken("tok");
                logout.setDeviceType(1);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.logoutUser(logout).enqueue(new BaseCallback<BaseResponse>(HomeActivity.this) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                BaseResponse changePasswordResult = response;
                             //   LoginManager.getInstance().logOut();
                               PreferenceUtils.deleteUserModel();


                                Intent m = new Intent(mContext, LogInActivity.class);
                                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                              //  SharedPref.removeFromPreference(AppConstant.SAVE__PROFILE);

                                startActivity(m);
                                finish();
                            } else {
                                ToastUtils.showToastLong(mContext, "" + response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        //ToastUtils.showToastLong(mContext, " ....... : ");
                    }

                });

            } catch (Exception e) {

            }
        } else {
            ToastUtils.showToastShort(HomeActivity.this, "Please check your internet.");
        }
    }


    public void showAlertLogOut() {

        dialog = new CustomDialog(mContext, R.layout.logout_popup);
        btYes = (TextView) dialog.findViewById(R.id.btYes);
        btNo = (TextView) dialog.findViewById(R.id.btNo);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                dialog.dismiss();
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setListener() {
        binding.mHome.setOnClickListener(this);
        binding.mFindClass.setOnClickListener(this);
        binding.mVideo.setOnClickListener(this);
        binding.mTopRatingClass.setOnClickListener(this);
        binding.mUpcoming.setOnClickListener(this);
        binding.mProfile.setOnClickListener(this);
        binding.mReferEarn.setOnClickListener(this);
        binding.mSetting.setOnClickListener(this);
        binding.mLogOut.setOnClickListener(this);
        binding.imUserProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        binding.drawerMainLayout.closeDrawer(Gravity.START);
        switch (v.getId()) {
            case R.id.mHome:
                setITemSelection(v);
                fragment = new HomeFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mFindClass:
                setITemSelection(v);
                fragment = new FragmentFindAClass();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mVideo:
                setITemSelection(v);
                fragment = new FragmentBrowserVideo();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mTopRatingClass:
                setITemSelection(v);
                ToastUtils.showToastShort(this, "coming soon");
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mUpcoming:
                setITemSelection(v);
                fragment = new UpComingFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mProfile:
                setITemSelection(v);
                fragment = new ProfileFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mReferEarn:
                setITemSelection(v);
                fragment = new FragmentRefer();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mSetting:
                setITemSelection(v);
                fragment = new SettingsFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            case R.id.mLogOut:
                setITemSelection(v);
                showAlertLogOut();
                break;
            case R.id.imUserProfile:
                setITemSelection(v);
                fragment = new ProfileFragment();
                CommonUtils.setFragment(fragment, true, this, R.id.flHome);
                break;
            default:
                break;

        }

    }

    public void userProfile() {
        UserModel userModel = PreferenceUtils.getUserModel();
        if (userModel != null && !TextUtils.isEmpty(userModel.getFirstName()) && !TextUtils.isEmpty(userModel.getLastName())) {
            binding.tvUserName.setText(userModel.getFirstName() + " " + userModel.getLastName());
        }

        if (userModel.getSocialId() != null && !userModel.getSocialId().equalsIgnoreCase("")) {
            Glide.with(this).load("https://graph.facebook.com/" + userModel.getSocialId() + "/picture?type=large").fitCenter().into(binding.imUserProfile);
            Picasso.with(mContext).load("https://graph.facebook.com/" + userModel.getSocialId() + "/picture?type=large").placeholder(R.drawable.ic_avtar).fit().into(binding.imUserProfile, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }

    }

    public void setBackgroundColor() {
        binding.mHome.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mFindClass.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mProfile.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mLogOut.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mReferEarn.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mSetting.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mTopRatingClass.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mUpcoming.setBackgroundColor(getResources().getColor(R.color.white));
        binding.mVideo.setBackgroundColor(getResources().getColor(R.color.white));

    }

    private void setITemSelection(View view) {
        switch (view.getId()) {
            case R.id.mHome:
                setBackgroundColor();
                binding.mHome.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mFindClass:
                setBackgroundColor();
                binding.mFindClass.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mProfile:
                setBackgroundColor();
                binding.mProfile.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mLogOut:
                setBackgroundColor();
                binding.mLogOut.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mReferEarn:
                setBackgroundColor();
                binding.mReferEarn.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mSetting:
                setBackgroundColor();
                binding.mSetting.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mTopRatingClass:
                setBackgroundColor();
                binding.mTopRatingClass.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mUpcoming:
                setBackgroundColor();
                binding.mUpcoming.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case R.id.mVideo:
                setBackgroundColor();
                binding.mVideo.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            default:
                setBackgroundColor();
                break;
        }
    }

   */
/* public void Shoe(){

            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.logout_popup);
        btYes=(Button)dialog.findViewById(R.id.btYes);
        btNo=(Button)dialog.findViewById(R.id.btNo);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




            dialog.show();

        }
*//*



}*/
