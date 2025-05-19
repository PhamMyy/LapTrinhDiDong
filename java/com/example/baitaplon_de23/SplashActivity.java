package com.example.baitaplon_de23;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Thực hiện một hành động nào đó, ví dụ chờ một vài giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra điều kiện đăng nhập
                Intent intent;
                if (isUserLoggedIn()) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, RegisterActivity.class);
                }
                startActivity(intent);
                finish(); // Đóng SplashActivity
            }
        }, 2000); // Delay trong 2 giây
    }

    // Kiểm tra trạng thái đăng nhập
    private boolean isUserLoggedIn() {
        // Trả về true nếu người dùng đã đăng nhập, false nếu chưa
        // Ở đây bạn cần thay thế bằng logic thực tế của ứng dụng
        return false; // Ví dụ mặc định là người dùng chưa đăng nhập
    }
}
