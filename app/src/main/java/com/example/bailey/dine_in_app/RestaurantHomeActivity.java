package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class RestaurantHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*** REMOVE BLUE STATUS BAR **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        setButtonListeners();

        // Set Title to restaurant name
        TextView title = this.findViewById(R.id.r_homepage_title);
        String r_name = DatabaseController.getInstance().get_logged_in_restaurant().split(";")[1];
        title.setText(r_name);
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

                Intent tablesActivity = new Intent(view.getContext(), ShowTableListActivity.class);
                startActivity(tablesActivity);
            }
        });
    }

}
