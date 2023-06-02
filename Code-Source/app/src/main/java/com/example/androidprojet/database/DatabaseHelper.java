package com.example.androidprojet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.androidprojet.enums.StatusDataBiometric;
import com.example.androidprojet.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_TOKEN = "token";
    private static final String COLUMN_STATUS_DATA = "statusDataBiometric";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_LOGIN + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_TOKEN+ " TEXT,"
                + COLUMN_STATUS_DATA + " INTEGER"
                + ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(String login, String password, String role, String token, StatusDataBiometric statusDataBiometric) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_TOKEN,token);
        values.put(COLUMN_STATUS_DATA, statusDataBiometric.ordinal());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public User getUserByLogin(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{login});
        User user = null;
        if (cursor.moveToFirst()) {
            int idx_pass = cursor.getColumnIndex(COLUMN_PASSWORD);
            String password = cursor.getString(idx_pass);
            int idx_role = cursor.getColumnIndex(COLUMN_ROLE);
            String role = cursor.getString(idx_role);
            int idx_token = cursor.getColumnIndex(COLUMN_TOKEN);
            String token = cursor.getString(idx_token);
            int idx_status = cursor.getColumnIndex(COLUMN_STATUS_DATA);
            int statusOrdinal = cursor.getInt(idx_status);
            StatusDataBiometric statusDataBiometric = StatusDataBiometric.values()[statusOrdinal]; // Map the ordinal value back to the enum

            user = new User(login, password, role, token, statusDataBiometric);
        }

        cursor.close();
        db.close();
        return user;
    }

    public int getTotalUserCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int totalCount = 0;
        if (cursor.moveToFirst()) {
            totalCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return totalCount;
    }


    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int idx_login = cursor.getColumnIndex(COLUMN_LOGIN);
            String login = cursor.getString(idx_login);
            int idx_pass = cursor.getColumnIndex(COLUMN_PASSWORD);
            String password = cursor.getString(idx_pass);
            int idx_role = cursor.getColumnIndex(COLUMN_ROLE);
            String role = cursor.getString(idx_role);
            int idx_token = cursor.getColumnIndex(COLUMN_TOKEN);
            String token = cursor.getString(idx_token);
            int idx_status = cursor.getColumnIndex(COLUMN_STATUS_DATA);
            int statusOrdinal = cursor.getInt(idx_status);
            StatusDataBiometric statusDataBiometric = StatusDataBiometric.values()[statusOrdinal]; // Map the ordinal value back to the enum

            user = new User(login, password, role, token, statusDataBiometric);
        }
        cursor.close();
        db.close();
        return user;
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public void updateUserByStatus(String login, StatusDataBiometric statusDataBiometric) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS_DATA, statusDataBiometric.ordinal());
        db.update(TABLE_NAME, cv, COLUMN_LOGIN+"=?", new String[]{login});
        db.close();
    }

}