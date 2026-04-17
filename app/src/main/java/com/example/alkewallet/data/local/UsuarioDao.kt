package com.example.alkewallet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface UsuarioDao {

    // INSERTAR USUARIO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario);

    // INSERTAR LISTA USUARIOS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLista(usuarios: List<Usuario>)

    // OBTENER TODOS LOS USUARIOS
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos(): List<Usuario>;

    // OBTENER TODOS LOS USUARIOS MENOS EL QUE CONSULTA
    @Query("SELECT * FROM usuarios WHERE id != :userId")
    suspend fun obtenerTodosSinConsultor(userId: String): List<Usuario>;
}





