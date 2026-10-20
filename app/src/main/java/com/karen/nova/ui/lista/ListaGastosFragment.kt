package com.karen.nova.ui.lista

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karen.nova.R
import com.karen.nova.databinding.FragmentListaGastosBinding

/** Pantalla de la lista de gastos (por ahora con la barra y el botón Salir). */
class ListaGastosFragment : Fragment(R.layout.fragment_lista_gastos) {

    private var _binding: FragmentListaGastosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListaGastosViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListaGastosBinding.bind(view)

        // Cuando tocan una opción del menú de la barra
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

    /** Pregunta antes de cerrar sesión, para evitar salidas por accidente. */
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