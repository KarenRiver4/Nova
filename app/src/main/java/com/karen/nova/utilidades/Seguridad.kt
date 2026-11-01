package com.karen.nova.utilidades

import java.security.MessageDigest

/** Funciones de seguridad para proteger las contraseñas. */
object Seguridad {

    /**
     * Convierte una contraseña en su hash SHA-256 (una "huella" irreversible).
     * Ejemplo: "secreto1" → "a3f1c9e8b2..." (64 caracteres).
     */
    fun hashear(contrasena: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(contrasena.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}