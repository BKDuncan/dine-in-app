package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateFoodItem extends AppCompatActivity {

    private CreateFoodTask foodTask = null;

    private EditText name;
    private EditText price;
    private EditText description;
    private Spinner food_type;
    private Spinner meal_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*** REMOVE BLUE STATUS BAR **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food_item);
        setButtonListener();

        name = findViewById(R.id.date_tv);
        price = findViewById(R.id.food_item_price_tv);
        description = findViewById(R.id.time_tv);
        meal_type = findViewById(R.id.meal_type_spin);
        food_type = findViewById(R.id.food_type_spin);

    }

    private void setButtonListener(){
        Button createFoodItem = this.findViewById(R.id.add_food_item_button);
        createFoodItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                    String f_name = name.getText().toString();
                    Double f_price = -1.0;
                try { f_price = Double.parseDouble(price.getText().toString()); } catch(NumberFormatException e){}
                    String f_description = description.getText().toString();
                    String f_food_type = food_type.getSelectedItem().toString();
                    String f_meal_type = meal_type.getSelectedItem().toString();

                boolean invalid = false;
                /*** ERROR CHECK ***/
                if(f_name.length() < 1){
                    name.setError("Required");
                    invalid = true;
                }
                if(f_price < 0.01){
                    price.setError("Invalid Price");
                    invalid = true;
                }
                if(invalid)
                    return;

                // Execute insert operation on separate thread
                foodTask = new CreateFoodTask(f_name, f_description, f_food_type, f_meal_type, f_price);
                foodTask.execute();
                Toast.makeText(CreateFoodItem.this.getBaseContext(), "Please be patient while the food item is added...", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Uses a separate thread to try and login the user
     */
    public class CreateFoodTask extends AsyncTask<Void, Void, Boolean> {

        private final String name, description, cuisine, meal_type;
        private final double price;

        CreateFoodTask(String name, String description, String cuisine, String meal_type, double price) {
            this.name = name;
            this.description = description;
            this.cuisine = cuisine;
            this.meal_type = meal_type;
            this.price = price;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.add_food_item(name, meal_type, true, cuisine, price, description);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            foodTask = null;

            if (success) {
                Toast.makeText(CreateFoodItem.this.getBaseContext(), "Food Added Successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(CreateFoodItem.this.getBaseContext(), "Add Food Item Failed!!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            foodTask = null;
            Toast.makeText(CreateFoodItem.this.getBaseContext(), "New Food Item Cancelled", Toast.LENGTH_LONG).show();
        }
    }

}
