package com.example.alkewallet.model

import java.util.Date

enum class TipoTransaccion {
    ENVIO,
    DEPOSITO
}

data class Transaccion(
    val nombre: String,
    val fecha: Date,
    val monto: Double,
    val tipo: TipoTransaccion,
    val fotoPerfil: Int
)
