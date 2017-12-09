package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class CheckoutActivity extends AppCompatActivity {

    private NetworkTask networkTask = null;
    private RemoveItemTask removeItemTask = null;

    private TextView subtotal;
    private ListView order_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*** REMOVE BLUE STATUS BAR **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        order_items = findViewById(R.id.checkout_order_list);
        setButtonListener1();
        setButtonListener2();

        String order = DatabaseController.getInstance().getOrderAdded();
        subtotal = findViewById(R.id.checkout_subtotal);
        subtotal.setText(String.format("$ %.2f", Double.parseDouble(order.split(";")[1])));

        networkTask = new NetworkTask();
        networkTask.execute();
    }

    // set the cancel button
    private void setButtonListener1(){
        Button cancel = this.findViewById(R.id.checkout_cancel_order_button);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // set the confirm button
    private void setButtonListener2() {
        Button confirm = this.findViewById(R.id.checkout_confirm_order_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TransactionActivity = new Intent(view.getContext(), TransactionActivity.class);
                startActivity(TransactionActivity);
            }
        });

        order_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                String selection = (String) order_items.getItemAtPosition(i);

                removeItemTask = new RemoveItemTask((selection.split("\\$")[0]).trim());
                removeItemTask.execute();

                ArrayAdapter<String> adapter = (ArrayAdapter<String>)order_items.getAdapter();
                adapter.remove(adapter.getItem(i));
                adapter.notifyDataSetChanged();

                if(adapter.getCount() == 0)
                    finish();
                return false;
            }
        });
    }

    /**
     * Uses a separate thread to fetch the order details
     */
    public class RemoveItemTask extends AsyncTask<Void, Void, Void> {
        private String item;

        public  RemoveItemTask(String item_name){
            item = item_name;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            double new_total = db.removeItemFromOrder(item);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    subtotal.setText(String.format("$ %.2f", new_total));
                }
            });
            removeItemTask = null;
            return (Void) null;
        }

        @Override
        protected void onCancelled() {
            removeItemTask = null;
        }
    }
    /**
     * Uses a separate thread to fetch the order details
     */
    public class NetworkTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.getOrderItems();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> reservation_list) {
            networkTask = null;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckoutActivity.this, R.layout.support_simple_spinner_dropdown_item);
            for(String reservation : reservation_list)
                adapter.add(reservation);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    order_items.setAdapter(adapter);
                }
            });
        }

        @Override
        protected void onCancelled() {
            networkTask = null;
        }
    }
}
