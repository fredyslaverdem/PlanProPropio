package com.utadeo.planpropropio

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tituloHabito: TextView = itemView.findViewById(R.id.actividad)


    fun bind(habito: Habito) {
        tituloHabito.text = habito.titulo

    }
}

