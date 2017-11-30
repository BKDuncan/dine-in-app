package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class ShowOrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setButtonListener();

    }
    private void setButtonListener(){
        Button returnToOrder = (Button)this.findViewById(R.id.return_button);
        returnToOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent showOrder = new Intent(view.getContext(), ShowOrderActivity.class);
                startActivity(showOrder);
            }
        });
    }
}
