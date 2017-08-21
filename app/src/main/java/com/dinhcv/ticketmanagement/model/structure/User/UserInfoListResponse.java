package com.dinhcv.ticketmanagement.model.structure.User;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YukiNoHara on 6/2/2017.
 */

public class UserInfoListResponse {
    @SerializedName("data")
    private List<UserInformation> list;

    public List<UserInformation> getList() {
        return list;
    }

}
