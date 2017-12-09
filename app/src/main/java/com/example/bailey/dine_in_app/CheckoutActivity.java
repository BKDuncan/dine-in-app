package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setButtonListener1();
        setButtonListener2();

    }// set the cancel button
    private void setButtonListener1(){
        Button cancel = (Button)this.findViewById(R.id.cancel_order_button);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent PlaceOrderActivity = new Intent(view.getContext(), PlaceOrderActivity.class);
                startActivity(PlaceOrderActivity);
            }
        });
    }
    // set the confirm button
    private void setButtonListener2(){
        Button confirm = (Button)this.findViewById(R.id.confirm_order_button);
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent TransactionActivity = new Intent(view.getContext(), TransactionActivity.class);
                startActivity(TransactionActivity);
            }
        });
    }

}
