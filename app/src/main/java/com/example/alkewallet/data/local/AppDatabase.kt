package com.example.alkewallet.data.local
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Usuario::class,Transaccion::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao() : UsuarioDao
    abstract fun transaccionDao() : TransaccionDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wallet_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}