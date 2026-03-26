package com.example.alkewallet.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R


class UsuariosAdapter(
private val lista: List<Usuario>,
private val listener: OnUsuarioClick
) : RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {

    fun interface OnUsuarioClick {
        fun onClick(usuario: Usuario)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val correo: TextView = view.findViewById(R.id.txtCorreo)
        val imagen: ImageView = view.findViewById(R.id.imgUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuarios_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = lista[position]

        holder.nombre.text = usuario.nombre
        holder.correo.text = usuario.correo
        holder.imagen.setImageResource(usuario.fotoPerfil)

        holder.itemView.setOnClickListener {
            listener.onClick(usuario)

        }
    }

}