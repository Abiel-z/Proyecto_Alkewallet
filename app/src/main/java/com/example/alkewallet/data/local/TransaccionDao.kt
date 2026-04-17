package com.example.alkewallet.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface TransaccionDao {

    // Query PARA INSERTAR TRANSFERENCIAS EN LA LISTA
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(transaccion: Transaccion)

    // Query PARA OBTENER TODAS LAS TRANSFERENCIAS
    @Query("SELECT * FROM transacciones")
    suspend fun obtenerTodas() : List<Transaccion>

    // Query PARA OBTENER LAS TRANSFERENCIAS DE UN USUARIO SIN NOMBRES
    @Query("""SELECT * FROM transacciones WHERE cuentaOrigen = :userId OR cuentaDestino = :userId""")
    suspend fun obtenerPorUsuario(userId : String) : List<Transaccion>

    // Query PARA ARMAR TABLA DE TRANSFERENCIAS DETALLADAS POR USUARIO
    @Query("""
        SELECT
            t.id AS transaccionId,
            t.monto,
            t.fecha,
            t.cuentaOrigen AS cuentaOrigenId,
            t.descripcion,
            uo.nombre AS cuentaOrigenNombre,
            t.cuentaDestino AS cuentaDestinoId,
            ud.nombre AS cuentaDestinoNombre
        FROM transacciones t
        LEFT JOIN usuarios uo ON t.cuentaOrigen = uo.id
        LEFT JOIN usuarios ud ON t.cuentaDestino = ud.id
        WHERE t.cuentaOrigen = :userId OR t.cuentaDestino = :userId
        ORDER BY t.fecha DESC
        """)
    suspend fun obtenerTransaccionesDetalladas(userId : String) : List<TransaccionDetallada>

}




