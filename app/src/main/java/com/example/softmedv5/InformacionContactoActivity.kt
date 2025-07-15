package com.example.softmedv5

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.database.SimpleDatabaseHelper
import com.example.softmedv5.modelo.InformacionContacto
import com.example.softmedv5.modelo.Paciente
import com.example.softmedv5.util.GestorAutenticacion
import java.util.*

class InformacionContactoActivity : AppCompatActivity() {
    
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextDireccion: EditText
    private lateinit var editTextCiudad: EditText
    private lateinit var editTextCodigoPostal: EditText
    private lateinit var editTextFechaNacimiento: EditText
    private lateinit var spinnerGenero: Spinner
    private lateinit var editTextDocumentoIdentidad: EditText
    private lateinit var spinnerTipoDocumento: Spinner
    private lateinit var editTextEmergenciaContacto: EditText
    private lateinit var editTextEmergenciaTelefono: EditText
    
    // Campos de información personal
    private lateinit var editTextNombre: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    
    // Campos específicos para pacientes
    private lateinit var editTextSeguro: EditText
    private lateinit var editTextNumeroSeguro: EditText
    private lateinit var spinnerTipoSeguro: Spinner
    private lateinit var linearLayoutCamposPaciente: LinearLayout
    
    private lateinit var textViewInformacionActual: TextView
    private lateinit var textViewEstadoInformacion: TextView
    private lateinit var textViewProgresoCompletitud: TextView
    private lateinit var buttonGuardar: Button
    private lateinit var buttonCancelar: Button
    private lateinit var buttonEditar: Button
    
