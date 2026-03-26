package com.example.alkewallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.alkewallet.controller.ControllerTransacciones;
import com.example.alkewallet.model.ModelTransacciones;
import com.example.alkewallet.model.ModelUsuarios;
import com.example.alkewallet.model.Transaccion;
import com.example.alkewallet.model.TransaccionAdapter;
import com.example.alkewallet.model.Usuario;

public class HomePage extends AppCompatActivity {

    RecyclerView recyclerTransacciones;
    TransaccionAdapter adapterTransacciones;
    List<Transaccion> lista = new ArrayList<>();
    ModelTransacciones cuenta;
    LinearLayout layEmpty;
    Button btnEnviar;
    Button btnIngresar;
    ImageView btnPerfilHome;
    TextView txtSaldo;
    ControllerTransacciones controller;
    List<Usuario> listaUsuarios;
    int saldo;

    static boolean hayTransacciones = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ModelUsuarios.INSTANCE.inicializar();
        controller = new ControllerTransacciones();
        txtSaldo = findViewById(R.id.txt_saldo_home_page);
        cuenta = ModelTransacciones.INSTANCE;
        saldo = (int) cuenta.getSaldo();
        txtSaldo.setText("$" + saldo);
        listaUsuarios = ModelUsuarios.INSTANCE.getUsuarios();

        recyclerTransacciones = findViewById(R.id.recycler_transacciones_home_page);
        recyclerTransacciones.setLayoutManager(new LinearLayoutManager(this));

        adapterTransacciones = new TransaccionAdapter(cuenta.getLista());
        recyclerTransacciones.setAdapter(adapterTransacciones);

        layEmpty = findViewById(R.id.lay_transacciones_home_page_empty);

        btnEnviar = findViewById(R.id.btn_enviar_home_page);
        btnIngresar = findViewById(R.id.btn_ingreso_home_page);

        btnEnviar.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, SendMoney.class);
            launcher.launch(intent);
        });
        btnIngresar.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, RequestMoney.class);
            launcher.launch(intent);
        });

        btnPerfilHome = findViewById(R.id.btn_perfil_home_page);
        btnPerfilHome.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, ProfilePage.class);
            startActivity(intent);
        });

        }

        private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if (data != null){
                            Double monto = data.getDoubleExtra("monto", 0.0);
                            String tipo = data.getStringExtra("tipo");
                            String usuario = data.getStringExtra("usuario");

                            int fotoPerfil = data.getIntExtra("fotoPerfil", 0);

                            if ("ENVIO".equals(data.getStringExtra("tipo"))) {
                                if (monto > saldo) {
                                    Toast.makeText(this, "SALDO INSUFICIENTE", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                assert usuario != null;
                                controller.realizarEnvio(usuario, new Date(), monto, fotoPerfil);
                                adapterTransacciones.notifyDataSetChanged();
                                hayTransacciones = true;
                                saldo = (int) cuenta.getSaldo();
                                txtSaldo.setText("$" + saldo);

                            } else{
                                controller.realizarDeposito(usuario, new Date(), monto, fotoPerfil);
                                hayTransacciones = true;
                                saldo = (int) cuenta.getSaldo();
                                txtSaldo.setText("$" + saldo);
                            }
                            hayTransacciones = true;
                        }

                    }
                }
        );

        @Override
        protected void onResume() {
            super.onResume();
            if (hayTransacciones) {
                recyclerTransacciones.setVisibility(View.VISIBLE);
                layEmpty.setVisibility(View.GONE);
            } else {
                recyclerTransacciones.setVisibility(View.GONE);
                layEmpty.setVisibility(View.VISIBLE);
            }
        }
    }
