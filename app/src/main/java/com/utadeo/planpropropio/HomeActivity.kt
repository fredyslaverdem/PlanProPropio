package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
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
        val botonAgregarNotas = findViewById<Button>(R.id.buttonagregarnotas)
        val imagenAgregarNotas = findViewById<ImageView>(R.id.AgregarNotas1)
        val botonlistaNotas = findViewById<Button>(R.id.buttonMisnotas)
        val imagenlistaNotas = findViewById<ImageView>(R.id.Listanotas2)
        val imagenHabitos1 = findViewById<ImageView>(R.id.Habitos1)
        val botonbuttonhabitos1 = findViewById<Button>(R.id.buttonhabitos1)

        botonlistaNotas.setOnClickListener {
            val intent = Intent(this, ListaNotasNoFinalizadas::class.java)
            startActivity(intent)
        }
        imagenlistaNotas.setOnClickListener {
            val intent = Intent(this, ListaNotasNoFinalizadas::class.java)
            startActivity(intent)
        }

        botonAgregarNotas.setOnClickListener {
            val intent = Intent(this, AgregarNotasActivity::class.java)
            startActivity(intent)
        }

        imagenAgregarNotas.setOnClickListener {
            val intent = Intent(this, AgregarNotasActivity::class.java)
            startActivity(intent)
        }

        imagenHabitos1.setOnClickListener {
            val intent = Intent(this, Habitos::class.java)
            startActivity(intent)
        }

        botonbuttonhabitos1.setOnClickListener {
            val intent = Intent(this, Habitos::class.java)
            startActivity(intent)
        }

        window.statusBarColor = backgroundColor

        // Configurar el comportamiento del botón "Salir"
        val salirButton = findViewById<ImageButton>(R.id.imageButtonSalirHome)
        salirButton.setOnClickListener {
            toastPerzonalizado(this, "Logout exitoso")
            cerrarSesion()
        }

        // Programación para el boton de editar usuario
        val editarUsuarioButton = findViewById<ImageButton>(R.id.imageButtonUsuarioHome)
        editarUsuarioButton.setOnClickListener{
            val intent = Intent(this, UsuarioActivity::class.java)
            startActivity(intent)
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
}