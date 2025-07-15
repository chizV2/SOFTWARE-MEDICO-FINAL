package com.example.softmedv5.util

import android.content.Context
import com.example.softmedv5.database.SimpleDatabaseHelper
import com.example.softmedv5.modelo.*

/**
 * Gestor de autenticación que maneja registro e inicio de sesión
 * Implementa el patrón Singleton para mantener una única instancia
 */
class GestorAutenticacion private constructor() {
    
    companion object {
        @Volatile
        private var INSTANCE: GestorAutenticacion? = null
        
        fun obtenerInstancia(): GestorAutenticacion {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GestorAutenticacion().also { INSTANCE = it }
            }
        }
    }
    
    private var dbHelper: SimpleDatabaseHelper? = null
    private var sesionActual: SesionUsuario? = null
    
    /**
     * Inicializa la conexión con la base de datos
     * @param context Contexto de la aplicación
     */
    fun inicializarBaseDatos(context: Context) {
        if (dbHelper == null) {
            dbHelper = SimpleDatabaseHelper(context)
        }
    }
    
    /**
     * Normaliza el rol del usuario para consistencia en la base de datos
     * @param rol Rol a normalizar
     * @return Rol normalizado en mayúsculas sin acentos
     */
    private fun normalizarRol(rol: String): String {
        return rol.uppercase()
            .replace("Á", "A")
            .replace("É", "E")
            .replace("Í", "I")
            .replace("Ó", "O")
            .replace("Ú", "U")
            .replace("Ñ", "N")
            .replace("ADMINISTRADOR", "PERSONAL ADMINISTRATIVO")
            .trim()
    }
    
    /**
     * Registra un nuevo usuario en el sistema
     * @param email Email del usuario
     * @param contrasena Contraseña del usuario
     * @param nombreCompleto Nombre completo del usuario
     * @param rol Rol del usuario
     * @return ResultadoAutenticacion del registro
     */
    fun registrarUsuario(email: String, contrasena: String, nombreCompleto: String, rol: String): ResultadoAutenticacion {
        try {
            // Verificar que la base de datos esté inicializada
            if (dbHelper == null) {
                return ResultadoAutenticacion(
                    exitoso = false,
                    mensaje = "Error: Base de datos no inicializada"
                )
            }
            
            // Verificar que el email no esté ya registrado
            if (dbHelper!!.existeUsuarioConEmail(email)) {
                return ResultadoAutenticacion(
                    exitoso = false,
                    mensaje = "El email ya está registrado en el sistema"
                )
            }
            
            // Normalizar el rol
            val rolNormalizado = normalizarRol(rol)
            
            // Insertar usuario en la base de datos
            val usuarioId = dbHelper!!.insertarUsuario(email, contrasena, nombreCompleto, rolNormalizado)
            
            if (usuarioId > 0) {
                // Crear el usuario según el rol
                val usuario = GestorUsuarios.crearUsuario(rolNormalizado)
                usuario.establecerInformacionBasica(usuarioId.toString(), nombreCompleto, email)
                
                // Registrar el usuario en GestorUsuarios para que esté disponible en toda la app
                GestorUsuarios.registrarUsuario(usuario)
                
                // Notificar al personal administrativo sobre el nuevo usuario
                val gestorNotificaciones = GestorNotificaciones.obtenerInstancia()
                gestorNotificaciones.notificarNuevoUsuario(usuario)
                
                // Crear sesión
                sesionActual = SesionUsuario(usuario)
                
                // Crear mensajes de prueba para verificar la comunicación
                crearMensajesDePrueba(usuarioId.toInt(), nombreCompleto, rolNormalizado)
                
                return ResultadoAutenticacion(
                    exitoso = true,
                    mensaje = "Usuario registrado exitosamente",
                    usuario = usuario,
                    sesion = sesionActual
                )
            } else {
                return ResultadoAutenticacion(
                    exitoso = false,
                    mensaje = "Error al registrar el usuario en la base de datos"
                )
            }
        } catch (e: Exception) {
            return ResultadoAutenticacion(
                exitoso = false,
                mensaje = "Error durante el registro: ${e.message}"
            )
        }
    }
    
    /**
     * Inicia sesión con credenciales existentes
     * @param email Email del usuario
     * @param contrasena Contraseña del usuario
     * @return ResultadoAutenticacion del inicio de sesión
     */
    fun iniciarSesion(email: String, contrasena: String): ResultadoAutenticacion {
        
        try {
            // Buscar usuario en la base de datos SQLite
            val usuarioDb = dbHelper?.obtenerUsuarioPorEmail(email)
            
            if (usuarioDb == null) {
                return ResultadoAutenticacion(
                    exitoso = false,
                    mensaje = "El email no está registrado en el sistema"
                )
            }
            
            // Verificar contraseña
            val contrasenaAlmacenada = usuarioDb["contrasena"] as? String ?: ""
            if (contrasenaAlmacenada != encriptarContrasena(contrasena)) {
                return ResultadoAutenticacion(
                    exitoso = false,
                    mensaje = "La contraseña es incorrecta"
                )
            }
            
            // Verificar que el usuario esté activo
            val activo = (usuarioDb["activo"] as? Long ?: 0) == 1L
            if (!activo) {
                return ResultadoAutenticacion(
                    exitoso = false,
                    mensaje = "La cuenta está desactivada"
                )
            }
            
            // Crear el usuario según el rol
            val rol = usuarioDb["rol"] as? String ?: ""
            val nombreCompleto = usuarioDb["nombre_completo"] as? String ?: ""
            val id = (usuarioDb["id"] as? Long ?: 0).toString()
            
            // Normalizar el rol para asegurar consistencia
            val rolNormalizado = normalizarRol(rol)
            
            val usuario = GestorUsuarios.crearUsuario(rolNormalizado)
            usuario.establecerInformacionBasica(id, nombreCompleto, email)
            
            // Cargar información de contacto desde la base de datos
            cargarInformacionContacto(email)
            
            // Crear sesión
            sesionActual = SesionUsuario(usuario)
            
            return ResultadoAutenticacion(
                exitoso = true,
                mensaje = "Sesión iniciada exitosamente",
                usuario = usuario,
                sesion = sesionActual
            )
        } catch (e: Exception) {
            return ResultadoAutenticacion(
                exitoso = false,
                mensaje = "Error durante el inicio de sesión: ${e.message}"
            )
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    fun cerrarSesion() {
        // Guardar cualquier cambio pendiente antes de cerrar la sesión
        guardarCambiosPendientes()
        
        sesionActual?.cerrarSesion()
        sesionActual = null
    }
    
    /**
     * Guarda los cambios pendientes del usuario actual
     */
    private fun guardarCambiosPendientes() {
        try {
            val sesion = obtenerSesionActual()
            if (sesion != null) {
                val usuario = sesion.usuario
                val informacionContacto = usuario.obtenerInformacionContacto()
                
                // Guardar información de contacto si hay cambios
                if (informacionContacto.esInformacionValida()) {
                    actualizarInformacionContacto(informacionContacto)
                }
            }
        } catch (e: Exception) {
            // Silenciar errores al guardar cambios pendientes
            println("Error al guardar cambios pendientes: ${e.message}")
        }
    }
    
    /**
     * Obtiene la sesión actual
     * @return SesionUsuario actual o null si no hay sesión
     */
    fun obtenerSesionActual(): SesionUsuario? {
        return sesionActual
    }
    
    /**
     * Verifica si hay una sesión activa
     * @return true si hay sesión activa, false en caso contrario
     */
    fun haySesionActiva(): Boolean {
        return sesionActual != null && sesionActual!!.esSesionActiva()
    }
    
    /**
     * Actualiza la contraseña del usuario actual
     * @param nuevaContrasena Nueva contraseña
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun actualizarContrasena(nuevaContrasena: String): Boolean {
        try {
            val sesion = obtenerSesionActual()
            if (sesion == null) {
                return false
            }
            
            val usuario = sesion.usuario
            val email = usuario.obtenerEmail()
            
            // Actualizar en la base de datos
            val exito = dbHelper?.actualizarContrasena(email, encriptarContrasena(nuevaContrasena)) ?: false
            
            return exito
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * Actualiza la información básica del usuario actual
     * @param nombre Nombre completo del usuario
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun actualizarInformacionBasica(nombre: String): Boolean {
        try {
            val sesion = obtenerSesionActual()
            if (sesion == null) {
                return false
            }
            
            val usuario = sesion.usuario
            val email = usuario.obtenerEmail()
            
            // Actualizar en la base de datos
            val exito = dbHelper?.actualizarInformacionBasica(email, nombre) ?: false
            
            if (exito) {
                // Actualizar también en la sesión actual
                usuario.establecerInformacionBasica(usuario.obtenerId(), nombre, email)
            }
            
            return exito
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * Actualiza la información de contacto del usuario actual
     * @param informacionContacto Nueva información de contacto
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun actualizarInformacionContacto(informacionContacto: com.example.softmedv5.modelo.InformacionContacto): Boolean {
        try {
            val sesion = obtenerSesionActual()
            if (sesion == null) {
                return false
            }
            
            val usuario = sesion.usuario
            val email = usuario.obtenerEmail()
            
            // Actualizar en la base de datos
            val exito = dbHelper?.actualizarInformacionContacto(email, informacionContacto) ?: false
            
            if (exito) {
                // Actualizar también en la sesión actual
                usuario.establecerInformacionContacto(informacionContacto)
            }
            
            return exito
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * Carga la información de contacto del usuario desde la base de datos
     * @param email Email del usuario
     * @return true si se cargó correctamente, false en caso contrario
     */
    fun cargarInformacionContacto(email: String): Boolean {
        try {
            // println("=== DEBUG: cargarInformacionContacto ===")
            // println("Email: $email")
            
            val sesion = obtenerSesionActual()
            if (sesion == null) {
                // println("❌ No hay sesión activa")
                return false
            }
            
            val usuario = sesion.usuario
            val rol = usuario.obtenerNombreRol()
            
            if (rol == "Paciente") {
                // Para pacientes, cargar datos completos
                val datosCompletos = dbHelper?.obtenerDatosCompletosPaciente(email)
                if (datosCompletos != null) {
                    // Crear información de contacto
                    val informacionContacto = com.example.softmedv5.modelo.InformacionContacto(
                        telefono = datosCompletos["telefono"] as? String ?: "",
                        direccion = datosCompletos["direccion"] as? String ?: "",
                        ciudad = datosCompletos["ciudad"] as? String ?: "",
                        codigoPostal = datosCompletos["codigo_postal"] as? String ?: "",
                        fechaNacimiento = datosCompletos["fecha_nacimiento"] as? String ?: "",
                        genero = datosCompletos["genero"] as? String ?: "",
                        documentoIdentidad = datosCompletos["documento_identidad"] as? String ?: "",
                        tipoDocumento = datosCompletos["tipo_documento"] as? String ?: "",
                        emergenciaContacto = datosCompletos["emergencia_contacto"] as? String ?: "",
                        emergenciaTelefono = datosCompletos["emergencia_telefono"] as? String ?: ""
                    )
                    
                    // Establecer información de contacto
                    usuario.establecerInformacionContacto(informacionContacto)
                    
                    // Si es paciente, establecer datos personales específicos
                    if (usuario is Paciente) {
                        val seguro = datosCompletos["seguro"] as? String ?: ""
                        val numeroSeguro = datosCompletos["numero_seguro"] as? String ?: ""
                        val fechaNacimiento = datosCompletos["fecha_nacimiento"] as? String ?: ""
                        val documentoIdentidad = datosCompletos["documento_identidad"] as? String ?: ""
                        val direccion = datosCompletos["direccion"] as? String ?: ""
                        val telefono = datosCompletos["telefono"] as? String ?: ""
                        
                        usuario.establecerDatosPersonales(
                            telefono = telefono,
                            direccion = direccion,
                            seguro = seguro,
                            numeroSeguro = numeroSeguro,
                            fechaNacimiento = fechaNacimiento,
                            documentoIdentidad = documentoIdentidad
                        )
                        
                        // println("✅ Datos del paciente cargados exitosamente")
                        // println("Teléfono: $telefono")
                        // println("Dirección: $direccion")
                        // println("Seguro: $seguro")
                        // println("Número de seguro: $numeroSeguro")
                        // println("Fecha de nacimiento: $fechaNacimiento")
                        // println("Documento: $documentoIdentidad")
                    }
                    
                    return true
                } else {
                    // println("❌ No se pudieron obtener los datos completos del paciente")
                    return false
                }
            } else {
                // Para otros tipos de usuario, cargar solo información de contacto básica
                val informacionContacto = dbHelper?.obtenerInformacionContacto(email)
                if (informacionContacto != null) {
                    usuario.establecerInformacionContacto(informacionContacto)
                    // println("✅ Información de contacto cargada para $rol")
                    return true
                } else {
                    // println("❌ No se encontró información de contacto para $rol")
                    return false
                }
            }
        } catch (e: Exception) {
            // println("❌ Error al cargar información de contacto: ${e.message}")
            // e.printStackTrace()
            return false
        }
    }
    
    /**
     * Encripta la contraseña (simulación básica)
     * @param contrasena Contraseña a encriptar
     * @return String con la contraseña encriptada
     */
    private fun encriptarContrasena(contrasena: String): String {
        // En un sistema real, usarías algoritmos de hash seguros como bcrypt
        return contrasena.hashCode().toString()
    }
    
    /**
     * Obtiene la cantidad de usuarios registrados
     * @return Int con la cantidad de usuarios
     */
    fun obtenerCantidadUsuariosRegistrados(): Int {
        return try {
            val usuarios = dbHelper?.obtenerTodosLosUsuarios() ?: emptyList()
            usuarios.size
        } catch (e: Exception) {
            0
        }
    }
    
    /**
     * Cierra la conexión con la base de datos
     */
    fun cerrarBaseDatos() {
        try {
            dbHelper?.close()
            dbHelper = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Limpia todos los usuarios y datos relacionados de la base de datos
     * SOLO USAR PARA LIMPIAR DATOS DE PRUEBA
     */
    fun limpiarTodosLosUsuarios(): Boolean {
        return try {
            val resultado = dbHelper?.limpiarTodosLosUsuarios() ?: false
            if (resultado) {
                // También limpiar la sesión actual si existe
                cerrarSesion()
            }
            resultado
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Crea mensajes de prueba para verificar la comunicación
     */
    private fun crearMensajesDePrueba(usuarioId: Int, nombreCompleto: String, rol: String) {
        try {
            // Obtener algunos usuarios existentes para crear conversaciones
            val usuariosExistentes = dbHelper?.obtenerTodosLosUsuarios() ?: emptyList()
            
            // Crear mensajes con algunos usuarios existentes
            usuariosExistentes.take(3).forEach { usuarioExistente ->
                val otroUsuarioId = (usuarioExistente["id"] as? Long)?.toInt() ?: 0
                val otroUsuarioNombre = usuarioExistente["nombre_completo"] as? String ?: "Usuario"
                val otroUsuarioRol = usuarioExistente["rol"] as? String ?: "DESCONOCIDO"
                
                if (otroUsuarioId > 0 && otroUsuarioId != usuarioId) {
                    // Mensaje del nuevo usuario a un usuario existente
                    val mensajeId1 = dbHelper?.insertarMensaje(
                        usuarioId,
                        nombreCompleto,
                        rol,
                        otroUsuarioId,
                        otroUsuarioNombre,
                        otroUsuarioRol,
                        "Mensaje de prueba",
                        "¡Hola! Soy $nombreCompleto, acabo de registrarme como $rol. ¿Cómo estás?"
                    ) ?: 0
                    
                    // Mensaje de respuesta del usuario existente al nuevo usuario
                    val mensajeId2 = dbHelper?.insertarMensaje(
                        otroUsuarioId,
                        otroUsuarioNombre,
                        otroUsuarioRol,
                        usuarioId,
                        nombreCompleto,
                        rol,
                        "Respuesta de prueba",
                        "¡Hola $nombreCompleto! Bienvenido al sistema. Soy $otroUsuarioNombre ($otroUsuarioRol)."
                    ) ?: 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * Clase que representa el resultado de una operación de autenticación
 */
data class ResultadoAutenticacion(
    val exitoso: Boolean,
    val mensaje: String,
    val usuario: Usuario? = null,
    val sesion: SesionUsuario? = null
) 