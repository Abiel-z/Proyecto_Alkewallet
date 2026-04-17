package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.alkewallet.data.local.AppDatabase
import com.example.alkewallet.data.network.RetrofitCliente
import com.example.alkewallet.data.local.Usuario
import java.util.*
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText


class SignUpPage : AppCompatActivity() {
    var btn_crear_cuenta_signup_page: Button? = null
    var btn_tienes_cuenta_signup_page: TextView? = null

    var txtNombre: TextInputEditText? = null
    var txtApellido: TextInputEditText? = null
    var txtEmail: TextInputEditText? = null
    var txtContraseña: TextInputEditText? = null
    var txtConfirmarContraseña: TextInputEditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })

        txtNombre = findViewById<TextInputEditText>(R.id.txt_ingreso_nombre_signup_page)
        txtApellido = findViewById<TextInputEditText>(R.id.txt_ingreso_apellido_signup_page)
        txtEmail = findViewById<TextInputEditText>(R.id.txt_ingreso_email_signup_page)
        txtContraseña = findViewById<TextInputEditText>(R.id.txt_ingreso_contrasena_signup_page)
        txtConfirmarContraseña = findViewById<TextInputEditText>(R.id.txt_ingreso_confirmar_contrasena_signup_page)
        btn_crear_cuenta_signup_page = findViewById<Button>(R.id.btn_crear_cuenta_signup_page)
        btn_tienes_cuenta_signup_page = findViewById<TextView>(R.id.btn_tienes_cuenta_signup_page)

        // LOGICA AL PRESIONAR EL BOTÓN CREAR CUENTA
        btn_crear_cuenta_signup_page!!.setOnClickListener {
            // ASIGNAR LOS LOS TEXTOS EN EL ACTIVITY A VARIABLES
            var nombre = txtNombre!!.text.toString() + " " + txtApellido!!.text.toString()
            var correo = txtEmail!!.text.toString()
            var password = txtContraseña!!.text.toString()
            var confirmarPassword = txtConfirmarContraseña!!.text.toString()


            // VALIDAR VACÍOS Y ERRORES
            if (nombre.trim().isEmpty()) {
                txtNombre?.error = "Ingrese Nombre"
                return@setOnClickListener
            }
            if (correo.isEmpty()) {
                txtEmail?.error = "Ingrese Correo"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                txtEmail?.error = "Formato de correo inválido"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                txtContraseña?.error = "Ingrese Contraseña"
                return@setOnClickListener
            }
            if (password != confirmarPassword) {
                txtConfirmarContraseña?.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val api = RetrofitCliente.userApi
                    val dao = AppDatabase.getDatabase(this@SignUpPage).usuarioDao()
                    // VALIDAR CORREO DUPLICADO
                    val usuarios = api.getUsuarios()
                    val existentes = usuarios.filter { it.correo == correo }

                    if (existentes.isNotEmpty()) {
                        runOnUiThread {
                            txtEmail?.error = "Correo ya registrado"
                        }
                        return@launch
                    }
                    println("NUEVO USUARIO : $nombre")
                    val nuevoUsuario = Usuario(
                        id = UUID.randomUUID().toString(),
                        nombre = nombre,
                        correo = correo,
                        saldo = 0.0,
                        password = password,
                        fotoPerfil = ""
                    )

                    val response = api.crearUsuario(nuevoUsuario)
                    dao.insertar(response)

                    runOnUiThread {
                        Toast.makeText(
                            this@SignUpPage,
                            "Usuario creado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        irALogin()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("ERROR : ${e.message}")
                    runOnUiThread {
                        Toast.makeText(
                            this@SignUpPage,
                            "ERROR : ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        btn_tienes_cuenta_signup_page!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        })
    }


    private fun irALogin() {
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
    }
}

