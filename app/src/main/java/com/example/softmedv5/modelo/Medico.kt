package com.example.softmedv5.modelo

/**
 * Clase que representa un Médico del sistema médico
 * Tiene acceso a funcionalidades relacionadas con la atención médica
 */
class Medico : Usuario() {
    
    private var especialidad: String = ""
    private var numeroLicencia: String = ""
    
    // Datos adicionales específicos del médico
    private var telefono: String = ""
    private var direccion: String = ""
    private var fechaNacimiento: String = ""
    private var documentoIdentidad: String = ""
    
    init {
        permisos = listOf(
            "GESTIONAR_PACIENTES",
            "GESTIONAR_CITAS",
            "CREAR_HISTORIALES",
            "PRESCRIBIR_MEDICAMENTOS",
            "VER_HISTORIALES",
            "GENERAR_DIAGNOSTICOS",
            "ACCESO_MEDICO"
        )
    }
    
    override fun obtenerNombreRol(): String {
        return "Médico"
    }
    
    override fun obtenerPermisos(): List<String> {
        return permisos
    }
    
    /**
     * Establece la información médica específica
     * @param especialidad Especialidad del médico
     * @param numeroLicencia Número de licencia médica
     */
    fun establecerInformacionMedica(especialidad: String, numeroLicencia: String) {
        this.especialidad = especialidad
        this.numeroLicencia = numeroLicencia
    }
    
    /**
     * Obtiene la especialidad del médico
     * @return String con la especialidad
     */
    fun obtenerEspecialidad(): String = especialidad
    
    /**
     * Obtiene el número de licencia
     * @return String con el número de licencia
     */
    fun obtenerNumeroLicencia(): String = numeroLicencia
    
    /**
     * Establece los datos personales del médico
     */
    fun establecerDatosPersonales(
        telefono: String,
        direccion: String,
        fechaNacimiento: String,
        documentoIdentidad: String
    ) {
        this.telefono = telefono
        this.direccion = direccion
        this.fechaNacimiento = fechaNacimiento
        this.documentoIdentidad = documentoIdentidad
    }
    
    /**
     * Obtiene el teléfono del médico
     */
    fun obtenerTelefono(): String = telefono
    
    /**
     * Obtiene la dirección del médico
     */
    fun obtenerDireccion(): String = direccion
    
    /**
     * Obtiene la fecha de nacimiento del médico
     */
    fun obtenerFechaNacimiento(): String = fechaNacimiento
    
    /**
     * Obtiene el documento de identidad del médico
     */
    fun obtenerDocumentoIdentidad(): String = documentoIdentidad
    
    /**
     * Método para crear un historial médico
     * @param datosPaciente Datos del paciente
     * @param diagnostico Diagnóstico realizado
     * @return String con el historial creado
     */
    fun crearHistorialMedico(datosPaciente: Map<String, String>, diagnostico: String): String {
        return "Historial médico creado para ${datosPaciente["nombre"]} - Diagnóstico: $diagnostico"
    }
    
    /**
     * Método para prescribir medicamentos
     * @param medicamento Nombre del medicamento
     * @param dosis Dosis prescrita
     * @param duracion Duración del tratamiento
     * @return String con la prescripción
     */
    fun prescribirMedicamento(medicamento: String, dosis: String, duracion: String): String {
        return "Prescripción: $medicamento - Dosis: $dosis - Duración: $duracion"
    }
} 