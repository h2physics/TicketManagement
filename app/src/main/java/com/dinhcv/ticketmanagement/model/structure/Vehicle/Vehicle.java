package com.dinhcv.ticketmanagement.model.structure.Vehicle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YukiNoHara on 5/24/2017.
 */

public class Vehicle implements Serializable{
    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private String mTypeVehicle;

    @SerializedName("park_id")
    private int parkId;

    @SerializedName("lic_plate")
    private String mLicensePlate;

    @SerializedName("bar_code")
    private String mBarcode;

    @SerializedName("time_in")
    private String mTimeIn;

    @SerializedName("time_out")
    private String mTimeOut;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("image_in")
    private String mImageCarIn;

    @SerializedName("image_out")
    private String mImageCarOut;

    @SerializedName("price")
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeVehicle() {
        return mTypeVehicle;
    }

    public void setTypeVehicle(String mTypeVehicle) {
        this.mTypeVehicle = mTypeVehicle;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public String getLicensePlate() {
        return mLicensePlate;
    }

    public Vehicle(){

    }

    public Vehicle(String mTimeOut, String mImageCarOut, double price) {
        this.mTimeOut = mTimeOut;
        this.mImageCarOut = mImageCarOut;
        this.price = price;
    }

    public Vehicle(String mTypeVehicle, int parkId, String mLicensePlate, String mBarcode, String mTimeIn, String mTimeOut, String mStatus, String mImageCarIn, String mImageCarOut, double price) {

        this.mTypeVehicle = mTypeVehicle;
        this.parkId = parkId;
        this.mLicensePlate = mLicensePlate;
        this.mBarcode = mBarcode;
        this.mTimeIn = mTimeIn;
        this.mTimeOut = mTimeOut;
        this.mStatus = mStatus;
        this.mImageCarIn = mImageCarIn;
        this.mImageCarOut = mImageCarOut;
        this.price = price;
    }

    public Vehicle(int id, String mTypeVehicle, int parkId, String mLicensePlate, String mBarcode, String mTimeIn, String mTimeOut, String mStatus, String mImageCarIn, String mImageCarOut, double price) {
        this.id = id;
        this.mTypeVehicle = mTypeVehicle;
        this.parkId = parkId;
        this.mLicensePlate = mLicensePlate;
        this.mBarcode = mBarcode;
        this.mTimeIn = mTimeIn;
        this.mTimeOut = mTimeOut;
        this.mStatus = mStatus;
        this.mImageCarIn = mImageCarIn;
        this.mImageCarOut = mImageCarOut;
        this.price = price;
    }

    public void setLicensePlate(String mLicensePlate) {
        this.mLicensePlate = mLicensePlate;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public void setBarcode(String mBarcode) {
        this.mBarcode = mBarcode;
    }

    public String getTimeIn() {
        return mTimeIn;
    }

    public void setTimeIn(String mTimeIn) {
        this.mTimeIn = mTimeIn;
    }

    public String getTimeOut() {
        return mTimeOut;
    }

    public void setTimeOut(String mTimeOut) {
        this.mTimeOut = mTimeOut;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getImageCarIn() {
        return mImageCarIn;
    }

    public void setImageCarIn(String mImageCarIn) {
        this.mImageCarIn = mImageCarIn;
    }

    public String getImageCarOut() {
        return mImageCarOut;
    }

    public void setImageCarOut(String mImageCarOut) {
        this.mImageCarOut = mImageCarOut;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", mTypeVehicle='" + mTypeVehicle + '\'' +
                ", parkId=" + parkId +
                ", mLicensePlate='" + mLicensePlate + '\'' +
                ", mBarcode='" + mBarcode + '\'' +
                ", mTimeIn='" + mTimeIn + '\'' +
                ", mTimeOut='" + mTimeOut + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mImageCarIn='" + mImageCarIn + '\'' +
                ", mImageCarOut='" + mImageCarOut + '\'' +
                ", price=" + price +
                '}';
    }
}
