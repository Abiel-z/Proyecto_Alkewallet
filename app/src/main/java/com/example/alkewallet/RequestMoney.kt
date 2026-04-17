package com.example.alkewallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alkewallet.model.ModelUsuarios;
import com.example.alkewallet.model.Usuario;
import com.example.alkewallet.model.UsuariosAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class RequestMoney extends AppCompatActivity {

    Button btnIngresarDinero;
    ImageView btnVolverAtras;
    TextView txtMonto;
    TextView txtUsuarioSeleccionado;
    List<Usuario> listaUsuarios;
    ImageView imgUsuarioSeleccionado;
    TextView txtCorreoSeleccionado;
    Usuario usuarioSeleccionado;
    View layUsuarioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_money_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtMonto = findViewById(R.id.txt_cantidad_a_transferir);
        btnIngresarDinero = findViewById(R.id.btn_ingresar_dinero_request_page);
        txtUsuarioSeleccionado = findViewById(R.id.txt_usuario_sel);
        imgUsuarioSeleccionado = findViewById(R.id.img_usuario_sel);
        txtCorreoSeleccionado = findViewById(R.id.txt_correo_sel);
        layUsuarioSeleccionado = findViewById(R.id.lay_usuario_seleccionado);

        listaUsuarios = ModelUsuarios.INSTANCE.getUsuarios();

        layUsuarioSeleccionado.setOnClickListener(v -> {
            mostrarBottomSheetUsuarios();
        });

        btnIngresarDinero.setOnClickListener(view ->{
            String sendMonto = txtMonto.getText().toString();

            if (sendMonto.isEmpty()) {
                return;
            }
            double monto;
            try {
                monto = Double.parseDouble(sendMonto);
            } catch (NumberFormatException e) {
                txtMonto.setError("Ingrese una cantidad válida");
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("monto", monto);
            resultIntent.putExtra("tipo", "DEPOSITO");

            if (usuarioSeleccionado == null){
                txtUsuarioSeleccionado.setError("Selecciona un usuario");
            }

            resultIntent.putExtra("usuario", usuarioSeleccionado.getNombre());
            resultIntent.putExtra("fotoPerfil", usuarioSeleccionado.getFotoPerfil());

            System.out.println("Transferencia realizada con éxito. Monto: " + monto + " DEPOSITO");
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnVolverAtras = findViewById(R.id.btn_volver_atras_request_page);
        btnVolverAtras.setOnClickListener(view ->{
            finish();
        });

    }
    private void mostrarBottomSheetUsuarios(){
        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(R.layout.item_bottomsheet_usuarios, null);

        dialog.setContentView(view);
        RecyclerView recycler = view.findViewById(R.id.recycler_bottom_usuarios);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        UsuariosAdapter adapter = new UsuariosAdapter(listaUsuarios, usuario -> {
            usuarioSeleccionado = usuario;
            txtUsuarioSeleccionado.setText(usuario.getNombre());
            imgUsuarioSeleccionado.setImageResource(usuario.getFotoPerfil());
            txtCorreoSeleccionado.setText(usuario.getCorreo());
            dialog.dismiss();
        });
        recycler.setAdapter(adapter);
        dialog.show();

    }
}