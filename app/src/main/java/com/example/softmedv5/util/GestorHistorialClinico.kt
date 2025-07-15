package com.example.softmedv5.util

import com.example.softmedv5.modelo.*
import java.time.LocalDateTime

/**
 * Gestor para manejar el historial clínico de los pacientes
 * Proporciona acceso de solo lectura a la información médica
 */
class GestorHistorialClinico private constructor() {
    
    companion object {
        @Volatile
        private var INSTANCIA: GestorHistorialClinico? = null
        
        fun obtenerInstancia(): GestorHistorialClinico {
            return INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: GestorHistorialClinico().also { INSTANCIA = it }
            }
        }
    }
    
    // Base de datos simulada de historiales clínicos
    private val historialesClinicos = mutableMapOf<String, HistorialClinico>()
    
    init {
        // Crear datos de demostración
        crearDatosDemostracion()
    }
    
    /**
     * Obtiene el historial clínico completo de un paciente
     */
    fun obtenerHistorialClinico(pacienteId: String): HistorialClinico? {
        return historialesClinicos[pacienteId]
    }
    
    /**
     * Obtiene solo los diagnósticos anteriores de un paciente
     */
    fun obtenerDiagnosticosAnteriores(pacienteId: String): List<Diagnostico> {
        return historialesClinicos[pacienteId]?.diagnosticosAnteriores ?: emptyList()
    }
    
    /**
     * Obtiene solo las consultas pasadas de un paciente
     */
    fun obtenerConsultasPasadas(pacienteId: String): List<ConsultaPasada> {
        return historialesClinicos[pacienteId]?.consultasPasadas ?: emptyList()
    }
    
    /**
     * Obtiene solo las enfermedades crónicas de un paciente
     */
    fun obtenerEnfermedadesCronicas(pacienteId: String): List<EnfermedadCronica> {
        return historialesClinicos[pacienteId]?.enfermedadesCronicas ?: emptyList()
    }
    
    /**
     * Obtiene solo las alergias registradas de un paciente
     */
    fun obtenerAlergiasRegistradas(pacienteId: String): List<Alergia> {
        return historialesClinicos[pacienteId]?.alergiasRegistradas ?: emptyList()
    }
    
    /**
     * Obtiene solo las cirugías y procedimientos de un paciente
     */
    fun obtenerCirugiasProcedimientos(pacienteId: String): List<CirugiaProcedimiento> {
        return historialesClinicos[pacienteId]?.cirugiasProcedimientos ?: emptyList()
    }
    
    /**
     * Obtiene las alertas médicas importantes de un paciente
     */
    fun obtenerAlertasMedicas(pacienteId: String): List<String> {
        return historialesClinicos[pacienteId]?.obtenerAlertasMedicas() ?: emptyList()
    }
    
    /**
     * Verifica si un paciente tiene historial clínico
     */
    fun tieneHistorialClinico(pacienteId: String): Boolean {
        return historialesClinicos.containsKey(pacienteId)
    }
    
    /**
     * Crea un historial clínico vacío para un paciente nuevo
     * @param pacienteId ID del paciente
     * @return true si se creó exitosamente, false si ya existe
     */
    fun crearHistorialVacio(pacienteId: String): Boolean {
        return if (!tieneHistorialClinico(pacienteId)) {
            val historialVacio = HistorialClinico.crearHistorialVacio(pacienteId)
            historialesClinicos[pacienteId] = historialVacio
            true
        } else {
            false
        }
    }
    
    /**
     * Actualiza el historial clínico después de una atención médica
     * @param pacienteId ID del paciente
     * @param nuevoDiagnostico Nuevo diagnóstico a agregar
     * @param nuevaConsulta Nueva consulta a agregar
     * @return true si se actualizó exitosamente
     */
    fun actualizarHistorialDespuesAtencion(
        pacienteId: String,
        nuevoDiagnostico: Diagnostico? = null,
        nuevaConsulta: ConsultaPasada? = null
    ): Boolean {
        val historial = historialesClinicos[pacienteId]
        if (historial == null) {
            // Si no existe historial, crear uno vacío
            crearHistorialVacio(pacienteId)
        }
        
        val historialActual = historialesClinicos[pacienteId] ?: return false
        
        // Crear nuevo historial con los datos actualizados
        val diagnosticosActualizados = if (nuevoDiagnostico != null) {
            historialActual.diagnosticosAnteriores + nuevoDiagnostico
        } else {
            historialActual.diagnosticosAnteriores
        }
        
        val consultasActualizadas = if (nuevaConsulta != null) {
            historialActual.consultasPasadas + nuevaConsulta
        } else {
            historialActual.consultasPasadas
        }
        
        val historialActualizado = historialActual.copy(
            diagnosticosAnteriores = diagnosticosActualizados,
            consultasPasadas = consultasActualizadas,
            fechaUltimaActualizacion = LocalDateTime.now()
        )
        
        historialesClinicos[pacienteId] = historialActualizado
        return true
    }
    
    /**
     * Agrega una nueva enfermedad crónica al historial
     * @param pacienteId ID del paciente
     * @param nuevaEnfermedad Nueva enfermedad crónica
     * @return true si se agregó exitosamente
     */
    fun agregarEnfermedadCronica(pacienteId: String, nuevaEnfermedad: EnfermedadCronica): Boolean {
        val historial = historialesClinicos[pacienteId] ?: return false
        
        val enfermedadesActualizadas = historial.enfermedadesCronicas + nuevaEnfermedad
        val historialActualizado = historial.copy(
            enfermedadesCronicas = enfermedadesActualizadas,
            fechaUltimaActualizacion = LocalDateTime.now()
        )
        
        historialesClinicos[pacienteId] = historialActualizado
        return true
    }
    
    /**
     * Agrega una nueva alergia al historial
     * @param pacienteId ID del paciente
     * @param nuevaAlergia Nueva alergia
     * @return true si se agregó exitosamente
     */
    fun agregarAlergia(pacienteId: String, nuevaAlergia: Alergia): Boolean {
        val historial = historialesClinicos[pacienteId] ?: return false
        
        val alergiasActualizadas = historial.alergiasRegistradas + nuevaAlergia
        val historialActualizado = historial.copy(
            alergiasRegistradas = alergiasActualizadas,
            fechaUltimaActualizacion = LocalDateTime.now()
        )
        
        historialesClinicos[pacienteId] = historialActualizado
        return true
    }
    
    /**
     * Agrega una nueva cirugía o procedimiento al historial
     * @param pacienteId ID del paciente
     * @param nuevaCirugia Nueva cirugía o procedimiento
     * @return true si se agregó exitosamente
     */
    fun agregarCirugiaProcedimiento(pacienteId: String, nuevaCirugia: CirugiaProcedimiento): Boolean {
        val historial = historialesClinicos[pacienteId] ?: return false
        
        val cirugiasActualizadas = historial.cirugiasProcedimientos + nuevaCirugia
        val historialActualizado = historial.copy(
            cirugiasProcedimientos = cirugiasActualizadas,
            fechaUltimaActualizacion = LocalDateTime.now()
        )
        
        historialesClinicos[pacienteId] = historialActualizado
        return true
    }
    
    /**
     * Obtiene un resumen del historial clínico
     */
    fun obtenerResumenHistorial(pacienteId: String): String {
        val historial = historialesClinicos[pacienteId]
        return if (historial != null) {
            if (historial.estaVacio()) {
                """
                📋 Historial Clínico Vacío
                
                Este es un paciente nuevo sin registros médicos previos.
                El historial se actualizará automáticamente después de cada atención médica.
                
                📅 Fecha de creación: ${historial.fechaCreacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
                """.trimIndent()
            } else {
                historial.obtenerResumen()
            }
        } else {
            "📋 No se encontró historial clínico para este paciente"
        }
    }
    
    /**
     * Crea datos de demostración para mostrar la funcionalidad
     */
    private fun crearDatosDemostracion() {
        // Crear historiales VACÍOS para pacientes nuevos
        // Los historiales se llenarán solo cuando el médico realice atención médica
        
        // Historial vacío para PACIENTE-1
        val historialPaciente1 = HistorialClinico.crearHistorialVacio("PACIENTE-1")
        
        // Historial vacío para PACIENTE-2  
        val historialPaciente2 = HistorialClinico.crearHistorialVacio("PACIENTE-2")
        
        // Historial vacío para PACIENTE-3
        val historialPaciente3 = HistorialClinico.crearHistorialVacio("PACIENTE-3")
        
        // Historial vacío para PACIENTE-4
        val historialPaciente4 = HistorialClinico.crearHistorialVacio("PACIENTE-4")
        
        // Agregar los historiales vacíos a la base de datos simulada
        historialesClinicos["PACIENTE-1"] = historialPaciente1
        historialesClinicos["PACIENTE-2"] = historialPaciente2
        historialesClinicos["PACIENTE-3"] = historialPaciente3
        historialesClinicos["PACIENTE-4"] = historialPaciente4
        
        // NOTA: Los historiales se llenarán automáticamente cuando el médico
        // realice atención médica y registre diagnósticos/consultas
    }
} 