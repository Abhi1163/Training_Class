package com.hobbyer.android.view.activities.billing;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.SaveCardRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanData;
import com.hobbyer.android.api.response.getcarddetail.GetCardDataResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.ActivityBillingBinding;
import com.hobbyer.android.model.CardDataModel;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CardValidation;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.CreditCardExpiryTextWatcher;
import com.hobbyer.android.utils.CreditCardFormattingTextWatcher;
import com.hobbyer.android.utils.CreditCardUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.freetrial.ActivityFreeTrial;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.view.activities.manageplan.ManagePlanActivity;
import com.hobbyer.android.view.activities.membership.ChooseMembershipActivity;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

import static com.hobbyer.android.constant.AppConstant.BUNDLE_KEY.SettingBill;

public class BillingActivity extends AppCompatActivity implements View.OnClickListener {

    String namepattern = "[a-zA-z]+";
    String cardpattern = "[0-9]+";
    String cvvpattern = "[0-9]+";
    int keyDel;
    String a;
    private Button changes;
    private CardDataModel userModel = PreferenceUtils.getCardModel();
    private Bundle bundle;
    private EditText cardName, cardNumber, cvv;
    private ActivityBillingBinding binding;
    private Context context;
    private String transactionId;
    private ManagePlanData managePlanData;

    private String email;
    private String bundleStr, bundleMon, bundleMemeber, bundleCurrent, bundleFree,bundleFreeTrial,bundleGetMembership,membershipType;
    private String from;
    private int bundleMembershipId;
    private int isCurrent,membershipId;

