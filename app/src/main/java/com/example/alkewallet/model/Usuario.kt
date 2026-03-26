package com.example.alkewallet.model

data class Usuario(
    val id : Int,
    val nombre: String,
    val correo: String,
    val saldo: Double,
    val fotoPerfil: Int
)