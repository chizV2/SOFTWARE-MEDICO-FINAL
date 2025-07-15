package com.example.softmedv5.util

import com.example.softmedv5.modelo.*

/**
 * Servicio de inyección de dependencias para mejorar el principio de inversión de dependencias
 * Implementa el patrón Service Locator
 */
object ServicioDependencias {
    
    // Gestores principales
    private val gestorAutenticacion: GestorAutenticacion by lazy { GestorAutenticacion.obtenerInstancia() }
    private val gestorCitas: GestorCitas by lazy { GestorCitas.obtenerInstancia() }
    private val gestorHistorial: GestorHistorialClinico by lazy { GestorHistorialClinico.obtenerInstancia() }
    private val gestorUsuarios: GestorUsuarios by lazy { GestorUsuarios() }
    
    /**
     * Obtiene el gestor de autenticación
     */
    fun obtenerGestorAutenticacion(): GestorAutenticacion = gestorAutenticacion
    
    /**
     * Obtiene el gestor de citas
     */
    fun obtenerGestorCitas(): GestorCitas = gestorCitas
    
    /**
     * Obtiene el gestor de historial clínico
     */
    fun obtenerGestorHistorial(): GestorHistorialClinico = gestorHistorial
    
    /**
     * Obtiene el gestor de usuarios
     */
    fun obtenerGestorUsuarios(): GestorUsuarios = gestorUsuarios
    
    /**
     * Crea un usuario según el rol especificado
     */
    fun crearUsuario(rol: String): Usuario = GestorUsuarios.crearUsuario(rol)
    
    /**
     * Obtiene la sesión actual
     */
    fun obtenerSesionActual(): SesionUsuario? = gestorAutenticacion.obtenerSesionActual()
    
    /**
     * Verifica si hay una sesión activa
     */
    fun haySesionActiva(): Boolean = gestorAutenticacion.obtenerSesionActual() != null
    
    /**
     * Cierra la sesión actual
     */
    fun cerrarSesion() = gestorAutenticacion.cerrarSesion()
} 