package com.karen.nova.ui.lista

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.karen.nova.datos.local.BaseDatosNova
import com.karen.nova.datos.local.Gasto
import com.karen.nova.datos.repositorio.RepositorioGasto
import com.karen.nova.utilidades.GestorSesion
import java.util.Calendar

/** Lógica de la pantalla de lista: los gastos del usuario, el total del mes y el cierre de sesión. */
class ListaGastosViewModel(application: Application) : AndroidViewModel(application) {

    private val gestorSesion = GestorSesion(application)
    private val repositorio = RepositorioGasto(
        BaseDatosNova.obtenerInstancia(application).gastoDao()
    )

    /** Lista "viva" con los gastos del usuario que inició sesión. */
    val gastos: LiveData<List<Gasto>> =
        repositorio.obtenerGastosDeUsuario(gestorSesion.obtenerUsuarioId())

    /** Suma de los gastos del mes actual. Se recalcula sola cuando cambia la lista. */
    val totalDelMes: LiveData<Long> = gastos.map { lista ->
        lista.filter { esDelMesActual(it.fecha) }.sumOf { it.monto }
    }

    /** Borra la sesión guardada. */
    fun cerrarSesion() {
        gestorSesion.cerrarSesion()
    }

    /** Responde si una fecha cae en el mes y año actuales. */
    private fun esDelMesActual(fecha: Long): Boolean {
        val hoy = Calendar.getInstance()
        val delGasto = Calendar.getInstance().apply { timeInMillis = fecha }
        return hoy.get(Calendar.YEAR) == delGasto.get(Calendar.YEAR) &&
                hoy.get(Calendar.MONTH) == delGasto.get(Calendar.MONTH)
    }
}