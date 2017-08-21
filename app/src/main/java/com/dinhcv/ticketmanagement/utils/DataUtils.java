package com.dinhcv.ticketmanagement.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.dinhcv.ticketmanagement.activity.ConfigActivity;
import com.dinhcv.ticketmanagement.activity.MainActivity;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Park.Park;
import com.dinhcv.ticketmanagement.model.structure.Price.Price;
import com.dinhcv.ticketmanagement.model.structure.User.UserInformation;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YukiNoHara on 5/19/2017.
 */

public class DataUtils {
    public static Cursor queryVehicle(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }

    public static Cursor queryVehicleInPark(Context context){
        return context.getContentResolver().query(ManagerContract.VehicleEntry.CONTENT_URI,
                null,
                ManagerContract.VehicleEntry.COLUMN_STATUS + "=?",
                new String[]{"1"},
                null);
    }

    public static Vehicle queryVehicleByLicenseCode(Context context, String licenseCode){
        Uri uri = ManagerContract.VehicleEntry.buildUriWithLicenseCode(licenseCode);
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                ManagerContract.VehicleEntry.COLUMN_BARCODE + "=?",
                new String[]{licenseCode},
                null,
                null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            Vehicle vehicle = new Vehicle(
                    cursor.getInt(PreferenceKey.INDEX_VEHICLE_ID),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_TYPE),
                    cursor.getInt(PreferenceKey.INDEX_VEHICLE_PARK_ID),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_LICENSE_PLATE),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_BARCODE),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_TIME_IN),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_TIME_OUT),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_STATUS),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_IMAGE_CAR_IN),
                    cursor.getString(PreferenceKey.INDEX_VEHICLE_IMAGE_CAR_OUT),
                    cursor.getDouble(PreferenceKey.INDEX_VEHICLE_PRICE)
            );
            return vehicle;
        }
        return null;
    }

    public static Cursor queryParkInfoById(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    public static Park getCurrentPark(Context context, Uri uri){
        Park park;
        Cursor cursor =  context.getContentResolver().query(uri,
                null,
                null,
                null,
                null,
                null);

        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            park = new Park(
                    cursor.getInt(ConfigActivity.INDEX_PARK_ID),
                    cursor.getString(ConfigActivity.INDEX_PARK_NAME),
                    cursor.getString(ConfigActivity.INDEX_PARK_ADDRESS),
                    cursor.getString(ConfigActivity.INDEX_PARK_HOTLINE),
                    cursor.getString(ConfigActivity.INDEX_PARK_WEBSITE)
            );
            return park;
        } else {
            return null;
        }
    }

    public static Price queryCurrentPrice(Context context, Uri uri){
        Price price;
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null,
                null);
        Log.e("COUNT", cursor.getCount() + "");
        if (cursor != null){
            cursor.moveToFirst();
            price = new Price(
                    cursor.getInt(PreferenceKey.INDEX_PRICE_TYPE),
                    cursor.getDouble(PreferenceKey.INDEX_PRICE_COST_BLOCK_1),
                    cursor.getString(PreferenceKey.INDEX_PRICE_TIME_BLOCK_1),
                    cursor.getDouble(PreferenceKey.INDEX_PRICE_COST_BLOCK_2),
                    cursor.getString(PreferenceKey.INDEX_PRICE_TIME_BLOCK_2),
                    cursor.getDouble(PreferenceKey.INDEX_PRICE_LIMITED_PRICE)
            );
            Log.e("LOG_TAG", price.toString());
            return price;
        }
        return null;
    }

    public static Cursor queryVehicleInWithDate(Context context, String time1, String time2){
        return context.getContentResolver().query(ManagerContract.VehicleEntry.CONTENT_URI,
                null,
                ManagerContract.VehicleEntry.COLUMN_TIME_IN + " BETWEEN ? AND ?",
                new String[]{time1, time2},
                null,
                null);
    }
    public static Cursor queryVehicleOutWithDate(Context context, String time1, String time2){
        return context.getContentResolver().query(ManagerContract.VehicleEntry.CONTENT_URI,
                null,
                ManagerContract.VehicleEntry.COLUMN_TIME_OUT + " BETWEEN ? AND ?",
                new String[]{time1, time2},
                null,
                null);
    }

    public static Cursor queryUserInfoById(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }

    public static List<UserInformation> getUserList(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null,
                null);

        List<UserInformation> list = new ArrayList<>();
        while (cursor.moveToNext()){
            UserInformation u = new UserInformation(
                    cursor.getInt(PreferenceKey.INDEX_USER_INFO_ID),
                    cursor.getString(PreferenceKey.INDEX_USER_INFO_USERNAME),
                    cursor.getString(PreferenceKey.INDEX_USER_INFO_EMAIL),
                    new UserInformation.Role(cursor.getInt(PreferenceKey.INDEX_USER_INFO_ROLE)),
                    cursor.getInt(PreferenceKey.INDEX_USER_INFO_PARK_ID),
                    cursor.getInt(PreferenceKey.INDEX_USER_INFO_WORKING_SHIFT),
                    cursor.getDouble(PreferenceKey.INDEX_USER_INFO_COST_BLOCK_1),
                    cursor.getDouble(PreferenceKey.INDEX_USER_INFO_COST_BLOCK_2),
                    cursor.getString(PreferenceKey.INDEX_USER_INFO_TIME_BLOCK_1),
                    cursor.getString(PreferenceKey.INDEX_USER_INFO_TIME_BLOCK_2),
                    cursor.getString(PreferenceKey.INDEX_USER_INFO_IP),
                    cursor.getString(PreferenceKey.INDEX_USER_INFO_LOGIN_TIME),
                    cursor.getString(PreferenceKey.INDEX_USER_INFO_LOGOUT_TIME)
            );
            list.add(u);
        }
        return list;
    }
    public static Cursor queryUserList(Context context, Uri uri){
        return context.getContentResolver().query(uri,
                null,
                null,
                null,
                null,
                null);

    }


    public static UserInformation queryCurrentUserInfo(Context context){
        Cursor cursor = context.getContentResolver().query(ManagerContract.UserInfoEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        UserInformation userInformation = new UserInformation(
                cursor.getInt(MainActivity.INDEX_USER_ID),
                cursor.getString(MainActivity.INDEX_NAME),
                cursor.getString(MainActivity.INDEX_EMAIL),
                new UserInformation.Role(cursor.getInt(MainActivity.INDEX_ROLE)),
                cursor.getInt(MainActivity.INDEX_PARK_ID),
                cursor.getInt(MainActivity.INDEX_WORKING_SHIFT),
                cursor.getDouble(MainActivity.INDEX_COST_BLOCK_1),
                cursor.getDouble(MainActivity.INDEX_COST_BLOCK_2),
                cursor.getString(MainActivity.INDEX_TIME_BLOCK_1),
                cursor.getString(MainActivity.INDEX_TIME_BLOCK_2),
                cursor.getString(MainActivity.INDEX_IP),
                cursor.getString(MainActivity.INDEX_LOGIN_TIME),
                cursor.getString(MainActivity.INDEX_LOGOUT_TIME)
        );
        return userInformation;
    }

//    public static int insertUserFromResponse(Context context, List<User> userList){
//        List<ContentValues> contentValueList = new ArrayList<>();
//        for (User user : userList){
//            ContentValues cv = new ContentValues();
//            cv.put(ManagerContract.UserEntry.COLUMN_USER_ID, user.getUserId());
//            cv.put(ManagerContract.UserEntry.COLUMN_ACCOUNT, user.getAccount());
//            cv.put(ManagerContract.UserEntry.COLUMN_NAME, user.getName());
//            cv.put(ManagerContract.UserEntry.COLUMN_PERMISSION, user.getPermission());
//            cv.put(ManagerContract.UserEntry.COLUMN_WORKING_SHIFT, user.getWorkingShift());
//            contentValueList.add(cv);
//        }
//
//        if (!contentValueList.isEmpty()){
//            ContentResolver contentResolver = context.getContentResolver();
//            contentResolver.delete(ManagerContract.UserEntry.CONTENT_URI,  null, null);
//            return contentResolver.bulkInsert(ManagerContract.UserEntry.CONTENT_URI,
//                                       contentValueList.toArray(new ContentValues[contentValueList.size()]));
//
//        }
//        return -1;
//    }

    public static Uri insertUserInfoFromResponse(Context context, UserInformation u){

        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.UserInfoEntry.COLUMN_USER_ID, u.getId());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_USERNAME, u.getName());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_EMAIL, u.getEmail());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_ROLE, u.getRole().getRoleId());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_PARK_ID, u.getParkId());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_WORKING_SHIFT, u.getWorkingShift());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_1, u.getCostBlock1());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_2, u.getCostBlock2());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_1, u.getTimeBlock1());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_2, u.getTimeBlock2());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_IP, u.getIp());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_LOGIN_TIME, u.getLoginTime());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_LOGOUT_TIME, u.getLogoutTime());

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(ManagerContract.UserInfoEntry.CONTENT_URI,  null, null);
        return contentResolver.insert(ManagerContract.UserInfoEntry.CONTENT_URI, cv);
    }

    public static int insertListUserFromResponse(Context context, List<UserInformation> list){
        List<ContentValues> values = new ArrayList<>();
        for (UserInformation u : list){
            ContentValues cv = new ContentValues();
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_USER_ID, u.getId());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_USERNAME, u.getName());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_EMAIL, u.getEmail());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_ROLE, u.getRole().getRoleId());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_PARK_ID, u.getParkId());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_WORKING_SHIFT, u.getWorkingShift());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_COST_BLOCK_1, u.getCostBlock1());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_COST_BLOCK_2, u.getCostBlock2());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_TIME_BLOCK_1, u.getTimeBlock1());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_TIME_BLOCK_2, u.getTimeBlock2());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_IP, u.getIp());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_LOGIN_TIME, u.getLoginTime());
            cv.put(ManagerContract.UserInfoBeingManagedEntry.COLUMN_LOGOUT_TIME, u.getLogoutTime());
            values.add(cv);
        }
        if (!values.isEmpty()){
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.delete(ManagerContract.UserInfoBeingManagedEntry.CONTENT_URI,
                    null,
                    null);
            return contentResolver.bulkInsert(ManagerContract.UserInfoBeingManagedEntry.CONTENT_URI,
                    values.toArray(new ContentValues[values.size()]));
        }
        return -1;
    }

