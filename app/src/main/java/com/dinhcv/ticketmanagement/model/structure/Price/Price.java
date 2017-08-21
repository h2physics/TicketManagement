package com.dinhcv.ticketmanagement.model.structure.Price;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 6/9/2017.
 */

public class Price {
    @SerializedName("type")
    private int type;

    @SerializedName("cost_block_1")
    private double cost_block_1;

    @SerializedName("time_block_1")
    private String time_block_1;

    @SerializedName("cost_block_2")
    private double cost_block_2;

    @SerializedName("time_block_2")
    private String time_block_2;

    @SerializedName("total_price")
    private double total_price;

    public Price(int type, double cost_block_1, String time_block_1, double cost_block_2, String time_block_2, double total_price) {
        this.type = type;
        this.cost_block_1 = cost_block_1;
        this.time_block_1 = time_block_1;
        this.cost_block_2 = cost_block_2;
        this.time_block_2 = time_block_2;
        this.total_price = total_price;
    }

    public Price(int type, double cost_block_1, String time_block_1) {
        this.type = type;
        this.cost_block_1 = cost_block_1;
        this.time_block_1 = time_block_1;
    }

    public int getType() {
        return type;
    }

    public double getCost_block_1() {
        return cost_block_1;
    }

    public String getTime_block_1() {
        return time_block_1;
    }

    public double getCost_block_2() {
        return cost_block_2;
    }

    public String getTime_block_2() {
        return time_block_2;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCost_block_1(double cost_block_1) {
        this.cost_block_1 = cost_block_1;
    }

    public void setTime_block_1(String time_block_1) {
        this.time_block_1 = time_block_1;
    }

    public void setCost_block_2(double cost_block_2) {
        this.cost_block_2 = cost_block_2;
    }

    public void setTime_block_2(String time_block_2) {
        this.time_block_2 = time_block_2;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "Price{" +
                "type=" + type +
                ", cost_block_1=" + cost_block_1 +
                ", time_block_1='" + time_block_1 + '\'' +
                ", cost_block_2=" + cost_block_2 +
                ", time_block_2='" + time_block_2 + '\'' +
                ", total_price=" + total_price +
                '}';
    }
}
