package com.dinhcv.ticketmanagement.network;

import com.dinhcv.ticketmanagement.model.structure.Park.ParkRequest;
import com.dinhcv.ticketmanagement.model.structure.Park.ParkResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by YukiNoHara on 6/7/2017.
 */

public interface IParkService {
    @GET("park/{id}")
    Call<ParkResponse> getPark(@Path("id") String id);

    @POST("park/{id}")
    Call<ParkResponse> updatePark(@Path("id") String id,
                                  @Body ParkRequest parkRequest);
}
