package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Crearhabitos : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var tituloHabito: EditText
    private lateinit var descripcionHabito: EditText
    private lateinit var fechaInicioHabito: TextView
    private lateinit var habitosId: String
    private lateinit var fechaFinalizacionHabito: TextView
    private lateinit var botonCrearHabitos: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_habitos)
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

        val planpro_icon = findViewById<ImageButton>(R.id.planpro_icon)
        val logout = findViewById<ImageButton>(R.id.logout)
        val icon_profile = findViewById<ImageButton>(R.id.icon_profile)
        val icon_atras = findViewById<ImageButton>(R.id.icon_atras)
        val imgcalendario = findViewById<ImageButton>(R.id.imgcaledario)

        logout.setOnClickListener {
            toastPerzonalizado(this, "Logout exitoso")
            cerrarSesion()
        }

        icon_profile.setOnClickListener {
            startActivity(Intent(this,UsuarioActivity::class.java))

        }

        icon_atras.setOnClickListener {
            startActivity(Intent(this,Habitos::class.java))
        }
        imgcalendario.setOnClickListener { showDatePickerDialog() }

        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        tituloHabito = findViewById(R.id.titulo_creacionHabitos)
        descripcionHabito = findViewById(R.id.descripcion_crearHabitos)
        botonCrearHabitos = findViewById(R.id.crearhabitos)

        botonCrearHabitos.setOnClickListener {
            if (auth.currentUser != null) {
                saveNoteToFirestore()
            } else {
                toastPerzonalizado(this, "Por favor, inicia sesión para guardar una nota")
                // Redirigir a la actividad de inicio de sesión (si tienes una)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun cerrarSesion() {
        auth.signOut()
        irALogin()
    }

    private fun irALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    //Fragmento para calendario de Crear Habitos
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        fechaInicioHabito = findViewById<TextView>(R.id.fechacrearhabito)
        fechaInicioHabito.text = "$day/$month/$year"
    }
    //Fin fragmento para calendario de Crear Habitos


    private fun saveNoteToFirestore() {
        val titulo = tituloHabito.text.toString()
        val descripcion = descripcionHabito.text.toString()
        val fechaInicioHabito = fechaInicioHabito.text.toString()
        val user = auth.currentUser
        val correo = user?.email

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            toastPerzonalizado(this, "Completa todos los campos")
            return
        }
        val habito = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "correo" to correo,
            "fechaInicioHabito" to fechaInicioHabito
        )

        database.collection("habitos")
            .add(habito)
            .addOnSuccessListener {
                toastPerzonalizado(this, "Nota agregada correctamente")
                val intent = Intent(this, Habitos::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                toastPerzonalizado(this, "Error al agregar la nota: ${e.message}")
            }
    }

}