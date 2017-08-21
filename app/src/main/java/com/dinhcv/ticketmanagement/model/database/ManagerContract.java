package com.dinhcv.ticketmanagement.model.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by YukiNoHara on 5/18/2017.
 */

public class ManagerContract {
    public static final String CONTENT_AUTHORITY = "com.dinhcv.ticketmanagement";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VEHICLE = "vehicles";
    public static final String PATH_PARK = "parks";
    public static final String PATH_USER_INFO = "user_information";
    public static final String PATH_USER_INFO_BEING_MANAGED = "user_being_managed";
    public static final String PATH_PRICE = "prices";

    public static final class VehicleEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_VEHICLE).build();

        public static final String TABLE_NAME = "vehicle";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_PARKING_ID = "parking_id";
        public static final String COLUMN_LICENSE_PLATE = "lic_plate";
        public static final String COLUMN_BARCODE = "barcode";
        public static final String COLUMN_TIME_IN = "time_in";
        public static final String COLUMN_TIME_OUT = "time_out";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_IMAGE_CAR_IN = "image_in";
        public static final String COLUMN_IMAGE_CAR_OUT = "image_out";
        public static final String COLUMN_PRICE = "price";
        public static Uri buildUriWithLicenseCode(String licensePlate){
            return CONTENT_URI.buildUpon()
                    .appendPath(licensePlate)
                    .build();
        }
    }

    public static final class ParkEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_PARK).build();

        public static final String TABLE_NAME = "park";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PARK_NAME = "park_name";
        public static final String COLUMN_PARK_ADDRESS = "park_address";
        public static final String COLUMN_PARK_HOTLINE = "park_hotline";
        public static final String COLUMN_PARK_WEBSITE = "park_website";

        public static Uri buildUriWithId(int id){
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(id))
                    .build();
        }
    }

    public static final class UserInfoEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_USER_INFO).build();

        public static final String TABLE_NAME = "user_info";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_PARK_ID = "park_id";
        public static final String COLUMN_WORKING_SHIFT = "working_shift";
        public static final String COLUMN_COST_BLOCK_1 = "cost_block_1";
        public static final String COLUMN_COST_BLOCK_2 = "cost_block_2";
        public static final String COLUMN_TIME_BLOCK_1 = "time_block_1";
        public static final String COLUMN_TIME_BLOCK_2 = "time_block_2";
        public static final String COLUMN_IP = "ip";
        public static final String COLUMN_LOGIN_TIME = "login_time";
        public static final String COLUMN_LOGOUT_TIME = "logout_time";
        public static Uri buildUriWithUserId(int userId){
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(userId))
                    .build();
        }
    }

    public static final class UserInfoBeingManagedEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_USER_INFO_BEING_MANAGED).build();

        public static final String TABLE_NAME = "user_info_being_managed";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_PARK_ID = "park_id";
        public static final String COLUMN_WORKING_SHIFT = "working_shift";
        public static final String COLUMN_COST_BLOCK_1 = "cost_block_1";
        public static final String COLUMN_COST_BLOCK_2 = "cost_block_2";
        public static final String COLUMN_TIME_BLOCK_1 = "time_block_1";
        public static final String COLUMN_TIME_BLOCK_2 = "time_block_2";
        public static final String COLUMN_IP = "ip";
        public static final String COLUMN_LOGIN_TIME = "login_time";
        public static final String COLUMN_LOGOUT_TIME = "logout_time";
        public static Uri buildUriWithUserId(int userId){
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(userId))
                    .build();
        }
    }

    public static final class PriceEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_PRICE).build();

        public static final String TABLE_NAME = "price";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COST_BLOCK_1 = "cost_block_1";
        public static final String COLUMN_TIME_BLOCK_1 = "time_block_1";
        public static final String COLUMN_COST_BLOCK_2 = "cost_block_2";
        public static final String COLUMN_TIME_BLOCK_2 = "time_block_2";
        public static final String COLUMN_LIMITED_PRICE = "limited_price";
    }
}