//    public static int insertTicketFromResponse(Context context, List<Ticket> ticketList){
//        List<ContentValues> contentValuesList = new ArrayList<>();
//        for (Ticket ticket : ticketList){
//            ContentValues cv = new ContentValues();
//            cv.put(ManagerContract.TicketEntry.COLUMN_USER_ID, ticket.getUserId());
//            cv.put(ManagerContract.TicketEntry.COLUMN_TICKET_TYPE, ticket.getTicketType());
//            cv.put(ManagerContract.TicketEntry.COLUMN_LICENSE_PLATE, ticket.getLicensePlate());
//            cv.put(ManagerContract.TicketEntry.COLUMN_LICENSE_CODE, ticket.getBarcode());
//            cv.put(ManagerContract.TicketEntry.COLUMN_STATUS, ticket.getStatus());
//            cv.put(ManagerContract.TicketEntry.COLUMN_CAR_IN_IMAGE, ticket.getCarInImage());
//            cv.put(ManagerContract.TicketEntry.COLUMN_CAR_OUT_IMAGE, ticket.getCarOutImage());
//            cv.put(ManagerContract.TicketEntry.COLUMN_TIME_IN, ticket.getTimeIn());
//            cv.put(ManagerContract.TicketEntry.COLUMN_TIME_OUT, ticket.getTimeOut());
//            cv.put(ManagerContract.TicketEntry.COLUMN_FEE, ticket.getFee());
//            cv.put(ManagerContract.TicketEntry.COLUMN_TEMP, ticket.getTemp());
//            contentValuesList.add(cv);
//        }
//
//        if (!contentValuesList.isEmpty()){
//            ContentResolver contentResolver = context.getContentResolver();
//            contentResolver.delete(ManagerContract.TicketEntry.CONTENT_URI, null, null);
//            return contentResolver.bulkInsert(ManagerContract.TicketEntry.CONTENT_URI,
//                                              contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
//        }
//        return -1;
//    }

