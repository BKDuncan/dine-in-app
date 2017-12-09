package com.example.bailey.dine_in_app;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.Array;
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
                    reservation_id_detail_order,
                    food_item_name;

    private ArrayList<String> tempFoodItemList = null;

    public boolean checkFoodItemEmpty()
    {
        if(tempFoodItemList == null)
            return true;
        else return false;
    }

    public void addFoodItem(String name) {
        if(tempFoodItemList == null)
        {
            tempFoodItemList = new ArrayList<>();
        }
        tempFoodItemList.add(name);
    }

    public void setSelectedTransaction(String s) {selected_transaction = s;}

    public void setFoodItemName(String n) {food_item_name = n;}

    public void setSelectedTable(String table) {selected_table = table;}

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

    public ArrayList<RestaurantListInfo> getRestaurantList()
    {
        ArrayList<RestaurantListInfo> temp = new ArrayList<>();
        try
        {
            if(searched_Restaurant.equals("Search"))
            {
                searched_Restaurant = "";
            }
            searched_Restaurant = "%" + searched_Restaurant + "%";
            PreparedStatement prepStatement = connection.prepareStatement("SELECT * " +
                    "FROM db_a2efef_dining.restaurant "+
                    "WHERE name LIKE ? AND cuisine_type = ? AND location = ?;");
            prepStatement.setString(1, searched_Restaurant);
            prepStatement.setString(2, selected_cuisineType);
            prepStatement.setString(3, selected_city);
            ResultSet rs = prepStatement.executeQuery();

            // Iterate through results
            while(rs.next())
            {
                temp.add(new RestaurantListInfo(rs.getInt("r_id"),rs.getString("name"), rs.getString("hours"), rs.getString("phone_number")));
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
            Log.e("SQLERROR", "Meal Type List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getItemTypeList()
    {
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
            Log.e("SQLERROR", "Item Type List" + e.getMessage());
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

    void addNewOrder()
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
            for(int i = 0; i < tempFoodItemList.size(); i++)
            {
                PreparedStatement prepStatement2 = connection.prepareStatement("INSERT INTO [DB_A2EFEF_dine].[db_a2efef_dining].[order_has_food_item] "+
                        "values (?,?)");
                prepStatement2.setInt(1, order_number);
                prepStatement2.setString(2, tempFoodItemList.get(i));
                prepStatement2.executeUpdate();

                PreparedStatement prepStatement3 = connection.prepareStatement("SELECT price FROM [DB_A2EFEF_dine].[db_a2efef_dining].[food_item] "+
                        "where name = ?");
                prepStatement3.setString(1, tempFoodItemList.get(i));
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
        tempFoodItemList = null;
    }

    public ArrayList<OrderListInfo> getOrderList(String from, String to)
    {
        String restaurant = logged_in_restaurant.split(";")[0];
        ArrayList<OrderListInfo> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT DISTINCT O1.order_number, O1.date, O1.time, O1.total_price "+
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[order] as O1, "+
                    "[DB_A2EFEF_dine].[db_a2efef_dining].[restaurant_has_food_item] as R ,"+
                    "[DB_A2EFEF_dine].[db_a2efef_dining].[order_has_food_item] as O "+
                    "WHERE O.name = R.name AND R.r_id  = ? AND O1.order_number = O.order_number AND O1.date >= ? AND O1.date <= ?");
            prepStatement.setInt(1, Integer.parseInt(restaurant));
            prepStatement.setString(2, from);
            prepStatement.setString(3, to);
            ResultSet rs = prepStatement.executeQuery();

            // Iterate through results
            while(rs.next())
            {
                temp.add(new OrderListInfo(rs.getInt("order_number"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("total_price")));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Get Order List" + e.getMessage());
        }
        return temp;
    }

    public ArrayList<TransactionListInfo> getTransactionList(String from, String to)
    {
        String restaurant = logged_in_restaurant.split(";")[0];
        ArrayList<TransactionListInfo> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT DISTINCT T.t_number, T.payment_type,T.tip, O.total_price "+
                    "FROM [DB_A2EFEF_dine].[db_a2efef_dining].[order] as O, "+
                    "[DB_A2EFEF_dine].[db_a2efef_dining].[transaction] as T "+
                    "WHERE O.order_number = T.order_number AND T.r_id  = ? AND O.date >= ? AND O.date <= ?");
            prepStatement.setInt(1, Integer.parseInt(restaurant));
            prepStatement.setString(2, from);
            prepStatement.setString(3, to);
            ResultSet rs = prepStatement.executeQuery();

            // Iterate through results
            while(rs.next())
            {
                temp.add(new TransactionListInfo(rs.getInt("t_number"),
                        rs.getString("payment_type"),
                        rs.getString("tip"),
                        rs.getString("total_price")));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Get Transaction List" + e.getMessage());
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

    public ArrayList<ReservationListInfo> getReservationList()
    {
        String customerEmail = logged_in_customer.split(";")[0];
        ArrayList<ReservationListInfo> temp = new ArrayList<>();
        try
        {
            PreparedStatement prepStatement = connection.prepareStatement("SELECT R1.reservation_id, R2.name, R1.date, R1.time \n" +
                    "  FROM [DB_A2EFEF_dine].[db_a2efef_dining].[reservation] as R1, \n" +
                    "  [DB_A2EFEF_dine].[db_a2efef_dining].[restaurant] as R2\n" +
                    "  WHERE R1.email = ? AND R1.r_id = R2.r_id;");
            prepStatement.setString(1, customerEmail);
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next())
            {
                temp.add(new ReservationListInfo(rs.getInt("reservation_id"), rs.getString("name"), rs.getString("date"),rs.getString("time") ));
            }
        }
        catch(SQLException e)
        {
            Log.e("ERROR", "Get Reservation List" + e.getMessage());
            e.printStackTrace();
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
            if(!this.is_connected())
                this.connect();
            PreparedStatement prepStatement = connection.prepareStatement("SELECT r_id" +
                    "  FROM [DB_A2EFEF_dine].[db_a2efef_dining].[reservation]" +
                    "  WHERE reservation_id = ?;");
            prepStatement.setInt(1, Integer.parseInt(reservation_id_detail_order));
            Log.d("Sequence 000", reservation_id_detail_order);
            ResultSet rs = prepStatement.executeQuery();
            Log.d("Sequence 001", reservation_id_detail_order);
            while(rs.next())
            {

                temp = String.valueOf(rs.getInt("r_id"));
            }
        }
        catch (Exception e)
        {
            Log.e ("SQL ERROR" , "Find Restaurant Id" + temp);
            e.printStackTrace();
        }
        Log.d("Sequence", " 00 Reserved Restaurant: " + temp);
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
                String available = "";
                if(rs.getString(("is_available")).equals("1")){
                    available = "yes";
                }
                else{
                    available = "no";
                }
                TableListInfo tempTable = new TableListInfo(rs.getString("table_number"), rs.getString("seats"), available);
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
        String transaction = selected_transaction;

        try {
            PreparedStatement prepStatement = connection.prepareStatement(
                    "SELECT * FROM [DB_A2EFEF_dine].[db_a2efef_dining].[transaction] as T  " +
                        "JOIN [DB_A2EFEF_dine].[db_a2efef_dining].[order] as O ON T.order_number = O.order_number " +
                       "WHERE T.t_number = ?;");
            prepStatement.setInt(1, Integer.parseInt(transaction));
            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()) {
                details.add("" + rs.getString("total_price"));
                details.add("" + rs.getString("tip"));
                details.add("" + rs.getString("date"));
                details.add("" + rs.getString("time"));
                details.add("" + rs.getString("payment_type"));
            }
        } catch(SQLException e){
            Log.e("ERROR", "Login Error: " + e.getMessage());
        }
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
            prepStatement.setInt(1, Integer.parseInt(table.split(";")[0]));
            prepStatement.setInt(2, Integer.parseInt(logged_in_restaurant.split(";")[0]));
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

    public void toggleAvailability()
    {
        String table = selected_table;
        String restaurant = logged_in_restaurant.split(";")[0];
        String available = "";
        if(table == null || restaurant == null)
            return;

        try {
            PreparedStatement prepStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM [db_a2efef_dining].[table] " +
                            "WHERE r_id = ? AND table_number = ?;");
            prepStatement.setInt(1, Integer.parseInt(restaurant));
            prepStatement.setInt(2, Integer.parseInt(table));

            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()) {
                available = rs.getString("is_available");
            }

            if(available.equals("1")){
                available = "0";
            }
            else{
                available = "1";
            }
            Log.d("Blah", available);
            PreparedStatement prepStatement2 = connection.prepareStatement(
                    "UPDATE [db_a2efef_dining].[table] " +
                            "SET is_available = ? "+
                            "WHERE r_id = ? AND table_number = ?;");
            prepStatement2.setInt(1, Integer.parseInt(available));
            prepStatement2.setInt(2, Integer.parseInt(restaurant));
            prepStatement2.setInt(3, Integer.parseInt(table));
            prepStatement2.executeUpdate();

        } catch(SQLException e){
            Log.e("ERROR", "Toggle Availability Error: " + e.getMessage());
        }
    }


    public TableListInfo getTableDetail()
    {
        String table = selected_table;
        String restaurant = logged_in_restaurant.split(";")[0];

        TableListInfo temp = null;
        if(table == null || restaurant == null)
            return null;

        try {
            PreparedStatement prepStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM [db_a2efef_dining].[table] " +
                            "WHERE r_id = ? AND table_number = ?;");
            prepStatement.setInt(1, Integer.parseInt(restaurant));
            prepStatement.setInt(2, Integer.parseInt(table));

            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()) {
                temp = new TableListInfo(rs.getString("table_number"), rs.getString("seats"), rs.getString("is_available"));
            }

        } catch(SQLException e){
            Log.e("ERROR", "Table Detail Error: " + e.getMessage());
        }
        return temp;
    }

    public ArrayList<String> getFoodItemDetail()
    {
        ArrayList<String> temp = new ArrayList<>();
        String name = food_item_name;
        try {
            PreparedStatement prepStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM [db_a2efef_dining].[food_item] " +
                            "WHERE name = ?;");
            prepStatement.setString(1, name);

            ResultSet rs = prepStatement.executeQuery();
            // Iterate through results
            while(rs.next()) {
                temp.add(rs.getString("name"));
                if(rs.getString("description") == null || rs.getString("description").equals(""))
                {
                    temp.add("no description available");
                }
                else {
                    temp.add(rs.getString("description"));
                }
                temp.add(rs.getString("price"));
            }

        } catch(SQLException e){
            Log.e("ERROR", "Food Item Detail Error: " + e.getMessage());
        }
        return temp;
    }
}





























