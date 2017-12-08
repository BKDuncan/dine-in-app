package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class ShowReservationActivity extends AppCompatActivity {
    private NetworkTasks n = null;
    private AddReservedRestaurant a = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reservation);
        setListSelectionListener();
        if(n == null)
        {
            n = new NetworkTasks();
            n.execute((Void) null);
        }

    }

    //TODO -- the list selection listener
    private void setListSelectionListener(){
        final ListView listView = (ListView) this.findViewById(R.id.show_reservation_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                db.setReservationIdDetailOrder(((String) listView.getItemAtPosition(i)).split(" ")[0]);
                Intent reservationDetail = new Intent(ShowReservationActivity.this, ShowReservationDetailActivity.class);
                startActivity(reservationDetail);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                db.setReservationIdDetailOrder(((String) listView.getItemAtPosition(i)).split(" ")[0]);
                if(a == null) {
                    a = new AddReservedRestaurant();
                    a.execute((Void) null);
                }

                Intent placeOrder = new Intent(ShowReservationActivity.this, PlaceOrderActivity.class);
                startActivity(placeOrder);
                return false;
            }
        });
    }
    public class AddReservedRestaurant extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            String s = db.findRestaurantId();
            db.setReservedRestaurant(s);
            ShowReservationActivity.this.a = null;
            return null;
        }
    }
    public class NetworkTasks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            ArrayList<String> reservations = db.getReservationList();
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowReservationActivity.this, R.layout.support_simple_spinner_dropdown_item);
            for(int i = 0; i < reservations.size(); i++)
            {
                adapter.add(reservations.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) ShowReservationActivity.this.findViewById(R.id.show_reservation_listView);
                    listView.setAdapter(adapter);
                }
            });
            ShowReservationActivity.this.n = null;
            return null;
        }
    }
}
