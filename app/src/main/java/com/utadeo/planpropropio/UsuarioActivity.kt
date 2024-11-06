package com.utadeo.planpropropio

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
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
    private lateinit var edtCorreo: TextView
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

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto

        window.statusBarColor = backgroundColor

        // Configurar el comportamiento del botón "Atrás"
        val atrasButton = findViewById<ImageButton>(R.id.imageButtonAtras)
        atrasButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Configurar el comportamiento del botón "Salir"
        val salirButton = findViewById<ImageButton>(R.id.imageButtonSalirUsuario)
        salirButton.setOnClickListener {
            toastPerzonalizado(this, "Logout exitoso")
            cerrarSesion()
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

        // Click en cambiar nombre de usuario
        val btnCambiarNombreDeUsuario = findViewById<ImageButton>(R.id.imageButtonCambiarUsuario)
        btnCambiarNombreDeUsuario.setOnClickListener {
            mostrarDialogoCambiarNombre()
        }
    }

    // funcion para cerrar sesión
    private fun cerrarSesion() {
        auth.signOut()
        irPantallaInicio()
    }

    // función para redirigir a la pantalla de inicio de sesión
    private fun irPantallaInicio() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
                        edtCorreo.text = document.getString("correo")
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
        val nombres = edtNombres.text.toString().trim()
        val apellidos = edtApellidos.text.toString().trim()
        val celular = edtCelular.text.toString().trim()

        if (nombres.isEmpty() || apellidos.isEmpty() || celular.isEmpty()) {
            toastPerzonalizado(this, "Por favor complete todos los campos")
            return
        }

        // Actualizar los datos del usuario en Firestore
        val usuarioData = mapOf(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "celular" to celular
        )

        userId?.let { id ->
            db.collection("usuarios").document(id).update(usuarioData)
                .addOnSuccessListener {
                    toastPerzonalizado(this, "Cambios guardados exitosamente")

                }
        }
    }

    // funciones para cambiar usuario
    private fun mostrarDialogoCambiarNombre() {
        val dialogView= layoutInflater.inflate(R.layout.dialog_edit_username, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Cambiar nombre de usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar") {dialog, _ ->
                val nuevoNombre = dialogView.findViewById<EditText>(R.id.etNuevoNombre).text.toString().trim()
                if (nuevoNombre.isNotEmpty()) {
                    actualizarNombreUsuario(nuevoNombre)
                } else {
                    toastPerzonalizado(this, "El nombre no puede estar vacio")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") {dialog, _ -> dialog.dismiss()}
        dialogBuilder.create().show()
    }

    private fun actualizarNombreUsuario(nuevoNombre: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("usuarios").document(userId).update("usuario", nuevoNombre)
                .addOnSuccessListener {
                    toastPerzonalizado(this, "Nombre de usuario actualizado")
                }
                .addOnFailureListener { e ->
                    toastPerzonalizado(this, "Error: ${e.message}")
                }
        } else {
            toastPerzonalizado(this, "Usuario no autenticado")
        }
    }
}
