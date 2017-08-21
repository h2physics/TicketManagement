package com.dinhcv.ticketmanagement.model.structure.Park;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 6/9/2017.
 */

public class ParkRequest {
    @SerializedName("name")
    private String mParkName;

    @SerializedName("address")
    private String mParkAddress;

    @SerializedName("hotline")
    private String mParkHotline;

    @SerializedName("website")
    private String mParkWebsite;

    @SerializedName("type")
    private int type;

    @SerializedName("cost_block_1")
    private double cost1;

    @SerializedName("time_block_1")
    private String time1;

    @SerializedName("cost_block_2")
    private double cost2;

    @SerializedName("time_block_2")
    private String time2;

    @SerializedName("total_price")
    private double total_price;

    public ParkRequest(String mParkName, String mParkAddress, String mParkHotline, String mParkWebsite, int type, double cost1, String time1, double cost2, String time2, double total_price) {
        this.mParkName = mParkName;
        this.mParkAddress = mParkAddress;
        this.mParkHotline = mParkHotline;
        this.mParkWebsite = mParkWebsite;
        this.type = type;
        this.cost1 = cost1;
        this.time1 = time1;
        this.cost2 = cost2;
        this.time2 = time2;
        this.total_price = total_price;
    }

    public ParkRequest(String mParkName, String mParkAddress, String mParkHotline, String mParkWebsite, int type, double cost1, String time1) {
        this.mParkName = mParkName;
        this.mParkAddress = mParkAddress;
        this.mParkHotline = mParkHotline;
        this.mParkWebsite = mParkWebsite;
        this.type = type;
        this.cost1 = cost1;
        this.time1 = time1;
    }
}
