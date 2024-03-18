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

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String loggedInUsername = sharedPreferences.getString("username", "");
        String loggedInPassword = sharedPreferences.getString("password", "");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int currentUserID = dbHelper.getUserId(loggedInUsername, loggedInPassword);
        // Check if there is a logged-in user
        if (!loggedInUsername.isEmpty() && !loggedInPassword.isEmpty()) {
            // If there is a logged-in user, start the DashboardActivity
            setContentView(R.layout.activity_dashboard);
        } else {
            // If there is no logged-in user, continue with the normal MainActivity setup
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent the user from returning to it using the back button
            // Other initialization code for MainActivity
        }

        Button btnLogout = findViewById(R.id.btnLogout);

        TextView currentUser = findViewById(R.id.currentUser);
        TextView currentUserItems = findViewById(R.id.userItems);

        if (!loggedInUsername.isEmpty() && !loggedInPassword.isEmpty()) {
            dbHelper.getUserItems(currentUserItems, currentUserID);
            String loggedInUserInfo = "Username: " + loggedInUsername + "\n" + "Password: " + loggedInPassword  + "ID: " + currentUserID;
//            currentUserItems.setText("Items:" + "\n" + "Item name: " + )
            currentUser.setText(loggedInUserInfo);
        } else {
            currentUser.setText("way acc");
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        EditText itemNameID = findViewById(R.id.itemName);
        EditText itemQuantityID = findViewById(R.id.itemQuantity);
        Button addItem = findViewById(R.id.btnAdd);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = itemNameID.getText().toString();
                String quantityInput = itemQuantityID.getText().toString();

                if (!itemName.isEmpty() && !quantityInput.isEmpty()) {
                    try {
                        int itemQuantity = Integer.parseInt(quantityInput);
                        // Quantity input is a valid integer
                        boolean success = dbHelper.addItemForUser(currentUserID, itemName, itemQuantity);

                        if (success) {
                            // Item added successfully
                            Toast.makeText(Dashboard.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                            dbHelper.getUserItems(currentUserItems, currentUserID);
                        } else {
                            // Failed to add item
                            Toast.makeText(Dashboard.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        // Quantity input is not a valid integer
                        Toast.makeText(Dashboard.this, "Please enter a valid integer for quantity", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Either item name or quantity field is empty
                    Toast.makeText(Dashboard.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        // Do nothing or show a message to inform the user they cannot go back
        Toast.makeText(this, "Press Logout to exit", Toast.LENGTH_SHORT).show();
    }






    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // Clear session data (example: username and authentication token)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();
        Toast.makeText(Dashboard.this, "Logout Successful", Toast.LENGTH_SHORT).show();
        // Update UI (example: navigate to login screen)
        Intent intent = new Intent(Dashboard.this, Login.class);
        startActivity(intent);
        finish(); // Close current activity to prevent going back to MainActivity when pressing back button
    }
}
