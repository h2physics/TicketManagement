package com.dinhcv.ticketmanagement.model.structure.Vehicle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 6/6/2017.
 */

public class VehicleUploadResponse {
    @SerializedName("data")
    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return vehicle;
    }
}
