package com.utadeo.planpropropio

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AgregarNotasActivity : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var fechaRegistro: TextView
    private lateinit var titulo: EditText
    private lateinit var descripcion: EditText
    private lateinit var fechaFinalizacion: TextView
    private lateinit var calendario: ImageView
    private lateinit var botonGuardarNota: Button
    private lateinit var botonAtrasHome: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_notas)

        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance() // Inicializar FirebaseAuth

        fechaRegistro = findViewById(R.id.FechaRegistro)
        titulo = findViewById(R.id.TituloNota)
        descripcion = findViewById(R.id.DescripcionNota)
        fechaFinalizacion = findViewById(R.id.FechaFinalizacion)
        calendario = findViewById(R.id.CalendarioAgregarNotas)
        botonGuardarNota = findViewById(R.id.buttonAgregarNota)
        botonAtrasHome = findViewById(R.id.ButtonAtrasHome1)

        botonAtrasHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


        val calen = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota")) // Configura el calendario a la zona horaria de Colombia
        val currentDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Bogota") // Establece la zona horaria en el formato
        }.format(calen.time)
        fechaRegistro.text = currentDate

        calendario.setOnClickListener {
            showDatePickerDialog()
        }

        botonGuardarNota.setOnClickListener {
            // Verificar autenticación antes de guardar la nota
            if (auth.currentUser != null) {
                saveNoteToFirestore()
            } else {
                toastPerzonalizado(this, "Por favor, inicia sesión para guardar una nota")
                // Redirigir a la actividad de inicio de sesión (si tienes una)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp)
        window.statusBarColor = backgroundColor
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, dayOfMonth, month, year ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                fechaFinalizacion.text = selectedDate
            },
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)

        )
        datePicker.show()
    }

    private fun saveNoteToFirestore() {
        val titulo = titulo.text.toString()
        val descripcion = descripcion.text.toString()
        val fechaRegistro = fechaRegistro.text.toString()
        val fechaFinalizacion = fechaFinalizacion.text.toString()

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            toastPerzonalizado(this, "Completa todos los campos")
            return
        }

        val note = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fechaRegistro" to fechaRegistro,
            "fechaFinalizacion" to fechaFinalizacion
        )

        database.collection("notas")
            .add(note)
            .addOnSuccessListener {
                toastPerzonalizado(this, "Nota agregada correctamente")
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                toastPerzonalizado(this, "Error al agregar la nota: ${e.message}")
            }
    }
}
