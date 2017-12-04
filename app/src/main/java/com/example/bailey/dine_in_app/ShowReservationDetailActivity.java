package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class ShowReservationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        setButtonListener1();

    }
    private void setButtonListener1(){
        Button returnToList = (Button)this.findViewById(R.id.returnToReservationList);
        returnToList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent reservation = new Intent(view.getContext(), ShowReservationActivity.class);
                startActivity(reservation);
            }
        });
    }
}
