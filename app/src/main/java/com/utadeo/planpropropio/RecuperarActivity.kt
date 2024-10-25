package com.utadeo.planpropropio

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecuperarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar)

        val textViewRegistro = findViewById<TextView>(R.id.textViewRegistroRecuperar)

        val textoCompleto = "¿No tienes una cuenta? Registrate"
        val spannableStringRegistro = SpannableString(textoCompleto)

        val start = textoCompleto.indexOf("Registrate")
        val end = start + "Registrate".length
        spannableStringRegistro.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringRegistro.setSpan(UnderlineSpan(),0, spannableStringRegistro.length, 0)

        textViewRegistro.text = spannableStringRegistro

    }
}