package com.example.ben.tezfillup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    Button buttonLogin;
    CheckBox checkBoxRememberMe;
    String a;
    String geleninfo;
    String logincontrol;
    String yaziliemail;

    SqliteDatabase sqliteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        setContentView(R.layout.activity_login);
        sqliteDatabase = new SqliteDatabase(this);
        initCreateAccountTextView();
        initViews();

        List<String> list = sqliteDatabase.UserList("true");
        if (list.size() != 0) {
            a = list.get(list.size() - 1);
            String email = a.split("-")[1];
            String password = a.split("-")[2];
            editTextEmail.setText(email);
            editTextPassword.setText(password);
            checkBoxRememberMe.setChecked(true);
        }



        Intent i = getIntent();
        this.geleninfo= i.getStringExtra("userinfo");
        if(geleninfo!=null){
            yaziliemail=geleninfo.split("-")[0];
            logincontrol=geleninfo.split("-")[1];
            editTextEmail.setText(yaziliemail);
            editTextPassword.setText("");
            checkBoxRememberMe.setChecked(false);
        }

        checkBoxRememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = editTextEmail.getText().toString();
                String Password = editTextPassword.getText().toString();
                Boolean BooleanRememberMe = (checkBoxRememberMe.isChecked());
                String RememberMe = BooleanRememberMe.toString();
                sqliteDatabase.UpdateUser(Email,Password, RememberMe);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {

                //Check user input is correct or not
                if (validate()) {
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    String currentUser = sqliteDatabase.LoginUser(Email, Password);
                    //Check Authentication is successful or not
                    if (currentUser != null) {
                        Snackbar.make(buttonLogin, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();
                        //User Logged in Successfully Launch You home screen activity
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        //User Logged in Failed
                        Snackbar.make(buttonLogin, "Failed to log in , please try again", Snackbar.LENGTH_LONG).show();
                    }
                }
                String Email = editTextEmail.getText().toString();

                SharedPreferences.Editor editor = getSharedPreferences("MYPREFS", MODE_PRIVATE).edit();
                editor.putString("ActiveUserEmail", Email);
                editor.commit();
            }
        });
    }

    //this method used to set Create account TextView text and click event( maltipal colors
    // for TextView yet not supported in Xml so i have done it programmatically)
    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#990000'>create one</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkbox_RememberMe);
    }

    //This method is for handling fromHtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
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
            }
            else {
                Passwordvalid = false;
                textInputLayoutPassword.setError("Enter minimum 4 characters for password!");
            }
        }

        //Handling validation for Email field
        if (Email.isEmpty()) {
            Emailvalid = false;
            textInputLayoutPassword.setError("Please enter valid email!");
        }
        else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                Emailvalid = false;
                textInputLayoutEmail.setError("Please enter valid email!");
            }
            else {
                Emailvalid = true;
                textInputLayoutEmail.setError(null);
            }
        }
        if (Emailvalid == true && Passwordvalid == true){
            return true;
        }
        else {
            return false;
        }
    }
}