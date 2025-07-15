package com.example.softmedv5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ComunicacionSeguimientoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonEnviarMensaje: Button
    private lateinit var buttonVerMensajes: Button
    private lateinit var buttonSeguimientoPacientes: Button
    private lateinit var buttonRecordatorios: Button
    private lateinit var buttonNotificaciones: Button
    private lateinit var buttonVolver: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comunicacion_seguimiento)
        
        inicializarVistas()
        configurarEventos()
        mostrarResumen()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonEnviarMensaje = findViewById(R.id.button_enviar_mensaje)
        buttonVerMensajes = findViewById(R.id.button_ver_mensajes)
        buttonSeguimientoPacientes = findViewById(R.id.button_seguimiento_pacientes)
        buttonRecordatorios = findViewById(R.id.button_recordatorios)
        buttonNotificaciones = findViewById(R.id.button_notificaciones)
        buttonVolver = findViewById(R.id.button_volver)
        
        textViewTitulo.text = "💬 Comunicación y Seguimiento"
    }
    
    private fun configurarEventos() {
        buttonEnviarMensaje.setOnClickListener {
            enviarMensaje()
        }
        
        buttonVerMensajes.setOnClickListener {
            verMensajes()
        }
        
        buttonSeguimientoPacientes.setOnClickListener {
            seguimientoPacientes()
        }
        
        buttonRecordatorios.setOnClickListener {
            recordatorios()
        }
        
        buttonNotificaciones.setOnClickListener {
            notificaciones()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun mostrarResumen() {
        val resumen = """
            💬 Resumen de Comunicación y Seguimiento:
            
            📧 Mensajes enviados hoy: 8
            📨 Mensajes recibidos: 12
            👥 Pacientes en seguimiento: 25
            ⏰ Recordatorios programados: 15
            🔔 Notificaciones pendientes: 5
            
            💡 Funcionalidades disponibles:
            • Enviar mensajes a pacientes
            • Ver mensajes recibidos
            • Seguimiento de pacientes
            • Programar recordatorios
            • Gestionar notificaciones
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun enviarMensaje() {
        val mensaje = """
            📧 Enviar Mensaje:
            
            Destinatarios disponibles:
            
            👤 Pacientes:
            • María González (PAC-001)
            • Carlos Rodríguez (PAC-002)
            • Ana Martínez (PAC-003)
            
            👨‍⚕️ Personal Médico:
            • Dr. García (Cardiología)
            • Dra. López (Dermatología)
            • Dr. Martínez (Endocrinología)
            
            📋 Personal Administrativo:
            • Sra. Pérez (Recepción)
            • Sr. González (Facturación)
            
            Tipos de mensaje:
            
            📝 Mensaje General:
            • Información general
            • Recordatorios
            • Confirmaciones
            
            🏥 Mensaje Médico:
            • Resultados de exámenes
            • Cambios en tratamiento
            • Citas reprogramadas
            
            ⚠️ Mensaje Urgente:
            • Alertas importantes
            • Cambios críticos
            • Emergencias
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📧 Enviar Mensaje")
            .setMessage(mensaje)
            .setPositiveButton("Enviar") { _, _ ->
                mostrarMensaje("📧 Abriendo formulario de mensaje...")
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun verMensajes() {
        val mensaje = """
            📨 Mensajes Recibidos:
            
            📧 De María González (PAC-001):
            • Asunto: Consulta sobre medicación
            • Fecha: 15/12/2024 10:30
            • Estado: ✅ Leído
            • "Doctor, tengo dudas sobre la dosis de metformina"
            
            📧 De Dr. García (Cardiología):
            • Asunto: Referencia de paciente
            • Fecha: 15/12/2024 09:15
            • Estado: ⏳ Pendiente
            • "Necesito referir paciente para evaluación cardiológica"
            
            📧 De Sra. Pérez (Recepción):
            • Asunto: Cita cancelada
            • Fecha: 14/12/2024 16:45
            • Estado: ✅ Leído
            • "Paciente Carlos Rodríguez canceló cita de mañana"
            
            📧 De Ana Martínez (PAC-003):
            • Asunto: Resultados de exámenes
            • Fecha: 14/12/2024 14:20
            • Estado: ⏳ Pendiente
            • "¿Puede revisar mis resultados de laboratorio?"
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📨 Mensajes Recibidos")
            .setMessage(mensaje)
            .setPositiveButton("Responder") { _, _ ->
                mostrarMensaje("📧 Abriendo formulario de respuesta...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun seguimientoPacientes() {
        val mensaje = """
            👥 Seguimiento de Pacientes:
            
            📊 Pacientes Activos:
            
            🟢 María González (PAC-001):
            • Última consulta: 15/12/2024
            • Próxima cita: 22/12/2024
            • Estado: En tratamiento
            • Seguimiento: Semanal
            
            🟡 Carlos Rodríguez (PAC-002):
            • Última consulta: 10/12/2024
            • Próxima cita: 20/12/2024
            • Estado: Pendiente de resultados
            • Seguimiento: Quincenal
            
            🔴 Ana Martínez (PAC-003):
            • Última consulta: 05/12/2024
            • Próxima cita: 18/12/2024
            • Estado: Requiere atención urgente
            • Seguimiento: Diario
            
            📈 Estadísticas de Seguimiento:
            • Pacientes con seguimiento activo: 25
            • Consultas de seguimiento esta semana: 8
            • Pacientes que requieren atención: 3
            • Seguimientos completados: 15
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("👥 Seguimiento de Pacientes")
            .setMessage(mensaje)
            .setPositiveButton("Ver Detalles") { _, _ ->
                mostrarMensaje("👥 Abriendo detalles de seguimiento...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun recordatorios() {
        val mensaje = """
            ⏰ Recordatorios Programados:
            
            📅 Para Hoy (15/12/2024):
            
            🕐 09:00 - Llamar a María González:
            • Recordar toma de medicación
            • Confirmar cita de la semana próxima
            • Estado: ⏳ Pendiente
            
            🕐 11:00 - Revisar resultados de Carlos:
            • Resultados de laboratorio pendientes
            • Evaluar necesidad de ajuste de tratamiento
            • Estado: ⏳ Pendiente
            
            🕐 14:00 - Seguimiento de Ana Martínez:
            • Verificar evolución de síntomas
            • Programar cita de control
            • Estado: ⏳ Pendiente
            
            📅 Para Mañana (16/12/2024):
            
            🕐 10:00 - Cita con Juan Pérez
            🕐 15:00 - Revisar historial de Laura Sánchez
            
            💡 Funcionalidades:
            • Crear nuevo recordatorio
            • Editar recordatorios existentes
            • Marcar como completado
            • Programar recordatorios recurrentes
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("⏰ Recordatorios")
            .setMessage(mensaje)
            .setPositiveButton("Crear Recordatorio") { _, _ ->
                mostrarMensaje("⏰ Abriendo formulario de recordatorio...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun notificaciones() {
        val mensaje = """
            🔔 Notificaciones del Sistema:
            
            ⚠️ Alertas Importantes:
            
            🚨 Resultado Anormal - Carlos Rodríguez:
            • Glucosa elevada en último examen
            • Requiere atención inmediata
            • Fecha: 15/12/2024 08:30
            
            🚨 Cita Cancelada - Ana Martínez:
            • Paciente canceló cita programada
            • Necesita reprogramación urgente
            • Fecha: 15/12/2024 09:15
            
            📋 Información General:
            
            ✅ Nuevo Paciente Registrado:
            • Laura Sánchez (PAC-004)
            • Asignado a su consultorio
            • Fecha: 15/12/2024 10:00
            
            ✅ Resultados Disponibles:
            • Hemograma de María González
            • Radiografía de Juan Pérez
            • Fecha: 15/12/2024 11:30
            
            📊 Estadísticas:
            • Notificaciones no leídas: 5
            • Alertas críticas: 2
            • Información general: 3
            
            💡 Acciones:
            • Marcar como leída
            • Responder a alertas
            • Configurar notificaciones
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🔔 Notificaciones")
            .setMessage(mensaje)
            .setPositiveButton("Responder") { _, _ ->
                mostrarMensaje("🔔 Abriendo panel de notificaciones...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
} 