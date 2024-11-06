package com.utadeo.planpropropio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class Habitos : AppCompatActivity() {
    private lateinit var firebasefirestore: FirebaseFirestore
    private lateinit var recyclerViewHabitos: RecyclerView
    private lateinit var firestoreRecyclerAdapter: FirestoreRecyclerAdapter<Habito, HabitoViewHolder>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var options: FirestoreRecyclerOptions<Habito>

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

        // Configurar RecyclerView y LayoutManager
        recyclerViewHabitos = findViewById(R.id.lista_tareas)
        recyclerViewHabitos.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewHabitos.layoutManager = linearLayoutManager

        firebasefirestore = FirebaseFirestore.getInstance()

        val query = firebasefirestore.collection("habitos")

        options = FirestoreRecyclerOptions.Builder<Habito>()
            .setQuery(query, Habito::class.java)
            .build()



        // Inicializar el adaptador de FirestoreRecyclerAdapter
        firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<Habito, HabitoViewHolder>(options) {
            override fun onBindViewHolder(holder: HabitoViewHolder, position: Int, model: Habito) {
                holder.bind(model)
                val habitoId = snapshots.getSnapshot(position).id
                toastPerzonalizado(this@Habitos, "Habito ID: $habitoId")

                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, detalles_actividad::class.java)
                    intent.putExtra("HABITO_ID", habitoId)
                    holder.itemView.context.startActivity(intent)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitoViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_habitos_creados, parent, false)
                return HabitoViewHolder(view)
            }

        }


        recyclerViewHabitos.adapter = firestoreRecyclerAdapter
    }



    override fun onStart() {
        super.onStart()
        firestoreRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        firestoreRecyclerAdapter.stopListening()
    }
}