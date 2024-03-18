package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShowData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        TextView textView = findViewById(R.id.showData);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.readData();

// Move the cursor to the first row
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int passwordIndex = cursor.getColumnIndex("password");

            do {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String password = cursor.getString(passwordIndex);
//                dbHelper.logDatabaseTable();
                textView.append("ID: " + id + ", Name: " + name + ", Password: " + password + "\n");
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        } else {
            // Handle case where the cursor is empty
            textView.setText("No data found.");
        }


    }
}