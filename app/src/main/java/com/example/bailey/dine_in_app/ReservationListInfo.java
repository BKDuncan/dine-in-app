package com.example.bailey.dine_in_app;

/**
 * Created by Anil Sood on 12/8/2017.
 */

public class ReservationListInfo {
    private int resId;
    private String name;
    private String date;
    private String time;

    public ReservationListInfo(int i, String n, String d, String t) {
        resId = i;
        name = n;
        date = d;
        time = t;
    }

    public int getResId() {return resId;}
    public String getName() {return name;}
    public String getDate() {return date;}
    public String getTime() {return time;}

    public void setResId(int i) {resId = i;}
    public void setName(String n) {name = n;}
    public void setDate(String d) {date = d;}
    public void setTime(String t) {time = t;}
}
