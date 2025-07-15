package com.example.softmedv5.modelo

/**
 * Clase que representa el Personal Administrativo del sistema médico
 * Tiene acceso a funcionalidades administrativas y de gestión
 */
class PersonalAdministrativo : Usuario() {
    
    private var departamento: String = ""
    private var cargo: String = ""
    
    // Datos adicionales específicos del personal administrativo
    private var telefono: String = ""
    private var direccion: String = ""
    private var fechaNacimiento: String = ""
    private var documentoIdentidad: String = ""
    
    init {
        permisos = listOf(
            "GESTIONAR_CITAS",
            "GESTIONAR_PACIENTES",
            "GESTIONAR_INVENTARIO",
            "GENERAR_REPORTES_BASICOS",
            "GESTIONAR_FACTURACION",
            "ACCESO_ADMINISTRATIVO"
        )
    }
    
    override fun obtenerNombreRol(): String {
        return "Personal Administrativo"
    }
    
    override fun obtenerPermisos(): List<String> {
        return permisos
    }
    
    /**
     * Establece la información administrativa específica
     * @param departamento Departamento donde trabaja
     * @param cargo Cargo que desempeña
     */
    fun establecerInformacionAdministrativa(departamento: String, cargo: String) {
        this.departamento = departamento
        this.cargo = cargo
    }
    
    /**
     * Obtiene el departamento del personal administrativo
     * @return String con el departamento
     */
    fun obtenerDepartamento(): String = departamento
    
    /**
     * Obtiene el cargo del personal administrativo
     * @return String con el cargo
     */
    fun obtenerCargo(): String = cargo
    
    /**
     * Establece los datos personales del personal administrativo
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
     * Obtiene el teléfono del personal administrativo
     */
    fun obtenerTelefono(): String = telefono
    
    /**
     * Obtiene la dirección del personal administrativo
     */
    fun obtenerDireccion(): String = direccion
    
    /**
     * Obtiene la fecha de nacimiento del personal administrativo
     */
    fun obtenerFechaNacimiento(): String = fechaNacimiento
    
    /**
     * Obtiene el documento de identidad del personal administrativo
     */
    fun obtenerDocumentoIdentidad(): String = documentoIdentidad
    
    /**
     * Método para gestionar citas médicas
     * @param accion Acción a realizar (crear, modificar, cancelar)
     * @param datosCita Datos de la cita
     * @return String con la confirmación de la gestión
     */
    fun gestionarCitas(accion: String, datosCita: Map<String, String>): String {
        return "Cita ${accion.lowercase()} para ${datosCita["paciente"]} el ${datosCita["fecha"]}"
    }
    
    /**
     * Método para gestionar el inventario
     * @param accion Acción a realizar (agregar, remover, actualizar)
     * @param item Item del inventario
     * @param cantidad Cantidad a gestionar
     * @return String con la confirmación de la gestión
     */
    fun gestionarInventario(accion: String, item: String, cantidad: Int): String {
        return "Inventario: $accion $cantidad unidades de $item"
    }
    
    /**
     * Método para generar reportes básicos
     * @param tipoReporte Tipo de reporte a generar
     * @return String con el reporte generado
     */
    fun generarReporteBasico(tipoReporte: String): String {
        return "Reporte básico de $tipoReporte generado por el personal administrativo"
    }
} 