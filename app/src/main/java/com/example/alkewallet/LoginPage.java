package com.example.alkewallet;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginPage extends AppCompatActivity {

    Button btn_inicio_sesion_cuenta_login_page;
    TextView btn_no_tienes_cuenta_login_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_inicio_sesion_cuenta_login_page = findViewById(R.id.btn_inicio_sesion_login_page);
        btn_inicio_sesion_cuenta_login_page.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, HomePage.class);
            startActivity(intent);
        });

        btn_no_tienes_cuenta_login_page = findViewById(R.id.btn_no_tienes_cuenta_login_page);
        btn_no_tienes_cuenta_login_page.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, SignUpPage.class);
            startActivity(intent);
        });
    }
}