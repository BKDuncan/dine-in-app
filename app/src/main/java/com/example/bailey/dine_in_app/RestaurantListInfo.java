package com.example.bailey.dine_in_app;

/**
 * Created by Anil Sood on 12/8/2017.
 */

public class RestaurantListInfo {

    private int res_Id;
    private String name;
    private String hours;
    private String phone;

    public RestaurantListInfo(int id, String name, String hours, String phone) {
        this.res_Id = id;
        this.name = name;
        this.hours = hours;
        this.phone = phone;
    }

    public void setRes_Id(int id) {

        this.res_Id = id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRes_Id() {

        return res_Id;
    }

    public String getName() {

        return name;
    }

    public String getHours() {
        return hours;
    }

    public String getPhone() {
        return phone;
    }
}
