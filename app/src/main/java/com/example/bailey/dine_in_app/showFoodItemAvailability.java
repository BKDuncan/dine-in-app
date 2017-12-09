package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class showFoodItemAvailability extends AppCompatActivity {
    private changeAvailabilityTask changeAvaibility = null;
    private checkAvailabilityTask checkAvaibility = null;
    String foodItemName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_item_availability);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        foodItemName = getIntent().getStringExtra("itemName");
        TextView title = (TextView)findViewById(R.id.fooditemAvail);
        title.setText(foodItemName);

        checkAvaibility = new showFoodItemAvailability.checkAvailabilityTask(foodItemName);
        checkAvaibility.execute();

        setButtonListener();

    }


    private void setButtonListener(){
        Button toggle = (Button) findViewById(R.id.toggleAvail);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeAvaibility = new showFoodItemAvailability.changeAvailabilityTask(foodItemName);
                changeAvaibility.execute();
            }
        });
    }




    public class checkAvailabilityTask extends AsyncTask<Void, Void, Boolean> {
        String foodName = "";

        checkAvailabilityTask(String foodName) {
            this.foodName = foodName;


            //Toast.makeText(MakeReservationActivity.this.getBaseContext(), time.toString() + " " + date.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.view_availability(foodName,showFoodItemAvailability.this);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            checkAvaibility = null;//

            if (success) {
                //finish();
                Toast.makeText(showFoodItemAvailability.this.getBaseContext(), "Sucessful", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(showFoodItemAvailability.this.getBaseContext(), "Failed to Change Availability", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            checkAvaibility = null;
            Toast.makeText(showFoodItemAvailability.this.getBaseContext(), " Cancelled", Toast.LENGTH_LONG).show();
        }
    }












    public class changeAvailabilityTask extends AsyncTask<Void, Void, Boolean> {

        public String foodName;




        changeAvailabilityTask(String foodName) {
            this.foodName = foodName;

            //Toast.makeText(MakeReservationActivity.this.getBaseContext(), time.toString() + " " + date.toString(), Toast.LENGTH_LONG).show();


        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.change_availability(foodName,showFoodItemAvailability.this);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            changeAvaibility = null;//

            if (success) {
                finish();
                //Toast.makeText(ShowFoodItemsActivity.this.getBaseContext(), "Sucessful", Toast.LENGTH_LONG).show();


                //Intent showFoodItem = new Intent(showFoodItemAvailability.this, showFoodItemAvailability.class);
                //startActivity(showFoodItem);
            } else {
                Toast.makeText(showFoodItemAvailability.this.getBaseContext(), "Failed to Change Availability 79", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            changeAvaibility = null;
            Toast.makeText(showFoodItemAvailability.this.getBaseContext(), " Cancelled", Toast.LENGTH_LONG).show();
        }
    }


}
