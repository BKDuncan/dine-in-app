package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anil Sood on 11/30/2017.
 */

public class ShowReservationDetailActivity extends AppCompatActivity {

    private NetworkTasks n = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reservation_detail);
        setButtonListener();
        if(n == null)
        {
            n = new NetworkTasks();
            n.execute((Void) null);
        }

    }

    private void setButtonListener(){
        Button order = (Button)this.findViewById(R.id.place_order_button);
        order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent placeOrder = new Intent(ShowReservationDetailActivity.this, PlaceOrderActivity.class);
                startActivity(placeOrder);
            }
        });
    }

    public class NetworkTasks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            final ArrayList<String> reservationDetail = db.getReservationDetail();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView res_name = (TextView)  ShowReservationDetailActivity.this.findViewById(R.id.restaurant_text);
                    res_name.setText(reservationDetail.get(0));

                    TextView table_num = (TextView)  ShowReservationDetailActivity.this.findViewById(R.id.table_number_text);
                    table_num.setText(reservationDetail.get(1));

                    TextView date = (TextView)  ShowReservationDetailActivity.this.findViewById(R.id.date_text);
                    date.setText(reservationDetail.get(2));

                    TextView time = (TextView)  ShowReservationDetailActivity.this.findViewById(R.id.time_text);
                    time.setText(reservationDetail.get(3));
                }
            });
            ShowReservationDetailActivity.this.n = null;
            return null;
        }
    }
}
