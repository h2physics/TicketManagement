package com.dinhcv.ticketmanagement.model.structure.Vehicle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YukiNoHara on 6/5/2017.
 */

public class VehicleResponse {
    @SerializedName("data")
    private List<Vehicle> list;

    public List<Vehicle> getList() {
        return list;
    }
}
