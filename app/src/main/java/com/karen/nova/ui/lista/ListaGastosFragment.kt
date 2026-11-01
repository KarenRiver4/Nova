package com.karen.nova.ui.lista

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karen.nova.R
import com.karen.nova.databinding.FragmentListaGastosBinding
import com.karen.nova.utilidades.Formato

/** Pantalla con la lista de gastos, el total del mes y el botón para agregar. */
class ListaGastosFragment : Fragment(R.layout.fragment_lista_gastos) {

    private var _binding: FragmentListaGastosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListaGastosViewModel by viewModels()

    private val adaptador = AdaptadorGastos { gasto ->
        // Vamos al detalle enviando el id del gasto tocado
        findNavController().navigate(
            R.id.accion_lista_a_detalle,
            Bundle().apply { putLong("gastoId", gasto.id) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListaGastosBinding.bind(view)

        binding.listaGastos.adapter = adaptador

        // Cada vez que cambian los gastos, actualizamos la lista y el estado vacío
        viewModel.gastos.observe(viewLifecycleOwner) { lista ->
            adaptador.submitList(lista)
            binding.estadoVacio.isVisible = lista.isEmpty()
            binding.listaGastos.isVisible = lista.isNotEmpty()
        }

        // Cada vez que cambia el total, lo mostramos con formato de dinero
        viewModel.totalDelMes.observe(viewLifecycleOwner) { total ->
            binding.textoTotal.text = Formato.dinero(total)
        }

        binding.botonAgregar.setOnClickListener {
            findNavController().navigate(R.id.accion_lista_a_formulario)
        }

        binding.barraSuperior.setOnMenuItemClickListener { opcion ->
            when (opcion.itemId) {
                R.id.accion_salir -> {
                    confirmarCierreSesion()
                    true
                }
                else -> false
            }
        }
    }

    private fun confirmarCierreSesion() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Cerrar sesión?")
            .setMessage("Tendrás que volver a ingresar tu correo y contraseña.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Salir") { _, _ ->
                viewModel.cerrarSesion()
                findNavController().navigate(R.id.accion_lista_a_inicioSesion)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}