//    public static void insertLog(Context context, Log log){
//        ContentValues cv = new ContentValues();
//        cv.put(ManagerContract.LogEntry.COLUMN_ACCOUNT, log.getAccount());
//        cv.put(ManagerContract.LogEntry.COLUMN_USER_ID, log.getUserId());
//        cv.put(ManagerContract.LogEntry.COLUMN_TIME_IN, log.getTimeIn());
//        cv.put(ManagerContract.LogEntry.COLUMN_TIME_OUT, log.getTimeOut());
//
//        context.getContentResolver().insert(ManagerContract.LogEntry.CONTENT_URI, cv);
//    }
//
//    public static void insertTicket(Context context, Ticket ticket){
//        ContentValues cv = new ContentValues();
//        cv.put(ManagerContract.TicketEntry.COLUMN_USER_ID, ticket.getUserId());
//        cv.put(ManagerContract.TicketEntry.COLUMN_LICENSE_PLATE, ticket.getLicensePlate());
//        cv.put(ManagerContract.TicketEntry.COLUMN_LICENSE_CODE, ticket.getBarcode());
//        cv.put(ManagerContract.TicketEntry.COLUMN_CAR_IN_IMAGE, ticket.getCarInImage());
//        cv.put(ManagerContract.TicketEntry.COLUMN_TIME_IN, ticket.getTimeIn());
//
//        context.getContentResolver().insert(ManagerContract.TicketEntry.CONTENT_URI, cv);
//    }
    public static int insertVehicleList(Context context, List<Vehicle> list){
        List<ContentValues> values = new ArrayList<>();
        for (Vehicle vehicle : list){
            ContentValues cv = new ContentValues();
            cv.put(ManagerContract.VehicleEntry.COLUMN_ID, vehicle.getId());
            cv.put(ManagerContract.VehicleEntry.COLUMN_TYPE, vehicle.getTypeVehicle());
            cv.put(ManagerContract.VehicleEntry.COLUMN_PARKING_ID, vehicle.getParkId());
            cv.put(ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE, vehicle.getLicensePlate());
            cv.put(ManagerContract.VehicleEntry.COLUMN_BARCODE, vehicle.getBarcode());
            cv.put(ManagerContract.VehicleEntry.COLUMN_TIME_IN, vehicle.getTimeIn());
            cv.put(ManagerContract.VehicleEntry.COLUMN_TIME_OUT, vehicle.getTimeOut());
            cv.put(ManagerContract.VehicleEntry.COLUMN_STATUS, vehicle.getStatus());
            cv.put(ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_IN, vehicle.getImageCarIn());
            cv.put(ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_OUT, vehicle.getImageCarOut());
            cv.put(ManagerContract.VehicleEntry.COLUMN_PRICE, vehicle.getPrice());
            values.add(cv);
        }
        if (!values.isEmpty()){
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.delete(ManagerContract.VehicleEntry.CONTENT_URI,
                    null,
                    null);
            return contentResolver.bulkInsert(ManagerContract.VehicleEntry.CONTENT_URI,
                    values.toArray(new ContentValues[values.size()]));
        }
        return -1;
    }

    public static Uri insertVehicle(Context context, Vehicle vehicle){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.VehicleEntry.COLUMN_TYPE, vehicle.getTypeVehicle());
        cv.put(ManagerContract.VehicleEntry.COLUMN_PARKING_ID, vehicle.getParkId());
        cv.put(ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE, vehicle.getLicensePlate());
        cv.put(ManagerContract.VehicleEntry.COLUMN_BARCODE, vehicle.getBarcode());
        cv.put(ManagerContract.VehicleEntry.COLUMN_TIME_IN, vehicle.getTimeIn());
        cv.put(ManagerContract.VehicleEntry.COLUMN_STATUS, vehicle.getStatus());
        cv.put(ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_IN, vehicle.getImageCarIn());
        Cursor cursor = queryVehicle(context, ManagerContract.VehicleEntry.CONTENT_URI);
        boolean isExist = false;
        while (cursor.moveToNext()){
            if (cursor.getString(PreferenceKey.INDEX_VEHICLE_LICENSE_PLATE).equalsIgnoreCase(vehicle.getLicensePlate())){
                isExist = true;
            }
        }
        if (isExist){
            Uri uri = ManagerContract.VehicleEntry.buildUriWithLicenseCode(vehicle.getLicensePlate());
            context.getContentResolver().delete(uri,
                    null,
                    null);
        }
        return context.getContentResolver().insert(ManagerContract.VehicleEntry.CONTENT_URI, cv);
    }

    public static Uri insertPark(Context context, Park park){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.ParkEntry.COLUMN_ID, park.getId());
        cv.put(ManagerContract.ParkEntry.COLUMN_PARK_NAME, park.getParkingName());
        cv.put(ManagerContract.ParkEntry.COLUMN_PARK_ADDRESS, park.getParkingAddress());
        cv.put(ManagerContract.ParkEntry.COLUMN_PARK_HOTLINE, park.getParkingHotline());
        cv.put(ManagerContract.ParkEntry.COLUMN_PARK_WEBSITE, park.getParkingWebsite());
        context.getContentResolver().delete(ManagerContract.ParkEntry.CONTENT_URI, null, null);
        return context.getContentResolver().insert(ManagerContract.ParkEntry.CONTENT_URI, cv);
    }

    public static Uri insertPrice(Context context, Price price){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.PriceEntry.COLUMN_TYPE, price.getType());
        cv.put(ManagerContract.PriceEntry.COLUMN_COST_BLOCK_1, price.getCost_block_1());
        cv.put(ManagerContract.PriceEntry.COLUMN_TIME_BLOCK_1, price.getTime_block_1());
        cv.put(ManagerContract.PriceEntry.COLUMN_COST_BLOCK_2, price.getCost_block_2());
        cv.put(ManagerContract.PriceEntry.COLUMN_TIME_BLOCK_2, price.getTime_block_2());
        cv.put(ManagerContract.PriceEntry.COLUMN_LIMITED_PRICE, price.getTotal_price());
        context.getContentResolver().delete(ManagerContract.PriceEntry.CONTENT_URI, null, null);
        return context.getContentResolver().insert(ManagerContract.PriceEntry.CONTENT_URI, cv);
    }

