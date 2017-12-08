package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class ShowFoodItemsActivity extends AppCompatActivity {
    private showFoodItemsTask showFoodItemsTask1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_items);
        setButtonListeners();
        setButtonListeners1();
        showFoodItemsTask n = new showFoodItemsTask();
        n.execute((Void)null);
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


    private void setButtonListeners1() {
        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFoodItemsTask1 = new showFoodItemsTask();
                showFoodItemsTask1.execute();

            }
        });
    }





    public class showFoodItemsTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();

            final ArrayAdapter<String> fooditemAdapter = new ArrayAdapter<String>(ShowFoodItemsActivity.this, R.layout.support_simple_spinner_dropdown_item);
            db.show_food_items();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;

        }


    }

}
