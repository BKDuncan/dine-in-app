package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class SearchRestaurantListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant_list);
        setButtonListener();

    }
    private void setButtonListener(){
        Button makeReservation = (Button)this.findViewById(R.id.make_reservation_button);
        makeReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent reservation = new Intent(view.getContext(), MakeReservationActivity.class);
                startActivity(reservation);
            }
        });
    }
}
