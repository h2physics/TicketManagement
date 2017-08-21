package com.dinhcv.ticketmanagement.network;

/**
 * Created by YukiNoHara on 5/31/2017.
 */

public interface NetworkExecuteCompleted {
    public void onSuccess();
    public void onError();
    public void onFailed();
}
