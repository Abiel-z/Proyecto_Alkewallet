package com.example.alkewallet.data.network
import com.example.alkewallet.data.local.Usuario
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApiService {
    @GET("usuarios")
    suspend fun getUsuarios(): List<Usuario>
    @GET("usuarios")
    suspend fun getUsuarioPorCorreo(
        @Query("correo") correo: String
    ): List<Usuario>
    @GET("usuarios/{id}")
    suspend fun obtenerUsuario(@Path("id") id : String): Usuario
    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: String,
        @Body usuario: Usuario
    ): Usuario
    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Usuario
}



