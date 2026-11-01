package com.karen.nova.ui.bienvenida

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.karen.nova.utilidades.GestorSesion

/** Lógica de la bienvenida: saber si ya se mostró y recordarlo. */
class BienvenidaViewModel(application: Application) : AndroidViewModel(application) {

    private val gestorSesion = GestorSesion(application)

    fun yaSeMostro(): Boolean = gestorSesion.bienvenidaYaVista()

    fun marcarComoVista() {
        gestorSesion.marcarBienvenidaVista()
    }
}