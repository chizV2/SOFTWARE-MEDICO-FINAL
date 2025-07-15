package com.example.softmedv5.modelo

/**
 * Clase de datos simple para representar un diagnóstico médico
 * que se puede almacenar en la base de datos
 */
data class DiagnosticoMedico(
    val id: String,
    val pacienteId: String,
    val medicoId: String,
    val fecha: String,
    val sintomas: String,
    val examenFisico: String,
    val diagnostico: String,
    val diagnosticosSecundarios: String,
    val tratamiento: String,
    val medicamentos: String,
    val recomendaciones: String,
    val proximaCita: String,
    val estado: String
) {
    /**
     * Obtiene una descripción formateada del diagnóstico
     */
    fun obtenerDescripcionCompleta(): String {
        return """
            📋 Diagnóstico Médico #$id
            📅 Fecha: $fecha
            👤 Paciente ID: $pacienteId
            👨‍⚕️ Médico ID: $medicoId
            
            🔍 Síntomas: $sintomas
            🏥 Examen Físico: $examenFisico
            📋 Diagnóstico Principal: $diagnostico
            📋 Diagnósticos Secundarios: $diagnosticosSecundarios
            💊 Tratamiento: $tratamiento
            💊 Medicamentos: $medicamentos
            📝 Recomendaciones: $recomendaciones
            📅 Próxima Cita: $proximaCita
            📊 Estado: $estado
        """.trimIndent()
    }
    
    /**
     * Obtiene un resumen corto del diagnóstico
     */
    fun obtenerResumen(): String {
        return "📋 $diagnostico - $fecha"
    }
} 