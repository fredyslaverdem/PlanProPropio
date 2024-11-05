package com.utadeo.planpropropio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ListaNotasNoFinalizadas : AppCompatActivity() {
    private lateinit var firebasefirestore: FirebaseFirestore
    private lateinit var recyclerViewNotas: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var firestoreRecyclerAdapter: FirestoreRecyclerAdapter<Nota, NotaViewHolder>
    private lateinit var options: FirestoreRecyclerOptions<Nota>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas_no_finalizadas)

        recyclerViewNotas = findViewById(R.id.recyclerNoFinalizadas)
        recyclerViewNotas.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewNotas.layoutManager = linearLayoutManager

        firebasefirestore = FirebaseFirestore.getInstance()

        // Set up FirestoreRecyclerOptions
        val query = firebasefirestore.collection("notas")
        options = FirestoreRecyclerOptions.Builder<Nota>()
            .setQuery(query, Nota::class.java)
            .build()

        // Initialize the FirestoreRecyclerAdapter
        firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<Nota, NotaViewHolder>(options) {
            override fun onBindViewHolder(holder: NotaViewHolder, position: Int, model: Nota) {
                // Bind the Nota data to the ViewHolder
                holder.bind(model)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
                // Inflate the layout for each item
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
                return NotaViewHolder(view)
            }
        }

        recyclerViewNotas.adapter = firestoreRecyclerAdapter
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

