package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowTableDetail extends AppCompatActivity {

    private PopulateTableDetail p = null;
    private ToggleAvailability t = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_detail);
        setButtonListener1();
        setButtonListener2();
        if(p == null){
            p = new PopulateTableDetail();
            p.execute((Void) null);
        }
    }

    private void setButtonListener1(){
        Button showTableReservation = (Button)this.findViewById(R.id.show_reservation_button);
        showTableReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent reservation = new Intent(view.getContext(), ShowTableReservationsActivity.class);
                startActivity(reservation);
            }
        });
    }
    private void setButtonListener2(){
        Button showTableReservation = (Button)this.findViewById(R.id.toggle_avilability_button);
        showTableReservation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatabaseController db = DatabaseController.getInstance();
                if(!db.is_connected())
                    db.connect();
                if(t == null){
                    t = new ToggleAvailability();
                    t.execute((Void)null);
                }
            }
        });
    }

    public class ToggleAvailability extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            db.toggleAvailability();
            if(p == null) {
                p = new PopulateTableDetail();
                p.execute((Void) null);
            }
            ShowTableDetail.this.t = null;
            return null;
        }
    }
    public class PopulateTableDetail extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            TableListInfo table = db.getTableDetail();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tableNum = (TextView) ShowTableDetail.this.findViewById(R.id.tabled_number_text);
                    tableNum.setText(table.getTableNumber());
                    TextView seats = (TextView) ShowTableDetail.this.findViewById(R.id.tabled_seats_text);
                    seats.setText(table.getSeats());
                    TextView ava = (TextView) ShowTableDetail.this.findViewById(R.id.tabled_available_text);
                    if(table.getAvailable().equals("1")) {
                        ava.setText("yes");
                    }
                    else{
                        ava.setText("no");
                    }
                }
            });
            ShowTableDetail.this.p = null;
            return null;
        }
    }
}
