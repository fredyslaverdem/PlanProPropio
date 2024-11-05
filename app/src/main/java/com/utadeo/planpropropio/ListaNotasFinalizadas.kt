package com.utadeo.planpropropio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListaNotasFinalizadas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas_finalizadas)

        val ButtonListaNotasNoFinalizadas2 = findViewById<Button>(R.id.ButtonListaNotasNoFinalizadas2)
        val ButtonAtrasHome2 = findViewById<ImageView>(R.id.ButtonAtrasHome2)
        val imageButtonSalirHome = findViewById<ImageButton>(R.id.imageButtonSalirHome)
        val imageButtonUsuarioHome = findViewById<ImageButton>(R.id.imageButtonUsuarioHome)

        ButtonAtrasHome2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        ButtonListaNotasNoFinalizadas2.setOnClickListener {
            val intent = Intent(this, ListaNotasNoFinalizadas::class.java)
            startActivity(intent)
        }

        imageButtonSalirHome.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        imageButtonUsuarioHome.setOnClickListener {
            val intent = Intent(this, UsuarioActivity::class.java)
            startActivity(intent)
        }


    }
}