package com.utadeo.planpropropio

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class Habitos : AppCompatActivity() {
    /*private lateinit var binding: ListaNoFinalizadoBinding
    private lateinit var listAdapter: Adaptador_lista
    private lateinit var listData: datos_lista
    var dataArrayList = ArrayList<datos_lista?>()*/
    private lateinit var firebasefirestore: FirebaseFirestore
    private lateinit var listView: ListView
    private lateinit var arrayList: ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_habitos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val logout = findViewById<ImageButton>(R.id.logout)
        val icon_profile = findViewById<ImageButton>(R.id.icon_profile)
        val icon_atras = findViewById<ImageButton>(R.id.icon_atras)
        val botonflotante = findViewById<FloatingActionButton>(R.id.botonflotante)

        icon_profile.setOnClickListener {
            startActivity(Intent(this,UsuarioActivity::class.java))
        }

        logout.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        botonflotante.setOnClickListener {
            startActivity(Intent(this,Crearhabitos::class.java))
        }

        icon_atras.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

        firebasefirestore = FirebaseFirestore.getInstance()
        val query = firebasefirestore.collection("notas")

        listView = findViewById<ListView>(R.id.lista_tareas)

        arrayAdapter = ArrayAdapter<String>(this,R.layout.activity_habitos_creados)
        listView.setAdapter(arrayAdapter)
    }
}

/*binding = ListaNoFinalizadoBinding.inflate(layoutInflater)
       setContentView(binding.root)

       val titulos = arrayOf(
           "notas SO",
           "notas emprendimiento",
           "notas apps moviles"
       )

       val descripcion = arrayOf(
           "sistemas distribuidos es chevere pero el cucho es mierda pura",
           "innovacion y empredimientos es lo mejor para el aburrimiento que mierda",
           "esta materia es chevere pero tambien es una mierda jaja"
       )

       for (i in titulos.indices) {
           listData = datos_lista(
               titulos[i],
               descripcion[i]
           )
           dataArrayList.add(listData)
       }
       listAdapter = Adaptador_lista(this@MainActivity, dataArrayList)
       binding.listaTareas.adapter = listAdapter
       binding.listaTareas.isClickable = true
       binding.listaTareas.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
           val intent = Intent(this@MainActivity, detalles_actividad::class.java)
           intent.putExtra("name", titulos[i])
           intent.putExtra("time", descripcion[i])
           startActivity(intent)
       }*/