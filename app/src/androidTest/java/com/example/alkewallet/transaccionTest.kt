package com.example.alkewallet

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.alkewallet.data.local.AppDatabase
import com.example.alkewallet.data.local.Transaccion
import com.example.alkewallet.data.local.TransaccionDao
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import com.example.alkewallet.data.local.Usuario
import com.example.alkewallet.data.local.UsuarioDao


@RunWith(AndroidJUnit4::class)
class TransaccionTest {

    private lateinit var db: AppDatabase
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var transaccionDao: TransaccionDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        usuarioDao = db.usuarioDao()
        transaccionDao = db.transaccionDao()
    }

    @After
    fun cerrarDB() {
        db.close()
    }

    @Test
    fun insertarYObtenerTransacciones() = runBlocking {

        val transaccion = Transaccion(
            id = "1",
            monto = 1000.0,
            fecha = "2025-05-12T10:44:45.522Z",
            cuentaOrigen = "1",
            cuentaDestino = "2",
            descripcion = "Pago"
        )

        transaccionDao.insertar(transaccion)

        val resultado = transaccionDao.obtenerTodas()

        assertEquals(1, resultado.size)
        assertEquals(1000.0, resultado[0].monto, 0.0)
    }

    @Test
    fun obtenerTransaccionesPorUsuario_filtraCorrectamente() = runBlocking {
        val t1 = Transaccion("1", 1000.0, "2025-05-12T10:44:45.522Z", "1", "2", "Pago")
        val t2 = Transaccion("2", 2000.0, "2025-05-12T10:44:45.522Z", "3", "1", "Transferencia")
        val t3 = Transaccion("3", 3000.0, "2025-05-12T10:44:45.522Z", "4", "5", "Otro")
        transaccionDao.insertar(t1)
        transaccionDao.insertar(t2)
        transaccionDao.insertar(t3)

        val resultado = transaccionDao.obtenerPorUsuario("1")
        assertEquals(2, resultado.size)
    }

    @Test
    fun obtenerTransaccionesDetalladas_devuelveDatosConNombres() = runBlocking {
        val usuario1 = Usuario("1", 1000.0, nombre = "Juan" , correo = "juan@test.com", password = "1234")
        val usuario2 = Usuario("2", 2000.0, nombre = "Pedro" , correo = "pedro@test.com" , password = "4321")
        usuarioDao.insertar(usuario1)
        usuarioDao.insertar(usuario2)
        val transaccion = Transaccion(
            id = "1",
            monto = 1500.0,
            fecha = "2025-05-12T10:44:45.522Z",
            cuentaOrigen = "1",
            cuentaDestino = "2",
            descripcion = "Pago"
        )
        transaccionDao.insertar(transaccion)
        val resultado = transaccionDao.obtenerTransaccionesDetalladas("1")
        assertEquals(1, resultado.size)
        assertEquals("Juan", resultado[0].cuentaOrigenNombre)
        assertEquals("Pedro", resultado[0].cuentaDestinoNombre)
    }

}




