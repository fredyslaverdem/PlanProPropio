package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val textViewOlvidaste = findViewById<TextView>(R.id.textViewOlvidastePerdisteLogin)

        val spannableStringOlvidaste = SpannableString(getString(R.string.olvidastetucontraseña))
        spannableStringOlvidaste.setSpan(UnderlineSpan(),0, spannableStringOlvidaste.length, 0)
        textViewOlvidaste.text = spannableStringOlvidaste

        val textViewRegistro = findViewById<TextView>(R.id.textViewRegistroLogin)

        val textoCompleto = "¿No tienes una cuenta? Registrate"
        val spannableStringRegistro = SpannableString(textoCompleto)

        val start = textoCompleto.indexOf("Registrate")
        val end = start + "Registrate".length
        spannableStringRegistro.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringRegistro.setSpan(UnderlineSpan(),0, spannableStringRegistro.length, 0)

        textViewRegistro.text = spannableStringRegistro

        textViewOlvidaste.setOnClickListener {
            val intent = Intent(this, RecuperarActivity::class.java)
            startActivity(intent)
        }
    }
}