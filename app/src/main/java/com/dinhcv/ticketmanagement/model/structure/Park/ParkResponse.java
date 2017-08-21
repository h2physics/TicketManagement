package com.dinhcv.ticketmanagement.model.structure.Park;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 6/1/2017.
 */

public class ParkResponse {
    @SerializedName("data")
    private Park park;

    public Park getPark() {
        return park;
    }
}
