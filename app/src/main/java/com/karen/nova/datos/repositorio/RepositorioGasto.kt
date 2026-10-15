package com.karen.nova.datos.repositorio

import androidx.lifecycle.LiveData
import com.karen.nova.datos.local.Gasto
import com.karen.nova.datos.local.GastoDao

/**
 * Intermediario entre las pantallas y la tabla de gastos.
 * Aquí pasan todas las operaciones del CRUD.
 */
class RepositorioGasto(private val gastoDao: GastoDao) {

    /** Lista "viva" de los gastos del usuario. Se actualiza sola. */
    fun obtenerGastosDeUsuario(usuarioId: Long): LiveData<List<Gasto>> =
        gastoDao.obtenerGastosDeUsuario(usuarioId)

    /** Un gasto específico, para el Detalle y el formulario de Editar. */
    fun obtenerGastoPorId(id: Long): LiveData<Gasto?> =
        gastoDao.obtenerGastoPorId(id)

    /** Crea un gasto nuevo y devuelve su id. */
    suspend fun insertar(gasto: Gasto): Long = gastoDao.insertar(gasto)

    /** Guarda los cambios de un gasto existente. */
    suspend fun actualizar(gasto: Gasto) = gastoDao.actualizar(gasto)

    /** Elimina un gasto. */
    suspend fun eliminar(gasto: Gasto) = gastoDao.eliminar(gasto)
}