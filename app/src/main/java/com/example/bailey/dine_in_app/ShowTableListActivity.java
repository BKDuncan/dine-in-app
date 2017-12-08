package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Anil Sood on 11/30/2017.
 */

public class ShowTableListActivity extends AppCompatActivity {
    PopulateTableList populateList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_list);
        setButtonListener1();
        setButtonListener2();
        setButtonListener3();
        setButtonListener4();
        if(populateList == null)
        {
            populateList = new PopulateTableList();
            populateList.execute((Void) null);
        }
    }
    private void setButtonListener1(){
        Button returnButton = (Button)this.findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent restaurantHome = new Intent(view.getContext(), RestaurantHomeActivity.class);
                startActivity(restaurantHome);
            }
        });
    }
    private void setButtonListener2(){
        Button availability = (Button)this.findViewById(R.id.change_availability_button);
        availability.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO -- add logic for the change availability in the show table activity
            }
        });
    }

    private void setButtonListener3(){
        Button addTable = (Button)this.findViewById(R.id.add_new_table_button);
        addTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent createTable = new Intent(view.getContext(), CreateTableActivity.class);
                startActivity(createTable);
            }
        });
    }
    private void setButtonListener4(){
        Button showTableReservation = (Button)this.findViewById(R.id.show_reservation_button);
        showTableReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent showReservations = new Intent(view.getContext(), ShowTableReservationsActivity.class);
                startActivity(showReservations);
            }
        });
    }

    public class PopulateTableList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            ArrayList<TableListInfo> tablesList = db.getTableList();
            final TableListAdapter adapter = new TableListAdapter(ShowTableListActivity.this, R.layout.show_table_adapter_layout, tablesList);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) ShowTableListActivity.this.findViewById(R.id.table_list);
                    listView.setAdapter(adapter);
                }
            });
            ShowTableListActivity.this.populateList = null;
            return null;
        }
    }
}