package com.example.softmedv5.modelo

/**
 * Clase de datos que representa el perfil completo de un paciente
 * Incluye toda la información personal y médica necesaria para la gestión administrativa
 */
data class PerfilPaciente(
    val id: String,
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val seguro: String,
    val numeroSeguro: String,
    val fechaNacimiento: String,
    val documentoIdentidad: String,
    val rol: String = "PACIENTE",
    val fechaRegistro: String = "",
    val estado: String = "ACTIVO",
    val documentosAdjuntos: List<String> = emptyList(),
    val validaciones: Map<String, Boolean> = emptyMap()
) {
    
    /**
     * Obtiene el nombre completo del paciente
     */
    fun obtenerNombre(): String = nombre
    
    /**
     * Verifica si el paciente tiene documentos validados
     */
    fun tieneDocumentosValidados(): Boolean {
        return validaciones.getOrDefault("documentoIdentidad", false) &&
               validaciones.getOrDefault("seguro", false)
    }
    
    /**
     * Obtiene el estado de validación como texto
     */
    fun obtenerEstadoValidacion(): String {
        return if (tieneDocumentosValidados()) "✅ Validado" else "⚠️ Pendiente"
    }
    
    /**
     * Obtiene información resumida del paciente
     */
    fun obtenerInformacionResumida(): String {
        return """
            👤 $nombre
            📧 $email
            📞 $telefono
            🏥 $seguro
            📄 $documentoIdentidad
        """.trimIndent()
    }
    
    /**
     * Obtiene información completa del paciente
     */
    fun obtenerInformacionCompleta(): String {
        return """
            👤 Nombre: $nombre
            📧 Email: $email
            📞 Teléfono: $telefono
            🏠 Dirección: $direccion
            🏥 Seguro: $seguro
            🔢 Número de Seguro: $numeroSeguro
            📅 Fecha de Nacimiento: $fechaNacimiento
            📄 Documento: $documentoIdentidad
            📋 Estado: $estado
            ✅ Validación: ${obtenerEstadoValidacion()}
        """.trimIndent()
    }
    
    /**
     * Crea una copia actualizada del perfil
     */
    fun actualizar(
        nombre: String = this.nombre,
        email: String = this.email,
        telefono: String = this.telefono,
        direccion: String = this.direccion,
        seguro: String = this.seguro,
        numeroSeguro: String = this.numeroSeguro,
        fechaNacimiento: String = this.fechaNacimiento,
        documentoIdentidad: String = this.documentoIdentidad
    ): PerfilPaciente {
        return copy(
            nombre = nombre,
            email = email,
            telefono = telefono,
            direccion = direccion,
            seguro = seguro,
            numeroSeguro = numeroSeguro,
            fechaNacimiento = fechaNacimiento,
            documentoIdentidad = documentoIdentidad
        )
    }
    
    /**
     * Valida que los campos obligatorios estén completos
     */
    fun validarCamposObligatorios(): Boolean {
        return nombre.isNotBlank() && 
               email.isNotBlank() && 
               telefono.isNotBlank() && 
               documentoIdentidad.isNotBlank()
    }
    
    /**
     * Obtiene lista de campos faltantes
     */
    fun obtenerCamposFaltantes(): List<String> {
        val camposFaltantes = mutableListOf<String>()
        
        if (nombre.isBlank()) camposFaltantes.add("Nombre")
        if (email.isBlank()) camposFaltantes.add("Email")
        if (telefono.isBlank()) camposFaltantes.add("Teléfono")
        if (documentoIdentidad.isBlank()) camposFaltantes.add("Documento de Identidad")
        
        return camposFaltantes
    }
} 