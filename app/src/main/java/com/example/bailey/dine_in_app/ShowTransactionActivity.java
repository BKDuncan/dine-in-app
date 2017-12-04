package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ShowTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction);
        setButtonListeners();
    }

    public void setButtonListeners() {
        Button navigateTransactionDetail = (Button) this.findViewById(R.id.show_transaction_detail_button);
        navigateTransactionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showTransactionDetailActivity = new Intent(view.getContext(), ShowTransactionDetail.class);
                startActivity(showTransactionDetailActivity);
            }
        });
    }

}
