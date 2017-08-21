package com.dinhcv.ticketmanagement.network;

import com.dinhcv.ticketmanagement.model.structure.Delete.DeleteResponse;
import com.dinhcv.ticketmanagement.model.structure.User.UserInfoListResponse;
import com.dinhcv.ticketmanagement.model.structure.User.UserInfoResponse;
import com.dinhcv.ticketmanagement.model.structure.User.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by YukiNoHara on 6/7/2017.
 */

public interface IUserService {
    @GET("me")
    Call<UserInfoResponse> getUserInfo();

    @GET("logout")
    Call<UserInfoResponse> updateUserTimeLogout();

    @DELETE("user/{id}")
    Call<DeleteResponse> deleteUser(@Path("id") String id);

    @GET("me")
    Call<UserInfoResponse> updateUserTimeLogin();

    @GET("user")
    Call<UserInfoListResponse> getUserBeingManaged();

    @POST("user")
    Call<UserInfoResponse> createUser(@Body UserRequest userRequest);
}
