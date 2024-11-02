package com.utadeo.planpropropio

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast


// función para el toast personalizado de la app
@SuppressLint("InflateParams")
fun toastPerzonalizado(context: Context, mensaje: String) {
    val inflater = LayoutInflater.from(context)
    val layout = inflater.inflate(R.layout.toast_personalizado, null)
    val textView = layout.findViewById<TextView>(R.id.text)
    textView.text = mensaje

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    toast.view = layout
    toast.show()
}



