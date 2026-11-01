package com.karen.nova.utilidades

import android.content.Context
import androidx.core.content.edit

/**
 * Recuerda qué usuario inició sesión, incluso si se cierra la app.
 * Guarda solo el id del usuario en las SharedPreferences del celular.
 */
class GestorSesion(context: Context) {

    // La "libretica" privada de la app, llamada "sesion_nova"
    private val preferencias = context.applicationContext
        .getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE)

    /** Guarda el id del usuario que acaba de iniciar sesión. */
    fun guardarSesion(usuarioId: Long) {
        preferencias.edit { putLong(CLAVE_USUARIO_ID, usuarioId) }
    }

    /** Devuelve el id del usuario con sesión activa, o SIN_SESION si no hay nadie. */
    fun obtenerUsuarioId(): Long =
        preferencias.getLong(CLAVE_USUARIO_ID, SIN_SESION)

    /** Responde si hay alguien con la sesión iniciada. */
    fun haySesionActiva(): Boolean = obtenerUsuarioId() != SIN_SESION

    /** Borra la sesión guardada (cuando el usuario toca "Salir"). */
    fun cerrarSesion() {
        preferencias.edit { remove(CLAVE_USUARIO_ID) }
    }

    /** Responde si el usuario ya vio la pantalla de bienvenida. */
    fun bienvenidaYaVista(): Boolean =
        preferencias.getBoolean(CLAVE_BIENVENIDA, false)

    /** Anota que la bienvenida ya se mostró, para no repetirla. */
    fun marcarBienvenidaVista() {
        preferencias.edit { putBoolean(CLAVE_BIENVENIDA, true) }
    }

    companion object {
        private const val NOMBRE_ARCHIVO = "sesion_nova"
        private const val CLAVE_USUARIO_ID = "usuario_id"

        private const val CLAVE_BIENVENIDA = "bienvenida_vista"

        // Valor que significa "no hay sesión". Los ids reales empiezan en 1.
        const val SIN_SESION = -1L
    }
}