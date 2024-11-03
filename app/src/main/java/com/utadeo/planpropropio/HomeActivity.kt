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

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto
        val botonAgregarNotas = findViewById<Button>(R.id.buttonagregarnotas)
        val imagenAgregarNotas = findViewById<ImageView>(R.id.AgregarNotas1)

        botonAgregarNotas.setOnClickListener {
            val intent = Intent(this, AgregarNotasActivity::class.java)
            startActivity(intent)
        }

        imagenAgregarNotas.setOnClickListener {
            val intent = Intent(this, AgregarNotasActivity::class.java)
            startActivity(intent)
        }
        window.statusBarColor = backgroundColor

        // Programación para el boton de salir
        val salirButton = findViewById<ImageButton>(R.id.imageButtonSalirHome)
        salirButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Programación para el boton de editar usuario
        val editarUsuarioButton = findViewById<ImageButton>(R.id.imageButtonUsuarioHome)
        editarUsuarioButton.setOnClickListener{
            val intent = Intent(this, UsuarioActivity::class.java)
            startActivity(intent)
        }

    }
}