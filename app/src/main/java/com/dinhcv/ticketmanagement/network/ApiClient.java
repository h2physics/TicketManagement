package com.dinhcv.ticketmanagement.network;

import android.content.Context;
import android.util.Log;

import com.dinhcv.ticketmanagement.BuildConfig;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.utils.PreferenceUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YukiNoHara on 5/24/2017.
 */

public class ApiClient {

    public static Retrofit getTokenClient(Context context){
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url_get_token))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient(final Context context){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest
                                .newBuilder()
                                .header("Accept", "application/json");
                        String token = PreferenceUtils.getTokenReferences(context);
                        setAuthHeader(builder, token);
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                });

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .callFactory(httpClientBuilder.build())
                .build();

    }
    private static void setAuthHeader(Request.Builder builder, String token) {
        if(token != null) {
            builder.header("Authorization", String.format("Bearer %s", token));
        }
    }
}