    private String cardNumberStr, cvvNumber, expiryName, postalCode, cardNameStr;
    private ProgressDialog progressDialog;
    private String list;
    private List<ManagePlanData> managePlanDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing);
        context = BillingActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(this, context.getResources().getColor(R.color.colorPrimary));
        }

        getDataIntent();

        context = BillingActivity.this;
        binding.btnSave.setOnClickListener(this);
        cardName = (EditText) findViewById(R.id.et_name);

        //    getCardApi();


        binding.etExpirary.addTextChangedListener(new CreditCardExpiryTextWatcher(binding.etExpirary));
        binding.etExpirary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.tvExpiratonError.setVisibility(View.GONE);


            }
        });
       // binding.etCard.addTextChangedListener(new CardValidation(binding.etCard));

          binding.etCard.addTextChangedListener(new CreditCardFormattingTextWatcher(binding.etCard));

    }


    private void getDataIntent() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle.getString(AppConstant.BUNDLE_KEY.OTP) != null) {
                bundleStr = bundle.getString(AppConstant.BUNDLE_KEY.OTP);
            } else if (bundle.getString(AppConstant.BUNDLE_KEY.SettingBill) != null) {
                bundleMon = bundle.getString(SettingBill);
            } else if (bundle.getString(AppConstant.BUNDLE_KEY.FREE) != null) {
                bundleFree = bundle.getString(AppConstant.BUNDLE_KEY.FREE);
            }



            if (bundleStr != null && bundleStr.equals("otp")) {
                from = "otp";
                binding.tvBilling.setText("Billing Information");
                binding.tvAddCard.setText("Add Card");
                binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
                setToolbar2();
                binding.btnSave.setText(getString(R.string.save));
                binding.toolbar.tvTitle.setText("Payment Method");
                binding.btnSave.setText(getString(R.string.saveandconti));

            }



            else if ((bundleMon != null && bundleMon.equals("setting"))) {
                from = "setting";
                binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
                binding.toolbar.tvTitle.setText("Manage Billing Information");
                binding.btnSave.setText(getString(R.string.savechnge));
                setToolbar();
                getCardApi();

                setDataInfo();

            }
            else if (bundleFree!=null && bundleFree.equals("free"))
            {

                membershipId = bundle.getInt(AppConstant.BUNDLE_KEY.Membership_ID);
                membershipType = bundle.getString(AppConstant.BUNDLE_KEY.Membership_TYPE);

                from="free";
                binding.tvBilling.setText("Billing Information");
                binding.tvAddCard.setText("Add Card");
                binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
                setToolbar2();
                binding.btnSave.setText(getString(R.string.save));
                binding.toolbar.tvTitle.setText("Payment Method");
                binding.btnSave.setText(getString(R.string.saveandconti));
                getCardApi();


            }
        }


    }


    private void setDataInfo() {

        if (userModel != null && !TextUtils.isEmpty(userModel.getCardName()) && !TextUtils.isEmpty(userModel.getCardNumber()) && !TextUtils.isEmpty(userModel.getExpiry())) {
            binding.etName.setText(userModel.getCardName());
            binding.etCard.setText(userModel.getCardNumber());
            binding.etBillingPostal.setText(userModel.getEmail());
            binding.etExpirary.setText(userModel.getExpiry());
            binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
            binding.toolbar.tvTitle.setText("Manage Billing Information");
            binding.btnSave.setText(R.string.savechnge);
        }

    }

    private void getCardApi() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);


                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", userModel.getId());

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.getCard(jsonObject).enqueue(new BaseCallback<GetCardDataResponse>((AppCompatActivity) context) {


                    @Override
                    public void onSuccess(GetCardDataResponse response) {
                        if (response != null) {

                            if (response.getStatus() == 1) {
                                if (response.getResult()!=null) {
                                    String card = response.getResult().getData().getName();
                                    String cardNumber = ("xxxx-xxxx-xxxx" + String.valueOf(response.getResult().getData().getCardNumber()));
                                    binding.etName.setText(card);
                                    // binding.etCard.setText(no);
                                    binding.etCard.setText(cardNumber);
                                    String expiryDate = response.getResult().getData().getExpMonth() + "/" + response.getResult().getData().getExp_year();
                                    binding.etExpirary.setText(expiryDate);
                                    ToastUtils.showToastShort(context, response.getMessage());


                                }
                                else
                                    {


                                }
                            }else {
                                ToastUtils.showToastShort(context, context.getResources().getString(R.string.something_went));

                            }
                        } else {
                            ToastUtils.showToastLong(context, " " + response.getMessage());

                        }
                        ProgressDialogUtils.dismiss();


                    }


                    @Override
                    public void onFail(Call<GetCardDataResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(context, " " + baseResponse.getMessage());

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private void setToolbar() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.tvTitle.setText("Manage Billing Information");
        binding.toolbar.ivBack.setOnClickListener(this);
    }

    private void setToolbar2() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.tvTitle.setText("Payment Method");
        binding.toolbar.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                if (from.equals("setting"))
                {
                    finish();
                }
                else {
                    finish();


/*
                    ActivityController.startActivity((Activity) context, HomeActivity.class, true);
*/
                }
                break;

            case R.id.btnSave:
                checkValidation();
                break;
        }

    }



    private void checkValidation() {
        clearError();

        cardNumberStr = binding.etCard.getText().toString();
        cvvNumber = binding.etCvv.getText().toString();
        cardNameStr = binding.etName.getText().toString();
        expiryName = binding.etExpirary.getText().toString();
        postalCode = binding.etBillingPostal.getText().toString();

        if (binding.etName.getText().toString().isEmpty()) {
            binding.tvNameError.setVisibility(View.VISIBLE);
            binding.tvNameError.setText("*Please enter name.");
            binding.etName.requestFocus();

        } else if (binding.etName.getText().toString().length() < 2) {
            binding.tvNameError.setVisibility(View.VISIBLE);
            binding.tvNameError.setText("*Please enter valid name.");
            binding.etName.requestFocus();

        } else if (cardNumberStr.isEmpty() || cardNumberStr.length() < 16) {
            binding.tvCardNumberError.setVisibility(View.VISIBLE);
            binding.tvCardNumberError.setText("*Please enter card number");
            binding.etCard.requestFocus();
        }/* else if (spMonth.getSelectedItem().toString().equalsIgnoreCase(spMonth.getItemAtPosition(0).toString())) {
            tvErrExpiryDate.setText(getResources().getString(R.string.sleectamonth));
            tvErrExpiryDate.setVisibility(View.VISIBLE);
        }*/ else if (binding.etExpirary.getText().toString().isEmpty()) {
            binding.tvExpiratonError.setText("*Please enter expiry date");
            binding.tvExpiratonError.setVisibility(View.VISIBLE);
            binding.etExpirary.requestFocus();
        } else if (!CreditCardUtils.isValidDate(binding.etExpirary.getText().toString().trim())) {
            binding.tvExpiratonError.setText(getResources().getString(R.string.enter_valid_expiry_date));
            binding.tvExpiratonError.setVisibility(View.VISIBLE);
            binding.etExpirary.requestFocus();
        } else if (cvvNumber.isEmpty() || cvvNumber.length() < 3) {
            binding.tvCvvError.setText("*Please enter cvv number");
            binding.tvCvvError.setVisibility(View.VISIBLE);
            binding.etCvv.requestFocus();
        } else if (postalCode.isEmpty()) {
            binding.tvPostalError.setText("*Please enter postal code.");
            binding.tvPostalError.setVisibility(View.VISIBLE);
            binding.etBillingPostal.requestFocus();
        } /*else if (!checkPostalValidation(postalCode) || postalCode.length()!= 6) {
            binding.tvPostalError.setText("Please enter in format A1A1A1");
            binding.tvPostalError.setVisibility(View.VISIBLE);
            binding.etBillingPostal.requestFocus();
        }*/ else {

            String[] expDate = binding.etExpirary.getText().toString().trim().split("/");
            Card card = new Card(cardNumberStr, Integer.valueOf(expDate[0]), Integer.valueOf(expDate[1]), cvvNumber,
                    binding.etName.getText().toString().trim(), "", "", "", "", postalCode, "", "");
            ProgressDialogUtils.show(context);
            String key = "pk_test_bLDaOJwrMgsBCWL9FTZC1PfW";
            Stripe stripe = new Stripe(context, key);
            stripe.createToken(card, key, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    ProgressDialogUtils.dismiss();
                    Toast.makeText(context, "unable to create token", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(Token token) {
                    ProgressDialogUtils.dismiss();
                    Log.e("card id", "" + token.getId());
                    Log.e("card getCard", "" + token.getCard());
                    Log.e("card getType", "" + token.getType());
                    Log.e("card getBankAccount", "" + token.getBankAccount());
                    Log.e("card getUsed", "" + token.getUsed());
                    transactionId = token.getId();
                    if (transactionId!=null) {
                        saveCardData();
                    }


                }
            });

        }


    }

    private void clearError() {

        binding.tvNameError.setVisibility(View.GONE);
        binding.tvCvvError.setVisibility(View.GONE);
        binding.tvCardNumberError.setVisibility(View.GONE);
        binding.tvExpiratonError.setVisibility(View.GONE);
    }

    public boolean checkPostalValidation(String postalCode) {
        boolean proceed = false;
        char[] charArray = postalCode.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (i % 2 == 0) {
               /* if (Util.isChar(String.valueOf(charArray[i]))) {
                    proceed = true;
                } else {
                    proceed = false;
                    break;
                }*/
            } else {
                proceed = Character.isDigit(charArray[i]);
            }
        }
        return proceed;
    }

    private void saveCardData() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }

                SaveCardRequest saveCardRequest = new SaveCardRequest();
                saveCardRequest.setStripeToken(transactionId);
                saveCardRequest.setUserId("" + userModel.getId());
                saveCardRequest.setUserEmail("" + userModel.getEmail());


                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.saveCard(saveCardRequest).enqueue(new BaseCallback<BaseResponse>((AppCompatActivity) context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                CardDataModel userModel = creatData();
                                PreferenceUtils.saveCardDetail(userModel);

                                if (from.equalsIgnoreCase("setting")) {
                                    getCardApi();
                                } else if (from.equalsIgnoreCase("start")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString(AppConstant.BUNDLE_KEY.USER_EMAIL, email);
                                    bundle.putString("membership", "membership");
                                    ActivityController.startActivityForResult(BillingActivity.this, ManagePlanActivity.class, bundle, true);


                                }

                                else if (from.equalsIgnoreCase("free"))
                                {

                                    Intent i = new Intent(BillingActivity.this, ActivityFreeTrial.class);
                                    i.putExtra(AppConstant.MEMBERSHIP_ID, membershipId);
                                    startActivity(i);
                                  //  Bundle bundle=new Bundle();
                                 //  bundle.putInt(AppConstant.MEMBERSHIP_ID,membershipId);

                                  //  ActivityController.startActivity(BillingActivity.this, ActivityFreeTrial.class,bundle,true);
                                }
                                else {

                                    Bundle bundle = new Bundle();
                                    bundle.putString(AppConstant.BUNDLE_KEY.USER_EMAIL, email);
                                    bundle.putString("BillingMember", "BillingMember");
                                    ActivityController.startActivityForResult(BillingActivity.this, ManagePlanActivity.class, bundle, true);
                                    //  ActivityController.startActivity(context, HomeActivity.class);
                                }


                            }
                        }
                        ProgressDialogUtils.dismiss();

                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(context, " " + baseResponse.getMessage());

                    }


                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidExpiryDate(String s) {

        //   return s.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        try {
            Date date = format.parse(s);
            if (date.getTime() > Calendar.getInstance().getTimeInMillis()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean validate() {

        if (!(cardName.getText().toString().matches(namepattern))) {
            return false;
        }
        if (!(cardNumber.getText().toString().matches(cardpattern)) || (cardNumber.length() < 16)) {
            return false;
        }
        if (!(cvv.getText().toString().matches(cardpattern)) || (cardNumber.length() < 3)) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from.equals("setting"))
        {
      //      ActivityController.startActivity((Activity) context, HomeActivity.class, true);

            finish();
        }
        else {

           // ActivityController.startActivity((Activity) context, HomeActivity.class, true);
        }
    }

    private CardDataModel creatData() {
        CardDataModel userModel = new CardDataModel();
        userModel.setCardNumber(cardNumberStr);
        userModel.setCvv(cvvNumber);
        System.out.println(cvvNumber);
        System.out.println(cardNumberStr);
        System.out.println(expiryName);
        userModel.setCardName(cardNameStr);
        userModel.setExpiry(expiryName);

        return userModel;
    }

}
