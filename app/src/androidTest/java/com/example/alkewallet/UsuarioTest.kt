package com.example.alkewallet

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.alkewallet.data.local.AppDatabase
import com.example.alkewallet.data.local.Usuario
import com.example.alkewallet.data.local.UsuarioDao
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsuarioTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: UsuarioDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.usuarioDao()
    }

    @After
    fun cerrarDB() {
        db.close()
    }

    @Test
    fun insertarYObtenerListaUsuarios() = runBlocking {

        val usuarios = listOf(
            Usuario(id = "1", nombre = "Juan", password = "1234", correo = "juan@test.com", saldo = 100.0),
            Usuario(id = "2", nombre = "Pedro", password = "4321", correo = "pedro@test.com", saldo = 200.0)
        )

        // Insertar lista
        dao.insertarLista(usuarios)

        // Obtener lista
        val resultado = dao.obtenerTodos()

        // Verificaciones
        assertEquals(2, resultado.size)
        assertEquals("Juan", resultado[0].nombre)
        assertEquals("Pedro", resultado[1].nombre)
    }

    @Test
    fun obtenerUsuariosSinConsultor_funcionaCorrectamente() = runBlocking {

        val usuarios = listOf(
            Usuario(id = "1", nombre = "Juan", password = "1234", correo = "juan@test.com", saldo = 100.0),
            Usuario(id = "2", nombre = "Pedro", password = "4321", correo = "pedro@test.com", saldo = 200.0)
        )

        dao.insertarLista(usuarios)

        val resultado = dao.obtenerTodosSinConsultor("1")

        assertEquals(1, resultado.size)
        assertEquals("Pedro", resultado[0].nombre)
    }

    @Test
    fun insertarUsuario_reemplazaSiExiste() = runBlocking {

        val usuario1 = Usuario(id = "1", nombre = "Juan", correo = "juan@test.com", saldo = 100.0, password = "1234")
        val usuarioActualizado = Usuario(id = "1", nombre = "Juan Actualizado", correo = "nuevo@test.com", saldo = 200.0, password = "4321")

        dao.insertar(usuario1)
        dao.insertar(usuarioActualizado)

        val resultado = dao.obtenerTodos()

        assertEquals(1, resultado.size)
        assertEquals("Juan Actualizado", resultado[0].nombre)
    }
}