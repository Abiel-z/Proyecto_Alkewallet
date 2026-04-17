package com.example.alkewallet.data.local

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import java.text.SimpleDateFormat
import java.util.Locale
import com.squareup.picasso.Picasso

class TransaccionAdapter(private val lista: MutableList<TransaccionDetallada>, private val usedId: String) :
    RecyclerView.Adapter<TransaccionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val fecha: TextView = view.findViewById(R.id.txtFecha)
        val monto: TextView = view.findViewById(R.id.txtMonto)
        val imagen: ImageView = view.findViewById(R.id.imgUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaccion_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        // FORMATO DE FECHA RECIBIDA DESDE LA API
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val date = parser.parse(item.fecha)
        val formato = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        holder.fecha.text = formato.format(date!!)

        // VALIDACION PARA INGRESAR DATOS A LA VISTA
        if (item.cuentaDestinoId == usedId) {
            holder.monto.text = "+$${item.monto}"
            holder.nombre.text = item.cuentaOrigenNombre
        } else {
            holder.monto.text = "-$${item.monto}"
            holder.nombre.text = item.cuentaDestinoNombre
        }

        // VALIDACION DE QUE USUARIO DEBE MOSTRARSE
        val id = if (item.cuentaDestinoId == usedId) {
            item.cuentaOrigenId
        } else {
            item.cuentaDestinoId
        }

        // SE LE ASIGNA CON LA LIBRERIA PICASSO Y UNA IMAGEN DE UN REPOSITORIO GRATUITO PARA LA PRUEBA
        val url = "https://i.pravatar.cc/150?img=$id"
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imagen)
        }
    }