package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setButtonListener1();
        setButtonListener2();


    }
    // sets the cancel button
    private void setButtonListener1(){
        Button cancel = (Button)this.findViewById(R.id.cancel_payment_button);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent checkoutActivity = new Intent(view.getContext(), CheckoutActivity.class);
                startActivity(checkoutActivity);
            }
        });
    }
    // sets the pay now button
    private void setButtonListener2(){
        Button payNow = (Button)this.findViewById(R.id.pay_now_button);
        payNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent customerHome = new Intent(view.getContext(), CustomerHomeActivity.class);
                startActivity(customerHome);
            }
        });
    }

}
