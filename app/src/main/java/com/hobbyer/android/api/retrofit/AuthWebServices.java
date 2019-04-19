package com.hobbyer.android.api.retrofit;

import com.google.gson.JsonObject;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.request.AcceptFriendRequest;
import com.hobbyer.android.api.request.AddPhoneNumberRequest;
import com.hobbyer.android.api.request.BlockFriendRequest;
import com.hobbyer.android.api.request.BlockFriendViewRequest;
import com.hobbyer.android.api.request.CancelFriendRequest;
import com.hobbyer.android.api.request.ChangePasswordRequest;
import com.hobbyer.android.api.request.CheckCityRequest;
import com.hobbyer.android.api.request.ClassDetailRequest;
import com.hobbyer.android.api.request.ConfirmFriendViewRequest;
import com.hobbyer.android.api.request.ContactUsRequest;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.request.EditProfileRequest;
import com.hobbyer.android.api.request.FavouriteStudioRequest;
import com.hobbyer.android.api.request.FavouriteUserViewRequest;
import com.hobbyer.android.api.request.FindClassRequest;
import com.hobbyer.android.api.request.FindClassTomorrowRequest;
import com.hobbyer.android.api.request.ForgotPasswordRequest;
import com.hobbyer.android.api.request.GetPrivacysSettingRequest;
import com.hobbyer.android.api.request.GetUserFriendViewRequest;
import com.hobbyer.android.api.request.HomePageRequest;
import com.hobbyer.android.api.request.LogoutRequest;
import com.hobbyer.android.api.request.PastUserViewRequest;
import com.hobbyer.android.api.request.PhoneOtpRequest;
import com.hobbyer.android.api.request.PrivacySettingRequest;
import com.hobbyer.android.api.request.RatingRequest;
import com.hobbyer.android.api.request.RecentChargesRequest;
import com.hobbyer.android.api.request.ReferEarnRequest;
import com.hobbyer.android.api.request.ReserveRequest;
import com.hobbyer.android.api.request.ReviewRequest;
import com.hobbyer.android.api.request.SaveCardRequest;
import com.hobbyer.android.api.request.SearchFriendViewRequest;
import com.hobbyer.android.api.request.SendFriendRequest;
import com.hobbyer.android.api.request.SignInRequest;
import com.hobbyer.android.api.request.SignUpRequest;
import com.hobbyer.android.api.request.SocialLoginRequest;
import com.hobbyer.android.api.request.StudioUserNotExistRequest;
import com.hobbyer.android.api.request.UpComingClassesRequest;
import com.hobbyer.android.api.request.UploadImagesRequest;
import com.hobbyer.android.api.request.VerifyOtpRequest;
import com.hobbyer.android.api.request.VideosRequest;
import com.hobbyer.android.api.response.auth.acceptfriendrequest.ConfirmFriendViewResponse;
import com.hobbyer.android.api.response.auth.addphonenumber.AddPhoneNumberResponse;
import com.hobbyer.android.api.response.auth.blockfriendlist.BlockFriendViewResponse;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailResponse;
import com.hobbyer.android.api.response.auth.contactUs.ContactUsResponse;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.countrylist.CountryListResponse;
import com.hobbyer.android.api.response.auth.editProfile.UpdateProfileResponse;
import com.hobbyer.android.api.response.auth.faq.FaqResponse;
import com.hobbyer.android.api.response.auth.favouriteuserview.FavouriteUserViewResponse;
import com.hobbyer.android.api.response.auth.filter.CategoryListResponse;
import com.hobbyer.android.api.response.auth.findclass.FindClassResponse;
import com.hobbyer.android.api.response.auth.findclasstomorrow.FindClassTomorrowResponse;
import com.hobbyer.android.api.response.auth.forgotpassword.ForgotPasswordResponse;
import com.hobbyer.android.api.response.auth.getfriends.GetUserFriendViewResponse;
import com.hobbyer.android.api.response.auth.getprivacysetting.GetPrivacySettingResponse;
import com.hobbyer.android.api.response.auth.homepage.HomePageResponse;
import com.hobbyer.android.api.response.auth.login.SignInResponse;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanResponse;
import com.hobbyer.android.api.response.auth.pastprofile.PastUserViewResponse;
import com.hobbyer.android.api.response.auth.planlist.PlanListResponse;
import com.hobbyer.android.api.response.auth.recentcharges.RecentChargeResponse;
import com.hobbyer.android.api.response.auth.recentcharges.RecentChargeResult;
import com.hobbyer.android.api.response.auth.referearn.ReferEarnResponse;
import com.hobbyer.android.api.response.auth.reserve.ReserveResponse;
import com.hobbyer.android.api.response.auth.review.ReviewResponse;
import com.hobbyer.android.api.response.auth.savefile.SaveFileResponse;
import com.hobbyer.android.api.response.auth.searchcity.CityResponse;
import com.hobbyer.android.api.response.auth.searchfriendlist.SearchFriendViewResponse;
import com.hobbyer.android.api.response.auth.signup.SignUpResponse;
import com.hobbyer.android.api.response.auth.upcomingclasses.UpComingClassesResponse;
import com.hobbyer.android.api.response.auth.verifyotp.VerifyOtpResponse;
import com.hobbyer.android.api.response.auth.videos.VideosResponse;
import com.hobbyer.android.api.response.auth.viewprofile.ViewProfileResponse;
import com.hobbyer.android.api.response.getcarddetail.GetCardDataResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Interface to declare web service stubs.
 */
