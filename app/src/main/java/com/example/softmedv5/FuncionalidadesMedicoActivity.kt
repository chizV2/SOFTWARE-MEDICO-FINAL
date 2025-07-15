package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.util.GestorAutenticacion

class FuncionalidadesMedicoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewBienvenida: TextView
    private lateinit var buttonGestionCitas: Button
    private lateinit var buttonPrescripcionOrdenes: Button
    private lateinit var buttonRevisionResultados: Button
    private lateinit var buttonHistorialClinico: Button
    private lateinit var buttonComunicacionSeguimiento: Button
    private lateinit var buttonVolver: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionalidades_medico)
        
        inicializarVistas()
        configurarEventos()
        mostrarInformacionMedico()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewBienvenida = findViewById(R.id.text_view_bienvenida)
        buttonGestionCitas = findViewById(R.id.button_gestion_citas)
        buttonPrescripcionOrdenes = findViewById(R.id.button_prescripcion_ordenes)
        buttonRevisionResultados = findViewById(R.id.button_revision_resultados)
        buttonHistorialClinico = findViewById(R.id.button_historial_clinico)
        buttonComunicacionSeguimiento = findViewById(R.id.button_comunicacion_seguimiento)
        buttonVolver = findViewById(R.id.button_volver)
    }
    
    private fun configurarEventos() {
        buttonGestionCitas.setOnClickListener {
            abrirGestionCitas()
        }
        
        buttonPrescripcionOrdenes.setOnClickListener {
            abrirPrescripcionOrdenes()
        }
        
        buttonRevisionResultados.setOnClickListener {
            abrirRevisionResultados()
        }
        
        buttonHistorialClinico.setOnClickListener {
            abrirHistorialClinico()
        }
        
        buttonComunicacionSeguimiento.setOnClickListener {
            abrirComunicacionSeguimiento()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun mostrarInformacionMedico() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val medico = sesion.usuario
            
            textViewTitulo.text = "👨‍⚕️ Funcionalidades del Médico"
            textViewBienvenida.text = "Bienvenido Dr. ${medico.obtenerNombre()}\n\nSelecciona la funcionalidad médica que deseas utilizar:"
        } else {
            mostrarMensaje("Error: No hay sesión activa")
            finish()
        }
    }
    
    private fun abrirGestionCitas() {
        val intent = Intent(this, GestionCitasMedicoActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirPrescripcionOrdenes() {
        val intent = Intent(this, PrescripcionOrdenesActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirRevisionResultados() {
        val intent = Intent(this, RevisionResultadosActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirHistorialClinico() {
        val intent = Intent(this, HistorialClinicoActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirComunicacionSeguimiento() {
        val intent = Intent(this, ChatUnificadoActivity::class.java)
        startActivity(intent)
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
} 