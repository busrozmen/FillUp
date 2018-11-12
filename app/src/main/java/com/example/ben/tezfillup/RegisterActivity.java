package com.example.ben.tezfillup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    Button buttonRegister;

    SqliteDatabase sqliteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        setContentView(R.layout.activity_register);
        sqliteDatabase = new SqliteDatabase(this);
        initTextViewLogin();
        initViews();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();
                    //Check in the database is there any user associated with  this email
                    if (!sqliteDatabase.isEmailExists(Email)) {
                        //Email does not exist now add new user to database
                        sqliteDatabase.InsertUser(Email, Password, "false");
                        Snackbar.make(buttonRegister, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, Snackbar.LENGTH_LONG);

                        String info=Email+"-1";
                        Intent i = new Intent();
                        i.setClass(RegisterActivity.this, LoginActivity.class);
                        i.putExtra("userinfo",info);
                        startActivity(i);
                    }
                    else {
                        //Email exists with email input provided so show error user already exist
                        Snackbar.make(buttonRegister, "User already exists with same email ", Snackbar.LENGTH_LONG).show();
                        editTextEmail.setText("");
                    }
                }
            }
        });
    }

    //this method used to set Login TextView click event
    private void initTextViewLogin() {
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean Emailvalid = false;
        boolean Passwordvalid = false;

        //Get values from EditText fields
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for Password field
        if (Password.isEmpty()) {
            Passwordvalid = false;
            textInputLayoutPassword.setError("Please enter valid password!");
        }
        else {
            if (Password.length() > 3) {
                Passwordvalid = true;
                textInputLayoutPassword.setError(null);
            } else {
                Passwordvalid = false;
                textInputLayoutPassword.setError("Enter minimum 4 characters for password!");
            }}

        //Handling validation for Email field
        if (Email.isEmpty()) {
            Emailvalid = false;
            textInputLayoutPassword.setError("Please enter valid email!");
        }
        else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                Emailvalid = false;
                textInputLayoutEmail.setError("Please enter valid email!");
            } else {
                Emailvalid = true;
                textInputLayoutEmail.setError(null);
            }}
        if (Emailvalid == true && Passwordvalid == true){
            return true;
        }
        else {
            return false;
        }
    }
}