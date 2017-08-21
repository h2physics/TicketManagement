package com.dinhcv.ticketmanagement.network;

import com.dinhcv.ticketmanagement.model.structure.AccessToken.AccessToken;
import com.dinhcv.ticketmanagement.model.structure.AccessToken.AccessTokenBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by YukiNoHara on 6/7/2017.
 */

public interface IAccessTokenService {
    @POST("token")
    Call<AccessToken> getAccessTokenData(@Body AccessTokenBody accessTokenBody);
}
