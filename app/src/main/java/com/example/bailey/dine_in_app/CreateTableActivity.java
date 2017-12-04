package com.example.bailey.dine_in_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class CreateTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setButtonListener();
    }

    private void setButtonListener(){
        Button createTable = (Button)this.findViewById(R.id.create_new_table_button);
        createTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent backToMain = new Intent(view.getContext(), MainActivity.class);
//                startActivity(backToMain);
            }
        });
    }

}
