package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowTableReservationsActivity extends AppCompatActivity {

    private NetworkTask networkTask = null;

    private ListView table_reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_reservations);

        table_reservations = findViewById(R.id.table_res_list);
        networkTask = new NetworkTask();
        networkTask.execute();
    }

//    private void setListSelectionListener(){
//        table_reservations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                DatabaseController db = DatabaseController.getInstance();
//                String selection = (String) table_reservations.getItemAtPosition(i);
//            }
//        });
//    }

    /**
     * Uses a separate thread to fetch the order details
     */
    public class NetworkTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.getTableReservations();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> reservation_list) {
            networkTask = null;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowTableReservationsActivity.this, R.layout.support_simple_spinner_dropdown_item);
            for(String reservation : reservation_list)
                adapter.add(reservation);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    table_reservations.setAdapter(adapter);
                }
            });
        }

        @Override
        protected void onCancelled() {
            networkTask = null;
        }
    }
}
