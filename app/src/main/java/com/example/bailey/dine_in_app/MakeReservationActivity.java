package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MakeReservationActivity extends AppCompatActivity {

    private makeReservationTask reservationTask = null;

    private EditText date;//
    private EditText time;//
    private EditText numOfSeats;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setButtonListener();//

        date = findViewById(R.id.date_tv);//
        time = findViewById(R.id.time_tv);//
        numOfSeats = findViewById(R.id.numOfSeat_tv);//


    }

    private void setButtonListener(){
        Button bookNow = (Button)this.findViewById(R.id.book_new_reservation_button);
        bookNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String f_date = date.getText().toString();
                String f_time = time.getText().toString();
                Integer f_numOfSeat = Integer.parseInt(numOfSeats.getText().toString());

                // Execute insert operation on separate thread
                reservationTask = new MakeReservationActivity.makeReservationTask(f_date,f_time,f_numOfSeat);
                reservationTask.execute();

                //Toast.makeText(CreateFoodItem.this.getBaseContext(), "Please be patient while the food item is added...", Toast.LENGTH_LONG).show();
            }
        });
    }


    public class makeReservationTask extends AsyncTask<Void, Void, Boolean> {

        private final String time,date;
        private final Integer numOfSeats;


        makeReservationTask(String time, String date, Integer numOfSeats) {
            this.time = time;
            this.date = date;
            this.numOfSeats = numOfSeats;


        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.make_reservation(date,time,numOfSeats);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            reservationTask = null;//

            if (success) {
                finish();
                Toast.makeText(MakeReservationActivity.this.getBaseContext(), "Successfully", Toast.LENGTH_LONG).show();
                //Intent showFoodItem = new Intent(CreateFoodItem.this, ShowFoodItemsActivity.class);
                //startActivity(showFoodItem);
            } else {
                Toast.makeText(MakeReservationActivity.this.getBaseContext(), "Failed!!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            reservationTask = null;
            Toast.makeText(MakeReservationActivity.this.getBaseContext(), " Cancelled", Toast.LENGTH_LONG).show();
        }
    }

}
