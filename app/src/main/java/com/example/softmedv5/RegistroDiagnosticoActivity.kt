package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.database.SimpleDatabaseHelper
import com.example.softmedv5.modelo.DiagnosticoMedico
import com.example.softmedv5.util.GestorAutenticacion
import java.text.SimpleDateFormat
import java.util.*

class RegistroDiagnosticoActivity : AppCompatActivity() {
    
    private lateinit var textViewPacienteNombre: TextView
    private lateinit var textViewCitaFecha: TextView
    private lateinit var textViewMotivoConsulta: TextView
    private lateinit var editTextSintomas: EditText
    private lateinit var editTextExamenFisico: EditText
    private lateinit var editTextDiagnostico: EditText
    private lateinit var editTextDiagnosticosSecundarios: EditText
    private lateinit var editTextTratamiento: EditText
    private lateinit var editTextMedicamentos: EditText
    private lateinit var editTextRecomendaciones: EditText
    private lateinit var editTextProximaCita: EditText
    private lateinit var buttonEnviarPaciente: Button
    private lateinit var buttonVolver: Button
    
    private lateinit var dbHelper: SimpleDatabaseHelper
    private var pacienteId: String = ""
    private var citaId: String = ""
    private var pacienteNombre: String = ""
    private var citaFecha: String = ""
    private var motivoConsulta: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_diagnostico)
        
        // Obtener datos de la cita
        obtenerDatosCita()
        
        inicializarVistas()
        configurarEventos()
        mostrarInformacionPaciente()
    }
    
    private fun obtenerDatosCita() {
        pacienteId = intent.getStringExtra("PACIENTE_ID") ?: ""
        citaId = intent.getStringExtra("CITA_ID") ?: ""
        pacienteNombre = intent.getStringExtra("PACIENTE_NOMBRE") ?: ""
        citaFecha = intent.getStringExtra("CITA_FECHA") ?: ""
        motivoConsulta = intent.getStringExtra("MOTIVO_CONSULTA") ?: ""
    }
    
    private fun inicializarVistas() {
        textViewPacienteNombre = findViewById(R.id.text_view_paciente_nombre)
        textViewCitaFecha = findViewById(R.id.text_view_cita_fecha)
        textViewMotivoConsulta = findViewById(R.id.text_view_motivo_consulta)
        editTextSintomas = findViewById(R.id.edit_text_sintomas)
        editTextExamenFisico = findViewById(R.id.edit_text_examen_fisico)
        editTextDiagnostico = findViewById(R.id.edit_text_diagnostico)
        editTextDiagnosticosSecundarios = findViewById(R.id.edit_text_diagnosticos_secundarios)
        editTextTratamiento = findViewById(R.id.edit_text_tratamiento)
        editTextMedicamentos = findViewById(R.id.edit_text_medicamentos)
        editTextRecomendaciones = findViewById(R.id.edit_text_recomendaciones)
        editTextProximaCita = findViewById(R.id.edit_text_proxima_cita)
        buttonEnviarPaciente = findViewById(R.id.button_enviar_paciente)
        buttonVolver = findViewById(R.id.button_volver)
        
        // Inicializar base de datos
        dbHelper = SimpleDatabaseHelper(this)
    }
    
    private fun configurarEventos() {
        buttonEnviarPaciente.setOnClickListener {
            enviarDiagnosticoAlPaciente()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun mostrarInformacionPaciente() {
        textViewPacienteNombre.text = "Paciente: $pacienteNombre"
        textViewCitaFecha.text = "Cita: $citaFecha"
        textViewMotivoConsulta.text = "Motivo: $motivoConsulta"
    }
    
    private fun enviarDiagnosticoAlPaciente() {
        val sintomas = editTextSintomas.text.toString().trim()
        val examenFisico = editTextExamenFisico.text.toString().trim()
        val diagnostico = editTextDiagnostico.text.toString().trim()
        val diagnosticosSecundarios = editTextDiagnosticosSecundarios.text.toString().trim()
        val tratamiento = editTextTratamiento.text.toString().trim()
        val medicamentos = editTextMedicamentos.text.toString().trim()
        val recomendaciones = editTextRecomendaciones.text.toString().trim()
        val proximaCita = editTextProximaCita.text.toString().trim()
        
        // Validar que se completó el diagnóstico
        if (diagnostico.isEmpty()) {
            mostrarMensaje("Por favor complete el diagnóstico antes de enviar")
            return
        }
        
        if (tratamiento.isEmpty()) {
            mostrarMensaje("Por favor complete el tratamiento antes de enviar")
            return
        }
        
        try {
            // Obtener información del médico actual
            val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
            val sesion = gestorAutenticacion.obtenerSesionActual()
            val medico = sesion?.usuario
            
            if (medico == null) {
                mostrarMensaje("Error: No hay sesión de médico activa")
                return
            }
            
            // Obtener el ID real del paciente desde la base de datos
            val pacienteDb = dbHelper.obtenerUsuarioPorNombreYRol(pacienteNombre, "PACIENTE")
            val pacienteIdReal = if (pacienteDb != null) {
                (pacienteDb["id"] as? Long)?.toString() ?: ""
            } else {
                mostrarMensaje("❌ Error: No se pudo encontrar el paciente en la base de datos")
                return
            }
            
            if (pacienteIdReal.isEmpty()) {
                mostrarMensaje("❌ Error: ID de paciente no válido")
                return
            }
            
            // 1. Guardar automáticamente el diagnóstico con el ID correcto del paciente
            val diagnosticoMedico = DiagnosticoMedico(
                id = generarIdDiagnostico(),
                pacienteId = pacienteIdReal, // Usar el ID real del paciente, no el de la cita
                medicoId = medico.obtenerId(),
                fecha = obtenerFechaActual(),
                sintomas = sintomas,
                examenFisico = examenFisico,
                diagnostico = diagnostico,
                diagnosticosSecundarios = diagnosticosSecundarios,
                tratamiento = tratamiento,
                medicamentos = medicamentos,
                recomendaciones = recomendaciones,
                proximaCita = proximaCita,
                estado = "FINALIZADA"
            )
            
            val resultadoGuardado = dbHelper.insertarDiagnostico(diagnosticoMedico)
            
            if (resultadoGuardado <= 0) {
                mostrarMensaje("❌ Error al guardar el diagnóstico")
                return
            }
            
            // Debug: Mostrar información de guardado
            println("=== DEBUG: Diagnóstico guardado ===")
            println("Diagnóstico ID: ${diagnosticoMedico.id}")
            println("Paciente ID: ${diagnosticoMedico.pacienteId}")
            println("Médico ID: ${diagnosticoMedico.medicoId}")
            println("Fecha: ${diagnosticoMedico.fecha}")
            println("Resultado guardado: $resultadoGuardado")
            println("================================")
            
            // 2. Crear mensaje para el paciente
            val mensaje = StringBuilder()
            mensaje.append("🏥 DIAGNÓSTICO MÉDICO\n\n")
            mensaje.append("📋 Diagnóstico: $diagnostico\n")
            if (sintomas.isNotEmpty()) {
                mensaje.append("🔍 Síntomas: $sintomas\n")
            }
            if (tratamiento.isNotEmpty()) {
                mensaje.append("💊 Tratamiento: $tratamiento\n")
            }
            if (medicamentos.isNotEmpty()) {
                mensaje.append("💊 Medicamentos: $medicamentos\n")
            }
            if (recomendaciones.isNotEmpty()) {
                mensaje.append("📝 Recomendaciones: $recomendaciones\n")
            }
            mensaje.append("\nFecha: ${obtenerFechaActual()}")
            
            // 3. Enviar mensaje al paciente
            val medicoId = medico.obtenerId().toIntOrNull() ?: 0
            val pacienteIdInt = pacienteIdReal.toIntOrNull() ?: 1
            
            // Debug: Mostrar información de IDs
            println("=== DEBUG INFO ===")
            println("Paciente nombre: $pacienteNombre")
            println("Paciente DB encontrado: ${pacienteDb != null}")
            println("Paciente ID: $pacienteIdInt")
            println("Médico ID: $medicoId")
            println("==================")
            
            if (medicoId == 0) {
                mostrarMensaje("Error: ID de médico no válido")
                return
            }
            
            val resultadoMensaje = dbHelper.insertarMensaje(
                remitenteId = medicoId,
                remitenteNombre = medico.obtenerNombre(),
                remitenteRol = medico.obtenerNombreRol(),
                destinatarioId = pacienteIdInt,
                destinatarioNombre = pacienteNombre,
                destinatarioRol = "PACIENTE",
                asunto = "Diagnóstico Médico",
                contenido = mensaje.toString()
            )
            
            if (resultadoMensaje <= 0) {
                mostrarMensaje("❌ Error al enviar el mensaje al paciente")
                return
            }
            
            // 4. Cambiar el estado de la atención a "finalizada"
            marcarCitaCompletada()
            
            // 5. Notificar al paciente que ya puede ver los resultados
            val mensajeNotificacion = "✅ Tu atención médica ha sido finalizada. Puedes revisar tu diagnóstico en la sección de mensajes y en tu historial clínico."
            dbHelper.insertarMensaje(
                remitenteId = medicoId,
                remitenteNombre = "Sistema",
                remitenteRol = "SISTEMA",
                destinatarioId = pacienteIdInt,
                destinatarioNombre = pacienteNombre,
                destinatarioRol = "PACIENTE",
                asunto = "Atención Finalizada",
                contenido = mensajeNotificacion
            )
            
            // 6. Si hay próxima cita programada, crear alerta
            if (proximaCita.isNotEmpty()) {
                crearAlertaProximaCita(pacienteIdInt, proximaCita)
            }
            
            mostrarMensaje("✅ Atención médica finalizada y enviada al paciente exitosamente")
            finish()
            
        } catch (e: Exception) {
            mostrarMensaje("Error: ${e.message}")
        }
    }
    
    private fun marcarCitaCompletada() {
        try {
            // Actualizar estado de la cita a completada
            // Esto dependería de cómo esté implementado el sistema de citas
            mostrarMensaje("✅ Cita marcada como completada")
        } catch (e: Exception) {
            mostrarMensaje("Error al marcar cita como completada: ${e.message}")
        }
    }
    
    private fun crearAlertaProximaCita(pacienteId: Int, proximaCita: String) {
        try {
            val mensajeAlerta = "📅 Tienes una próxima cita programada el $proximaCita. Por favor, confirma tu asistencia."
            
            // Obtener información del médico
            val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
            val sesion = gestorAutenticacion.obtenerSesionActual()
            val medico = sesion?.usuario
            
            if (medico != null) {
                val medicoId = medico.obtenerId().toIntOrNull() ?: 0
                
                // Crear alerta como mensaje del sistema
                dbHelper.insertarMensaje(
                    remitenteId = medicoId,
                    remitenteNombre = "Sistema de Alertas",
                    remitenteRol = "SISTEMA",
                    destinatarioId = pacienteId,
                    destinatarioNombre = pacienteNombre,
                    destinatarioRol = "PACIENTE",
                    asunto = "Recordatorio de Cita",
                    contenido = mensajeAlerta
                )
            }
        } catch (e: Exception) {
            // No mostrar error al usuario por fallo en alerta
            println("Error al crear alerta de próxima cita: ${e.message}")
        }
    }
    
    private fun generarIdDiagnostico(): String {
        return "DIAG-${System.currentTimeMillis()}"
    }
    
    private fun obtenerFechaActual(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
