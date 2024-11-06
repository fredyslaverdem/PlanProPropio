package com.utadeo.planpropropio

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class ListaNotasFinalizadas : AppCompatActivity() {
    private lateinit var firebasefirestore: FirebaseFirestore
    private lateinit var recyclerViewNotasFinalizadas: RecyclerView
    private lateinit var firestoreRecyclerAdapter: FirestoreRecyclerAdapter<Nota, NotaViewHolder>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var options: FirestoreRecyclerOptions<Nota>

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas_finalizadas)

        // Difiniciendo variable para el layout principlal y asi el fondo se configure con ui del dispositivo.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        // Obtener el color de fondo y aplicarlo a la barra de estado
        val backgroundColor = (mainLayout.background as? ColorDrawable)?.color
            ?: ContextCompat.getColor(this, R.color.colorFondoApp) // Color por defecto

        window.statusBarColor = backgroundColor

        val ButtonListaNotasNoFinalizadas2 = findViewById<Button>(R.id.ButtonListaNotasNoFinalizadas2)
        val ButtonAtrasHome2 = findViewById<ImageView>(R.id.ButtonAtrasHome2)
        val imageButtonSalirHome = findViewById<ImageButton>(R.id.imageButtonSalirHome)
        val imageButtonUsuarioHome = findViewById<ImageButton>(R.id.imageButtonUsuarioHome)

        imageButtonUsuarioHome.setOnClickListener {
            val intent = Intent(this, UsuarioActivity::class.java)
            startActivity(intent)
        }

        ButtonAtrasHome2.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        ButtonListaNotasNoFinalizadas2.setOnClickListener {
            val intent = Intent(this, ListaNotasNoFinalizadas::class.java)
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

        // Configurar RecyclerView y LayoutManager
        recyclerViewNotasFinalizadas = findViewById(R.id.recyclerFinalizadas)
        recyclerViewNotasFinalizadas.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewNotasFinalizadas.layoutManager = linearLayoutManager

        firebasefirestore = FirebaseFirestore.getInstance()

        // Set up FirestoreRecyclerOptions
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userEmail = user?.email
        val query = firebasefirestore.collection("notas")
            .whereEqualTo("finalizado", true)
            .whereEqualTo("correo", userEmail)


        options = FirestoreRecyclerOptions.Builder<Nota>()
            .setQuery(query, Nota::class.java)
            .build()

        // Inicializar el adaptador de FirestoreRecyclerAdapter
        firestoreRecyclerAdapter = object : FirestoreRecyclerAdapter<Nota, NotaViewHolder>(options) {
            override fun onBindViewHolder(holder: NotaViewHolder, position: Int, model: Nota) {
                holder.bind(model)
                val notaId = snapshots.getSnapshot(position).id

                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, EditarNotaActivity::class.java)
                    intent.putExtra("NOTA_ID", notaId)
                    holder.itemView.context.startActivity(intent)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
                return NotaViewHolder(view)
            }
        }

        recyclerViewNotasFinalizadas.adapter = firestoreRecyclerAdapter

    }

    // Función para cerrar sesión
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