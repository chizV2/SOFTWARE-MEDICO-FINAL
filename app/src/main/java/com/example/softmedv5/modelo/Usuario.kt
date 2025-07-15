package com.example.softmedv5.modelo

/**
 * Clase base abstracta que representa un usuario del sistema médico
 * Implementa el patrón de herencia para diferentes tipos de usuarios
 */
abstract class Usuario {
    
    protected var id: String = ""
    protected var nombre: String = ""
    protected var email: String = ""
    protected var permisos: List<String> = listOf()
    protected var informacionContacto: InformacionContacto = InformacionContacto()
    
    /**
     * Obtiene el nombre del rol del usuario
     * @return String con el nombre del rol
     */
    abstract fun obtenerNombreRol(): String
    
    /**
     * Obtiene los permisos específicos del usuario
     * @return Lista de permisos disponibles
     */
    abstract fun obtenerPermisos(): List<String>
    
    /**
     * Verifica si el usuario puede realizar una acción específica
     * @param accion La acción a verificar
     * @return true si tiene permiso, false en caso contrario
     */
    fun puedeRealizarAccion(accion: String): Boolean {
        return permisos.contains(accion)
    }
    
    /**
     * Establece la información básica del usuario
     * @param id Identificador único
     * @param nombre Nombre completo
     * @param email Correo electrónico
     */
    fun establecerInformacionBasica(id: String, nombre: String, email: String) {
        this.id = id
        this.nombre = nombre
        this.email = email
    }
    
    /**
     * Verifica si el usuario tiene información personal básica completa
     * @return true si tiene nombre y email, false en caso contrario
     */
    fun tieneInformacionBasicaCompleta(): Boolean {
        return nombre.isNotBlank() && email.isNotBlank()
    }
    
    /**
     * Obtiene los campos faltantes de la información básica
     * @return Lista de campos faltantes
     */
    fun obtenerCamposBasicosFaltantes(): List<String> {
        val camposFaltantes = mutableListOf<String>()
        
        if (nombre.isBlank()) camposFaltantes.add("Nombre completo")
        if (email.isBlank()) camposFaltantes.add("Correo electrónico")
        
        return camposFaltantes
    }
    
    /**
     * Valida y formatea el nombre del usuario
     * @param nombre Nombre a validar
     * @return Nombre formateado o null si es inválido
     */
    fun validarYFormatearNombre(nombre: String): String? {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.length < 2) {
            return null
        }
        
        // Capitalizar primera letra de cada palabra
        return nombreLimpio.split(" ").joinToString(" ") { palabra ->
            if (palabra.isNotEmpty()) {
                palabra[0].uppercase() + palabra.substring(1).lowercase()
            } else {
                palabra
            }
        }
    }
    
    /**
     * Obtiene el identificador del usuario
     * @return String con el ID
     */
    fun obtenerId(): String = id
    
    /**
     * Obtiene el nombre del usuario
     * @return String con el nombre
     */
    fun obtenerNombre(): String = nombre
    
    /**
     * Obtiene el email del usuario
     * @return String con el email
     */
    fun obtenerEmail(): String = email
    
    /**
     * Obtiene la información de contacto del usuario
     * @return InformacionContacto del usuario
     */
    fun obtenerInformacionContacto(): InformacionContacto {
        return informacionContacto
    }
    
    /**
     * Establece la información de contacto del usuario
     * @param informacionContacto Nueva información de contacto
     */
    fun establecerInformacionContacto(informacionContacto: InformacionContacto) {
        this.informacionContacto = informacionContacto
    }
    
    /**
     * Actualiza la información de contacto del usuario
     * @param nuevaInformacion Nueva información a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun actualizarInformacionContacto(nuevaInformacion: InformacionContacto): Boolean {
        return try {
            informacionContacto.actualizarInformacion(nuevaInformacion)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Verifica si el usuario tiene información de contacto completa
     * @return true si tiene información completa, false en caso contrario
     */
    fun tieneInformacionContactoCompleta(): Boolean {
        return informacionContacto.esInformacionValida()
    }
} 