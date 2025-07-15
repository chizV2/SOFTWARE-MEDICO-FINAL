package com.example.softmedv5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ReportesControlActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonReporteAtencion: Button
    private lateinit var buttonReporteCancelaciones: Button
    private lateinit var buttonReporteIngresos: Button
    private lateinit var buttonCargaTrabajo: Button
    private lateinit var buttonReportarError: Button
    private lateinit var buttonVolver: Button
    private lateinit var linearLayoutReportes: LinearLayout
    private lateinit var textViewSinReportes: TextView
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes_control)
        
        inicializarVistas()
        configurarEventos()
        cargarReportesRegistrados()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonReporteAtencion = findViewById(R.id.button_reporte_atencion)
        buttonReporteCancelaciones = findViewById(R.id.button_reporte_cancelaciones)
        buttonReporteIngresos = findViewById(R.id.button_reporte_ingresos)
        buttonCargaTrabajo = findViewById(R.id.button_carga_trabajo)
        buttonReportarError = findViewById(R.id.button_reportar_error)
        buttonVolver = findViewById(R.id.button_volver)
        linearLayoutReportes = findViewById(R.id.linear_layout_reportes)
        textViewSinReportes = findViewById(R.id.text_view_sin_reportes)
        
        textViewTitulo.text = "📊 Reportes y Control"
    }
    
    private fun configurarEventos() {
        buttonReporteAtencion.setOnClickListener {
            generarReporteAtencion()
        }
        
        buttonReporteCancelaciones.setOnClickListener {
            generarReporteCancelaciones()
        }
        
        buttonReporteIngresos.setOnClickListener {
            generarReporteIngresos()
        }
        
        buttonCargaTrabajo.setOnClickListener {
            mostrarCargaTrabajo()
        }
        
        buttonReportarError.setOnClickListener {
            mostrarFormularioReportarError()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun cargarReportesRegistrados() {
        val reportes = obtenerReportesRegistrados()
        
        mostrarResumen(reportes)
        
        if (reportes.isNotEmpty()) {
            reportes.forEach { reporte ->
                agregarReporteALista(reporte)
            }
        } else {
            mostrarSinReportes()
        }
    }
    
    private fun obtenerReportesRegistrados(): List<Reporte> {
        return listOf(
            Reporte(
                id = "REP-001",
                tipo = "Atención Diaria",
                fecha = "15/12/2024",
                descripcion = "Reporte de atención del día",
                estadisticas = "Total pacientes: 45 | Consultas: 38 | Exámenes: 7",
                estado = "GENERADO",
                prioridad = "NORMAL"
            ),
            Reporte(
                id = "REP-002",
                tipo = "Cancelaciones",
                fecha = "14/12/2024",
                descripcion = "Análisis de cancelaciones semanales",
                estadisticas = "Total cancelaciones: 12 | Motivo principal: Emergencias",
                estado = "ANALIZADO",
                prioridad = "ALTA"
            ),
            Reporte(
                id = "REP-003",
                tipo = "Ingresos",
                fecha = "13/12/2024",
                descripcion = "Reporte de ingresos mensuales",
                estadisticas = "Total ingresos: $2,450,000 | Facturas pagadas: 89%",
                estado = "GENERADO",
                prioridad = "NORMAL"
            ),
            Reporte(
                id = "REP-004",
                tipo = "Carga de Trabajo",
                fecha = "12/12/2024",
                descripcion = "Distribución de carga por especialidad",
                estadisticas = "Medicina General: 40% | Cardiología: 25% | Laboratorio: 35%",
                estado = "GENERADO",
                prioridad = "NORMAL"
            ),
            Reporte(
                id = "REP-005",
                tipo = "Error del Sistema",
                fecha = "11/12/2024",
                descripcion = "Problema con sincronización de citas",
                estadisticas = "Error crítico | Usuarios afectados: 15",
                estado = "EN REVISIÓN",
                prioridad = "CRÍTICA"
            )
        )
    }
    
    private fun mostrarResumen(reportes: List<Reporte>) {
        val totalReportes = reportes.size
        val reportesGenerados = reportes.count { it.estado == "GENERADO" }
        val reportesAnalizados = reportes.count { it.estado == "ANALIZADO" }
        val reportesEnRevision = reportes.count { it.estado == "EN REVISIÓN" }
        val erroresCriticos = reportes.count { it.prioridad == "CRÍTICA" }
        
        val resumen = """
            📊 Resumen de Reportes y Control:
            
            📄 Total de reportes: $totalReportes
            ✅ Reportes generados: $reportesGenerados
            📋 Reportes analizados: $reportesAnalizados
            ⏳ En revisión: $reportesEnRevision
            🚨 Errores críticos: $erroresCriticos
            
            💡 Funcionalidades disponibles:
            • Generar reportes de atención diaria
            • Analizar cancelaciones y tendencias
            • Reportar ingresos y finanzas
            • Ver carga de trabajo por especialidad
            • Reportar errores del sistema
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun agregarReporteALista(reporte: Reporte) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_reporte_administrativo,
            linearLayoutReportes,
            false
        )
        
        val textViewId = cardView.findViewById<TextView>(R.id.text_view_id_reporte)
        val textViewTipo = cardView.findViewById<TextView>(R.id.text_view_tipo_reporte)
        val textViewFecha = cardView.findViewById<TextView>(R.id.text_view_fecha_reporte)
        val textViewDescripcion = cardView.findViewById<TextView>(R.id.text_view_descripcion_reporte)
        val textViewEstadisticas = cardView.findViewById<TextView>(R.id.text_view_estadisticas_reporte)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado_reporte)
        val textViewPrioridad = cardView.findViewById<TextView>(R.id.text_view_prioridad_reporte)
        val buttonVerDetalle = cardView.findViewById<Button>(R.id.button_ver_detalle_reporte)
        val buttonDescargar = cardView.findViewById<Button>(R.id.button_descargar_reporte)
        val buttonCompartir = cardView.findViewById<Button>(R.id.button_compartir_reporte)
        
        textViewId.text = "Reporte: ${reporte.id}"
        textViewTipo.text = "📋 ${reporte.tipo}"
        textViewFecha.text = "📅 ${reporte.fecha}"
        textViewDescripcion.text = reporte.descripcion
        textViewEstadisticas.text = reporte.estadisticas
        
        // Configurar estado con colores
        when (reporte.estado) {
            "GENERADO" -> {
                textViewEstado.text = "✅ Generado"
                textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
            }
            "ANALIZADO" -> {
                textViewEstado.text = "📊 Analizado"
                textViewEstado.setTextColor(resources.getColor(R.color.colorPrimary, null))
            }
            "EN REVISIÓN" -> {
                textViewEstado.text = "⏳ En Revisión"
                textViewEstado.setTextColor(resources.getColor(R.color.colorWarning, null))
            }
            else -> {
                textViewEstado.text = "❌ Cancelado"
                textViewEstado.setTextColor(resources.getColor(R.color.colorError, null))
            }
        }
        
        // Configurar prioridad con colores
        when (reporte.prioridad) {
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
            mostrarDetalleReporte(reporte)
        }
        
        buttonDescargar.setOnClickListener {
            descargarReporte(reporte)
        }
        
        buttonCompartir.setOnClickListener {
            compartirReporte(reporte)
        }
        
        linearLayoutReportes.addView(cardView)
    }
    
    private fun generarReporteAtencion() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_generar_reporte_atencion, null)
        
        val editTextFecha = dialogView.findViewById<EditText>(R.id.edit_text_fecha_reporte)
        val spinnerEspecialidad = dialogView.findViewById<Spinner>(R.id.spinner_especialidad_reporte)
        val checkBoxIncluirEstadisticas = dialogView.findViewById<CheckBox>(R.id.check_box_estadisticas)
        val checkBoxIncluirGraficos = dialogView.findViewById<CheckBox>(R.id.check_box_graficos)
        
        // Configurar fecha actual
        editTextFecha.setText(dateFormat.format(calendar.time))
        
        // Configurar spinner de especialidades
        val especialidades = arrayOf("Todas las especialidades", "Medicina General", "Cardiología", "Dermatología", "Laboratorio", "Radiología", "Cirugía General")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, especialidades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEspecialidad.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Generar Reporte de Atención Diaria")
            .setView(dialogView)
            .setPositiveButton("Generar Reporte") { _, _ ->
                val fecha = editTextFecha.text.toString()
                val especialidad = spinnerEspecialidad.selectedItem.toString()
                val incluirEstadisticas = checkBoxIncluirEstadisticas.isChecked
                val incluirGraficos = checkBoxIncluirGraficos.isChecked
                
                if (fecha.isNotBlank()) {
                    generarReporteAtencionDiaria(fecha, especialidad, incluirEstadisticas, incluirGraficos)
                } else {
                    mostrarMensaje("Por favor ingresa la fecha del reporte")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun generarReporteAtencionDiaria(fecha: String, especialidad: String, estadisticas: Boolean, graficos: Boolean) {
        val reporteId = "REP-${System.currentTimeMillis()}"
        val descripcion = "Reporte de atención diaria - $especialidad"
        val estadisticasTexto = "Total pacientes: 45 | Consultas: 38 | Exámenes: 7 | Tiempo promedio: 25 min"
        
        val nuevoReporte = Reporte(
            id = reporteId,
            tipo = "Atención Diaria",
            fecha = fecha,
            descripcion = descripcion,
            estadisticas = estadisticasTexto,
            estado = "GENERADO",
            prioridad = "NORMAL"
        )
        
        mostrarMensaje("✅ Reporte de atención generado exitosamente: $reporteId")
        cargarReportesRegistrados() // Recargar la lista
    }
    
    private fun generarReporteCancelaciones() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_generar_reporte_cancelaciones, null)
        
        val editTextFechaInicio = dialogView.findViewById<EditText>(R.id.edit_text_fecha_inicio)
        val editTextFechaFin = dialogView.findViewById<EditText>(R.id.edit_text_fecha_fin)
        val spinnerMotivo = dialogView.findViewById<Spinner>(R.id.spinner_motivo_cancelacion)
        val checkBoxIncluirTendencias = dialogView.findViewById<CheckBox>(R.id.check_box_tendencias)
        
        // Configurar fechas
        val fechaInicio = Calendar.getInstance()
        fechaInicio.add(Calendar.DAY_OF_MONTH, -7)
        editTextFechaInicio.setText(dateFormat.format(fechaInicio.time))
        editTextFechaFin.setText(dateFormat.format(calendar.time))
        
        // Configurar spinner de motivos
        val motivos = arrayOf("Todos los motivos", "Emergencias", "Enfermedad del paciente", "Problemas de transporte", "Cambio de horario", "Otros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, motivos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMotivo.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Generar Reporte de Cancelaciones")
            .setView(dialogView)
            .setPositiveButton("Generar Reporte") { _, _ ->
                val fechaInicio = editTextFechaInicio.text.toString()
                val fechaFin = editTextFechaFin.text.toString()
                val motivo = spinnerMotivo.selectedItem.toString()
                val incluirTendencias = checkBoxIncluirTendencias.isChecked
                
                if (fechaInicio.isNotBlank() && fechaFin.isNotBlank()) {
                    generarReporteCancelacionesPeriodo(fechaInicio, fechaFin, motivo, incluirTendencias)
                } else {
                    mostrarMensaje("Por favor completa las fechas del período")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun generarReporteCancelacionesPeriodo(fechaInicio: String, fechaFin: String, motivo: String, tendencias: Boolean) {
        val reporteId = "REP-${System.currentTimeMillis()}"
        val descripcion = "Análisis de cancelaciones del $fechaInicio al $fechaFin"
        val estadisticasTexto = "Total cancelaciones: 12 | Motivo principal: Emergencias | Tendencia: -15% vs mes anterior"
        
        val nuevoReporte = Reporte(
            id = reporteId,
            tipo = "Cancelaciones",
            fecha = fechaFin,
            descripcion = descripcion,
            estadisticas = estadisticasTexto,
            estado = "ANALIZADO",
            prioridad = "ALTA"
        )
        
        mostrarMensaje("✅ Reporte de cancelaciones generado exitosamente: $reporteId")
        cargarReportesRegistrados() // Recargar la lista
    }
    
    private fun generarReporteIngresos() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_generar_reporte_ingresos, null)
        
        val editTextMes = dialogView.findViewById<EditText>(R.id.edit_text_mes_ingresos)
        val editTextAnio = dialogView.findViewById<EditText>(R.id.edit_text_anio_ingresos)
        val spinnerTipoIngreso = dialogView.findViewById<Spinner>(R.id.spinner_tipo_ingreso)
        val checkBoxIncluirComparacion = dialogView.findViewById<CheckBox>(R.id.check_box_comparacion)
        
        // Configurar mes y año actual
        val mesActual = calendar.get(Calendar.MONTH) + 1
        val anioActual = calendar.get(Calendar.YEAR)
        editTextMes.setText(mesActual.toString())
        editTextAnio.setText(anioActual.toString())
        
        // Configurar spinner de tipos de ingreso
        val tiposIngreso = arrayOf("Todos los ingresos", "Consultas médicas", "Exámenes de laboratorio", "Procedimientos", "Medicamentos", "Otros servicios")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposIngreso)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoIngreso.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("💰 Generar Reporte de Ingresos")
            .setView(dialogView)
            .setPositiveButton("Generar Reporte") { _, _ ->
                val mes = editTextMes.text.toString()
                val anio = editTextAnio.text.toString()
                val tipoIngreso = spinnerTipoIngreso.selectedItem.toString()
                val incluirComparacion = checkBoxIncluirComparacion.isChecked
                
                if (mes.isNotBlank() && anio.isNotBlank()) {
                    generarReporteIngresosPeriodo(mes, anio, tipoIngreso, incluirComparacion)
                } else {
                    mostrarMensaje("Por favor completa el mes y año")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun generarReporteIngresosPeriodo(mes: String, anio: String, tipoIngreso: String, comparacion: Boolean) {
        val reporteId = "REP-${System.currentTimeMillis()}"
        val descripcion = "Reporte de ingresos - $tipoIngreso - $mes/$anio"
        val estadisticasTexto = "Total ingresos: $2,450,000 | Facturas pagadas: 89% | Crecimiento: +12% vs mes anterior"
        
        val nuevoReporte = Reporte(
            id = reporteId,
            tipo = "Ingresos",
            fecha = "$mes/$anio",
            descripcion = descripcion,
            estadisticas = estadisticasTexto,
            estado = "GENERADO",
            prioridad = "NORMAL"
        )
        
        mostrarMensaje("✅ Reporte de ingresos generado exitosamente: $reporteId")
        cargarReportesRegistrados() // Recargar la lista
    }
    
    private fun mostrarCargaTrabajo() {
        val opciones = arrayOf(
            "👨‍⚕️ Por Médico",
            "🏥 Por Especialidad",
            "📅 Por Día de la Semana",
            "⏰ Por Horario",
            "📊 Comparativo Mensual"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Ver Carga de Trabajo")
            .setItems(opciones) { _, which ->
                val tipoCarga = opciones[which]
                generarReporteCargaTrabajo(tipoCarga)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun generarReporteCargaTrabajo(tipoCarga: String) {
        val reporteId = "REP-${System.currentTimeMillis()}"
        val descripcion = "Análisis de carga de trabajo - $tipoCarga"
        val estadisticasTexto = "Medicina General: 40% | Cardiología: 25% | Laboratorio: 35% | Total pacientes: 156"
        
        val nuevoReporte = Reporte(
            id = reporteId,
            tipo = "Carga de Trabajo",
            fecha = dateFormat.format(calendar.time),
            descripcion = descripcion,
            estadisticas = estadisticasTexto,
            estado = "GENERADO",
            prioridad = "NORMAL"
        )
        
        mostrarMensaje("✅ Reporte de carga de trabajo generado exitosamente: $reporteId")
        cargarReportesRegistrados() // Recargar la lista
    }
    
    private fun mostrarFormularioReportarError() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_reportar_error, null)
        
        val spinnerTipoError = dialogView.findViewById<Spinner>(R.id.spinner_tipo_error)
        val editTextDescripcion = dialogView.findViewById<EditText>(R.id.edit_text_descripcion_error)
        val editTextPasos = dialogView.findViewById<EditText>(R.id.edit_text_pasos_error)
        val spinnerPrioridad = dialogView.findViewById<Spinner>(R.id.spinner_prioridad_error)
        val editTextContacto = dialogView.findViewById<EditText>(R.id.edit_text_contacto_error)
        
        // Configurar spinner de tipos de error
        val tiposError = arrayOf("Error del Sistema", "Problema de Rendimiento", "Error de Interfaz", "Problema de Sincronización", "Error de Seguridad", "Otro")
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposError)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoError.adapter = adapterTipo
        
        // Configurar spinner de prioridad
        val prioridades = arrayOf("Baja", "Normal", "Alta", "Crítica")
        val adapterPrioridad = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapterPrioridad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapterPrioridad
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🚨 Reportar Error del Sistema")
            .setView(dialogView)
            .setPositiveButton("Reportar Error") { _, _ ->
                val tipoError = spinnerTipoError.selectedItem.toString()
                val descripcion = editTextDescripcion.text.toString()
                val pasos = editTextPasos.text.toString()
                val prioridad = spinnerPrioridad.selectedItem.toString()
                val contacto = editTextContacto.text.toString()
                
                if (descripcion.isNotBlank()) {
                    reportarErrorSistema(tipoError, descripcion, pasos, prioridad, contacto)
                } else {
                    mostrarMensaje("Por favor describe el error")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun reportarErrorSistema(tipoError: String, descripcion: String, pasos: String, prioridad: String, contacto: String) {
        val reporteId = "REP-${System.currentTimeMillis()}"
        val descripcionCompleta = "Error: $tipoError - $descripcion"
        val estadisticasTexto = "Prioridad: $prioridad | Usuarios afectados: 15 | Estado: En revisión"
        
        val nuevoReporte = Reporte(
            id = reporteId,
            tipo = "Error del Sistema",
            fecha = dateFormat.format(calendar.time),
            descripcion = descripcionCompleta,
            estadisticas = estadisticasTexto,
            estado = "EN REVISIÓN",
            prioridad = prioridad.uppercase()
        )
        
        mostrarMensaje("✅ Error reportado exitosamente: $reporteId\n\nEl administrador del sistema ha sido notificado.")
        cargarReportesRegistrados() // Recargar la lista
    }
    
    private fun mostrarDetalleReporte(reporte: Reporte) {
        val mensaje = """
            📊 Detalle del Reporte: ${reporte.id}
            
            📋 Tipo: ${reporte.tipo}
            📅 Fecha: ${reporte.fecha}
            📝 Descripción: ${reporte.descripcion}
            
            📈 Estadísticas:
            ${reporte.estadisticas}
            
            📊 Estado: ${reporte.estado}
            🚨 Prioridad: ${reporte.prioridad}
            
            💡 Acciones disponibles:
            • Descargar reporte en PDF
            • Compartir por email
            • Enviar al administrador
            • Archivar reporte
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Detalle del Reporte")
            .setMessage(mensaje)
            .setPositiveButton("Descargar PDF") { _, _ ->
                descargarReporte(reporte)
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun descargarReporte(reporte: Reporte) {
        mostrarMensaje("📥 Descargando reporte ${reporte.id} en formato PDF...\n\nEl archivo se guardará en la carpeta 'Descargas'")
    }
    
    private fun compartirReporte(reporte: Reporte) {
        mostrarMensaje("📤 Compartiendo reporte ${reporte.id}...\n\nSe abrirá la aplicación de email con el reporte adjunto")
    }
    
    private fun mostrarSinReportes() {
        textViewSinReportes.visibility = View.VISIBLE
        linearLayoutReportes.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarReportesRegistrados() // Recargar cuando se regrese a esta actividad
    }
}

// Clase de datos para representar un reporte
data class Reporte(
    val id: String,
    val tipo: String,
    val fecha: String,
    val descripcion: String,
    val estadisticas: String,
    val estado: String,
    val prioridad: String
) 