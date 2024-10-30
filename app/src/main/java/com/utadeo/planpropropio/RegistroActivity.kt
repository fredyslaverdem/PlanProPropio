package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity () {

    // Definimos la variable para firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

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

        // Configuración para la base de datos y funcionalidad con los botones y edittext

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val editTextCorreo = findViewById<EditText>(R.id.editTextIngresarCorreoRegistro)
        val edittextUsuario = findViewById<EditText>(R.id.editTextCrearUsernameRegistro)
        val editTextPassword = findViewById<EditText>(R.id.editTextCrearPasswordRegistro)
        val editTextConfirmarPassword = findViewById<EditText>(R.id.editTextConfirmarPasswordRegistro)
        val buttonCrearCuenta = findViewById<TextView>(R.id.buttonCrearCuentaRegistro)

        buttonCrearCuenta.setOnClickListener {
            val correo = editTextCorreo.text.toString().trim()
            val usuario = edittextUsuario.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmarPassword = editTextConfirmarPassword.text.toString().trim()

            if (correo.isEmpty() && usuario.isEmpty() && password.isEmpty() && confirmarPassword.isEmpty()) {
                toastPerzonalizado(this, "Todos los campos son obligaotrios")
                return@setOnClickListener
            }

            if (password != confirmarPassword) {
                toastPerzonalizado(this, "Las contraseñas no coinciden")
                return@setOnClickListener
            }

            if (password.length < 6) {
                toastPerzonalizado(this, "La contraseña debe tener al menos 6 caracteres")
                return@setOnClickListener
            }


            // Verificar si el usuario ya existe en la base de datos
            verificarUsuarioExistente(usuario) { existe, error ->
                if (error != null) {
                    toastPerzonalizado(this, "Error al verificar el usuario: $error")
                } else if (existe) {
                    toastPerzonalizado(this, "El usuario ya existe, elige otro nombre")
                } else {
                    // Crear el usuario en Firebase si no existe
                    auth.createUserWithEmailAndPassword(correo, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Registro exitoso
                                val userId = auth.currentUser?.uid
                                val userData = hashMapOf(
                                    "correo" to correo,
                                    "usuario" to usuario
                                )
                                userId?.let {
                                    db.collection("usuarios").document(it)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            toastPerzonalizado(this, "Registro exitoso")
                                            val intent = Intent(this, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            toastPerzonalizado(this, "Error al guardar los datos: ${e.message}")
                                        }
                                }

                            } else {
                                // Error en el registro
                                toastPerzonalizado(this, "Error en el registro: ${task.exception?.message}")
                            }
                        }
                }

            }
        }


    }
    // Función para verificar si el usuario ya existe
    private fun verificarUsuarioExistente(usuario: String, onComplete: (Boolean, String?) -> Unit) {
        db.collection("usuarios")
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener { documents ->
                onComplete(!documents.isEmpty, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }
}