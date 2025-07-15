package com.example.softmedv5.modelo

/**
 * Interfaces granulares para mejorar el principio de segregación de interfaces
 */

/**
 * Interface para funcionalidades básicas de usuario
 */
interface UsuarioBasico {
    fun obtenerId(): String
    fun obtenerNombre(): String
    fun obtenerEmail(): String
    fun tieneInformacionBasicaCompleta(): Boolean
    fun obtenerCamposBasicosFaltantes(): List<String>
}

/**
 * Interface para gestión de permisos
 */
interface UsuarioConPermisos {
    fun obtenerPermisos(): List<String>
    fun puedeRealizarAccion(accion: String): Boolean
    fun obtenerNombreRol(): String
}

/**
 * Interface para gestión de información de contacto
 */
interface UsuarioConContacto {
    fun obtenerInformacionContacto(): InformacionContacto
    fun actualizarInformacionContacto(informacion: InformacionContacto): Boolean
    fun tieneInformacionContactoCompleta(): Boolean
}

/**
 * Interface para validación de datos
 */
interface UsuarioValidable {
    fun validarYFormatearNombre(nombre: String): String?
    fun validarYEstablecerNombre(nombre: String): Boolean
}

/**
 * Interface para funcionalidades específicas de paciente
 */
interface FuncionalidadesPaciente {
    fun tieneDatosPersonalesCompletos(): Boolean
    fun obtenerCamposFaltantes(): List<String>
    fun obtenerPorcentajeCompletitud(): Int
    fun obtenerInformacionResumida(): String
    fun solicitarCita(especialidad: String, fecha: String): String
    fun verHistorialPersonal(): String
}

/**
 * Interface para funcionalidades médicas
 */
interface FuncionalidadesMedico {
    fun obtenerEspecialidad(): String
    fun obtenerNumeroLicencia(): String
    fun crearHistorialMedico(datosPaciente: Map<String, String>, diagnostico: String): String
    fun prescribirMedicamento(medicamento: String, dosis: String, duracion: String): String
}

/**
 * Interface para funcionalidades administrativas
 */
interface FuncionalidadesAdministrativas {
    fun obtenerDepartamento(): String
    fun obtenerCargo(): String
    fun gestionarCitas(accion: String, datosCita: Map<String, String>): String
    fun gestionarPacientes(accion: String, datosPaciente: Map<String, String>): String
    fun generarReportes(tipo: String, parametros: Map<String, String>): String
} 