public interface AuthWebServices {

    String SEARCH_CITY = "search-city";
    String SIGN_UP = "sign-up";
    String SIGN_IN = "login";
    String FORGOT_PASSWORD = "forgot-password";
    String LOGOUT = "logout";
    String VERIFY_OTP = "verify-otp";
    String RESET_PASSWORD = "reset-password";
    String VIEW_PROFILE = "view-profile";
    String COUNTRY_LIST = "country-list";
    String EDIT_PROFILE = "edit-profile";
    String SOCIAL_LOGIN = "social-login";
    String CONTACT_US = "contact-us";
    String FAQ = "frequently-asked-question";
    String HOME_PAGE = "home-page";
    String REFER_EARN = "refer-earn";
    String GET_PRIVACY_SETTING = "get-privacy-setting";
    String PRIVACY_SETTING = "privacy-setting";
    String FIND_CLASS = "find-class";
    String SAVE_FILE = "save-file";
    String ADD_PHONE_NUMBER = "add-phone-number";
    String VIDEOS = "videos";
    String CLASS_DETAILS = "class-detail";
    String CHECK_CITY = "check-city";
    String STUDIO_USER_NOT_EXIST = "studio-user-not-exist";
    String COUNT_POINTS = "count-points";
    String UPCOMING_CLASSES = "upcoming-classes";
    String FAVOURITE_USER_VIEW = "favourite-user-view";
    String CATEGORY_LIST = "category-list";
    String FAVOURITE_STUDIO = "favourite-studio";
    String STUDIO_REVIEW = "studio-reviews";
    String PAST_CLASSES = "past-classes";
    String RESERVE_CANCEL_CLASSES = "reserve-cancel-classes";
    String SAVE_UPDATE_CARD_DETAILS = "save-update-card-details";
    String GET_CARD_DETAILS = "get-card-details";
    String PLAN_LIST = "plan-list";
    String UPDATE_PLAN_LIST = "update-user-plan";
    String MEMBERSHIP_LIST = "membership-list";
    String UPDATE_USER_MEMBERSHIP = "update-user-membership";
    String GET_TOP_CATEGORY_LIST = "category-list";

    //friend

