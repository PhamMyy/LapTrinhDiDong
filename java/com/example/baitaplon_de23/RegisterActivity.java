package com.example.baitaplon_de23;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button btnSignUpFree;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ nút "Đăng ký miễn phí"
        btnSignUpFree = findViewById(R.id.btnSignUpFree);

        // Thiết lập sự kiện khi nhấn nút "Đăng ký miễn phí"
        btnSignUpFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang RegisterActivity2
                Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });

        // Ánh xạ TextView "Đăng nhập"
        txtLogin = findViewById(R.id.txtLogin);

        // Thiết lập sự kiện khi nhấn vào "Đăng nhập"
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
