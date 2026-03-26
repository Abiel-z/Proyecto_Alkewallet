package com.example.alkewallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginSignupPage extends AppCompatActivity {


    Button btn_crear_cuenta_login_signup_page;
    TextView btn_tienes_cuenta_login_signup_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_singup_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_crear_cuenta_login_signup_page = findViewById(R.id.btn_crear_cuenta_login_signup_page);
        btn_crear_cuenta_login_signup_page.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpPage.class);
            startActivity(intent);
        });

        btn_tienes_cuenta_login_signup_page = findViewById(R.id.btn_tienes_cuenta_login_signup_page);
        btn_tienes_cuenta_login_signup_page.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
        });

    }
    }