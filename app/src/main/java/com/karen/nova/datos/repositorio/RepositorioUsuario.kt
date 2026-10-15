package com.karen.nova.datos.repositorio

import android.database.sqlite.SQLiteConstraintException
import com.karen.nova.datos.local.Usuario
import com.karen.nova.datos.local.UsuarioDao

/**
 * Intermediario entre las pantallas y la tabla de usuarios.
 * Se encarga del registro y del inicio de sesión.
 */
class RepositorioUsuario(private val usuarioDao: UsuarioDao) {

    /**
     * Registra un usuario nuevo.
     * Devuelve el id del usuario creado, o null si el correo ya estaba registrado.
     */
    suspend fun registrar(nombre: String, correo: String, contrasena: String): Long? {
        val usuario = Usuario(
            nombre = nombre.trim(),
            correo = normalizarCorreo(correo),
            contrasena = contrasena
        )
        return try {
            usuarioDao.insertar(usuario)
        } catch (e: SQLiteConstraintException) {
            // La base de datos rechazó el correo porque ya existe
            null
        }
    }

    /**
     * Revisa si el correo y la contraseña coinciden.
     * Devuelve el usuario si son correctos, o null si no.
     */
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        val usuario = usuarioDao.buscarPorCorreo(normalizarCorreo(correo))
        return if (usuario != null && usuario.contrasena == contrasena) usuario else null
    }

    /** Busca un usuario por su id (por ejemplo, para mostrar su nombre). */
    suspend fun buscarPorId(id: Long): Usuario? = usuarioDao.buscarPorId(id)

    /**
     * Quita espacios y pasa el correo a minúsculas,
     * para que "Karen@Mail.com " y "karen@mail.com" se traten como el mismo.
     */
    private fun normalizarCorreo(correo: String): String = correo.trim().lowercase()
}