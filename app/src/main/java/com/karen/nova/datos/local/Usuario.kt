package com.karen.nova.datos.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa a una persona registrada en Nova.
 */
@Entity(
    tableName = "usuarios",
    // El correo no se puede repetir.
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(
    // Número único de cada usuario.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,
    val correo: String,
    // Guarda el hash SHA-256 de la contraseña, nunca el texto original
    val contrasena: String,

    // Fecha de registro, guardada como milisegundos.
    val fechaCreacion: Long = System.currentTimeMillis()
)