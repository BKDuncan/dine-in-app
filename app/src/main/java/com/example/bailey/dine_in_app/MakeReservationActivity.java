package com.example.bailey.dine_in_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Calendar;

public class MakeReservationActivity extends AppCompatActivity {

    private makeReservationTask reservationTask = null;

    private TextView date;//
    private TextView time;//
    private EditText numOfSeats;//

    private DatePickerDialog.OnDateSetListener resDateSetListener;
    private TimePickerDialog.OnTimeSetListener resTimeSetListener;
    private String reservation_date;
    private String reservation_time;

    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);

        date = findViewById(R.id.date_tv);//
        time = findViewById(R.id.time_tv);//
        numOfSeats = findViewById(R.id.numOfSeat_tv);//

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                cal.set(year, month, day);
                long min = cal.getTime().getTime();

                DatePickerDialog dialog1 = new DatePickerDialog(MakeReservationActivity.this,
                        android.R.style.Theme_Holo,
                        resDateSetListener,
                        year, month, day);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.getDatePicker().setMinDate(min);
                dialog1.show();


            }
        });
        resDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                reservation_date = year + "-" + month + "-" + day;
                String displayDate = day + "-" +
                        DateFormatSymbols.getInstance().getMonths()[month - 1] +
                        " - " + year;
                date.setText(displayDate);
            }
        };

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog1 = new TimePickerDialog(MakeReservationActivity.this,
                        android.R.style.Theme_Holo,
                        resTimeSetListener,
                        hour, minute, true);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
            }
        });

        resTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                reservation_time = hour + ":" + minute + ":" + 00;
                time.setText(reservation_time);
            }
        };

        setButtonListener();
    }

    private void setButtonListener(){
        Button bookNow = this.findViewById(R.id.book_new_reservation_button);
        bookNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int f_numOfSeat = -1;
                try { f_numOfSeat = Integer.parseInt(numOfSeats.getText().toString()); }catch(NumberFormatException e){}

                if(reservation_date == null){
                    date.setError("required");
                    return;
                } else if(reservation_time == null){
                    time.setError("required");
                    return;
                } else if(f_numOfSeat < 1){
                    numOfSeats.setError("invalid");
                    return;
                }
                // Execute insert operation on separate thread
                reservationTask = new MakeReservationActivity.makeReservationTask(reservation_date, reservation_time, f_numOfSeat);
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
