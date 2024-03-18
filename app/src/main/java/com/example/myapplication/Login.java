package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String loggedInUsername = sharedPreferences.getString("username", "");
        String loggedInPassword = sharedPreferences.getString("password", "");

        // Check if there is a logged-in user
        if (!loggedInUsername.isEmpty() && !loggedInPassword.isEmpty()) {
            // If there is a logged-in user, start the DashboardActivity
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent the user from returning to it using the back button
            return;
        } else {
            // If there is no logged-in user, continue with the normal MainActivity setup
            setContentView(R.layout.activity_login);
            // Other initialization code for MainActivity
        }
        TextInputLayout userNameID = findViewById(R.id.txtUsername);
        TextInputLayout passwordID = findViewById(R.id.txtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameID.getEditText().getText().toString();
                String password = passwordID.getEditText().getText().toString();

                if(userName.equals("") || password.equals("")) {
                    Toast.makeText(Login.this, "Fields cannot be empty, please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean isValidLogin = dbHelper.checkUsernamePassword(userName,password);

                    if(isValidLogin) {
                        Toast.makeText(Login.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                        // Save the logged-in user's username to SharedPreferences

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", userName);
                        editor.putString("password", password);



                        editor.apply();


                        Intent intent = new Intent(Login.this, Dashboard.class);

                        // Start the new activity
                        startActivity(intent);
                    } else {


                        Toast.makeText(Login.this, "Username or Password is incorrect, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);

                // Start the new activity
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
       //Prevent user from going back to the registration activity or dashboard

    }
}