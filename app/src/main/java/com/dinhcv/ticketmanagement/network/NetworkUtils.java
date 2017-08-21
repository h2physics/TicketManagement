package com.dinhcv.ticketmanagement.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.AccessToken.AccessToken;
import com.dinhcv.ticketmanagement.model.structure.AccessToken.AccessTokenBody;
import com.dinhcv.ticketmanagement.model.structure.Delete.DeleteResponse;
import com.dinhcv.ticketmanagement.model.structure.Park.Park;
import com.dinhcv.ticketmanagement.model.structure.Park.ParkRequest;
import com.dinhcv.ticketmanagement.model.structure.Park.ParkResponse;
import com.dinhcv.ticketmanagement.model.structure.Price.Price;
import com.dinhcv.ticketmanagement.model.structure.User.UserInfoListResponse;
import com.dinhcv.ticketmanagement.model.structure.User.UserInfoResponse;
import com.dinhcv.ticketmanagement.model.structure.User.UserInformation;
import com.dinhcv.ticketmanagement.model.structure.User.UserRequest;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.VehicleResponse;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.VehicleUploadResponse;
import com.dinhcv.ticketmanagement.utils.DataUtils;
import com.dinhcv.ticketmanagement.utils.PreferenceKey;
import com.dinhcv.ticketmanagement.utils.PreferenceUtils;
import java.io.File;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YukiNoHara on 5/29/2017.
 */

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void getAccessTokenFromServer(final Context context, AccessTokenBody accessTokenBody, final NetworkExecuteCompleted callback){
        final IAccessTokenService iAccessTokenService = ApiClient.getTokenClient(context).create(IAccessTokenService.class);
        Log.e(LOG_TAG, "Token body: " + accessTokenBody.toString());
        Call<AccessToken> call = iAccessTokenService.getAccessTokenData(accessTokenBody);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

               if (response.isSuccessful() && response.code() == 200){
                   AccessToken accessToken = response.body();
                   preferences = context.getSharedPreferences(PreferenceKey.PREF_NAME, Context.MODE_PRIVATE);
                   editor = preferences.edit();
                   if (!accessToken.getAccessToken().isEmpty()){
                       PreferenceUtils.setTokenReferences(context, accessToken.getAccessToken());
                   }
                   editor.putString(PreferenceKey.PREF_REFRESH_TOKEN, accessToken.getRefreshToken());
                   editor.apply();
                   callback.onSuccess();
               } else {
                   callback.onError();
               }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(LOG_TAG, "on failure");
                PreferenceUtils.setIsLogin(context, false);
                callback.onFailed();
            }
        });

    }

    public static void getUserInfoFromServer(final Context context, final NetworkExecuteCompleted callback){
        final IUserService iUserService = ApiClient.getClient(context).create(IUserService.class);

        Call<UserInfoResponse> call = iUserService.getUserInfo();
        call.enqueue(new Callback<UserInfoResponse>() {
                @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    Log.e(LOG_TAG, "Login isSuccessful: " + response.isSuccessful() + " - " + response.code());
                    if (response.isSuccessful() && response.code() == 200){
                        Log.e(LOG_TAG, "Success");
                        UserInformation userInformation = response.body().getUserInformation();
                        Log.e(LOG_TAG, userInformation.toString());
                        Uri uri = DataUtils.insertUserInfoFromResponse(context, userInformation);
                        PreferenceUtils.setIsLogin(context, true);
                        Log.e(LOG_TAG, "Uri: " + uri.toString());
                        if (uri.toString() != null){
                            callback.onSuccess();
                        }
                    } else {
                        Log.e(LOG_TAG, "Error");
                        callback.onError();
                    }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void getUserBeingManagedFromServer(final Context context, final NetworkExecuteCompleted callback){
        IUserService iUserService = ApiClient.getClient(context).create(IUserService.class);
        Call<UserInfoListResponse> call = iUserService.getUserBeingManaged();
        call.enqueue(new Callback<UserInfoListResponse>() {
            @Override
            public void onResponse(Call<UserInfoListResponse> call, Response<UserInfoListResponse> response) {
                if (response.isSuccessful() && response.code() == 200){
                    List<UserInformation> userList = response.body().getList();
                    Log.e(LOG_TAG, "Userlist size: " + userList.size());
                    DataUtils.insertListUserFromResponse(context, userList);
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<UserInfoListResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void postCreateUser(Context context, UserRequest userRequest, final NetworkExecuteCompleted callback){
        IUserService iUserService = ApiClient.getClient(context).create(IUserService.class);
        Call<UserInfoResponse> call = iUserService.createUser(userRequest);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.code() == 200){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void getParkFromServer(final Context context, String id, final NetworkExecuteCompleted callback){
        IParkService iParkService = ApiClient.getClient(context).create(IParkService.class);
        Call<ParkResponse> call = iParkService.getPark(id);
        call.enqueue(new Callback<ParkResponse>() {
            @Override
            public void onResponse(Call<ParkResponse> call, Response<ParkResponse> response) {
                Log.e(LOG_TAG, "Status code: " + response.code() + " - " + response.isSuccessful());
                if (response.isSuccessful() && response.code() == 200){
                    Park park = response.body().getPark();
                    Price price = park.getPrice();
                    Log.e(LOG_TAG, "Price: " + price.toString());
                    Uri uri = DataUtils.insertPark(context, park);
                    Uri uri1 = DataUtils.insertPrice(context, price);
                    Log.e(LOG_TAG, "Uri park: " + uri.toString());
                    Log.e(LOG_TAG, "Uri price: " + uri1.toString());
                    if (uri.toString() != null && uri1.toString() != null){
                        callback.onSuccess();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<ParkResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void updateCurrentPark(final Context context, String id, ParkRequest parkRequest, final NetworkExecuteCompleted callback){
        IParkService iParkService = ApiClient.getClient(context).create(IParkService.class);
        Call<ParkResponse> call = iParkService.updatePark(id, parkRequest);
        call.enqueue(new Callback<ParkResponse>() {
            @Override
            public void onResponse(Call<ParkResponse> call, Response<ParkResponse> response) {
                if (response.isSuccessful() && response.code() == 200){
                    Park park = response.body().getPark();
                    Price price = park.getPrice();
                    Log.e(LOG_TAG, "Price: " + price.toString());
                    Uri uri = DataUtils.insertPark(context, park);
                    Uri uri1 = DataUtils.insertPrice(context, price);
                    Log.e(LOG_TAG, "Uri park: " + uri.toString());
                    Log.e(LOG_TAG, "Uri price: " + uri1.toString());
                    if (uri.toString() != null && uri1.toString() != null){
                        callback.onSuccess();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<ParkResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void getVehicleListFromServer(final Context context, String id, final NetworkExecuteCompleted callback){
        IVehicleService iVehicleService = ApiClient.getClient(context).create(IVehicleService.class);
        Call<VehicleResponse> call = iVehicleService.getVehicleList(id);
        call.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                if (response.isSuccessful() && response.code() == 200){
                    List<Vehicle> list = response.body().getList();
                    DataUtils.insertVehicleList(context, list);
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void updateUserTimeLogin(Context context, final NetworkExecuteCompleted callback){
        IUserService iUserService = ApiClient.getClient(context).create(IUserService.class);
        Call<UserInfoResponse> call = iUserService.updateUserTimeLogin();
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                Log.e(LOG_TAG, "Status code: " + response.code());
                if (response.isSuccessful() && response.code() == 200){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void updateUserTimeLogout(Context context, final NetworkExecuteCompleted callback){
        IUserService iUserService = ApiClient.getClient(context).create(IUserService.class);
        Call<UserInfoResponse> call = iUserService.updateUserTimeLogout();
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.code() == 200){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void deleteUser(final Context context, final String id, final NetworkExecuteCompleted callback){
        IUserService iUserService = ApiClient.getClient(context).create(IUserService.class);
        Call<DeleteResponse> call = iUserService.deleteUser(id);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful() && response.code() == 200){
                    Uri uri = ManagerContract.UserInfoBeingManagedEntry.buildUriWithUserId(Integer.parseInt(id));
                    DataUtils.deleteUser(context, uri);
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void updateVehicleIn(final Context context, Vehicle vehicle, final NetworkExecuteCompleted callback){
        Log.e(LOG_TAG, "PATH: " + vehicle.getImageCarIn());
        File file = new File(vehicle.getImageCarIn());
        Log.e(LOG_TAG, "FILE: " + file.exists());
        RequestBody requestImage = RequestBody.create(MediaType.parse(context.getString(R.string.image_type)), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image_in", file.getName(), requestImage);
        RequestBody requestType = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), vehicle.getTypeVehicle());
        RequestBody requestLicPlate = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), vehicle.getLicensePlate());
        RequestBody requestBarcode = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), vehicle.getBarcode());
        RequestBody requestParkId = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), String.valueOf(vehicle.getParkId()));

        IVehicleService iVehicleService = ApiClient.getClient(context).create(IVehicleService.class);
        Call<VehicleUploadResponse> call = iVehicleService.updateVehicleIn(requestType, requestLicPlate, requestBarcode, requestParkId, imagePart);
        call.enqueue(new Callback<VehicleUploadResponse>() {
            @Override
            public void onResponse(Call<VehicleUploadResponse> call, Response<VehicleUploadResponse> response) {
                if (response.isSuccessful() && response.code() == 200){
                    Vehicle v = response.body().getVehicle();
                    Uri uri = DataUtils.insertVehicle(context, v);
                    if (uri != null){
                        callback.onSuccess();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<VehicleUploadResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void updateVehicleOut(final Context context, Vehicle vehicle, final NetworkExecuteCompleted callback){
        File file = new File(vehicle.getImageCarOut());
        Log.e(LOG_TAG, "FILE image out path: " + file.getAbsolutePath());

        RequestBody requestImage = RequestBody.create(MediaType.parse(context.getString(R.string.image_type)), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image_out", file.getName(), requestImage);
        RequestBody requestType = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), vehicle.getTypeVehicle());
        RequestBody requestLicPlate = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), vehicle.getLicensePlate());
        RequestBody requestBarcode = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), vehicle.getBarcode());
        RequestBody requestParkId = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), String.valueOf(vehicle.getParkId()));
        RequestBody requestPrice = RequestBody.create(MediaType.parse(context.getString(R.string.text_type)), String.valueOf(vehicle.getPrice()));

        IVehicleService iVehicleService = ApiClient.getClient(context).create(IVehicleService.class);
        Call<VehicleUploadResponse> call = iVehicleService.updateVehicleOut(requestType, requestLicPlate, requestBarcode, imagePart, requestParkId, requestPrice);
        call.enqueue(new Callback<VehicleUploadResponse>() {
            @Override
            public void onResponse(Call<VehicleUploadResponse> call, Response<VehicleUploadResponse> response) {
                if(response.isSuccessful() && response.code() == 200){
                    Vehicle v = response.body().getVehicle();
                    Uri uri = DataUtils.insertVehicle(context, v);
                    if (uri != null){
                        callback.onSuccess();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<VehicleUploadResponse> call, Throwable t) {
                callback.onFailed();
            }
        });
    }
}
