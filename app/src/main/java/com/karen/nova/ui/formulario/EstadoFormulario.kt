package com.karen.nova.ui.formulario

/** Mensajes de error de cada campo del formulario. null = el campo está bien. */
data class ErroresFormulario(
    val concepto: String? = null,
    val monto: String? = null,
    val categoria: String? = null,
    val fecha: String? = null,
    val metodoPago: String? = null,
    val notas: String? = null
) {
    fun hayErrores(): Boolean =
        listOf(concepto, monto, categoria, fecha, metodoPago, notas).any { it != null }
}

/** Las situaciones en las que puede estar el formulario. */
sealed class EstadoFormulario {
    data object Inicial : EstadoFormulario()
    data object Guardando : EstadoFormulario()
    data class ErroresValidacion(val errores: ErroresFormulario) : EstadoFormulario()
    data class ErrorAlGuardar(val mensaje: String) : EstadoFormulario()
    data object Guardado : EstadoFormulario()
}