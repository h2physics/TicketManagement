package com.dinhcv.ticketmanagement.model.structure.Park;

import com.dinhcv.ticketmanagement.model.structure.Price.Price;
import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 5/24/2017.
 */

public class Park {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String mParkingName;

    @SerializedName("address")
    private String mParkingAddress;

    @SerializedName("hotline")
    private String mParkingHotline;

    @SerializedName("website")
    private String mParkingWebsite;

    @SerializedName("price")
    private Price price;

    public Park(){

    }

    public Park(int id, String mParkingName, String mParkingAddress, String mParkingHotline, String mParkingWebsite) {
        this.id = id;
        this.mParkingName = mParkingName;
        this.mParkingAddress = mParkingAddress;
        this.mParkingHotline = mParkingHotline;
        this.mParkingWebsite = mParkingWebsite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParkingName() {
        return mParkingName;
    }

    public void setParkingName(String mParkingName) {
        this.mParkingName = mParkingName;
    }

    public String getParkingAddress() {
        return mParkingAddress;
    }

    public void setParkingAddress(String mParkingAddress) {
        this.mParkingAddress = mParkingAddress;
    }

    public String getParkingHotline() {
        return mParkingHotline;
    }

    public void setParkingHotline(String mParkingHotline) {
        this.mParkingHotline = mParkingHotline;
    }

    public String getParkingWebsite() {
        return mParkingWebsite;
    }

    public void setParkingWebsite(String mParkingWebsite) {
        this.mParkingWebsite = mParkingWebsite;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Park{" +
                "id=" + id +
                ", mParkingName='" + mParkingName + '\'' +
                ", mParkingAddress='" + mParkingAddress + '\'' +
                ", mParkingHotline='" + mParkingHotline + '\'' +
                ", mParkingWebsite='" + mParkingWebsite + '\'' +
                '}';
    }
}
