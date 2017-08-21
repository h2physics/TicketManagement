package com.dinhcv.ticketmanagement.model.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by YukiNoHara on 5/18/2017.
 */

public class ManagerDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "manager.db";
    public static final int DATABASE_VERSION = 15;

    public ManagerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_VEHICLE_DATABASE = "CREATE TABLE "
                + ManagerContract.VehicleEntry.TABLE_NAME + " ("
                + ManagerContract.VehicleEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + ManagerContract.VehicleEntry.COLUMN_TYPE + " TEXT NOT NULL, "
                + ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE + " TEXT NOT NULL, "
                + ManagerContract.VehicleEntry.COLUMN_BARCODE + " TEXT NOT NULL, "
                + ManagerContract.VehicleEntry.COLUMN_TIME_IN + " DATETIME NOT NULL, "
                + ManagerContract.VehicleEntry.COLUMN_TIME_OUT + " DATETIME, "
                + ManagerContract.VehicleEntry.COLUMN_STATUS + " INTEGER NOT NULL, "
                + ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_IN + " TEXT NOT NULL, "
                + ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_OUT + " TEXT, "
                + ManagerContract.VehicleEntry.COLUMN_PRICE + " REAL, "
                + ManagerContract.VehicleEntry.COLUMN_PARKING_ID + " INTEGER FOREIGNKEY REFERENCES "
                + ManagerContract.ParkEntry.TABLE_NAME + "(" + ManagerContract.ParkEntry.COLUMN_ID + ")"
                + ");";

        final String SQL_CREATE_PARK_DATABASE = "CREATE TABLE "
                + ManagerContract.ParkEntry.TABLE_NAME + " ("
                + ManagerContract.ParkEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + ManagerContract.ParkEntry.COLUMN_PARK_NAME + " TEXT, "
                + ManagerContract.ParkEntry.COLUMN_PARK_ADDRESS + " TEXT, "
                + ManagerContract.ParkEntry.COLUMN_PARK_HOTLINE + " TEXT, "
                + ManagerContract.ParkEntry.COLUMN_PARK_WEBSITE + " TEXT"
                + ");";

        final String SQL_CREATE_USER_INFO_DATABASE = "CREATE TABLE "
                + ManagerContract.UserInfoEntry.TABLE_NAME + " ("
                + ManagerContract.UserInfoEntry.COLUMN_USER_ID + " INTEGER NOT NULL, "
                + ManagerContract.UserInfoEntry.COLUMN_USERNAME + " TEXT NOT NULL, "
                + ManagerContract.UserInfoEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + ManagerContract.UserInfoEntry.COLUMN_ROLE + " INTEGER NOT NULL, "
                + ManagerContract.UserInfoEntry.COLUMN_PARK_ID + " INTEGER, "
                + ManagerContract.UserInfoEntry.COLUMN_WORKING_SHIFT + " INTEGER, "
                + ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_1 + " REAL, "
                + ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_2 + " REAL, "
                + ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_1 + " TEXT, "
                + ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_2 + " TEXT, "
                + ManagerContract.UserInfoEntry.COLUMN_IP + " TEXT, "
                + ManagerContract.UserInfoEntry.COLUMN_LOGIN_TIME + " TEXT, "
                + ManagerContract.UserInfoEntry.COLUMN_LOGOUT_TIME + " TEXT"
                + ");";

        final String SQL_CREATE_USER_INFO_BEING_MANAGED_DATABASE = "CREATE TABLE "
                + ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME + " ("
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_USER_ID + " INTEGER NOT NULL, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_USERNAME + " TEXT NOT NULL, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_ROLE + " INTEGER NOT NULL, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_PARK_ID + " INTEGER, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_WORKING_SHIFT + " INTEGER, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_COST_BLOCK_1 + " REAL, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_COST_BLOCK_2 + " REAL, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_TIME_BLOCK_1 + " TEXT, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_TIME_BLOCK_2 + " TEXT, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_IP + " TEXT, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_LOGIN_TIME + " TEXT, "
                + ManagerContract.UserInfoBeingManagedEntry.COLUMN_LOGOUT_TIME + " TEXT"
                + ");";

        final String SQL_CREATE_PRICE_DATABASE = "CREATE TABLE "
                + ManagerContract.PriceEntry.TABLE_NAME + " ("
                + ManagerContract.PriceEntry.COLUMN_TYPE + " INTEGER, "
                + ManagerContract.PriceEntry.COLUMN_COST_BLOCK_1 + " REAL, "
                + ManagerContract.PriceEntry.COLUMN_TIME_BLOCK_1 + " TEXT, "
                + ManagerContract.PriceEntry.COLUMN_COST_BLOCK_2 + " REAL, "
                + ManagerContract.PriceEntry.COLUMN_TIME_BLOCK_2 + " TEXT, "
                + ManagerContract.PriceEntry.COLUMN_LIMITED_PRICE + " REAL"
                + ");";

        db.execSQL(SQL_CREATE_PARK_DATABASE);
        Log.e("CREATE PARK", SQL_CREATE_PARK_DATABASE);
        db.execSQL(SQL_CREATE_VEHICLE_DATABASE);
        Log.e("CREATE VEHICLE", SQL_CREATE_VEHICLE_DATABASE);
        db.execSQL(SQL_CREATE_USER_INFO_DATABASE);
        Log.e("CREATE USER INFO", SQL_CREATE_USER_INFO_DATABASE);
        db.execSQL(SQL_CREATE_USER_INFO_BEING_MANAGED_DATABASE);
        Log.e("CREATE USER MANAGED", SQL_CREATE_USER_INFO_BEING_MANAGED_DATABASE);
        db.execSQL(SQL_CREATE_PRICE_DATABASE);
        Log.e("CREATE PRICE", SQL_CREATE_PRICE_DATABASE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ManagerContract.UserInfoEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ManagerContract.ParkEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ManagerContract.VehicleEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + ManagerContract.PriceEntry.TABLE_NAME + ";");
        onCreate(db);
    }
}
