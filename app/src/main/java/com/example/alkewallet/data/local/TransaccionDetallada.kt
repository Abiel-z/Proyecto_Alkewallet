package com.example.alkewallet.data.local


// TABLA PARA Query <obtenerTransaccionesDetalladas>
data class TransaccionDetallada(
    val transaccionId : String,
    val fecha : String,
    val monto : Double,
    val cuentaOrigenId : String,
    val descripcion : String? = null,
    val cuentaOrigenNombre : String?,
    val cuentaDestinoId : String,
    val cuentaDestinoNombre : String?,
)




