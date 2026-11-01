package com.karen.nova.ui.detalle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.karen.nova.R
import com.karen.nova.databinding.FragmentDetalleGastoBinding
import com.karen.nova.datos.local.Gasto
import com.karen.nova.utilidades.Formato

/** Pantalla de detalle: muestra un gasto con las acciones Editar, Compartir y Eliminar. */
class DetalleGastoFragment : Fragment(R.layout.fragment_detalle_gasto) {

    private var _binding: FragmentDetalleGastoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalleGastoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetalleGastoBinding.bind(view)

        binding.barraSuperior.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Mostramos los datos del gasto. Si se borra, llega null y lo ignoramos.
        viewModel.gasto.observe(viewLifecycleOwner) { gasto ->
            if (gasto == null) return@observe
            mostrarGasto(gasto)
        }

        binding.botonEditar.setOnClickListener {
            viewModel.gasto.value?.let { gasto ->
                findNavController().navigate(
                    R.id.accion_detalle_a_formulario,
                    Bundle().apply { putLong("gastoId", gasto.id) }
                )
            }
        }

        binding.botonCompartir.setOnClickListener {
            viewModel.gasto.value?.let { compartir(it) }
        }

        binding.botonEliminar.setOnClickListener {
            confirmarEliminacion()
        }

        // Cuando se eliminó, avisamos y volvemos a la lista
        viewModel.eliminado.observe(viewLifecycleOwner) { eliminado ->
            if (eliminado) {
                Toast.makeText(requireContext(), "Gasto eliminado", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje != null) {
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG).show()
                viewModel.mensajeMostrado()
            }
        }
    }

    private fun mostrarGasto(gasto: Gasto) {
        binding.textoMonto.text = Formato.dinero(gasto.monto)
        binding.textoConcepto.text = gasto.concepto
        binding.textoCategoria.text = gasto.categoria
        binding.textoFecha.text = Formato.fecha(gasto.fecha)
        binding.textoMetodoPago.text = gasto.metodoPago
        binding.textoNotas.text = gasto.notas ?: "Sin notas"
    }

    /** Pregunta antes de borrar, porque no se puede deshacer. */
    private fun confirmarEliminacion() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Eliminar este gasto?")
            .setMessage("Esta acción no se puede deshacer.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ -> viewModel.eliminar() }
            .show()
    }

    /** Abre el menú de compartir del celular (WhatsApp, correo, etc.). */
    private fun compartir(gasto: Gasto) {
        val texto = "Gasté ${Formato.dinero(gasto.monto)} en ${gasto.categoria} " +
                "(${gasto.concepto}) el ${Formato.fecha(gasto.fecha)} 💸 Registrado con Nova."
        val intencion = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texto)
        }
        startActivity(Intent.createChooser(intencion, "Compartir gasto"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}