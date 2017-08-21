package com.dinhcv.ticketmanagement.network;

import com.dinhcv.ticketmanagement.model.structure.Vehicle.VehicleResponse;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.VehicleUploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by YukiNoHara on 6/7/2017.
 */

public interface IVehicleService {
    @GET("park/{id}/vehicles")
    Call<VehicleResponse> getVehicleList(@Path("id") String id);

    @Multipart
    @POST("vehicle/check-in")
    Call<VehicleUploadResponse> updateVehicleIn(@Part("type") RequestBody type,
                                                @Part("lic_plate") RequestBody licPlate,
                                                @Part("bar_code") RequestBody barcode,
                                                @Part("park_id") RequestBody parkId,
                                                @Part MultipartBody.Part image);

    @Multipart
    @POST("vehicle/check-out")
    Call<VehicleUploadResponse> updateVehicleOut(@Part("type") RequestBody type,
                                                 @Part("lic_plate") RequestBody licPlate,
                                                 @Part("bar_code") RequestBody barcode,
                                                 @Part MultipartBody.Part image,
                                                 @Part("park_id") RequestBody parkId,
                                                 @Part("price") RequestBody price);
}
