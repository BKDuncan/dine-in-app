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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        setListSelectionListener();
        setButtonListener();
    }

    private void setButtonListener(){
        Button search = (Button)this.findViewById(R.id.show_order_button);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(ShowOrderActivity.this.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
               NetworkTasks n = new NetworkTasks();
               n.execute((Void) null);
            }
        });
    }
    private void setListSelectionListener(){
        final ListView listView = (ListView) this.findViewById(R.id.order_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                String selection = (String) listView.getItemAtPosition(i);
                db.setOrderNumberDetail((selection));
                Intent orderDetail = new Intent(view.getContext(), ShowOrderDetailActivity.class);
                startActivity(orderDetail);
            }
        });
    }

    public class NetworkTasks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            TextView from = (TextView)  ShowOrderActivity.this.findViewById(R.id.from_date_text);
            TextView to = (TextView)  ShowOrderActivity.this.findViewById(R.id.to_date_text);
            String fromDate = ""  + from.getText().toString();
            String toDate = "" + to.getText().toString();
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            ArrayList<String> orders = db.getOrderList(fromDate, toDate);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowOrderActivity.this, R.layout.support_simple_spinner_dropdown_item);
            for(int i = 0; i < orders.size(); i++)
            {
                adapter.add(orders.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) ShowOrderActivity.this.findViewById(R.id.order_listView);
                    listView.setAdapter(adapter);
                }
            });

            return null;
        }
    }
}