//    public static int updateTicket(Context context, Ticket ticket, Uri uri){
//        ContentValues cv = new ContentValues();
//        cv.put(ManagerContract.TicketEntry.COLUMN_CAR_OUT_IMAGE, ticket.getCarOutImage());
//        cv.put(ManagerContract.TicketEntry.COLUMN_TIME_OUT, ticket.getTimeOut());
//        cv.put(ManagerContract.TicketEntry.COLUMN_FEE, ticket.getFee());
//
//        return context.getContentResolver().update(uri,
//                cv,
//                null,
//                null);
//    }

    public static int updateVehicle(Context context, Vehicle vehicle, Uri uri){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.VehicleEntry.COLUMN_STATUS, vehicle.getStatus());
        cv.put(ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_OUT, vehicle.getImageCarOut());
        cv.put(ManagerContract.VehicleEntry.COLUMN_TIME_OUT, vehicle.getTimeOut());
        cv.put(ManagerContract.VehicleEntry.COLUMN_PRICE, vehicle.getPrice());

        return context.getContentResolver().update(uri,
                cv,
                null,
                null);
    }

    public static int updateVehicleImageCarIn(Context context, Uri uri, String path){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_IN, path);
        return context.getContentResolver().update(uri,
                cv,
                ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE + "=?",
                new String[]{uri.getLastPathSegment()});
    }

    public static int updateLoginTimeUserInfo(Context context, UserInformation userInformation, Uri uri){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.UserInfoEntry.COLUMN_LOGIN_TIME, userInformation.getLoginTime());
        return context.getContentResolver().update(uri,
                cv,
                null,
                null);
    }

    public static int updateLogOutTimeUserInfo(Context context, UserInformation userInformation, Uri uri){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.UserInfoEntry.COLUMN_LOGOUT_TIME, userInformation.getLogoutTime());
        return context.getContentResolver().update(uri,
                cv,
                null,
                null);
    }

    public static int updateConfigUserInfo(Context context, UserInformation userInformation, Uri uri){
        ContentValues cv = new ContentValues();
        cv.put(ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_1, userInformation.getTimeBlock1());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_2, userInformation.getTimeBlock2());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_1, userInformation.getCostBlock1());
        cv.put(ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_2, userInformation.getCostBlock2());
        return context.getContentResolver().update(uri,
                cv,
                null,
                null);
    }

    public static int deleteUser(Context context, Uri uri){
        return context.getContentResolver().delete(uri,
                                                   null,
                                                   null);
    }

