package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class PlaceOrderActivity extends AppCompatActivity {

    private ArrayList<String> tempFoodItemList = new ArrayList<>();
    private NetworkTasks n = null;
    private UpdateFoodItemList u = null;
    private AddOrder a = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        setButtonListener1();
        setButtonListener2();
        setListSelectionListener();
        if(n == null) {
            n = new NetworkTasks();
            n.execute((Void) null);
        }

    }

    private void setButtonListener1(){
        Button checkout = (Button)this.findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(tempFoodItemList.size() == 0)
                {
                    Toast.makeText(PlaceOrderActivity.this.getBaseContext(), "your order is empty, please add some items first", Toast.LENGTH_LONG).show();
                }
                else {
                    if(a == null) {
                        AddOrder a = new AddOrder();
                        a.execute((Void) null);
                    }
                    Intent checkoutActivity = new Intent(view.getContext(), CheckoutActivity.class);
                    startActivity(checkoutActivity);
                }
            }
        });
    }

    private void setButtonListener2(){
        Button checkout = (Button)this.findViewById(R.id.refresh_button);
        checkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               if(u == null) {
                   u = new UpdateFoodItemList();
                   u.execute((Void) null);
               }
            }
        });
    }
    private void setListSelectionListener(){
        final ListView listView = (ListView) this.findViewById(R.id.food_item_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // TODO implement click to show details on the food item
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO add logic to show the food item detail
//                DatabaseController db = DatabaseController.getInstance();
//                String selection = (String) listView.getItemAtPosition(i);
//                tempFoodItemList.add(selection);
//                Toast.makeText(PlaceOrderActivity.this.getBaseContext(), "food item added to your order", Toast.LENGTH_LONG).show();
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                String selection = (String) listView.getItemAtPosition(i);
                tempFoodItemList.add(selection);
                Toast.makeText(PlaceOrderActivity.this.getBaseContext(), "food item added to your order", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public class AddOrder extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            db.addNewOrder(tempFoodItemList);
            return null;
        }

    }

    public class UpdateFoodItemList extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            Spinner mealTypeSpinner = (Spinner) PlaceOrderActivity.this.findViewById(R.id.meal_type_spinner);
            Spinner itemTypeSpinner = (Spinner) PlaceOrderActivity.this.findViewById(R.id.item_type_spinner);
            ArrayList<String> foodItems = db.getFoodItemsList(mealTypeSpinner.getSelectedItem().toString(), itemTypeSpinner.getSelectedItem().toString());
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlaceOrderActivity.this, R.layout.support_simple_spinner_dropdown_item);
            for(int i = 0; i < foodItems.size(); i++)
            {
                adapter.add(foodItems.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) PlaceOrderActivity.this.findViewById(R.id.food_item_listView);
                    listView.setAdapter(adapter);
                }
            });
            PlaceOrderActivity.this.u = null;
            return null;
        }

    }


    public class NetworkTasks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            final ArrayAdapter<String> mealTypeSpinnerAdapter = new ArrayAdapter<>(PlaceOrderActivity.this, R.layout.support_simple_spinner_dropdown_item);
            final ArrayAdapter<String> itemTypeSpinnerAdapter = new ArrayAdapter<>(PlaceOrderActivity.this, R.layout.support_simple_spinner_dropdown_item);
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            ArrayList<String> mealTypes = db.getMealTypeList();
            for(int i = 0; i < mealTypes.size(); i++)
            {
                mealTypeSpinnerAdapter.add(mealTypes.get(i));
            }
            ArrayList<String> itemType = db.getItemTypeList();
            for(int i = 0; i < itemType.size(); i++)
            {
                itemTypeSpinnerAdapter.add(itemType.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Spinner mealTypeSpinner = (Spinner) PlaceOrderActivity.this.findViewById(R.id.meal_type_spinner);
                    mealTypeSpinner.setAdapter(mealTypeSpinnerAdapter);
                    Spinner itemTypeSpinner = (Spinner) PlaceOrderActivity.this.findViewById(R.id.item_type_spinner);
                    itemTypeSpinner.setAdapter(itemTypeSpinnerAdapter);
                    UpdateFoodItemList u = new UpdateFoodItemList();
                    u.execute((Void) null);
                }
            });
            PlaceOrderActivity.this.n = null;
            return null;
        }
    }

}