package com.example.softmedv5

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.MedioCita
import com.example.softmedv5.modelo.Paciente
import com.example.softmedv5.util.GestorAutenticacion
import com.example.softmedv5.util.GestorCitas
import com.example.softmedv5.util.ResultadoCita
import java.text.SimpleDateFormat
import java.util.*

class SolicitarCitaActivity : AppCompatActivity() {
    
    private lateinit var editTextFecha: EditText
    private lateinit var editTextHora: EditText
    private lateinit var spinnerEspecialidad: Spinner
    private lateinit var spinnerMedio: Spinner
    private lateinit var editTextOpcion: EditText
    private lateinit var buttonSolicitar: Button
    private lateinit var buttonVolver: Button
    private lateinit var textViewTitulo: TextView
    
    private val gestorCitas = GestorCitas.obtenerInstancia()
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_cita)
        
        inicializarVistas()
        configurarEventos()
        configurarSpinners()
        validarDatosPaciente()
    }
    
    private fun inicializarVistas() {
        editTextFecha = findViewById(R.id.edit_text_fecha)
        editTextHora = findViewById(R.id.edit_text_hora)
        spinnerEspecialidad = findViewById(R.id.spinner_especialidad)
        spinnerMedio = findViewById(R.id.spinner_medio)
        editTextOpcion = findViewById(R.id.edit_text_opcion)
        buttonSolicitar = findViewById(R.id.button_solicitar)
        buttonVolver = findViewById(R.id.button_volver)
        textViewTitulo = findViewById(R.id.text_view_titulo)
        
        textViewTitulo.text = "📅 Solicitar Cita Médica"
    }
    
    private fun validarDatosPaciente() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null && sesion.usuario is Paciente) {
            val paciente = sesion.usuario as Paciente
            
            // Validar información básica (nombre y email)
            if (!paciente.tieneInformacionBasicaCompleta()) {
                val camposBasicosFaltantes = paciente.obtenerCamposBasicosFaltantes()
                val mensaje = """
                    ⚠️ Información Básica Incompleta
                    
                    Para solicitar citas médicas, es necesario que su información básica esté completa.
                    
                    📋 Campos faltantes:
                    ${camposBasicosFaltantes.joinToString("\n• ", "• ")}
                    
                    🔧 Contacte al personal administrativo para completar su información básica.
                    
                    📞 Contacto: (555) 123-4567
                    📧 Email: admin@softmed.com
                """.trimIndent()
                
                android.app.AlertDialog.Builder(this)
                    .setTitle("⚠️ Información Básica Incompleta")
                    .setMessage(mensaje)
                    .setPositiveButton("Entendido") { _, _ -> }
                    .setNegativeButton("Contactar Administrativo") { _, _ ->
                        mostrarOpcionesContacto()
                    }
                    .setCancelable(false)
                    .show()
                return
            }
            
            if (!paciente.tieneDatosPersonalesCompletos()) {
                mostrarAdvertenciaDatosIncompletos(paciente)
            }
        }
    }
    
    private fun mostrarAdvertenciaDatosIncompletos(paciente: Paciente) {
        val camposFaltantes = paciente.obtenerCamposFaltantes()
        val porcentajeCompletitud = paciente.obtenerPorcentajeCompletitud()
        
        val mensaje = """
            ⚠️ Datos Personales Incompletos
            
            Para solicitar citas médicas, es necesario tener todos los datos personales completos.
            
            📊 Completitud actual: $porcentajeCompletitud%
            
            📋 Campos faltantes:
            ${camposFaltantes.joinToString("\n• ", "• ")}
            
            🔧 Para completar tus datos:
            1. Ve a "Gestionar Información de Contacto"
            2. Completa todos los campos requeridos
            3. Guarda la información
            4. Regresa aquí para solicitar tu cita
            
            💡 Los datos del paciente son cruciales para:
            • Programar citas correctamente
            • Contactar en caso de cambios
            • Verificar cobertura médica
            • Generar documentación clínica
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("⚠️ Datos Incompletos")
            .setMessage(mensaje)
            .setPositiveButton("Completar Datos") { _, _ ->
                abrirInformacionContacto()
            }
            .setNegativeButton("Continuar de Todos Modos") { _, _ ->
                // Permitir continuar pero mostrar advertencia
                mostrarMensaje("⚠️ Se recomienda completar los datos antes de solicitar la cita")
            }
            .setCancelable(false)
            .show()
    }
    
    private fun abrirInformacionContacto() {
        val intent = Intent(this, InformacionContactoActivity::class.java)
        startActivity(intent)
    }
    
    private fun configurarEventos() {
        editTextFecha.setOnClickListener {
            mostrarSelectorFecha()
        }
        
        editTextHora.setOnClickListener {
            mostrarSelectorHora()
        }
        
        buttonSolicitar.setOnClickListener {
            solicitarCita()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun configurarSpinners() {
        // Configurar spinner de especialidades
        val especialidades = gestorCitas.especialidadesDisponibles
        val adapterEspecialidades = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            especialidades
        )
        adapterEspecialidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEspecialidad.adapter = adapterEspecialidades
        
        // Configurar spinner de medios
        val medios = MedioCita.values().map { it.descripcion }
        val adapterMedios = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            medios
        )
        adapterMedios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedio.adapter = adapterMedios
    }
    
    private fun mostrarSelectorFecha() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                editTextFecha.setText(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        // Establecer fecha mínima como hoy
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    
    private fun mostrarSelectorHora() {
        // Obtener las horas disponibles del gestor de citas
        val horariosDisponibles = gestorCitas.horariosDisponibles
        
        // Crear un diálogo personalizado para seleccionar hora
        val horas = horariosDisponibles.map { it.split(":")[0].toInt() }.distinct().sorted()
        val minutos = listOf(0, 30)
        
        val horasArray = horas.map { String.format("%02d", it) }.toTypedArray()
        val minutosArray = minutos.map { String.format("%02d", it) }.toTypedArray()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Seleccionar Hora")
            .setItems(horariosDisponibles.toTypedArray()) { _, which ->
                val horaSeleccionada = horariosDisponibles[which]
                editTextHora.setText(horaSeleccionada)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun solicitarCita() {
        // Validar campos
        if (!validarCampos()) {
            return
        }
        
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion == null) {
            mostrarMensaje("Error: No hay sesión activa")
            return
        }
        
        // Validar que sea un paciente con datos completos
        if (sesion.usuario is Paciente) {
            val paciente = sesion.usuario as Paciente
            
            // Validar información básica (nombre y email)
            if (!paciente.tieneInformacionBasicaCompleta()) {
                val camposBasicosFaltantes = paciente.obtenerCamposBasicosFaltantes()
                val mensaje = """
                    ⚠️ Información Básica Incompleta
                    
                    Para solicitar citas médicas, es necesario que su información básica esté completa.
                    
                    📋 Campos faltantes:
                    ${camposBasicosFaltantes.joinToString("\n• ", "• ")}
                    
                    🔧 Contacte al personal administrativo para completar su información básica.
                    
                    📞 Contacto: (555) 123-4567
                    📧 Email: admin@softmed.com
                """.trimIndent()
                
                android.app.AlertDialog.Builder(this)
                    .setTitle("⚠️ Información Básica Incompleta")
                    .setMessage(mensaje)
                    .setPositiveButton("Entendido") { _, _ -> }
                    .setNegativeButton("Contactar Administrativo") { _, _ ->
                        mostrarOpcionesContacto()
                    }
                    .setCancelable(false)
                    .show()
                return
            }
            
            if (!paciente.tieneDatosPersonalesCompletos()) {
                mostrarAdvertenciaDatosIncompletos(paciente)
                return
            }
        }
        
        val pacienteId = sesion.usuario.obtenerId()
        val fecha = editTextFecha.text.toString()
        val hora = editTextHora.text.toString()
        val especialidad = spinnerEspecialidad.selectedItem.toString()
        val medio = when (spinnerMedio.selectedItemPosition) {
            0 -> MedioCita.PRESENCIAL
            1 -> MedioCita.VIRTUAL
            else -> MedioCita.PRESENCIAL
        }
        val opcion = editTextOpcion.text.toString()
        
        // Obtener información del paciente desde la sesión
        val nombrePaciente = sesion.usuario.obtenerNombre()
        val emailPaciente = sesion.usuario.obtenerEmail()
        
        // Obtener información adicional del paciente si es un Paciente
        val telefonoPaciente = if (sesion.usuario is Paciente) {
            (sesion.usuario as Paciente).obtenerTelefono()
        } else {
            "No disponible"
        }
        
        val documentoPaciente = if (sesion.usuario is Paciente) {
            (sesion.usuario as Paciente).obtenerDocumentoIdentidad()
        } else {
            "No disponible"
        }
        
        val edadPaciente = if (sesion.usuario is Paciente) {
            (sesion.usuario as Paciente).obtenerEdad()
        } else {
            0
        }
        
        // Crear la cita con toda la información del paciente
        val resultado = gestorCitas.crearCita(
            pacienteId = pacienteId,
            fecha = fecha,
            hora = hora,
            especialidad = especialidad,
            medio = medio,
            opcion = opcion,
            nombrePaciente = nombrePaciente,
            emailPaciente = emailPaciente,
            telefonoPaciente = telefonoPaciente,
            documentoPaciente = documentoPaciente,
            edadPaciente = edadPaciente
        )
        
        when (resultado) {
            is ResultadoCita.Exito -> {
                mostrarCitaCreada(resultado.cita)
            }
            is ResultadoCita.Error -> {
                mostrarMensaje("Error: ${resultado.mensaje}")
            }
        }
    }
    
    private fun validarCampos(): Boolean {
        if (editTextFecha.text.isNullOrBlank()) {
            mostrarMensaje("Por favor selecciona una fecha")
            return false
        }
        
        if (editTextHora.text.isNullOrBlank()) {
            mostrarMensaje("Por favor selecciona una hora")
            return false
        }
        
        if (editTextOpcion.text.isNullOrBlank()) {
            mostrarMensaje("Por favor ingresa una opción o motivo de consulta")
            return false
        }
        
        return true
    }
    
    private fun mostrarCitaCreada(cita: com.example.softmedv5.modelo.Cita) {
        val mensaje = """
            ✅ Cita creada exitosamente!
            
            ${cita.obtenerDescripcionCompleta()}
            
            📧 Recibirás una confirmación por email.
            📱 También puedes consultar tus citas en cualquier momento.
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Cita Creada")
            .setMessage(mensaje)
            .setPositiveButton("Ver Mis Citas") { _, _ ->
                abrirMisCitas()
            }
            .setNegativeButton("Nueva Cita") { _, _ ->
                limpiarFormulario()
            }
            .setNeutralButton("Volver") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun abrirMisCitas() {
        val intent = Intent(this, MisCitasActivity::class.java)
        startActivity(intent)
    }
    
    private fun limpiarFormulario() {
        editTextFecha.text.clear()
        editTextHora.text.clear()
        editTextOpcion.text.clear()
        spinnerEspecialidad.setSelection(0)
        spinnerMedio.setSelection(0)
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    private fun mostrarOpcionesContacto() {
        val opciones = arrayOf(
            "📞 Llamar al administrativo",
            "📧 Enviar email",
            "💬 Chat con soporte",
            "📋 Ver horarios de atención"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📞 Contactar Personal Administrativo")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarMensaje("📞 Llamando al administrativo... (555) 123-4567")
                    1 -> mostrarMensaje("📧 Redirigiendo a email: admin@softmed.com")
                    2 -> mostrarMensaje("💬 Abriendo chat de soporte...")
                    3 -> mostrarHorariosAtencion()
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarHorariosAtencion() {
        val horarios = """
            🕒 Horarios de Atención Administrativa:
            
            📅 Lunes a Viernes:
            ⏰ 8:00 AM - 6:00 PM
            
            📅 Sábados:
            ⏰ 8:00 AM - 12:00 PM
            
            📅 Domingos:
            ❌ Cerrado
            
            📞 Teléfono: (555) 123-4567
            📧 Email: admin@softmed.com
            🏥 Ubicación: Piso 1, Oficina 101
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("�� Horarios de Atención")
            .setMessage(horarios)
            .setPositiveButton("Entendido") { _, _ -> }
            .show()
    }
} 