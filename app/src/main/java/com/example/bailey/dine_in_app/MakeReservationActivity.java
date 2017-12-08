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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class MakeReservationActivity extends AppCompatActivity {

    private makeReservationTask reservationTask = null;

    private EditText date;//
    private EditText time;//
    private EditText numOfSeats;//

    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

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


        //Time time_v = new Time(10,10,10);




        makeReservationTask(String date, String time, Integer numOfSeats) {
            this.time = time;
            this.date = date;
            this.numOfSeats = numOfSeats;
            //Toast.makeText(MakeReservationActivity.this.getBaseContext(), time.toString() + " " + date.toString(), Toast.LENGTH_LONG).show();


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
                Toast.makeText(MakeReservationActivity.this.getBaseContext(), "Reservation Sucessful", Toast.LENGTH_LONG).show();
                //Intent showFoodItem = new Intent(CreateFoodItem.this, ShowFoodItemsActivity.class);
                //startActivity(showFoodItem);
            } else {
                Toast.makeText(MakeReservationActivity.this.getBaseContext(), "Failed to add reservation", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            reservationTask = null;
            Toast.makeText(MakeReservationActivity.this.getBaseContext(), " Cancelled", Toast.LENGTH_LONG).show();
        }
    }

}
