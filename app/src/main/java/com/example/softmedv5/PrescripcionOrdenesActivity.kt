package com.example.softmedv5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class PrescripcionOrdenesActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonNuevaPrescripcion: Button
    private lateinit var buttonNuevaOrden: Button
    private lateinit var buttonVerPrescripciones: Button
    private lateinit var buttonVerOrdenes: Button
    private lateinit var buttonRenovarPrescripcion: Button
    private lateinit var buttonVolver: Button
    private lateinit var linearLayoutPrescripciones: LinearLayout
    private lateinit var textViewSinPrescripciones: TextView
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescripcion_ordenes)
        
        inicializarVistas()
        configurarEventos()
        cargarPrescripcionesOrdenes()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonNuevaPrescripcion = findViewById(R.id.button_nueva_prescripcion)
        buttonNuevaOrden = findViewById(R.id.button_nueva_orden)
        buttonVerPrescripciones = findViewById(R.id.button_ver_prescripciones)
        buttonVerOrdenes = findViewById(R.id.button_ver_ordenes)
        buttonRenovarPrescripcion = findViewById(R.id.button_renovar_prescripcion)
        buttonVolver = findViewById(R.id.button_volver)
        linearLayoutPrescripciones = findViewById(R.id.linear_layout_prescripciones)
        textViewSinPrescripciones = findViewById(R.id.text_view_sin_prescripciones)
        
        textViewTitulo.text = "💊 Prescripción y Órdenes Médicas"
    }
    
    private fun configurarEventos() {
        buttonNuevaPrescripcion.setOnClickListener {
            mostrarFormularioNuevaPrescripcion()
        }
        
        buttonNuevaOrden.setOnClickListener {
            mostrarFormularioNuevaOrden()
        }
        
        buttonVerPrescripciones.setOnClickListener {
            mostrarPrescripciones()
        }
        
        buttonVerOrdenes.setOnClickListener {
            mostrarOrdenes()
        }
        
        buttonRenovarPrescripcion.setOnClickListener {
            mostrarFormularioRenovarPrescripcion()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun cargarPrescripcionesOrdenes() {
        val prescripciones = obtenerPrescripcionesOrdenes()
        
        mostrarResumen(prescripciones)
        
        if (prescripciones.isNotEmpty()) {
            prescripciones.forEach { prescripcion ->
                agregarPrescripcionALista(prescripcion)
            }
        } else {
            mostrarSinPrescripciones()
        }
    }
    
    private fun obtenerPrescripcionesOrdenes(): List<PrescripcionOrden> {
        return listOf(
            PrescripcionOrden(
                id = "PRESC-001",
                paciente = "María González",
                fecha = "15/12/2024",
                tipo = "PRESCRIPCIÓN",
                medicamento = "Paracetamol 500mg",
                dosis = "1 tableta cada 8 horas",
                duracion = "7 días",
                indicaciones = "Para dolor de cabeza",
                estado = "ACTIVA",
                prioridad = "NORMAL"
            ),
            PrescripcionOrden(
                id = "ORDEN-001",
                paciente = "Carlos Rodríguez",
                fecha = "14/12/2024",
                tipo = "ORDEN",
                medicamento = "Hemograma completo",
                dosis = "1 vez",
                duracion = "Inmediato",
                indicaciones = "Para control de presión arterial",
                estado = "PENDIENTE",
                prioridad = "ALTA"
            ),
            PrescripcionOrden(
                id = "PRESC-002",
                paciente = "Ana Martínez",
                fecha = "13/12/2024",
                tipo = "PRESCRIPCIÓN",
                medicamento = "Ibuprofeno 400mg",
                dosis = "1 tableta cada 6 horas",
                duracion = "5 días",
                indicaciones = "Para inflamación",
                estado = "VENCIDA",
                prioridad = "NORMAL"
            ),
            PrescripcionOrden(
                id = "ORDEN-002",
                paciente = "Juan Pérez",
                fecha = "12/12/2024",
                tipo = "ORDEN",
                medicamento = "Radiografía de tórax",
                dosis = "1 vez",
                duracion = "Inmediato",
                indicaciones = "Para evaluación de síntomas respiratorios",
                estado = "COMPLETADA",
                prioridad = "ALTA"
            )
        )
    }
    
    private fun mostrarResumen(prescripciones: List<PrescripcionOrden>) {
        val totalPrescripciones = prescripciones.count { it.tipo == "PRESCRIPCIÓN" }
        val totalOrdenes = prescripciones.count { it.tipo == "ORDEN" }
        val activas = prescripciones.count { it.estado == "ACTIVA" }
        val pendientes = prescripciones.count { it.estado == "PENDIENTE" }
        val vencidas = prescripciones.count { it.estado == "VENCIDA" }
        
        val resumen = """
            💊 Resumen de Prescripciones y Órdenes:
            
            💊 Prescripciones: $totalPrescripciones
            📋 Órdenes médicas: $totalOrdenes
            ✅ Activas: $activas
            ⏳ Pendientes: $pendientes
            ⏰ Vencidas: $vencidas
            
            💡 Funcionalidades disponibles:
            • Crear nueva prescripción
            • Crear nueva orden médica
            • Ver prescripciones activas
            • Ver órdenes pendientes
            • Renovar prescripciones
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun agregarPrescripcionALista(prescripcion: PrescripcionOrden) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_prescripcion_orden,
            linearLayoutPrescripciones,
            false
        )
        
        val textViewId = cardView.findViewById<TextView>(R.id.text_view_id_prescripcion)
        val textViewPaciente = cardView.findViewById<TextView>(R.id.text_view_paciente_prescripcion)
        val textViewFecha = cardView.findViewById<TextView>(R.id.text_view_fecha_prescripcion)
        val textViewTipo = cardView.findViewById<TextView>(R.id.text_view_tipo_prescripcion)
        val textViewMedicamento = cardView.findViewById<TextView>(R.id.text_view_medicamento_prescripcion)
        val textViewDosis = cardView.findViewById<TextView>(R.id.text_view_dosis_prescripcion)
        val textViewDuracion = cardView.findViewById<TextView>(R.id.text_view_duracion_prescripcion)
        val textViewIndicaciones = cardView.findViewById<TextView>(R.id.text_view_indicaciones_prescripcion)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado_prescripcion)
        val textViewPrioridad = cardView.findViewById<TextView>(R.id.text_view_prioridad_prescripcion)
        val buttonVerDetalle = cardView.findViewById<Button>(R.id.button_ver_detalle_prescripcion)
        val buttonRenovar = cardView.findViewById<Button>(R.id.button_renovar_prescripcion_item)
        val buttonCancelar = cardView.findViewById<Button>(R.id.button_cancelar_prescripcion_item)
        
        textViewId.text = "${prescripcion.tipo}: ${prescripcion.id}"
        textViewPaciente.text = "👤 ${prescripcion.paciente}"
        textViewFecha.text = "📅 ${prescripcion.fecha}"
        textViewTipo.text = if (prescripcion.tipo == "PRESCRIPCIÓN") "💊 Prescripción" else "📋 Orden"
        textViewMedicamento.text = "💊 ${prescripcion.medicamento}"
        textViewDosis.text = "📏 ${prescripcion.dosis}"
        textViewDuracion.text = "⏰ ${prescripcion.duracion}"
        textViewIndicaciones.text = "📝 ${prescripcion.indicaciones}"
        
        // Configurar estado con colores
        when (prescripcion.estado) {
            "ACTIVA" -> {
                textViewEstado.text = "✅ Activa"
                textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
            }
            "PENDIENTE" -> {
                textViewEstado.text = "⏳ Pendiente"
                textViewEstado.setTextColor(resources.getColor(R.color.colorWarning, null))
            }
            "VENCIDA" -> {
                textViewEstado.text = "⏰ Vencida"
                textViewEstado.setTextColor(resources.getColor(R.color.colorError, null))
            }
            "COMPLETADA" -> {
                textViewEstado.text = "✅ Completada"
                textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
            }
            else -> {
                textViewEstado.text = "❌ Cancelada"
                textViewEstado.setTextColor(resources.getColor(R.color.colorError, null))
            }
        }
        
        // Configurar prioridad con colores
        when (prescripcion.prioridad) {
            "CRÍTICA" -> {
                textViewPrioridad.text = "🚨 Crítica"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorError, null))
            }
            "ALTA" -> {
                textViewPrioridad.text = "⚠️ Alta"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorWarning, null))
            }
            "NORMAL" -> {
                textViewPrioridad.text = "📋 Normal"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorSuccess, null))
            }
            else -> {
                textViewPrioridad.text = "📋 Baja"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorTextSecondary, null))
            }
        }
        
        buttonVerDetalle.setOnClickListener {
            mostrarDetallePrescripcion(prescripcion)
        }
        
        buttonRenovar.setOnClickListener {
            renovarPrescripcion(prescripcion)
        }
        
        buttonCancelar.setOnClickListener {
            cancelarPrescripcion(prescripcion)
        }
        
        linearLayoutPrescripciones.addView(cardView)
    }
    
    private fun mostrarFormularioNuevaPrescripcion() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_prescripcion, null)
        
        val editTextPaciente = dialogView.findViewById<EditText>(R.id.edit_text_paciente_prescripcion)
        val editTextMedicamento = dialogView.findViewById<EditText>(R.id.edit_text_medicamento_prescripcion)
        val editTextDosis = dialogView.findViewById<EditText>(R.id.edit_text_dosis_prescripcion)
        val editTextDuracion = dialogView.findViewById<EditText>(R.id.edit_text_duracion_prescripcion)
        val editTextIndicaciones = dialogView.findViewById<EditText>(R.id.edit_text_indicaciones_prescripcion)
        val spinnerPrioridad = dialogView.findViewById<Spinner>(R.id.spinner_prioridad_prescripcion)
        
        // Configurar spinner de prioridad
        val prioridades = arrayOf("Normal", "Alta", "Crítica")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("💊 Nueva Prescripción")
            .setView(dialogView)
            .setPositiveButton("Crear Prescripción") { _, _ ->
                val paciente = editTextPaciente.text.toString()
                val medicamento = editTextMedicamento.text.toString()
                val dosis = editTextDosis.text.toString()
                val duracion = editTextDuracion.text.toString()
                val indicaciones = editTextIndicaciones.text.toString()
                val prioridad = spinnerPrioridad.selectedItem.toString()
                
                if (paciente.isNotBlank() && medicamento.isNotBlank() && dosis.isNotBlank() && duracion.isNotBlank()) {
                    crearNuevaPrescripcion(paciente, medicamento, dosis, duracion, indicaciones, prioridad)
                } else {
                    mostrarMensaje("Por favor completa todos los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarFormularioNuevaOrden() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_orden, null)
        
        val editTextPaciente = dialogView.findViewById<EditText>(R.id.edit_text_paciente_orden)
        val editTextExamen = dialogView.findViewById<EditText>(R.id.edit_text_examen_orden)
        val editTextIndicaciones = dialogView.findViewById<EditText>(R.id.edit_text_indicaciones_orden)
        val spinnerPrioridad = dialogView.findViewById<Spinner>(R.id.spinner_prioridad_orden)
        
        // Configurar spinner de prioridad
        val prioridades = arrayOf("Normal", "Alta", "Crítica")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📋 Nueva Orden Médica")
            .setView(dialogView)
            .setPositiveButton("Crear Orden") { _, _ ->
                val paciente = editTextPaciente.text.toString()
                val examen = editTextExamen.text.toString()
                val indicaciones = editTextIndicaciones.text.toString()
                val prioridad = spinnerPrioridad.selectedItem.toString()
                
                if (paciente.isNotBlank() && examen.isNotBlank()) {
                    crearNuevaOrden(paciente, examen, indicaciones, prioridad)
                } else {
                    mostrarMensaje("Por favor completa todos los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarPrescripciones() {
        val prescripciones = obtenerPrescripcionesOrdenes()
        val prescripcionesActivas = prescripciones.filter { it.tipo == "PRESCRIPCIÓN" && it.estado == "ACTIVA" }
        
        if (prescripcionesActivas.isNotEmpty()) {
            val mensaje = """
                💊 Prescripciones Activas:
                
                ${prescripcionesActivas.joinToString("\n\n") { prescripcion ->
                    "👤 ${prescripcion.paciente}\n" +
                    "💊 ${prescripcion.medicamento}\n" +
                    "📏 ${prescripcion.dosis}\n" +
                    "⏰ ${prescripcion.duracion}\n" +
                    "📝 ${prescripcion.indicaciones}\n" +
                    "📅 ${prescripcion.fecha}"
                }}
            """.trimIndent()
            
            android.app.AlertDialog.Builder(this)
                .setTitle("💊 Prescripciones Activas")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar") { _, _ -> }
                .show()
        } else {
            mostrarMensaje("No hay prescripciones activas")
        }
    }
    
    private fun mostrarOrdenes() {
        val prescripciones = obtenerPrescripcionesOrdenes()
        val ordenesPendientes = prescripciones.filter { it.tipo == "ORDEN" && it.estado == "PENDIENTE" }
        
        if (ordenesPendientes.isNotEmpty()) {
            val mensaje = """
                📋 Órdenes Pendientes:
                
                ${ordenesPendientes.joinToString("\n\n") { orden ->
                    "👤 ${orden.paciente}\n" +
                    "📋 ${orden.medicamento}\n" +
                    "📝 ${orden.indicaciones}\n" +
                    "🚨 Prioridad: ${orden.prioridad}\n" +
                    "📅 ${orden.fecha}"
                }}
            """.trimIndent()
            
            android.app.AlertDialog.Builder(this)
                .setTitle("📋 Órdenes Pendientes")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar") { _, _ -> }
                .show()
        } else {
            mostrarMensaje("No hay órdenes pendientes")
        }
    }
    
    private fun mostrarFormularioRenovarPrescripcion() {
        val prescripciones = obtenerPrescripcionesOrdenes()
        val prescripcionesVencidas = prescripciones.filter { it.tipo == "PRESCRIPCIÓN" && it.estado == "VENCIDA" }
        
        if (prescripcionesVencidas.isNotEmpty()) {
            val opciones = prescripcionesVencidas.map { "${it.id} - ${it.paciente} (${it.medicamento})" }.toTypedArray()
            
            android.app.AlertDialog.Builder(this)
                .setTitle("🔄 Renovar Prescripción")
                .setItems(opciones) { _, which ->
                    val prescripcionSeleccionada = prescripcionesVencidas[which]
                    renovarPrescripcion(prescripcionSeleccionada)
                }
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        } else {
            mostrarMensaje("No hay prescripciones vencidas para renovar")
        }
    }
    
    private fun crearNuevaPrescripcion(paciente: String, medicamento: String, dosis: String, duracion: String, indicaciones: String, prioridad: String) {
        val prescripcionId = "PRESC-${System.currentTimeMillis()}"
        
        mostrarMensaje("✅ Prescripción creada exitosamente: $prescripcionId\n\nPaciente: $paciente\nMedicamento: $medicamento\nDosis: $dosis")
        cargarPrescripcionesOrdenes() // Recargar la lista
    }
    
    private fun crearNuevaOrden(paciente: String, examen: String, indicaciones: String, prioridad: String) {
        val ordenId = "ORDEN-${System.currentTimeMillis()}"
        
        mostrarMensaje("✅ Orden médica creada exitosamente: $ordenId\n\nPaciente: $paciente\nExamen: $examen")
        cargarPrescripcionesOrdenes() // Recargar la lista
    }
    
    private fun renovarPrescripcion(prescripcion: PrescripcionOrden) {
        mostrarMensaje("🔄 Renovando prescripción ${prescripcion.id}...\n\nPaciente: ${prescripcion.paciente}\nMedicamento: ${prescripcion.medicamento}")
    }
    
    private fun cancelarPrescripcion(prescripcion: PrescripcionOrden) {
        android.app.AlertDialog.Builder(this)
            .setTitle("❌ Confirmar Cancelación")
            .setMessage("¿Estás seguro de que deseas cancelar la ${prescripcion.tipo.lowercase()} ${prescripcion.id}?\n\nPaciente: ${prescripcion.paciente}")
            .setPositiveButton("Sí, Cancelar") { _, _ ->
                mostrarMensaje("❌ ${prescripcion.tipo} ${prescripcion.id} cancelada exitosamente")
                cargarPrescripcionesOrdenes() // Recargar la lista
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }
    
    private fun mostrarDetallePrescripcion(prescripcion: PrescripcionOrden) {
        val mensaje = """
            ${if (prescripcion.tipo == "PRESCRIPCIÓN") "💊" else "📋"} Detalle de ${prescripcion.tipo}: ${prescripcion.id}
            
            👤 Paciente: ${prescripcion.paciente}
            📅 Fecha: ${prescripcion.fecha}
            ${if (prescripcion.tipo == "PRESCRIPCIÓN") "💊 Medicamento: ${prescripcion.medicamento}" else "📋 Examen: ${prescripcion.medicamento}"}
            📏 Dosis: ${prescripcion.dosis}
            ⏰ Duración: ${prescripcion.duracion}
            📝 Indicaciones: ${prescripcion.indicaciones}
            📊 Estado: ${prescripcion.estado}
            🚨 Prioridad: ${prescripcion.prioridad}
            
            💡 Acciones disponibles:
            • Ver historial del paciente
            • Modificar prescripción/orden
            • Renovar prescripción
            • Cancelar prescripción/orden
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("${if (prescripcion.tipo == "PRESCRIPCIÓN") "💊" else "📋"} Detalle de ${prescripcion.tipo}")
            .setMessage(mensaje)
            .setPositiveButton("Modificar") { _, _ ->
                mostrarMensaje("📝 Abriendo formulario de modificación...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarSinPrescripciones() {
        textViewSinPrescripciones.visibility = View.VISIBLE
        linearLayoutPrescripciones.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarPrescripcionesOrdenes() // Recargar cuando se regrese a esta actividad
    }
}

// Clase de datos para representar una prescripción u orden médica
data class PrescripcionOrden(
    val id: String,
    val paciente: String,
    val fecha: String,
    val tipo: String,
    val medicamento: String,
    val dosis: String,
    val duracion: String,
    val indicaciones: String,
    val estado: String,
    val prioridad: String
) 