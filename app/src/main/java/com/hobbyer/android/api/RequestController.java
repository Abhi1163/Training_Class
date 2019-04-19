package com.hobbyer.android.api;

import android.text.TextUtils;

import com.hobbyer.android.BuildConfig;
import com.hobbyer.android.prefs.PreferenceUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Controller class for handling all network calls.
 */
public final class RequestController {

    private static String TAG = "RequestController";
    private static String HEADER_USER_ID = "loggedUserId";
    private static String HEADER_ACCESS_TOKEN = "accessToken";
    private static String HEADER_PERSONA_TYPE = "personaType";
    private static String HEADER_CONTENT_TYPE = "Content-Type";
    private static String MULTI_PART = "multipart/form-data";
    private static String HEADER_ACCEPT = "accept";
    private static String APPLICATION_JSON = "application/json";
    private static RequestController requestController;
    private static boolean from;
    private static Retrofit retrofit;
    private static HttpLoggingInterceptor logger = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static Interceptor headerInterceptor;
    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(logger)
    .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(2 * 60 * 1000, TimeUnit.SECONDS);
            /*.readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES);*/
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static RequestController getInstance() {
        if (requestController == null) {
            requestController = new RequestController();
        }
        return requestController;
    }

   /* public static <S> S createService(Class<S> serviceClass, boolean from) {
        RequestController.from = from;
        return createService(serviceClass, false);
    }*/

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass, boolean from) {
        RequestController.from = from;
        if (headerInterceptor == null || !okHttpClient.interceptors().contains(headerInterceptor)) {
            addNetworkInterceptor(from);
        }
        Retrofit retrofit = builder.client(okHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private static void addNetworkInterceptor(boolean isFrom) {

        headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Response response = null;
//                String accessToken = PreferenceUtils.getKeyUserToken();
                String accessToken = "cf8218b6e13e6e96d5e0dd1b814b4fbc";
                if (!TextUtils.isEmpty(accessToken)) {
                    Request.Builder requestBuilder;
                    if (from) {
                        requestBuilder = original.newBuilder()
                                /*.header(HEADER_USER_ID, userId)*/
                                .header(HEADER_ACCESS_TOKEN, accessToken)
                                .header(HEADER_CONTENT_TYPE, MULTI_PART)
                                .method(original.method(), original.body());
                        response = chain.proceed(requestBuilder.build());
                    } else {

                        requestBuilder = original.newBuilder()
                                /*.header(HEADER_USER_ID, userId)*/
                                .header(HEADER_ACCESS_TOKEN, accessToken)
                                .header(HEADER_CONTENT_TYPE, APPLICATION_JSON)
                                .method(original.method(), original.body());
                        response = chain.proceed(requestBuilder.build());
                    }


                } else {
                    response = chain.proceed(original);
                }

                return response;
            }
        };

        okHttpClient.addInterceptor(headerInterceptor);
    }

}
