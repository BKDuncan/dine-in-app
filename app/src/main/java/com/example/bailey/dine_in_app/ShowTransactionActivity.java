package com.example.bailey.dine_in_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowTransactionActivity extends AppCompatActivity {

    private TextView fromDate;
    private TextView toDate;
    private DatePickerDialog.OnDateSetListener fromDateSetListener;
    private DatePickerDialog.OnDateSetListener toDateSetListener;

    private String from;
    private String to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction);
        fromDate = (TextView)  findViewById(R.id.transaction_from_date_text);
        toDate = (TextView)  findViewById(R.id.transaction_to_date_text);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog1 = new DatePickerDialog(ShowTransactionActivity.this,
                        android.R.style.Theme_Holo,
                        fromDateSetListener,
                        year, month, day);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
            }
        });
        fromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                from = year + "-" + month + "-" + day;
                String displayDate = day + "-" +
                        DateFormatSymbols.getInstance().getMonths()[month - 1] +
                        " - " + year;
                fromDate.setText(displayDate);
            }
        };

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from == null || from.equals("")) {
                    Toast.makeText(ShowTransactionActivity.this.getBaseContext(),
                            "please select the from date first",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Calendar cal = Calendar.getInstance();
                int year = Integer.parseInt(from.split("-")[0]);
                int month = Integer.parseInt(from.split("-")[1])-1;
                int day = Integer.parseInt(from.split("-")[2]);

                cal.set(year, month, day);
                long min = cal.getTime().getTime();
                Log.d("Date", "Year: " + String.valueOf(year) + " Month: "
                        + String.valueOf(month) + " Day: " + String.valueOf(day)
                        + " Long value: " + String.valueOf(min));

                DatePickerDialog dialog2 = new DatePickerDialog(ShowTransactionActivity.this,
                        android.R.style.Theme_Holo,
                        toDateSetListener, year, month, day);
                dialog2.getDatePicker().setMinDate(min);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        toDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                to = year + "-" + month + "-" + day;
                String displayDate = day + "-" +
                        DateFormatSymbols.getInstance().getMonths()[month - 1] +
                        " - " + year;
                toDate.setText(displayDate);
            }
        };
        setButtonListener();
        setListSelectionListener();
    }

    private void setButtonListener(){
        Button search = (Button)this.findViewById(R.id.show_transactions);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(from == null || to == null) {
                    Toast.makeText(ShowTransactionActivity.this.getBaseContext(),
                            "please select the dates first",
                            Toast.LENGTH_LONG).show();
                }
                ShowTransactionActivity.NetworkTasks n = new ShowTransactionActivity.NetworkTasks();
                n.execute((Void) null);
            }
        });
    }

    private void setListSelectionListener(){
        final ListView listView = (ListView) this.findViewById(R.id.transaction_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseController db = DatabaseController.getInstance();
                TransactionListInfo selection = (TransactionListInfo) listView.getItemAtPosition(i);
                db.setSelectedTransaction(String.valueOf(selection.getTransaction_id()));
                Intent tDetail = new Intent(view.getContext(), ShowTransactionDetail.class);
                startActivity(tDetail);
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
            ArrayList<TransactionListInfo> transactions = db.getTransactionList(from, to);

            final TransactionListAdapter adapter = new TransactionListAdapter(ShowTransactionActivity.this,
                    R.layout.transaction_list_adapter, transactions);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = (ListView) ShowTransactionActivity.this.findViewById(R.id.transaction_list_view);
                    listView.setAdapter(adapter);
                }
            });

            return null;
        }
    }

}
