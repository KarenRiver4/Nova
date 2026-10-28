package com.karen.nova.utilidades

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Convierte números en textos bonitos para mostrar en pantalla. */
object Formato {

    private val colombia = Locale.forLanguageTag("es-CO")

    /** 25000 → "$25.000" */
    fun dinero(monto: Long): String =
        "$" + NumberFormat.getNumberInstance(colombia).format(monto)

    /** Milisegundos → "27/10/2026" */
    fun fecha(milisegundos: Long): String =
        SimpleDateFormat("dd/MM/yyyy", colombia).format(Date(milisegundos))
}