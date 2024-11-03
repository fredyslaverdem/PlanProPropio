package com.utadeo.planpropropio

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class UsuarioActivity : AppCompatActivity() {

    // Declaración de variables
    private lateinit var imgFotoPerfil: ImageView
    private lateinit var edtNombres: EditText
    private lateinit var edtApellidos: EditText
    private lateinit var edtCorreo: EditText
    private lateinit var edtCelular: EditText
    private lateinit var btnGuardarCambios: Button
    private lateinit var textViewNombreUsuario: TextView

    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid
    private var correoActual: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuario)

        // Configurar el comportamiento del botón "Atrás"
        val atrasButton = findViewById<ImageButton>(R.id.imageButtonAtras)
        atrasButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Configurar el comportamiento del botón "Salir"
        val salirButton = findViewById<ImageButton>(R.id.imageButtonSalirUsuario)
        salirButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Funcionalidad para que se muestre el nombre de usuario
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario)
        cargarNombreUsuario()

        // Inicializar las vistas
        imgFotoPerfil = findViewById(R.id.imageViewFotoUsuario)
        edtNombres = findViewById(R.id.editTextNombreUsuario)
        edtApellidos = findViewById(R.id.edittextApellidoUsuario)
        edtCorreo = findViewById(R.id.editTextCorreoUsuario)
        edtCelular = findViewById(R.id.editTextTelefonoUsuario)
        btnGuardarCambios = findViewById(R.id.buttonGuardarUsuario)

        // Cargar datos del usuario
        cargarDatosUsuario()


        // Click en guardar cambios
        btnGuardarCambios.setOnClickListener {
            guardarCambiosUsuario()
        }
    }

    // Función para cargar el nombre de usuario
    private fun cargarNombreUsuario() {
        val usuarioId = FirebaseAuth.getInstance().currentUser?.uid

        if (usuarioId != null) {
            val usuarioRef = FirebaseFirestore.getInstance().collection("usuarios").document(usuarioId)
            usuarioRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val nombreUsuario = document.getString("usuario") ?: "Usuario desconocido"
                    textViewNombreUsuario.text = nombreUsuario
                } else {
                    textViewNombreUsuario.text = getString(R.string.predeterminadousuario)
                }
            }.addOnFailureListener { e ->
                textViewNombreUsuario.text = "Error al cargar usuario: ${e.message}"
            }
        } else {
            textViewNombreUsuario.text = "No hay sesión iniciada"
        }
    }

    // Función para cargar los datos del usuario
    private fun cargarDatosUsuario() {
        userId?.let { id ->
            db.collection("usuarios").document(id).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        edtNombres.setText(document.getString("nombres"))
                        edtApellidos.setText(document.getString("apellidos"))
                        edtCorreo.setText(document.getString("correo"))
                        edtCelular.setText(document.getString("celular"))
                        correoActual = document.getString("correo")

                    }
                }
                .addOnFailureListener { e ->
                    toastPerzonalizado(this, "Error al cargar los datos del usuario: ${e.message}")
                }
        }
    }

    // Función para guardar los cambios
    private fun guardarCambiosUsuario() {
        val nombres = edtNombres.text.toString()
        val apellidos = edtApellidos.text.toString()
        val correoNuevo = edtCorreo.text.toString()
        val celular = edtCelular.text.toString()

        if (nombres.isEmpty() || apellidos.isEmpty() || correoNuevo.isEmpty() || celular.isEmpty()) {
            toastPerzonalizado(this, "Por favor complete todos los campos")
            return
        }

        // Actualizar los datos del usuario en Firestore
        val usuarioData = mapOf(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "correo" to correoNuevo,
            "celular" to celular
        )

        userId?.let { id ->
            db.collection("usuarios").document(id).update(usuarioData)
                .addOnSuccessListener {
                    if (correoNuevo != correoActual) {
                        actualizarCorreoAutenticacion(correoNuevo)
                    }  else {
                        toastPerzonalizado(this, "Cambios guardados exitosamente")
                    }
                }
        }
    }

    // Función para actualizar el correo de autenticación
    private fun actualizarCorreoAutenticacion(nuevoCorreo: String) {
        val user = auth.currentUser
        user?.let {
            it.updateEmail(nuevoCorreo)
                .addOnSuccessListener {
                    toastPerzonalizado(this, "Correo actualizado exitosamente")

                }
                .addOnFailureListener { e ->
                    toastPerzonalizado(this, "Error al actualizar el correo: ${e.message}")
                }
        }
    }
}
