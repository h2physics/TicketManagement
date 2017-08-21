package com.dinhcv.ticketmanagement.model.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by YukiNoHara on 5/18/2017.
 */

public class ManagerProvider extends ContentProvider{
    private static final String LOG_TAG = ManagerProvider.class.getSimpleName();
    private ManagerDbHelper mHelper;

    public static final int CODE_USER_INFO = 600;
    public static final int CODE_USER_INFO_WITH_ID = 601;
    public static final int CODE_PARK = 700;
    public static final int CODE_PARK_WITH_ID = 701;
    public static final int CODE_VEHICLE = 800;
    public static final int CODE_VEHICLE_WITH_LICENSE_PLATE = 801;
    public static final int CODE_USER_INFO_BEING_MANAGED = 900;
    public static final int CODE_USER_INFO_BEING_MANAGED_WITH_ID = 901;
    public static final int CODE_PRICE = 1000;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = ManagerContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, ManagerContract.PATH_PARK, CODE_PARK);
        uriMatcher.addURI(authority, ManagerContract.PATH_PARK + "/#", CODE_PARK_WITH_ID);
        uriMatcher.addURI(authority, ManagerContract.PATH_VEHICLE, CODE_VEHICLE);
        uriMatcher.addURI(authority, ManagerContract.PATH_VEHICLE + "/*", CODE_VEHICLE_WITH_LICENSE_PLATE);
        uriMatcher.addURI(authority, ManagerContract.PATH_USER_INFO, CODE_USER_INFO);
        uriMatcher.addURI(authority, ManagerContract.PATH_USER_INFO + "/#", CODE_USER_INFO_WITH_ID);
        uriMatcher.addURI(authority, ManagerContract.PATH_USER_INFO_BEING_MANAGED, CODE_USER_INFO_BEING_MANAGED);
        uriMatcher.addURI(authority, ManagerContract.PATH_USER_INFO_BEING_MANAGED + "/#", CODE_USER_INFO_BEING_MANAGED_WITH_ID);
        uriMatcher.addURI(authority, ManagerContract.PATH_PRICE, CODE_PRICE);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        mHelper = new ManagerDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Log.e(LOG_TAG, "In Query method with match code: " + match);

        switch (match){
            case CODE_PARK: {
                cursor = db.query(ManagerContract.ParkEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_PARK_WITH_ID: {
                String id = uri.getLastPathSegment();
                String[] selectionId = new String[]{id};
                cursor = db.query(ManagerContract.ParkEntry.TABLE_NAME,
                        projection,
                        ManagerContract.ParkEntry.COLUMN_ID + "=?",
                        selectionId,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_VEHICLE: {
                cursor = db.query(ManagerContract.VehicleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_VEHICLE_WITH_LICENSE_PLATE: {
                String licensePlate = uri.getLastPathSegment();
                String[]  selectionLicPlate = new String[]{licensePlate};
                cursor = db.query(ManagerContract.VehicleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }
            case CODE_USER_INFO_BEING_MANAGED: {
                cursor = db.query(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_USER_INFO_BEING_MANAGED_WITH_ID: {
                String id = uri.getLastPathSegment();
                String[] selectionId = new String[]{id};
                cursor = db.query(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                        projection,
                        ManagerContract.UserInfoBeingManagedEntry.COLUMN_USER_ID + "=?",
                        selectionId,
                        null,
                        null,
                        null);
                break;
            }
            case CODE_USER_INFO: {
                cursor = db.query(ManagerContract.UserInfoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_USER_INFO_WITH_ID: {
                String id = uri.getLastPathSegment();
                String[] selectionId = new String[]{id};
                cursor = db.query(ManagerContract.UserInfoEntry.TABLE_NAME,
                        projection,
                        ManagerContract.UserInfoEntry.COLUMN_USER_ID + "=?",
                        selectionId,
                        null,
                        null,
                        null);
                break;

            }
            case CODE_PRICE: {
                cursor = db.query(ManagerContract.PriceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Log.d(LOG_TAG, "In Insert method with match code: " + match);
        Uri returnUri = null;
        switch (match){
            case CODE_PARK: {
                long id = db.insert(ManagerContract.ParkEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(uri, id);
                }
                break;
            }
            case CODE_USER_INFO: {
                long id = db.insert(ManagerContract.UserInfoEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(uri, id);
                }
                break;
            }
            case CODE_USER_INFO_BEING_MANAGED: {
                long id = db.insert(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(uri, id);
                }
                break;
            }
            case CODE_VEHICLE: {
                long id = db.insert(ManagerContract.VehicleEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(uri, id);
                }
                break;
            }
            case CODE_PRICE: {
                long id = db.insert(ManagerContract.PriceEntry.TABLE_NAME,
                        null,
                        values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(uri, id);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Log.d(LOG_TAG, "In BulkInsert method with match code: " + match);
        switch (match){
            case CODE_PARK: {
                db.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues cv : values){
                        long id = db.insert(ManagerContract.ParkEntry.TABLE_NAME,
                                null,
                                cv);
                        if (id != -1) rowInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            case CODE_VEHICLE: {
                db.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues cv : values){
                        long id = db.insert(ManagerContract.VehicleEntry.TABLE_NAME,
                                null,
                                cv);
                        if (id != -1) rowInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            case CODE_USER_INFO: {
                db.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues cv : values){
                        long id = db.insert(ManagerContract.UserInfoEntry.TABLE_NAME,
                                null,
                                cv);
                        if (id != -1) rowInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            case CODE_USER_INFO_BEING_MANAGED: {
                db.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues cv : values){
                        long id = db.insert(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                                null,
                                cv);
                        if (id != -1) rowInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowDeleted = 0;
        Log.d(LOG_TAG, "In Delete method with match code: " + match);

        if (selection == null){
            selection = "1";
        }
        switch (match){
            case CODE_PARK: {
                rowDeleted = db.delete(ManagerContract.ParkEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_VEHICLE: {
                rowDeleted = db.delete(ManagerContract.VehicleEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_VEHICLE_WITH_LICENSE_PLATE: {
                String licPlate = uri.getLastPathSegment();
                String[] selectionLicPlate = new String[]{licPlate};
                rowDeleted = db.delete(ManagerContract.VehicleEntry.TABLE_NAME,
                        ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE + "=?",
                        selectionLicPlate);
                break;
            }
            case CODE_USER_INFO: {
                rowDeleted = db.delete(ManagerContract.UserInfoEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_USER_INFO_BEING_MANAGED: {
                rowDeleted = db.delete(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_USER_INFO_BEING_MANAGED_WITH_ID: {
                String id = uri.getLastPathSegment();
                String[] selectionId = new String[]{id};
                rowDeleted = db.delete(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                        ManagerContract.UserInfoBeingManagedEntry.COLUMN_USER_ID + "=?",
                        selectionId);
                break;
            }
            case CODE_PRICE: {
                rowDeleted = db.delete(ManagerContract.PriceEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowDeleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowUpdated = 0;
        Log.d(LOG_TAG, "In Update method with match code: " + match);
        if (selection == null){
            selection = "1";
        }
        switch (match){
            case CODE_PARK: {
                rowUpdated = db.update(ManagerContract.ParkEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_VEHICLE: {
                rowUpdated = db.update(ManagerContract.VehicleEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_VEHICLE_WITH_LICENSE_PLATE: {
                String licPlate = uri.getLastPathSegment();
                String[] selectionLicPlate = new String[]{licPlate};
                rowUpdated = db.update(ManagerContract.VehicleEntry.TABLE_NAME,
                        values,
                        ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE + "=?",
                        selectionLicPlate);
                break;
            }
            case CODE_USER_INFO: {
                rowUpdated = db.update(ManagerContract.UserInfoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_USER_INFO_WITH_ID: {
                String id = uri.getLastPathSegment();
                String[] selectionId = new String[]{id};
                rowUpdated = db.update(ManagerContract.UserInfoEntry.TABLE_NAME,
                        values,
                        ManagerContract.UserInfoEntry.COLUMN_USER_ID + "=?",
                        selectionId);
                break;
            }
            case CODE_USER_INFO_BEING_MANAGED: {
                rowUpdated = db.update(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_USER_INFO_BEING_MANAGED_WITH_ID: {
                String id = uri.getLastPathSegment();
                String[] selectionId = new String[]{id};
                rowUpdated = db.update(ManagerContract.UserInfoBeingManagedEntry.TABLE_NAME,
                        values,
                        ManagerContract.UserInfoBeingManagedEntry.COLUMN_USER_ID + "=?",
                        selectionId);
                break;
            }
            case CODE_PRICE: {
                rowUpdated = db.update(ManagerContract.PriceEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;
    }

    @Override
    public void shutdown() {
        mHelper.close();
        super.shutdown();
    }
}
