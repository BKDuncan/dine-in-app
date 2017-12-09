package com.example.bailey.dine_in_app;

/**
 * Created by Anil Sood on 12/8/2017.
 */

public class OrderListInfo {
    private int orderNum;
    private String date;
    private String time;
    private String total;

    public OrderListInfo(int orderNum, String date, String time, String total) {
        this.orderNum = orderNum;
        this.date = date;
        this.time = time;
        this.total = total;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTotal() {
        return total;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
