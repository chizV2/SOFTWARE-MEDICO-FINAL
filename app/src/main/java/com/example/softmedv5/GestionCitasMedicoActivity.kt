package com.example.softmedv5

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.Medico
import com.example.softmedv5.util.GestorAutenticacion
import com.example.softmedv5.util.GestorCitas
import java.text.SimpleDateFormat
import java.util.*

class GestionCitasMedicoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonAgendaCalendario: Button
    private lateinit var buttonCitasHoy: Button
    private lateinit var buttonVolver: Button
    private lateinit var linearLayoutCitas: LinearLayout
    private lateinit var textViewSinCitas: TextView
    private lateinit var spinnerFiltroEstado: Spinner
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private var citasActuales = mutableListOf<CitaMedico>()
    private var filtroEstadoActual = "TODAS"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_citas_medico)
        
        inicializarVistas()
        configurarEventos()
        configurarSpinnerFiltro()
        cargarCitasMedico()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonAgendaCalendario = findViewById(R.id.button_agenda_calendario)
        buttonCitasHoy = findViewById(R.id.button_citas_hoy)
        buttonVolver = findViewById(R.id.button_volver)
        linearLayoutCitas = findViewById(R.id.linear_layout_citas)
        textViewSinCitas = findViewById(R.id.text_view_sin_citas)
        spinnerFiltroEstado = findViewById(R.id.spinner_filtro_estado)
        
        // Mostrar título genérico
        textViewTitulo.text = "📅 Gestión de Citas Médicas"
    }
    
    private fun configurarEventos() {
        buttonAgendaCalendario.setOnClickListener {
            mostrarAgendaCalendario()
        }
        
        buttonCitasHoy.setOnClickListener {
            mostrarCitasHoy()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun configurarSpinnerFiltro() {
        val estados = arrayOf("TODAS", "CONFIRMADA", "PENDIENTE", "COMPLETADA", "NO ASISTIÓ", "CANCELADA")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFiltroEstado.adapter = adapter
        
        spinnerFiltroEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filtroEstadoActual = estados[position]
                aplicarFiltro()
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun cargarCitasMedico() {
        citasActuales = obtenerCitasMedico().toMutableList()
        mostrarResumen(citasActuales)
        mostrarCitasEnTarjetas(citasActuales)
    }
    
    private fun obtenerCitasMedico(): List<CitaMedico> {
        // Primero intentar obtener citas del sistema real
        val citasReales = obtenerCitasDelSistemaReal()
        if (citasReales.isNotEmpty()) {
            return citasReales
        }
        
        // Si no hay citas reales, usar datos de prueba
        return obtenerCitasDePrueba()
    }
    
    private fun obtenerCitasDelSistemaReal(): List<CitaMedico> {
        try {
            val gestorCitas = GestorCitas.obtenerInstancia()
            val todasLasCitas = gestorCitas.obtenerTodasLasCitas()
            
            // Convertir a CitaMedico
            return todasLasCitas.map { cita ->
                CitaMedico(
                    id = cita.id,
                    paciente = cita.nombrePaciente.ifEmpty { "Paciente ${cita.pacienteId}" },
                    fecha = cita.obtenerFechaFormateada(),
                    hora = cita.obtenerHoraFormateada(),
                    especialidad = "Medicina General",
                    motivo = cita.opcion,
                    estado = cita.estado.name,
                    prioridad = "NORMAL", // Por defecto
                    tipo = cita.medio.descripcion
                )
            }
        } catch (e: Exception) {
            println("=== ERROR al obtener citas del sistema real: ${e.message} ===")
            return emptyList()
        }
    }
    
    private fun obtenerCitasDePrueba(): List<CitaMedico> {
        // Obtener todas las citas del sistema
        val todasLasCitas = listOf(
            CitaMedico(
                id = "CITA-001",
                paciente = "María González",
                fecha = "15/12/2024",
                hora = "09:00",
                especialidad = "Medicina General",
                motivo = "Control rutinario",
                estado = "CONFIRMADA",
                prioridad = "NORMAL",
                tipo = "PRESENCIAL"
            ),
            CitaMedico(
                id = "CITA-002",
                paciente = "Carlos Rodríguez",
                fecha = "15/12/2024",
                hora = "10:30",
                especialidad = "Cardiología",
                motivo = "Revisión de presión arterial",
                estado = "PENDIENTE",
                prioridad = "ALTA",
                tipo = "VIRTUAL"
            ),
            CitaMedico(
                id = "CITA-003",
                paciente = "Ana Martínez",
                fecha = "15/12/2024",
                hora = "14:00",
                especialidad = "Dermatología",
                motivo = "Consulta por erupción cutánea",
                estado = "CONFIRMADA",
                prioridad = "NORMAL",
                tipo = "PRESENCIAL"
            ),
            CitaMedico(
                id = "CITA-004",
                paciente = "Juan Pérez",
                fecha = "16/12/2024",
                hora = "08:30",
                especialidad = "Medicina General",
                motivo = "Seguimiento tratamiento",
                estado = "COMPLETADA",
                prioridad = "NORMAL",
                tipo = "PRESENCIAL"
            ),
            CitaMedico(
                id = "CITA-005",
                paciente = "Laura Sánchez",
                fecha = "16/12/2024",
                hora = "11:00",
                especialidad = "Ginecología",
                motivo = "Control prenatal",
                estado = "NO ASISTIÓ",
                prioridad = "ALTA",
                tipo = "PRESENCIAL"
            ),
            CitaMedico(
                id = "CITA-006",
                paciente = "Pedro López",
                fecha = "17/12/2024",
                hora = "15:00",
                especialidad = "Cardiología",
                motivo = "Electrocardiograma",
                estado = "PENDIENTE",
                prioridad = "ALTA",
                tipo = "PRESENCIAL"
            ),
            CitaMedico(
                id = "CITA-007",
                paciente = "Carmen Ruiz",
                fecha = "17/12/2024",
                hora = "16:30",
                especialidad = "Dermatología",
                motivo = "Revisión de lunares",
                estado = "CONFIRMADA",
                prioridad = "NORMAL",
                tipo = "PRESENCIAL"
            )
        )
        
        return todasLasCitas
    }
    
    private fun mostrarResumen(citas: List<CitaMedico>) {
        val totalCitas = citas.size
        val citasHoy = citas.count { it.fecha == dateFormat.format(calendar.time).split(" ")[0] }
        val citasPendientes = citas.count { it.estado == "PENDIENTE" }
        
        textViewResumen.text = "📊 Citas: $totalCitas | Hoy: $citasHoy | Pendientes: $citasPendientes"
    }
    
    private fun mostrarCitasEnTarjetas(citas: List<CitaMedico>) {
        linearLayoutCitas.removeAllViews()
        
        if (citas.isEmpty()) {
            textViewSinCitas.visibility = View.VISIBLE
            textViewSinCitas.text = "📭 No hay citas programadas"
            return
        }
        
        textViewSinCitas.visibility = View.GONE
        
        citas.forEach { cita ->
            agregarCitaTarjeta(cita)
        }
    }
    
    private fun agregarCitaTarjeta(cita: CitaMedico) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_cita_medico, linearLayoutCitas, false
        )
        
        // Configurar información de la cita
        cardView.findViewById<TextView>(R.id.text_view_paciente).text = cita.paciente
        cardView.findViewById<TextView>(R.id.text_view_fecha_hora).text = "📅 ${cita.fecha} - ⏰ ${cita.hora}"
        cardView.findViewById<TextView>(R.id.text_view_especialidad).text = "🏥 ${cita.especialidad}"
        cardView.findViewById<TextView>(R.id.text_view_motivo).text = "📝 ${cita.motivo}"
        cardView.findViewById<TextView>(R.id.text_view_tipo).text = "📍 ${cita.tipo}"
        cardView.findViewById<TextView>(R.id.text_view_prioridad).text = cita.prioridad
        
        // Configurar icono de estado
        val iconoEstado = when (cita.estado) {
            "CONFIRMADA" -> "✅"
            "PENDIENTE" -> "⏳"
            "COMPLETADA" -> "✅"
            "NO ASISTIÓ" -> "❌"
            "CANCELADA" -> "🚫"
            else -> "❓"
        }
        cardView.findViewById<TextView>(R.id.text_view_estado_icono).text = iconoEstado
        
        // Configurar botones
        cardView.findViewById<Button>(R.id.button_ver_detalles).setOnClickListener {
            mostrarDetallesCita(cita)
        }
        
        cardView.findViewById<Button>(R.id.button_completada).setOnClickListener {
            marcarCitaCompletada(cita)
        }
        
        cardView.findViewById<Button>(R.id.button_no_asistio).setOnClickListener {
            marcarCitaNoAsistio(cita)
        }
        
        linearLayoutCitas.addView(cardView)
    }
    
    private fun mostrarDetallesCita(cita: CitaMedico) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_detalles_cita, null)
        
        // Configurar información del paciente
        dialogView.findViewById<TextView>(R.id.text_view_paciente_detalle).text = "Nombre: ${cita.paciente}"
        dialogView.findViewById<TextView>(R.id.text_view_edad_paciente).text = "Edad: 35 años"
        dialogView.findViewById<TextView>(R.id.text_view_telefono_paciente).text = "Teléfono: +34 612 345 678"
        dialogView.findViewById<TextView>(R.id.text_view_email_paciente).text = "Email: ${cita.paciente.lowercase().replace(" ", ".")}@email.com"
        
        // Configurar información de la cita
        dialogView.findViewById<TextView>(R.id.text_view_fecha_detalle).text = "📅 Fecha: ${cita.fecha}"
        dialogView.findViewById<TextView>(R.id.text_view_hora_detalle).text = "⏰ Hora: ${cita.hora}"
        dialogView.findViewById<TextView>(R.id.text_view_especialidad_detalle).text = "🏥 Especialidad: ${cita.especialidad}"
        dialogView.findViewById<TextView>(R.id.text_view_motivo_detalle).text = "📝 Motivo: ${cita.motivo}"
        dialogView.findViewById<TextView>(R.id.text_view_tipo_detalle).text = "📍 Tipo: ${cita.tipo}"
        dialogView.findViewById<TextView>(R.id.text_view_prioridad_detalle).text = "⚡ Prioridad: ${cita.prioridad}"
        dialogView.findViewById<TextView>(R.id.text_view_estado_detalle).text = "✅ Estado: ${cita.estado}"
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        
        // Configurar botones del diálogo
        dialogView.findViewById<Button>(R.id.button_iniciar_atencion).setOnClickListener {
            iniciarAtencionMedica(cita)
            dialog.dismiss()
        }
        
        dialogView.findViewById<Button>(R.id.button_completada_detalle).setOnClickListener {
            marcarCitaCompletada(cita)
            dialog.dismiss()
        }
        
        dialogView.findViewById<Button>(R.id.button_no_asistio_detalle).setOnClickListener {
            marcarCitaNoAsistio(cita)
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun marcarCitaCompletada(cita: CitaMedico) {
        cita.estado = "COMPLETADA"
        mostrarMensaje("✅ Cita marcada como completada")
        aplicarFiltro()
    }
    
    private fun marcarCitaNoAsistio(cita: CitaMedico) {
        cita.estado = "NO ASISTIÓ"
        mostrarMensaje("❌ Cita marcada como no asistió")
        aplicarFiltro()
    }
    
    private fun aplicarFiltro() {
        val citasFiltradas = if (filtroEstadoActual == "TODAS") {
            citasActuales
        } else {
            citasActuales.filter { it.estado == filtroEstadoActual }
        }
        
        mostrarCitasEnTarjetas(citasFiltradas)
        mostrarResumen(citasFiltradas)
    }
    
    private fun mostrarAgendaCalendario() {
        val opciones = arrayOf("📅 Vista Diaria", "📅 Vista Semanal", "📅 Vista Mensual")
        
        AlertDialog.Builder(this)
            .setTitle("📅 Seleccionar Vista de Agenda")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarAgendaDiaria()
                    1 -> mostrarAgendaSemanal()
                    2 -> mostrarAgendaMensual()
                }
            }
            .show()
    }
    
    private fun mostrarAgendaDiaria() {
        val fechaHoy = dateFormat.format(calendar.time).split(" ")[0]
        val citasHoy = citasActuales.filter { it.fecha == fechaHoy }
        
        val mensaje = StringBuilder()
        mensaje.append("📅 Agenda del día: $fechaHoy\n\n")
        
        if (citasHoy.isEmpty()) {
            mensaje.append("No hay citas programadas para hoy")
        } else {
            citasHoy.sortedBy { it.hora }.forEach { cita ->
                mensaje.append("⏰ ${cita.hora} - ${cita.paciente}\n")
                mensaje.append("   ${cita.especialidad} - ${cita.motivo}\n")
                mensaje.append("   Estado: ${cita.estado}\n\n")
            }
        }
        
        AlertDialog.Builder(this)
            .setTitle("📅 Agenda Diaria")
            .setMessage(mensaje.toString())
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun mostrarAgendaSemanal() {
        mostrarMensaje("📅 Vista semanal - En desarrollo")
    }
    
    private fun mostrarAgendaMensual() {
        mostrarMensaje("📅 Vista mensual - En desarrollo")
    }
    
    private fun mostrarCitasHoy() {
        val fechaHoy = dateFormat.format(calendar.time).split(" ")[0]
        val citasHoy = citasActuales.filter { it.fecha == fechaHoy }
        
        if (citasHoy.isEmpty()) {
            mostrarMensaje("📭 No hay citas programadas para hoy")
        } else {
            mostrarCitasEnTarjetas(citasHoy)
            mostrarResumen(citasHoy)
        }
    }
    
    private fun iniciarAtencionMedica(cita: CitaMedico) {
        // Abrir actividad de registro de diagnóstico
        val intent = Intent(this, RegistroDiagnosticoActivity::class.java).apply {
            putExtra("PACIENTE_ID", cita.id) // Usar el ID de la cita como paciente ID temporal
            putExtra("CITA_ID", cita.id)
            putExtra("PACIENTE_NOMBRE", cita.paciente)
            putExtra("CITA_FECHA", "${cita.fecha} - ${cita.hora}")
            putExtra("MOTIVO_CONSULTA", cita.motivo)
        }
        startActivity(intent)
    }
    
    private fun reprogramarCita(cita: CitaMedico) {
        mostrarMensaje("🔄 Reprogramando cita de ${cita.paciente}")
        // Aquí se abriría el formulario de reprogramación
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}

// Clase de datos para representar una cita del médico
data class CitaMedico(
    val id: String,
    val paciente: String,
    val fecha: String,
    val hora: String,
    val especialidad: String,
    val motivo: String,
    var estado: String,
    val prioridad: String,
    val tipo: String
) 