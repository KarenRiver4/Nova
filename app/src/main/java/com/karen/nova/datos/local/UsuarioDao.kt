package com.karen.nova.datos.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Órdenes que se le pueden dar a la tabla "usuarios".
 */
@Dao
interface UsuarioDao {

    // Guarda un usuario nuevo y devuelve el id que le asignó Room.
    // Si el correo ya existe, falla (ABORT) en vez de reemplazarlo.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long

    // Busca un usuario por su correo. Devuelve null si no existe.
    // Lo usaremos en el Login y para evitar correos repetidos en el Registro.
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    // Busca un usuario por su id (útil para mostrar su nombre).
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): Usuario?
}