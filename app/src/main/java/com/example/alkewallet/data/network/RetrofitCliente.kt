package com.example.alkewallet.data.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCliente {
    private const val BASE_URL = "https://69dd6a5184f912a264050c96.mockapi.io/alkewallet/"


    val userApi: UsuarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApiService::class.java)
    }

    val transaccionApi: TransaccionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransaccionApiService::class.java)
    }
}



