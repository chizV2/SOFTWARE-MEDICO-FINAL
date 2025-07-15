package com.example.softmedv5

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.database.SimpleDatabaseHelper
import com.example.softmedv5.modelo.*
import com.example.softmedv5.util.GestorAutenticacion
import com.example.softmedv5.util.GestorUsuarios
import java.io.File

class MainActivity : AppCompatActivity() {
    
    private lateinit var spinnerRoles: Spinner
    private lateinit var botonIngresar: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        inicializarVistas()
        configurarSpinner()
        configurarBotonIngresar()
    }
    
    private fun inicializarVistas() {
        spinnerRoles = findViewById(R.id.spinner_roles)
        botonIngresar = findViewById(R.id.boton_ingresar)
    }
    
    private fun configurarSpinner() {
        val roles = arrayOf(
            "Seleccione un rol",
            "Médico",
            "Paciente",
            "Personal Administrativo"
        )
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRoles.adapter = adapter
    }
    
    private fun configurarBotonIngresar() {
        botonIngresar.setOnClickListener {
            val rolSeleccionado = spinnerRoles.selectedItem.toString()
            
            if (rolSeleccionado == "Seleccione un rol") {
                mostrarMensaje("Por favor seleccione un rol válido")
                return@setOnClickListener
            }
            
            // Convertir el rol amigable al formato correcto para la base de datos
            val rolFormateado = when (rolSeleccionado) {
                "Médico" -> "MEDICO"
                "Paciente" -> "PACIENTE"
                "Personal Administrativo" -> "PERSONAL ADMINISTRATIVO"
                else -> rolSeleccionado.uppercase()
            }
            
            // Redirigir a la pantalla de autenticación
            abrirPantallaAutenticacion(rolFormateado)
        }
    }
    
    private fun abrirPantallaAutenticacion(rol: String) {
        val intent = Intent(this, AutenticacionActivity::class.java).apply {
            putExtra("ROL_SELECCIONADO", rol)
        }
        startActivity(intent)
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
} 