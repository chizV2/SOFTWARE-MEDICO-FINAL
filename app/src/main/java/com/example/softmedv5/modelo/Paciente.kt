package com.example.softmedv5.modelo

/**
 * Clase que representa un Paciente del sistema médico
 * Tiene acceso limitado a funcionalidades básicas relacionadas con su atención médica
 */
class Paciente : Usuario() {
    
    // Datos adicionales específicos del paciente
    private var telefono: String = ""
    private var direccion: String = ""
    private var seguro: String = ""
    private var numeroSeguro: String = ""
    private var fechaNacimiento: String = ""
    private var documentoIdentidad: String = ""
    private var fechaRegistro: String = ""
    private var estado: String = "ACTIVO"
    
    init {
        permisos = listOf(
            "VER_PERFIL",
            "SOLICITAR_CITAS",
            "VER_HISTORIAL_PERSONAL",
            "ACCESO_BASICO",
            "GESTIONAR_INFORMACION_CONTACTO",
            "CONSULTAR_DISPONIBILIDAD"
        )
    }
    
    override fun obtenerNombreRol(): String {
        return "Paciente"
    }
    
    override fun obtenerPermisos(): List<String> {
        return permisos
    }
    
    /**
     * Verifica si el paciente tiene todos sus datos personales completos
     * @return true si tiene todos los datos, false en caso contrario
     */
    fun tieneDatosPersonalesCompletos(): Boolean {
        return nombre.isNotBlank() && 
               email.isNotBlank() && 
               telefono.isNotBlank() && 
               direccion.isNotBlank() && 
               seguro.isNotBlank() && 
               numeroSeguro.isNotBlank() && 
               fechaNacimiento.isNotBlank() && 
               documentoIdentidad.isNotBlank()
    }
    
    /**
     * Obtiene la lista de campos faltantes en los datos personales
     * @return Lista de nombres de campos que faltan
     */
    fun obtenerCamposFaltantes(): List<String> {
        val camposFaltantes = mutableListOf<String>()
        
        if (nombre.isBlank()) camposFaltantes.add("Nombre completo")
        if (email.isBlank()) camposFaltantes.add("Correo electrónico")
        if (telefono.isBlank()) camposFaltantes.add("Teléfono")
        if (direccion.isBlank()) camposFaltantes.add("Dirección")
        if (seguro.isBlank()) camposFaltantes.add("Seguro médico")
        if (numeroSeguro.isBlank()) camposFaltantes.add("Número de seguro")
        if (fechaNacimiento.isBlank()) camposFaltantes.add("Fecha de nacimiento")
        if (documentoIdentidad.isBlank()) camposFaltantes.add("Documento de identidad")
        
        return camposFaltantes
    }
    
    /**
     * Obtiene el porcentaje de completitud de los datos personales
     * @return Porcentaje de 0 a 100
     */
    fun obtenerPorcentajeCompletitud(): Int {
        val camposRequeridos = 8 // nombre, email, telefono, direccion, seguro, numeroSeguro, fechaNacimiento, documentoIdentidad
        val camposCompletados = listOf(
            nombre.isNotBlank(),
            email.isNotBlank(),
            telefono.isNotBlank(),
            direccion.isNotBlank(),
            seguro.isNotBlank(),
            numeroSeguro.isNotBlank(),
            fechaNacimiento.isNotBlank(),
            documentoIdentidad.isNotBlank()
        ).count { it }
        
        return (camposCompletados * 100) / camposRequeridos
    }
    
    /**
     * Valida y establece el nombre del paciente
     * @param nombre Nombre a validar y establecer
     * @return true si el nombre es válido y se estableció, false en caso contrario
     */
    fun validarYEstablecerNombre(nombre: String): Boolean {
        val nombreValidado = validarYFormatearNombre(nombre)
        return if (nombreValidado != null) {
            this.nombre = nombreValidado
            true
        } else {
            false
        }
    }
    
