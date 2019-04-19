package com.hobbyer.android.viewmodel.fragment.profileviewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.login.SignInData;
import com.hobbyer.android.api.response.auth.savefile.SaveFileData;
import com.hobbyer.android.api.response.auth.savefile.SaveFileResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.FragmentProfileBinding;
import com.hobbyer.android.interfaces.Check;
import com.hobbyer.android.model.ProfileImageModel;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CheckPermission;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.TakePictureUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.view.fragments.base.FragmentViewModel;
import com.hobbyer.android.view.fragments.profile.FavouritesFragment;
import com.hobbyer.android.view.fragments.profile.FriendFragment;
import com.hobbyer.android.view.fragments.profile.PastsFragment;
import com.hobbyer.android.view.fragments.profile.ProfileFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragmentViewModel extends FragmentViewModel<ProfileFragment, FragmentProfileBinding> {
    public static final int TAKE_PICTURE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public String imageName;
    File file;
    File imageFile = null;
    private Activity activity;
    private ProfileFragment fragment;
    private ImageView imUserImage, imEdit;
    private TextView mPasts, mFavourites, mFriends;
    private LinearLayout mPastLinear, mFavouritesLinear, mFriendLinear;
    private TextView tvUserName;
    private Bitmap imageBitmap;
    private HomeActivity homeActivity;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransection;
    private Fragment mFragment;
    private SaveFileData saveFileData;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String imageUrl;

    private TextView tvTitle, tvRight;
    private ImageView ivBack, ivSearch, ivLocation,ivPlace;
    private DrawerLayout drawerLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ProfileFragmentViewModel(ProfileFragment fragment, FragmentProfileBinding binding) {
        super(fragment);
        this.fragment = fragment;
        this.activity = getActivity();
        init();
        setToolbar();
        mPasts = (TextView) activity.findViewById(R.id.mPasts);
        mFavourites = (TextView) activity.findViewById(R.id.mFavourites);
        mFriends = (TextView) activity.findViewById(R.id.mFriends);
        mFavouritesLinear = (LinearLayout) activity.findViewById(R.id.mFavouritesLinear);
        mPastLinear = (LinearLayout) activity.findViewById(R.id.mPastLinear);
        mFriendLinear = (LinearLayout) activity.findViewById(R.id.mFriendLinear);
        tvUserName = (TextView) activity.findViewById(R.id.tvUserName);
        imUserImage = (ImageView) activity.findViewById(R.id.imUserImage);
        imEdit = (ImageView) activity.findViewById(R.id.imEdit);
        countPointConnection();
        setProfile();

        sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        try {
            CallPastsFragment();
        } catch (Exception ex) {

        }
        imEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        mPasts.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                CallPastsFragment();
            }
        });
        mFavourites.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                CallFavourites();
            }
        });
        mFriends.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                CallFriend();
            }
        });
    }


    @Override
    protected void initialize(FragmentProfileBinding binding) {

    }

    private void setProfile() {
    //    UserModel userModel = PreferenceUtils.getUserModel();

        ProfileImageModel userModel = PreferenceUtils.getProfile();


        if (userModel != null && !TextUtils.isEmpty(userModel.getFirstName()) && !TextUtils.isEmpty(userModel.getLastName())) {
            tvUserName.setText(userModel.getFirstName() + " " + userModel.getLastName());
        }

        //String img = SharedPref.getStringPreferences(activity, AppConstant.SAVE__PROFILE);
        if( userModel.getUserImage() !=null)
        {
            //  Glide.with(activity).load(img).into(fragment.getBinding().imUserImage);
            Glide.with(activity).load(userModel.getUserImage()).error(R.mipmap.img_568656).placeholder(R.mipmap.img_568656).into(fragment.getBinding().imUserImage);

        }

        if (userModel.getSocialId() != null && !userModel.getSocialId().equalsIgnoreCase("")) {
            Glide.with(activity).load("https://graph.facebook.com/" + userModel.getSocialId() + "/picture?type=large").fitCenter().into(imUserImage);
            Picasso.with(activity).load("https://graph.facebook.com/" + userModel.getSocialId() + "/picture?type=large").placeholder(R.mipmap.img_568656).fit().into(imUserImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CallPastsFragment() {

        mFavourites.setBackgroundResource(R.color.white);
        mFavourites.setTextColor(activity.getResources().getColor(R.color.black));
        mFriends.setBackgroundResource(R.color.white);
        mFriends.setTextColor(activity.getResources().getColor(R.color.black));
        mFragmentManager = activity.getFragmentManager();
        mFragmentTransection = mFragmentManager.beginTransaction();
        if (mFragment != null) {
            mPasts.setBackgroundResource(R.color.black);
            mPasts.setVisibility(View.VISIBLE);
            mPasts.setTextColor(activity.getResources().getColor(R.color.yellow));
            mFragmentTransection.detach(mFragment);
            mFragment = null;
        }
        mFragment = new PastsFragment();
        mFragmentTransection.replace(R.id.Fragment_Container, mFragment);
        mFragmentTransection.addToBackStack("");
        mFragmentTransection.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CallFavourites() {
        mPasts.setBackgroundResource(R.color.white);
        mPasts.setTextColor(activity.getResources().getColor(R.color.black));
        mFriends.setBackgroundResource(R.color.white);
        mFriends.setTextColor(activity.getResources().getColor(R.color.black));
        mFragmentManager = activity.getFragmentManager();
        mFragmentTransection = mFragmentManager.beginTransaction();
        if (mFragment != null) {
            mFavourites.setBackgroundResource(R.color.black);
            mFavourites.setVisibility(View.VISIBLE);
            mFavourites.setTextColor(activity.getResources().getColor(R.color.yellow));
            mFragmentTransection.detach(mFragment);
            mFragment = null;
        }

        mFragment = new FavouritesFragment(fragment);
        mFragmentTransection.replace(R.id.Fragment_Container, mFragment);
        //  mFragmentTransection.addToBackStack("");
        mFragmentTransection.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CallFriend() {
        mPasts.setBackgroundResource(R.color.white);
        mPasts.setTextColor(activity.getResources().getColor(R.color.black));
        mFavourites.setBackgroundResource(R.color.white);
        mFavourites.setTextColor(activity.getResources().getColor(R.color.black));
        mFragmentManager = activity.getFragmentManager();
        mFragmentTransection = mFragmentManager.beginTransaction();
        if (mFragment != null) {
            mFriends.setBackgroundResource(R.color.black);
            mFriends.setVisibility(View.VISIBLE);
            mFriends.setTextColor(activity.getResources().getColor(R.color.yellow));
            mFragmentTransection.detach(mFragment);
            mFragment = null;
        }

        mFragment = new FriendFragment();
        mFragmentTransection.replace(R.id.Fragment_Container, mFragment);
        //  mFragmentTransection.addToBackStack("");
        mFragmentTransection.commit();
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
        tvTitle.setVisibility(View.VISIBLE);
        ivLocation.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.profile);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
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
                    }
                    else
                        {
                        CheckPermission.requestCameraPermission(activity, TakePictureUtils.PICK_GALLERY_PROFILE);
                      //per

                    }
                } else if (items[item].equals(activity.getString(R.string.choose_from_gallery))) {
                    if (CheckPermission.checkPermissionStorage(activity)) {
                        imageName = "picture_" + System.currentTimeMillis();
                        TakePictureUtils.openGalleryProfile(activity);
                    } else {
                        CheckPermission.requestCameraPermission(activity, TakePictureUtils.PICK_GALLERY_PROFILE);
                        //per

                    }
                } else if (items[item].equals(activity.getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if (requestCode == TakePictureUtils.PICK_GALLERY_PROFILE) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = intent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageName = cursor.getString(columnIndex);
                    cursor.close();
                    imageBitmap = BitmapFactory.decodeFile(imageName);
                    Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 300, 300, true);
                    Drawable ob = new BitmapDrawable(activity.getResources(), scaled);
                    imUserImage.setImageDrawable(ob);


                    imageFile = new File(imageName);

                    postImageConnection();
                }
            } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    TakePictureUtils.refreshGallery(activity.getApplicationContext(), imageName);
                    previewCapturedImage();
                } else if (resultCode == RESULT_CANCELED) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = TakePictureUtils.getOutputMediaFile(TAKE_PICTURE);
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
            imUserImage.setImageBitmap(bitmap);
            imageFile = new File(imageName);
            postImageConnection();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void postImageConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                long userId = userModel.getId();
                int inUserId = (int) userId;
                if (userModel == null) {
                    return;
                }
               /* MultipartBody.Part imagePart = null;
                RequestBody fBody = null;
                if (file != null) {
                    fBody = RequestBody.create(MediaType.parse("image"), imageFile);
                }*/

                RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(userId));
                RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), "profile");


                MultipartBody.Part imagePart = null;
                if (imageFile != null) {
                    RequestBody imageFileBody = RequestBody.create(MediaType.parse(AppConstant.MEDIA_TYPE_IMAGE), imageFile);
                    imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageFileBody);
                }
                //imagePart= ConverterUtils.getMultipartFromFile("image",imageFile);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
                webServices.uploadImage(imagePart, id, type).enqueue(new BaseCallback<SaveFileResponse>((AppCompatActivity) activity) {
                    @Override
                    public void onSuccess(SaveFileResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SaveFileData saveFileData = response.getResult().getData();
                                imageUrl = response.getResult().getData().getUrl();
                            //
                                //    ToastUtils.showToastLong(activity, "" + saveFileData.getUrl());

                                if (saveFileData.getUrl() != null) {

                                    Glide.with(activity).load(saveFileData.getUrl()).into(fragment.getBinding().imUserImage);
                                    //img
                                  /*  UserModel userModel=createUserModel(saveFileData);
                                    PreferenceUtils.saveUserModel(userModel);*/
                                    ProfileImageModel userModel=createUserModel(saveFileData);
                                    PreferenceUtils.saveProfle(userModel);

                                    SharedPref.saveStringPreferences(activity, AppConstant.SAVE__PROFILE, imageUrl);
                                    ((HomeActivity)fragment.getContext()).userProfile();



                                    editor.putString("image", response.getResult().getData().getUrl());
                                    editor.commit();
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

   /* private SaveFileData saveFile(SaveFileData getProfile) {
        SaveFileData saveFileData = new SaveFileData();
        saveFileData.setUrl(getProfile.getUrl());
        return saveFileData;
    }*/



    private ProfileImageModel createUserModel(SaveFileData saveFileData) {
        ProfileImageModel userModel = new ProfileImageModel();
        userModel.setUserImage(saveFileData.getUrl());


        return userModel;
    }

    public void countPointConnection() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                CountPointRequest countPointRequest = new CountPointRequest();
                countPointRequest.setUserId("" + userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.count_Point(countPointRequest).enqueue(new BaseCallback<CountPointResponse>(fragment) {
                    @Override
                    public void onSuccess(CountPointResponse response) {

                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CountPointData countPointData = response.getResult().getData();
                                CountPointData countPoint = createCountPoint(countPointData);
                                PreferenceUtils.savePoint(countPoint);
                                pointData();
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<CountPointResponse> call, BaseResponse baseResponse) {
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

    private CountPointData createCountPoint(CountPointData countPointData) {
        CountPointData countPointData1 = new CountPointData();
        countPointData1.setFriend_count(countPointData.getFriend_count());
        countPointData1.setFavourite_studios(countPointData.getFavourite_studios());
        countPointData1.setCompleted_class(countPointData.getCompleted_class());
        countPointData1.setUpcoming_class(countPointData.getUpcoming_class());
        countPointData1.setUser_point(countPointData.getUser_point());


        return countPointData1;
    }

    private void pointData() {
        CountPointData point = PreferenceUtils.getPoint();
        tvRight.setText(point.getUser_point() + " Points ");
        fragment.getBinding().mFavourites.setText(" Favourites" + "(" + point.getFavourite_studios() + ") ");
        fragment.getBinding().mPasts.setText(" Pasts" + "(" + point.getCompleted_class() + ") ");
        fragment.getBinding().mFriends.setText(" Friends" + "(" + point.getFriend_count() + ") ");
    }


}
