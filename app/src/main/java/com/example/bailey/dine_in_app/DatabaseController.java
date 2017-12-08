package com.example.bailey.dine_in_app;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
                   selected_reservation,
                   selected_transaction,
                   selected_order,
                   selected_table,
                   selected_reservation ,
                    selected_cuisineType,
                    selected_city,
                    searched_Restaurant,
                    reserved_Restaurant,
                    order_added,
                    order_number_detail,
                    reservation_id_detail_order;


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

    public void setOrderAdded(String o ) {order_added = o;}

    public void setOrderNumberDetail(String o ) {order_number_detail = o;}

    public void setReservedRestaurant (String r) {reserved_Restaurant = r;}

    public void setReservationIdDetailOrder (String i) {reservation_id_detail_order = i;}

    /**
     * Database Connection Constants
     */
    private  Connection connection = null;
    private final String USERNAME = "DB_A2EFEF_dine_admin",
                         PASSWORD = "CPSC471Project" ,
                         DB = "DB_A2EFEF_dine",
                         SERVER = "sql7003.site4now.net";

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

    public boolean add_food_item(String name, String meal_type, boolean is_available, String item_type, double price, String description){
        try {
            // Insert Food Item
            PreparedStatement prepStatement = connection.prepareStatement(
                    "INSERT INTO db_a2efef_dining.food_item " +
                            "( name, meal_type, is_available, item_type, price, description ) " +
                            "VALUES ( ?, ?, ?, ?, ?, ? ) ;");
            prepStatement.setString(1, name);
            prepStatement.setString(2, meal_type);
            prepStatement.setBoolean(3, is_available);
            prepStatement.setString(4, item_type);
            prepStatement.setDouble(5, price);
            prepStatement.setString(6, description);
            prepStatement.executeUpdate();

            // Insert ( Food_Item <---> Restuarant ) Relation
            prepStatement = connection.prepareStatement(
                    "INSERT INTO db_a2efef_dining.restaurant_has_food_item " +
                            "( r_id, name ) " +
                            "VALUES ( ?, ? ) ;");
            prepStatement.setInt(1, Integer.parseInt(logged_in_restaurant.split(";")[0]));
            prepStatement.setString(2, name);
            prepStatement.executeUpdate();
            return true;
        } catch(SQLException e){
            Log.e("ERROR", "Insert Food Error: " + e.getMessage());
        }
        // Only get here if SQL Exception thrown, ie. the insert operation failed
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
                    "WHERE r_id = ? AND F.name = R.name AND F.item_type = ? AND F.meal_type = ?;");
            prepStatement.setString(1, reserved_Restaurant);
            prepStatement.setString(2, itemType);
            prepStatement.setString(3, mealType);
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

    void addNewOrder(ArrayList<String> foodItems)
    {
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO [DB_A2EFEF_dine].[db_a2efef_dining].[order] "+
            "(total_price, date, time, special_request, served) "+
                    "OUTPUT Inserted.order_number "+
                    "values (0.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '', 0)");
            ResultSet rs = prepStatement.executeQuery();
            int order_number = 0;
            // Iterate through results
            while(rs.next())
            {
                order_number = rs.getInt("order_number");
            }
            double food_items_total = 0.0;
            for(int i = 0; i < foodItems.size(); i++)
            {
                PreparedStatement prepStatement2 = connection.prepareStatement("INSERT INTO [DB_A2EFEF_dine].[db_a2efef_dining].[order_has_food_item] "+
                        "values (?,?)");
                prepStatement2.setInt(1, order_number);
                prepStatement2.setString(2, foodItems.get(i));
                prepStatement2.executeUpdate();

                PreparedStatement prepStatement3 = connection.prepareStatement("SELECT price FROM [DB_A2EFEF_dine].[db_a2efef_dining].[food_item] "+
                        "where name = ?");
                prepStatement3.setString(1, foodItems.get(i));
                ResultSet rs2 = prepStatement3.executeQuery();
                while(rs2.next())
                {
                    food_items_total += rs2.getDouble("price");
                }
            }
            PreparedStatement prepStatement4 = connection.prepareStatement("UPDATE [DB_A2EFEF_dine].[db_a2efef_dining].[order] "+
                    "SET total_price = ? " +
                    "WHERE order_number = ?");
            prepStatement4.setDouble(1, food_items_total);
            prepStatement4.setInt(2, order_number);
            prepStatement4.executeUpdate();
            this.setOrderAdded(Double.toString(order_number));
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Add New Order" + e.getMessage() + e.getStackTrace() );
            e.printStackTrace();
        }

    }

    public ArrayList<String> getOrderList(String from, String to)
    {
        // TODO -- remove this static assignment
        logged_in_restaurant = "4";
        ArrayList<String> temp = new ArrayList<>();
        // TODO -- if to and from is empty then it doesn't work
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT DISTINCT O.order_number "+
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[order] as O1, "+
                    "[DB_A2EFEF_dine].[db_a2efef_dining].[restaurant_has_food_item] as R ,"+
                    "[DB_A2EFEF_dine].[db_a2efef_dining].[order_has_food_item] as O "+
                    "WHERE O.name = R.name AND R.r_id  = ? AND O1.order_number = O.order_number AND O1.date >= ? AND O1.date <= ?");
            prepStatement.setInt(1, Integer.parseInt(logged_in_restaurant));
            prepStatement.setString(2, from);
            prepStatement.setString(3, to);
            ResultSet rs = prepStatement.executeQuery();

            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("order_number"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Get Order List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getOrderDetail()
    {

        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * "+
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[order] "+
                    "WHERE order_number = ?;");

            prepStatement.setInt(1, Integer.parseInt(order_number_detail));
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("order_number"));
                temp.add(rs.getString("date"));
                temp.add(rs.getString("time"));
                temp.add(rs.getString("special_request"));
                temp.add(rs.getString("served"));
                temp.add(rs.getString("total_price"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Get Order List" + e.getMessage());
        }
        return temp;
    }

    public void serveOrder()
    {
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("UPDATE [DB_A2EFEF_dine].[db_a2efef_dining].[order] "+
                    "SET served = 1 WHERE order_number = ?;");

            prepStatement.setInt(1, Integer.parseInt(order_number_detail));
            prepStatement.executeUpdate();
            // Iterate through results
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Serve Order" + e.getMessage());
        }
    }

    public ArrayList<String> getReservationList()
    {
        //TODO -- remove this
        logged_in_customer = "a@gmail.com;";

        String customerEmail = logged_in_customer.split(";")[0];
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * "+
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[reservation] "+
                    "WHERE email = ?;");
            prepStatement.setString(1, customerEmail);
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                temp.add(rs.getString("reservation_id") + "   " + rs.getString("email"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Get Reservation List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getReservationDetail()
    {
        ArrayList<String> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * "+
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[reservation] "+
                    "WHERE reservation_id = ?;");
            prepStatement.setInt(1, Integer.parseInt(reservation_id_detail_order));
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                int r_id = rs.getInt("r_id");
                Log.d("restaurant id", String.valueOf(r_id));
                PreparedStatement prepStatement2 = connection.prepareStatement("SELECT name "+
                "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[restaurant] WHERE r_id = ?;");
                prepStatement2.setInt(1, r_id);
                ResultSet rs2 = prepStatement2.executeQuery();
                while(rs2.next()) {
                    temp.add(rs2.getString("name"));
                }
                temp.add(rs.getString("table_number"));
                temp.add(rs.getString("date"));
                temp.add(rs.getString("time"));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Get Reservation Detail" + e.getMessage());
        }
        return temp;
    }

    public String findRestaurantId()
    {
        String temp = "";
        try {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT r_id " +
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[reservation] " +
                    "WHERE reservation_id = ?;");
            prepStatement.setInt(1, Integer.parseInt(reservation_id_detail_order));
            ResultSet rs = prepStatement.executeQuery();
            while(rs.next())
            {
                temp = rs.getString("r_id");
            }
        }
        catch (Exception e)
        {
            Log.e ("SQL ERROR" , "Find Restaurant Id");
        }
        return temp;
    }

    public ArrayList<TableListInfo> getTableList()
    {
        // TODO -- remove this
        logged_in_restaurant = "4;";
        ArrayList<TableListInfo> temp = new ArrayList<>();
        try {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * " +
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[table] " +
                    "WHERE r_id = ?;");
            prepStatement.setInt(1, Integer.parseInt(logged_in_restaurant.split(";")[0]));
            ResultSet rs = prepStatement.executeQuery();

            while(rs.next())
            {
                TableListInfo tempTable = new TableListInfo(rs.getString("table_number"), rs.getString("seats"), rs.getString(("is_available")));
                temp.add(tempTable);
            }
        }
        catch (Exception e)
        {
            Log.e ("SQL ERROR" , "Get Table List");
        }
        Log.d("blah", String.valueOf(temp.size()));
        return temp;
    }

    public boolean add_table(int seats, boolean is_available){
        try {
            // Insert Table (with unique ID for that restaurants tables)
            PreparedStatement prepStatement = connection.prepareStatement(
                    "INSERT INTO [db_a2efef_dining].[table]" +
                            " ( r_id, table_number, seats, is_available )" +
                            " VALUES( ?," +
                            " ( SELECT MAX( T.table_number ) + 1" + // Get New Unique Table Number
                                " FROM [db_a2efef_dining].[table] T" +
                                " WHERE T.r_id = ?), " +
                            " ?, ? ) ;");

            prepStatement.setInt(1, Integer.parseInt(logged_in_restaurant.split(";")[0]));
            prepStatement.setInt(2, Integer.parseInt(logged_in_restaurant.split(";")[0]));
            prepStatement.setInt(3, seats);
            prepStatement.setBoolean(4, is_available);
            prepStatement.executeUpdate();

            return true;
        } catch(SQLException e){
            Log.e("ERROR", "Insert Table Error: " + e.getMessage());
        }
        // Only get here if SQL Exception thrown, ie. the insert operation failed
        return false;
    }

    public ArrayList<String> getTransactionDetail(){
        ArrayList<String> details = new ArrayList<>();
        // TODO: Replace temp transaction string
        String transaction = selected_transaction;
        if(transaction == null)
            transaction = "1;Mastercard;10.00;4;6";
        // For Each loop that populates the arraylist with transaction info
        for(String s : transaction.split(";"))
            details.add(s);

        try {
            PreparedStatement prepStatement = connection.prepareStatement(
                    "SELECT * FROM db_a2efef_dining.transaction T  " +
                        "JOIN db_a2efef_dining.order O ON T.order_number = O.order_number " +
                       "WHERE T.t_number = ?;");
            prepStatement.setInt(1, Integer.parseInt(details.get(0)));
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()) {
                details.add("" + rs.getDouble("total_price"));
                details.add("" + rs.getDate("date"));
                details.add("" + rs.getTime("time"));
            }
        } catch(SQLException e){
            Log.e("ERROR", "Login Error: " + e.getMessage());
        }
        // TODO: remove temp orders here
        details.add("50.92"); // Total
        details.add("12/05/17"); // Date
        details.add("08:05:17"); // Time
        return details;
    }

    public ArrayList<String> getTableReservations(){
        ArrayList<String> reservations = new ArrayList<>();
        // TODO: Replace temp string
        String table = selected_table;
        if(table == null)
            table = "6;1;4;1";

        try {
            // Get future table reservations
            PreparedStatement prepStatement = connection.prepareStatement(
                    "SELECT * " +
                       "FROM [db_a2efef_dining].[reservation] R " +
                       "WHERE R.table_number = ? AND R.r_id = ? " +
                            "AND ( R.date >= GETDATE() )" +
                       "ORDER BY R.date, R.time ASC;");
            prepStatement.setInt(1, Integer.parseInt(table.split(";")[1]));
            prepStatement.setInt(2, Integer.parseInt(table.split(";")[0]));
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()) {
                reservations.add( rs.getString("email") + "       " + rs.getDate("date") + "      " + rs.getTime("time") );
            }
        } catch(SQLException e){
            Log.e("ERROR", "Table Reservations Error: " + e.getMessage());
        }
        return reservations;
    }
}
