package com.dinhcv.ticketmanagement.model.structure.Delete;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 6/10/2017.
 */

public class DeleteResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;
}
