package com.example.softmedv5.util

import com.example.softmedv5.modelo.*

/**
 * Clase gestora de usuarios que implementa el patrón Factory
 * Permite crear usuarios según el rol seleccionado de manera escalable
 */
class GestorUsuarios {
    
    companion object {
        /**
         * Crea un usuario según el rol especificado
         * @param rol El rol del usuario a crear
         * @return Usuario creado según el rol
         * @throws IllegalArgumentException si el rol no es válido
         */
        fun crearUsuario(rol: String): Usuario {
            println("=== DEBUG: GestorUsuarios.crearUsuario ===")
            println("Rol recibido: '$rol'")
            
            // Normalizar el rol a mayúsculas y remover acentos para comparación
            val rolNormalizado = normalizarRol(rol)
            println("Rol normalizado: '$rolNormalizado'")
            
            // Mapeo más flexible de roles
            val rolMapeado = when {
                rolNormalizado.contains("MEDICO") || rolNormalizado.contains("DOCTOR") -> "MEDICO"
                rolNormalizado.contains("PACIENTE") || rolNormalizado.contains("CLIENTE") -> "PACIENTE"
                rolNormalizado.contains("ADMINISTRATIVO") || rolNormalizado.contains("ADMIN") -> "PERSONAL ADMINISTRATIVO"
                else -> rolNormalizado
            }
            
            println("Rol mapeado: '$rolMapeado'")
            
            return when (rolMapeado) {
                "MEDICO" -> {
                    val medico = Medico()
                    medico.establecerInformacionBasica(
                        "MED-001",
                        "Dr. Carlos Rodríguez",
                        "carlos.rodriguez@softmed.com"
                    )
                    medico
                }
                "PACIENTE" -> {
                    val paciente = Paciente()
                    paciente.establecerInformacionBasica(
                        "PAC-001",
                        "María González López",
                        "maria.gonzalez@softmed.com"
                    )
                    paciente
                }
                "PERSONAL ADMINISTRATIVO" -> {
                    val administrativo = PersonalAdministrativo()
                    administrativo.establecerInformacionBasica(
                        "ADM-001",
                        "Lic. Ana Martínez",
                        "ana.martinez@softmed.com"
                    )
                    administrativo
                }
                else -> {
                    throw IllegalArgumentException("Rol no válido: $rolMapeado")
                }
            }
        }
        
        /**
         * Genera un ID único para el usuario
         * @return String con el ID generado
         */
        private fun generarId(): String {
            return "USR-${System.currentTimeMillis()}"
        }
        
        /**
         * Obtiene la lista de roles disponibles en el sistema
         * @return Lista de roles disponibles
         */
        fun obtenerRolesDisponibles(): List<String> {
            return listOf("MEDICO", "PACIENTE", "PERSONAL ADMINISTRATIVO")
        }
        
        /**
         * Verifica si un rol es válido
         * @param rol El rol a verificar
         * @return true si el rol es válido, false en caso contrario
         */
        fun esRolValido(rol: String): Boolean {
            // Normalizar el rol a mayúsculas y remover acentos para comparación
            val rolNormalizado = normalizarRol(rol)
            return obtenerRolesDisponibles().contains(rolNormalizado)
        }
        
        /**
         * Normaliza un rol removiendo acentos y convirtiendo a mayúsculas
         * @param rol El rol a normalizar
         * @return Rol normalizado
         */
        private fun normalizarRol(rol: String): String {
            return rol.uppercase()
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("Ñ", "N")
                .trim()
        }
        
        // Lista dinámica de usuarios registrados (en una app real sería una base de datos)
        private val usuariosRegistrados = mutableListOf<Usuario>()
        
        /**
         * Registra un nuevo usuario en el sistema
         * @param usuario El usuario a registrar
         */
        fun registrarUsuario(usuario: Usuario) {
            if (!usuariosRegistrados.any { it.obtenerId() == usuario.obtenerId() }) {
                usuariosRegistrados.add(usuario)
            }
        }
        
        /**
         * Obtiene todos los usuarios registrados para el chat
         * @return Lista de usuarios disponibles para comunicación
         */
        fun obtenerUsuariosParaChat(): List<Usuario> {
            // Si no hay usuarios registrados, crear algunos de demostración
            if (usuariosRegistrados.isEmpty()) {
                crearUsuariosDemo()
            }
            return usuariosRegistrados.toList()
        }
        
        /**
         * Obtiene usuarios por rol específico
         * @param rol El rol a filtrar
         * @return Lista de usuarios del rol especificado
         */
        fun obtenerUsuariosPorRol(rol: String): List<Usuario> {
            return usuariosRegistrados.filter { it.obtenerNombreRol() == rol }
        }
        
        /**
         * Obtiene todos los usuarios registrados en el sistema
         * @return Lista de todos los usuarios
         */
        fun obtenerTodosLosUsuarios(): List<Usuario> {
            // Si no hay usuarios registrados, crear algunos de demostración
            if (usuariosRegistrados.isEmpty()) {
                crearUsuariosDemo()
            }
            return usuariosRegistrados.toList()
        }
        
        /**
         * Busca usuarios por nombre
         * @param nombre El nombre a buscar
         * @return Lista de usuarios que coinciden con el nombre
         */
        fun buscarUsuariosPorNombre(nombre: String): List<Usuario> {
            return usuariosRegistrados.filter { 
                it.obtenerNombre().contains(nombre, ignoreCase = true) 
            }
        }
        
        /**
         * Crea usuarios de demostración para el chat
         */
        private fun crearUsuariosDemo() {
            // Pacientes
            val paciente1 = Paciente()
            paciente1.establecerInformacionBasica("PAC-001", "María González", "maria@email.com")
            registrarUsuario(paciente1)
            
            val paciente2 = Paciente()
            paciente2.establecerInformacionBasica("PAC-002", "Carlos Rodríguez", "carlos@email.com")
            registrarUsuario(paciente2)
            
            val paciente3 = Paciente()
            paciente3.establecerInformacionBasica("PAC-003", "Ana Fernández", "ana@email.com")
            registrarUsuario(paciente3)
            
            // Médicos
            val medico1 = Medico()
            medico1.establecerInformacionBasica("MED-001", "Dr. Juan Pérez", "dr.perez@softmed.com")
            medico1.establecerInformacionMedica("Cardiología", "MED-12345")
            registrarUsuario(medico1)
            
            val medico2 = Medico()
            medico2.establecerInformacionBasica("MED-002", "Dra. Laura Sánchez", "dra.sanchez@softmed.com")
            medico2.establecerInformacionMedica("Dermatología", "MED-67890")
            registrarUsuario(medico2)
            
            val medico3 = Medico()
            medico3.establecerInformacionBasica("MED-003", "Dr. Roberto Silva", "dr.silva@softmed.com")
            medico3.establecerInformacionMedica("Médico General", "MED-11111")
            registrarUsuario(medico3)
            
            // Personal Administrativo
            val admin1 = PersonalAdministrativo()
            admin1.establecerInformacionBasica("ADM-001", "Lic. Ana Martínez", "ana.martinez@softmed.com")
            registrarUsuario(admin1)
            
            val admin2 = PersonalAdministrativo()
            admin2.establecerInformacionBasica("ADM-002", "Sr. Roberto Jiménez", "roberto.jimenez@softmed.com")
            registrarUsuario(admin2)
        }
        
        /**
         * Obtiene la representación en string de un usuario para el chat
         * @param usuario El usuario a formatear
         * @return String formateado para mostrar en el chat
         */
        fun formatearUsuarioParaChat(usuario: Usuario): String {
            val emoji = when (usuario.obtenerNombreRol()) {
                "Paciente" -> "👤"
                "Médico" -> "👨‍⚕️"
                "Personal Administrativo" -> "👩‍💼"
                else -> "👤"
            }
            
            val rol = when (usuario.obtenerNombreRol()) {
                "Médico" -> {
                    val medico = usuario as? Medico
                    medico?.obtenerEspecialidad() ?: "MEDICO"
                }
                "Paciente" -> "PACIENTE"
                "Personal Administrativo" -> "PERSONAL ADMINISTRATIVO"
                else -> usuario.obtenerNombreRol().uppercase()
            }
            
            return "$emoji ${usuario.obtenerNombre()} ($rol)"
        }
    }
} 