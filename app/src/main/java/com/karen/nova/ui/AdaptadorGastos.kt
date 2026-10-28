package com.karen.nova.ui.lista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karen.nova.databinding.ItemGastoBinding
import com.karen.nova.datos.local.Gasto
import com.karen.nova.utilidades.Formato

/**
 * Llena la lista con los gastos: por cada gasto, llena una tarjeta.
 * @param alTocarGasto qué hacer cuando el usuario toca un gasto.
 */
class AdaptadorGastos(
    private val alTocarGasto: (Gasto) -> Unit
) : ListAdapter<Gasto, AdaptadorGastos.TarjetaGasto>(ComparadorGastos) {

    /** Guarda la conexión con una tarjeta (item_gasto.xml). */
    class TarjetaGasto(val binding: ItemGastoBinding) : RecyclerView.ViewHolder(binding.root)

    /** Crea una tarjeta nueva y vacía. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaGasto {
        val binding = ItemGastoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TarjetaGasto(binding)
    }

    /** Llena una tarjeta con los datos del gasto que está en esa posición. */
    override fun onBindViewHolder(tarjeta: TarjetaGasto, posicion: Int) {
        val gasto = getItem(posicion)
        tarjeta.binding.textoConcepto.text = gasto.concepto
        tarjeta.binding.textoCategoriaFecha.text =
            "${gasto.categoria} · ${Formato.fecha(gasto.fecha)}"
        tarjeta.binding.textoMonto.text = Formato.dinero(gasto.monto)
        tarjeta.binding.root.setOnClickListener { alTocarGasto(gasto) }
    }

    /** Le enseña a la lista a detectar qué gastos cambiaron, para animar solo esos. */
    object ComparadorGastos : DiffUtil.ItemCallback<Gasto>() {
        override fun areItemsTheSame(viejo: Gasto, nuevo: Gasto) = viejo.id == nuevo.id
        override fun areContentsTheSame(viejo: Gasto, nuevo: Gasto) = viejo == nuevo
    }
}