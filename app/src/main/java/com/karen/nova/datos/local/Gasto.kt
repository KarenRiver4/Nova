package com.karen.nova.datos.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa un gasto registrado por un usuario.
 */
@Entity(
    tableName = "gastos",
    // Cada gasto pertenece a un usuario.
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Hace más rápidas las búsquedas de "los gastos de este usuario"
    indices = [Index(value = ["usuarioId"])]
)
data class Gasto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // El id del usuario dueño de este gasto
    val usuarioId: Long,

    val concepto: String,

    // En pesos, sin centavos.
    val monto: Long,

    val categoria: String,

    // Fecha del gasto en milisegundos
    val fecha: Long,

    val metodoPago: String,

    // El signo ? significa que puede quedar vacío.
    val notas: String? = null,

    val fechaCreacion: Long = System.currentTimeMillis()
)