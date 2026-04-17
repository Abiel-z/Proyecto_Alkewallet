package com.example.alkewallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario (
    @PrimaryKey
    val id : String,

    val saldo : Double,
    val nombre : String,
    val password : String,
    val correo : String,
    val fotoPerfil : String? = null
)




