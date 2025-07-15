package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.Cita
import com.example.softmedv5.modelo.EstadoCita
import com.example.softmedv5.util.GestorAutenticacion
import com.example.softmedv5.util.GestorCitas
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlertasCitasActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var linearLayoutAlertas: LinearLayout
    private lateinit var linearLayoutCitas: LinearLayout
    private lateinit var textViewSinCitas: TextView
    private lateinit var buttonVerTodasCitas: Button
    private lateinit var buttonNuevaCita: Button
    private lateinit var buttonVolver: Button
    
    private val gestorCitas = GestorCitas.obtenerInstancia()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alertas_citas)
        
        inicializarVistas()
        configurarEventos()
        cargarAlertasYCitas()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        linearLayoutAlertas = findViewById(R.id.linear_layout_alertas)
        linearLayoutCitas = findViewById(R.id.linear_layout_citas)
        textViewSinCitas = findViewById(R.id.text_view_sin_citas)
        buttonVerTodasCitas = findViewById(R.id.button_ver_todas_citas)
        buttonNuevaCita = findViewById(R.id.button_nueva_cita)
        buttonVolver = findViewById(R.id.button_volver)
        
        textViewTitulo.text = "🔔 Alertas y Mis Citas"
    }
    
    private fun configurarEventos() {
        buttonVerTodasCitas.setOnClickListener {
            val intent = Intent(this, MisCitasActivity::class.java)
            startActivity(intent)
        }
        
        buttonNuevaCita.setOnClickListener {
            val intent = Intent(this, SolicitarCitaActivity::class.java)
            startActivity(intent)
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun cargarAlertasYCitas() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion == null) {
            mostrarMensaje("Error: No hay sesión activa")
            finish()
            return
        }
        
        val pacienteId = sesion.usuario.obtenerId()
        val citas = gestorCitas.obtenerCitasPaciente(pacienteId)
        val citasProximas = gestorCitas.obtenerCitasProximas(pacienteId)
        
        // Mostrar resumen
        mostrarResumen(citas, citasProximas)
        
        // Mostrar alertas
        mostrarAlertas(citasProximas)
        
        // Mostrar próximas citas
        if (citasProximas.isNotEmpty()) {
            mostrarCitasProximas(citasProximas)
        } else {
            mostrarSinCitas()
        }
    }
    
    private fun mostrarResumen(citas: List<Cita>, citasProximas: List<Cita>) {
        val totalCitas = citas.size
        val citasConfirmadas = citas.count { it.estado == EstadoCita.CONFIRMADA }
        val citasPendientes = citas.count { it.estado == EstadoCita.PENDIENTE }
        val citasHoy = citasProximas.count { 
            it.fechaHora.toLocalDate() == LocalDateTime.now().toLocalDate() 
        }
        
        val resumen = """
            📊 Resumen de Citas:
            
            📅 Total de citas: $totalCitas
            ✅ Confirmadas: $citasConfirmadas
            ⏳ Pendientes: $citasPendientes
            🕐 Citas hoy: $citasHoy
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun mostrarAlertas(citasProximas: List<Cita>) {
        linearLayoutAlertas.removeAllViews()
        
        val alertas = mutableListOf<String>()
        
        // Alerta para citas hoy
        val citasHoy = citasProximas.filter { 
            it.fechaHora.toLocalDate() == LocalDateTime.now().toLocalDate() 
        }
        if (citasHoy.isNotEmpty()) {
            alertas.add("📅 Tienes ${citasHoy.size} cita(s) programada(s) para hoy")
        }
        
        // Alerta para citas mañana
        val citasManana = citasProximas.filter { 
            it.fechaHora.toLocalDate() == LocalDateTime.now().plusDays(1).toLocalDate() 
        }
        if (citasManana.isNotEmpty()) {
            alertas.add("📅 Tienes ${citasManana.size} cita(s) programada(s) para mañana")
        }
        
        // Alerta para citas pendientes de confirmación
        val citasPendientes = citasProximas.filter { it.estado == EstadoCita.PENDIENTE }
        if (citasPendientes.isNotEmpty()) {
            alertas.add("⏳ Tienes ${citasPendientes.size} cita(s) pendiente(s) de confirmación")
        }
        
        // Alerta para citas próximas (próximos 3 días)
        val citasProximos3Dias = citasProximas.filter { 
            it.fechaHora.isBefore(LocalDateTime.now().plusDays(3)) 
        }
        if (citasProximos3Dias.isNotEmpty()) {
            alertas.add("🔔 Tienes ${citasProximos3Dias.size} cita(s) en los próximos 3 días")
        }
        
        if (alertas.isEmpty()) {
            val textView = TextView(this)
            textView.text = "✅ No hay alertas pendientes"
            textView.textSize = 16f
            textView.setTextColor(resources.getColor(R.color.colorSuccess, null))
            textView.setPadding(0, 16, 0, 16)
            linearLayoutAlertas.addView(textView)
        } else {
            alertas.forEach { alerta ->
                val textView = TextView(this)
                textView.text = "🔔 $alerta"
                textView.textSize = 14f
                textView.setTextColor(resources.getColor(R.color.colorWarning, null))
                textView.setPadding(0, 8, 0, 8)
                linearLayoutAlertas.addView(textView)
            }
        }
    }
    
    private fun mostrarCitasProximas(citasProximas: List<Cita>) {
        textViewSinCitas.visibility = View.GONE
        linearLayoutCitas.visibility = View.VISIBLE
        linearLayoutCitas.removeAllViews()
        
        // Mostrar solo las próximas 3 citas
        val citasAMostrar = citasProximas.take(3)
        
        citasAMostrar.forEach { cita ->
            agregarCitaResumen(cita)
        }
        
        if (citasProximas.size > 3) {
            val textView = TextView(this)
            textView.text = "... y ${citasProximas.size - 3} cita(s) más"
            textView.textSize = 14f
            textView.setTextColor(resources.getColor(R.color.colorTextSecondary, null))
            textView.setPadding(0, 16, 0, 0)
            linearLayoutCitas.addView(textView)
        }
    }
    
    private fun agregarCitaResumen(cita: Cita) {
        val cardView = layoutInflater.inflate(R.layout.item_cita_resumen, linearLayoutCitas, false)
        
        val textViewInfo = cardView.findViewById<TextView>(R.id.text_view_info_cita)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado)
        val textViewTiempo = cardView.findViewById<TextView>(R.id.text_view_tiempo)
        
        // Información básica de la cita
        val info = """
            📅 Cita #${cita.id.takeLast(8)}
            📋 ${cita.especialidad}
            🕐 ${cita.obtenerFechaHoraCompleta()}
            💻 ${cita.medio.descripcion}
        """.trimIndent()
        
        textViewInfo.text = info
        textViewEstado.text = "Estado: ${cita.estado.descripcion}"
        
        // Calcular tiempo restante
        val tiempoRestante = calcularTiempoRestante(cita.fechaHora)
        textViewTiempo.text = tiempoRestante
        
        // Configurar colores según el estado
        when (cita.estado) {
            EstadoCita.CONFIRMADA -> {
                textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
            }
            EstadoCita.PENDIENTE -> {
                textViewEstado.setTextColor(resources.getColor(R.color.colorWarning, null))
            }
            else -> {
                textViewEstado.setTextColor(resources.getColor(R.color.colorTextSecondary, null))
            }
        }
        
        linearLayoutCitas.addView(cardView)
    }
    
    private fun calcularTiempoRestante(fechaHora: LocalDateTime): String {
        val ahora = LocalDateTime.now()
        val diferencia = java.time.Duration.between(ahora, fechaHora)
        
        return when {
            diferencia.toDays() > 0 -> "⏰ En ${diferencia.toDays()} día(s)"
            diferencia.toHours() > 0 -> "⏰ En ${diferencia.toHours()} hora(s)"
            diferencia.toMinutes() > 0 -> "⏰ En ${diferencia.toMinutes()} minuto(s)"
            else -> "⏰ ¡Es ahora!"
        }
    }
    
    private fun mostrarSinCitas() {
        textViewSinCitas.visibility = View.VISIBLE
        linearLayoutCitas.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarAlertasYCitas() // Recargar cuando se regrese a esta actividad
    }
} 