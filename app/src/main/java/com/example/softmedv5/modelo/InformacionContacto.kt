package com.example.softmedv5.modelo

/**
 * Clase que representa la información de contacto de un usuario
 * Permite gestionar datos personales y de contacto
 */
data class InformacionContacto(
    var telefono: String = "",
    var direccion: String = "",
    var ciudad: String = "",
    var codigoPostal: String = "",
    var fechaNacimiento: String = "",
    var genero: String = "",
    var documentoIdentidad: String = "",
    var tipoDocumento: String = "",
    var emergenciaContacto: String = "",
    var emergenciaTelefono: String = "",
    var fechaActualizacion: Long = System.currentTimeMillis()
) {
    
    /**
     * Valida que la información de contacto sea correcta
     * @return true si la información es válida, false en caso contrario
     */
    fun esInformacionValida(): Boolean {
        return telefono.isNotEmpty() && 
               direccion.isNotEmpty() && 
               ciudad.isNotEmpty() &&
               documentoIdentidad.isNotEmpty()
    }
    
    /**
     * Obtiene la información de contacto formateada
     * @return String con la información formateada
     */
    fun obtenerInformacionFormateada(): String {
        return """
            📞 Teléfono: $telefono
            🏠 Dirección: $direccion
            🏙️ Ciudad: $ciudad
            📮 Código Postal: $codigoPostal
            🎂 Fecha de Nacimiento: $fechaNacimiento
            👤 Género: $genero
            🆔 $tipoDocumento: $documentoIdentidad
            🚨 Contacto de Emergencia: $emergenciaContacto
            📱 Teléfono de Emergencia: $emergenciaTelefono
            📅 Última actualización: ${formatearFecha(fechaActualizacion)}
        """.trimIndent()
    }
    
    /**
     * Formatea la fecha de actualización
     * @param timestamp Timestamp en milisegundos
     * @return String con la fecha formateada
     */
    private fun formatearFecha(timestamp: Long): String {
        val fecha = java.util.Date(timestamp)
        val formato = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale("es", "ES"))
        return formato.format(fecha)
    }
    
    /**
     * Actualiza la información de contacto
     * @param nuevaInformacion Nueva información a actualizar
     */
    fun actualizarInformacion(nuevaInformacion: InformacionContacto) {
        this.telefono = nuevaInformacion.telefono
        this.direccion = nuevaInformacion.direccion
        this.ciudad = nuevaInformacion.ciudad
        this.codigoPostal = nuevaInformacion.codigoPostal
        this.fechaNacimiento = nuevaInformacion.fechaNacimiento
        this.genero = nuevaInformacion.genero
        this.documentoIdentidad = nuevaInformacion.documentoIdentidad
        this.tipoDocumento = nuevaInformacion.tipoDocumento
        this.emergenciaContacto = nuevaInformacion.emergenciaContacto
        this.emergenciaTelefono = nuevaInformacion.emergenciaTelefono
        this.fechaActualizacion = System.currentTimeMillis()
    }
} 