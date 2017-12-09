package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    private PopulateReservationList populateList = null;
    private AddReservedRestaurant a = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reservation);
        setListSelectionListener();
        if(populateList == null)
        {
            populateList = new PopulateReservationList();
            populateList.execute((Void) null);
        }

    }

    //TODO -- the list selection listener
    private void setListSelectionListener(){
        final ListView listView = (ListView) this.findViewById(R.id.show_reservation_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                ReservationListInfo temp = (ReservationListInfo) listView.getItemAtPosition(i);
                db.setReservationIdDetailOrder(String.valueOf(temp.getResId()));
                if(a == null){
                    a = new AddReservedRestaurant();
                    a.execute((Void) null);
                }
                Intent reservationDetail = new Intent(ShowReservationActivity.this, ShowReservationDetailActivity.class);
                startActivity(reservationDetail);
            }
        });
    }

    public class AddReservedRestaurant extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();

            String s = db.findRestaurantId();
            db.setReservedRestaurant(s);
            ShowReservationActivity.this.a = null;
            return null;
        }
    }

    public class PopulateReservationList extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();

            ArrayList<ReservationListInfo> reservations = db.getReservationList();

            final ReservationListAdapter adapter = new ReservationListAdapter(ShowReservationActivity.this, R.layout.reservation_list_adapter, reservations);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) ShowReservationActivity.this.findViewById(R.id.show_reservation_listView);
                    listView.setAdapter(adapter);
                }
            });
            ShowReservationActivity.this.populateList = null;
            return null;
        }
    }
}
