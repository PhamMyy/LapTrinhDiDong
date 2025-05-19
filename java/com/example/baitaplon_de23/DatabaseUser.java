package com.example.baitaplon_de23;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseUser extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserDatabase.db";
    private static final int DB_VERSION = 1;
    private String DB_PATH;
    private final Context context;
    private SQLiteDatabase database;

    public DatabaseUser(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }

    public void createDatabase() {
        if (!checkDatabase()) {
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Database does not exist yet.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDatabase() {
        try {
            InputStream input = context.getAssets().open(DB_NAME);
            OutputStream output = new FileOutputStream(DB_PATH);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error copying database: " + e.getMessage());
        }
    }

    public SQLiteDatabase openDatabase() {
        database = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}