package com.example.alkewallet.data.network
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import com.example.alkewallet.data.local.Transaccion

interface TransaccionApiService {
    @GET("transacciones")
    suspend fun getTransacciones(): List<Transaccion>
    @POST("transacciones")
    suspend fun crearTransaccion(@Body transaccion: Transaccion): Transaccion
}




