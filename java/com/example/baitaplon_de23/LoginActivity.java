package com.example.baitaplon_de23;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private ImageButton btnBack2;
    private EditText loginUsername, loginPassword;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        DatabaseUser dbHelper = new DatabaseUser(this);
        dbHelper.createDatabase();
        db = dbHelper.openDatabase();
        // Ánh xạ nút btn_back
        btnBack2 = findViewById(R.id.btn_back2);

        // Thiết lập sự kiện khi nhấn btn_back
        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc Activity hiện tại để quay lại RegisterActivity
                finish();
            }
        });
    }

    public void onLoginClick(View view) {
        String username = loginUsername.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND password = ?", new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng LoginActivity
        } else {
            Toast.makeText(this, "Tài khoản hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

//    public void goToRegisterActivity(View view) {
//        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//        startActivity(intent);
//    }

}
