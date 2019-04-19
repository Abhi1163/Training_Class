package com.hobbyer.android.view.fragments.refer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.hobbyer.android.api.request.ContactUsRequest;
import com.hobbyer.android.api.request.ReferEarnRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.referearn.ReferEarnData;
import com.hobbyer.android.api.response.auth.referearn.ReferEarnResponse;
import com.hobbyer.android.api.response.auth.signup.SignUpData;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.ActivityReferBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;

import java.util.Calendar;
import java.util.Objects;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import retrofit2.Call;

public class FragmentRefer extends Fragment implements View.OnClickListener{

    private Context mContext;
    ReferEarnData referEarn;
    LinkProperties linkProperties;
    BranchUniversalObject branchUniversalObject;
    ShareSheetStyle ss;
    private TextView tvTitle,tvRight;
    private ImageView ivBack,ivSearch,ivLocation;

    private DrawerLayout drawerLayout;
    private ActivityReferBinding binding;
    private String mSmsShareLink="https://hobbyertest.app.link?%24identity_id=625696885633607574";
    public FragmentRefer() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_refer, container, false);
        mContext=getActivity();
        init();
        setToolbar();
        referEarn = PreferenceUtils.getReferEarn();
        referEarnConnection();
        setOnclickListner();


        branchUniversalObject  = new BranchUniversalObject()
                .setCanonicalIdentifier("/1234")
                .setTitle("Test for alias")
                .setContentDescription("Your friend has invited you to check out my app!")
                .setContentImageUrl("")
                .addContentMetadata("var1", "abc")
                .addContentMetadata("var2", "def");

        linkProperties = new LinkProperties()
                .setChannel("all")
                .setFeature("Sharing")
                .setAlias("aliastest");




        return binding.getRoot();
    }

    private void setOnclickListner() {
        binding.ivWattsapp.setOnClickListener(this);
        binding.ivSkype.setOnClickListener(this);
        binding.ivInsta.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
    }

    private void referEarnConnection() {
        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                ReferEarnRequest contactUs = new ReferEarnRequest();
                contactUs.setUser_id("" + userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.refer_Earn(contactUs).enqueue(new BaseCallback<ReferEarnResponse>((AppCompatActivity) mContext) {
                    @Override
                    public void onSuccess(ReferEarnResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ReferEarnData referEarnData = response.getRequest().getData();
                                ReferEarnData EarnModel = createReferEarn(referEarnData);
                                PreferenceUtils.saveReferEarn(EarnModel);
                             //   ToastUtils.showToastLong(mContext, "" + referEarnData.getRefer_earn_code());

                            } else {
                                ToastUtils.showToastLong(mContext, "" + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ReferEarnResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(mContext, "" + baseResponse.getMessage());
                    }

                });


            } catch (Exception e) {

            }
        }else {
            Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

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
        tvTitle.setText(R.string.refer);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    public void referEarnShare(String shareTitle) {
        if (referEarn == null) {
            return;
        }
        if (shareTitle.equalsIgnoreCase("whatsApp")) {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, mSmsShareLink + " " + referEarn.getRefer_earn_code());
            //whatsappIntent.putExtra(Intent.EXTRA_TEXT, referEarn.getRefer_earn_code());
            try {
                Objects.requireNonNull(getActivity()).startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
            }
        }
        else  if (shareTitle.equalsIgnoreCase("facebook")) {
            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
            facebookIntent.setType("text/plain");
            facebookIntent.setPackage("com.facebook.katana");
            facebookIntent.putExtra(Intent.EXTRA_TEXT, "Application of social rating share with your friend");
            facebookIntent.putExtra(Intent.EXTRA_TEXT,mSmsShareLink + " " + referEarn.getRefer_earn_code());
            try {
                Objects.requireNonNull(getActivity()).startActivity(facebookIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/")));
            }
        }
        else  if (shareTitle.equalsIgnoreCase("skype")) {
            Intent skypeIntent = new Intent(Intent.ACTION_SEND);
            skypeIntent.setType("text/plain");
            //skypeIntent.setAction(Intent.ACTION_SEND);
           /* skypeIntent.setPackage("com.skype.raider");*/
            skypeIntent.putExtra(Intent.EXTRA_TEXT, "Application of social rating share with your friend");
            skypeIntent.putExtra(Intent.EXTRA_TEXT,mSmsShareLink + " " + referEarn.getRefer_earn_code());
            try {
                Objects.requireNonNull(getActivity()).startActivity(skypeIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.skype.com")));
            }
        }
        else if(shareTitle.equalsIgnoreCase("gmail")) {
            Intent gmailIntent = new Intent(Intent.ACTION_SEND);
            gmailIntent.setType("text/plain");
            gmailIntent.setPackage("com.google.android.gm");
            gmailIntent.putExtra(Intent.EXTRA_TEXT, "Application of social rating share with your friend");
            gmailIntent.putExtra(Intent.EXTRA_TEXT,mSmsShareLink + " " + referEarn.getRefer_earn_code());
            try {
                Objects.requireNonNull(getActivity()).startActivity(gmailIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("an-saddam@mobiloitte.com")));
            }
        }
        else  if (shareTitle.equalsIgnoreCase("instagram")) {
            Intent instagramIntent = new Intent(Intent.ACTION_SEND);
            instagramIntent.setType("text/plain");
            instagramIntent.setPackage("com.instagram.android");
            instagramIntent.putExtra(Intent.EXTRA_TEXT, "Application of social rating share with your friend");
            instagramIntent.putExtra(Intent.EXTRA_TEXT, mSmsShareLink + " " +referEarn.getRefer_earn_code());
            try {
                Objects.requireNonNull(getActivity()).startActivity(instagramIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/_u/bitter_truth_lol")));
            }

        }


    }




    private ReferEarnData createReferEarn(ReferEarnData referEarn) {
        ReferEarnData refer = new ReferEarnData();
        refer.setRefer_earn_code(referEarn.getRefer_earn_code());
        return refer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_skype:
            referEarnShare("skype");

                break;
            case R.id.iv_wattsapp:
                referEarnShare("whatsApp");
                break;
            case R.id.iv_insta:
                referEarnShare("instagram");

                break;
            case R.id.iv_fb:
                referEarnShare("facebook");

                break;
            case R.id.iv_google:
                referEarnShare("gmail");

                break;
        }

    }
}
