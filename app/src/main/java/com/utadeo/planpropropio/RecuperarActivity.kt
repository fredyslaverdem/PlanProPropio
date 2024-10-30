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
import com.google.firebase.firestore.FirebaseFirestore


class RecuperarActivity : AppCompatActivity() {

    // Definimos variables para gestion del cambio

    private lateinit var editTextCorreo : EditText
    private lateinit var buttonEnviar : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

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
        db = FirebaseFirestore.getInstance()

        buttonEnviar.setOnClickListener {
            val correo = editTextCorreo.text.toString().trim()
            if (correo.isEmpty()) {
                // Toast personalizado monstrando el nensaje de correo vacío
                toastPerzonalizado(this, getString(R.string.ingresarcorreo))
            } else {
                verificarCorreoRegistrado(correo) { correoExiste ->
                    if (correoExiste) {
                        enviarCorreoRecuperar(correo)
                    } else {
                        toastPerzonalizado(this, "El correo es Incorrecto o no esta Registrado")
                    }
                }
            }
        }
    }

    // Funcioón para enviar el correo de cambio de contraseña
    private fun enviarCorreoRecuperar(correo : String){
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Toast personalizado mostrando el mensaje de correo enviado
                    toastPerzonalizado(this, getString(R.string.correoenviadorecuperar))

                    // Ir a la vista de login
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    toastPerzonalizado(this, "Error al enviar correo: ${task.exception?.message}")
                }
            }
    }

    // Función para vefificar si el usuario existe en la base de datos
    private fun verificarCorreoRegistrado(correo: String, onResultado: (Boolean) -> Unit){
        // Consultamos si existe en firestore
        db.collection("usuarios")
            .whereEqualTo("correo", correo)
            .get()
            .addOnSuccessListener { documents ->
                // Si encuentra almenos un documento el correo existe
                onResultado(!documents.isEmpty)
            }
            .addOnFailureListener { e ->
                // Manejar el error al buscar el usuario
                toastPerzonalizado(this, "Error al verificar el correo: ${e.message}")
                onResultado(false)
            }
    }
}