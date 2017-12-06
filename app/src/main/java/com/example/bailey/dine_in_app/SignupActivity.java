package com.example.bailey.dine_in_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private UserSignupTask signupTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setButtonListener();
    }

    private void setButtonListener(){
        EditText email_text = this.findViewById(R.id.username_tv);
        EditText first_text = this.findViewById(R.id.fname_tv);
        EditText last_text = this.findViewById(R.id.lname_tv);
        EditText password_text = this.findViewById(R.id.password_tv);
        EditText phone_text = this.findViewById(R.id.phone_tv);
        Spinner city_spinner = this.findViewById(R.id.city_spin);

        Button signup = (Button)this.findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = (email_text.getText().toString());
                String fname = (first_text.getText().toString());
                String lname = (last_text.getText().toString());
                String password = (password_text.getText().toString());
                String phone = (phone_text.getText().toString());
                String city = (city_spinner.getSelectedItem().toString());
                // Execute insert on separate thread
                signupTask = new UserSignupTask(email, phone, city, fname, lname, password);
                signupTask.execute();
                Toast.makeText(SignupActivity.this.getBaseContext(), "Please be patient while we set up your account...", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Uses a separate thread to try and login the user
     */
    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String email, phone, location, firstName, lastName, password;

        UserSignupTask(String email, String phone, String location, String fname, String lname, String password) {
            this.email = email;
            this.phone = phone;
            this.location = location;
            this.firstName = fname;
            this.lastName = lname;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseController db = DatabaseController.getInstance();
            if(!db.is_connected())
                db.connect();
            return db.signup(email, phone, location, firstName, lastName, password);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            signupTask = null;

            if (success) {
                finish();
                Toast.makeText(SignupActivity.this.getBaseContext(), "Signup Successful", Toast.LENGTH_LONG).show();
                Intent backToMain = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(backToMain);
            } else {
                Toast.makeText(SignupActivity.this.getBaseContext(), "Signup Failed!!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            signupTask = null;
            Toast.makeText(SignupActivity.this.getBaseContext(), "Signup Cancelled", Toast.LENGTH_LONG).show();
        }
    }


}
