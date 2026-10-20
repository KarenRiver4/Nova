package com.karen.nova.ui.autenticacion

/** Mensajes de error de cada campo. null significa "este campo está bien". */
data class ErroresRegistro(
    val nombre: String? = null,
    val correo: String? = null,
    val contrasena: String? = null,
    val confirmacion: String? = null
) {
    /** Responde si al menos un campo tiene error. */
    fun hayErrores(): Boolean =
        listOf(nombre, correo, contrasena, confirmacion).any { it != null }
}

/** Las situaciones en las que puede estar la pantalla de registro. */
sealed class EstadoRegistro {
    data object Inicial : EstadoRegistro()
    data object Cargando : EstadoRegistro()
    data class ErroresValidacion(val errores: ErroresRegistro) : EstadoRegistro()
    data object Exitoso : EstadoRegistro()
}