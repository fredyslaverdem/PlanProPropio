package com.utadeo.planpropropio

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditarNotaActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var tituloNotaE: EditText
    private lateinit var descripcionNota: EditText
    private lateinit var checkboxEditar: CheckBox
    private lateinit var buttonGuardarEditar: Button
    private lateinit var fechaRegistroE: TextView
    private lateinit var notaId: String
    private lateinit var fechaFinalizacionE: TextView
    private lateinit var ButtonBorrarNota: ImageView

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_nota)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto

        window.statusBarColor = backgroundColor

        val buttonSalirHome = findViewById<ImageButton>(R.id.imageButtonSalirHome)
        val buttonUsuarioHome = findViewById<ImageButton>(R.id.imageButtonUsuarioHome)
        val buttonAtrasHome1 = findViewById<ImageView>(R.id.ButtonAtrasHome1)

        buttonSalirHome.setOnClickListener {
            cerrarSesion()
            toastPerzonalizado(this, "Logout exitoso")
        }
        buttonUsuarioHome.setOnClickListener {
            val intent = Intent(this, UsuarioActivity::class.java)
            startActivity(intent)
        }
        buttonAtrasHome1.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        db = FirebaseFirestore.getInstance()
        tituloNotaE = findViewById(R.id.TituloNotaE)
        descripcionNota = findViewById(R.id.DescripcionNota)
        checkboxEditar = findViewById(R.id.checkboxEditar)
        buttonGuardarEditar = findViewById(R.id.buttonGuardarEditar)
        fechaRegistroE = findViewById(R.id.FechaRegistroE)
        fechaFinalizacionE = findViewById(R.id.FechaFinalizacionE)
        ButtonBorrarNota = findViewById(R.id.ButtonBorrarNota)


        this.notaId = intent.getStringExtra("NOTA_ID") ?: run {
            toastPerzonalizado(this, "Error: ID de nota no proporcionado")
            finish() // Cerrar la actividad si no se proporciona un ID
            return // Terminar el método onCreate
        }
        cargarNota(notaId)

        buttonGuardarEditar.setOnClickListener {
            guardarNota()
        }
        ButtonBorrarNota.setOnClickListener {
            eliminarNota()
        }

        auth = FirebaseAuth.getInstance()
    }

    //función para cerrar sesión
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

    private fun cargarNota(notaId: String) {
        db.collection("notas").document(notaId).get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                tituloNotaE.setText(document.getString("titulo"))
                descripcionNota.setText(document.getString("descripcion"))
                fechaRegistroE.setText(document.getString("fechaRegistro"))
                fechaFinalizacionE.setText(document.getString("fechaFinalizacion"))

                // Cargar el estado del checkbox basado en el campo 'finalizado'
                val finalizado = document.getBoolean("finalizado") ?: false
                checkboxEditar.isChecked = finalizado // Establecer el estado del checkbox
            } else {
                toastPerzonalizado(this, "Nota no encontrada")
                finish() // Opcional: Cerrar actividad si la nota no existe
            }
        }.addOnFailureListener {
            toastPerzonalizado(this, "Error al cargar la nota")
        }
    }

    private fun guardarNota() {
        val titulo = tituloNotaE.text.toString()
        val descripcion = descripcionNota.text.toString()
        val fechaRegistro = fechaRegistroE.text.toString()
        val fechaFinalizacion = fechaFinalizacionE.text.toString()
        // Verificar el estado del checkbox
        val finalizado = checkboxEditar.isChecked // Obtener el estado del checkbox
        val user = auth.currentUser
        val userEmail = user?.email

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            toastPerzonalizado(this, "Por favor complete todos los campos")
            return
        }

        val nota = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fechaRegistro" to fechaRegistro,
            "fechaFinalizacion" to fechaFinalizacion,
            "correo" to userEmail,
            "finalizado" to finalizado // Agregar el estado finalizado
        )

        db.collection("notas").document(notaId).set(nota).addOnSuccessListener {
            toastPerzonalizado(this, "Nota guardada con éxito")
            val intent = Intent(this, ListaNotasNoFinalizadas::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            toastPerzonalizado(this, "Error al guardar la nota")
        }
    }
    // Función para confirmar y eliminar la nota
    private fun eliminarNota() {
        // Crear el diálogo de confirmación
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
        builder.setMessage("¿Estás seguro de que deseas eliminar esta nota?")

        // Botón de confirmación
        builder.setPositiveButton("Sí") { dialog, _ ->
            // Proceder con la eliminación si el usuario confirma
            db.collection("notas").document(notaId).delete().addOnSuccessListener {
                toastPerzonalizado(this, "Nota eliminada con éxito")
                val intent = Intent(this, ListaNotasFinalizadas::class.java)
                startActivity(intent) // Cerrar la actividad después de eliminar la nota
            }.addOnFailureListener {
                toastPerzonalizado(this, "Error al eliminar la nota")
            }
            dialog.dismiss() // Cerrar el diálogo
        }

        // Botón de cancelación
        builder.setNegativeButton("No") { dialog, _ ->
            // Cerrar el diálogo si el usuario cancela
            dialog.dismiss()
        }

        // Mostrar el diálogo
        val dialog = builder.create()
        dialog.show()
    }
}

