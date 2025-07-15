package com.example.softmedv5.modelo

/**
 * Clase que representa la sesión de un usuario autenticado
 * Contiene información de autenticación y estado de la sesión
 */
data class SesionUsuario(
    val usuario: Usuario,
    val fechaInicio: Long = System.currentTimeMillis(),
    val tokenSesion: String = generarTokenSesion(),
    var activa: Boolean = true
) {
    
    companion object {
        /**
         * Genera un token único para la sesión
         * @return String con el token generado
         */
        private fun generarTokenSesion(): String {
            return "TOKEN-${System.currentTimeMillis()}-${(1000..9999).random()}"
        }
    }
    
    /**
     * Verifica si la sesión está activa
     * @return true si la sesión está activa, false en caso contrario
     */
    fun esSesionActiva(): Boolean {
        return activa
    }
    
    /**
     * Cierra la sesión del usuario
     */
    fun cerrarSesion() {
        activa = false
    }
    
    /**
     * Obtiene la duración de la sesión en minutos
     * @return Long con la duración en minutos
     */
    fun obtenerDuracionSesion(): Long {
        return (System.currentTimeMillis() - fechaInicio) / (1000 * 60)
    }
} 