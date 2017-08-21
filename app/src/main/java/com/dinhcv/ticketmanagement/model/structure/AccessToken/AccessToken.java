package com.dinhcv.ticketmanagement.model.structure.AccessToken;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 5/29/2017.
 */

public class AccessToken {
    @SerializedName("token_type")
    private String mTokenType;

    @SerializedName("access_token")
    private String mAccessToken;

    @SerializedName("refresh_token")
    private String mRefreshToken;

    public String getTokenType() {
        return mTokenType;
    }

    public void setTokenType(String mTokenType) {
        this.mTokenType = mTokenType;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "mTokenType='" + mTokenType + '\'' +
                ", mAccessToken='" + mAccessToken + '\'' +
                ", mRefreshToken='" + mRefreshToken + '\'' +
                '}';
    }
}
