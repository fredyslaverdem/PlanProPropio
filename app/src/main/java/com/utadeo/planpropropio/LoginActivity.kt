package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Definimos la variable para firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto

        window.statusBarColor = backgroundColor

        // Obtener referencia al TextView Olvidaste tu contraseña
        val textViewOlvidaste = findViewById<TextView>(R.id.textViewOlvidastePerdisteLogin)

        // Aplicar subrayado al texto de olvide contraseña
        val spannableStringOlvidaste = SpannableString(getString(R.string.olvidastetucontraseña))
        spannableStringOlvidaste.setSpan(UnderlineSpan(),0, spannableStringOlvidaste.length, 0)
        textViewOlvidaste.text = spannableStringOlvidaste

        // Obtener referencia al TextView Registro
        val textViewRegistro = findViewById<TextView>(R.id.textViewRegistroLogin)
        // Aplicar subrayado al texto de Registro
        val textoCompleto = "¿No tienes una cuenta? Registrate"
        val spannableStringRegistro = SpannableString(textoCompleto)

        val start = textoCompleto.indexOf("Registrate")
        val end = start + "Registrate".length
        spannableStringRegistro.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringRegistro.setSpan(UnderlineSpan(),0, spannableStringRegistro.length, 0)

        textViewRegistro.text = spannableStringRegistro


        // Configurar el click listener para el TextView de Olvidaste tu contraseña
        textViewOlvidaste.setOnClickListener {
            val intent = Intent(this, RecuperarActivity::class.java)
            startActivity(intent)
        }

        // Configurar el click listener para el TextView de Registro
        textViewRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }


        // configuración para la base de datos y funcionalidad con los botones y edittext



    }
}