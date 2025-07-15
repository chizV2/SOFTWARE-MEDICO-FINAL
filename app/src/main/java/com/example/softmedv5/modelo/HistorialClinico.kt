package com.example.softmedv5.modelo

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Clase que representa el historial clínico completo de un paciente
 * Contiene información médica que el paciente puede consultar pero no modificar
 */
data class HistorialClinico(
    val pacienteId: String,
    val diagnosticosAnteriores: List<Diagnostico>,
    val consultasPasadas: List<ConsultaPasada>,
    val enfermedadesCronicas: List<EnfermedadCronica>,
    val alergiasRegistradas: List<Alergia>,
    val cirugiasProcedimientos: List<CirugiaProcedimiento>,
    val fechaCreacion: LocalDateTime = LocalDateTime.now(),
    val fechaUltimaActualizacion: LocalDateTime = LocalDateTime.now()
) {
    
    /**
     * Crea un historial clínico vacío para un paciente nuevo
     * @param pacienteId ID del paciente
     * @return HistorialClinico vacío
     */
    companion object {
        fun crearHistorialVacio(pacienteId: String): HistorialClinico {
            return HistorialClinico(
                pacienteId = pacienteId,
                diagnosticosAnteriores = emptyList(),
                consultasPasadas = emptyList(),
                enfermedadesCronicas = emptyList(),
                alergiasRegistradas = emptyList(),
                cirugiasProcedimientos = emptyList()
            )
        }
    }
    
    /**
     * Verifica si el historial está vacío (sin registros médicos)
     * @return true si está vacío, false en caso contrario
     */
    fun estaVacio(): Boolean {
        return diagnosticosAnteriores.isEmpty() &&
               consultasPasadas.isEmpty() &&
               enfermedadesCronicas.isEmpty() &&
               alergiasRegistradas.isEmpty() &&
               cirugiasProcedimientos.isEmpty()
    }
    
    /**
     * Obtiene un resumen del historial clínico
     */
    fun obtenerResumen(): String {
        return """
            📋 Resumen del Historial Clínico
            
            🔍 Diagnósticos: ${diagnosticosAnteriores.size} registros
            🏥 Consultas: ${consultasPasadas.size} consultas
            ⚠️ Enfermedades crónicas: ${enfermedadesCronicas.size} condiciones
            🚨 Alergias: ${alergiasRegistradas.size} alergias
            🔪 Cirugías/Procedimientos: ${cirugiasProcedimientos.size} intervenciones
            
            📅 Última actualización: ${obtenerFechaUltimaActualizacionFormateada()}
        """.trimIndent()
    }
    
    /**
     * Obtiene información de alertas médicas importantes
     */
    fun obtenerAlertasMedicas(): List<String> {
        val alertas = mutableListOf<String>()
        
        // Alertas por alergias activas
        val alergiasActivas = alergiasRegistradas.filter { it.activa }
        if (alergiasActivas.isNotEmpty()) {
            alertas.add("🚨 Alergias activas: ${alergiasActivas.joinToString(", ") { it.nombre }}")
        }
        
        // Alertas por enfermedades crónicas
        val enfermedadesActivas = enfermedadesCronicas.filter { it.activa }
        if (enfermedadesActivas.isNotEmpty()) {
            alertas.add("⚠️ Enfermedades crónicas: ${enfermedadesActivas.joinToString(", ") { it.nombre }}")
        }
        
        // Alertas por cirugías recientes
        val cirugiasRecientes = cirugiasProcedimientos.filter { 
            it.fecha.isAfter(LocalDateTime.now().minusMonths(6)) 
        }
        if (cirugiasRecientes.isNotEmpty()) {
            alertas.add("🔪 Cirugías recientes (últimos 6 meses): ${cirugiasRecientes.size} intervenciones")
        }
        
        return alertas
    }
    
    private fun obtenerFechaUltimaActualizacionFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return fechaUltimaActualizacion.format(formatter)
    }
}

/**
 * Clase que representa un diagnóstico médico
 */
data class Diagnostico(
    val id: String,
    val fecha: LocalDateTime,
    val especialidad: String,
    val medico: String,
    val diagnostico: String,
    val codigoCIE10: String?,
    val observaciones: String?,
    val tratamiento: String?,
    val estado: EstadoDiagnostico = EstadoDiagnostico.ACTIVO
) {
    fun obtenerFechaFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return fecha.format(formatter)
    }
    
    fun obtenerDescripcionCompleta(): String {
        return """
            📋 Diagnóstico #$id
            📅 Fecha: ${obtenerFechaFormateada()}
            👨‍⚕️ Especialidad: $especialidad
            👨‍⚕️ Médico: $medico
            🔍 Diagnóstico: $diagnostico
            ${codigoCIE10?.let { "🏷️ CIE-10: $it" } ?: ""}
            ${observaciones?.let { "📝 Observaciones: $it" } ?: ""}
            ${tratamiento?.let { "💊 Tratamiento: $it" } ?: ""}
            📊 Estado: ${estado.descripcion}
        """.trimIndent()
    }
}

/**
 * Clase que representa una consulta médica pasada
 */