    String GET_USER_FRIENDS = "get-friends";
    String SEND_FRIEND_REQUEST = "send-friend-request";
    String BLOCK_FRIEND = "block-friend-request";
    String ACCEPT_FRIEND = "accept-friend";
    String CANCEL_FRIEND = "cancel-friend-request";
    String UPDATE_MEMBERSHIP = "update-user-membership";
    String GET_BLOCK_USER_FRIENDS = "blocked-friends";
    String CONFIRM_FRIENDS_REQUEST = "friend-request-list";
    String SEARCH_USER_FRIENDS = "search-user-friend";
    String RECENT_CHARGES_LIST = "recent-charges-list";
    String SUBMIT_RATING_FEEDBACK = "submit-rating-feedback";


    @GET(SEARCH_CITY + "/{keyword}")
    Call<CityResponse> searchCity(@Path("keyword") String keyword);

    @POST(SIGN_UP)
    Call<SignUpResponse> signUp(@Body SignUpRequest request);

    @POST(SIGN_IN)
    Call<SignInResponse> signIn(@Body SignInRequest request);

    @POST(FORGOT_PASSWORD)
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST(LOGOUT)
    Call<BaseResponse> logoutUser(@Body LogoutRequest request);

    // Verify otp
    @POST(VERIFY_OTP)
    Call<VerifyOtpResponse> verifyOtp(@Body VerifyOtpRequest request);

    @POST(VERIFY_OTP)
    Call<VerifyOtpResponse> verifyPhoneOtp(@Body PhoneOtpRequest request);

    // reset password
    @POST(RESET_PASSWORD)
    Call<BaseResponse> resetPassword(@Body ChangePasswordRequest request);

    @GET(VIEW_PROFILE + "/{user_id}")
    Call<ViewProfileResponse> viewProfile(@Path("user_id") int user_id);


    @POST(EDIT_PROFILE)
    Call<UpdateProfileResponse> updateProfile(@Body EditProfileRequest request);

    @GET(FAQ)
    Call<FaqResponse> Faq();

    @GET(COUNTRY_LIST)
    Call<CountryListResponse> country_List();


    @POST(SOCIAL_LOGIN)
    Call<SignInResponse> socialLogin(@Body SocialLoginRequest request);


    @Multipart
   /* @POST(CONTACT_US)
    Call<BaseResponse> contactUs(@Part("user_id") RequestBody user_id,
                                 @Part("email") RequestBody email,
                                 @Part("subject") RequestBody subject,
                                 @Part("description") RequestBody description,
                                 @Part("issue") RequestBody issue,
                                 @Part("help")  RequestBody help ,
                                 @Part("different_email") RequestBody different_email);
*/

    @POST(CONTACT_US)
    Call<ContactUsResponse> contactUs(@Body ContactUsRequest request);

    @POST(HOME_PAGE)
    Call<HomePageResponse> home_page(@Body HomePageRequest request);


    @POST(REFER_EARN)
    Call<ReferEarnResponse> refer_Earn(@Body ReferEarnRequest request);


    @POST(GET_PRIVACY_SETTING)
    Call<GetPrivacySettingResponse> get_Privacy_Setting(@Body GetPrivacysSettingRequest request);

    @POST(PRIVACY_SETTING)
    Call<BaseResponse> privacy_Setting(@Body PrivacySettingRequest request);


    @POST(FIND_CLASS)
    Call<FindClassResponse> find_Class(@Body FindClassRequest request);


    @POST(FIND_CLASS)
    Call<FindClassTomorrowResponse> find_Class_Tomorrow(@Body FindClassTomorrowRequest request);


    @POST(SAVE_FILE)
    Call<SaveFileResponse> uploadImages(@Body UploadImagesRequest request);


    @POST(ADD_PHONE_NUMBER)
    Call<AddPhoneNumberResponse> addPhoneNumber(@Body AddPhoneNumberRequest request);

    @POST(VIDEOS)
    Call<VideosResponse> videos_list(@Body VideosRequest request);


    @POST(CLASS_DETAILS)
    Call<ClassDetailResponse> class_Details(@Body ClassDetailRequest request);


    @POST(CHECK_CITY)
    Call<BaseResponse> check_City(@Body CheckCityRequest request);

