package com.example.softmedv5.modelo

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Cita(
    val id: String,
    val pacienteId: String,
    val fechaHora: LocalDateTime,
    val especialidad: String,
    val medio: MedioCita,
    val opcion: String,
    val estado: EstadoCita = EstadoCita.PENDIENTE,
    val fechaCreacion: LocalDateTime = LocalDateTime.now(),
    val fechaModificacion: LocalDateTime = LocalDateTime.now(),
    // Información del paciente para mostrar en gestión administrativa
    val nombrePaciente: String = "",
    val emailPaciente: String = "",
    val telefonoPaciente: String = "",
    val documentoPaciente: String = "",
    val edadPaciente: Int = 0
) {
    
    fun obtenerFechaFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return fechaHora.format(formatter)
    }
    
    fun obtenerHoraFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return fechaHora.format(formatter)
    }
    
    fun obtenerFechaHoraCompleta(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return fechaHora.format(formatter)
    }
    
    fun obtenerFechaCreacionFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return fechaCreacion.format(formatter)
    }
    
    fun puedeCancelar(): Boolean {
        return estado == EstadoCita.CONFIRMADA || estado == EstadoCita.PENDIENTE
    }
    
    fun puedeReprogramar(): Boolean {
        return estado == EstadoCita.CONFIRMADA || estado == EstadoCita.PENDIENTE
    }
    
    fun esCitaFutura(): Boolean {
        return fechaHora.isAfter(LocalDateTime.now())
    }
    
    fun obtenerDescripcionCompleta(): String {
        return """
            📅 Cita #$id
            📋 Especialidad: $especialidad
            🕐 Fecha y Hora: ${obtenerFechaHoraCompleta()}
            💻 Medio: ${medio.descripcion}
            📝 Opción: $opcion
            ✅ Estado: ${estado.descripcion}
        """.trimIndent()
    }
    
    fun obtenerDescripcionConPaciente(): String {
        val infoPaciente = if (nombrePaciente.isNotEmpty()) {
            """
            👤 Paciente: $nombrePaciente
            📧 Email: $emailPaciente
            📞 Teléfono: $telefonoPaciente
            🆔 Documento: $documentoPaciente
            🎂 Edad: $edadPaciente años
            """.trimIndent()
        } else {
            "👤 Paciente: Información no disponible"
        }
        
        val infoCita = """
            📅 Cita #$id
            $infoPaciente
            📋 Especialidad: $especialidad
            🕐 Fecha y Hora: ${obtenerFechaHoraCompleta()}
            💻 Medio: ${medio.descripcion}
            📝 Motivo: $opcion
            ✅ Estado: ${estado.descripcion}
            📅 Creada: ${obtenerFechaCreacionFormateada()}
        """.trimIndent()
        
        return infoCita
    }
}

enum class MedioCita(val descripcion: String) {
    PRESENCIAL("Presencial"),
    VIRTUAL("Virtual")
}

enum class EstadoCita(val descripcion: String) {
    PENDIENTE("Pendiente de Confirmación"),
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada"),
    REPROGRAMADA("Reprogramada"),
    COMPLETADA("Completada")
} 