package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class CreateFoodItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setButtonListener();
    }

    private void setButtonListener(){
        Button createFoodItem = (Button)this.findViewById(R.id.add_food_item_button);
        createFoodItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent showFoodItem = new Intent(view.getContext(), ShowFoodItemsActivity.class);
                startActivity(showFoodItem);
            }
        });
    }
}
