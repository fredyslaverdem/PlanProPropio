package com.utadeo.planpropropio

import android.graphics.drawable.ColorDrawable
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ListaNotasNoFinalizadas : AppCompatActivity() {
    private lateinit var firebasefirestore: FirebaseFirestore
    private lateinit var recyclerViewNotas: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var firestoreRecyclerAdapter: FirestoreRecyclerAdapter<Nota, NotaViewHolder>
    private lateinit var options: FirestoreRecyclerOptions<Nota>

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas_no_finalizadas)

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto

        window.statusBarColor = backgroundColor


        val ButtonAtrasHome2 = findViewById<ImageView>(R.id.ButtonAtrasHome2)
        val ButtonNotasFinalizadas = findViewById<Button>(R.id.ButtonListaNotasFinalizadas1)
        val imageButtonSalirHome = findViewById<ImageButton>(R.id.imageButtonSalirHome)
        val imageButtonUsuarioHome = findViewById<ImageButton>(R.id.imageButtonUsuarioHome)

        ButtonAtrasHome2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        ButtonNotasFinalizadas.setOnClickListener {
            val intent = Intent(this, ListaNotasFinalizadas::class.java)
            startActivity(intent)
        }
        imageButtonSalirHome.setOnClickListener {
            toastPerzonalizado(this, "Logout exitoso")
            cerrarSesion()

        }
        imageButtonUsuarioHome.setOnClickListener {
            val intent = Intent(this, UsuarioActivity::class.java)
            startActivity(intent)
        }


        recyclerViewNotas = findViewById(R.id.recyclerNoFinalizadas)
        recyclerViewNotas.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewNotas.layoutManager = linearLayoutManager

        firebasefirestore = FirebaseFirestore.getInstance()

        // Set up FirestoreRecyclerOptions
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userEmail = user?.email
        val query = firebasefirestore.collection("notas")
            .whereEqualTo("finalizado", false)
            .whereEqualTo("correo", userEmail)

        options = FirestoreRecyclerOptions.Builder<Nota>()
            .setQuery(query, Nota::class.java)
            .build()

        // Initialize the FirestoreRecyclerAdapter
        firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<Nota, NotaViewHolder>(options) {
            override fun onBindViewHolder(holder: NotaViewHolder, position: Int, model: Nota) {
                // Bind the Nota data to the ViewHolder
                holder.bind(model)
                val notaId= snapshots.getSnapshot(position).id

                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, EditarNotaActivity::class.java)
                    intent.putExtra("NOTA_ID", notaId)
                    holder.itemView.context.startActivity(intent)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
                // Inflate the layout for each item
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
                return NotaViewHolder(view)
            }
        }

        recyclerViewNotas.adapter = firestoreRecyclerAdapter
    }

    // función para cerrar sesion
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

    override fun onStart() {
        super.onStart()
        firestoreRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        firestoreRecyclerAdapter.stopListening()
    }
}

