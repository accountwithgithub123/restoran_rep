package com.example.restoran;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "OrdersDB";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_NAME = "OrderTable";
    private static final String COLUMN_ORDER_NO = "orderNo";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_nGuest = "noguest";
    private static final String COLUMN_Request = "request";

    // SQL query to create the Order table
    private static final String CREATE_TABLE_ORDER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            COLUMN_ORDER_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_TIME + " TEXT, " +
            COLUMN_nGuest + " TEXT, " +
            COLUMN_Request + " TEXT)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the Order table
        db.execSQL(CREATE_TABLE_ORDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert values into the Order table
    public void insertData(String name, String email, String date, String time, String noOfGuests, String request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Insert values into the ContentValues object
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_nGuest, noOfGuests);
        values.put(COLUMN_Request, request);

        // Insert the values into the Order table
        db.insert(TABLE_NAME, null, values);

        // Close the database connection
//        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<Orders> getAllData() {
        ArrayList<Orders> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select all rows from the Order table
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add them to the list
        if (cursor.moveToFirst()) {
            do {
                Orders order = new Orders();
                order.setOrderNo(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_NO)));
                order.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                order.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                order.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                order.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                order.setNumberOfGuests(cursor.getString(cursor.getColumnIndex(COLUMN_nGuest)));
                order.setRequest(cursor.getString(cursor.getColumnIndex(COLUMN_Request)));

                // Add order to the list
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
//        db.close();

        return orderList;
    }
}
