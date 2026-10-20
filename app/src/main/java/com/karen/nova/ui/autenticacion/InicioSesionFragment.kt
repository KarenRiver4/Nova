package com.karen.nova.ui.autenticacion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.karen.nova.R
import com.karen.nova.databinding.FragmentInicioSesionBinding

/** Pantalla de inicio de sesión. */
class InicioSesionFragment : Fragment(R.layout.fragment_inicio_sesion) {

    private var _binding: FragmentInicioSesionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AutenticacionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInicioSesionBinding.bind(view)
        // Si ya hay sesión guardada, vamos directo a la lista
        if (viewModel.haySesionActiva()) {
            findNavController().navigate(R.id.accion_inicioSesion_a_lista)
            return
        }

        // Al tocar "Ingresar", enviamos los datos al ViewModel
        binding.botonIngresar.setOnClickListener {
            viewModel.iniciarSesion(
                correo = binding.campoCorreo.text.toString(),
                contrasena = binding.campoContrasena.text.toString()
            )
        }

        // Al tocar "Regístrate", limpiamos el formulario y vamos al registro
        binding.textoIrARegistro.setOnClickListener {
            limpiarFormulario()
            viewModel.reiniciarEstadoInicioSesion()
            findNavController().navigate(R.id.accion_inicioSesion_a_registro)
        }

        viewModel.estadoInicioSesion.observe(viewLifecycleOwner) { estado ->
            mostrarEstado(estado)
        }
    }

    private fun mostrarEstado(estado: EstadoInicioSesion) {
        binding.layoutCorreo.error = null
        binding.layoutContrasena.error = null
        binding.botonIngresar.isEnabled = estado !is EstadoInicioSesion.Cargando

        when (estado) {
            is EstadoInicioSesion.Inicial, is EstadoInicioSesion.Cargando -> Unit

            is EstadoInicioSesion.ErroresValidacion -> {
                binding.layoutCorreo.error = estado.errores.correo
                binding.layoutContrasena.error = estado.errores.contrasena
            }

            is EstadoInicioSesion.CredencialesIncorrectas -> {
                Snackbar.make(
                    binding.root,
                    "Correo o contraseña incorrectos",
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.reiniciarEstadoInicioSesion()
            }

            is EstadoInicioSesion.Exitoso -> {
                viewModel.reiniciarEstadoInicioSesion()
                findNavController().navigate(R.id.accion_inicioSesion_a_lista)
            }
        }
    }

    /** Borra lo escrito y los errores, para que el formulario quede limpio al volver. */
    private fun limpiarFormulario() {
        binding.campoCorreo.text?.clear()
        binding.campoContrasena.text?.clear()
        binding.layoutCorreo.error = null
        binding.layoutContrasena.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}