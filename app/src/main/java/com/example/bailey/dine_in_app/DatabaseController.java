package com.example.bailey.dine_in_app;


import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                   selected_reservation ,
                    selected_cuisineType,
                    selected_city,
                    searched_Restaurant,
                    reserved_Restaurant;

    public void setSelectedCuisineType(String cT)
    {
        selected_cuisineType = cT;
    }

    public void setSelectedCity(String c)
    {
        selected_city = c;
    }

    public void setSearchedRestaurant(String r)
    {
        searched_Restaurant = r;
    }

    public void setSelectedRestaurant(String s){selected_restaurant = s;}
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
    //@SuppressLint("NewApi")
    public void connect(){
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        try{
            //Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            Class.forName("com.mysql.jdbc.Driver");
            String connectionURL = "jdbc:jtds:sqlserver://" + SERVER + ";" + "databaseName=" + DB + ";user=" + USERNAME + ";password=" + PASSWORD + ";";
//            String connectionURL = "jdbc:mysql://" + SERVER + "/" + DB;
            connection = DriverManager.getConnection(connectionURL);
//            connection = DriverManager.getConnection(connectionURL, USERNAME, PASSWORD);
            if(connection == null) {
                Log.d("CONNECTION", "NOT CONNECTED");
                //return false;
            }
                else {
                Log.d("CONNECTION", "CONNECTED");
                //return true;
            }
        }catch(Exception e){
            Log.e("ERROR: ", "Connect Error: " + e.getMessage());
            e.printStackTrace();
        }
        //return false;
        Log.d("CONNECTED", "Success =" + attemptLogin("b@gmail.com", "b"));
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

    public void logout(){
        logged_in_customer = null;
        logged_in_restaurant = null;
    }

    /**
     * Queries
     */
    public boolean attemptLogin(String email, String password){
        try {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * " +
                    "FROM db_a2efef_dining.customer " +
                    "WHERE email = ? AND password = ?;");
            prepStatement.setString(1, email);
            prepStatement.setString(2, password);
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()){
                logged_in_customer = rs.getString("firstName") + rs.getString("lastName");
                Log.d("CUSTOMER", logged_in_customer);
            }
            if(logged_in_restaurant != null || logged_in_customer != null)
                return true;
        } catch(SQLException e){
            Log.e("ERROR", "Login Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Query to retrieve cuisine types for the the search restaurant cuisine type spinner
     */
    public ArrayList<String> getCuisineTypeList()
    {
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT DISTINCT cuisine_type " +
                    "FROM db_a2efef_dining.restaurant;");
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("cuisine_type"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Cuisine Type List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getCityList()
    {
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT DISTINCT location " +
                    "FROM db_a2efef_dining.restaurant;");
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("location"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "City List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getRestaurantList()
    {
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            if(searched_Restaurant.equals("Search"))
            {
                searched_Restaurant = "";
            }
            searched_Restaurant = "%" + searched_Restaurant + "%";
            PreparedStatement prepStatement = connection.prepareStatement("SELECT name " +
                    "FROM db_a2efef_dining.restaurant "+
                    "WHERE name LIKE ? AND cuisine_type = ? AND location = ?;");
            prepStatement.setString(1, searched_Restaurant);
            prepStatement.setString(2, selected_cuisineType);
            prepStatement.setString(3, selected_city);
            ResultSet rs = prepStatement.executeQuery();

            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("name"));
            }

        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Restaurant List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getMealTypeList()
    {
        //TODO -- Remove this line
        reserved_Restaurant = "4";
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT DISTINCT meal_type " +
                    "FROM db_a2efef_dining.food_item as F, db_a2efef_dining.restaurant_has_food_item as R "+
                    "WHERE r_id = ? AND F.name = R.name ");
            prepStatement.setString(1, reserved_Restaurant);

            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("meal_type"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Meal Type List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getItemTypeList()
    {
        //TODO -- Remove this line
        reserved_Restaurant = "4";
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT DISTINCT item_type " +
                    "FROM db_a2efef_dining.food_item as F, db_a2efef_dining.restaurant_has_food_item as R "+
                    "WHERE r_id = ? AND F.name = R.name ");
            prepStatement.setString(1, reserved_Restaurant);

            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("item_type"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Item Type List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getFoodItemsList(String mealType, String itemType)
    {
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT F.name " +
                    "FROM db_a2efef_dining.food_item as F, db_a2efef_dining.restaurant_has_food_item as R "+
                    "WHERE r_id = ? AND F.name = R.name AND F.item_type = ? AND F.mealType = ?;");
            prepStatement.setString(1, searched_Restaurant);
            prepStatement.setString(2, mealType);
            prepStatement.setString(3, itemType);
            ResultSet rs = prepStatement.executeQuery();

            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("name"));
            }

        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Food Items List" + e.getMessage());
        }
        return temp;

    }
}
