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
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    // Definimos la variables para firebase y funcionalidad de la vista
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var editTextCorreoUsuario: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonIniciarSesion: Button

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
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        editTextCorreoUsuario = findViewById(R.id.editTextUserCorreoLogin)
        editTextPassword = findViewById(R.id.editTextPasswordLogin)
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesionLogin)

        buttonIniciarSesion.setOnClickListener {
            val correoUsuario = editTextCorreoUsuario.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (correoUsuario.isEmpty() || password.isEmpty()) {
                toastPerzonalizado(this, "Completa todos los campos")
                return@setOnClickListener
            }
            // Verificamos si el campo es un correo
            if (Patterns.EMAIL_ADDRESS.matcher(correoUsuario).matches()) {
                iniciarSesionConCorreo(correoUsuario, password)
            } else {
                buscarCorreoPorUsuario(correoUsuario, password)
            }
        }


    }
    // Función para iniciar sesión con correo electrónico
    private fun iniciarSesionConCorreo(correo: String, password: String) {
        auth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    toastPerzonalizado(this, "Inicio de sesión exitoso")
                    val intent = Intent(this, CargaActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    toastPerzonalizado(this, "Error en el inicio de sesión: ${task.exception?.message}")
                }
            }
    }
    // Función para buscar el correo por el nombre de usuario
    private fun buscarCorreoPorUsuario(usuario: String, password: String) {
        db.collection("usuarios")
            .whereEqualTo("usuario", usuario) // Asegúrate de que este campo sea correcto
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Si no se encuentra el documento
                    toastPerzonalizado(this, "Usuario no encontrado")
                } else {
                    // Si se encuentra, obtener el correo
                    val correo = documents.documents[0].getString("correo")
                    if (correo != null) {
                        iniciarSesionConCorreo(correo, password)
                    } else {
                        toastPerzonalizado(this, "Error: Correo no encontrado")
                    }
                }
            }
            .addOnFailureListener { e ->
                // Manejar el error al buscar el usuario
                toastPerzonalizado(this, "Error al buscar el usuario: ${e.message}")
            }
    }

}