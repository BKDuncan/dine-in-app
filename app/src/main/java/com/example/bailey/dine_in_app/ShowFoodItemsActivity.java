package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ShowFoodItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_items);
        setButtonListeners();
    }

    private void setButtonListeners() {
        Button navigateTransactions = (Button) findViewById(R.id.add_new_food_item_button);
        navigateTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createFoodItemActivity = new Intent(view.getContext(), CreateFoodItem.class);
                startActivity(createFoodItemActivity);
            }
        });
    }
}
