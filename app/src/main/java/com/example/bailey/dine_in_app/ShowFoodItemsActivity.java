package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowFoodItemsActivity extends AppCompatActivity {

    String selectedItem = "";
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_items);
        setButtonListeners();
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

        ListView listView = (ListView) ShowFoodItemsActivity.this.findViewById(R.id.foodlist);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // highlights the item
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelected(true);
                //selectedItem = (String) (listView.getItemAtPosition(i));
                //Toast.makeText(ShowFoodItemsActivity.this.getBaseContext(), selectedItem, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        // selects the item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (String) (listView.getItemAtPosition(i));
                //Toast.makeText(ShowFoodItemsActivity.this.getBaseContext(), selectedItem, Toast.LENGTH_LONG).show();
            }
        });


        Button changeAvailabilityButton = (Button) findViewById(R.id.food_availability_button);
        changeAvailabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItem.length() != 0){
                    Intent foodItemAvailability = new Intent(view.getContext(), showFoodItemAvailability.class);
                    foodItemAvailability.putExtra("itemName",selectedItem);
                    startActivity(foodItemAvailability);

                }

            }
        });


        /*
        changeAvailabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if an item is selected
                if (selectedItem.length() != 0){
                    changeAvaibility = new ShowFoodItemsActivity.changeAvailabilityTask(selectedItem);
                    changeAvaibility.execute();

                }

            }
        });

        */

    }








    public class showFoodItemsTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();

            final ArrayAdapter<String> fooditemAdapter = new ArrayAdapter<String>(ShowFoodItemsActivity.this, R.layout.support_simple_spinner_dropdown_item);
            ArrayList<String> foundFoodItems = db.show_food_items(4);

            for(int i = 0; i < foundFoodItems.size(); i++){
                fooditemAdapter.add(foundFoodItems.get(i));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) ShowFoodItemsActivity.this.findViewById(R.id.foodlist);
                    listView.setAdapter(fooditemAdapter);
                }
            });
            return null;

        }


    }




}
