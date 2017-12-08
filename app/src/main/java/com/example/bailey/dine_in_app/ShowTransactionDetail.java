package com.example.bailey.dine_in_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowTransactionDetail extends AppCompatActivity {

    private TransactionDetailTask detailTask = null;

    private TextView subtotal;
    private TextView tip;
    private TextView date;
    private TextView time;
    private TextView payment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*** REMOVE BLUE STATUS BAR **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction_detail);

        subtotal = findViewById(R.id.transaction_detail_price);
        tip = findViewById(R.id.transaction_detail_tip);
        date = findViewById(R.id.transaction_detail_date);
        time = findViewById(R.id.transaction_detail_time);
        payment_type = findViewById(R.id.transaction_detail_payment_type);

        detailTask = new TransactionDetailTask();
        detailTask.execute();
    }

    /**
     * Uses a separate thread to fetch the transaction details
     */
    public class TransactionDetailTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.getTransactionDetail();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> details) {
            detailTask = null;

            if (details.size() == 8) {
                // Arraylist Received in the form: t_number, payment_type, tip, r_id, order_number, total_price, date time
                subtotal.setText(details.get(5));
                tip.setText(details.get(2));
                date.setText(details.get(6));
                time.setText(details.get(7));
                payment_type.setText(details.get(1));
            } else {
                Toast.makeText(ShowTransactionDetail.this.getBaseContext(), "Transaction Detail Fetch Failed!!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            detailTask = null;
        }
    }
}
