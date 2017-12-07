package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ShowOrderDetailActivity extends AppCompatActivity {

    // Text Views to Populate
    private TextView order_number;
    private TextView date;
    private TextView time;
    private TextView special_request;
    private TextView is_served;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_detail);
        order_number = findViewById(R.id.detail_order_number);
        date = findViewById(R.id.detail_order_date);
        time = findViewById(R.id.detail_order_time);
        special_request = findViewById(R.id.detail_order_special_request);
        is_served = findViewById(R.id.detail_order_is_served);
        total = findViewById(R.id.detail_order_price);

        ArrayList<String> order_details = DatabaseController.getInstance().getOrderDetail();
        order_number.setText(order_details.get(0));
        date.setText(order_details.get(1));
        time.setText(order_details.get(2));
        special_request.setText(order_details.get(3));
        is_served.setText((order_details.get(4).matches("1"))?"Served":"In Progress");
        total.setText(order_details.get(5));
    }

}
