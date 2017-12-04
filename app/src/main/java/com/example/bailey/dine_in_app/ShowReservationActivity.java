package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class ShowReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reservation);
        setButtonListener1();
        setButtonListener2();

    }
    private void setButtonListener1(){
        Button placeOrder = (Button)this.findViewById(R.id.place_order_button);
        placeOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent placeOrderView = new Intent(view.getContext(), PlaceOrderActivity.class);
                startActivity(placeOrderView);
            }
        });
    }

    private void setButtonListener2(){
        Button showDetail = (Button)this.findViewById(R.id.show_detail_button);
        showDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent showReservationDetail = new Intent(view.getContext(), ShowReservationDetailActivity.class);
                startActivity(showReservationDetail);
            }
        });
    }
}
