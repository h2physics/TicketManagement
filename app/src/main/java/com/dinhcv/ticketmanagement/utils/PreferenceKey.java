package com.dinhcv.ticketmanagement.utils;

/**
 * Created by YukiNoHara on 5/17/2017.
 */

public class PreferenceKey {
    //SharePreferences
    public static final String PREF_NAME = "manager_preference";
    public static final String PREF_IS_LOGIN = "is_login";
    public static final String PREF_USER_PERMISSION = "user_permission";
    public static final String PREF_USERNAME = "username";
    public static final String PREF_ACCOUNT = "account";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_LICENSE_CODE = "licensecode";
    public static final String PREF_BLOCK_NUMBER_1 = "block1";
    public static final String PREF_BLOCK_NUMBER_2 = "block2";
    public static final String PREF_MONEY_1 = "money1";
    public static final String PREF_MONEY_2 = "money2";
    public static final String PREF_PARKING_NAME = "parking_name";
    public static final String PREF_PARKING_ADDRESS = "parking_address";
    public static final String PREF_PARKING_HOTLINE = "parking_hotline";
    public static final String PREF_PARKING_WEBSITE = "parking_website";
    public static final String PREF_ACCESS_TOKEN = "access_token";
    public static final String PREF_REFRESH_TOKEN = "refresh_token";

    //Extra Intent
    public static final String EXTRA_TICKET = "ticket";
    public static final String EXTRA_BARCODE = "barcode";
    public static final String EXTRA_VEHICLE = "vehicle";
    public static final String EXTRA_VEHICLE_ID = "vehicle_id";

    //Index Park database
    public static final int INDEX_PARK_ID = 0;
    public static final int INDEX_PARK_NAME = 1;
    public static final int INDEX_PARK_ADDRESS = 2;
    public static final int INDEX_PARK_HOTLINE = 3;
    public static final int INDEX_PARK_WEBSITE = 4;

    //Index Vehicle database
    public static final int INDEX_VEHICLE_ID = 0;
    public static final int INDEX_VEHICLE_TYPE = 1;
    public static final int INDEX_VEHICLE_LICENSE_PLATE = 2;
    public static final int INDEX_VEHICLE_BARCODE = 3;
    public static final int INDEX_VEHICLE_TIME_IN = 4;
    public static final int INDEX_VEHICLE_TIME_OUT = 5;
    public static final int INDEX_VEHICLE_STATUS = 6;
    public static final int INDEX_VEHICLE_IMAGE_CAR_IN = 7;
    public static final int INDEX_VEHICLE_IMAGE_CAR_OUT = 8;
    public static final int INDEX_VEHICLE_PRICE = 9;
    public static final int INDEX_VEHICLE_PARK_ID = 10;

    //Index UserInfo database
    public static final int INDEX_USER_INFO_ID = 0;
    public static final int INDEX_USER_INFO_USERNAME = 1;
    public static final int INDEX_USER_INFO_EMAIL = 2;
    public static final int INDEX_USER_INFO_ROLE = 3;
    public static final int INDEX_USER_INFO_PARK_ID = 4;
    public static final int INDEX_USER_INFO_WORKING_SHIFT = 5;
    public static final int INDEX_USER_INFO_COST_BLOCK_1 = 6;
    public static final int INDEX_USER_INFO_COST_BLOCK_2 = 7;
    public static final int INDEX_USER_INFO_TIME_BLOCK_1 = 8;
    public static final int INDEX_USER_INFO_TIME_BLOCK_2 = 9;
    public static final int INDEX_USER_INFO_IP = 10;
    public static final int INDEX_USER_INFO_LOGIN_TIME = 11;
    public static final int INDEX_USER_INFO_LOGOUT_TIME = 12;

    //Index Price database
    public static final int INDEX_PRICE_TYPE = 0;
    public static final int INDEX_PRICE_COST_BLOCK_1 = 1;
    public static final int INDEX_PRICE_TIME_BLOCK_1 = 2;
    public static final int INDEX_PRICE_COST_BLOCK_2 = 3;
    public static final int INDEX_PRICE_TIME_BLOCK_2 = 4;
    public static final int INDEX_PRICE_LIMITED_PRICE = 5;
}
