package com.example.softmedv5.util

import com.example.softmedv5.modelo.Usuario
import java.time.LocalDateTime

/**
 * Sistema de notificaciones para el personal administrativo
 * Gestiona las notificaciones de nuevos usuarios registrados
 */
class GestorNotificaciones {
    
    companion object {
        private var instancia: GestorNotificaciones? = null
        
        fun obtenerInstancia(): GestorNotificaciones {
            if (instancia == null) {
                instancia = GestorNotificaciones()
            }
            return instancia!!
        }
    }
    
    // Lista de notificaciones pendientes para el personal administrativo
    private val notificacionesPendientes = mutableListOf<NotificacionUsuario>()
    
    /**
     * Notifica al personal administrativo sobre un nuevo usuario registrado
     * @param usuario Usuario recién registrado
     * @param fechaRegistro Fecha y hora del registro
     */
    fun notificarNuevoUsuario(usuario: Usuario, fechaRegistro: LocalDateTime = LocalDateTime.now()) {
        val notificacion = NotificacionUsuario(
            id = generarIdNotificacion(),
            tipo = TipoNotificacion.NUEVO_USUARIO,
            usuario = usuario,
            fechaCreacion = fechaRegistro,
            estado = EstadoNotificacion.PENDIENTE,
            mensaje = generarMensajeNotificacion(usuario),
            requiereAccion = true
        )
        
        notificacionesPendientes.add(notificacion)
        println("🔔 Nueva notificación: ${notificacion.mensaje}")
    }
    
    /**
     * Obtiene todas las notificaciones pendientes
     * @return Lista de notificaciones pendientes
     */
    fun obtenerNotificacionesPendientes(): List<NotificacionUsuario> {
        return notificacionesPendientes.filter { it.estado == EstadoNotificacion.PENDIENTE }
    }
    
    /**
     * Obtiene todas las notificaciones (pendientes y procesadas)
     * @return Lista completa de notificaciones
     */
    fun obtenerTodasLasNotificaciones(): List<NotificacionUsuario> {
        return notificacionesPendientes.toList()
    }
    
    /**
     * Marca una notificación como procesada
     * @param notificacionId ID de la notificación
     */
    fun marcarComoProcesada(notificacionId: String) {
        val notificacion = notificacionesPendientes.find { it.id == notificacionId }
        notificacion?.let {
            it.estado = EstadoNotificacion.PROCESADA
            it.fechaProcesamiento = LocalDateTime.now()
        }
    }
    
    /**
     * Obtiene el número de notificaciones pendientes
     * @return Número de notificaciones pendientes
     */
    fun obtenerCantidadNotificacionesPendientes(): Int {
        return notificacionesPendientes.count { it.estado == EstadoNotificacion.PENDIENTE }
    }
    
    /**
     * Genera un mensaje de notificación para un nuevo usuario
     * @param usuario Usuario recién registrado
     * @return Mensaje formateado
     */
    private fun generarMensajeNotificacion(usuario: Usuario): String {
        val emoji = when (usuario.obtenerNombreRol()) {
            "Paciente" -> "👤"
            "Médico" -> "👨‍⚕️"
            "Personal Administrativo" -> "👩‍💼"
            else -> "👤"
        }
        
        return """
            $emoji Nuevo usuario registrado
            
            📋 Información del usuario:
            👤 Nombre: ${usuario.obtenerNombre()}
            📧 Email: ${usuario.obtenerEmail()}
            🏷️ Rol: ${usuario.obtenerNombreRol()}
            🆔 ID: ${usuario.obtenerId()}
            
            ⚠️ Requiere gestión administrativa:
            • Verificar información personal
            • Completar datos faltantes
            • Validar documentos
            • Asignar permisos específicos
        """.trimIndent()
    }
    
    /**
     * Genera un ID único para la notificación
     * @return ID único
     */
    private fun generarIdNotificacion(): String {
        return "NOTIF-${System.currentTimeMillis()}"
    }
    
    /**
     * Limpia las notificaciones procesadas antiguas
     * @param diasAntiguedad Número de días para considerar una notificación como antigua
     */
    fun limpiarNotificacionesAntiguas(diasAntiguedad: Int = 30) {
        val fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad.toLong())
        notificacionesPendientes.removeAll { 
            it.estado == EstadoNotificacion.PROCESADA && 
            it.fechaProcesamiento != null && 
            it.fechaProcesamiento!!.isBefore(fechaLimite)
        }
    }
}

/**
 * Clase que representa una notificación de usuario
 */
data class NotificacionUsuario(
    val id: String,
    val tipo: TipoNotificacion,
    val usuario: Usuario,
    val fechaCreacion: LocalDateTime,
    var estado: EstadoNotificacion,
    val mensaje: String,
    val requiereAccion: Boolean,
    var fechaProcesamiento: LocalDateTime? = null
)

/**
 * Tipos de notificación
 */
enum class TipoNotificacion {
    NUEVO_USUARIO,
    ACTUALIZACION_DATOS,
    SOLICITUD_CITA,
    DOCUMENTO_PENDIENTE
}

/**
 * Estados de notificación
 */
enum class EstadoNotificacion {
    PENDIENTE,
    PROCESADA,
    CANCELADA
} 