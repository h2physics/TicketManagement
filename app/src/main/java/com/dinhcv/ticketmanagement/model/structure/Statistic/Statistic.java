package com.dinhcv.ticketmanagement.model.structure.Statistic;

/**
 * Created by YukiNoHara on 6/10/2017.
 */

public class Statistic {
    private String totalCarInPark;
    private String carInDay;
    private String carOutDay;
    private String priceDay;
    private String time1;
    private String time2;
    private String totalCarIn;
    private String totalCarOut;
    private String totalPrice;

    public Statistic(String totalCarInPark, String carInDay, String carOutDay, String priceDay, String time1, String time2, String totalCarIn, String totalCarOut, String totalPrice) {
        this.totalCarInPark = totalCarInPark;
        this.carInDay = carInDay;
        this.carOutDay = carOutDay;
        this.priceDay = priceDay;
        this.time1 = time1;
        this.time2 = time2;
        this.totalCarIn = totalCarIn;
        this.totalCarOut = totalCarOut;
        this.totalPrice = totalPrice;
    }

    public String getTotalCarInPark() {
        return totalCarInPark;
    }

    public String getCarInDay() {
        return carInDay;
    }

    public String getCarOutDay() {
        return carOutDay;
    }

    public String getPriceDay() {
        return priceDay;
    }

    public String getTime1() {
        return time1;
    }

    public String getTime2() {
        return time2;
    }

    public String getTotalCarIn() {
        return totalCarIn;
    }

    public String getTotalCarOut() {
        return totalCarOut;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "totalCarInPark='" + totalCarInPark + '\'' +
                ", carInDay='" + carInDay + '\'' +
                ", carOutDay='" + carOutDay + '\'' +
                ", priceDay='" + priceDay + '\'' +
                ", time1='" + time1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", totalCarIn='" + totalCarIn + '\'' +
                ", totalCarOut='" + totalCarOut + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                '}';
    }
}
