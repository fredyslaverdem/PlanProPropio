package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class RecuperarActivity : AppCompatActivity() {

    // Definimos variables para gestion del cambio

    private lateinit var editTextCorreo : EditText
    private lateinit var buttonEnviar : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar)

        // Obtener referencia al TextView Registro
        val textViewRegistro = findViewById<TextView>(R.id.textViewRegistroRecuperar)
        // Aplicar subrayado al texto de Registro
        val textoCompleto = "¿No tienes una cuenta? Registrate"
        val spannableStringRegistro = SpannableString(textoCompleto)

        val start = textoCompleto.indexOf("Registrate")
        val end = start + "Registrate".length
        spannableStringRegistro.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringRegistro.setSpan(UnderlineSpan(),0, spannableStringRegistro.length, 0)

        textViewRegistro.text = spannableStringRegistro


        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto

        window.statusBarColor = backgroundColor

        // Configurar el click listener para el TextView de Registro
        textViewRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Configuramos la gestión para que se haga el cambio de contraseña

        editTextCorreo = findViewById(R.id.editTextUserCorreoRecuperar)
        buttonEnviar = findViewById(R.id.buttonEnviarRecuperar)
        auth = FirebaseAuth.getInstance()

        buttonEnviar.setOnClickListener {
            val correo = editTextCorreo.text.toString().trim()
            if (correo.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese su correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            enviarCorreoRecuperar(correo)
        }


    }

    // Funcioón para enviar el correo de cambio de contraseña
    private fun enviarCorreoRecuperar(correo : String){
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Correo de cambio de contraseña enviado, verifique su bandeja de entrada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al enviar correo: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}