package com.example.softmedv5

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.database.SimpleDatabaseHelper
import com.example.softmedv5.modelo.DiagnosticoMedico
import com.example.softmedv5.modelo.Paciente
import com.example.softmedv5.util.GestorAutenticacion
import com.example.softmedv5.util.GestorHistorialClinico

class HistorialClinicoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var linearLayoutContenido: LinearLayout
    private lateinit var textViewSinHistorial: TextView
    private lateinit var spinnerFiltroFecha: Spinner
    private lateinit var buttonLimpiarFiltro: Button
    private lateinit var linearLayoutDiagnosticos: LinearLayout
    private lateinit var textViewTituloDiagnosticos: TextView
    private lateinit var linearLayoutListaDiagnosticos: LinearLayout
    private lateinit var buttonVerDiagnosticos: Button
    private lateinit var buttonVerConsultas: Button
    private lateinit var buttonVerEnfermedades: Button
    private lateinit var buttonVerAlergias: Button
    private lateinit var buttonVerCirugias: Button
    private lateinit var buttonVerAlertas: Button
    private lateinit var buttonVolver: Button
    
    private lateinit var dbHelper: SimpleDatabaseHelper
    private var diagnosticos: List<DiagnosticoMedico> = emptyList()
    private lateinit var gestorHistorial: GestorHistorialClinico
    private var filtroFechaActual = "TODAS"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_clinico)
        
        inicializarVistas()
        configurarEventos()
        configurarSpinnerFiltro()
        cargarHistorialPaciente()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        linearLayoutContenido = findViewById(R.id.linear_layout_contenido)
        textViewSinHistorial = findViewById(R.id.text_view_sin_historial)
        spinnerFiltroFecha = findViewById(R.id.spinner_filtro_fecha)
        buttonLimpiarFiltro = findViewById(R.id.button_limpiar_filtro)
        linearLayoutDiagnosticos = findViewById(R.id.linear_layout_diagnosticos)
        textViewTituloDiagnosticos = findViewById(R.id.text_view_titulo_diagnosticos)
        linearLayoutListaDiagnosticos = findViewById(R.id.linear_layout_lista_diagnosticos)
        buttonVerDiagnosticos = findViewById(R.id.button_ver_diagnosticos)
        buttonVerConsultas = findViewById(R.id.button_ver_consultas)
        buttonVerEnfermedades = findViewById(R.id.button_ver_enfermedades)
        buttonVerAlergias = findViewById(R.id.button_ver_alergias)
        buttonVerCirugias = findViewById(R.id.button_ver_cirugias)
        buttonVerAlertas = findViewById(R.id.button_ver_alertas)
        buttonVolver = findViewById(R.id.button_volver)
        
        textViewTitulo.text = "📋 Mi Historial Clínico"
        
        // Inicializar base de datos
        dbHelper = SimpleDatabaseHelper(this)
        gestorHistorial = GestorHistorialClinico.obtenerInstancia()
        
        // Agregar botón de prueba temporal
        agregarBotonPrueba()
    }
    
    private fun agregarBotonPrueba() {
        val botonPrueba = Button(this)
        botonPrueba.text = "🧪 Crear Diagnóstico de Prueba"
        botonPrueba.textSize = 12f
        botonPrueba.background = resources.getDrawable(R.drawable.button_background, null)
        botonPrueba.setTextColor(resources.getColor(android.R.color.white, null))
        botonPrueba.setPadding(16, 8, 16, 8)
        
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8)
        botonPrueba.layoutParams = layoutParams
        
        botonPrueba.setOnClickListener {
            ejecutarPruebaDiagnosticos()
        }
        
        // Insertar después del título
        val parent = textViewTitulo.parent as LinearLayout
        val index = parent.indexOfChild(textViewTitulo) + 1
        parent.addView(botonPrueba, index)
    }
    
    private fun configurarEventos() {
        buttonLimpiarFiltro.setOnClickListener {
            limpiarFiltro()
        }
        
        buttonVerDiagnosticos.setOnClickListener {
            mostrarDiagnosticos()
        }
        
        buttonVerConsultas.setOnClickListener {
            mostrarConsultas()
        }
        
        buttonVerEnfermedades.setOnClickListener {
            mostrarEnfermedades()
        }
        
        buttonVerAlergias.setOnClickListener {
            mostrarAlergias()
        }
        
        buttonVerCirugias.setOnClickListener {
            mostrarCirugias()
        }
        
        buttonVerAlertas.setOnClickListener {
            mostrarAlertas()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
        
        // Agregar evento de prueba temporal
        textViewTitulo.setOnLongClickListener {
            ejecutarPruebaDiagnosticos()
            true
        }
    }
    
    private fun ejecutarPruebaDiagnosticos() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion == null) {
            mostrarMensaje("No hay sesión activa")
            return
        }
        
        val pacienteId = sesion.usuario.obtenerId()
        println("=== PRUEBA: Creando diagnóstico de prueba ===")
        println("Paciente ID: $pacienteId")
        
        // Crear un diagnóstico de prueba
        val diagnosticoPrueba = com.example.softmedv5.modelo.DiagnosticoMedico(
            id = "PRUEBA-${System.currentTimeMillis()}",
            pacienteId = pacienteId,
            medicoId = "1", // ID de médico de prueba
            fecha = obtenerFechaActual(),
            sintomas = "Síntomas de prueba: dolor de cabeza, fiebre",
            examenFisico = "Examen físico de prueba: temperatura 38°C, presión normal",
            diagnostico = "Diagnóstico de prueba: Resfriado común",
            diagnosticosSecundarios = "Diagnósticos secundarios de prueba: Sin complicaciones",
            tratamiento = "Tratamiento de prueba: Paracetamol 500mg cada 6 horas",
            medicamentos = "Medicamentos de prueba: Paracetamol, Vitamina C",
            recomendaciones = "Recomendaciones de prueba: Descanso, hidratación abundante",
            proximaCita = "Próxima cita de prueba: En 1 semana si persisten síntomas",
            estado = "FINALIZADA"
        )
        
        println("Diagnóstico creado: ${diagnosticoPrueba.id}")
        println("Fecha: ${diagnosticoPrueba.fecha}")
        
        val resultado = dbHelper.insertarDiagnostico(diagnosticoPrueba)
        
        if (resultado > 0) {
            mostrarMensaje("✅ Diagnóstico de prueba creado exitosamente (ID: ${diagnosticoPrueba.id})")
            println("Diagnóstico guardado con resultado: $resultado")
            
            // Recargar diagnósticos después de un breve delay
            linearLayoutListaDiagnosticos.postDelayed({
                cargarDiagnosticosReales(pacienteId)
            }, 500)
        } else {
            mostrarMensaje("❌ Error al crear diagnóstico de prueba")
            println("Error al guardar diagnóstico")
        }
    }
    
    private fun obtenerFechaActual(): String {
        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }
    
    private fun configurarSpinnerFiltro() {
        val opciones = arrayOf("TODAS", "ÚLTIMA SEMANA", "ÚLTIMO MES", "ÚLTIMO AÑO")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFiltroFecha.adapter = adapter
        
        spinnerFiltroFecha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filtroFechaActual = opciones[position]
                aplicarFiltroFecha()
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun cargarHistorialPaciente() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion == null) {
            mostrarMensaje("Error: No hay sesión activa")
            finish()
            return
        }
        
        val pacienteId = sesion.usuario.obtenerId()
        val paciente = sesion.usuario as? Paciente
        
        // Mostrar información del paciente
        if (paciente != null) {
            mostrarInformacionPaciente(paciente)
        }
        
        // Cargar diagnósticos reales desde la base de datos
        cargarDiagnosticosReales(pacienteId)
        
        // Verificar si existe historial clínico, si no, crear uno vacío
        if (!gestorHistorial.tieneHistorialClinico(pacienteId)) {
            gestorHistorial.crearHistorialVacio(pacienteId)
        }
        
        // Obtener historial clínico del gestor
        val historialClinico = gestorHistorial.obtenerHistorialClinico(pacienteId)
        
        if (historialClinico != null && !historialClinico.estaVacio()) {
            mostrarHistorialDisponible(historialClinico)
        } else {
            mostrarSinHistorial()
        }
    }
    
    private fun cargarDiagnosticosReales(pacienteId: String) {
        try {
            println("=== DEBUG: Cargando diagnósticos ===")
            println("Paciente ID: $pacienteId")
            println("Tipo de Paciente ID: ${pacienteId::class.simpleName}")
            
            // Obtener diagnósticos desde la base de datos
            val diagnosticosDb = dbHelper.obtenerDiagnosticosPorPaciente(pacienteId)
            println("Diagnósticos encontrados: ${diagnosticosDb.size}")
            
            // Debug: Mostrar cada diagnóstico encontrado
            diagnosticosDb.forEachIndexed { index, diagnostico ->
                println("Diagnóstico ${index + 1}:")
                println("  - ID: ${diagnostico.id}")
                println("  - Paciente ID: ${diagnostico.pacienteId}")
                println("  - Médico ID: ${diagnostico.medicoId}")
                println("  - Fecha: ${diagnostico.fecha}")
                println("  - Diagnóstico: ${diagnostico.diagnostico}")
                println("  - Estado: ${diagnostico.estado}")
            }
            
            diagnosticos = diagnosticosDb
            
            // Mostrar diagnósticos iniciales
            mostrarDiagnosticosFiltrados()
            
        } catch (e: Exception) {
            println("Error al cargar diagnósticos: ${e.message}")
            e.printStackTrace()
            diagnosticos = emptyList()
        }
    }
    
    private fun mostrarInformacionPaciente(paciente: Paciente) {
        val informacionPaciente = """
            👤 Información del Paciente
            
            ${paciente.obtenerInformacionResumida()}
            
            📊 Estado del perfil: ${paciente.obtenerPorcentajeCompletitud()}% completo
        """.trimIndent()
        
        // Crear un TextView para mostrar la información del paciente
        val textViewInfoPaciente = TextView(this)
        textViewInfoPaciente.text = informacionPaciente
        textViewInfoPaciente.setPadding(0, 0, 0, 32)
        textViewInfoPaciente.textSize = 14f
        
        // Insertar al inicio del layout
        val parent = textViewResumen.parent as LinearLayout
        parent.addView(textViewInfoPaciente, 0)
    }
    
    private fun mostrarHistorialDisponible(historialClinico: com.example.softmedv5.modelo.HistorialClinico) {
        textViewSinHistorial.visibility = View.GONE
        linearLayoutContenido.visibility = View.VISIBLE
        
        val resumen = """
            📋 Resumen del Historial Clínico
            
            🔍 Diagnósticos: ${diagnosticos.size} registros
            🏥 Consultas: ${historialClinico.consultasPasadas.size} consultas
            ⚠️ Enfermedades crónicas: ${historialClinico.enfermedadesCronicas.size} condiciones
            🚨 Alergias: ${historialClinico.alergiasRegistradas.size} alergias
            🔪 Cirugías/Procedimientos: ${historialClinico.cirugiasProcedimientos.size} intervenciones
            
            📅 Última actualización: ${historialClinico.fechaUltimaActualizacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
            
            💡 Funcionalidades disponibles:
            • Ver diagnósticos médicos (${diagnosticos.size} disponibles)
            • Filtrar por fecha
            • Consultar tratamientos
            • Revisar recomendaciones
            • Ver medicamentos prescritos
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun mostrarSinHistorial() {
        textViewSinHistorial.visibility = View.VISIBLE
        linearLayoutContenido.visibility = View.GONE
        
        textViewSinHistorial.text = """
            📋 No se encontró historial clínico
            
            ℹ️ Información:
            • Tu historial clínico se creará automáticamente
            • Los médicos agregarán información durante las consultas
            • Podrás ver diagnósticos, consultas y tratamientos
            • Esta información es de solo lectura para tu seguridad
            
            💡 ¿Qué incluye tu historial?
            • Diagnósticos médicos
            • Tratamientos prescritos
            • Medicamentos
            • Recomendaciones médicas
            • Próximas citas sugeridas
        """.trimIndent()
    }
    
    private fun mostrarDiagnosticos() {
        if (diagnosticos.isNotEmpty()) {
            val mensaje = """
                🔍 Diagnósticos Médicos (${diagnosticos.size} registros):
                
                ${diagnosticos.sortedByDescending { parsearFecha(it.fecha) }.joinToString("\n\n") { diagnostico ->
                    """
                    📅 ${diagnostico.fecha}
                    🔍 ${diagnostico.diagnostico}
                    💊 ${diagnostico.tratamiento}
                    📝 ${diagnostico.recomendaciones.ifEmpty { "Sin recomendaciones" }}
                    """.trimIndent()
                }}
            """.trimIndent()
            
            mostrarDialogo("🔍 Diagnósticos Médicos", mensaje)
        } else {
            mostrarMensaje("No hay diagnósticos registrados en tu historial")
        }
    }
    
    private fun mostrarConsultas() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val pacienteId = sesion.usuario.obtenerId()
            val consultas = gestorHistorial.obtenerConsultasPasadas(pacienteId)
            
            if (consultas.isNotEmpty()) {
                val mensaje = """
                    🏥 Consultas Médicas:
                    
                    ${consultas.joinToString("\n\n") { consulta ->
                        consulta.obtenerDescripcionCompleta()
                    }}
                """.trimIndent()
                
                mostrarDialogo("🏥 Consultas Médicas", mensaje)
            } else {
                mostrarMensaje("No hay consultas registradas en tu historial")
            }
        }
    }
    
    private fun mostrarEnfermedades() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val pacienteId = sesion.usuario.obtenerId()
            val enfermedades = gestorHistorial.obtenerEnfermedadesCronicas(pacienteId)
            
            if (enfermedades.isNotEmpty()) {
                val mensaje = """
                    ⚠️ Enfermedades Crónicas:
                    
                    ${enfermedades.joinToString("\n\n") { enfermedad ->
                        enfermedad.obtenerDescripcionCompleta()
                    }}
                """.trimIndent()
                
                mostrarDialogo("⚠️ Enfermedades Crónicas", mensaje)
            } else {
                mostrarMensaje("No hay enfermedades crónicas registradas en tu historial")
            }
        }
    }
    
    private fun mostrarAlergias() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val pacienteId = sesion.usuario.obtenerId()
            val alergias = gestorHistorial.obtenerAlergiasRegistradas(pacienteId)
            
            if (alergias.isNotEmpty()) {
                val mensaje = """
                    🚨 Alergias Registradas:
                    
                    ${alergias.joinToString("\n\n") { alergia ->
                        alergia.obtenerDescripcionCompleta()
                    }}
                """.trimIndent()
                
                mostrarDialogo("🚨 Alergias Registradas", mensaje)
            } else {
                mostrarMensaje("No hay alergias registradas en tu historial")
            }
        }
    }
    
    private fun mostrarCirugias() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val pacienteId = sesion.usuario.obtenerId()
            val cirugias = gestorHistorial.obtenerCirugiasProcedimientos(pacienteId)
            
            if (cirugias.isNotEmpty()) {
                val mensaje = """
                    🔪 Cirugías y Procedimientos:
                    
                    ${cirugias.joinToString("\n\n") { cirugia ->
                        cirugia.obtenerDescripcionCompleta()
                    }}
                """.trimIndent()
                
                mostrarDialogo("🔪 Cirugías y Procedimientos", mensaje)
            } else {
                mostrarMensaje("No hay cirugías o procedimientos registrados en tu historial")
            }
        }
    }
    
    private fun mostrarAlertas() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val pacienteId = sesion.usuario.obtenerId()
            val alertas = gestorHistorial.obtenerAlertasMedicas(pacienteId)
            
            if (alertas.isNotEmpty()) {
                val mensaje = """
                    🚨 Alertas Médicas:
                    
                    ${alertas.joinToString("\n")}
                    
                    💡 Recomendaciones:
                    • Mantén tus citas al día
                    • Toma tus medicamentos según lo prescrito
                    • Sigue las recomendaciones médicas
                    • Consulta con tu médico si tienes dudas
                """.trimIndent()
                
                mostrarDialogo("🚨 Alertas Médicas", mensaje)
            } else {
                mostrarMensaje("No hay alertas médicas activas")
            }
        }
    }
    
    private fun mostrarDialogo(titulo: String, mensaje: String) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .create()
        
        dialog.show()
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
    
    private fun aplicarFiltroFecha() {
        mostrarDiagnosticosFiltrados()
    }
    
    private fun limpiarFiltro() {
        filtroFechaActual = "TODAS"
        spinnerFiltroFecha.setSelection(0)
        mostrarDiagnosticosFiltrados()
    }
    
    private fun mostrarDiagnosticosFiltrados() {
        val diagnosticosFiltrados = when (filtroFechaActual) {
            "ÚLTIMA SEMANA" -> filtrarPorUltimaSemana()
            "ÚLTIMO MES" -> filtrarPorUltimoMes()
            "ÚLTIMO AÑO" -> filtrarPorUltimoAno()
            else -> diagnosticos
        }
        
        mostrarDiagnosticosEnUI(diagnosticosFiltrados)
    }
    
    private fun filtrarPorUltimaSemana(): List<DiagnosticoMedico> {
        val unaSemanaAtras = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        return diagnosticos.filter { diagnostico ->
            try {
                val fechaDiagnostico = parsearFecha(diagnostico.fecha)
                println("DEBUG: Filtro semana - Fecha diagnóstico: ${diagnostico.fecha} -> $fechaDiagnostico")
                fechaDiagnostico >= unaSemanaAtras
            } catch (e: Exception) {
                println("DEBUG: Error al parsear fecha en filtro semana: ${e.message}")
                false
            }
        }
    }
    
    private fun filtrarPorUltimoMes(): List<DiagnosticoMedico> {
        val unMesAtras = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
        return diagnosticos.filter { diagnostico ->
            try {
                val fechaDiagnostico = parsearFecha(diagnostico.fecha)
                println("DEBUG: Filtro mes - Fecha diagnóstico: ${diagnostico.fecha} -> $fechaDiagnostico")
                fechaDiagnostico >= unMesAtras
            } catch (e: Exception) {
                println("DEBUG: Error al parsear fecha en filtro mes: ${e.message}")
                false
            }
        }
    }
    
    private fun filtrarPorUltimoAno(): List<DiagnosticoMedico> {
        val unAnoAtras = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000)
        return diagnosticos.filter { diagnostico ->
            try {
                val fechaDiagnostico = parsearFecha(diagnostico.fecha)
                println("DEBUG: Filtro año - Fecha diagnóstico: ${diagnostico.fecha} -> $fechaDiagnostico")
                fechaDiagnostico >= unAnoAtras
            } catch (e: Exception) {
                println("DEBUG: Error al parsear fecha en filtro año: ${e.message}")
                false
            }
        }
    }
    
    private fun parsearFecha(fechaString: String): Long {
        return try {
            // Intentar diferentes formatos de fecha
            val formatos = listOf(
                "dd/MM/yyyy HH:mm",
                "dd/MM/yyyy",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd"
            )
            
            for (formato in formatos) {
                try {
                    val dateFormat = java.text.SimpleDateFormat(formato, java.util.Locale.getDefault())
                    val fecha = dateFormat.parse(fechaString)
                    if (fecha != null) {
                        println("DEBUG: Fecha parseada correctamente con formato '$formato': $fechaString -> ${fecha.time}")
                        return fecha.time
                    }
                } catch (e: Exception) {
                    // Continuar con el siguiente formato
                }
            }
            
            // Si ningún formato funciona, devolver 0
            println("DEBUG: No se pudo parsear la fecha: $fechaString")
            0L
        } catch (e: Exception) {
            println("DEBUG: Error general al parsear fecha: $fechaString - ${e.message}")
            0L
        }
    }
    
    private fun mostrarDiagnosticosEnUI(diagnosticosFiltrados: List<DiagnosticoMedico>) {
        linearLayoutListaDiagnosticos.removeAllViews()
        
        println("DEBUG: Mostrando ${diagnosticosFiltrados.size} diagnósticos en UI")
        
        if (diagnosticosFiltrados.isEmpty()) {
            val textViewVacio = TextView(this)
            textViewVacio.text = "📭 No hay diagnósticos para mostrar con el filtro seleccionado"
            textViewVacio.textSize = 16f
            textViewVacio.setTextColor(resources.getColor(android.R.color.darker_gray, null))
            textViewVacio.gravity = android.view.Gravity.CENTER
            textViewVacio.setPadding(0, 32, 0, 32)
            linearLayoutListaDiagnosticos.addView(textViewVacio)
            return
        }
        
        // Ordenar por fecha (más reciente primero)
        val diagnosticosOrdenados = diagnosticosFiltrados.sortedByDescending { 
            parsearFecha(it.fecha) 
        }
        
        // Agregar título con contador
        val textViewTitulo = TextView(this)
        textViewTitulo.text = "🔍 Diagnósticos Médicos (${diagnosticosOrdenados.size} encontrados)"
        textViewTitulo.textSize = 18f
        textViewTitulo.setTypeface(null, android.graphics.Typeface.BOLD)
        textViewTitulo.setPadding(0, 16, 0, 16)
        linearLayoutListaDiagnosticos.addView(textViewTitulo)
        
        diagnosticosOrdenados.forEach { diagnostico ->
            agregarDiagnosticoCard(diagnostico)
        }
    }
    
    private fun agregarDiagnosticoCard(diagnostico: DiagnosticoMedico) {
        val cardView = android.view.LayoutInflater.from(this).inflate(
            R.layout.item_consulta_medica, linearLayoutListaDiagnosticos, false
        )
        
        // Configurar información del diagnóstico
        cardView.findViewById<TextView>(R.id.text_view_fecha_hora).text = "📅 ${diagnostico.fecha}"
        cardView.findViewById<TextView>(R.id.text_view_diagnostico).text = "🔍 ${diagnostico.diagnostico}"
        cardView.findViewById<TextView>(R.id.text_view_tratamiento).text = "💊 ${diagnostico.tratamiento}"
        cardView.findViewById<TextView>(R.id.text_view_estado).text = "✅ ${diagnostico.estado}"
        
        // Ocultar botones que no necesitamos para diagnósticos
        cardView.findViewById<Button>(R.id.button_iniciar).visibility = android.view.View.GONE
        cardView.findViewById<Button>(R.id.button_finalizar).visibility = android.view.View.GONE
        
        // Configurar botón para ver detalles
        cardView.findViewById<Button>(R.id.button_ver).setOnClickListener {
            mostrarDetallesDiagnostico(diagnostico)
        }
        
        // Cambiar texto del botón
        cardView.findViewById<Button>(R.id.button_ver).text = "Ver Detalles"
        
        // Agregar margen entre cards
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8)
        cardView.layoutParams = layoutParams
        
        linearLayoutListaDiagnosticos.addView(cardView)
        
        println("DEBUG: Agregado diagnóstico card - Fecha: ${diagnostico.fecha}, Diagnóstico: ${diagnostico.diagnostico}")
    }
    
    private fun mostrarDetallesDiagnostico(diagnostico: DiagnosticoMedico) {
        val mensaje = """
            🔍 DIAGNÓSTICO MÉDICO
            
            📅 Fecha: ${diagnostico.fecha}
            🔍 Diagnóstico: ${diagnostico.diagnostico}
            
            🔍 Síntomas:
            ${diagnostico.sintomas.ifEmpty { "No especificados" }}
            
            🏥 Examen Físico:
            ${diagnostico.examenFisico.ifEmpty { "No especificado" }}
            
            📋 Diagnósticos Secundarios:
            ${diagnostico.diagnosticosSecundarios.ifEmpty { "Ninguno" }}
            
            💊 Tratamiento:
            ${diagnostico.tratamiento.ifEmpty { "No especificado" }}
            
            💊 Medicamentos:
            ${diagnostico.medicamentos.ifEmpty { "No especificados" }}
            
            📝 Recomendaciones:
            ${diagnostico.recomendaciones.ifEmpty { "No especificadas" }}
            
            📅 Próxima Cita:
            ${diagnostico.proximaCita.ifEmpty { "No programada" }}
            
            ✅ Estado: ${diagnostico.estado}
        """.trimIndent()
        
        mostrarDialogo("🔍 Detalles del Diagnóstico", mensaje)
    }
} 