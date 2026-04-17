package com.example.alkewallet

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.alkewallet.data.local.AppDatabase
import com.example.alkewallet.data.network.RetrofitCliente
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePage : AppCompatActivity() {
    // INICIALIZAR VARIABLES
    private var userId : String = ""
    var fotoPerfil : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })

        userId = intent.getStringExtra("userId") ?: ""
        println("ID DEL USUARIO AL RECIBIR INTENT: $userId")
        iniciarUsuario()
        fotoPerfil = findViewById<ImageView>(R.id.img_fotoperfil_profile_page)
    }

    // FUNCION PARA CARGAR LOS DATOS DEL USUARIO
    private fun iniciarUsuario() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitCliente.userApi
                val dao = AppDatabase.getDatabase(this@ProfilePage).usuarioDao()

                runOnUiThread {
                    val url = "https://i.pravatar.cc/300?img=${userId}"
                    Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(fotoPerfil)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ProfilePage, "Error al obtener el usuario", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }
}