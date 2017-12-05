package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CustomerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        addButtonListeners();
    }

    public void addButtonListeners(){
        Button show_reservations = (Button)findViewById(R.id.show_reservation_button);
        show_reservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Make showReservationActivity
                Intent showReservationActivity = new Intent(CustomerHomeActivity.this, ShowReservationActivity.class);
                startActivity(showReservationActivity);
            }
        });

        Button make_reservations = (Button)findViewById(R.id.add_food_item_button);
        make_reservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Make searchRestaurantActivity
                 Intent searchRestaurantActivity = new Intent(CustomerHomeActivity.this, SearchRestaurantActivity.class);
                 startActivity(searchRestaurantActivity);
                //Intent makeReservationActivity = new Intent(CustomerHomeActivity.this, MakeReservationActivity.class);
                //startActivity(makeReservationActivity);
            }
        });
    }
}
