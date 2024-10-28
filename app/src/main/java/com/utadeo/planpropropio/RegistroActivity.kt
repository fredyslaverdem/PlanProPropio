package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class RegistroActivity : AppCompatActivity () {
    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto

        window.statusBarColor = backgroundColor

        // Obtener referencia de TextView Iniciar Sesion
        val textViewIniciarSesion = findViewById<TextView>(R.id.textViewIniciarSesionRegistro)
        // Aplicar subrayado al texto de Iniciar Sesion
        val textoCompleto = "¿Ya tienes una cuenta? Inicia Sesión"
        val spannableStringIniciarSesion = SpannableString(textoCompleto)

        val start = textoCompleto.indexOf("Inicia Sesión")
        val end = start + "Inicia Sesión".length
        spannableStringIniciarSesion.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringIniciarSesion.setSpan(UnderlineSpan(), 0, spannableStringIniciarSesion.length, 0)

        textViewIniciarSesion.text = spannableStringIniciarSesion


        // Programación del textview Iniciar Sesion para ir a la vista de Login
        textViewIniciarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }



    }
}