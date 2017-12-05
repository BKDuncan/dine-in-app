package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class PlaceOrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        setButtonListener1();


    }
    private void setButtonListener1(){
        Button checkout = (Button)this.findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent checkoutActivity = new Intent(view.getContext(), CheckoutActivity.class);
                startActivity(checkoutActivity);
            }
        });
    }



}