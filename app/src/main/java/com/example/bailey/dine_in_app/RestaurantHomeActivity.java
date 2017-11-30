package com.example.bailey.dine_in_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class RestaurantHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setButtonListeners();
    }

    private void setButtonListeners(){
        Button navigateTransactions = (Button)this.findViewById(R.id.transactionButton);
        navigateTransactions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent backToMain = new Intent(view.getContext(), MainActivity.class);
//                startActivity(backToMain);
            }
        });

        Button navigateFoodItems = (Button)this.findViewById(R.id.foodItemButton);
        navigateFoodItems.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent backToMain = new Intent(view.getContext(), MainActivity.class);
//                startActivity(backToMain);
            }
        });

        Button navigateOrders = (Button)this.findViewById(R.id.orderButton);
        navigateOrders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent backToMain = new Intent(view.getContext(), MainActivity.class);
//                startActivity(backToMain);
            }
        });

        Button navigateTables = (Button)this.findViewById(R.id.tablesButton);
        navigateTables.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent backToMain = new Intent(view.getContext(), MainActivity.class);
//                startActivity(backToMain);
            }
        });
    }

}
