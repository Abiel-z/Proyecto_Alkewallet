package com.example.alkewallet.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class Transaccion(
    @PrimaryKey
    val id : String,

    val monto : Double,
    val fecha : String,
    val cuentaOrigen : String,
    val cuentaDestino : String,
    val descripcion: String? = null
)