data class ConsultaPasada(
    val id: String,
    val fecha: LocalDateTime,
    val especialidad: String,
    val medico: String,
    val motivo: String,
    val sintomas: String?,
    val exploracion: String?,
    val conclusiones: String?,
    val recomendaciones: String?,
    val proximaCita: LocalDateTime?
) {
    fun obtenerFechaFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return fecha.format(formatter)
    }
    
    fun obtenerDescripcionCompleta(): String {
        return """
            🏥 Consulta #$id
            📅 Fecha: ${obtenerFechaFormateada()}
            👨‍⚕️ Especialidad: $especialidad
            👨‍⚕️ Médico: $medico
            📝 Motivo: $motivo
            ${sintomas?.let { "🤒 Síntomas: $it" } ?: ""}
            ${exploracion?.let { "🔍 Exploración: $it" } ?: ""}
            ${conclusiones?.let { "📋 Conclusiones: $it" } ?: ""}
            ${recomendaciones?.let { "💡 Recomendaciones: $it" } ?: ""}
            ${proximaCita?.let { "📅 Próxima cita: ${it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}" } ?: ""}
        """.trimIndent()
    }
}

/**
 * Clase que representa una enfermedad crónica
 */
data class EnfermedadCronica(
    val id: String,
    val nombre: String,
    val fechaDiagnostico: LocalDateTime,
    val medico: String,
    val descripcion: String,
    val sintomas: String?,
    val tratamiento: String?,
    val medicamentos: List<String>,
    val activa: Boolean = true,
    val observaciones: String?
) {
    fun obtenerFechaDiagnosticoFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return fechaDiagnostico.format(formatter)
    }
    
    fun obtenerDescripcionCompleta(): String {
        return """
            ⚠️ Enfermedad Crónica: $nombre
            📅 Diagnóstico: ${obtenerFechaDiagnosticoFormateada()}
            👨‍⚕️ Médico: $medico
            📝 Descripción: $descripcion
            ${sintomas?.let { "🤒 Síntomas: $it" } ?: ""}
            ${tratamiento?.let { "💊 Tratamiento: $it" } ?: ""}
            💊 Medicamentos: ${medicamentos.joinToString(", ")}
            📊 Estado: ${if (activa) "Activa" else "Inactiva"}
            ${observaciones?.let { "📝 Observaciones: $it" } ?: ""}
        """.trimIndent()
    }
}

/**
 * Clase que representa una alergia
 */
data class Alergia(
    val id: String,
    val nombre: String,
    val tipo: TipoAlergia,
    val fechaRegistro: LocalDateTime,
    val medico: String,
    val descripcion: String,
    val sintomas: String?,
    val severidad: SeveridadAlergia,
    val activa: Boolean = true,
    val observaciones: String?
) {
    fun obtenerFechaRegistroFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return fechaRegistro.format(formatter)
    }
    
    fun obtenerDescripcionCompleta(): String {
        return """
            🚨 Alergia: $nombre
            🏷️ Tipo: ${tipo.descripcion}
            📅 Registro: ${obtenerFechaRegistroFormateada()}
            👨‍⚕️ Médico: $medico
            📝 Descripción: $descripcion
            ${sintomas?.let { "🤒 Síntomas: $it" } ?: ""}
            ⚠️ Severidad: ${severidad.descripcion}
            📊 Estado: ${if (activa) "Activa" else "Inactiva"}
            ${observaciones?.let { "📝 Observaciones: $it" } ?: ""}
        """.trimIndent()
    }
}

/**
 * Clase que representa una cirugía o procedimiento
 */
data class CirugiaProcedimiento(
    val id: String,
    val nombre: String,
    val tipo: TipoCirugia,
    val fecha: LocalDateTime,
    val medico: String,
    val hospital: String,
    val descripcion: String,
    val motivo: String,
    val resultado: String?,
    val complicaciones: String?,
    val observaciones: String?
) {
    fun obtenerFechaFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return fecha.format(formatter)
    }
    
    fun obtenerDescripcionCompleta(): String {
        return """
            🔪 ${tipo.descripcion}: $nombre
            📅 Fecha: ${obtenerFechaFormateada()}
            👨‍⚕️ Médico: $medico
            🏥 Hospital: $hospital
            📝 Descripción: $descripcion
            🎯 Motivo: $motivo
            ${resultado?.let { "✅ Resultado: $it" } ?: ""}
            ${complicaciones?.let { "⚠️ Complicaciones: $it" } ?: ""}
            ${observaciones?.let { "📝 Observaciones: $it" } ?: ""}
        """.trimIndent()
    }
}

/**
 * Enumeración para el estado del diagnóstico
 */
enum class EstadoDiagnostico(val descripcion: String) {
    ACTIVO("Activo"),
    RESUELTO("Resuelto"),
    CRONICO("Crónico"),
    EN_SEGUIMIENTO("En seguimiento")
}

/**
 * Enumeración para el tipo de alergia
 */
enum class TipoAlergia(val descripcion: String) {
    MEDICAMENTO("Medicamento"),
    ALIMENTO("Alimento"),
    AMBIENTAL("Ambiental"),
    INSECTO("Insecto"),
    OTRO("Otro")
}

/**
 * Enumeración para la severidad de la alergia
 */
enum class SeveridadAlergia(val descripcion: String) {
    LEVE("Leve"),
    MODERADA("Moderada"),
    SEVERA("Severa"),
    CRITICA("Crítica")
}

/**
 * Enumeración para el tipo de cirugía
 */
enum class TipoCirugia(val descripcion: String) {
    CIRUGIA_MAYOR("Cirugía Mayor"),
    CIRUGIA_MENOR("Cirugía Menor"),
    PROCEDIMIENTO_DIAGNOSTICO("Procedimiento Diagnóstico"),
    PROCEDIMIENTO_TERAPEUTICO("Procedimiento Terapéutico"),
    INTERVENCION_URGENTE("Intervención Urgente")
} 