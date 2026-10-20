package com.karen.nova.ui.autenticacion

/** Mensajes de error de cada campo del inicio de sesión. */
data class ErroresInicioSesion(
    val correo: String? = null,
    val contrasena: String? = null
) {
    fun hayErrores(): Boolean = correo != null || contrasena != null
}

/** Las situaciones en las que puede estar la pantalla de inicio de sesión. */
sealed class EstadoInicioSesion {
    data object Inicial : EstadoInicioSesion()
    data object Cargando : EstadoInicioSesion()
    data class ErroresValidacion(val errores: ErroresInicioSesion) : EstadoInicioSesion()
    data object CredencialesIncorrectas : EstadoInicioSesion()
    data object Exitoso : EstadoInicioSesion()
}