    /**
     * Obtiene información resumida del paciente para mostrar en interfaces
     * @return String con información formateada del paciente
     */
    fun obtenerInformacionResumida(): String {
        return """
            👤 ${nombre.ifBlank { "Sin nombre" }}
            📧 ${email.ifBlank { "Sin correo" }}
            📞 ${telefono.ifBlank { "Sin teléfono" }}
            🏠 ${direccion.ifBlank { "Sin dirección" }}
            🏥 ${seguro.ifBlank { "Sin seguro" }}
            🔢 ${numeroSeguro.ifBlank { "Sin número de seguro" }}
            📅 ${fechaNacimiento.ifBlank { "Sin fecha de nacimiento" }}
            📄 ${documentoIdentidad.ifBlank { "Sin documento" }}
        """.trimIndent()
    }
    
    /**
     * Establece los datos personales del paciente
     */
    fun establecerDatosPersonales(
        telefono: String,
        direccion: String,
        seguro: String,
        numeroSeguro: String,
        fechaNacimiento: String,
        documentoIdentidad: String
    ) {
        this.telefono = telefono
        this.direccion = direccion
        this.seguro = seguro
        this.numeroSeguro = numeroSeguro
        this.fechaNacimiento = fechaNacimiento
        this.documentoIdentidad = documentoIdentidad
    }
    
    /**
     * Obtiene el teléfono del paciente
     */
    fun obtenerTelefono(): String = telefono
    
    /**
     * Obtiene la dirección del paciente
     */
    fun obtenerDireccion(): String = direccion
    
    /**
     * Obtiene el seguro del paciente
     */
    fun obtenerSeguro(): String = seguro
    
    /**
     * Obtiene el número de seguro del paciente
     */
    fun obtenerNumeroSeguro(): String = numeroSeguro
    
    /**
     * Obtiene la fecha de nacimiento del paciente
     */
    fun obtenerFechaNacimiento(): String = fechaNacimiento
    
    /**
     * Obtiene el documento de identidad del paciente
     */
    fun obtenerDocumentoIdentidad(): String = documentoIdentidad
    
    /**
     * Obtiene la edad del paciente calculada a partir de la fecha de nacimiento
     * @return Edad en años, 0 si no se puede calcular
     */
    fun obtenerEdad(): Int {
        return if (fechaNacimiento.isNotBlank()) {
            try {
                val fechaNac = java.time.LocalDate.parse(fechaNacimiento, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                val fechaActual = java.time.LocalDate.now()
                val edad = java.time.Period.between(fechaNac, fechaActual).years
                edad
            } catch (e: Exception) {
                0
            }
        } else {
            0
        }
    }
    
    /**
     * Método para solicitar una cita médica
     * @param especialidad Especialidad requerida
     * @param fecha Fecha deseada para la cita
     * @return String con la confirmación de la solicitud
     */
    fun solicitarCita(especialidad: String, fecha: String): String {
        return "Cita solicitada para $especialidad el $fecha"
    }
    
    /**
     * Método para ver el historial médico personal
     * @return String con el historial del paciente
     */
    fun verHistorialPersonal(): String {
        return "Historial médico personal del paciente: $nombre"
    }
    
    /**
     * Método para actualizar información personal
     * @param nuevaInformacion Map con la nueva información
     * @return String con la confirmación de actualización
     */
    fun actualizarInformacionPersonal(nuevaInformacion: Map<String, String>): String {
        // Simular la actualización de información personal
        val camposActualizados = nuevaInformacion.keys.joinToString(", ")
        return "Información personal actualizada para el paciente: $nombre\n" +
               "Campos actualizados: $camposActualizados"
    }
    
    /**
     * Método para consultar disponibilidad de médicos
     * @param especialidad Especialidad médica
     * @param fecha Fecha de consulta
     * @return String con la disponibilidad
     */
    fun consultarDisponibilidad(especialidad: String, fecha: String): String {
        return "Disponibilidad consultada para $especialidad el $fecha"
    }
} 