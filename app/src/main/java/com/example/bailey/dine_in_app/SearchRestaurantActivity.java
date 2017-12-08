package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class SearchRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);
        setButtonListener();
        NetworkTasks n = new NetworkTasks();
        n.execute((Void)null);
    }
    private void setButtonListener(){
        Button search = (Button)this.findViewById(R.id.search_restaurant_button);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatabaseController db = DatabaseController.getInstance();
                Spinner city = (Spinner) SearchRestaurantActivity.this.findViewById(R.id.city_spinner);
                Spinner cuisine = (Spinner) SearchRestaurantActivity.this.findViewById(R.id.cusisine_type_spinner);
                TextView text = (TextView) SearchRestaurantActivity.this.findViewById(R.id.restaurant_name_textbox);
                db.setSearchedRestaurant(text.getText().toString());
                db.setSelectedCity(city.getSelectedItem().toString());
                db.setSelectedCuisineType(cuisine.getSelectedItem().toString());
                Intent restaurantList = new Intent(view.getContext(), SearchRestaurantListActivity.class);
                startActivity(restaurantList);
            }
        });
    }

    public class NetworkTasks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            final ArrayAdapter<String> cuisineSpinnerAdapter = new ArrayAdapter<String>(SearchRestaurantActivity.this, R.layout.support_simple_spinner_dropdown_item);
            final ArrayAdapter<String> citySpinnerAdapter = new ArrayAdapter<String>(SearchRestaurantActivity.this, R.layout.support_simple_spinner_dropdown_item);
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            ArrayList<String> cuisineTypes = db.getCuisineTypeList();
            for(int i = 0; i < cuisineTypes.size(); i++)
            {
                cuisineSpinnerAdapter.add(cuisineTypes.get(i));
            }
            ArrayList<String> cities = db.getCityList();
            for(int i = 0; i < cities.size(); i++)
            {
                citySpinnerAdapter.add(cities.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Spinner cuisineTypeSpinner = (Spinner) SearchRestaurantActivity.this.findViewById(R.id.cusisine_type_spinner);
                    cuisineTypeSpinner.setAdapter(cuisineSpinnerAdapter);
                    Spinner citySpinner = (Spinner) SearchRestaurantActivity.this.findViewById(R.id.city_spinner);
                    citySpinner.setAdapter(citySpinnerAdapter);
                }
            });

            return null;
        }
    }
}