    @POST(STUDIO_USER_NOT_EXIST)
    Call<BaseResponse> studio_User_not_Exist(@Body StudioUserNotExistRequest request);

    @POST(COUNT_POINTS)
    Call<CountPointResponse> count_Point(@Body CountPointRequest request);


    @POST(UPCOMING_CLASSES)
    Call<UpComingClassesResponse> upComing_Classes(@Body UpComingClassesRequest request);

    @POST(FAVOURITE_USER_VIEW)
    Call<FavouriteUserViewResponse> favouriteUserView(@Body FavouriteUserViewRequest request);

    /*@Multipart
    @POST(SAVE_FILE)
    Call<SaveFileResponse> uploadImage(@Part("image\"; type=image/jpeg\"") MultipartBody.Part file,
                                       @Part("id") RequestBody id,
                                       @Part("type") RequestBody type);*/

    @GET(CATEGORY_LIST)
    Call<CategoryListResponse> category_List();


    @POST(FAVOURITE_STUDIO)
    Call<BaseResponse> favouriteStudio(@Body FavouriteStudioRequest request);


    @POST(STUDIO_REVIEW)
    Call<ReviewResponse> studioReview(@Body ReviewRequest request);

    @Multipart
    @POST(SAVE_FILE)
    Call<SaveFileResponse> uploadImage(@Part MultipartBody.Part image,
                                       @Part("id") RequestBody id,
                                       @Part("type") RequestBody type);

    @POST(RESERVE_CANCEL_CLASSES)
    Call<ReserveResponse> reserveCancelClasses(@Body ReserveRequest request);

    @POST(SAVE_UPDATE_CARD_DETAILS)
    Call<BaseResponse> saveCard(@Body SaveCardRequest request);

    @POST(GET_CARD_DETAILS)
    Call<GetCardDataResponse> getCard(@Body JsonObject jsonObject);


    @POST(UPDATE_PLAN_LIST)
    Call<BaseResponse> getUpdatePlan(@Body JsonObject jsonObject);

    @GET(PLAN_LIST)
    Call<PlanListResponse> getPlanList();

    @POST(PAST_CLASSES)
    Call<PastUserViewResponse> pastUserView(@Body PastUserViewRequest request);

    @GET(MEMBERSHIP_LIST + "/{user_id}")
    Call<ManagePlanResponse> getplan(@Path("user_id") int  userId);

   @POST(UPDATE_USER_MEMBERSHIP)
    Call<BaseResponse>updateMembership(@Body JsonObject jsonObject);

   ///Friend


    @POST(GET_USER_FRIENDS)
    Call<GetUserFriendViewResponse> getUserFriendView(@Body GetUserFriendViewRequest request);

    @POST(SEND_FRIEND_REQUEST)
    Call<BaseResponse> sendFriendRequest(@Body SendFriendRequest request);

    @POST(BLOCK_FRIEND)
    Call<BaseResponse> blockFriend(@Body BlockFriendRequest request);

    @POST(ACCEPT_FRIEND)
    Call<BaseResponse> acceptFriend(@Body AcceptFriendRequest request);

    @POST(CANCEL_FRIEND)
    Call<BaseResponse> cancelFriend(@Body CancelFriendRequest request);

    @POST(GET_BLOCK_USER_FRIENDS)
    Call<BlockFriendViewResponse> getBlockUserFriendView(@Body BlockFriendViewRequest request);

    @POST(CONFIRM_FRIENDS_REQUEST)
    Call<ConfirmFriendViewResponse> confirmFriendView(@Body ConfirmFriendViewRequest request);

    @POST(SEARCH_USER_FRIENDS)
    Call<SearchFriendViewResponse> searchFriendView(@Body SearchFriendViewRequest request);

    @POST(RECENT_CHARGES_LIST)
    Call<RecentChargeResponse> recentCharges(@Body RecentChargesRequest request);


    @POST(SUBMIT_RATING_FEEDBACK)
    Call<BaseResponse> submitRating(@Body RatingRequest request);


}
