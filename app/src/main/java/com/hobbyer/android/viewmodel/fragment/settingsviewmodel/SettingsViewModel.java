package com.hobbyer.android.viewmodel.fragment.settingsviewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.FragmentSettingsBinding;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.view.activities.addpoint.AddPointsActivity;
import com.hobbyer.android.view.activities.billing.BillingActivity;
import com.hobbyer.android.view.activities.contactus.ContactUsActivity;
import com.hobbyer.android.view.activities.editprofile.EditProfileActivity;
import com.hobbyer.android.view.activities.faq.ActivityFAQ;
import com.hobbyer.android.view.activities.manageplan.ManagePlanActivity;
import com.hobbyer.android.view.activities.privacysetting.PrivacySettings;
import com.hobbyer.android.view.activities.recentcharges.RecentCharges;
import com.hobbyer.android.view.activities.termandcondition.TermsAndConditionsActivity;
import com.hobbyer.android.view.fragments.base.FragmentViewModel;
import com.hobbyer.android.view.fragments.setting.SettingsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SettingsViewModel extends FragmentViewModel<SettingsFragment, FragmentSettingsBinding> {

    private Activity activity;
    private SettingsFragment fragment;
    private Context mContext;
    private TextView txName, txEmail;
    private TextView tvTitle, tvRight;
    private Date dateTime;
    private String date="";

    private ImageView ivBack, ivSearch, ivLocation;


    private DrawerLayout drawerLayout;

    public SettingsViewModel(SettingsFragment fragment, FragmentSettingsBinding binding) {
        super(fragment);
        this.fragment = fragment;
        this.activity = getActivity();
        this.fragment = fragment;

        init();

        setToolbar();

        txName = (TextView) activity.findViewById(R.id.txName);
        txEmail = (TextView) activity.findViewById(R.id.txEmail);

        date = PrefManager.getPreferencesString(activity, AppConstant.DATE);

        if (date==null || date.equalsIgnoreCase("0")) {
            fragment.getBinding().tvPlanDate.setText("No plan selected" );
        } else {


            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");
            SimpleDateFormat month_date = new SimpleDateFormat("dd MMMM", Locale.ENGLISH);
            String actualDate = date;
            try {
                dateTime = sdf.parse(actualDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String month_name = month_date.format(dateTime);
            System.out.println("Month :" + month_name);
            fragment.getBinding().tvPlanDate.setText("Next cycle beings " + month_name);
        }




        setProfile();
    }


    @Override
    protected void initialize(FragmentSettingsBinding binding) {

    }

    private void init() {
        ivBack = getActivity().findViewById(R.id.ivBack);
        ivLocation = getActivity().findViewById(R.id.ivLocation);
        ivSearch = getActivity().findViewById(R.id.ivSearch);
        tvTitle = getActivity().findViewById(R.id.tvTitle);
        tvRight = getActivity().findViewById(R.id.tvRight);
        drawerLayout = getActivity().findViewById(R.id.drawer_Main_layout);
    }

    private void setToolbar() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setImageResource(R.mipmap.ic_menu);
        tvRight.setVisibility(View.INVISIBLE);
        ivLocation.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.setting);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.llTermsAndConditions:
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.TITLE_NAME, AppConstant.TERMS_AND_CONDITIONS_TITLE);
                bundle.putString(AppConstant.URL_KEY, AppConstant.TERMS_AND_CONDITIONS_URL);
                Intent intent = new Intent(fragment.getActivity(), TermsAndConditionsActivity.class);
                intent.putExtras(bundle);
                fragment.getActivity().startActivity(intent);
                break;
            case R.id.llPrivacyPolicy:
                Bundle bundlePrivacyPolicy = new Bundle();
                bundlePrivacyPolicy.putString(AppConstant.TITLE_NAME, AppConstant.POLICY_TITLE_NAME);
                bundlePrivacyPolicy.putString(AppConstant.URL_KEY, AppConstant.PRIVACY_POLICY_URL);
                Intent intentPrivacyPolicy = new Intent(fragment.getActivity(), TermsAndConditionsActivity.class);
                intentPrivacyPolicy.putExtras(bundlePrivacyPolicy);
                fragment.getActivity().startActivity(intentPrivacyPolicy);
                break;
            case R.id.llHelpCenter:
                Bundle bundleHelpCenter = new Bundle();
                bundleHelpCenter.putString(AppConstant.TITLE_NAME, AppConstant.HELP_TITLE_NAME);
                bundleHelpCenter.putString(AppConstant.URL_KEY, AppConstant.HELP_CENTER_URL);
                Intent intentHelpCenter = new Intent(fragment.getActivity(), TermsAndConditionsActivity.class);
                intentHelpCenter.putExtras(bundleHelpCenter);
                fragment.getActivity().startActivity(intentHelpCenter);
                break;
            case R.id.llCommunity:
                Bundle bundleCommunity = new Bundle();
                bundleCommunity.putString(AppConstant.TITLE_NAME, AppConstant.COMMUNITY_GUIDE_NAME);
                bundleCommunity.putString(AppConstant.URL_KEY, AppConstant.COMMUNITY_GUIDE_URL);
                Intent intentCommunity = new Intent(fragment.getActivity(), TermsAndConditionsActivity.class);
                intentCommunity.putExtras(bundleCommunity);
                fragment.getActivity().startActivity(intentCommunity);
                break;
            case R.id.llFAQ:
                Intent intentFAQ = new Intent(fragment.getActivity(), ActivityFAQ.class);
                intentFAQ.putExtras(intentFAQ);
                fragment.getActivity().startActivity(intentFAQ);
                break;
            case R.id.llAccount1:
                Intent intentAccount = new Intent(fragment.getActivity(), EditProfileActivity.class);
                fragment.getActivity().startActivity(intentAccount);
                break;
            case R.id.llContactUs:
                Intent intent3 = new Intent(fragment.getActivity(), ContactUsActivity.class);
                fragment.getActivity().startActivity(intent3);
                break;
            case R.id.llAddPoint:
                Intent intentAddPoint = new Intent(fragment.getActivity(), AddPointsActivity.class);
                fragment.getActivity().startActivity(intentAddPoint);
                break;
            case R.id.llManagePlan:

               /* Bundle bundle1 = new Bundle();
                bundle1.putString(AppConstant.BUNDLE_KEY.OTP, "setting");*/
                //   ActivityController.startActivityForResult(activity, HomeActivity.class, bundle,true);
                Intent intentManagePlan = new Intent(fragment.getActivity(), ManagePlanActivity.class);
                fragment.getActivity().startActivity(intentManagePlan);
                break;
            case R.id.llManageBilling:

                Bundle bundleSetting = new Bundle();
                bundleSetting.putString(AppConstant.BUNDLE_KEY.SettingBill, "setting");
                //   ActivityController.startActivityForResult(activity, HomeActivity.class, bundle,true);
                ActivityController.startActivity(fragment.getActivity(), BillingActivity.class, bundleSetting, false);

              /*  Intent intentManageBilling = new Intent(fragment.getActivity(),BillingActivity.class);
                fragment.getActivity().startActivity(intentManageBilling);*/
                break;
            case R.id.llSeeRecentCharges:
                Intent intentSeeRecentCharges = new Intent(fragment.getActivity(), RecentCharges.class);
                fragment.getActivity().startActivity(intentSeeRecentCharges);
                break;

            case R.id.llManagePrivacy:
                Intent intentManagePrivacy = new Intent(fragment.getActivity(), PrivacySettings.class);
                fragment.getActivity().startActivity(intentManagePrivacy);
                break;

        }
    }

    private void setProfile() {
        UserModel userModel = PreferenceUtils.getUserModel();
        if (userModel != null && !TextUtils.isEmpty(userModel.getFirstName()) && !TextUtils.isEmpty(userModel.getLastName()) && !TextUtils.isEmpty(userModel.getEmail())) {
            txName.setText(userModel.getFirstName() + "  " + userModel.getLastName());
            txEmail.setText(userModel.getEmail());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfile();
    }
}
