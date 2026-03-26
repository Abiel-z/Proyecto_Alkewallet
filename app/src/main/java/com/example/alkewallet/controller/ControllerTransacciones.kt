package com.example.alkewallet.controller

import android.widget.Toast
import com.example.alkewallet.model.ModelTransacciones.lista
import com.example.alkewallet.model.ModelTransacciones.saldo
import com.example.alkewallet.model.TipoTransaccion
import com.example.alkewallet.model.Transaccion
import java.util.Date

class ControllerTransacciones {

    fun realizarEnvio(destino: String, fecha: Date, monto: Double, imagen : Int ): Boolean {
        if (monto <= saldo) {
            val t = Transaccion(destino, fecha, monto, TipoTransaccion.ENVIO, imagen)
            lista.add(t)
            saldo -= monto
            return true
        } else {
            return false
        }
    }

    fun realizarDeposito(origen: String, fecha: Date, monto: Double, imagen: Int) {
        val t = Transaccion(origen, fecha, monto, TipoTransaccion.DEPOSITO, imagen)
        lista.add(t)
        saldo += monto
    }

}