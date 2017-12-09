package com.example.bailey.dine_in_app;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TransactionActivity extends AppCompatActivity {
    private CreateTransactionTask transactionTask = null;

    private EditText tip_text;
    private Spinner payment_type_spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*** REMOVE BLUE STATUS BAR **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        setButtonListener1();
        setButtonListener2();

        tip_text = findViewById(R.id.transaction_tip);
        payment_type_spin = findViewById(R.id.transaction_payment_type);
    }

    // sets the cancel button
    private void setButtonListener1(){
        Button cancel = this.findViewById(R.id.cancel_payment_button);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    // sets the pay now button
    private void setButtonListener2(){
        Button payNow = this.findViewById(R.id.pay_now_button);
        payNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String payment_type = payment_type_spin.getSelectedItem().toString();

                /*** ERROR CHECK ***/
                double tip = -1.0;
                try { tip = Double.parseDouble(tip_text.getText().toString()); } catch(NumberFormatException e){}
                if(tip < 0){
                    tip_text.setError("Invalid Value");
                    return;
                }

                transactionTask = new CreateTransactionTask(payment_type, tip);
                transactionTask.execute();
            }
        });
    }

    /**
     * Uses a separate thread to try and login the user
     */
    public class CreateTransactionTask extends AsyncTask<Void, Void, Boolean> {
        String payment_type;
        double tip;

        public CreateTransactionTask(String pt, double t){
            payment_type = pt;
            tip = t;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.add_transaction(payment_type, tip);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            transactionTask = null;

            if (success) {
                Toast.makeText(TransactionActivity.this.getBaseContext(), "Payment Successful", Toast.LENGTH_LONG).show();
                // Clear the activity stack of everything between This Activity and Customer Homepage Activity
                Intent customerHome = new Intent(TransactionActivity.this, CustomerHomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(customerHome);
            } else {
                Toast.makeText(TransactionActivity.this.getBaseContext(), "Payment Failed!!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            transactionTask = null;
            Toast.makeText(TransactionActivity.this.getBaseContext(), "Payment Cancelled", Toast.LENGTH_LONG).show();
        }
    }


}
