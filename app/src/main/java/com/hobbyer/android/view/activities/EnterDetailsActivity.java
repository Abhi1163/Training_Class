/*
package com.hobbyer.android.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hobbyer.android.R;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.CommonUtils;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterDetailsActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private Toolbar toolbar;
    private TextView tvHeader;
    private EditText etExpiryDate, etName;
    private Context mContext;
    private TextView tvSubmit;
    private EditText etCardNumber, etCvvNumber, etPostalCode, etEmail;
    private TextView tvErrCardNumber, tvErrCvvNumber, tvErrPostalCode, tvErrEmail;
    private Spinner spMonth, spYear;
    private TextView tvErrExpiryDate, tvErrName;
    private CardInputWidget cardDetail;
    private Dialog mDialog;
    private PrefManager prefManager;
    private String amount;
    private List<ApiResponse.CartBean> cartList;
    private List<VoucherDetail> voucherDetails;
    private TwoDigitsCardTextWatcher twoDigitsCardTextWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterdetails);
        getIDs();
        initializedControl();
        setToolbar();
        setListeners();
        setSpinner();
    }

    private void setSpinner() {
        String month[] = {"MM", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        ArrayList<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("YYYY");
        for (int i = thisYear; i <= 2060; i++) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.layout_spinner, R.id.tvSpinner, month);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(mContext, R.layout.layout_spinner, R.id.tvSpinner, years);

//        spMonth.setAdapter(arrayAdapter);
//        spYear.setAdapter(arrayAdapter1);
//        spMonth.setSelection(0);
//        spYear.setSelection(0);
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // toolbar.setNavigationIcon(R.mipmap.n4);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initializedControl() {
        mContext = EnterDetailsActivity.this;
        tvHeader.setText("Card Details");
        prefManager = PrefManager.getInstance(mContext);
        voucherDetails = new ArrayList<>();
        if (getIntent().getStringExtra(AppConstant.PAYMENTFROM) != null) {
            if (getIntent().getStringExtra(AppConstant.PAYMENTFROM).equalsIgnoreCase("AddCard")) {
                tvSubmit.setText(getString(R.string.addCard));
            } else {
                if (getIntent().getStringExtra(AppConstant.PayableAmount) != null) {

                    PayCardModel payCardModel = (PayCardModel) getIntent().getSerializableExtra(AppConstant.CARD_DETAILS);

                    etCardNumber.setText(payCardModel.getCardRealNumber());
                    etExpiryDate.setText(payCardModel.getCardExpiryDate());
                    etName.setText(payCardModel.getCardName());
                    etPostalCode.setText(payCardModel.getCardPostalCode());

                    amount = getIntent().getStringExtra(AppConstant.PayableAmount);
                    tvSubmit.setText(getString(R.string.pay_dollor) + " " + CommonUtils.convertCurrency(amount));
                    if (getIntent().getStringExtra("quantity") != null && getIntent().getStringExtra("voucherID") != null) {
                        VoucherDetail voucherDetail = new VoucherDetail();
                        String quantity = getIntent().getStringExtra("quantity");
                        String voucherID = getIntent().getStringExtra("voucherID");
                        voucherDetail.quantity = quantity;
                        voucherDetail.voucherID = voucherID;
                        voucherDetails.add(voucherDetail);
                    }
                    if (getIntent().getSerializableExtra("cartList") != null) {
                        cartList = (List<ApiResponse.CartBean>) getIntent().getSerializableExtra("cartList");
                        for (int i = 0; i < cartList.size(); i++) {
                            VoucherDetail voucherDetail = new VoucherDetail();
                            voucherDetail.voucherID = cartList.get(i).voucherID;
                            voucherDetail.quantity = cartList.get(i).quantity;
                            voucherDetails.add(voucherDetail);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvPaymentSubmit:
                checkValidation();
                break;
        }
    }

    private boolean isValidExpiryDate(String s) {
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

    private void checkValidation() {
        String cardNumber = etCardNumber.getText().toString();
        String cvvNumber = etCvvNumber.getText().toString();
        String emailID = etEmail.getText().toString();
        String postalCode = etPostalCode.getText().toString();
        if (cardNumber.isEmpty() || cardNumber.length() < 16) {
            tvErrCardNumber.setVisibility(View.VISIBLE);
            etCardNumber.requestFocus();
        }
else if (spMonth.getSelectedItem().toString().equalsIgnoreCase(spMonth.getItemAtPosition(0).toString())) {
            tvErrExpiryDate.setText(getResources().getString(R.string.sleectamonth));
            tvErrExpiryDate.setVisibility(View.VISIBLE);
        }
 else if (etExpiryDate.getText().toString().isEmpty()) {
            tvErrExpiryDate.setText(getResources().getString(R.string.enter_expiry_date));
            tvErrExpiryDate.setVisibility(View.VISIBLE);
            etExpiryDate.requestFocus();
        } else if (!isValidExpiryDate(etExpiryDate.getText().toString().trim())) {
            tvErrExpiryDate.setText(getResources().getString(R.string.enter_valid_expiry_date));
            tvErrExpiryDate.setVisibility(View.VISIBLE);
            etExpiryDate.requestFocus();
        } else if (cvvNumber.isEmpty() || cvvNumber.length() < 3) {
            tvErrCvvNumber.setText(getResources().getString(R.string.entercvvnumber));
            tvErrCvvNumber.setVisibility(View.VISIBLE);
            etCvvNumber.requestFocus();
        }
//        else if (emailID.isEmpty() ) {
//            tvErrEmail.setText(getResources().getString(R.string.enteremailid));
//            tvErrEmail.setVisibility(View.VISIBLE);
//            etEmail.requestFocus();
//        }else if(!Util.isValidEmail(emailID)){
//            tvErrEmail.setVisibility(View.VISIBLE);
//            etEmail.requestFocus();
//            tvErrEmail.setText(getResources().getString(R.string.pleaseenteremailid));
//        }
        else if (postalCode.isEmpty()) {
            tvErrPostalCode.setText(getResources().getString(R.string.enterpostalcode));
            tvErrPostalCode.setVisibility(View.VISIBLE);
            etPostalCode.requestFocus();
        } else if (!checkPostalValidation(postalCode) || postalCode.length()!= 6) {
            tvErrPostalCode.setText("Please enter in format A1A1A1");
            tvErrPostalCode.setVisibility(View.VISIBLE);
            etPostalCode.requestFocus();
        } else if (etName.getText().toString().isEmpty()) {
            tvErrName.setText(getResources().getString(R.string.enter_name));
            tvErrName.setVisibility(View.VISIBLE);
            etName.requestFocus();
        } else {
//            if (getIntent().getStringExtra(AppConstant.PAYMENTFROM) != null) {
//                if (getIntent().getStringExtra(AppConstant.PAYMENTFROM).equalsIgnoreCase("AddCard")) {
//
//                    // For insert Card Detail into database
//                    ExcampleDBHelper excampleDBHelper = new ExcampleDBHelper(mContext);
//                    excampleDBHelper.insertPerson(prefManager.getUserDetail().userID, cardNumber,
//                            etExpiryDate.getText().toString().trim(), postalCode, etname.getText().toString().trim());
//                    finish();
//                } else {

            String[] expDate = etExpiryDate.getText().toString().trim().split("/");
            Card card = new Card(cardNumber, Integer.valueOf(expDate[0]), Integer.valueOf(expDate[1]), cvvNumber,
                    etName.getText().toString().trim(), emailID, "", "", "", postalCode, "", "");
            mDialog = CommonUtils.CustomDialog(mContext);
            mDialog.show();
            String key = "pk_test_lYnBcZaF9EekBGmtB92uXENN";
            Stripe stripe = new Stripe(mContext, key);
            stripe.createToken(card, key, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    mDialog.dismiss();
                    Toast.makeText(mContext, "unable to create token", Toast.LENGTH_SHORT).show();
                    CommonUtils.showSnakbar(tvHeader, error.getLocalizedMessage());
                }

                @Override
                public void onSuccess(Token token) {
                    Log.e("card id", "" + token.getId());
                    Log.e("card getCard", "" + token.getCard());
                    Log.e("card getType", "" + token.getType());
                    Log.e("card getBankAccount", "" + token.getBankAccount());
                    Log.e("card getUsed", "" + token.getUsed());

                    callAddCardApi(token);
                }
            });
            // }
            //  }
        }
    }

    public boolean checkPostalValidation(String postalCode) {
        boolean proceed = false;
        char[] charArray = postalCode.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (i % 2 == 0) {
                if (Util.isChar(String.valueOf(charArray[i]))) {
                    proceed = true;
                } else {
                    proceed = false;
                    break;
                }
            } else {
                proceed = Character.isDigit(charArray[i]);
            }
        }
        return proceed;
    }


    private void callAddCardApi(Token token) {
//        double stripeAmount = 0;
//        try {
//            stripeAmount = Double.parseDouble(amount) * 100;
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
        Gson gson = new Gson();
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.userID = prefManager.getUserDetail().userID;
        apiRequest.token = token.getId();
//        apiRequest.amount = "" + stripeAmount;
//        apiRequest.voucherDetails = voucherDetails;
        apiRequest.sessionID = prefManager.getUserDetail() != null ? prefManager.getUserDetail().sessionID : "";
        JsonObject request = (JsonObject) gson.toJsonTree(apiRequest);
        APIExecutor.getClient().addCardApi(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                mDialog.dismiss();
                if (response != null && response.body() != null && response.body().responseCode != null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        Toast.makeText(mContext, response.body().responseMessage, Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(mContext, HomeActivity.class).putExtra(AppConstant.PAYMENTDETAIL, AppConstant.PAYMENTDETAIL));
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                        // startActivity(new Intent(mContext,PurchasedSuccessfullyActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else if (response.body().responseCode.equalsIgnoreCase("401")) {
                        CommonUtils.showToast(mContext, response.body().responseMessage);
                        CommonUtils.sessionExpired(mContext);
                    } else {
                        mDialog.dismiss();
                        CommonUtils.showSnakbar(tvHeader, response.body().responseMessage);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mContext, getString(R.string.server_not_respoonding), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tvErrCardNumber.setVisibility(View.GONE);
        tvErrCvvNumber.setVisibility(View.GONE);
        tvErrEmail.setVisibility(View.GONE);
        tvErrPostalCode.setVisibility(View.GONE);
        tvErrExpiryDate.setVisibility(View.GONE);
        tvErrName.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void getIDs() {
//        spMonth = (Spinner) findViewById(R.id.spMonth);
//        spYear = (Spinner) findViewById(R.id.spYear);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etExpiryDate = (EditText) findViewById(R.id.etExpiryDate);
        etName = (EditText) findViewById(R.id.etName);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvSubmit = (TextView) findViewById(R.id.tvPaymentSubmit);
        etCardNumber = (EditText) findViewById(R.id.etCardNumber);
        etCvvNumber = (EditText) findViewById(R.id.etCvvNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPostalCode = (EditText) findViewById(R.id.etPostalCode);
        tvErrCardNumber = (TextView) findViewById(R.id.tvErrCardNumber);
        tvErrCvvNumber = (TextView) findViewById(R.id.tvErrCvvNumber);
        tvErrEmail = (TextView) findViewById(R.id.tvErrEmail);
        tvErrPostalCode = (TextView) findViewById(R.id.tvErrPostalCode);
        tvErrExpiryDate = (TextView) findViewById(R.id.tvErrExpiryDate);
        tvErrName = (TextView) findViewById(R.id.tvErrName);

        cardDetail = (CardInputWidget) findViewById(R.id.cardDetail);
    }

    @Override
    public void setListeners() {
        tvSubmit.setOnClickListener(this);
        etCardNumber.addTextChangedListener(this);
        etCvvNumber.addTextChangedListener(this);
        etEmail.addTextChangedListener(this);
        etPostalCode.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        twoDigitsCardTextWatcher = new TwoDigitsCardTextWatcher(etExpiryDate, tvErrExpiryDate, tvErrCardNumber, tvErrCvvNumber, tvErrEmail, tvErrPostalCode, tvErrName);
        etExpiryDate.addTextChangedListener(twoDigitsCardTextWatcher);


    }
}
*/
