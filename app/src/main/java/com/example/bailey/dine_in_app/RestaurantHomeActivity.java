package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RestaurantHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        setButtonListeners();
    }

    public void setButtonListeners(){
        Button navigateTransactions = (Button) findViewById(R.id.transaction_menu_button);
        navigateTransactions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent transactionsActivity = new Intent(view.getContext(), ShowTransactionActivity.class);
                startActivity(transactionsActivity);
            }
        });

        Button navigateFoodItems = (Button) findViewById(R.id.food_item_menu_button);
        navigateFoodItems.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent foodItemsActivity = new Intent(view.getContext(), ShowFoodItemsActivity.class);
                startActivity(foodItemsActivity);
            }
        });

        Button navigateOrders = (Button) findViewById(R.id.order_menu_button);
        navigateOrders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent ordersActivity = new Intent(view.getContext(), ShowOrderActivity.class);
                startActivity(ordersActivity);
            }
        });

        Button navigateTables = (Button) findViewById(R.id.table_menu_button);
        navigateTables.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: ShowTables Activity
//                Intent tablesActivity = new Intent(view.getContext(), ShowTablesActivity.class);
//                startActivity(tablesActivity);
            }
        });
    }

}
