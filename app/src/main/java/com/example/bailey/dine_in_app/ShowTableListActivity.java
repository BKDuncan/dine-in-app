package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;



public class ShowTableListActivity extends AppCompatActivity {
    PopulateTableList populateList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_list);
        setButtonListener3();
        setButtonListener4();
        setListSelectionListener();
        if(populateList == null)
        {
            populateList = new PopulateTableList();
            populateList.execute((Void) null);
        }
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        setContentView(R.layout.activity_show_table_list);
        setButtonListener3();
        setButtonListener4();
        setListSelectionListener();
        if(populateList == null)
        {
            populateList = new PopulateTableList();
            populateList.execute((Void) null);
        }
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
        Button showTableReservation = (Button)this.findViewById(R.id.refresh_button);
        showTableReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(ShowTableListActivity.this.populateList == null){
                    populateList = new PopulateTableList();
                    populateList.execute((Void) null);
                }
            }
        });
    }
    private void setListSelectionListener(){
        final ListView listView = (ListView) this.findViewById(R.id.table_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                TableListInfo selection = (TableListInfo) listView.getItemAtPosition(i);
                db.setSelectedTable(selection.getTableNumber());
                Intent tableDetail = new Intent(view.getContext(), ShowTableDetail.class);
                startActivity(tableDetail);
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