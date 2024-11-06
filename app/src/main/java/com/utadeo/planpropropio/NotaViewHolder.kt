package com.utadeo.planpropropio

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titulo: TextView = itemView.findViewById(R.id.tituloNota)
    private val descripcion: TextView = itemView.findViewById(R.id.descripcionNota)
    private val fechaRegistro: TextView = itemView.findViewById(R.id.FechaRegistro)
    private val fechaFinalizacion: TextView = itemView.findViewById(R.id.FechaFinalizacion)
    private val iconoNota: ImageView = itemView.findViewById(R.id.iconoNota)

    fun bind(nota: Nota) {
        titulo.text = nota.titulo
        descripcion.text = nota.descripcion
        fechaRegistro.text = nota.fechaRegistro
        fechaFinalizacion.text = nota.fechaFinalizacion


    }
}

