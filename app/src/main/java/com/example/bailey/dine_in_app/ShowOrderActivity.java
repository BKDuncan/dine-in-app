package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ShowOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        setButtonListeners();
    }

    private void setButtonListeners() {
        Button showOrderDetail = (Button) this.findViewById(R.id.show_order_detail_button);
        showOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: ShowOrderDetailActivity
//                Intent showOrderDetailActivity = new Intent(view.getContext(), ShowOrderDetailActivity.class);
//                startActivity(showOrderDetailActivity);
            }
        });
    }
}
