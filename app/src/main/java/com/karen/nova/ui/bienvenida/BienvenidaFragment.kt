package com.karen.nova.ui.bienvenida

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karen.nova.R
import com.karen.nova.databinding.FragmentBienvenidaBinding

/** Pantalla informativa que pone en contexto al usuario la primera vez que abre la app. */
class BienvenidaFragment : Fragment(R.layout.fragment_bienvenida) {

    private var _binding: FragmentBienvenidaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BienvenidaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBienvenidaBinding.bind(view)

        // Si ya la vio antes, pasamos directo al inicio de sesión
        if (viewModel.yaSeMostro()) {
            irAlInicioSesion()
            return
        }

        binding.botonComenzar.setOnClickListener {
            viewModel.marcarComoVista()
            irAlInicioSesion()
        }
    }

    private fun irAlInicioSesion() {
        findNavController().navigate(R.id.accion_bienvenida_a_inicioSesion)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}