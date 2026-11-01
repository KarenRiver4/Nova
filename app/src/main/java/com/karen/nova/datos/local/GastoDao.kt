package com.karen.nova.datos.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Órdenes que se le pueden dar a la tabla "gastos".
 * Aquí está todo el CRUD: crear, leer, actualizar y eliminar.
 */
@Dao
interface GastoDao {

    // CREAR: guarda un gasto nuevo y devuelve su id
    @Insert
    suspend fun insertar(gasto: Gasto): Long

    // ACTUALIZAR: guarda los cambios de un gasto que ya existe
    @Update
    suspend fun actualizar(gasto: Gasto)

    // ELIMINAR: borra un gasto
    @Delete
    suspend fun eliminar(gasto: Gasto)

    // LEER (lista): todos los gastos de un usuario, del más reciente al más antiguo.
    // Como devuelve LiveData, la lista se actualiza SOLA cuando algo cambia.
    @Query("SELECT * FROM gastos WHERE usuarioId = :usuarioId ORDER BY fecha DESC, id DESC")
    fun obtenerGastosDeUsuario(usuarioId: Long): LiveData<List<Gasto>>

    // LEER (uno): un gasto específico, para la pantalla de Detalle y para Editar
    @Query("SELECT * FROM gastos WHERE id = :id LIMIT 1")
    fun obtenerGastoPorId(id: Long): LiveData<Gasto?>

    // Busca un gasto una sola vez (no "vivo"), para llenar el formulario al editar
    @Query("SELECT * FROM gastos WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): Gasto?
}