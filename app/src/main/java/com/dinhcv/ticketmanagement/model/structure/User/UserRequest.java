package com.dinhcv.ticketmanagement.model.structure.User;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 6/1/2017.
 */

public class UserRequest {
    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("name")
    private String mName;

    @SerializedName("role")
    private int role;

    @SerializedName("park_id")
    private int park_id;

    public UserRequest(String mEmail, String mPassword, String mName, int role, int park_id) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mName = mName;
        this.role = role;
        this.park_id = park_id;
    }
}
