package com.example.softmedv5

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.Cita
import com.example.softmedv5.util.GestorAutenticacion
import com.example.softmedv5.util.GestorCitas
import com.example.softmedv5.util.ResultadoCita
import java.util.*

class MisCitasActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var linearLayoutCitas: LinearLayout
    private lateinit var textViewSinCitas: TextView
    private lateinit var buttonVolver: Button
    private lateinit var buttonNuevaCita: Button
    
    private val gestorCitas = GestorCitas.obtenerInstancia()
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_citas)
        
        inicializarVistas()
        configurarEventos()
        cargarCitas()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        linearLayoutCitas = findViewById(R.id.linear_layout_citas)
        textViewSinCitas = findViewById(R.id.text_view_sin_citas)
        buttonVolver = findViewById(R.id.button_volver)
        buttonNuevaCita = findViewById(R.id.button_nueva_cita)
        
        textViewTitulo.text = "📅 Mis Citas Médicas"
    }
    
    private fun configurarEventos() {
        buttonVolver.setOnClickListener {
            finish()
        }
        
        buttonNuevaCita.setOnClickListener {
            val intent = android.content.Intent(this, SolicitarCitaActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun cargarCitas() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion == null) {
            mostrarMensaje("Error: No hay sesión activa")
            finish()
            return
        }
        
        val pacienteId = sesion.usuario.obtenerId()
        val citas = gestorCitas.obtenerCitasPaciente(pacienteId)
        
        if (citas.isEmpty()) {
            mostrarSinCitas()
        } else {
            mostrarCitas(citas)
        }
    }
    
    private fun mostrarSinCitas() {
        textViewSinCitas.visibility = View.VISIBLE
        linearLayoutCitas.visibility = View.GONE
    }
    
    private fun mostrarCitas(citas: List<Cita>) {
        textViewSinCitas.visibility = View.GONE
        linearLayoutCitas.visibility = View.VISIBLE
        linearLayoutCitas.removeAllViews()
        
        // Separar citas futuras y pasadas
        val citasFuturas = gestorCitas.obtenerCitasProximas(citas.first().pacienteId)
        val citasPasadas = gestorCitas.obtenerCitasPasadas(citas.first().pacienteId)
        
        // Mostrar citas futuras primero
        if (citasFuturas.isNotEmpty()) {
            agregarTituloSeccion("🕐 Próximas Citas")
            citasFuturas.forEach { cita ->
                agregarCita(cita, true)
            }
        }
        
        // Mostrar citas pasadas
        if (citasPasadas.isNotEmpty()) {
            agregarTituloSeccion("📋 Historial de Citas")
            citasPasadas.forEach { cita ->
                agregarCita(cita, false)
            }
        }
    }
    
    private fun agregarTituloSeccion(titulo: String) {
        val textView = TextView(this)
        textView.text = titulo
        textView.textSize = 18f
        textView.setTypeface(null, android.graphics.Typeface.BOLD)
        textView.setTextColor(resources.getColor(com.example.softmedv5.R.color.colorPrimary, null))
        textView.setPadding(0, 32, 0, 16)
        
        linearLayoutCitas.addView(textView)
    }
    
    private fun agregarCita(cita: Cita, esFutura: Boolean) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_cita,
            linearLayoutCitas,
            false
        )
        
        // Configurar información de la cita
        val textViewInfo = cardView.findViewById<TextView>(R.id.text_view_info_cita)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado)
        val buttonCancelar = cardView.findViewById<Button>(R.id.button_cancelar)
        val buttonReprogramar = cardView.findViewById<Button>(R.id.button_reprogramar)
        
        textViewInfo.text = cita.obtenerDescripcionCompleta()
        textViewEstado.text = "Estado: ${cita.estado.descripcion}"
        
        // Configurar colores según el estado
        when (cita.estado) {
            com.example.softmedv5.modelo.EstadoCita.CONFIRMADA -> {
                textViewEstado.setTextColor(resources.getColor(com.example.softmedv5.R.color.colorSuccess, null))
            }
            com.example.softmedv5.modelo.EstadoCita.PENDIENTE -> {
                textViewEstado.setTextColor(resources.getColor(com.example.softmedv5.R.color.colorWarning, null))
            }
            com.example.softmedv5.modelo.EstadoCita.CANCELADA -> {
                textViewEstado.setTextColor(resources.getColor(com.example.softmedv5.R.color.colorError, null))
            }
            else -> {
                textViewEstado.setTextColor(resources.getColor(com.example.softmedv5.R.color.colorTextSecondary, null))
            }
        }
        
        // Configurar botones según si es futura y puede modificarse
        if (esFutura && cita.puedeCancelar()) {
            buttonCancelar.visibility = View.VISIBLE
            buttonCancelar.setOnClickListener {
                confirmarCancelarCita(cita)
            }
        } else {
            buttonCancelar.visibility = View.GONE
        }
        
        if (esFutura && cita.puedeReprogramar()) {
            buttonReprogramar.visibility = View.VISIBLE
            buttonReprogramar.setOnClickListener {
                mostrarReprogramarCita(cita)
            }
        } else {
            buttonReprogramar.visibility = View.GONE
        }
        
        linearLayoutCitas.addView(cardView)
    }
    
    private fun confirmarCancelarCita(cita: Cita) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Cancelar Cita")
            .setMessage("¿Estás seguro de que deseas cancelar esta cita?\n\n${cita.obtenerDescripcionCompleta()}")
            .setPositiveButton("Sí, Cancelar") { _, _ ->
                cancelarCita(cita)
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }
    
    private fun cancelarCita(cita: Cita) {
        val resultado = gestorCitas.cancelarCita(cita.id)
        
        when (resultado) {
            is ResultadoCita.Exito -> {
                mostrarMensaje("✅ Cita cancelada exitosamente")
                cargarCitas() // Recargar la lista
            }
            is ResultadoCita.Error -> {
                mostrarMensaje("❌ Error: ${resultado.mensaje}")
            }
        }
    }
    
    private fun mostrarReprogramarCita(cita: Cita) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_reprogramar_cita, null)
        val editTextNuevaFecha = dialogView.findViewById<EditText>(R.id.edit_text_nueva_fecha)
        val editTextNuevaHora = dialogView.findViewById<EditText>(R.id.edit_text_nueva_hora)
        
        editTextNuevaFecha.setOnClickListener {
            mostrarSelectorFecha(editTextNuevaFecha)
        }
        
        editTextNuevaHora.setOnClickListener {
            mostrarSelectorHora(editTextNuevaHora)
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Reprogramar Cita")
            .setView(dialogView)
            .setPositiveButton("Reprogramar") { _, _ ->
                val nuevaFecha = editTextNuevaFecha.text.toString()
                val nuevaHora = editTextNuevaHora.text.toString()
                
                if (nuevaFecha.isNotBlank() && nuevaHora.isNotBlank()) {
                    reprogramarCita(cita, nuevaFecha, nuevaHora)
                } else {
                    mostrarMensaje("Por favor completa todos los campos")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarSelectorFecha(editText: EditText) {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                editText.setText(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    
    private fun mostrarSelectorHora(editText: EditText) {
        // Obtener las horas disponibles del gestor de citas
        val horariosDisponibles = gestorCitas.horariosDisponibles
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Seleccionar Nueva Hora")
            .setItems(horariosDisponibles.toTypedArray()) { _, which ->
                val horaSeleccionada = horariosDisponibles[which]
                editText.setText(horaSeleccionada)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun reprogramarCita(cita: Cita, nuevaFecha: String, nuevaHora: String) {
        val resultado = gestorCitas.reprogramarCita(cita.id, nuevaFecha, nuevaHora)
        
        when (resultado) {
            is ResultadoCita.Exito -> {
                mostrarMensaje("✅ Cita reprogramada exitosamente")
                cargarCitas() // Recargar la lista
            }
            is ResultadoCita.Error -> {
                mostrarMensaje("❌ Error: ${resultado.mensaje}")
            }
        }
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarCitas() // Recargar citas cuando se regrese a esta actividad
    }
} 