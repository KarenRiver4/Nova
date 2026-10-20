package com.karen.nova.ui.lista

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.karen.nova.utilidades.GestorSesion

/**
 * Lógica de la pantalla de lista de gastos.
 * Por ahora solo maneja el cierre de sesión; en la Fase 4 manejará los gastos.
 */
class ListaGastosViewModel(application: Application) : AndroidViewModel(application) {

    private val gestorSesion = GestorSesion(application)

    /** Borra la sesión guardada. */
    fun cerrarSesion() {
        gestorSesion.cerrarSesion()
    }
}