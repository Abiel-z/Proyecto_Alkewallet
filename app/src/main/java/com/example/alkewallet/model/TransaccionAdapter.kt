package com.example.alkewallet.model

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ImageView


class TransaccionAdapter(private val lista: List<Transaccion>) :
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

        holder.nombre.text = item.nombre

        val formato = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        holder.fecha.text = formato.format(item.fecha)

        if (item.tipo == TipoTransaccion.DEPOSITO) {
            holder.monto.text = "+ $${item.monto}"
        } else {
            holder.monto.text = "- $${item.monto}"
        }
        holder.imagen.setImageResource(item.fotoPerfil)
    }
}