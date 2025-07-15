package com.example.softmedv5

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.Cita
import com.example.softmedv5.modelo.EstadoCita
import com.example.softmedv5.modelo.MedioCita
import com.example.softmedv5.util.GestorCitas
import com.example.softmedv5.util.ResultadoCita
import com.example.softmedv5.util.ServicioDependencias
import java.util.*

class GestionCitasAdministrativoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonMostrarResumen: Button
    private lateinit var spinnerFiltro: Spinner
    private lateinit var linearLayoutCitas: LinearLayout
    private lateinit var textViewSinCitas: TextView
    private lateinit var buttonVolver: Button
    
    private val gestorCitas = GestorCitas.obtenerInstancia()
    private val calendar = Calendar.getInstance()
    private var resumenVisible = false
    private var filtroActual = "TODAS"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_citas_administrativo)
        
        inicializarVistas()
        configurarEventos()
        configurarSpinnerFiltro()
        
        // Crear citas de demostración si no hay ninguna
        gestorCitas.crearCitasDemostracion()
        
        cargarCitasPendientes()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonMostrarResumen = findViewById(R.id.button_mostrar_resumen)
        spinnerFiltro = findViewById(R.id.spinner_filtro)
        linearLayoutCitas = findViewById(R.id.linear_layout_citas)
        textViewSinCitas = findViewById(R.id.text_view_sin_citas)
        buttonVolver = findViewById(R.id.button_volver)
        
        textViewTitulo.text = "📅 Gestión de Citas - Personal Administrativo"
    }
    
    private fun configurarEventos() {
        buttonVolver.setOnClickListener {
            finish()
        }
        
        buttonMostrarResumen.setOnClickListener {
            toggleResumen()
        }
    }
    
    private fun configurarSpinnerFiltro() {
        val opcionesFiltro = arrayOf(
            "TODAS",
            "PENDIENTES",
            "CONFIRMADAS", 
            "CANCELADAS",
            "COMPLETADAS"
        )
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesFiltro)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFiltro.adapter = adapter
        
        spinnerFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filtroActual = opcionesFiltro[position]
                aplicarFiltro()
            }
            
            override fun onNothingSelected(parent: AdapterView<*>?) {
                filtroActual = "TODAS"
                aplicarFiltro()
            }
        }
    }
    
    private fun aplicarFiltro() {
        val todasLasCitas = obtenerTodasLasCitas()
        val citasFiltradas = when (filtroActual) {
            "PENDIENTES" -> todasLasCitas.filter { it.estado == EstadoCita.PENDIENTE }
            "CONFIRMADAS" -> todasLasCitas.filter { it.estado == EstadoCita.CONFIRMADA }
            "CANCELADAS" -> todasLasCitas.filter { it.estado == EstadoCita.CANCELADA }
            "COMPLETADAS" -> todasLasCitas.filter { it.estado == EstadoCita.COMPLETADA }
            else -> todasLasCitas
        }
        
        mostrarCitasFiltradas(citasFiltradas)
    }
    
    private fun mostrarCitasFiltradas(citas: List<Cita>) {
        // Limpiar la lista actual
        linearLayoutCitas.removeAllViews()
        
        if (citas.isEmpty()) {
            mostrarSinCitas()
            return
        }
        
        // Agrupar por estado
        val citasPendientes = citas.filter { it.estado == EstadoCita.PENDIENTE }
        val citasConfirmadas = citas.filter { it.estado == EstadoCita.CONFIRMADA }
        val citasCanceladas = citas.filter { it.estado == EstadoCita.CANCELADA }
        val citasCompletadas = citas.filter { it.estado == EstadoCita.COMPLETADA }
        
        // Mostrar citas por estado
        if (citasPendientes.isNotEmpty()) {
            agregarTituloSeccion("⏳ Citas Pendientes de Confirmación")
            citasPendientes.forEach { cita ->
                agregarCitaAdministrativa(cita, true)
            }
        }
        
        if (citasConfirmadas.isNotEmpty()) {
            agregarTituloSeccion("✅ Citas Confirmadas")
            citasConfirmadas.forEach { cita ->
                agregarCitaAdministrativa(cita, false)
            }
        }
        
        if (citasCompletadas.isNotEmpty()) {
            agregarTituloSeccion("✅ Citas Completadas")
            citasCompletadas.forEach { cita ->
                agregarCitaAdministrativa(cita, false)
            }
        }
        
        if (citasCanceladas.isNotEmpty()) {
            agregarTituloSeccion("❌ Citas Canceladas")
            citasCanceladas.forEach { cita ->
                agregarCitaAdministrativa(cita, false)
            }
        }
    }
    
    private fun toggleResumen() {
        resumenVisible = !resumenVisible
        
        if (resumenVisible) {
            textViewResumen.visibility = View.VISIBLE
            buttonMostrarResumen.text = "📊 Ocultar Resumen de Citas"
            mostrarResumen()
        } else {
            textViewResumen.visibility = View.GONE
            buttonMostrarResumen.text = "📊 Mostrar Resumen de Citas"
        }
    }
    
    private fun cargarCitasPendientes() {
        // Aplicar el filtro actual
        aplicarFiltro()
    }
    
    private fun obtenerTodasLasCitas(): List<Cita> {
        // Usar el método del gestor de citas
        return gestorCitas.obtenerTodasLasCitas()
    }
    
    private fun mostrarResumen() {
        val todasLasCitas = obtenerTodasLasCitas()
        val citasPendientes = todasLasCitas.filter { it.estado == EstadoCita.PENDIENTE }
        val citasConfirmadas = todasLasCitas.filter { it.estado == EstadoCita.CONFIRMADA }
        val citasCanceladas = todasLasCitas.filter { it.estado == EstadoCita.CANCELADA }
        val citasCompletadas = todasLasCitas.filter { it.estado == EstadoCita.COMPLETADA }
        
        // Estadísticas por especialidad
        val especialidades = todasLasCitas.groupBy { it.especialidad }
        val especialidadesMasSolicitadas = especialidades.entries
            .sortedByDescending { it.value.size }
            .take(3)
        
        // Estadísticas por medio
        val citasPresenciales = todasLasCitas.count { cita -> cita.medio == MedioCita.PRESENCIAL }
        val citasVirtuales = todasLasCitas.count { cita -> cita.medio == MedioCita.VIRTUAL }
        
        val resumen = """
            📊 Resumen Detallado de Gestión de Citas:
            
            📈 ESTADÍSTICAS GENERALES:
            📅 Total de citas: ${todasLasCitas.size}
            ⏳ Pendientes de confirmación: ${citasPendientes.size}
            ✅ Confirmadas: ${citasConfirmadas.size}
            ❌ Canceladas: ${citasCanceladas.size}
            ✅ Completadas: ${citasCompletadas.size}
            
            🏥 DISTRIBUCIÓN POR ESPECIALIDAD:
            ${especialidadesMasSolicitadas.joinToString("\n") { "• ${it.key}: ${it.value.size} citas" }}
            
            💻 DISTRIBUCIÓN POR MEDIO:
            📍 Presenciales: $citasPresenciales
            💻 Virtuales: $citasVirtuales
            
            💡 ACCIONES ADMINISTRATIVAS DISPONIBLES:
            • 👁️ Ver detalles completos de cada cita
            • ✅ Confirmar citas pendientes
            • 🔄 Reprogramar citas confirmadas
            • ❌ Cancelar citas por solicitud
            • 📊 Generar reportes de gestión
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun agregarTituloSeccion(titulo: String) {
        val textView = TextView(this)
        textView.text = titulo
        textView.textSize = 20f
        textView.setTypeface(null, android.graphics.Typeface.BOLD)
        textView.setTextColor(resources.getColor(R.color.colorPrimary, null))
        textView.setPadding(0, 32, 0, 16)
        textView.gravity = android.view.Gravity.CENTER
        
        linearLayoutCitas.addView(textView)
    }
    
    private fun agregarCitaAdministrativa(cita: Cita, esPendiente: Boolean) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_cita_administrativa,
            linearLayoutCitas,
            false
        )
        
        // Configurar información de la cita
        val textViewInfo = cardView.findViewById<TextView>(R.id.text_view_info_cita)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado)
        val buttonVerDetalles = cardView.findViewById<Button>(R.id.button_ver_detalles)
        val buttonConfirmar = cardView.findViewById<Button>(R.id.button_confirmar)
        val buttonReprogramar = cardView.findViewById<Button>(R.id.button_reprogramar)
        val buttonCancelar = cardView.findViewById<Button>(R.id.button_cancelar)
        
        // Usar la descripción que incluye información del paciente
        textViewInfo.text = cita.obtenerDescripcionConPaciente()
        
        // Configurar botón de ver detalles
        buttonVerDetalles.setOnClickListener {
            mostrarDetallesCita(cita)
        }
        
        // Configurar estado con emoji y color
        when (cita.estado) {
            EstadoCita.CONFIRMADA -> {
                textViewEstado.text = "✅ Confirmada"
                textViewEstado.setBackgroundResource(R.drawable.button_background)
            }
            EstadoCita.PENDIENTE -> {
                textViewEstado.text = "⏳ Pendiente"
                textViewEstado.setBackgroundResource(R.drawable.button_background)
            }
            EstadoCita.CANCELADA -> {
                textViewEstado.text = "❌ Cancelada"
                textViewEstado.setBackgroundResource(R.drawable.button_cerrar_sesion_background)
            }
            else -> {
                textViewEstado.text = "❓ Desconocido"
                textViewEstado.setBackgroundResource(R.drawable.button_background)
            }
        }
        
        // Configurar botones según el estado
        if (esPendiente) {
            buttonConfirmar.visibility = View.VISIBLE
            buttonConfirmar.setOnClickListener {
                confirmarCita(cita)
            }
        } else {
            buttonConfirmar.visibility = View.GONE
        }
        
        if (cita.estado != EstadoCita.CANCELADA) {
            buttonReprogramar.visibility = View.VISIBLE
            buttonReprogramar.setOnClickListener {
                mostrarReprogramarCita(cita)
            }
            
            buttonCancelar.visibility = View.VISIBLE
            buttonCancelar.setOnClickListener {
                confirmarCancelarCita(cita)
            }
        } else {
            buttonReprogramar.visibility = View.GONE
            buttonCancelar.visibility = View.GONE
        }
        
        linearLayoutCitas.addView(cardView)
    }
    
    private fun mostrarDetallesCita(cita: Cita) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_detalles_cita, null)
        
        // Configurar información del paciente
        dialogView.findViewById<TextView>(R.id.text_view_paciente_detalle).text = "Nombre: ${cita.nombrePaciente}"
        dialogView.findViewById<TextView>(R.id.text_view_edad_paciente).text = "Edad: ${cita.edadPaciente} años"
        dialogView.findViewById<TextView>(R.id.text_view_telefono_paciente).text = "Teléfono: ${cita.telefonoPaciente}"
        dialogView.findViewById<TextView>(R.id.text_view_email_paciente).text = "Email: ${cita.emailPaciente}"
        
        // Configurar información de la cita
        dialogView.findViewById<TextView>(R.id.text_view_fecha_detalle).text = "📅 Fecha: ${cita.obtenerFechaFormateada()}"
        dialogView.findViewById<TextView>(R.id.text_view_hora_detalle).text = "⏰ Hora: ${cita.obtenerHoraFormateada()}"
        dialogView.findViewById<TextView>(R.id.text_view_especialidad_detalle).text = "🏥 Especialidad: ${cita.especialidad}"
        dialogView.findViewById<TextView>(R.id.text_view_motivo_detalle).text = "📝 Motivo: ${cita.opcion}"
        dialogView.findViewById<TextView>(R.id.text_view_tipo_detalle).text = "📍 Tipo: ${cita.medio.descripcion}"
        dialogView.findViewById<TextView>(R.id.text_view_estado_detalle).text = "✅ Estado: ${cita.estado.descripcion}"
        
        // Configurar información del historial médico
        val gestorHistorial = ServicioDependencias.obtenerGestorHistorial()
        val historial = gestorHistorial.obtenerHistorialClinico(cita.pacienteId)
        
        val textViewHistorial = dialogView.findViewById<TextView>(R.id.text_view_historial)
        if (historial != null) {
            if (historial.estaVacio()) {
                textViewHistorial.text = """
                    📋 Historial Clínico Vacío
                    
                    Este es un paciente nuevo sin registros médicos previos.
                    El historial se actualizará automáticamente después de cada atención médica.
                    
                    📅 Fecha de creación: ${historial.fechaCreacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
                """.trimIndent()
            } else {
                textViewHistorial.text = historial.obtenerResumen()
            }
        } else {
            textViewHistorial.text = """
                📋 Historial Clínico No Disponible
                
                No se encontró historial clínico para este paciente.
                Se creará automáticamente cuando el médico realice la primera atención.
            """.trimIndent()
        }
        
        // Ocultar botones que no aplican para personal administrativo
        dialogView.findViewById<Button>(R.id.button_iniciar_atencion).visibility = View.GONE
        dialogView.findViewById<Button>(R.id.button_completada_detalle).visibility = View.GONE
        dialogView.findViewById<Button>(R.id.button_no_asistio_detalle).visibility = View.GONE
        
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        
        dialog.show()
    }
    
    private fun confirmarCita(cita: Cita) {
        android.app.AlertDialog.Builder(this)
            .setTitle("✅ Confirmar Cita")
            .setMessage("¿Confirmar esta cita?\n\n${cita.obtenerDescripcionConPaciente()}")
            .setPositiveButton("Sí, Confirmar") { _, _ ->
                confirmarCitaAccion(cita)
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }
    
    private fun confirmarCitaAccion(cita: Cita) {
        val resultado = gestorCitas.confirmarCita(cita.id)
        
        when (resultado) {
            is ResultadoCita.Exito -> {
                mostrarMensaje("✅ Cita confirmada exitosamente")
                aplicarFiltro() // Recargar la lista con filtro actual
                if (resumenVisible) {
                    mostrarResumen() // Actualizar resumen si está visible
                }
            }
            is ResultadoCita.Error -> {
                mostrarMensaje("❌ Error: ${resultado.mensaje}")
            }
        }
    }
    
    private fun confirmarCancelarCita(cita: Cita) {
        android.app.AlertDialog.Builder(this)
            .setTitle("❌ Cancelar Cita")
            .setMessage("¿Está seguro de cancelar esta cita?\n\n${cita.obtenerDescripcionConPaciente()}")
            .setPositiveButton("Sí, Cancelar") { _, _ ->
                cancelarCitaAccion(cita)
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }
    
    private fun cancelarCitaAccion(cita: Cita) {
        val resultado = gestorCitas.cancelarCita(cita.id)
        
        when (resultado) {
            is ResultadoCita.Exito -> {
                mostrarMensaje("❌ Cita cancelada exitosamente")
                aplicarFiltro() // Recargar la lista con filtro actual
                if (resumenVisible) {
                    mostrarResumen() // Actualizar resumen si está visible
                }
            }
            is ResultadoCita.Error -> {
                mostrarMensaje("❌ Error: ${resultado.mensaje}")
            }
        }
    }
    
    private fun mostrarReprogramarCita(cita: Cita) {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("🔄 Reprogramar Cita")
            .setMessage("Seleccione la nueva fecha y hora para la cita:\n\n${cita.obtenerDescripcionConPaciente()}")
            .setPositiveButton("Seleccionar Fecha", null)
            .setNegativeButton("Cancelar") { _, _ -> }
            .create()
        
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                mostrarSelectorFecha(cita, dialog)
            }
        }
        
        dialog.show()
    }
    
    private fun mostrarSelectorFecha(cita: Cita, dialog: android.app.AlertDialog) {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                mostrarSelectorHora(cita, dialog)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        // Establecer fecha mínima como hoy
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    
    private fun mostrarSelectorHora(cita: Cita, dialog: android.app.AlertDialog) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                
                reprogramarCitaAccion(cita, calendar.time, dialog)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        
        timePickerDialog.show()
    }
    
    private fun reprogramarCitaAccion(cita: Cita, nuevaFecha: Date, dialog: android.app.AlertDialog) {
        val resultado = gestorCitas.reprogramarCita(cita.id, nuevaFecha)
        
        when (resultado) {
            is ResultadoCita.Exito -> {
                dialog.dismiss()
                mostrarMensaje("🔄 Cita reprogramada exitosamente")
                aplicarFiltro() // Recargar la lista con filtro actual
                if (resumenVisible) {
                    mostrarResumen() // Actualizar resumen si está visible
                }
            }
            is ResultadoCita.Error -> {
                mostrarMensaje("❌ Error: ${resultado.mensaje}")
            }
        }
    }
    
    private fun mostrarSinCitas() {
        textViewSinCitas.visibility = View.VISIBLE
        linearLayoutCitas.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
} 