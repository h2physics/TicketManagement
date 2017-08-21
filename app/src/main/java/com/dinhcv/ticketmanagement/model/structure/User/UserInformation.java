package com.dinhcv.ticketmanagement.model.structure.User;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 5/24/2017.
 */

public class UserInformation {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String mName;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("role")
    private Role mRole;

    @SerializedName("park_id")
    private int parkId;

    @SerializedName("working_shift")
    private int workingShift;

    @SerializedName("cost_block_1")
    private double costBlock1;

    @SerializedName("cost_block_2")
    private double costBlock2;

    @SerializedName("time_block_1")
    private String mTimeBlock1;

    @SerializedName("time_block_2")
    private String mTimeBlock2;

    @SerializedName("ip")
    private String mIp;

    @SerializedName("login_time")
    private String mLoginTime;

    @SerializedName("logout_time")
    private String mLogoutTime;

    public UserInformation(){

    }

    public UserInformation(int id, String mName, String mEmail, Role mRole, int parkId, int workingShift, double costBlock1, double costBlock2, String mTimeBlock1, String mTimeBlock2, String mIp, String mLoginTime, String mLogoutTime) {
        this.id = id;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mRole = mRole;
        this.parkId = parkId;
        this.workingShift = workingShift;
        this.costBlock1 = costBlock1;
        this.costBlock2 = costBlock2;
        this.mTimeBlock1 = mTimeBlock1;
        this.mTimeBlock2 = mTimeBlock2;
        this.mIp = mIp;
        this.mLoginTime = mLoginTime;
        this.mLogoutTime = mLogoutTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public Role getRole() {
        return mRole;
    }

    public void setRole(Role mRole) {
        this.mRole = mRole;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public int getWorkingShift() {
        return workingShift;
    }

    public void setWorkingShift(int workingShift) {
        this.workingShift = workingShift;
    }

    public double getCostBlock1() {
        return costBlock1;
    }

    public void setCostBlock1(double costBlock1) {
        this.costBlock1 = costBlock1;
    }

    public double getCostBlock2() {
        return costBlock2;
    }

    public void setCostBlock2(double costBlock2) {
        this.costBlock2 = costBlock2;
    }

    public String getTimeBlock1() {
        return mTimeBlock1;
    }

    public void setTimeBlock1(String mTimeBlock1) {
        this.mTimeBlock1 = mTimeBlock1;
    }

    public String getTimeBlock2() {
        return mTimeBlock2;
    }

    public void setTimeBlock2(String mTimeBlock2) {
        this.mTimeBlock2 = mTimeBlock2;
    }

    public String getIp() {
        return mIp;
    }

    public void setIp(String mIp) {
        this.mIp = mIp;
    }

    public String getLoginTime() {
        return mLoginTime;
    }

    public void setLoginTime(String mLoginTime) {
        this.mLoginTime = mLoginTime;
    }

    public String getLogoutTime() {
        return mLogoutTime;
    }

    public void setLogoutTime(String mLogoutTime) {
        this.mLogoutTime = mLogoutTime;
    }

    public static class Role{
        @SerializedName("id")
        private int roleId;

        @SerializedName("name")
        private String mNameRole;

        @SerializedName("display_name")
        private String mDisplayName;

        public Role(int roleId) {
            this.roleId = roleId;
        }

        public int getRoleId() {
            return roleId;
        }

        public String getNameRole() {
            return mNameRole;
        }

        public String getDisplayName() {
            return mDisplayName;
        }

        @Override
        public String toString() {
            return "Role{" +
                    "roleId=" + roleId +
                    ", mNameRole='" + mNameRole + '\'' +
                    ", mDisplayName='" + mDisplayName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "id=" + id +
                ", mName='" + mName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mRole=" + mRole.toString() +
                ", parkId=" + parkId +
                ", workingShift=" + workingShift +
                ", costBlock1=" + costBlock1 +
                ", costBlock2=" + costBlock2 +
                ", mTimeBlock1='" + mTimeBlock1 + '\'' +
                ", mTimeBlock2='" + mTimeBlock2 + '\'' +
                ", mIp='" + mIp + '\'' +
                ", mLoginTime='" + mLoginTime + '\'' +
                ", mLogoutTime='" + mLogoutTime + '\'' +
                '}';
    }
}
