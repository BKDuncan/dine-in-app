package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import android.util.Log;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class SearchRestaurantListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant_list);
        //setButtonListener();
        setListSelectionListener();
        NetworkTasks n = new NetworkTasks();
        n.execute((Void) null);
    }
//    private void setButtonListener(){
//        Button makeReservation = (Button)this.findViewById(R.id.make_reservation_button);
//        makeReservation.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent reservation = new Intent(view.getContext(), MakeReservationActivity.class);
//                startActivity(reservation);
//            }
//        });
//    }

    private void setListSelectionListener(){
        final ListView listView = (ListView) this.findViewById(R.id.restaurant_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                String selection = (String) listView.getItemAtPosition(i);
                db.setSelectedRestaurant((selection));
                Intent reservation = new Intent(view.getContext(), MakeReservationActivity.class);
                startActivity(reservation);
            }
        });
    }

    public class NetworkTasks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            ArrayList<String> restaurants = db.getRestaurantList();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchRestaurantListActivity.this, R.layout.support_simple_spinner_dropdown_item);
            for(int i = 0; i < restaurants.size(); i++)
            {
                adapter.add(restaurants.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) SearchRestaurantListActivity.this.findViewById(R.id.restaurant_listview);
                    listView.setAdapter(adapter);
                }
            });

            return null;
        }
    }
}
