package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    PopulateRestaurantList populate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant_list);
        //setButtonListener();
        setListSelectionListener();
        if(populate == null){
            populate = new PopulateRestaurantList();
            populate.execute((Void) null);
        }
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
                RestaurantListInfo selection = (RestaurantListInfo) listView.getItemAtPosition(i);
                db.setSelectedRestaurant(String.valueOf(selection.getRes_Id()));
                Intent reservation = new Intent(view.getContext(), MakeReservationActivity.class);
                startActivity(reservation);
            }
        });
    }

    public class PopulateRestaurantList extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            ArrayList<RestaurantListInfo> restaurants = db.getRestaurantList();

            final RestaurantListAdapter adapter = new RestaurantListAdapter(SearchRestaurantListActivity.this, R.layout.restaurant_list_adapter, restaurants);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) SearchRestaurantListActivity.this.findViewById(R.id.restaurant_listview);
                    listView.setAdapter(adapter);
                }
            });
            SearchRestaurantListActivity.this.populate = null;
            return null;
        }
    }
}
