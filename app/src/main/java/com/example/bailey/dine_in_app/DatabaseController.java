package com.example.bailey.dine_in_app;


import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Singleton Class for database connection
 */
public class DatabaseController {
    /**
     * Only instance of DatabaseController
     */
    private static DatabaseController instance;

    /**
     * Stored values used by other activities (instead of passing them in constructors)
     */
    private String logged_in_customer ,
                   logged_in_restaurant ,
                   selected_restaurant ,
                   selected_reservation ;

    /**
     * Database Connection Constants
     */
    private  Connection connection = null;
    //Initial Catalog=DB_A2F662_dining;User Id=DB_A2F662_dining_admin;Password=YOUR_DB_PASSWORD;
    private final String USERNAME = "DB_A2EFEF_dine_admin", //"a2efef_dining" ,
                         PASSWORD = "CPSC471Project" ,
                         DB = "DB_A2EFEF_dine", //"db_a2efef_dining" ,
                         SERVER = "sql7003.site4now.net"; //"mysql7001.site4now.net" ;

    public static DatabaseController getInstance(){
        if(instance == null)
            instance = new DatabaseController();
        return instance;
    }

    /**
     * Attempt to connect to the database
     */
    public void connect(){
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionURL = "jdbc:jtds:sqlserver://" + SERVER + ";" + "databaseName=" + DB + ";user=" + USERNAME + ";password=" + PASSWORD + ";";
            connection = DriverManager.getConnection(connectionURL);

            // Log Result
            if(connection == null)
                Log.d("CONNECTION", "NOT CONNECTED");
            else
                Log.d("CONNECTION", "CONNECTED");
        }catch(Exception e){
            Log.e("ERROR: ", "Connect Error: " + e.getMessage());
        }
    }

    /**
     * Getters & Setters
     */
    public String get_logged_in_customer(){
        return logged_in_customer;
    }

    public String get_logged_in_restaurant(){
        return logged_in_restaurant;
    }

    public boolean is_connected(){ return connection != null; }

    public void logout(){
        logged_in_customer = null;
        logged_in_restaurant = null;
    }

    /**
     * Queries
     */
    public boolean attemptCustomerLogin(String email, String password){
        try {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * " +
                    "FROM db_a2efef_dining.customer " +
                    "WHERE email = ? AND password = ?;");
            prepStatement.setString(1, email);
            prepStatement.setString(2, password);
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()){
                // Customer info delimited by semicolons
                logged_in_customer = rs.getString("email") + ";" + rs.getString("phone") + ";"
                                   + rs.getString("location") + ";" + rs.getString("firstName") + ";"
                                   + rs.getString("lastName") + ";" + rs.getString("password");
                Log.d("CUSTOMER", logged_in_customer);
            }
            if(logged_in_customer != null)
                return true;
        } catch(SQLException e){
            Log.e("ERROR", "Login Error: " + e.getMessage());
        }
        return false;
    }

    public boolean attemptRestaurantLogin(int rid, String password){
        try {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * " +
                    "FROM db_a2efef_dining.restaurant " +
                    "WHERE r_id = ? AND password = ?;");
            prepStatement.setInt(1, rid);
            prepStatement.setString(2, password);
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()){
                // All restaurant info delimited by semicolons
                logged_in_restaurant = rs.getInt("r_id") +            ";" + rs.getString("name") +         ";"
                                     + rs.getString("location") +     ";" + rs.getString("hours") +        ";"
                                     + rs.getString("cuisine_type") + ";" + rs.getString("phone_number") + ";"
                                     + rs.getString("password");
                Log.d("RESTAURANT", logged_in_restaurant);
            }
            if(logged_in_restaurant != null)
                return true;
        } catch(SQLException e){
            Log.e("ERROR", "Login Error: " + e.getMessage());
        }
        return false;
    }

    public boolean signup(String email, String phone, String location, String fname, String lname, String password){
        try {
            PreparedStatement prepStatement = connection.prepareStatement(
                    "INSERT INTO db_a2efef_dining.customer " +
                       "( email, phone, location, firstName, lastName, password ) " +
                       "VALUES ( ?, ?, ?, ?, ?, ? ) ;");
            prepStatement.setString(1, email);
            prepStatement.setString(2, phone);
            prepStatement.setString(3, location);
            prepStatement.setString(4, fname);
            prepStatement.setString(5, lname);
            prepStatement.setString(6, password);
            prepStatement.executeUpdate();
            return true;
        } catch(SQLException e){
            Log.e("ERROR", "Signup Error: " + e.getMessage());
        }
        // Only get here if SQL Exception thrown, ie. the insert operation failed
        return false;
    }
}