    private var modoEdicion = false
    private var informacionOriginal: InformacionContacto? = null
    private var esPaciente = false
    private var paciente: Paciente? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_contacto)
        
        inicializarVistas()
        configurarSpinners()
        configurarEventos()
        verificarTipoUsuario()
        cargarInformacionUsuario()
    }
    
    private fun inicializarVistas() {
        editTextTelefono = findViewById(R.id.edit_text_telefono)
        editTextDireccion = findViewById(R.id.edit_text_direccion)
        editTextCiudad = findViewById(R.id.edit_text_ciudad)
        editTextCodigoPostal = findViewById(R.id.edit_text_codigo_postal)
        editTextFechaNacimiento = findViewById(R.id.edit_text_fecha_nacimiento)
        spinnerGenero = findViewById(R.id.spinner_genero)
        editTextDocumentoIdentidad = findViewById(R.id.edit_text_documento_identidad)
        spinnerTipoDocumento = findViewById(R.id.spinner_tipo_documento)
        editTextEmergenciaContacto = findViewById(R.id.edit_text_emergencia_contacto)
        editTextEmergenciaTelefono = findViewById(R.id.edit_text_emergencia_telefono)
        
        // Campos de información personal
        editTextNombre = findViewById(R.id.edit_text_nombre)
        editTextEmail = findViewById(R.id.edit_text_email)
        editTextPassword = findViewById(R.id.edit_text_password)
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password)
        
        // Campos específicos para pacientes
        editTextSeguro = findViewById(R.id.edit_text_seguro)
        editTextNumeroSeguro = findViewById(R.id.edit_text_numero_seguro)
        spinnerTipoSeguro = findViewById(R.id.spinner_tipo_seguro)
        linearLayoutCamposPaciente = findViewById(R.id.linear_layout_campos_paciente)
        
        textViewInformacionActual = findViewById(R.id.text_view_informacion_actual)
        textViewEstadoInformacion = findViewById(R.id.text_view_estado_informacion)
        textViewProgresoCompletitud = findViewById(R.id.text_view_progreso_completitud)
        buttonGuardar = findViewById(R.id.button_guardar)
        buttonCancelar = findViewById(R.id.button_cancelar)
        buttonEditar = findViewById(R.id.button_editar)
    }
    
    private fun verificarTipoUsuario() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null && sesion.usuario is Paciente) {
            esPaciente = true
            paciente = sesion.usuario as Paciente
            linearLayoutCamposPaciente.visibility = View.VISIBLE
        } else {
            esPaciente = false
            linearLayoutCamposPaciente.visibility = View.GONE
        }
    }
    
    private fun configurarSpinners() {
        // Configurar spinner de género
        val generos = arrayOf("Seleccione género", "Masculino", "Femenino", "No binario", "Prefiero no decir")
        val adaptadorGenero = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adaptadorGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adaptadorGenero
        
        // Configurar spinner de tipo de documento
        val tiposDocumento = arrayOf("Seleccione tipo", "DNI", "Pasaporte", "Cédula", "Otro")
        val adaptadorDocumento = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDocumento)
        adaptadorDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDocumento.adapter = adaptadorDocumento
        
        // Configurar spinner de tipo de seguro (solo para pacientes)
        val tiposSeguro = arrayOf("Seleccione tipo", "EPS", "IPS", "Prepagada", "Particular", "Otro")
        val adaptadorSeguro = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposSeguro)
        adaptadorSeguro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoSeguro.adapter = adaptadorSeguro
    }
    
    private fun configurarEventos() {
        buttonEditar.setOnClickListener {
            activarModoEdicion()
        }
        
        buttonGuardar.setOnClickListener {
            guardarInformacion()
        }
        
        buttonCancelar.setOnClickListener {
            cancelarEdicion()
        }
        
        // Configurar selector de fecha
        editTextFechaNacimiento.setOnClickListener {
            mostrarSelectorFecha()
        }
    }
    
    private fun mostrarSelectorFecha() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                editTextFechaNacimiento.setText(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    
    private fun cargarInformacionUsuario() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val usuario = sesion.usuario
            val informacionContacto = usuario.obtenerInformacionContacto()
            
            // Guardar información original
            informacionOriginal = informacionContacto.copy()
            
            // Mostrar información actual
            mostrarInformacionActual(informacionContacto)
            
            // Cargar datos en los campos
            cargarDatosEnCampos(informacionContacto)
            
            // Configurar modo inicial (solo lectura)
            configurarModoSoloLectura()
        } else {
            mostrarMensaje("Error: No hay sesión activa")
            finish()
        }
    }
    
    private fun mostrarInformacionActual(informacion: InformacionContacto) {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val usuario = sesion.usuario
            
            if (esPaciente && paciente != null) {
                // Para pacientes, mostrar información específica
                val porcentajeCompletitud = paciente!!.obtenerPorcentajeCompletitud()
                val camposFaltantes = paciente!!.obtenerCamposFaltantes()
                
                textViewProgresoCompletitud.text = "📊 Completitud del perfil: $porcentajeCompletitud%"
                
                if (paciente!!.tieneDatosPersonalesCompletos()) {
                    textViewInformacionActual.text = """
                        ✅ Perfil Completo
                        
                        👤 ${usuario.obtenerNombre()}
                        📧 ${usuario.obtenerEmail()}
                        📞 ${informacion.telefono}
                        🏠 ${informacion.direccion}
                        🏥 ${paciente!!.obtenerSeguro()}
                        🔢 ${paciente!!.obtenerNumeroSeguro()}
                        📅 ${informacion.fechaNacimiento}
                        📄 ${informacion.documentoIdentidad}
                        
                        💡 Ahora puedes acceder a todas las funcionalidades del paciente.
                    """.trimIndent()
                    textViewEstadoInformacion.text = "✅ Perfil Completo"
                    textViewEstadoInformacion.setTextColor(resources.getColor(R.color.colorSuccess, null))
                } else {
                    textViewInformacionActual.text = """
                        ⚠️ Perfil Incompleto
                        
                        👤 ${usuario.obtenerNombre()}
                        📧 ${usuario.obtenerEmail()}
                        📞 ${informacion.telefono}
                        🏠 ${informacion.direccion}
                        
                        📋 Campos faltantes:
                        ${camposFaltantes.joinToString("\n") { "• $it" }}
                        
                        💡 Completa tu información para acceder a todas las funcionalidades.
                    """.trimIndent()
                    textViewEstadoInformacion.text = "⚠️ Incompleto"
                    textViewEstadoInformacion.setTextColor(resources.getColor(R.color.colorWarning, null))
                }
            } else {
                // Para otros tipos de usuario
                textViewProgresoCompletitud.text = "📊 Información de Contacto"
                
                if (usuario.tieneInformacionContactoCompleta()) {
                    textViewInformacionActual.text = """
                        ✅ Información Completa
                        
                        👤 ${usuario.obtenerNombre()}
                        📧 ${usuario.obtenerEmail()}
                        📞 ${informacion.telefono}
                        🏠 ${informacion.direccion}
                        🏙️ ${informacion.ciudad}
                        📅 ${informacion.fechaNacimiento}
                        📄 ${informacion.documentoIdentidad}
                        
                        💡 Tu información de contacto está completa.
                    """.trimIndent()
                    textViewEstadoInformacion.text = "✅ Completo"
                    textViewEstadoInformacion.setTextColor(resources.getColor(R.color.colorSuccess, null))
                } else {
                    textViewInformacionActual.text = """
                        ⚠️ Información Incompleta
                        
                        👤 ${usuario.obtenerNombre()}
                        📧 ${usuario.obtenerEmail()}
                        📞 ${informacion.telefono}
                        🏠 ${informacion.direccion}
                        
                        💡 Completa tu información de contacto.
                    """.trimIndent()
                    textViewEstadoInformacion.text = "⚠️ Incompleto"
                    textViewEstadoInformacion.setTextColor(resources.getColor(R.color.colorWarning, null))
                }
            }
        }
    }
    
    private fun cargarDatosEnCampos(informacion: InformacionContacto) {
        // Cargar información personal del usuario
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val usuario = sesion.usuario
            editTextNombre.setText(usuario.obtenerNombre())
            editTextEmail.setText(usuario.obtenerEmail())
        }
        
        // Cargar información de contacto
        editTextTelefono.setText(informacion.telefono)
        editTextDireccion.setText(informacion.direccion)
        editTextCiudad.setText(informacion.ciudad)
        editTextCodigoPostal.setText(informacion.codigoPostal)
        editTextFechaNacimiento.setText(informacion.fechaNacimiento)
        editTextDocumentoIdentidad.setText(informacion.documentoIdentidad)
        editTextEmergenciaContacto.setText(informacion.emergenciaContacto)
        editTextEmergenciaTelefono.setText(informacion.emergenciaTelefono)
        
        // Cargar datos específicos del paciente si es paciente
        if (esPaciente && paciente != null) {
            editTextSeguro.setText(paciente!!.obtenerSeguro())
            editTextNumeroSeguro.setText(paciente!!.obtenerNumeroSeguro())
        }
        
        // Configurar spinners
        configurarSpinnerSeleccion(spinnerGenero, informacion.genero)
        configurarSpinnerSeleccion(spinnerTipoDocumento, informacion.tipoDocumento)
        if (esPaciente) {
            configurarSpinnerSeleccion(spinnerTipoSeguro, "EPS") // Valor por defecto
        }
    }
    
    private fun configurarSpinnerSeleccion(spinner: Spinner, valor: String) {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == valor) {
                spinner.setSelection(i)
                break
            }
        }
    }
    
    private fun configurarModoSoloLectura() {
        modoEdicion = false
        
        // Deshabilitar campos
        habilitarCampos(false)
        
        // Mostrar/ocultar botones
        buttonEditar.visibility = View.VISIBLE
        buttonGuardar.visibility = View.GONE
        buttonCancelar.visibility = View.GONE
    }
    
    private fun activarModoEdicion() {
        modoEdicion = true
        
        // Habilitar campos
        habilitarCampos(true)
        
        // Mostrar/ocultar botones
        buttonEditar.visibility = View.GONE
        buttonGuardar.visibility = View.VISIBLE
        buttonCancelar.visibility = View.VISIBLE
    }
    
    private fun habilitarCampos(habilitado: Boolean) {
        // Campos de información personal
        editTextNombre.isEnabled = habilitado
        editTextEmail.isEnabled = habilitado
        editTextPassword.isEnabled = habilitado
        editTextConfirmPassword.isEnabled = habilitado
        
        // Campos de información de contacto
        editTextTelefono.isEnabled = habilitado
        editTextDireccion.isEnabled = habilitado
        editTextCiudad.isEnabled = habilitado
        editTextCodigoPostal.isEnabled = habilitado
        editTextFechaNacimiento.isEnabled = habilitado
        spinnerGenero.isEnabled = habilitado
        editTextDocumentoIdentidad.isEnabled = habilitado
        spinnerTipoDocumento.isEnabled = habilitado
        editTextEmergenciaContacto.isEnabled = habilitado
        editTextEmergenciaTelefono.isEnabled = habilitado
        
        // Campos específicos para pacientes
        if (esPaciente) {
            editTextSeguro.isEnabled = habilitado
            editTextNumeroSeguro.isEnabled = habilitado
            spinnerTipoSeguro.isEnabled = habilitado
        }
    }
    
    private fun guardarInformacion() {
        val nuevaInformacion = obtenerInformacionDeCampos()
        
        if (validarInformacion(nuevaInformacion)) {
            val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
            val sesion = gestorAutenticacion.obtenerSesionActual()
            
            if (sesion != null) {
                val usuario = sesion.usuario
                
                // Actualizar información personal del usuario
                val nombreNuevo = editTextNombre.text.toString().trim()
                val emailNuevo = editTextEmail.text.toString().trim()
                val passwordNuevo = editTextPassword.text.toString().trim()
                val confirmPassword = editTextConfirmPassword.text.toString().trim()
                
                // Validar contraseña si se está cambiando
                if (passwordNuevo.isNotEmpty()) {
                    if (passwordNuevo != confirmPassword) {
                        mostrarMensaje("Las contraseñas no coinciden")
                        return
                    }
                    if (passwordNuevo.length < 6) {
                        mostrarMensaje("La contraseña debe tener al menos 6 caracteres")
                        return
                    }
                }
                
                var cambiosRealizados = false
                
                // Actualizar información básica en la base de datos
                if (nombreNuevo != usuario.obtenerNombre()) {
                    val exitoNombre = gestorAutenticacion.actualizarInformacionBasica(nombreNuevo)
                    if (exitoNombre) {
                        cambiosRealizados = true
                        mostrarMensaje("Nombre actualizado exitosamente")
                    } else {
                        mostrarMensaje("Error al actualizar el nombre")
                        return
                    }
                }
                
                // Actualizar contraseña si se proporcionó una nueva
                if (passwordNuevo.isNotEmpty()) {
                    val exitoContrasena = gestorAutenticacion.actualizarContrasena(passwordNuevo)
                    if (exitoContrasena) {
                        cambiosRealizados = true
                        mostrarMensaje("Contraseña actualizada exitosamente")
                    } else {
                        mostrarMensaje("Error al actualizar la contraseña")
                        return
                    }
                }
                
                // Actualizar información de contacto en la base de datos
                val exitoContacto = gestorAutenticacion.actualizarInformacionContacto(nuevaInformacion)
                if (exitoContacto) {
                    cambiosRealizados = true
                } else {
                    mostrarMensaje("Error al actualizar la información de contacto")
                    return
                }
                
                // Si es paciente, actualizar también los datos específicos
                if (esPaciente && paciente != null) {
                    actualizarDatosPaciente()
                }
                
                if (cambiosRealizados) {
                    mostrarMensaje("Información actualizada exitosamente")
                    mostrarInformacionActual(nuevaInformacion)
                    configurarModoSoloLectura()
                    
                    // Limpiar campos de contraseña
                    editTextPassword.text.clear()
                    editTextConfirmPassword.text.clear()
                    
                    // Notificar que los cambios se han guardado
                    setResult(RESULT_OK)
                } else {
                    mostrarMensaje("No se realizaron cambios")
                }
            }
        }
    }
    
    private fun actualizarDatosPaciente() {
        val seguro = editTextSeguro.text.toString().trim()
        val numeroSeguro = editTextNumeroSeguro.text.toString().trim()
        val fechaNacimiento = editTextFechaNacimiento.text.toString().trim()
        val documentoIdentidad = editTextDocumentoIdentidad.text.toString().trim()
        val direccion = editTextDireccion.text.toString().trim()
        val telefono = editTextTelefono.text.toString().trim()
        
        paciente?.establecerDatosPersonales(
            telefono = telefono,
            direccion = direccion,
            seguro = seguro,
            numeroSeguro = numeroSeguro,
            fechaNacimiento = fechaNacimiento,
            documentoIdentidad = documentoIdentidad
        )
        
        // Guardar datos específicos del paciente en la base de datos
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val usuarioId = sesion.usuario.obtenerId().toInt()
            val dbHelper = SimpleDatabaseHelper(this)
            val resultado = dbHelper.guardarDatosEspecificosPaciente(
                usuarioId = usuarioId,
                seguro = seguro,
                numeroSeguro = numeroSeguro
            )
            
            if (resultado) {
                // println("✅ Datos específicos del paciente guardados en la base de datos")
            } else {
                // println("❌ Error al guardar datos específicos del paciente en la base de datos")
            }
        }
    }
    
    private fun cancelarEdicion() {
        // Restaurar información original
        if (informacionOriginal != null) {
            cargarDatosEnCampos(informacionOriginal!!)
        }
        
        // Limpiar campos de contraseña
        editTextPassword.text.clear()
        editTextConfirmPassword.text.clear()
        
        configurarModoSoloLectura()
    }
    
    private fun obtenerInformacionDeCampos(): InformacionContacto {
        return InformacionContacto(
            telefono = editTextTelefono.text.toString().trim(),
            direccion = editTextDireccion.text.toString().trim(),
            ciudad = editTextCiudad.text.toString().trim(),
            codigoPostal = editTextCodigoPostal.text.toString().trim(),
            fechaNacimiento = editTextFechaNacimiento.text.toString().trim(),
            genero = spinnerGenero.selectedItem.toString(),
            documentoIdentidad = editTextDocumentoIdentidad.text.toString().trim(),
            tipoDocumento = spinnerTipoDocumento.selectedItem.toString(),
            emergenciaContacto = editTextEmergenciaContacto.text.toString().trim(),
            emergenciaTelefono = editTextEmergenciaTelefono.text.toString().trim()
        )
    }
    
    private fun validarInformacion(informacion: InformacionContacto): Boolean {
        // Validar información personal
        val nombre = editTextNombre.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()
        
        if (nombre.isEmpty()) {
            mostrarMensaje("El nombre es obligatorio")
            return false
        }
        
        if (email.isEmpty()) {
            mostrarMensaje("El correo electrónico es obligatorio")
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarMensaje("El formato del correo electrónico no es válido")
            return false
        }
        
        // Validar contraseña si se está cambiando
        if (password.isNotEmpty()) {
            if (password != confirmPassword) {
                mostrarMensaje("Las contraseñas no coinciden")
                return false
            }
            if (password.length < 6) {
                mostrarMensaje("La contraseña debe tener al menos 6 caracteres")
                return false
            }
        }
        
        // Validar información de contacto
        if (informacion.telefono.isEmpty()) {
            mostrarMensaje("El teléfono es obligatorio")
            return false
        }
        
        if (informacion.direccion.isEmpty()) {
            mostrarMensaje("La dirección es obligatoria")
            return false
        }
        
        if (informacion.ciudad.isEmpty()) {
            mostrarMensaje("La ciudad es obligatoria")
            return false
        }
        
        if (informacion.fechaNacimiento.isEmpty()) {
            mostrarMensaje("La fecha de nacimiento es obligatoria")
            return false
        }
        
        if (informacion.documentoIdentidad.isEmpty()) {
            mostrarMensaje("El documento de identidad es obligatorio")
            return false
        }
        
        if (informacion.emergenciaContacto.isEmpty()) {
            mostrarMensaje("El contacto de emergencia es obligatorio")
            return false
        }
        
        if (informacion.emergenciaTelefono.isEmpty()) {
            mostrarMensaje("El teléfono de emergencia es obligatorio")
            return false
        }
        
        // Validar información específica del paciente
        if (esPaciente) {
            val seguro = editTextSeguro.text.toString().trim()
            val numeroSeguro = editTextNumeroSeguro.text.toString().trim()
            
            if (seguro.isEmpty()) {
                mostrarMensaje("El seguro médico es obligatorio para pacientes")
                return false
            }
            
            if (numeroSeguro.isEmpty()) {
                mostrarMensaje("El número de seguro es obligatorio para pacientes")
                return false
            }
        }
        
        return true
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
    
    override fun onPause() {
        super.onPause()
        
        // Si estamos en modo edición, guardar automáticamente los cambios
        if (modoEdicion) {
            guardarCambiosAutomaticamente()
        }
    }
    
    private fun guardarCambiosAutomaticamente() {
        val nuevaInformacion = obtenerInformacionDeCampos()
        
        // Solo guardar si hay cambios válidos
        if (validarInformacion(nuevaInformacion)) {
            val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
            val sesion = gestorAutenticacion.obtenerSesionActual()
            
            if (sesion != null) {
                val usuario = sesion.usuario
                val nombreNuevo = editTextNombre.text.toString().trim()
                val passwordNuevo = editTextPassword.text.toString().trim()
                
                var cambiosGuardados = false
                
                // Guardar cambios de nombre si es diferente
                if (nombreNuevo != usuario.obtenerNombre()) {
                    gestorAutenticacion.actualizarInformacionBasica(nombreNuevo)
                    cambiosGuardados = true
                }
                
                // Guardar cambios de contraseña si se proporcionó una nueva
                if (passwordNuevo.isNotEmpty()) {
                    gestorAutenticacion.actualizarContrasena(passwordNuevo)
                    cambiosGuardados = true
                }
                
                // Guardar cambios de información de contacto
                gestorAutenticacion.actualizarInformacionContacto(nuevaInformacion)
                cambiosGuardados = true
                
                if (cambiosGuardados) {
                    // Notificar que se guardaron cambios automáticamente
                    setResult(RESULT_OK)
                }
            }
        }
    }
} 