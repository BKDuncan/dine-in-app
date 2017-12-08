package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;


public class ShowTableListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_list);
        setButtonListener1();
        setButtonListener2();
        setButtonListener3();
        setButtonListener4();

    }
    private void setButtonListener1(){
        Button returnButton = (Button)this.findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent restaurantHome = new Intent(view.getContext(), RestaurantHomeActivity.class);
                startActivity(restaurantHome);
            }
        });
    }
    private void setButtonListener2(){
        Button availability = (Button)this.findViewById(R.id.change_availability_button);
        availability.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setButtonListener3(){
        Button addTable = (Button)this.findViewById(R.id.add_new_table_button);
        addTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent createTable = new Intent(view.getContext(), CreateTableActivity.class);
                startActivity(createTable);
            }
        });
    }
    private void setButtonListener4(){
        Button showTableReservation = (Button)this.findViewById(R.id.show_reservation_button);
        showTableReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent showReservations = new Intent(view.getContext(), ShowTableReservationsActivity.class);
                startActivity(showReservations);
            }
        });
    }

}