//    public static void insertDummyData(Context context){
//        List<Park> listPark = new ArrayList<>();
//        listPark.add(new Park(1, "Holi Park", "California", "04555666", "https://holipark.com.vn"));
//        listPark.add(new Park(2, "Fsoft Park", "Cau Giay", "04555643", "https://fsoftpark.com.vn"));
//        listPark.add(new Park(3, "FPT Park", "Hoa Lac", "0455432", "https://fptpark.com.vn"));
//        listPark.add(new Park(4, "Viettel Park", "Tu Liem", "04444666", "https://viettelpark.com.vn"));
//        listPark.add(new Park(5, "Vinaphone Park", "Bac Ninh", "04433636", "https://vinaphonepark.com.vn"));
//        List<ContentValues> parkValueList = new ArrayList<>();
//        for (Park p : listPark){
//            ContentValues cv = new ContentValues();
//            cv.put(ManagerContract.ParkEntry.COLUMN_ID, p.getId());
//            cv.put(ManagerContract.ParkEntry.COLUMN_PARK_NAME, p.getParkingName());
//            cv.put(ManagerContract.ParkEntry.COLUMN_PARK_ADDRESS, p.getParkingAddress());
//            cv.put(ManagerContract.ParkEntry.COLUMN_PARK_HOTLINE, p.getParkingHotline());
//            cv.put(ManagerContract.ParkEntry.COLUMN_PARK_WEBSITE, p.getParkingWebsite());
//            parkValueList.add(cv);
//        }
//        if (!parkValueList.isEmpty()){
//            ContentResolver contentResolver = context.getContentResolver();
//            contentResolver.delete(ManagerContract.ParkEntry.CONTENT_URI,
//                                    null,
//                                    null);
//            contentResolver.bulkInsert(ManagerContract.ParkEntry.CONTENT_URI, parkValueList.toArray(new ContentValues[parkValueList.size()]));
//        }
//
//        List<Vehicle> listVehicle = new ArrayList<>();
//
//        listVehicle.add(new Vehicle("car", 1, "32645", "17100001", "26/05/2017 9:45:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 1, "32646", "17100002", "26/05/2017 10:45:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 1, "32647", "17100003", "26/05/2017 11:00:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 1, "32648", "17100004", "26/05/2017 12:00:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 1, "32649", "17100005", "26/05/2017 13:00:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 1, "32650", "17100006", "26/05/2017 8:15:09", "26/05/2017 15:45:09", "Xe ra khỏi bãi", "Image in", "Image out", 30000));
//        listVehicle.add(new Vehicle("car", 1, "32651", "17100007", "26/05/2017 13:25:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 1, "32652", "17100008", "26/05/2017 13:45:09", "26/05/2017 15:45:09", "Xe ra khỏi bãi", "Image in", "Image out", 90000));
//        listVehicle.add(new Vehicle("car", 1, "32653", "17100009", "26/05/2017 14:00:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 1, "32654", "17100010", "26/05/2017 14:15:09", "26/05/2017 15:45:09", "Xe ra khỏi bãi", "Image in", "Image out", 120000));
//        listVehicle.add(new Vehicle("car", 1, "32655", "17100011", "26/05/2017 14:25:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 2, "32656", "17100012", "26/05/2017 14:35:09", null, "Xe trong bãi", "Image in", null, 0));
//        listVehicle.add(new Vehicle("car", 2, "32667", "17100013", "26/05/2017 14:45:09", null, "Xe trong bãi", "Image in", null, 0));
//        List<ContentValues> vehicleValueList = new ArrayList<>();
//        for (Vehicle v : listVehicle){
//            ContentValues cv = new ContentValues();
//            //cv.put(ManagerContract.VehicleEntry.COLUMN_ID, v.getId());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_TYPE, v.getTypeVehicle());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_PARKING_ID, v.getParkId());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_LICENSE_PLATE, v.getLicensePlate());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_BARCODE, v.getBarcode());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_TIME_IN, v.getTimeIn());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_TIME_OUT, v.getTimeOut());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_STATUS, v.getStatus());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_IN, v.getImageCarIn());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_IMAGE_CAR_OUT, v.getImageCarOut());
//            cv.put(ManagerContract.VehicleEntry.COLUMN_PRICE, v.getPrice());
//            vehicleValueList.add(cv);
//        }
//        if (!vehicleValueList.isEmpty()){
//            ContentResolver contentResolver = context.getContentResolver();
//            contentResolver.delete(ManagerContract.VehicleEntry.CONTENT_URI,
//                                    null,
//                                    null);
//            contentResolver.bulkInsert(ManagerContract.VehicleEntry.CONTENT_URI, vehicleValueList.toArray(new ContentValues[vehicleValueList.size()]));
//        }
//
//        List<User> userList = new ArrayList<>();
//        userList.add(new User(110, "Nguyen Van Hung", "hungnv@gmail.com", 0));
//        userList.add(new User(111, "Nguyen Dac Sang", "sangnd@gmail.com", 0));
//        userList.add(new User(112, "Nguyen Thuy Dung", "dungnt@gmail.com", 1));
//        userList.add(new User(113, "Le Dinh Nhat Khanh", "khanhldn@gmail.com", 1));
//        userList.add(new User(114, "Ho Hoang Hiep", "hiephh@gmail.com", 1));
//        userList.add(new User(115, "Vu Cuc Phuong", "phuongvc@gmail.com", 1));
//        userList.add(new User(116, "Doan Ngoc Thach", "thachdn@gmail.com", 2));
//        userList.add(new User(117, "Pham Van Phong", "phongpv@gmail.com", 2));
//        userList.add(new User(118, "Tran Anh Duc", "ducta@gmail.com", 2));
//        userList.add(new User(119, "Nguyen Trung Hung", "hungnt@gmail.com", 2));
//        userList.add(new User(120, "Vu Thi Ngoc Vien", "vienvtn@gmail.com", 0));
//        List<ContentValues> userValueList = new ArrayList<>();
//        for (User u : userList){
//            ContentValues cv = new ContentValues();
//            cv.put(ManagerContract.UserEntry.COLUMN_USER_ID, u.getUserId());
//            cv.put(ManagerContract.UserEntry.COLUMN_NAME, u.getName());
//            cv.put(ManagerContract.UserEntry.COLUMN_ACCOUNT, u.getAccount());
//            cv.put(ManagerContract.UserEntry.COLUMN_PERMISSION, u.getPermission());
//            userValueList.add(cv);
//        }
//
//        if (!userValueList.isEmpty()){
//            ContentResolver contentResolver = context.getContentResolver();
//            contentResolver.delete(ManagerContract.UserEntry.CONTENT_URI,
//                                    null,
//                                    null);
//            contentResolver.bulkInsert(ManagerContract.UserEntry.CONTENT_URI, userValueList.toArray(new ContentValues[userValueList.size()]));
//        }
//
//        List<UserInformation> userInfoList = new ArrayList<>();
//        userInfoList.add(new UserInformation(110, 1, 1, 20000, 30000, "7:30", "13:30", "21214124141", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(111, 1, 2, 20000, 30000, "7:30", "13:30", "21214124142", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(112, 1, 1, 20000, 30000, "7:30", "13:30", "21214124143", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(113, 1, 2, 20000, 30000, "7:30", "13:30", "21214124144", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(114, 1, 1, 20000, 30000, "7:30", "13:30", "21214124145", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(115, 1, 2, 20000, 30000, "7:30", "13:30", "21214124146", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(116, 1, 1, 0, 0, null, null, "21214124147", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(117, 1, 2, 0, 0, null, null, "21214124148", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(118, 2, 1, 0, 0, null, null, "21214124149", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(119, 2, 1, 0, 0, null, null, "21214124150", "Wed-25-May 10:20:00", null));
//        userInfoList.add(new UserInformation(120, 1, 1, 20000, 30000, "7:30", "13:30", "21214124151", "Wed-25-May 10:20:00", null));
//        List<ContentValues> userInfoValueList = new ArrayList<>();
//        for (UserInformation u : userInfoList){
//            ContentValues cv = new ContentValues();
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_USER_ID, u.getUserId());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_PARK_ID, u.getParkId());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_WORKING_SHIFT, u.getWorkingShift());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_1, u.getCostBlock1());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_COST_BLOCK_2, u.getCostBlock2());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_1, u.getTimeBlock1());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_TIME_BLOCK_2, u.getTimeBlock2());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_IP, u.getIp());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_LOGIN_TIME, u.getLoginTime());
//            cv.put(ManagerContract.UserInfoEntry.COLUMN_LOGOUT_TIME, u.getLogoutTime());
//            userInfoValueList.add(cv);
//        }
//        if (!userInfoValueList.isEmpty()){
//            ContentResolver contentResolver = context.getContentResolver();
//            contentResolver.delete(ManagerContract.UserInfoEntry.CONTENT_URI,
//                                   null,
//                                    null);
//            contentResolver.bulkInsert(ManagerContract.UserInfoEntry.CONTENT_URI, userInfoValueList.toArray(new ContentValues[userInfoValueList.size()]));
//        }
//
//
//    }


}
