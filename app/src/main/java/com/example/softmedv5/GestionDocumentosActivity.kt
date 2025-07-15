package com.example.softmedv5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class GestionDocumentosActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonDigitalizarDocumento: Button
    private lateinit var buttonSubirReferencia: Button
    private lateinit var buttonOrganizarArchivos: Button
    private lateinit var buttonVerEstado: Button
    private lateinit var buttonBuscarDocumento: Button
    private lateinit var buttonVolver: Button
    private lateinit var linearLayoutDocumentos: LinearLayout
    private lateinit var textViewSinDocumentos: TextView
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_documentos)
        
        inicializarVistas()
        configurarEventos()
        cargarDocumentosRegistrados()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonDigitalizarDocumento = findViewById(R.id.button_digitalizar_documento)
        buttonSubirReferencia = findViewById(R.id.button_subir_referencia)
        buttonOrganizarArchivos = findViewById(R.id.button_organizar_archivos)
        buttonVerEstado = findViewById(R.id.button_ver_estado)
        buttonBuscarDocumento = findViewById(R.id.button_buscar_documento)
        buttonVolver = findViewById(R.id.button_volver)
        linearLayoutDocumentos = findViewById(R.id.linear_layout_documentos)
        textViewSinDocumentos = findViewById(R.id.text_view_sin_documentos)
        
        textViewTitulo.text = "📄 Gestión de Documentos Clínicos"
    }
    
    private fun configurarEventos() {
        buttonDigitalizarDocumento.setOnClickListener {
            mostrarFormularioDigitalizar()
        }
        
        buttonSubirReferencia.setOnClickListener {
            mostrarFormularioSubirReferencia()
        }
        
        buttonOrganizarArchivos.setOnClickListener {
            mostrarOpcionesOrganizacion()
        }
        
        buttonVerEstado.setOnClickListener {
            mostrarEstadoDocumentos()
        }
        
        buttonBuscarDocumento.setOnClickListener {
            mostrarFormularioBuscar()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun cargarDocumentosRegistrados() {
        val documentos = obtenerDocumentosRegistrados()
        
        mostrarResumen(documentos)
        
        if (documentos.isNotEmpty()) {
            documentos.forEach { documento ->
                agregarDocumentoALista(documento)
            }
        } else {
            mostrarSinDocumentos()
        }
    }
    
    private fun obtenerDocumentosRegistrados(): List<DocumentoClinico> {
        return listOf(
            DocumentoClinico(
                id = "DOC-001",
                tipo = "Examen de Laboratorio",
                paciente = "María González",
                fecha = "15/12/2024",
                hora = "09:30",
                estado = "DIGITALIZADO",
                prioridad = "NORMAL",
                descripcion = "Hemograma completo - Resultados normales",
                archivo = "hemograma_maria_gonzalez.pdf",
                tamano = "2.5 MB"
            ),
            DocumentoClinico(
                id = "DOC-002",
                tipo = "Referencia Externa",
                paciente = "Carlos Rodríguez",
                fecha = "14/12/2024",
                hora = "14:00",
                estado = "PENDIENTE",
                prioridad = "ALTA",
                descripcion = "Derivación a cardiólogo - Dr. López",
                archivo = "referencia_cardiologia_carlos.pdf",
                tamano = "1.8 MB"
            ),
            DocumentoClinico(
                id = "DOC-003",
                tipo = "Certificado Médico",
                paciente = "Ana Martínez",
                fecha = "13/12/2024",
                hora = "11:15",
                estado = "DIGITALIZADO",
                prioridad = "NORMAL",
                descripcion = "Certificado de aptitud física",
                archivo = "certificado_ana_martinez.pdf",
                tamano = "3.2 MB"
            ),
            DocumentoClinico(
                id = "DOC-004",
                tipo = "Radiografía",
                paciente = "Juan Pérez",
                fecha = "12/12/2024",
                hora = "16:30",
                estado = "EN PROCESO",
                prioridad = "ALTA",
                descripcion = "Radiografía de tórax - Fractura costilla",
                archivo = "radiografia_juan_perez.dicom",
                tamano = "15.7 MB"
            ),
            DocumentoClinico(
                id = "DOC-005",
                tipo = "Historia Clínica",
                paciente = "Laura Sánchez",
                fecha = "11/12/2024",
                hora = "08:45",
                estado = "DIGITALIZADO",
                prioridad = "NORMAL",
                descripcion = "Actualización de historia clínica",
                archivo = "historia_laura_sanchez.pdf",
                tamano = "8.9 MB"
            )
        )
    }
    
    private fun mostrarResumen(documentos: List<DocumentoClinico>) {
        val totalDocumentos = documentos.size
        val documentosDigitalizados = documentos.count { it.estado == "DIGITALIZADO" }
        val documentosPendientes = documentos.count { it.estado == "PENDIENTE" }
        val documentosEnProceso = documentos.count { it.estado == "EN PROCESO" }
        val documentosCriticos = documentos.count { it.prioridad == "CRÍTICA" }
        val totalTamano = documentos.sumOf { it.tamano.replace(" MB", "").toDoubleOrNull() ?: 0.0 }
        
        val resumen = """
            📄 Resumen de Documentos Clínicos:
            
            📋 Total de documentos: $totalDocumentos
            ✅ Digitalizados: $documentosDigitalizados
            ⏳ Pendientes: $documentosPendientes
            🔄 En proceso: $documentosEnProceso
            🚨 Críticos: $documentosCriticos
            💾 Espacio total: ${String.format("%.1f", totalTamano)} MB
            
            💡 Funcionalidades disponibles:
            • Digitalizar documentos médicos (exámenes, certificados)
            • Subir referencias externas o derivaciones
            • Organizar archivos por paciente o por fecha
            • Ver el estado de los documentos solicitados
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun agregarDocumentoALista(documento: DocumentoClinico) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_documento_administrativo,
            linearLayoutDocumentos,
            false
        )
        
        val textViewTipo = cardView.findViewById<TextView>(R.id.text_view_tipo)
        val textViewPaciente = cardView.findViewById<TextView>(R.id.text_view_paciente)
        val textViewFechaHora = cardView.findViewById<TextView>(R.id.text_view_fecha_hora)
        val textViewDescripcion = cardView.findViewById<TextView>(R.id.text_view_descripcion)
        val textViewArchivo = cardView.findViewById<TextView>(R.id.text_view_archivo)
        val textViewTamano = cardView.findViewById<TextView>(R.id.text_view_tamano)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado)
        val textViewPrioridad = cardView.findViewById<TextView>(R.id.text_view_prioridad)
        val buttonVer = cardView.findViewById<Button>(R.id.button_ver)
        val buttonDescargar = cardView.findViewById<Button>(R.id.button_descargar)
        val buttonCompartir = cardView.findViewById<Button>(R.id.button_compartir)
        
        textViewTipo.text = documento.tipo
        textViewPaciente.text = documento.paciente
        textViewFechaHora.text = "${documento.fecha} - ${documento.hora}"
        textViewDescripcion.text = documento.descripcion
        textViewArchivo.text = documento.archivo
        textViewTamano.text = documento.tamano
        
        // Configurar estado con colores
        when (documento.estado) {
            "DIGITALIZADO" -> {
                textViewEstado.text = "Digitalizado"
                textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
            }
            "PENDIENTE" -> {
                textViewEstado.text = "Pendiente"
                textViewEstado.setTextColor(resources.getColor(R.color.colorWarning, null))
            }
            "EN PROCESO" -> {
                textViewEstado.text = "En Proceso"
                textViewEstado.setTextColor(resources.getColor(R.color.colorPrimary, null))
            }
            else -> {
                textViewEstado.text = "Cancelado"
                textViewEstado.setTextColor(resources.getColor(R.color.colorError, null))
            }
        }
        
        // Configurar prioridad con colores
        when (documento.prioridad) {
            "CRÍTICA" -> {
                textViewPrioridad.text = "Crítica"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorError, null))
            }
            "ALTA" -> {
                textViewPrioridad.text = "Alta"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorWarning, null))
            }
            "NORMAL" -> {
                textViewPrioridad.text = "Normal"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorSuccess, null))
            }
            else -> {
                textViewPrioridad.text = "Baja"
                textViewPrioridad.setTextColor(resources.getColor(R.color.colorTextSecondary, null))
            }
        }
        
        buttonVer.setOnClickListener {
            mostrarDetalleDocumento(documento)
        }
        
        buttonDescargar.setOnClickListener {
            descargarDocumento(documento)
        }
        
        buttonCompartir.setOnClickListener {
            compartirDocumento(documento)
        }
        
        linearLayoutDocumentos.addView(cardView)
    }
    
    private fun mostrarFormularioDigitalizar() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_digitalizar_documento, null)
        
        val spinnerTipoDocumento = dialogView.findViewById<Spinner>(R.id.spinner_tipo_documento)
        val editTextPaciente = dialogView.findViewById<EditText>(R.id.edit_text_paciente_digitalizar)
        val editTextDescripcion = dialogView.findViewById<EditText>(R.id.edit_text_descripcion_digitalizar)
        val spinnerPrioridad = dialogView.findViewById<Spinner>(R.id.spinner_prioridad_digitalizar)
        val checkBoxEscaneoAutomatico = dialogView.findViewById<CheckBox>(R.id.check_box_escaneo_automatico)
        val checkBoxOCR = dialogView.findViewById<CheckBox>(R.id.check_box_ocr)
        
        // Configurar spinner de tipos de documento
        val tiposDocumento = arrayOf("Examen de Laboratorio", "Radiografía", "Certificado Médico", "Historia Clínica", "Receta Médica", "Consentimiento Informado", "Otro")
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDocumento)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDocumento.adapter = adapterTipo
        
        // Configurar spinner de prioridad
        val prioridades = arrayOf("Normal", "Alta", "Crítica")
        val adapterPrioridad = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapterPrioridad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapterPrioridad
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Digitalizar Documento Clínico")
            .setView(dialogView)
            .setPositiveButton("Iniciar Digitalización") { _, _ ->
                val tipoDocumento = spinnerTipoDocumento.selectedItem.toString()
                val paciente = editTextPaciente.text.toString()
                val descripcion = editTextDescripcion.text.toString()
                val prioridad = spinnerPrioridad.selectedItem.toString()
                val escaneoAutomatico = checkBoxEscaneoAutomatico.isChecked
                val ocr = checkBoxOCR.isChecked
                
                if (paciente.isNotBlank() && descripcion.isNotBlank()) {
                    digitalizarDocumento(tipoDocumento, paciente, descripcion, prioridad, escaneoAutomatico, ocr)
                } else {
                    mostrarMensaje("Por favor completa los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun digitalizarDocumento(tipoDocumento: String, paciente: String, descripcion: String, prioridad: String, escaneoAutomatico: Boolean, ocr: Boolean) {
        val documentoId = "DOC-${System.currentTimeMillis()}"
        val archivo = "${tipoDocumento.lowercase().replace(" ", "_")}_${paciente.lowercase().replace(" ", "_")}.pdf"
        val tamano = "${(Math.random() * 10 + 1).toInt()}.${(Math.random() * 9 + 1).toInt()} MB"
        
        val nuevoDocumento = DocumentoClinico(
            id = documentoId,
            tipo = tipoDocumento,
            paciente = paciente,
            fecha = dateFormat.format(calendar.time).split(" ")[0],
            hora = dateFormat.format(calendar.time).split(" ")[1],
            estado = "EN PROCESO",
            prioridad = prioridad.uppercase(),
            descripcion = descripcion,
            archivo = archivo,
            tamano = tamano
        )
        
        mostrarMensaje("✅ Digitalización iniciada para documento: $documentoId\n\nEl documento se procesará automáticamente.")
        cargarDocumentosRegistrados() // Recargar la lista
    }
    
    private fun mostrarFormularioSubirReferencia() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_subir_referencia, null)
        
        val editTextPaciente = dialogView.findViewById<EditText>(R.id.edit_text_paciente_referencia)
        val editTextEspecialidad = dialogView.findViewById<EditText>(R.id.edit_text_especialidad_referencia)
        val editTextMedicoReferente = dialogView.findViewById<EditText>(R.id.edit_text_medico_referente)
        val editTextMotivo = dialogView.findViewById<EditText>(R.id.edit_text_motivo_referencia)
        val editTextObservaciones = dialogView.findViewById<EditText>(R.id.edit_text_observaciones_referencia)
        val spinnerPrioridad = dialogView.findViewById<Spinner>(R.id.spinner_prioridad_referencia)
        
        // Configurar spinner de prioridad
        val prioridades = arrayOf("Normal", "Alta", "Crítica")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Subir Referencia Externa")
            .setView(dialogView)
            .setPositiveButton("Subir Referencia") { _, _ ->
                val paciente = editTextPaciente.text.toString()
                val especialidad = editTextEspecialidad.text.toString()
                val medicoReferente = editTextMedicoReferente.text.toString()
                val motivo = editTextMotivo.text.toString()
                val observaciones = editTextObservaciones.text.toString()
                val prioridad = spinnerPrioridad.selectedItem.toString()
                
                if (paciente.isNotBlank() && especialidad.isNotBlank() && medicoReferente.isNotBlank()) {
                    subirReferencia(paciente, especialidad, medicoReferente, motivo, observaciones, prioridad)
                } else {
                    mostrarMensaje("Por favor completa los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun subirReferencia(paciente: String, especialidad: String, medicoReferente: String, motivo: String, observaciones: String, prioridad: String) {
        val documentoId = "DOC-${System.currentTimeMillis()}"
        val archivo = "referencia_${especialidad.lowercase().replace(" ", "_")}_${paciente.lowercase().replace(" ", "_")}.pdf"
        val tamano = "${(Math.random() * 5 + 1).toInt()}.${(Math.random() * 9 + 1).toInt()} MB"
        val descripcion = "Referencia a $especialidad - Dr. $medicoReferente. Motivo: $motivo"
        
        val nuevoDocumento = DocumentoClinico(
            id = documentoId,
            tipo = "Referencia Externa",
            paciente = paciente,
            fecha = dateFormat.format(calendar.time).split(" ")[0],
            hora = dateFormat.format(calendar.time).split(" ")[1],
            estado = "PENDIENTE",
            prioridad = prioridad.uppercase(),
            descripcion = descripcion,
            archivo = archivo,
            tamano = tamano
        )
        
        mostrarMensaje("✅ Referencia subida exitosamente: $documentoId")
        cargarDocumentosRegistrados() // Recargar la lista
    }
    
    private fun mostrarOpcionesOrganizacion() {
        val opciones = arrayOf(
            "👤 Por Paciente",
            "📅 Por Fecha",
            "📋 Por Tipo de Documento",
            "🏥 Por Especialidad",
            "📊 Por Estado"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📁 Organizar Archivos")
            .setItems(opciones) { _, which ->
                val tipoOrganizacion = opciones[which]
                organizarArchivos(tipoOrganizacion)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun organizarArchivos(tipoOrganizacion: String) {
        val documentos = obtenerDocumentosRegistrados()
        val mensaje = when (tipoOrganizacion) {
            "👤 Por Paciente" -> {
                val pacientes = documentos.map { it.paciente }.distinct()
                "📁 Organización por Paciente:\n\n${pacientes.joinToString("\n") { "• $it" }}"
            }
            "📅 Por Fecha" -> {
                val fechas = documentos.map { it.fecha }.distinct().sorted()
                "📁 Organización por Fecha:\n\n${fechas.joinToString("\n") { "• $it" }}"
            }
            "📋 Por Tipo de Documento" -> {
                val tipos = documentos.map { it.tipo }.distinct()
                "📁 Organización por Tipo:\n\n${tipos.joinToString("\n") { "• $it" }}"
            }
            "🏥 Por Especialidad" -> {
                "📁 Organización por Especialidad:\n\n• Cardiología\n• Laboratorio\n• Radiología\n• Medicina General"
            }
            else -> {
                val estados = documentos.map { it.estado }.distinct()
                "📁 Organización por Estado:\n\n${estados.joinToString("\n") { "• $it" }}"
            }
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📁 Organización de Archivos")
            .setMessage(mensaje)
            .setPositiveButton("Aplicar Organización") { _, _ ->
                mostrarMensaje("✅ Archivos organizados por $tipoOrganizacion")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarEstadoDocumentos() {
        val documentos = obtenerDocumentosRegistrados()
        val digitalizados = documentos.count { it.estado == "DIGITALIZADO" }
        val pendientes = documentos.count { it.estado == "PENDIENTE" }
        val enProceso = documentos.count { it.estado == "EN PROCESO" }
        val criticos = documentos.count { it.prioridad == "CRÍTICA" }
        
        val mensaje = """
            📊 Estado de Documentos Solicitados:
            
            ✅ Digitalizados: $digitalizados
            ⏳ Pendientes: $pendientes
            🔄 En Proceso: $enProceso
            🚨 Críticos: $criticos
            
            📋 Documentos Pendientes:
            ${documentos.filter { it.estado == "PENDIENTE" }.joinToString("\n") { "• ${it.id} - ${it.paciente} (${it.tipo})" }}
            
            🔄 Documentos en Proceso:
            ${documentos.filter { it.estado == "EN PROCESO" }.joinToString("\n") { "• ${it.id} - ${it.paciente} (${it.tipo})" }}
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Estado de Documentos")
            .setMessage(mensaje)
            .setPositiveButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarFormularioBuscar() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_buscar_documento, null)
        
        val editTextPaciente = dialogView.findViewById<EditText>(R.id.edit_text_paciente_buscar)
        val editTextTipo = dialogView.findViewById<EditText>(R.id.edit_text_tipo_buscar)
        val editTextFecha = dialogView.findViewById<EditText>(R.id.edit_text_fecha_buscar)
        val spinnerEstado = dialogView.findViewById<Spinner>(R.id.spinner_estado_buscar)
        
        // Configurar spinner de estado
        val estados = arrayOf("Todos", "Digitalizado", "Pendiente", "En Proceso", "Cancelado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🔍 Buscar Documento")
            .setView(dialogView)
            .setPositiveButton("Buscar") { _, _ ->
                val paciente = editTextPaciente.text.toString()
                val tipo = editTextTipo.text.toString()
                val fecha = editTextFecha.text.toString()
                val estado = spinnerEstado.selectedItem.toString()
                
                buscarDocumentos(paciente, tipo, fecha, estado)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun buscarDocumentos(paciente: String, tipo: String, fecha: String, estado: String) {
        val documentos = obtenerDocumentosRegistrados()
        val resultados = documentos.filter { documento ->
            (paciente.isBlank() || documento.paciente.contains(paciente, ignoreCase = true)) &&
            (tipo.isBlank() || documento.tipo.contains(tipo, ignoreCase = true)) &&
            (fecha.isBlank() || documento.fecha == fecha) &&
            (estado == "Todos" || documento.estado == estado)
        }
        
        val mensaje = if (resultados.isNotEmpty()) {
            "🔍 Resultados de búsqueda (${resultados.size} documentos):\n\n" +
            resultados.joinToString("\n") { documento ->
                "• ${documento.id} - ${documento.paciente} (${documento.tipo}) - ${documento.estado}"
            }
        } else {
            "🔍 No se encontraron documentos con los criterios especificados."
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🔍 Resultados de Búsqueda")
            .setMessage(mensaje)
            .setPositiveButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarDetalleDocumento(documento: DocumentoClinico) {
        val mensaje = """
            📄 Detalle del Documento: ${documento.id}
            
            📋 Tipo: ${documento.tipo}
            👤 Paciente: ${documento.paciente}
            📅 Fecha: ${documento.fecha} - ${documento.hora}
            📝 Descripción: ${documento.descripcion}
            
            📁 Archivo: ${documento.archivo}
            💾 Tamaño: ${documento.tamano}
            📊 Estado: ${documento.estado}
            🚨 Prioridad: ${documento.prioridad}
            
            💡 Acciones disponibles:
            • Descargar documento
            • Compartir por email
            • Ver historial de cambios
            • Actualizar estado
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Detalle del Documento")
            .setMessage(mensaje)
            .setPositiveButton("Descargar") { _, _ ->
                descargarDocumento(documento)
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun descargarDocumento(documento: DocumentoClinico) {
        mostrarMensaje("📥 Descargando documento ${documento.id}...\n\nEl archivo se guardará en la carpeta 'Descargas'")
    }
    
    private fun compartirDocumento(documento: DocumentoClinico) {
        mostrarMensaje("📤 Compartiendo documento ${documento.id}...\n\nSe abrirá la aplicación de email con el documento adjunto")
    }
    
    private fun mostrarSinDocumentos() {
        textViewSinDocumentos.visibility = View.VISIBLE
        linearLayoutDocumentos.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarDocumentosRegistrados() // Recargar cuando se regrese a esta actividad
    }
}

// Clase de datos para representar un documento clínico
data class DocumentoClinico(
    val id: String,
    val tipo: String,
    val paciente: String,
    val fecha: String,
    val hora: String,
    val estado: String,
    val prioridad: String,
    val descripcion: String,
    val archivo: String,
    val tamano: String
) 