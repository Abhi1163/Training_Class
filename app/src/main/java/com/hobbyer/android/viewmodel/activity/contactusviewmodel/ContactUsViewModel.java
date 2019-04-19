package com.hobbyer.android.viewmodel.activity.contactusviewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.ContactUsRequest;
import com.hobbyer.android.api.request.UploadImagesRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.contactUs.ContactData;
import com.hobbyer.android.api.response.auth.contactUs.ContactUsResponse;
import com.hobbyer.android.api.response.auth.savefile.SaveFileData;
import com.hobbyer.android.api.response.auth.savefile.SaveFileResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CheckPermission;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.TakePictureUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.contactus.ContactUsActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import retrofit2.Call;

public class ContactUsViewModel extends ActivityViewModel<ContactUsActivity> implements TextWatcher,View.OnClickListener {

    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final int TAKE_PICTURE = 1;
    public String imageName;
    File imageFile = null;
    private String userIdUpdate;
    String accountDetails;
    private String email, subject, differentEmail, help, description;


    public ContactUsViewModel(ContactUsActivity activity) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        setTextWatcher();
        activity.getBinding().spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int item, long l) {
                accountDetails = activity.getBinding().spinner.getSelectedItem().toString();


            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

    }

    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText("Contact us");
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }

    private void setTextWatcher() {
        activity.getBinding().etEmail.addTextChangedListener(this);
        activity.getBinding().etClassPassEmailId.addTextChangedListener(this);
        activity.getBinding().etHowCanHelp.addTextChangedListener(this);
        activity.getBinding().etSubject.addTextChangedListener(this);
        activity.getBinding().etDescription.addTextChangedListener(this);
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_file:
                selectImage();
                break;
            case R.id.llDone:
                ToastUtils.showToastShort(activity,"dfcdf");

                if (isValid()) {
                    contactUsConnection();
                }
                break;


            case R.id.ivBack:
                finish();
                break;
            default:
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {activity.getString(R.string.take_photo), activity.getString(R.string.choose_from_gallery), activity.getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(activity.getString(R.string.take_photo))) {
                    if (CheckPermission.checkPermissionStorage(activity)) {
                        // Permission is not granted
                        imageName = "picture_" + System.currentTimeMillis();
                        captureImage();
                    } else {
                        CheckPermission.requestCameraPermission(activity, TakePictureUtils.PICK_GALLERY_PROFILE);
                    }
                } else if (items[item].equals(activity.getString(R.string.choose_from_gallery))) {
                    if (CheckPermission.checkPermissionStorage(activity)) {
                        imageName = "picture_" + System.currentTimeMillis();
                        TakePictureUtils.openGalleryProfile(activity);
                    } else {
                        CheckPermission.requestCameraPermission(activity, TakePictureUtils.PICK_GALLERY_PROFILE);
                    }
                } else if (items[item].equals(activity.getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageName);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageName = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        try {
            if (requestCode == TakePictureUtils.PICK_GALLERY_PROFILE) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        File myFile = new File(uri.toString());
                        String path = myFile.getName();
                        Pattern p = Pattern.compile("\\%20");
                        Matcher m = p.matcher(path);
                        imageName = m.replaceAll(" ");
                        Pattern pp = Pattern.compile("\\%2F");
                        Matcher mp = pp.matcher(imageName);
                        String mfileName = mp.replaceAll("/");
                        Pattern ppp = Pattern.compile("\\%3A");
                        Matcher mpp = ppp.matcher(mfileName);
                        String mmfileName = mpp.replaceAll(" ");
                        String segments[] = mmfileName.split("/");
                        String document = segments[segments.length - 1];
                        activity.getBinding().tvFile.setText(document.replace("primary", " "));

                        imageFile = new File(imageName);

                    }
                }
            } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    TakePictureUtils.refreshGallery(activity.getApplicationContext(), imageName);
                    Pattern ppp = Pattern.compile("\\%3A");
                    Matcher mpp = ppp.matcher(imageName);
                    String mmfileName = mpp.replaceAll(" ");
                    String segments[] = mmfileName.split("/");
                    String document = segments[segments.length - 1];
                    activity.getBinding().tvFile.setText(document.replace("primary", " "));
                    imageFile = new File(imageName);
                } else if (resultCode == RESULT_CANCELED) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isValid() {
        email = activity.getBinding().etEmail.getText().toString().trim();
        subject = activity.getBinding().etSubject.getText().toString().trim();
        differentEmail = activity.getBinding().etClassPassEmailId.getText().toString();
        help = activity.getBinding().etHowCanHelp.getText().toString();
        description = activity.getBinding().etDescription.getText().toString();

        if (StringUtils.isBlank(email)) {
            activity.getBinding().etEmail.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(activity.getResources().getString(R.string.please_enter_email));

            return false;
        } else if (!RegexUtils.isValidEmail(email)) {
            activity.getBinding().etEmail.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(R.string.please_enter_valid_email);
            return false;
        } else if (StringUtils.isBlank(differentEmail)) {
            activity.getBinding().etClassPassEmailId.requestFocus();
            activity.getBinding().tvDifferentEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvDifferentEmailError.setText(activity.getResources().getString(R.string.please_enter_different_email));
            return false;
        } else if (StringUtils.isBlank(help)) {
            activity.getBinding().etHowCanHelp.requestFocus();
            activity.getBinding().tvHelpError.setVisibility(View.VISIBLE);
            activity.getBinding().tvHelpError.setText(activity.getResources().getString(R.string.please_enter_help));
            return false;
        } else if (StringUtils.isBlank(subject)) {
            activity.getBinding().etSubject.requestFocus();
            activity.getBinding().tvSubjectError.setVisibility(View.VISIBLE);
            activity.getBinding().tvSubjectError.setText(activity.getResources().getString(R.string.please_enter_Subject));
            return false;
        } else if (StringUtils.isBlank(description))
        {
            activity.getBinding().etDescription.requestFocus();
            activity.getBinding().tvDescriptionError.setVisibility(View.VISIBLE);
            activity.getBinding().tvDescriptionError.setText(activity.getResources().getString(R.string.please_enter_description));
            return false;
        }

            return true;


    }

    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = TakePictureUtils.getOutputMediaFile(TAKE_PICTURE);
        if (file != null) {
            imageName = file.getAbsolutePath();
        }
        Uri fileUri = TakePictureUtils.getOutputMediaFileUri(activity.getApplicationContext(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    private void previewCapturedImage() {
        try {
            Bitmap bitmap = TakePictureUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageName);
            activity.getBinding().imUserImage.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        activity.getBinding().tvEmailError.setVisibility(View.GONE);
        activity.getBinding().tvSubjectError.setVisibility(View.GONE);
        activity.getBinding().tvHelpError.setVisibility(View.GONE);
        activity.getBinding().tvDifferentEmailError.setVisibility(View.GONE);
        activity.getBinding().tvDescriptionError.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void postImageConnection(String userId) {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);

                if (userId == null) {
                    return;
                }
                int inUserId = Integer.parseInt(userId);
                UploadImagesRequest contactUs = new UploadImagesRequest();
                contactUs.setImage(imageFile);
                contactUs.setId(inUserId);
                contactUs.setType("contact");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.uploadImages(contactUs).enqueue(new BaseCallback<SaveFileResponse>((AppCompatActivity) activity) {
                    @Override
                    public void onSuccess(SaveFileResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                SaveFileData saveFileData = response.getResult().getData();
                                ToastUtils.showToastLong(activity, "" + saveFileData.getUrl());
                                if (saveFileData.getUrl() != null && !saveFileData.getUrl().equalsIgnoreCase("")) {
                                    Picasso.with(activity).load(saveFileData.getUrl()).placeholder(R.drawable.ic_image).fit().into(activity.getBinding().imUserImage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            //pbHome.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {
                                            //pbHome.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    activity.getBinding().imUserImage.setImageResource(R.drawable.ic_image);
                                }

                                //flagFav=getPrivacyData.getFavouriteClass();
                            } else {
                                ToastUtils.showToastLong(activity, "" + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<SaveFileResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(activity, "" + baseResponse.getMessage());
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void contactUsConnection() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                ContactUsRequest contactUs = new ContactUsRequest();
                contactUs.setUser_id("" + userModel.getId());
                contactUs.setEmail(activity.getBinding().etEmail.getText().toString());
                contactUs.setSubject(activity.getBinding().etSubject.getText().toString());
                contactUs.setDescription(activity.getBinding().etDescription.getText().toString());
                contactUs.setDifferent_email(activity.getBinding().etClassPassEmailId.getText().toString());
                contactUs.setIssue(accountDetails);
                contactUs.setHelp(activity.getBinding().etHowCanHelp.getText().toString());

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.contactUs(contactUs).enqueue(new BaseCallback<ContactUsResponse>((AppCompatActivity) activity) {
                    @Override
                    public void onSuccess(ContactUsResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ContactData contactData = response.getResult().getData();
                                userIdUpdate = contactData.getId();
                                postImageConnection(userIdUpdate);
                                //ToastUtils.showToastLong(activity, "" + contactData.getId());

                            }
                            else {
                                ProgressDialogUtils.dismiss();
                                Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            ProgressDialogUtils.dismiss();
                            Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ContactUsResponse> call, BaseResponse baseResponse) {
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
