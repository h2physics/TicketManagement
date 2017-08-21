package com.dinhcv.ticketmanagement.model.structure.User;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 5/31/2017.
 */

public class UserInfoResponse {
    @SerializedName("data")
    UserInformation userInformation;

    public UserInformation getUserInformation() {
        return userInformation;
    }
}
