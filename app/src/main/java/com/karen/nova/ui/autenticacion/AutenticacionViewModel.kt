package com.karen.nova.ui.autenticacion

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karen.nova.datos.local.BaseDatosNova
import com.karen.nova.datos.repositorio.RepositorioUsuario
import com.karen.nova.utilidades.GestorSesion
import kotlinx.coroutines.launch

/**
 * Maneja la lógica del registro y del inicio de sesión:
 * valida los datos, habla con el repositorio y guarda la sesión.
 */
class AutenticacionViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = RepositorioUsuario(
        BaseDatosNova.obtenerInstancia(application).usuarioDao()
    )
    private val gestorSesion = GestorSesion(application)

    // ---------- REGISTRO ----------

    private val _estadoRegistro = MutableLiveData<EstadoRegistro>(EstadoRegistro.Inicial)
    val estadoRegistro: LiveData<EstadoRegistro> = _estadoRegistro

    /** Se llama cuando el usuario toca "Registrarme". */
    fun registrar(nombre: String, correo: String, contrasena: String, confirmacion: String) {
        val errores = validarRegistro(nombre, correo, contrasena, confirmacion)
        if (errores.hayErrores()) {
            _estadoRegistro.value = EstadoRegistro.ErroresValidacion(errores)
            return
        }

        _estadoRegistro.value = EstadoRegistro.Cargando
        viewModelScope.launch {
            val idNuevo = repositorio.registrar(nombre, correo, contrasena)
            _estadoRegistro.value = if (idNuevo != null) {
                EstadoRegistro.Exitoso
            } else {
                EstadoRegistro.ErroresValidacion(
                    ErroresRegistro(correo = "Este correo ya está registrado")
                )
            }
        }
    }

    fun reiniciarEstadoRegistro() {
        _estadoRegistro.value = EstadoRegistro.Inicial
    }

    private fun validarRegistro(
        nombre: String,
        correo: String,
        contrasena: String,
        confirmacion: String
    ): ErroresRegistro = ErroresRegistro(
        nombre = if (nombre.isBlank()) "Escribe tu nombre" else null,
        correo = validarCorreo(correo),
        contrasena = validarContrasena(contrasena),
        confirmacion = when {
            confirmacion.isEmpty() -> "Confirma tu contraseña"
            confirmacion != contrasena -> "Las contraseñas no coinciden"
            else -> null
        }
    )

    // ---------- INICIO DE SESIÓN ----------

    private val _estadoInicioSesion =
        MutableLiveData<EstadoInicioSesion>(EstadoInicioSesion.Inicial)
    val estadoInicioSesion: LiveData<EstadoInicioSesion> = _estadoInicioSesion

    /** Se llama cuando el usuario toca "Ingresar". */
    fun iniciarSesion(correo: String, contrasena: String) {
        val errores = ErroresInicioSesion(
            correo = validarCorreo(correo),
            contrasena = validarContrasena(contrasena)
        )
        if (errores.hayErrores()) {
            _estadoInicioSesion.value = EstadoInicioSesion.ErroresValidacion(errores)
            return
        }

        _estadoInicioSesion.value = EstadoInicioSesion.Cargando
        viewModelScope.launch {
            val usuario = repositorio.iniciarSesion(correo, contrasena)
            if (usuario != null) {
                // Recordamos quién inició sesión
                gestorSesion.guardarSesion(usuario.id)
                _estadoInicioSesion.value = EstadoInicioSesion.Exitoso
            } else {
                _estadoInicioSesion.value = EstadoInicioSesion.CredencialesIncorrectas
            }
        }
    }

    fun reiniciarEstadoInicioSesion() {
        _estadoInicioSesion.value = EstadoInicioSesion.Inicial
    }

    // ---------- VALIDACIONES COMPARTIDAS ----------

    /** Devuelve un mensaje de error para el correo, o null si está bien. */
    private fun validarCorreo(correo: String): String? = when {
        correo.isBlank() -> "Escribe tu correo"
        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> "El correo no es válido"
        else -> null
    }

    /** Devuelve un mensaje de error para la contraseña, o null si está bien. */
    private fun validarContrasena(contrasena: String): String? = when {
        contrasena.isEmpty() -> "Escribe tu contraseña"
        contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        else -> null
    }
}