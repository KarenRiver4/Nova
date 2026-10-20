package com.karen.nova.ui.autenticacion

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karen.nova.R
import com.karen.nova.databinding.FragmentRegistroBinding

/** Pantalla de registro: muestra el formulario y reacciona a los estados del ViewModel. */
class RegistroFragment : Fragment(R.layout.fragment_registro) {

    // Conexión con el diseño (ViewBinding)
    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    // El ViewModel de esta pantalla
    private val viewModel: AutenticacionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistroBinding.bind(view)

        // Al tocar "Registrarme", enviamos los datos al ViewModel
        binding.botonRegistrarme.setOnClickListener {
            viewModel.registrar(
                nombre = binding.campoNombre.text.toString(),
                correo = binding.campoCorreo.text.toString(),
                contrasena = binding.campoContrasena.text.toString(),
                confirmacion = binding.campoConfirmacion.text.toString()
            )
        }

        // Al tocar "¿Ya tienes cuenta?", volvemos al inicio de sesión
        binding.textoIrAInicioSesion.setOnClickListener {
            findNavController().popBackStack()
        }

        // Cada vez que cambia el estado, actualizamos la pantalla
        viewModel.estadoRegistro.observe(viewLifecycleOwner) { estado ->
            mostrarEstado(estado)
        }
    }

    private fun mostrarEstado(estado: EstadoRegistro) {
        limpiarErrores()
        // Mientras se guarda, el botón se desactiva para evitar doble registro
        binding.botonRegistrarme.isEnabled = estado !is EstadoRegistro.Cargando

        when (estado) {
            is EstadoRegistro.Inicial, is EstadoRegistro.Cargando -> Unit

            is EstadoRegistro.ErroresValidacion -> {
                binding.layoutNombre.error = estado.errores.nombre
                binding.layoutCorreo.error = estado.errores.correo
                binding.layoutContrasena.error = estado.errores.contrasena
                binding.layoutConfirmacion.error = estado.errores.confirmacion
            }

            is EstadoRegistro.Exitoso -> {
                Toast.makeText(
                    requireContext(),
                    "¡Cuenta creada! Ahora inicia sesión",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.reiniciarEstadoRegistro()
                findNavController().popBackStack()
            }
        }
    }

    private fun limpiarErrores() {
        binding.layoutNombre.error = null
        binding.layoutCorreo.error = null
        binding.layoutContrasena.error = null
        binding.layoutConfirmacion.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Soltamos la conexión con el diseño para no gastar memoria
        _binding = null
    }
}