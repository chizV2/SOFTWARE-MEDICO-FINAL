package com.example.softmedv5

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.PerfilPaciente
import com.example.softmedv5.modelo.Paciente
import com.example.softmedv5.modelo.Medico
import com.example.softmedv5.modelo.PersonalAdministrativo
import com.example.softmedv5.util.GestorUsuarios
import com.example.softmedv5.util.GestorNotificaciones
import com.example.softmedv5.util.ServicioDependencias
import com.example.softmedv5.database.SimpleDatabaseHelper
import java.util.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.Toast

class RegistroPacientesActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var buttonNuevoPaciente: Button
    private lateinit var buttonBuscarPaciente: Button
    private lateinit var buttonResumen: Button
    private lateinit var buttonVolver: Button
    private lateinit var linearLayoutPacientes: LinearLayout
    private lateinit var textViewSinPacientes: TextView
    
    private val calendar = Calendar.getInstance()
    private val pacientesRegistrados = mutableListOf<PerfilPaciente>()
    private lateinit var dbHelper: SimpleDatabaseHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pacientes)
        
        // Inicializar el helper de base de datos
        dbHelper = SimpleDatabaseHelper(this)
        
        inicializarVistas()
        configurarEventos()
        cargarUsuariosDesdeBaseDatos()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        buttonNuevoPaciente = findViewById(R.id.button_nuevo_paciente)
        buttonBuscarPaciente = findViewById(R.id.button_buscar_paciente)
        buttonResumen = findViewById(R.id.button_resumen)
        buttonVolver = findViewById(R.id.button_volver)
        linearLayoutPacientes = findViewById(R.id.linear_layout_pacientes)
        textViewSinPacientes = findViewById(R.id.text_view_sin_pacientes)
        
        textViewTitulo.text = "👥 Registro y Actualización de Usuarios"
        
        // Agregar botón de prueba temporal
        agregarBotonPrueba()
    }
    
    private fun agregarBotonPrueba() {
        val botonPrueba = Button(this)
        botonPrueba.text = "🧪 Crear Paciente de Prueba"
        botonPrueba.textSize = 12f
        botonPrueba.background = resources.getDrawable(R.drawable.button_background, null)
        botonPrueba.setTextColor(resources.getColor(android.R.color.white, null))
        botonPrueba.setPadding(16, 8, 16, 8)
        
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 8, 0, 8)
        botonPrueba.layoutParams = layoutParams
        
        botonPrueba.setOnClickListener {
            crearPacientePrueba()
        }
        
        // Insertar después del título
        val parent = textViewTitulo.parent as LinearLayout
        val index = parent.indexOfChild(textViewTitulo) + 1
        parent.addView(botonPrueba, index)
    }
    
    private fun crearPacientePrueba() {
        println("=== PRUEBA: Creando paciente de prueba ===")
        
        crearNuevoUsuario(
            nombre = "Paciente Prueba",
            email = "paciente.prueba@test.com",
            telefono = "+34 600 000 000",
            direccion = "Calle Prueba 123",
            ciudad = "Madrid",
            codigoPostal = "28001",
            fechaNacimiento = "15/03/1990",
            genero = "Femenino",
            tipoDocumento = "DNI",
            documento = "12345678A",
            emergenciaContacto = "María González",
            emergenciaTelefono = "+34 600 000 001",
            seguro = "Sanitas",
            numeroSeguro = "SAN-123456"
        )
    }
    
    private fun configurarEventos() {
        buttonNuevoPaciente.setOnClickListener {
            mostrarFormularioNuevoPaciente()
        }
        
        buttonBuscarPaciente.setOnClickListener {
            mostrarBusquedaPaciente()
        }
        
        buttonResumen.setOnClickListener {
            mostrarResumenPacientes()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun cargarUsuariosDesdeBaseDatos() {
        // Limpiar la lista actual
        linearLayoutPacientes.removeAllViews()
        
        // Obtener TODOS los usuarios desde la base de datos
        val usuariosDb = dbHelper.obtenerTodosLosUsuarios()
        
        // Convertir usuarios de la base de datos a PerfilPaciente para mostrar
        pacientesRegistrados.clear()
        usuariosDb.forEach { usuarioDb ->
            val perfilUsuario = PerfilPaciente(
                id = (usuarioDb["id"] as? Long ?: 0).toString(),
                nombre = usuarioDb["nombre_completo"] as? String ?: "Sin nombre",
                email = usuarioDb["email"] as? String ?: "Sin email",
                telefono = obtenerTelefonoDesdeContacto((usuarioDb["id"] as? Long ?: 0).toInt()),
                direccion = obtenerDireccionDesdeContacto((usuarioDb["id"] as? Long ?: 0).toInt()),
                seguro = "No aplica", // Los seguros solo aplican a pacientes
                numeroSeguro = "No aplica",
                fechaNacimiento = obtenerFechaNacimientoDesdeContacto((usuarioDb["id"] as? Long ?: 0).toInt()),
                documentoIdentidad = obtenerDocumentoDesdeContacto((usuarioDb["id"] as? Long ?: 0).toInt()),
                rol = usuarioDb["rol"] as? String ?: "DESCONOCIDO"
            )
            pacientesRegistrados.add(perfilUsuario)
        }
        
        if (pacientesRegistrados.isNotEmpty()) {
            pacientesRegistrados.forEach { usuario ->
                agregarUsuarioALista(usuario)
            }
            textViewSinPacientes.visibility = View.GONE
            linearLayoutPacientes.visibility = View.VISIBLE
        } else {
            mostrarSinUsuarios()
        }
    }
    
    private fun mostrarResumenPacientes() {
        val todosLosUsuarios = GestorUsuarios.obtenerTodosLosUsuarios()
        val pacientes = todosLosUsuarios.filter { it.obtenerNombreRol() == "Paciente" }
        val medicos = todosLosUsuarios.filter { it.obtenerNombreRol() == "Médico" }
        val administrativos = todosLosUsuarios.filter { it.obtenerNombreRol() == "Personal Administrativo" }
        
        val resumen = """
            📊 Resumen de Usuarios Registrados:
            
            👥 Total de usuarios: ${todosLosUsuarios.size}
            👤 Pacientes: ${pacientes.size}
            👨‍⚕️ Médicos: ${medicos.size}
            👩‍💼 Personal Administrativo: ${administrativos.size}
            
            📋 Usuarios activos: ${todosLosUsuarios.size}
            ✅ Documentos validados: ${pacientesRegistrados.size}
            
            💡 Funcionalidades disponibles:
            • Crear nuevo perfil de usuario
            • Actualizar información existente
            • Escanear y adjuntar documentos
            • Eliminar usuarios del sistema
            • Verificar documentos de identidad
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Resumen del Sistema")
            .setMessage(resumen)
            .setPositiveButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarDetallesEstadisticos() {
        val todosLosUsuarios = GestorUsuarios.obtenerTodosLosUsuarios()
        
        val detalles = todosLosUsuarios.joinToString("\n\n") { usuario ->
            val rolEmoji = when (usuario.obtenerNombreRol()) {
                "Paciente" -> "👤"
                "Médico" -> "👨‍⚕️"
                "Personal Administrativo" -> "👩‍💼"
                else -> "👤"
            }
            
            """
            $rolEmoji ${usuario.obtenerNombre()} (${usuario.obtenerNombreRol()})
            📧 ${usuario.obtenerEmail()}
            🆔 ${usuario.obtenerId()}
            ${if (usuario is Paciente) "🏥 ${usuario.obtenerSeguro()}" else ""}
            ${if (usuario is Medico) "🏥 ${usuario.obtenerEspecialidad()}" else ""}
            ${if (usuario is PersonalAdministrativo) "🏢 Personal Administrativo" else ""}
            """.trimIndent()
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📋 Detalles de Usuarios")
            .setMessage(detalles)
            .setPositiveButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun agregarUsuarioALista(usuario: PerfilPaciente) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_paciente_administrativo,
            linearLayoutPacientes,
            false
        )
        
        val textViewNombre = cardView.findViewById<TextView>(R.id.text_view_nombre)
        val textViewInfo = cardView.findViewById<TextView>(R.id.text_view_info)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado)
        val buttonEditar = cardView.findViewById<Button>(R.id.button_editar)
        val buttonDocumentos = cardView.findViewById<Button>(R.id.button_documentos)
        val buttonEliminar = cardView.findViewById<Button>(R.id.button_eliminar)
        
        textViewNombre.text = usuario.obtenerNombre()
        
        // Determinar el rol del usuario basado en el campo rol del PerfilPaciente
        val rolUsuario = when (usuario.rol.uppercase()) {
            "PACIENTE" -> "👤 Paciente"
            "MEDICO" -> "👨‍⚕️ Médico"
            "ADMINISTRATIVO" -> "👩‍💼 Administrativo"
            else -> "👤 Usuario"
        }
        
        textViewInfo.text = """
            $rolUsuario
            📧 ${usuario.email}
            📞 ${usuario.telefono}
            🏠 ${usuario.direccion}
            ${if (usuario.seguro != "No aplica") "🏥 ${usuario.seguro}" else ""}
            📄 ${usuario.documentoIdentidad}
        """.trimIndent()
        
        // Simular estado de validación
        val documentosValidados = usuario.id != "PACIENTE-3" // PACIENTE-3 tiene documentos pendientes
        if (documentosValidados) {
            textViewEstado.text = "✅ Documentos Validados"
            textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
        } else {
            textViewEstado.text = "⚠️ Documentos Pendientes"
            textViewEstado.setTextColor(resources.getColor(R.color.colorWarning, null))
        }
        
        buttonEditar.setOnClickListener {
            mostrarFormularioEditarPaciente(usuario)
        }
        
        buttonDocumentos.setOnClickListener {
            mostrarGestionDocumentos(usuario)
        }
        
        buttonEliminar.setOnClickListener {
            eliminarUsuario(usuario)
        }
        
        linearLayoutPacientes.addView(cardView)
    }
    
    private fun mostrarFormularioNuevoPaciente() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_formulario_paciente, null)
        
        val editTextNombre = dialogView.findViewById<EditText>(R.id.edit_text_nombre)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.edit_text_email)
        val editTextTelefono = dialogView.findViewById<EditText>(R.id.edit_text_telefono)
        val editTextDireccion = dialogView.findViewById<EditText>(R.id.edit_text_direccion)
        val editTextCiudad = dialogView.findViewById<EditText>(R.id.edit_text_ciudad)
        val editTextCodigoPostal = dialogView.findViewById<EditText>(R.id.edit_text_codigo_postal)
        val editTextFechaNacimiento = dialogView.findViewById<EditText>(R.id.edit_text_fecha_nacimiento)
        val spinnerGenero = dialogView.findViewById<Spinner>(R.id.spinner_genero)
        val spinnerTipoDocumento = dialogView.findViewById<Spinner>(R.id.spinner_tipo_documento)
        val editTextDocumento = dialogView.findViewById<EditText>(R.id.edit_text_documento)
        val editTextEmergenciaContacto = dialogView.findViewById<EditText>(R.id.edit_text_emergencia_contacto)
        val editTextEmergenciaTelefono = dialogView.findViewById<EditText>(R.id.edit_text_emergencia_telefono)
        val editTextSeguro = dialogView.findViewById<EditText>(R.id.edit_text_seguro)
        val editTextNumeroSeguro = dialogView.findViewById<EditText>(R.id.edit_text_numero_seguro)
        
        // Configurar spinners
        configurarSpinnerGenero(spinnerGenero)
        configurarSpinnerTipoDocumento(spinnerTipoDocumento)
        
        editTextFechaNacimiento.setOnClickListener {
            mostrarSelectorFecha(editTextFechaNacimiento)
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📝 Crear Nuevo Usuario")
            .setView(dialogView)
            .setPositiveButton("Crear Usuario") { _, _ ->
                val nombre = editTextNombre.text.toString()
                val email = editTextEmail.text.toString()
                val telefono = editTextTelefono.text.toString()
                val direccion = editTextDireccion.text.toString()
                val ciudad = editTextCiudad.text.toString()
                val codigoPostal = editTextCodigoPostal.text.toString()
                val fechaNacimiento = editTextFechaNacimiento.text.toString()
                val genero = spinnerGenero.selectedItem.toString()
                val tipoDocumento = spinnerTipoDocumento.selectedItem.toString()
                val documento = editTextDocumento.text.toString()
                val emergenciaContacto = editTextEmergenciaContacto.text.toString()
                val emergenciaTelefono = editTextEmergenciaTelefono.text.toString()
                val seguro = editTextSeguro.text.toString()
                val numeroSeguro = editTextNumeroSeguro.text.toString()
                
                if (validarCamposObligatoriosCompletos(nombre, email, telefono, direccion, ciudad, fechaNacimiento, genero, tipoDocumento, documento, emergenciaContacto, emergenciaTelefono)) {
                    crearNuevoUsuario(nombre, email, telefono, direccion, ciudad, codigoPostal, fechaNacimiento, genero, tipoDocumento, documento, emergenciaContacto, emergenciaTelefono, seguro, numeroSeguro)
                } else {
                    mostrarMensaje("Por favor completa todos los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarFormularioEditarPaciente(usuario: PerfilPaciente) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_formulario_paciente, null)
        
        val editTextNombre = dialogView.findViewById<EditText>(R.id.edit_text_nombre)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.edit_text_email)
        val editTextTelefono = dialogView.findViewById<EditText>(R.id.edit_text_telefono)
        val editTextDireccion = dialogView.findViewById<EditText>(R.id.edit_text_direccion)
        val editTextCiudad = dialogView.findViewById<EditText>(R.id.edit_text_ciudad)
        val editTextCodigoPostal = dialogView.findViewById<EditText>(R.id.edit_text_codigo_postal)
        val editTextFechaNacimiento = dialogView.findViewById<EditText>(R.id.edit_text_fecha_nacimiento)
        val spinnerGenero = dialogView.findViewById<Spinner>(R.id.spinner_genero)
        val spinnerTipoDocumento = dialogView.findViewById<Spinner>(R.id.spinner_tipo_documento)
        val editTextDocumento = dialogView.findViewById<EditText>(R.id.edit_text_documento)
        val editTextEmergenciaContacto = dialogView.findViewById<EditText>(R.id.edit_text_emergencia_contacto)
        val editTextEmergenciaTelefono = dialogView.findViewById<EditText>(R.id.edit_text_emergencia_telefono)
        val editTextSeguro = dialogView.findViewById<EditText>(R.id.edit_text_seguro)
        val editTextNumeroSeguro = dialogView.findViewById<EditText>(R.id.edit_text_numero_seguro)
        
        // Configurar spinners
        configurarSpinnerGenero(spinnerGenero)
        configurarSpinnerTipoDocumento(spinnerTipoDocumento)
        
        // Obtener información de contacto completa desde la base de datos
        val informacionContacto = dbHelper.obtenerInformacionContacto(usuario.email)
        
        // Llenar con datos existentes
        editTextNombre.setText(usuario.nombre)
        editTextEmail.setText(usuario.email)
        editTextTelefono.setText(usuario.telefono)
        
        // Separar dirección y ciudad
        val direccionCompleta = usuario.direccion
        val partesDireccion = direccionCompleta.split(", ")
        if (partesDireccion.size >= 2) {
            editTextDireccion.setText(partesDireccion[0])
            editTextCiudad.setText(partesDireccion[1])
        } else {
            editTextDireccion.setText(direccionCompleta)
        }
        
        // Llenar información de contacto si existe
        if (informacionContacto != null) {
            editTextCodigoPostal.setText(informacionContacto.codigoPostal)
            editTextFechaNacimiento.setText(informacionContacto.fechaNacimiento)
            
            // Seleccionar género en spinner
            val generoIndex = when (informacionContacto.genero) {
                "Masculino" -> 1
                "Femenino" -> 2
                "Otro" -> 3
                else -> 0
            }
            spinnerGenero.setSelection(generoIndex)
            
            // Seleccionar tipo de documento en spinner
            val tipoDocIndex = when (informacionContacto.tipoDocumento) {
                "CC" -> 1
                "CE" -> 2
                "TI" -> 3
                "PP" -> 4
                "NIT" -> 5
                else -> 0
            }
            spinnerTipoDocumento.setSelection(tipoDocIndex)
            
            editTextDocumento.setText(informacionContacto.documentoIdentidad)
            editTextEmergenciaContacto.setText(informacionContacto.emergenciaContacto)
            editTextEmergenciaTelefono.setText(informacionContacto.emergenciaTelefono)
        }
        
        editTextSeguro.setText(usuario.seguro)
        editTextNumeroSeguro.setText(usuario.numeroSeguro)
        
        editTextFechaNacimiento.setOnClickListener {
            mostrarSelectorFecha(editTextFechaNacimiento)
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("✏️ Actualizar Usuario")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { _, _ ->
                val nombre = editTextNombre.text.toString()
                val email = editTextEmail.text.toString()
                val telefono = editTextTelefono.text.toString()
                val direccion = editTextDireccion.text.toString()
                val ciudad = editTextCiudad.text.toString()
                val codigoPostal = editTextCodigoPostal.text.toString()
                val fechaNacimiento = editTextFechaNacimiento.text.toString()
                val genero = spinnerGenero.selectedItem.toString()
                val tipoDocumento = spinnerTipoDocumento.selectedItem.toString()
                val documento = editTextDocumento.text.toString()
                val emergenciaContacto = editTextEmergenciaContacto.text.toString()
                val emergenciaTelefono = editTextEmergenciaTelefono.text.toString()
                val seguro = editTextSeguro.text.toString()
                val numeroSeguro = editTextNumeroSeguro.text.toString()
                
                if (validarCamposObligatoriosCompletos(nombre, email, telefono, direccion, ciudad, fechaNacimiento, genero, tipoDocumento, documento, emergenciaContacto, emergenciaTelefono)) {
                    actualizarUsuario(usuario, nombre, email, telefono, direccion, ciudad, codigoPostal, fechaNacimiento, genero, tipoDocumento, documento, emergenciaContacto, emergenciaTelefono, seguro, numeroSeguro)
                } else {
                    mostrarMensaje("Por favor completa todos los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarSelectorFecha(editText: EditText) {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                editText.setText(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        datePickerDialog.show()
    }
    
    private fun validarCamposObligatorios(nombre: String, email: String, telefono: String, documento: String): Boolean {
        return nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && documento.isNotBlank()
    }
    
    private fun validarCamposObligatoriosCompletos(nombre: String, email: String, telefono: String, direccion: String, ciudad: String, fechaNacimiento: String, genero: String, tipoDocumento: String, documento: String, emergenciaContacto: String, emergenciaTelefono: String): Boolean {
        return nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank() && ciudad.isNotBlank() && fechaNacimiento.isNotBlank() && genero.isNotBlank() && tipoDocumento.isNotBlank() && documento.isNotBlank() && emergenciaContacto.isNotBlank() && emergenciaTelefono.isNotBlank()
    }
    
    private fun configurarSpinnerGenero(spinner: Spinner) {
        val generos = arrayOf("Seleccionar género", "Masculino", "Femenino", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
    
    private fun configurarSpinnerTipoDocumento(spinner: Spinner) {
        val tiposDocumento = arrayOf("Seleccionar tipo", "CC", "CE", "TI", "PP", "NIT")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDocumento)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
    
    private fun crearNuevoUsuario(
        nombre: String,
        email: String,
        telefono: String,
        direccion: String,
        ciudad: String,
        codigoPostal: String,
        fechaNacimiento: String,
        genero: String,
        tipoDocumento: String,
        documento: String,
        emergenciaContacto: String,
        emergenciaTelefono: String,
        seguro: String,
        numeroSeguro: String
    ) {
        try {
            println("=== DEBUG: Creando nuevo usuario ===")
            println("Nombre: $nombre")
            println("Email: $email")
            println("Rol: PACIENTE")
            
            // 1. Primero, guardar el usuario en la base de datos SQLite
            val contrasenaPorDefecto = "123456" // Contraseña por defecto
            val resultadoUsuario = dbHelper.insertarUsuario(
                email = email,
                contrasena = contrasenaPorDefecto,
                nombreCompleto = nombre,
                rol = "PACIENTE"
            )
            
            if (resultadoUsuario <= 0) {
                mostrarMensaje("❌ Error al crear usuario en la base de datos")
                println("Error al insertar usuario en BD: $resultadoUsuario")
                return
            }
            
            println("Usuario creado en BD con ID: $resultadoUsuario")
            
            // 2. Crear un nuevo usuario usando GestorUsuarios (para la lógica de negocio)
            val nuevoUsuario = Paciente()
            val usuarioId = resultadoUsuario.toString()
            
            // Establecer información básica
            nuevoUsuario.establecerInformacionBasica(usuarioId, nombre, email)
            
            // Establecer datos personales
            nuevoUsuario.establecerDatosPersonales(
                telefono = telefono,
                direccion = direccion,
                seguro = seguro,
                numeroSeguro = numeroSeguro,
                fechaNacimiento = fechaNacimiento,
                documentoIdentidad = documento
            )
            
            // Registrar el usuario en GestorUsuarios
            GestorUsuarios.registrarUsuario(nuevoUsuario)
            
            // 3. Crear información de contacto completa
            val informacionContacto = com.example.softmedv5.modelo.InformacionContacto(
                telefono = telefono,
                direccion = direccion,
                ciudad = ciudad,
                codigoPostal = codigoPostal,
                fechaNacimiento = fechaNacimiento,
                genero = genero,
                documentoIdentidad = documento,
                tipoDocumento = tipoDocumento,
                emergenciaContacto = emergenciaContacto,
                emergenciaTelefono = emergenciaTelefono
            )
            
            // 4. Guardar información de contacto en la base de datos
            val resultadoContacto = dbHelper.actualizarInformacionContacto(email, informacionContacto)
            
            if (!resultadoContacto) {
                // println("⚠️ Advertencia: No se pudo guardar la información de contacto")
            } else {
                // println("Información de contacto guardada exitosamente")
            }
            
            // 5. Guardar datos específicos del paciente (seguro, número de seguro)
            val usuarioIdInt = resultadoUsuario.toInt()
            val resultadoDatosPaciente = dbHelper.guardarDatosEspecificosPaciente(
                usuarioId = usuarioIdInt,
                seguro = seguro,
                numeroSeguro = numeroSeguro
            )
            
            if (!resultadoDatosPaciente) {
                // println("⚠️ Advertencia: No se pudieron guardar los datos específicos del paciente")
            } else {
                // println("Datos específicos del paciente guardados exitosamente")
            }
            
            // 6. Notificar al personal administrativo sobre el nuevo usuario
            val gestorNotificaciones = GestorNotificaciones.obtenerInstancia()
            gestorNotificaciones.notificarNuevoUsuario(nuevoUsuario)
            
            // 7. Crear PerfilPaciente para mostrar en la lista
            val perfilUsuario = PerfilPaciente(
                id = usuarioId,
                nombre = nombre,
                email = email,
                telefono = telefono,
                direccion = "$direccion, $ciudad",
                seguro = seguro,
                numeroSeguro = numeroSeguro,
                fechaNacimiento = fechaNacimiento,
                documentoIdentidad = "$tipoDocumento-$documento",
                rol = "PACIENTE"
            )
            
            // 8. Agregar a la lista local y recargar
            pacientesRegistrados.add(perfilUsuario)
            cargarUsuariosDesdeBaseDatos() // Recargar la lista
            
            println("=== Usuario creado exitosamente ===")
            mostrarMensaje("✅ Usuario creado exitosamente: ${perfilUsuario.obtenerNombre()}\nContraseña por defecto: $contrasenaPorDefecto")
            
        } catch (e: Exception) {
            println("Error al crear usuario: ${e.message}")
            e.printStackTrace()
            mostrarMensaje("❌ Error al crear usuario: ${e.message}")
        }
    }
    
    private fun actualizarUsuario(
        usuarioOriginal: PerfilPaciente,
        nombre: String,
        email: String,
        telefono: String,
        direccion: String,
        ciudad: String,
        codigoPostal: String,
        fechaNacimiento: String,
        genero: String,
        tipoDocumento: String,
        documento: String,
        emergenciaContacto: String,
        emergenciaTelefono: String,
        seguro: String,
        numeroSeguro: String
    ) {
        // Buscar el usuario en GestorUsuarios y actualizarlo
        val todosLosUsuarios = GestorUsuarios.obtenerTodosLosUsuarios()
        val usuarioEncontrado = todosLosUsuarios.find { it.obtenerId() == usuarioOriginal.id }
        
        if (usuarioEncontrado != null) {
            // Actualizar información básica
            usuarioEncontrado.establecerInformacionBasica(usuarioEncontrado.obtenerId(), nombre, email)
            
            // Actualizar datos personales según el tipo de usuario
            when (usuarioEncontrado) {
                is Paciente -> {
                    usuarioEncontrado.establecerDatosPersonales(
                        telefono = telefono,
                        direccion = direccion,
                        seguro = seguro,
                        numeroSeguro = numeroSeguro,
                        fechaNacimiento = fechaNacimiento,
                        documentoIdentidad = documento
                    )
                }
                is Medico -> {
                    usuarioEncontrado.establecerDatosPersonales(
                        telefono = telefono,
                        direccion = direccion,
                        fechaNacimiento = fechaNacimiento,
                        documentoIdentidad = documento
                    )
                }
                is PersonalAdministrativo -> {
                    usuarioEncontrado.establecerDatosPersonales(
                        telefono = telefono,
                        direccion = direccion,
                        fechaNacimiento = fechaNacimiento,
                        documentoIdentidad = documento
                    )
                }
            }
        }
        
        // Crear información de contacto completa
        val informacionContacto = com.example.softmedv5.modelo.InformacionContacto(
            telefono = telefono,
            direccion = direccion,
            ciudad = ciudad,
            codigoPostal = codigoPostal,
            fechaNacimiento = fechaNacimiento,
            genero = genero,
            documentoIdentidad = documento,
            tipoDocumento = tipoDocumento,
            emergenciaContacto = emergenciaContacto,
            emergenciaTelefono = emergenciaTelefono
        )
        
        // Actualizar información de contacto
        val resultadoContacto = dbHelper.actualizarInformacionContacto(email, informacionContacto)
        
        if (!resultadoContacto) {
            // println("⚠️ Advertencia: No se pudo actualizar la información de contacto")
        } else {
            // println("Información de contacto actualizada exitosamente")
        }
        
        // Actualizar datos específicos del paciente si es paciente
        if (usuarioOriginal.rol == "PACIENTE") {
            val usuarioIdInt = usuarioOriginal.id.toInt()
            val resultadoDatosPaciente = dbHelper.guardarDatosEspecificosPaciente(
                usuarioId = usuarioIdInt,
                seguro = seguro,
                numeroSeguro = numeroSeguro
            )
            
            if (!resultadoDatosPaciente) {
                // println("⚠️ Advertencia: No se pudieron actualizar los datos específicos del paciente")
            } else {
                // println("Datos específicos del paciente actualizados exitosamente")
            }
        }
        
        // Actualizar en la lista local
        val usuarioActualizado = usuarioOriginal.copy(
            nombre = nombre,
            email = email,
            telefono = telefono,
            direccion = "$direccion, $ciudad",
            seguro = seguro,
            numeroSeguro = numeroSeguro,
            fechaNacimiento = fechaNacimiento,
            documentoIdentidad = "$tipoDocumento-$documento"
        )
        
        val index = pacientesRegistrados.indexOf(usuarioOriginal)
        if (index != -1) {
            pacientesRegistrados[index] = usuarioActualizado
            cargarUsuariosDesdeBaseDatos() // Recargar la lista
            mostrarMensaje("✅ Usuario actualizado exitosamente: ${usuarioActualizado.obtenerNombre()}")
        }
    }
    
    private fun mostrarBusquedaPaciente() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_buscar_paciente, null)
        val editTextBuscar = dialogView.findViewById<EditText>(R.id.edit_text_buscar)
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🔍 Buscar Usuario")
            .setView(dialogView)
            .setPositiveButton("Buscar") { _, _ ->
                val terminoBusqueda = editTextBuscar.text.toString()
                if (terminoBusqueda.isNotBlank()) {
                    buscarUsuario(terminoBusqueda)
                } else {
                    mostrarMensaje("Por favor ingresa un término de búsqueda")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun buscarUsuario(termino: String) {
        val resultados = pacientesRegistrados.filter { usuario ->
            usuario.nombre.contains(termino, ignoreCase = true) ||
            usuario.email.contains(termino, ignoreCase = true) ||
            usuario.documentoIdentidad.contains(termino, ignoreCase = true)
        }
        
        if (resultados.isNotEmpty()) {
            mostrarResultadosBusqueda(resultados)
        } else {
            mostrarMensaje("No se encontraron usuarios con ese término")
        }
    }
    
    private fun mostrarResultadosBusqueda(usuarios: List<PerfilPaciente>) {
        val mensaje = usuarios.joinToString("\n\n") { usuario ->
            """
            👤 ${usuario.obtenerNombre()}
            📧 ${usuario.email}
            📞 ${usuario.telefono}
            📄 ${usuario.documentoIdentidad}
            """.trimIndent()
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🔍 Resultados de Búsqueda")
            .setMessage(mensaje)
            .setPositiveButton("Ver Detalles") { _, _ ->
                // Aquí se podría abrir una vista detallada del primer resultado
                mostrarMensaje("Funcionalidad de vista detallada en desarrollo")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarGestionDocumentos(usuario: PerfilPaciente) {
        val opciones = arrayOf(
            "📷 Escanear Carnet de Identidad",
            "📄 Adjuntar Referencias Médicas",
            "🏥 Escanear Carnet de Seguro",
            "📋 Ver Documentos Adjuntos"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Gestión de Documentos - ${usuario.obtenerNombre()}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> escanearDocumento("Carnet de Identidad", usuario)
                    1 -> adjuntarReferencias(usuario)
                    2 -> escanearDocumento("Carnet de Seguro", usuario)
                    3 -> verDocumentosAdjuntos(usuario)
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun escanearDocumento(tipoDocumento: String, usuario: PerfilPaciente) {
        mostrarMensaje("📷 Abriendo cámara para escanear $tipoDocumento de ${usuario.obtenerNombre()}")
        
        // En una aplicación real, aquí se abriría la cámara o galería
        android.app.AlertDialog.Builder(this)
            .setTitle("📷 Escanear $tipoDocumento")
            .setMessage("""
                Funcionalidad de escaneo:
                
                • Abrir cámara para capturar documento
                • Seleccionar imagen desde galería
                • Procesar y extraer información automáticamente
                • Validar formato y calidad del documento
                • Guardar en expediente del paciente
                
                Documento: $tipoDocumento
                Paciente: ${usuario.obtenerNombre()}
            """.trimIndent())
            .setPositiveButton("Simular Escaneo") { _, _ ->
                mostrarMensaje("✅ $tipoDocumento escaneado y guardado exitosamente")
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun adjuntarReferencias(usuario: PerfilPaciente) {
        mostrarMensaje("📄 Adjuntando referencias médicas para ${usuario.obtenerNombre()}")
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Adjuntar Referencias Médicas")
            .setMessage("""
                Funcionalidad de adjuntar referencias:
                
                • Seleccionar archivo PDF o imagen
                • Subir desde dispositivo o nube
                • Categorizar tipo de referencia
                • Asociar con especialidad médica
                • Notificar al médico correspondiente
                
                Paciente: ${usuario.obtenerNombre()}
            """.trimIndent())
            .setPositiveButton("Simular Adjuntar") { _, _ ->
                mostrarMensaje("✅ Referencias médicas adjuntadas exitosamente")
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun verDocumentosAdjuntos(usuario: PerfilPaciente) {
        val documentos = mutableListOf(
            "✅ Carnet de Identidad - CC-${usuario.documentoIdentidad.split("-").lastOrNull() ?: "XXXX"}"
        )
        
        if (usuario.id == "PACIENTE-3") {
            documentos.add("⚠️ Referencias Médicas - Pendiente de validación")
        } else {
            documentos.add("✅ Referencias Médicas - Validadas")
        }
        
        documentos.add("✅ Carnet de Seguro - ${usuario.seguro}")
        
        val mensaje = documentos.joinToString("\n")
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📋 Documentos de ${usuario.obtenerNombre()}")
            .setMessage(mensaje)
            .setPositiveButton("Ver Detalles") { _, _ ->
                mostrarMensaje("Funcionalidad de vista detallada de documentos en desarrollo")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun eliminarUsuario(usuario: PerfilPaciente) {
        android.app.AlertDialog.Builder(this)
            .setTitle("🗑️ Eliminar Usuario")
            .setMessage("""
                ¿Estás seguro de que deseas eliminar al usuario?
                
                👤 ${usuario.obtenerNombre()}
                📧 ${usuario.email}
                📄 ${usuario.documentoIdentidad}
                
                ⚠️ Esta acción no se puede deshacer.
            """.trimIndent())
            .setPositiveButton("Eliminar") { _, _ ->
                // Debug: Mostrar usuarios antes de eliminar
                val usuariosAntes = dbHelper.obtenerTodosLosUsuariosDebug()
                println("DEBUG: Usuarios antes de eliminar: ${usuariosAntes.size}")
                
                // Eliminar de la base de datos
                val eliminado = dbHelper.eliminarUsuarioPorEmail(usuario.email)
                
                if (eliminado) {
                    // Debug: Mostrar usuarios después de eliminar
                    val usuariosDespues = dbHelper.obtenerTodosLosUsuariosDebug()
                    println("DEBUG: Usuarios después de eliminar: ${usuariosDespues.size}")
                    
                    // Eliminar de la lista local
                    pacientesRegistrados.remove(usuario)
                    
                    // Recargar la lista desde la base de datos
                    cargarUsuariosDesdeBaseDatos()
                    mostrarMensaje("✅ Usuario eliminado exitosamente: ${usuario.obtenerNombre()}")
                } else {
                    mostrarMensaje("❌ Error al eliminar el usuario. Inténtalo de nuevo.")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarSinUsuarios() {
        textViewSinPacientes.visibility = View.VISIBLE
        linearLayoutPacientes.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarUsuariosDesdeBaseDatos() // Recargar cuando se regrese a esta actividad
    }
    
    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
    
    // Métodos auxiliares para obtener información de contacto desde la base de datos
    private fun obtenerTelefonoDesdeContacto(usuarioId: Int): String {
        return try {
            val contacto = dbHelper.obtenerInformacionContacto(usuarioId)
            contacto?.get("telefono") as? String ?: "No disponible"
        } catch (e: Exception) {
            "No disponible"
        }
    }
    
    private fun obtenerDireccionDesdeContacto(usuarioId: Int): String {
        return try {
            val contacto = dbHelper.obtenerInformacionContacto(usuarioId)
            val direccion = contacto?.get("direccion") as? String ?: ""
            val ciudad = contacto?.get("ciudad") as? String ?: ""
            if (direccion.isNotBlank() && ciudad.isNotBlank()) {
                "$direccion, $ciudad"
            } else if (direccion.isNotBlank()) {
                direccion
            } else {
                "No disponible"
            }
        } catch (e: Exception) {
            "No disponible"
        }
    }
    
    private fun obtenerFechaNacimientoDesdeContacto(usuarioId: Int): String {
        return try {
            val contacto = dbHelper.obtenerInformacionContacto(usuarioId)
            contacto?.get("fecha_nacimiento") as? String ?: "No disponible"
        } catch (e: Exception) {
            "No disponible"
        }
    }
    
    private fun obtenerDocumentoDesdeContacto(usuarioId: Int): String {
        return try {
            val contacto = dbHelper.obtenerInformacionContacto(usuarioId)
            val tipoDoc = contacto?.get("tipo_documento") as? String ?: ""
            val numDoc = contacto?.get("documento_identidad") as? String ?: ""
            if (tipoDoc.isNotBlank() && numDoc.isNotBlank()) {
                "$tipoDoc-$numDoc"
            } else if (numDoc.isNotBlank()) {
                numDoc
            } else {
                "No disponible"
            }
        } catch (e: Exception) {
            "No disponible"
        }
    }
} 