package com.dinhcv.ticketmanagement.model.structure.AccessToken;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 5/29/2017.
 */

public class AccessTokenBody {
    @SerializedName("client_id")
    private String client_id;

    @SerializedName("client_secret")
    private String client_secret;

    @SerializedName("grant_type")
    private String grant_type;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public AccessTokenBody(String client_id, String client_secret, String grant_type, String username, String password) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.grant_type = grant_type;
        this.username = username;
        this.password = password;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccessTokenBody{" +
                "client_id='" + client_id + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", grant_type='" + grant_type + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
