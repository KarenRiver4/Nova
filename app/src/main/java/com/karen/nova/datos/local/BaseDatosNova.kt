package com.karen.nova.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * La base de datos de Nova.
 * Junta las tablas (Usuario y Gasto) con sus órdenes (los DAO).
 */
@Database(
    entities = [Usuario::class, Gasto::class],
    version = 1,
    exportSchema = false
)
abstract class BaseDatosNova : RoomDatabase() {

    // Acceso a las órdenes de cada tabla
    abstract fun usuarioDao(): UsuarioDao
    abstract fun gastoDao(): GastoDao

    companion object {
        // Aquí se guarda la única copia de la base de datos
        @Volatile
        private var INSTANCIA: BaseDatosNova? = null

        /**
         * Devuelve la base de datos. Si no existe, la crea.
         * Así toda la app comparte UNA sola base de datos.
         */
        fun obtenerInstancia(context: Context): BaseDatosNova {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosNova::class.java,
                    "nova_base_datos"
                ).build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}