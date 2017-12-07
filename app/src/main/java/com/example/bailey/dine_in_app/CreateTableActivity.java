package com.example.bailey.dine_in_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTableActivity extends AppCompatActivity {

    private CreateTableTask tableTask = null;

    EditText seat_text;
    CheckBox is_available_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        setButtonListener();

        seat_text = findViewById(R.id.new_table_seats);
        is_available_box = findViewById(R.id.table_available_for_res_checkbox);
    }

    private void setButtonListener(){
        Button createTable = (Button)this.findViewById(R.id.create_new_table_button);
        createTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tableTask = new CreateTableTask();
                tableTask.execute();
            }
        });
    }

    /**
     * Uses a separate thread to try and login the user
     */
    public class CreateTableTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            int seats = Integer.parseInt(seat_text.getText().toString());
            boolean is_available = is_available_box.isActivated();
            return db.add_table(seats, is_available);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            tableTask = null;

            if (success) {
                Toast.makeText(CreateTableActivity.this.getBaseContext(), "Table Added Successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(CreateTableActivity.this.getBaseContext(), "Add Table Failed!!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            tableTask = null;
            Toast.makeText(CreateTableActivity.this.getBaseContext(), "New Table Cancelled", Toast.LENGTH_LONG).show();
        }
    }

}
