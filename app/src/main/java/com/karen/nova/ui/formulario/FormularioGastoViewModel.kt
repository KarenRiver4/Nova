package com.karen.nova.ui.formulario

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karen.nova.datos.local.BaseDatosNova
import com.karen.nova.datos.local.Gasto
import com.karen.nova.datos.repositorio.RepositorioGasto
import com.karen.nova.utilidades.GestorSesion
import com.karen.nova.utilidades.Opciones
import kotlinx.coroutines.launch
import java.util.Calendar

/** Lógica del formulario: guarda la fecha elegida, valida los campos y guarda el gasto. */
class FormularioGastoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = RepositorioGasto(
        BaseDatosNova.obtenerInstancia(application).gastoDao()
    )
    private val gestorSesion = GestorSesion(application)

    // La fecha elegida en el calendario. Empieza en hoy.
    private val _fecha = MutableLiveData(System.currentTimeMillis())
    val fecha: LiveData<Long> = _fecha

    private val _estado = MutableLiveData<EstadoFormulario>(EstadoFormulario.Inicial)
    val estado: LiveData<EstadoFormulario> = _estado

    /** Se llama cuando el usuario elige una fecha en el calendario. */
    fun cambiarFecha(nuevaFecha: Long) {
        _fecha.value = nuevaFecha
    }

    /** Se llama cuando el usuario toca "Guardar". */
    fun guardar(
        concepto: String,
        montoTexto: String,
        categoria: String,
        metodoPago: String,
        notas: String
    ) {
        val fechaElegida = _fecha.value ?: System.currentTimeMillis()
        val errores = validar(concepto, montoTexto, categoria, fechaElegida, metodoPago, notas)
        if (errores.hayErrores()) {
            _estado.value = EstadoFormulario.ErroresValidacion(errores)
            return
        }

        _estado.value = EstadoFormulario.Guardando
        val gasto = Gasto(
            usuarioId = gestorSesion.obtenerUsuarioId(),
            concepto = concepto.trim(),
            monto = montoTexto.toLong(),
            categoria = categoria,
            fecha = fechaElegida,
            metodoPago = metodoPago,
            notas = notas.trim().ifBlank { null }
        )

        viewModelScope.launch {
            try {
                repositorio.insertar(gasto)
                _estado.value = EstadoFormulario.Guardado
            } catch (e: Exception) {
                _estado.value = EstadoFormulario.ErrorAlGuardar(
                    "No se pudo guardar el gasto. Intenta de nuevo."
                )
            }
        }
    }

    fun reiniciarEstado() {
        _estado.value = EstadoFormulario.Inicial
    }

    /** Revisa cada campo y devuelve los errores encontrados. */
    private fun validar(
        concepto: String,
        montoTexto: String,
        categoria: String,
        fecha: Long,
        metodoPago: String,
        notas: String
    ): ErroresFormulario {
        val monto = montoTexto.toLongOrNull()
        return ErroresFormulario(
            concepto = when {
                concepto.isBlank() -> "Escribe el concepto"
                concepto.trim().length > 50 -> "Máximo 50 caracteres"
                else -> null
            },
            monto = when {
                montoTexto.isBlank() -> "Escribe el monto"
                monto == null || monto <= 0 -> "El monto debe ser mayor a 0"
                monto > 1_000_000_000 -> "El monto es demasiado alto"
                else -> null
            },
            categoria = if (categoria !in Opciones.CATEGORIAS) "Selecciona una categoría" else null,
            fecha = if (fecha > finDeHoy()) "La fecha no puede ser posterior a hoy" else null,
            metodoPago = if (metodoPago !in Opciones.METODOS_PAGO) "Selecciona un método de pago" else null,
            notas = if (notas.trim().length > 200) "Máximo 200 caracteres" else null
        )
    }

    /** El último milisegundo del día de hoy (23:59:59.999). */
    private fun finDeHoy(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.timeInMillis
}