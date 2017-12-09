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

public class ShowOrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_detail);
        setButtonListener();
        NetworkTasks n = new NetworkTasks();
        n.execute((Void) null);
    }

    private void setButtonListener(){
        Button search = (Button)this.findViewById(R.id.order_served_button);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ServeOrder o = new ServeOrder();
                o.execute((Void) null);
            }
        });
    }

    public class ServeOrder extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            db.serveOrder();
            NetworkTasks n = new NetworkTasks();
            n.execute((Void) null);
            return null;
        }
    }

    public class NetworkTasks extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseController db = DatabaseController.getInstance();
            db.connect();
            final ArrayList<String> orderDetail = db.getOrderDetail();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView order_number = (TextView)  ShowOrderDetailActivity.this.findViewById(R.id.order_number_text);
                    order_number.setText(orderDetail.get(0));

                    TextView date = (TextView)  ShowOrderDetailActivity.this.findViewById(R.id.date_text);
                    date.setText(orderDetail.get(1));

                    TextView time = (TextView)  ShowOrderDetailActivity.this.findViewById(R.id.time_text);
                    time.setText(orderDetail.get(2));

                    TextView special = (TextView)  ShowOrderDetailActivity.this.findViewById(R.id.special_request_text);
                    special.setText(orderDetail.get(3));

                    TextView served = (TextView)  ShowOrderDetailActivity.this.findViewById(R.id.is_served_text);
                    if(orderDetail.get(4).equals("0"))
                    {
                        served.setText("no");
                    }
                    else
                    {
                        served.setText("yes");
                        Button servedButton = (Button) ShowOrderDetailActivity.this.findViewById(R.id.order_served_button);
                        servedButton.setVisibility(View.GONE);
                    }

                    TextView price = (TextView)  ShowOrderDetailActivity.this.findViewById(R.id.total_price_text);
                    price.setText(orderDetail.get(5));
                }
            });

            return null;
        }
    }

}
