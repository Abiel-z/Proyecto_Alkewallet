package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.data.local.AppDatabase
import com.example.alkewallet.data.local.TransaccionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.alkewallet.data.network.RetrofitCliente
import com.squareup.picasso.Picasso

import java.util.Date

class HomePage : AppCompatActivity() {
    var recyclerTransacciones: RecyclerView? = null
    var layEmpty: LinearLayout? = null
    var btnEnviar: Button? = null
    var btnIngresar: Button? = null
    var btnPerfilHome: ImageView? = null
    var txtSaldo: TextView? = null
    private lateinit var adapter: TransaccionAdapter
    private val lista = mutableListOf<com.example.alkewallet.data.local.TransaccionDetallada>()
    private var userId : String = ""
    var txtNombreUsuario : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })


        userId = intent.getStringExtra("userId") ?: ""
        println("ID DEL USUARIO AL RECIBIR INTENT: $userId")

        btnPerfilHome = findViewById<ImageView>(R.id.btn_perfil_home_page)
        txtSaldo = findViewById<TextView>(R.id.txt_saldo_home_page)
        txtNombreUsuario = findViewById<TextView>(R.id.txt_bienvenido_saldo_home_page)
        recyclerTransacciones = findViewById<RecyclerView>(R.id.recycler_transacciones_home_page)
        recyclerTransacciones!!.setLayoutManager(LinearLayoutManager(this))
        iniciarUsuario()

        // ENCONTRAR LAYOUT EN ACTVITY
        layEmpty = findViewById<LinearLayout>(R.id.lay_transacciones_home_page_empty)
        adapter = TransaccionAdapter(lista, userId)
        recyclerTransacciones!!.adapter = adapter

        cargarTransacciones()
        actualizarSaldo()

        println("LISTA DE TRANSACCIONES : $lista")

        // ENCONTRAR LOS BOTONES EN ACTIVITY
        btnEnviar = findViewById<Button>(R.id.btn_enviar_home_page)
        btnIngresar = findViewById<Button>(R.id.btn_ingreso_home_page)

        // CONTROL BOTONES
        // ACCESO A ENVIAR DINERO <Send Money - activity_send_money_page>
        btnEnviar!!.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@HomePage, SendMoney::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        })
        // ACCESO A DEPOSITAR DINERO <RequestMoney - activity_request_money_page>
        btnIngresar!!.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@HomePage, RequestMoney::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        })

        // ACCESO A CONFIGURACION DEL PERFIL <ProfilePage - activity_profile_page>
        btnPerfilHome = findViewById<ImageView>(R.id.btn_perfil_home_page)
        btnPerfilHome!!.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@HomePage, ProfilePage::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        })
        // FIN CONTROL BOTONES

    }

    override fun onResume() {
        super.onResume()
        cargarTransacciones()
        actualizarSaldo()
    }

    private fun actualizarSaldo() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiuser = RetrofitCliente.userApi
                val usuario = apiuser.obtenerUsuario(userId)

                runOnUiThread {
                    txtSaldo!!.text = "$${usuario.saldo}"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@HomePage, "Error al obtener el saldo", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    // FUNCION PARA CARGAR LOS DATOS DEL USUARIO
    private fun iniciarUsuario() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitCliente.userApi
                val dao = AppDatabase.getDatabase(this@HomePage).usuarioDao()
                val usuario = api.obtenerUsuario(userId)
                runOnUiThread {
                    txtNombreUsuario!!.text = getString(R.string.homepage_bienvenido, usuario.nombre)
                    val url = "https://i.pravatar.cc/300?img=${userId}"
                    Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(btnPerfilHome)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@HomePage, "Error al obtener el usuario", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }



    // FUNCION PARA CARGAR LAS TRANSACCIONES EXISTENTES
    private fun cargarTransacciones() {
        CoroutineScope(Dispatchers.IO).launch {
            // CREAR VARIABLES PARA LA API (RETROFIT) Y BASE DE DATOS (ROOM)
            val db = AppDatabase.getDatabase(this@HomePage)
            val transaccionApi = RetrofitCliente.transaccionApi
            val usuarioApi = RetrofitCliente.userApi

            // LLAMADA A LA API Y GUARDADO EN BASE DE DATOS
            val transaccionDao = db.transaccionDao()
            val usuarioDao = db.usuarioDao()

            // TRAER USUARIOS
            val usuariosApi = usuarioApi.getUsuarios()

            // GUARDAR USUARIOS
            usuarioDao.insertarLista(usuariosApi)
            val usuarios = usuarioDao.obtenerTodos()
            println("USUARIOS : $usuarios")

            // GUARDAR TRANSACCIONES
            val transaccionesApi = transaccionApi.getTransacciones()

            for (t in transaccionesApi){
                transaccionDao.insertar(t)
            }


            // CREACION DE VARIABLE PARA OBTENER LAS TRANSACCIONES DEL USUARIO
            val transacciones = transaccionDao.obtenerTransaccionesDetalladas(userId)
            println("TRANSACCIONES DEL USUARIO : $transacciones")

            // GUARDADO DE DATOS EN LA LISTA
            runOnUiThread {
                lista.clear()
                lista.addAll(transacciones)
                adapter.notifyDataSetChanged()

                // CONTROL DE APARICION LAYERS
                if (lista.isEmpty()) {
                    layEmpty!!.visibility = View.VISIBLE
                    recyclerTransacciones!!.visibility = View.GONE
                } else {
                    layEmpty!!.visibility = View.GONE
                    recyclerTransacciones!!.visibility = View.VISIBLE
                }
            }
        }
    }
}
