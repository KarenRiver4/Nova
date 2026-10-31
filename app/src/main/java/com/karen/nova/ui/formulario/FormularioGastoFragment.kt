package com.karen.nova.ui.formulario

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.karen.nova.R
import com.karen.nova.databinding.FragmentFormularioGastoBinding
import com.karen.nova.utilidades.Formato
import com.karen.nova.utilidades.Opciones
import java.util.Calendar
import java.util.TimeZone

/** Pantalla del formulario para crear un gasto. */
class FormularioGastoFragment : Fragment(R.layout.fragment_formulario_gasto) {

    private var _binding: FragmentFormularioGastoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FormularioGastoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFormularioGastoBinding.bind(view)

        // Flecha ← de la barra: volver a la lista
        binding.barraSuperior.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Llenamos las listas desplegables con las opciones oficiales
        binding.campoCategoria.setSimpleItems(Opciones.CATEGORIAS.toTypedArray())
        binding.campoMetodoPago.setSimpleItems(Opciones.METODOS_PAGO.toTypedArray())

        // Tocar el campo de fecha o su ícono abre el calendario
        binding.campoFecha.setOnClickListener { abrirCalendario() }
        binding.layoutFecha.setEndIconOnClickListener { abrirCalendario() }

        binding.botonGuardar.setOnClickListener {
            viewModel.guardar(
                concepto = binding.campoConcepto.text.toString(),
                montoTexto = binding.campoMonto.text.toString(),
                categoria = binding.campoCategoria.text.toString(),
                metodoPago = binding.campoMetodoPago.text.toString(),
                notas = binding.campoNotas.text.toString()
            )
        }

        // Mostramos la fecha elegida con formato bonito
        viewModel.fecha.observe(viewLifecycleOwner) { fecha ->
            binding.campoFecha.setText(Formato.fecha(fecha))
        }

        viewModel.estado.observe(viewLifecycleOwner) { estado ->
            mostrarEstado(estado)
        }
    }

    private fun mostrarEstado(estado: EstadoFormulario) {
        limpiarErrores()
        binding.botonGuardar.isEnabled = estado !is EstadoFormulario.Guardando

        when (estado) {
            is EstadoFormulario.Inicial, is EstadoFormulario.Guardando -> Unit

            is EstadoFormulario.ErroresValidacion -> {
                binding.layoutConcepto.error = estado.errores.concepto
                binding.layoutMonto.error = estado.errores.monto
                binding.layoutCategoria.error = estado.errores.categoria
                binding.layoutFecha.error = estado.errores.fecha
                binding.layoutMetodoPago.error = estado.errores.metodoPago
                binding.layoutNotas.error = estado.errores.notas
            }

            is EstadoFormulario.ErrorAlGuardar -> {
                Snackbar.make(binding.root, estado.mensaje, Snackbar.LENGTH_LONG).show()
                viewModel.reiniciarEstado()
            }

            is EstadoFormulario.Guardado -> {
                Toast.makeText(requireContext(), "Gasto guardado", Toast.LENGTH_SHORT).show()
                viewModel.reiniciarEstado()
                findNavController().popBackStack()
            }
        }
    }

    private fun limpiarErrores() {
        binding.layoutConcepto.error = null
        binding.layoutMonto.error = null
        binding.layoutCategoria.error = null
        binding.layoutFecha.error = null
        binding.layoutMetodoPago.error = null
        binding.layoutNotas.error = null
    }

    /** Abre el calendario de Material, sin permitir fechas futuras. */
    private fun abrirCalendario() {
        val fechaActual = viewModel.fecha.value ?: System.currentTimeMillis()
        val calendario = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha del gasto")
            .setSelection(localAUtc(fechaActual))
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build()
            )
            .build()

        calendario.addOnPositiveButtonClickListener { seleccion ->
            viewModel.cambiarFecha(utcALocal(seleccion))
        }
        calendario.show(parentFragmentManager, "selector_fecha")
    }

    /**
     * El calendario de Material trabaja en hora UTC (hora de Londres).
     * Convertimos el día elegido a ese mismo día en la hora del celular,
     * para que en Colombia no aparezca un día menos.
     */
    private fun utcALocal(utc: Long): Long {
        val diaUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utc }
        return Calendar.getInstance().apply {
            clear()
            set(
                diaUtc.get(Calendar.YEAR),
                diaUtc.get(Calendar.MONTH),
                diaUtc.get(Calendar.DAY_OF_MONTH),
                12, 0
            )
        }.timeInMillis
    }

    /** Lo contrario: un día en hora local, convertido al formato que espera el calendario. */
    private fun localAUtc(local: Long): Long {
        val diaLocal = Calendar.getInstance().apply { timeInMillis = local }
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(
                diaLocal.get(Calendar.YEAR),
                diaLocal.get(Calendar.MONTH),
                diaLocal.get(Calendar.DAY_OF_MONTH)
            )
        }.timeInMillis
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}