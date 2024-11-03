package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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







    }
}