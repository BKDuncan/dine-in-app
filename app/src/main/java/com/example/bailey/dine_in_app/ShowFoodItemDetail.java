package com.example.bailey.dine_in_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowFoodItemDetail extends AppCompatActivity {
    private PopulateFoodItemDetail populate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_item_detail);
        setButtonListener();
        if(populate == null)
        {
            populate = new PopulateFoodItemDetail();
            populate.execute((Void) null);
        }
    }
    private void setButtonListener() {
        Button checkout = (Button) this.findViewById(R.id.add_to_order_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView name = (TextView) ShowFoodItemDetail.this.findViewById(R.id.food_item_name_text);
                DatabaseController db = DatabaseController.getInstance();
                db.addFoodItem(name.getText().toString());
                Toast.makeText(ShowFoodItemDetail.this.getBaseContext(), "food item added to your order", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public class PopulateFoodItemDetail extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            ArrayList<String> foodItem = db.getFoodItemDetail();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView name = (TextView) ShowFoodItemDetail.this.findViewById(R.id.food_item_name_text);
                    name.setText(foodItem.get(0));

                    TextView desc = (TextView) ShowFoodItemDetail.this.findViewById(R.id.food_item_description_text);
                    desc.setText(foodItem.get(1));

                    TextView price = (TextView) ShowFoodItemDetail.this.findViewById(R.id.food_item_price_text);
                    price.setText(foodItem.get(2));
                }
            });
            ShowFoodItemDetail.this.populate = null;
            return null;
        }

    }
}
