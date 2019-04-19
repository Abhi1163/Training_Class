package com.hobbyer.android.viewmodel.activity.editprofileviewmodel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.EditProfileRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countrylist.CountryContentList;
import com.hobbyer.android.api.response.auth.countrylist.CountryListResponse;
import com.hobbyer.android.api.response.auth.editProfile.UpdateProfileResponse;
import com.hobbyer.android.api.response.auth.login.SignInData;
import com.hobbyer.android.api.response.auth.viewprofile.ViewProfileResponse;
import com.hobbyer.android.api.response.auth.viewprofile.ViewProfileUser;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.DialogCountryCodeBinding;
import com.hobbyer.android.interfaces.ProfileInterface;
import com.hobbyer.android.model.ProfileImageModel;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.editprofile.EditProfileActivity;
import com.hobbyer.android.view.adapter.CountrySpinnerAdapter;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class EditProfileViewModel extends ActivityViewModel<EditProfileActivity> implements TextWatcher,View.OnClickListener {
    private String firstname, lastname, email, dob, phonenum, street, apartment, city, postalcode, emergencycontact, emergencyname;
    final Calendar myCalendar = Calendar.getInstance();
    UserModel userModel = PreferenceUtils.getUserModel();
    ArrayList<CountryContentList> contentList;
    private EditProfileActivity activity;
    private Dialog dialog;
    private DialogCountryCodeBinding dialogFeedbackRatingBinding;
    private String countryCode;
    private CountrySpinnerAdapter adapterReview;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(YEAR, year);
            myCalendar.set(MONTH, monthOfYear);
            myCalendar.set(DAY_OF_MONTH, dayOfMonth);
            //   activity.getBinding().etDOB.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
        }
    };
    public EditProfileViewModel(EditProfileActivity activity) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        if (CommonUtils.isOnline(activity)) {
            CountryConnection();
            /*getEditProfileConnection();*/
        }
        sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        addTextWatcher();
        setProfile();
        if (activity.getBinding().rgGroup != null) {
            activity.getBinding().rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (activity.getBinding().rdMail.isChecked()) {
                        activity.getBinding().gender.setText(" Male");
                    } else if (activity.getBinding().rdFemale.isChecked()) {
                        activity.getBinding().gender.setText("Female");
                    }
                }
            });
        }
    }
    private void openDailogAlternate() {
        dialogFeedbackRatingBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_country_code, null, false);
        dialog = DialogUtils.createDialog(activity, dialogFeedbackRatingBinding.getRoot());
        dialogFeedbackRatingBinding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setRecycelView();
        countryConnectionAlter();
    }
    private void countryConnectionAlter() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.country_List().enqueue(new BaseCallback<CountryListResponse>(activity) {
                    @Override
                    public void onSuccess(CountryListResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                contentList = response.getResult().getContentList();
                                adapterReview = new CountrySpinnerAdapter(activity, contentList, new ProfileInterface() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        switch (view.getId()) {
                                            case R.id.tvName:
                                                countryCode = "+" + contentList.get(position).getPhonecode();
                                                activity.getBinding().tvAlternate.setText(countryCode);
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                });
                                dialogFeedbackRatingBinding.rvCountry.setAdapter(adapterReview);
                            }
                        }
                        ProgressDialogUtils.dismiss();
                    }
                    @Override
                    public void onFail(Call<CountryListResponse> call, BaseResponse baseResponse) {
                        ToastUtils.showToastLong(activity, " " + baseResponse.getMessage());
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
    private void openDailog() {
        dialogFeedbackRatingBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_country_code, null, false);
        dialog = DialogUtils.createDialog(activity, dialogFeedbackRatingBinding.getRoot());
        dialogFeedbackRatingBinding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setRecycelView();
        countryConnection();
    }
    private void setRecycelView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        dialogFeedbackRatingBinding.rvCountry.setLayoutManager(layoutManager);
    }
    private void countryConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.country_List().enqueue(new BaseCallback<CountryListResponse>(activity) {
                    @Override
                    public void onSuccess(CountryListResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                contentList = response.getResult().getContentList();
                                adapterReview = new CountrySpinnerAdapter(activity, contentList, new ProfileInterface() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        switch (view.getId()) {
                                            case R.id.tvName:
                                                countryCode = "+" + contentList.get(position).getPhonecode();
                                                activity.getBinding().ietCountryCode.setText(countryCode);
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                });
                                dialogFeedbackRatingBinding.rvCountry.setAdapter(adapterReview);
                            }
                        }
                        ProgressDialogUtils.dismiss();
                    }
                    @Override
                    public void onFail(Call<CountryListResponse> call, BaseResponse baseResponse) {
                        ToastUtils.showToastLong(activity, " " + baseResponse.getMessage());
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
    /* activity.getBinding().spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             activity.getBinding().txCity.setText(contentList.get(position).getCode());
         }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
     });*/
    private void setToolbar() {
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
        activity.getBinding().toolbar.tvTitle.setText("My Account");
    }
    public void filter(String text){
        List<CountryContentList> filteredName = new ArrayList<>();
        for (CountryContentList model : contentList){
            if (model.getCountry().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }
        }
        // binding.tvNumberingBroadcast.setText(count + " " +getString(R.string.of)+ " " + filteredName.size());
        adapterReview.updatedList(filteredName);
    }
    private void addTextWatcher() {
        activity.getBinding().etDOB.addTextChangedListener(this);
        activity.getBinding().etPhone.addTextChangedListener(this);
        activity.getBinding().etStreet.addTextChangedListener(this);
        activity.getBinding().etApartmentAddress.addTextChangedListener(this);
        activity.getBinding().etCity.addTextChangedListener(this);
        activity.getBinding().etEmergencyName.addTextChangedListener(this);
        activity.getBinding().etEmergencyNumber.addTextChangedListener(this);
    }
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.etDOB:
                openDatePickerDialog();
                break;
            case R.id.btEditProfileSubmit:
                if (isValid()) {
                    EditProfileConnection();
                }
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ietCountryCode:
                openDailog();
                break;
            case R.id.tvAlternate:
                openDailogAlternate();
                break;
        }
    }
    private boolean isValid() {
        firstname = activity.getBinding().etName.getText().toString().trim();
        lastname = activity.getBinding().etLastName.getText().toString().trim();
        phonenum = activity.getBinding().etPhone.getText().toString().trim();
        email = activity.getBinding().eEmail.getText().toString().trim();
        apartment = activity.getBinding().etApartmentAddress.getText().toString().trim();
        city = activity.getBinding().etCity.getText().toString().trim();
        dob = activity.getBinding().etDOB.getText().toString().trim();
        emergencyname = activity.getBinding().etEmergencyName.getText().toString().trim();
        emergencycontact = activity.getBinding().etEmergencyNumber.getText().toString().trim();
        postalcode = activity.getBinding().etPostCode.getText().toString().trim();
        street = activity.getBinding().etStreet.getText().toString().trim();
      /*  if (StringUtils.isBlank(dob)) {
            activity.getBinding().etDOB.requestFocus();
            activity.getBinding().tvdobError.setVisibility(View.VISIBLE);
            activity.getBinding().tvdobError.setText(R.string.please_enter_dob);
            return false;
        }*/  if (StringUtils.isBlank(phonenum)) {
            activity.getBinding().etPhone.requestFocus();
            activity.getBinding().tvPhoneError.setVisibility(View.VISIBLE);
            activity.getBinding().tvPhoneError.setText(R.string.please_enter_phone);
            return false;
        }
        if (StringUtils.isBlank(email)) {
            activity.getBinding().eEmail.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(activity.getResources().getString(R.string.please_enter_email));
            return false;
        }
        else if (!RegexUtils.isEmailValid(email)) {
            activity.getBinding().eEmail.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(activity.getResources().getString(R.string.please_enter_valid_email));
            return false;
        }
        else if (phonenum.length() < 10) {
            activity.getBinding().etPhone.requestFocus();
            activity.getBinding().tvPhoneError.setVisibility(View.VISIBLE);
            activity.getBinding().tvPhoneError.setText(R.string.please_enter_valid_phone);
            return false;
        } else if (StringUtils.isBlank(street)) {
            activity.getBinding().etStreet.requestFocus();
            activity.getBinding().tvStreetError.setVisibility(View.VISIBLE);
            activity.getBinding().tvStreetError.setText(R.string.please_enter_street);
            return false;
        } else if (StringUtils.isBlank(apartment)) {
            activity.getBinding().etApartmentAddress.requestFocus();
            activity.getBinding().tvApartmentError.setVisibility(View.VISIBLE);
            activity.getBinding().tvApartmentError.setText(R.string.please_enter_apartment);
            return false;
        } else if (StringUtils.isBlank(city)) {
            activity.getBinding().etCity.requestFocus();
            activity.getBinding().tvCityError.setVisibility(View.VISIBLE);
            activity.getBinding().tvCityError.setText(R.string.please_enter_city);
            return false;
        } else if (StringUtils.isBlank(emergencyname)) {
            activity.getBinding().etEmergencyName.requestFocus();
            activity.getBinding().tvEmergencyNameError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmergencyNameError.setText(R.string.please_enter_emergencyname);
            return false;
        } else if (StringUtils.isBlank(emergencycontact)) {
            activity.getBinding().etEmergencyNumber.requestFocus();
            activity.getBinding().tvEmergencyPhoneError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmergencyPhoneError.setText(R.string.please_enter_emergencycontact);
            return false;
        } else {
            return true;
        }
    }
    private void CountryConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.country_List().enqueue(new BaseCallback<CountryListResponse>(activity) {
                    @Override
                    public void onSuccess(CountryListResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                contentList = response.getResult().getContentList();
                                //  activity.getBinding().spCountry.setAdapter(new CountrySpinnerAdapter(activity, contentList));
                            }
                        }
                        ProgressDialogUtils.dismiss();
                    }
                    @Override
                    public void onFail(Call<CountryListResponse> call, BaseResponse baseResponse) {
                        ToastUtils.showToastLong(activity, " " + baseResponse.getMessage());
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
    private void openDatePickerDialog() {
        /*
         * Get Current date
         * */
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                activity.getBinding().etDOB.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void setProfile() {
        if (userModel != null && !TextUtils.isEmpty(userModel.getFirstName()) && !TextUtils.isEmpty(userModel.getLastName()) && !TextUtils.isEmpty(userModel.getEmail())) {
            activity.getBinding().etName.setText(userModel.getFirstName());
            activity.getBinding().etLastName.setText(userModel.getLastName());
            activity.getBinding().eEmail.setText(userModel.getEmail());
            activity.getBinding().etDOB.setText(userModel.getDob());
      //   activity.getBinding().ietCountryCode.setText("+"+userModel.getPhone_code_number());
            activity.getBinding().etPhone.setText(userModel.getPhoneNumber());
            activity.getBinding().etAlternateNumber.setText(userModel.getAlternatePhone());
            activity.getBinding().etStreet.setText(userModel.getStreet());
            activity.getBinding().etApartmentAddress.setText(userModel.getApartment());
            activity.getBinding().etCity.setText(userModel.getCity());
            activity.getBinding().etPostCode.setText(userModel.getPostalCode());
            activity.getBinding().etPostCode.setText(userModel.getPostalCode());
            if (userModel.getGender()!=null &&!userModel.getGender().equalsIgnoreCase("")) {
                if (userModel.getGender().equalsIgnoreCase("0")) {
                    activity.getBinding().rdMail.setChecked(true);
                } else if (userModel.getGender().equalsIgnoreCase("1")) {
                    activity.getBinding().rdFemale.setChecked(true);
                }
            }
        }
    }
    private void EditProfileConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                EditProfileRequest signInRequest = new EditProfileRequest();
                signInRequest.setFirstName(activity.getBinding().etName.getText().toString());
                signInRequest.setLastName(activity.getBinding().etLastName.getText().toString());
                signInRequest.setUser_id(userModel.getId());
                signInRequest.setDob(activity.getBinding().etDOB.getText().toString());
                signInRequest.setGender(activity.getBinding().gender.getText().toString());
                signInRequest.setPhone_number(activity.getBinding().etPhone.getText().toString());
                signInRequest.setAlternate_phone_number(activity.getBinding().etAlternateNumber.getText().toString());
                signInRequest.setStreet(activity.getBinding().etStreet.getText().toString());
                signInRequest.setPhoneCode(activity.getBinding().ietCountryCode.getText().toString());
                signInRequest.setApartment(activity.getBinding().etApartmentAddress.getText().toString());
                signInRequest.setCity(activity.getBinding().etCity.getText().toString());
                signInRequest.setPostal(activity.getBinding().etPostCode.getText().toString());
                signInRequest.setCountry(activity.getBinding().txCity.getText().toString());
                signInRequest.setEmergency_contact_name(activity.getBinding().etEmergencyNumber.getText().toString());
                signInRequest.setEmergency_phone_number(activity.getBinding().etEmergencyName.getText().toString());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.updateProfile(signInRequest).enqueue(new BaseCallback<UpdateProfileResponse>((AppCompatActivity) activity) {
                    @Override
                    public void onSuccess(UpdateProfileResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SignInData signInData = response.getResult().getData();
                                UserModel userModel = createUserModel(signInData);
                                PreferenceUtils.saveUserModel(userModel);
                                ProfileImageModel profileModel = createProfle(signInData);
                                PreferenceUtils.saveProfle(profileModel);
                                finish();
                                /* getEditProfileConnection();*/
                            } else {
                                ToastUtils.showToastLong(activity, " " + response.getMessage());
                            }
                        }
                        ProgressDialogUtils.dismiss();
                    }
                    @Override
                    public void onFail(Call<UpdateProfileResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(activity, " " + baseResponse.getMessage());
                    }
                });
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }
    private ProfileImageModel createProfle(SignInData signInData) {
        ProfileImageModel profileImageModel = new ProfileImageModel();
        profileImageModel.setFirstName(signInData.getFirstName());
        profileImageModel.setLastName(signInData.getLastName());
        return profileImageModel;
    }
    private UserModel createUserModel(SignInData signInData) {
        UserModel userModel = new UserModel();
        userModel.setId(signInData.getId());
        userModel.setFirstName(signInData.getFirstName());
        userModel.setLastName(signInData.getLastName());
        userModel.setBusinessName(signInData.getBusinessName());
        userModel.setWebsiteUrl(signInData.getWebUrl());
        userModel.setEmail(signInData.getEmail());
        userModel.setPhoneNumber(signInData.getPhoneNumber());
     //   userModel.setPhoneCode(signInData.getPhoneCode());
        userModel.setCountryId(signInData.getCountryId());
        userModel.setBusinessOpen(signInData.getBusinessOpen());
        userModel.setCity(signInData.getCity());
        userModel.setSocialType(signInData.getSocialType());
        userModel.setSocialId(signInData.getSocialId());
        userModel.setAlternatePhone(signInData.getAlternatePhone());
        userModel.setStreet(signInData.getStreet());
        userModel.setApartment(signInData.getApartment());
        userModel.setPostalCode(signInData.getPostalCode());
        userModel.setGender(signInData.getGender());
        userModel.setDob(signInData.getDob());
        userModel.setEmergencyContactName(signInData.getEmergencyContactName());
        userModel.setEmergencyContactPhone(signInData.getEmergencyContactPhone());
        return userModel;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        activity.getBinding().tvdobError.setVisibility(View.INVISIBLE);
        activity.getBinding().tvPhoneError.setVisibility(View.INVISIBLE);
        activity.getBinding().tvStreetError.setVisibility(View.INVISIBLE);
        activity.getBinding().tvApartmentError.setVisibility(View.INVISIBLE);
        activity.getBinding().tvCityError.setVisibility(View.INVISIBLE);
        activity.getBinding().tvEmergencyNameError.setVisibility(View.INVISIBLE);
        activity.getBinding().tvEmergencyPhoneError.setVisibility(View.INVISIBLE);
    }
    @Override
    public void afterTextChanged(Editable s) {
    }
    private void getEditProfileConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                long userId= userModel.getId();
                int inUserId = (int) userId;
                if (userModel == null) {
                    return;
                }
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.viewProfile(inUserId).enqueue(new BaseCallback<ViewProfileResponse>(activity) {
                    @Override
                    public void onSuccess(ViewProfileResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ViewProfileUser viewProfileUser = response.getResult().getData().getUser();
                                UserModel userModel = createUser(viewProfileUser);
                                PreferenceUtils.saveUserModel(userModel);
                                setProfile();
                            }
                        }
                        ProgressDialogUtils.dismiss();
                    }
                    @Override
                    public void onFail(Call<ViewProfileResponse> call, BaseResponse baseResponse) {
                        ToastUtils.showToastLong(activity, " " + baseResponse.getMessage());
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
    private UserModel createUser(ViewProfileUser signInData) {
        UserModel userModel = new UserModel();
        userModel.setId(signInData.getId());
        userModel.setFirstName(signInData.getFirstName());
        userModel.setLastName(signInData.getLastName());
        userModel.setBusinessName(signInData.getBusinessName());
        userModel.setWebsiteUrl(signInData.getWebUrl());
        userModel.setEmail(signInData.getEmail());
        userModel.setPhoneNumber(signInData.getPhoneNumber());
        userModel.setCountryId(signInData.getCountryId());
        userModel.setBusinessOpen(signInData.getBusinessOpen());
        userModel.setCity(signInData.getCity());
        userModel.setSocialType(signInData.getSocialType());
        userModel.setSocialId(signInData.getSocialId());
        userModel.setAlternatePhone(signInData.getAlternatePhone());
        userModel.setStreet(signInData.getStreet());
        userModel.setApartment(signInData.getApartment());
        userModel.setPostalCode(signInData.getPostalCode());
        userModel.setGender(signInData.getGender());
        userModel.setDob(signInData.getDob());
        userModel.setEmergencyContactName(signInData.getEmergencyContactName());
        userModel.setEmergencyContactPhone(signInData.getEmergencyContactPhone());
        return userModel;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
