package com.example.baitaplon_de23;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseThuan {

    private static final String DB_PATH_SUFFIX = "/databases/";
    private static final String DATABASE_NAME = "radio1.db";
    private SQLiteDatabase database;
    private final Context context;

    public DatabaseThuan(Context context) {
        this.context = context;
        processCopy(); // Sao chép CSDL nếu chưa có
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    private void processCopy() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset();
                Toast.makeText(context, "Database copied successfully!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    private void copyDataBaseFromAsset() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = getDatabasePath();

        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists()) f.mkdir();

        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public ArrayList<String> getMusicList3() {
        ArrayList<String> musicList = new ArrayList<>();
        Cursor c = database.query("ThuanPodcast", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String songTitle = c.getString(1); // Lấy tên bài hát
                musicList.add(songTitle);
            } while (c.moveToNext());
        }
        c.close();
        return musicList;
    }

    public ArrayList<String> getArtistList3() {
        ArrayList<String> artistList = new ArrayList<>();
        Cursor c = database.query("ThuanPodcast", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String artistName = c.getString(2); // Lấy tên nghệ sĩ
                artistList.add(artistName);
            } while (c.moveToNext());
        }
        c.close();
        return artistList;
    }
}
