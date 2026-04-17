package com.example.alkewallet

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.data.local.Usuario
import com.example.alkewallet.data.local.UsuariosAdapter
import com.example.alkewallet.data.local.UsuariosAdapter.OnUsuarioClick
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.alkewallet.data.local.AppDatabase
import com.example.alkewallet.data.local.Transaccion
import com.example.alkewallet.data.network.RetrofitCliente
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class RequestMoney : AppCompatActivity() {
    var btnIngresarDinero: Button? = null
    var btnVolverAtras: ImageView? = null
    var txtMonto: TextView? = null
    var txtUsuarioSeleccionado: TextView? = null
    private val listaUsuarios = mutableListOf<Usuario>()
    var imgUsuarioSeleccionado: ImageView? = null
    var txtCorreoSeleccionado: TextView? = null
    var usuarioSeleccionado: Usuario? = null
    var layUsuarioSeleccionado: View? = null
    private var userId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_request_money_page)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })

        userId = intent.getStringExtra("userId") ?: ""
        println("USER ID RECIBIDO EN REQUEST : $userId")
        cargarUsuarios()

        txtMonto = findViewById<TextView>(R.id.txt_cantidad_a_transferir)
        btnIngresarDinero = findViewById<Button>(R.id.btn_ingresar_dinero_request_page)
        txtUsuarioSeleccionado = findViewById<TextView>(R.id.txt_usuario_sel)
        imgUsuarioSeleccionado = findViewById<ImageView?>(R.id.img_usuario_sel)
        txtCorreoSeleccionado = findViewById<TextView>(R.id.txt_correo_sel)
        layUsuarioSeleccionado = findViewById<View>(R.id.lay_usuario_seleccionado)

        layUsuarioSeleccionado!!.setOnClickListener(View.OnClickListener { v: View? ->
            mostrarBottomSheetUsuarios()
        })

        btnIngresarDinero!!.setOnClickListener {

            // RECIBIR LA INFORMACION DEL FORMULARIO EN EL ACTIVITY
            val sendMonto = txtMonto!!.getText().toString()
            if (sendMonto.isEmpty()) {
                return@setOnClickListener
            }

            // VALIDAR SI LA ES UN MONTO VALIDO
            val monto = try {
                sendMonto.toDouble()
            } catch (e: NumberFormatException) {
                txtMonto!!.error = "Ingrese una cantidad válida"
                return@setOnClickListener
            }

            // CONFIRMAR QUE EXISTA UN USUARIO SELECCIONADO
            if (usuarioSeleccionado == null) {
                txtUsuarioSeleccionado!!.error = "Selecciona un usuario"
            }

            // INICIO CORRUTINA
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // INICIAR VARIABLES TRANSACCION
                    val api = RetrofitCliente.transaccionApi
                    val userApi = RetrofitCliente.userApi
                    val db = AppDatabase.getDatabase(this@RequestMoney)

                    // OBTENER USUARIOS DE LA API PARA LA TRANSACCION
                    val origen = userApi.obtenerUsuario(usuarioSeleccionado!!.id)
                    val destino = userApi.obtenerUsuario(userId)

                    // VALIDAR SALDO DISPONIBLE PARA REALIZAR LA TRANSACCION
                    if (origen.saldo < monto) {
                        runOnUiThread {
                            txtMonto!!.error = "Saldo insuficiente"
                        }
                        return@launch
                    }

                    // CREAR NUEVOS ESTADOS DE LAS CUENTAS
                    val origenActualizado = origen.copy(saldo = origen.saldo - monto)
                    val destinoActualizado = destino.copy(saldo = destino.saldo + monto)
                    // ACTUALIZAR NUEVOS ESTADOS EN LA API
                    userApi.actualizarUsuario(origen.id, origenActualizado)
                    userApi.actualizarUsuario(destino.id, destinoActualizado)
                    //CREAR NUEVA TRANSACCION
                    val nueva = Transaccion(
                        id = UUID.randomUUID().toString(),
                        monto = monto,
                        fecha = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date()),
                        cuentaOrigen = usuarioSeleccionado!!.id,
                        cuentaDestino = userId,
                        descripcion = ""
                    )

                    // ENVÍO DE LOS DATOS A LA DB A TRAVÉS DE LA API
                    val response = api.crearTransaccion(nueva)
                    db.transaccionDao().insertar(response)
                    db.usuarioDao().insertar(origenActualizado)
                    db.usuarioDao().insertar(destinoActualizado)

                    runOnUiThread {
                        finish()
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        txtMonto!!.error = "Error al enviar"
                    }
                }
            }
        }

        // LOGICA BOTON PARA VOLVER A LA PESTAÑA ANTERIOR
        btnVolverAtras = findViewById<ImageView>(R.id.btn_volver_atras_request_page)
        btnVolverAtras!!.setOnClickListener(View.OnClickListener { view: View? ->
            finish()
        })
    }

    // FUNCION PARA CARGAR LOS USUARIOS DISPONIBLES DESDE LA API
        private fun cargarUsuarios(){
            CoroutineScope(Dispatchers.IO).launch {
                val dao = AppDatabase.getDatabase(this@RequestMoney).usuarioDao()
                val usuariosDb = dao.obtenerTodosSinConsultor(userId)

                runOnUiThread {
                    listaUsuarios.clear()
                    listaUsuarios.addAll(usuariosDb)
                }
            }
    }

    // FUNCION PARA MOSTRAR EL LAYOUT DE USUARIO DISPONIBLES
    private fun mostrarBottomSheetUsuarios() {
        val dialog = BottomSheetDialog(this)

        val view = getLayoutInflater().inflate(R.layout.item_bottomsheet_usuarios, null)

        dialog.setContentView(view)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_bottom_usuarios)
        recycler.setLayoutManager(LinearLayoutManager(this))
        val adapter = UsuariosAdapter(listaUsuarios, OnUsuarioClick { usuario: Usuario? ->
            usuarioSeleccionado = usuario
            txtUsuarioSeleccionado!!.setText(usuario!!.nombre)
            txtCorreoSeleccionado!!.setText(usuario.correo)
            val url = "https://i.pravatar.cc/150?img=${usuario.id}"
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imgUsuarioSeleccionado)
            dialog.dismiss()
        })
        recycler.setAdapter(adapter)
        dialog.show()
    }
}