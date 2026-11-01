package com.karen.nova.ui.detalle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.karen.nova.datos.local.BaseDatosNova
import com.karen.nova.datos.local.Gasto
import com.karen.nova.datos.repositorio.RepositorioGasto
import kotlinx.coroutines.launch

/** Lógica del detalle: muestra un gasto y permite eliminarlo. */
class DetalleGastoViewModel(
    application: Application,
    estadoGuardado: SavedStateHandle
) : AndroidViewModel(application) {

    private val repositorio = RepositorioGasto(
        BaseDatosNova.obtenerInstancia(application).gastoDao()
    )

    // El id que nos envió la lista al navegar
    private val gastoId: Long = estadoGuardado["gastoId"] ?: -1L

    /** El gasto "vivo": si se edita, el detalle se actualiza solo. */
    val gasto: LiveData<Gasto?> = repositorio.obtenerGastoPorId(gastoId)

    private val _eliminado = MutableLiveData(false)
    val eliminado: LiveData<Boolean> = _eliminado

    private val _mensajeError = MutableLiveData<String?>(null)
    val mensajeError: LiveData<String?> = _mensajeError

    /** Elimina el gasto que se está mostrando. */
    fun eliminar() {
        val gastoActual = gasto.value ?: return
        viewModelScope.launch {
            try {
                repositorio.eliminar(gastoActual)
                _eliminado.value = true
            } catch (e: Exception) {
                _mensajeError.value = "No se pudo eliminar el gasto. Intenta de nuevo."
            }
        }
    }

    fun mensajeMostrado() {
        _mensajeError.value = null
    }
}