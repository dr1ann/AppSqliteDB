package com.example.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.media.RouteListingPreference;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;



    private static final String CREATE_USERS_TABLE = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT)";
    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER,  name TEXT, quantity INTEGER)";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade here
    }

    public boolean isUsernameTaken(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the username is already in use
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            // Username is already taken
            Log.d("Database", "Username already taken");
            return true;
        } else {
            Log.d("Database", "Username bug");
            return false;
        }
    }
    public boolean insertData(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        long result = db.insert("users", null, contentValues);
        db.close(); // Close the database connection

        // Check if the insertion was successful
        return result != -1;
    }


    public Cursor readData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    public Boolean checkUsernamePassword(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where name = ? and password = ?", new String[] {name, password});

        if(cursor.getCount() > 0) {
            Log.d("Database", "User found");
            return true;
        } else {
            Log.d("Database", "User not found");
            return false;
        }

    }

    public int getUserId(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where name = ? and password = ?", new String[] {name, password});
        if (cursor.moveToFirst()) {
            int currentUserIdIndex = cursor.getColumnIndex("id");
            int currentUserId = cursor.getInt(currentUserIdIndex);
            cursor.close();
            return currentUserId;
        } else {
            cursor.close();
            return -1;
        }

    }

    public void getUserItems(TextView textview, int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE userID = ?", new String[]{String.valueOf(userID)});

        StringBuilder itemsText = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                int itemIdIndex = cursor.getColumnIndex("id");
                int itemNameIndex = cursor.getColumnIndex("name");
                int itemQuantityIndex = cursor.getColumnIndex("quantity");

                int itemId = cursor.getInt(itemIdIndex);
                String itemName = cursor.getString(itemNameIndex);
                int itemQuantity = cursor.getInt(itemQuantityIndex);

                // Append the item information to the StringBuilder
                itemsText.append("ID: ").append(itemId).append(", Name: ").append(itemName).append(", Quantity: ").append(itemQuantity).append("\n");

            } while (cursor.moveToNext());
        } else {
            // Handle case where the user has no items
            itemsText.append("User has no items");
        }

        cursor.close();

        // Set the text of the TextView with the concatenated item information
        textview.setText("Items:\n" + itemsText.toString());
    }






    public boolean addItemForUser(int userId, String itemName, int itemQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userID", userId);
        values.put("name", itemName);
        values.put("quantity", itemQuantity);

        long result = db.insert("items", null, values); // Assuming your table name is 'items'

        return result != -1; // Returns true if insert was successful, false otherwise
    }
    public void logDatabaseTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                // Iterate over each column in the current row
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    Log.d("Database", columnName + ": " + columnValue);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
    }


}

