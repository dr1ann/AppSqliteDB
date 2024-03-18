package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {

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
            setContentView(R.layout.activity_main);
            // Other initialization code for MainActivity
        }

        TextInputLayout userName = findViewById(R.id.username);
        TextInputLayout password = findViewById(R.id.password);
        TextInputLayout confirmPass = findViewById(R.id.confirmPass);
        Button regBtn = findViewById(R.id.regBtn);
        Button showData = findViewById(R.id.showData);
        Button loginBtn = findViewById(R.id.loginBtn);
        // Create an instance of the DatabaseHelper class
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameValue = userName.getEditText().getText().toString();
                String passwordValue = password.getEditText().getText().toString();
                String confirmPasswordValue = confirmPass.getEditText().getText().toString();

                boolean isUsernameTaken = dbHelper.isUsernameTaken(usernameValue);

                if(usernameValue.equals("") || passwordValue.equals("") || confirmPasswordValue.equals("")) {
                    Toast.makeText(MainActivity.this, "Fields cannot be empty. please try again.", Toast.LENGTH_SHORT).show();
                } else {



                if(isUsernameTaken == true) {

                    Toast.makeText(MainActivity.this, "Username already taken, please try another one", Toast.LENGTH_SHORT).show();
                } else {

                    if(passwordValue.equals(confirmPasswordValue)) {

                    boolean isSuccess = dbHelper.insertData(usernameValue, passwordValue);
                    if (isSuccess) {
                        Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish(); // Finish the MainActivity to prevent the user from returning to it using the back button
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
                    }

                    } else {
                        Toast.makeText(MainActivity.this, "Password doesn't match, Please try again.", Toast.LENGTH_SHORT).show();
                    }

                }


            }
            }
        });

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(MainActivity.this, ShowData.class);

                // Start the new activity
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);

                // Start the new activity
                startActivity(intent);
            }
        });


    }
}

