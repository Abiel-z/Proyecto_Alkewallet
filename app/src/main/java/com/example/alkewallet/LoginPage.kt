package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.alkewallet.data.local.AppDatabase
import com.example.alkewallet.data.network.RetrofitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginPage : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // ENCONTRAR BOTONES EN ACTIVITY
        btnLogin = findViewById(R.id.btn_inicio_sesion_login_page)
        btnSignUp = findViewById(R.id.btn_no_tienes_cuenta_login_page)
        txtEmail = findViewById(R.id.txt_email_input_login_page)
        txtPassword = findViewById(R.id.txt_contrasena_input_login_page)

        btnLogin.setOnClickListener {
            println("boton presionado")
            val correo = txtEmail.text.toString()
            val password = txtPassword.text.toString()

            if (correo.isEmpty() || password.isEmpty()) return@setOnClickListener
            // INICIO CORRUTINA PARA CONSULTA DE ACCESO A LA API
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // VARIABLES DE LA API PARA LA CONSULTA
                    val api = RetrofitCliente.userApi
                    val dao = AppDatabase.getDatabase(this@LoginPage).usuarioDao()
                    // CONSULTA DE USUARIOS POR CORREO
                    val usuarios = api.getUsuarioPorCorreo(correo)
                    // SI EL USUARIO EXISTE
                    if (usuarios.isNotEmpty()) {
                        // SELECCIONAR EL USUARIO ENCONTRADO
                        val usuario = usuarios[0]
                        // SI LA CONTRASEÑA COINCIDE CON LA DE "val usuario"
                        if (usuario.password == password) {
                            // GUARDAR USUARIO EN LA BASE DE DATOS INTERNA (ROOM)
                            dao.insertar(usuario)
                            // MOSTRAR MENSAJE DE ACCESO EXITOSO E INICIAR PANTALLA HOME
                            runOnUiThread {
                                Toast.makeText(this@LoginPage,"Inicio de sesión exitoso",Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginPage, HomePage::class.java)
                                intent.putExtra("userId",usuario.id)
                                println("ID DEL USUARIO AL SALIR INTENT: ${usuario.id}")
                                startActivity(intent)
                            }

                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginPage,"Contraseña incorrecta",Toast.LENGTH_SHORT).show()
                            }
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginPage,"Usuario no encontrado",Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginPage,"Error ${e.message}",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpPage::class.java))
        }
    }
}