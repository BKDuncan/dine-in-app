package com.example.bailey.dine_in_app;

/**
 * Created by Anil Sood on 12/7/2017.
 */

public class TableListInfo {
    private String table_number;
    private String seats;
    private String is_available;

    /**
     * Constructor
     */
    public TableListInfo(String t, String s, String i)
    {
        table_number = t;
        seats = s;
        is_available = i;
    }

    /**
     * Getters
     */
    public String getTableNumber() { return table_number;}
    public String getSeats() { return seats;}
    public String getAvailable() { return is_available;}

    /**
     * Setters
     */
    public void setTableNumber(String t) {table_number = t;}
    public void setSeats(String s) {seats = s;}
    public void setIsAvailable(String i) {is_available = i;}
}
