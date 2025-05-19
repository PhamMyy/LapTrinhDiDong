package com.example.baitaplon_de23;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity2 extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText registerUsername, registerPassword;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);

        DatabaseUser dbHelper = new DatabaseUser(this);
        dbHelper.createDatabase();
        db = dbHelper.openDatabase();

        // Ánh xạ nút btn_back
        btnBack = findViewById(R.id.btn_back);

        // Thiết lập sự kiện khi nhấn btn_back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc Activity hiện tại để quay lại RegisterActivity
                finish();
            }
        });
    }

    public void onRegisterClick(View view) {
        String username = registerUsername.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên người dùng và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            long result = db.insert("Users", null, values);

            if (result != -1) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity và quay lại LoginActivity
            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}
