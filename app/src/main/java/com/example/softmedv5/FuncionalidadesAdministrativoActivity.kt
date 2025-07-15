package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.util.GestorAutenticacion
import com.example.softmedv5.util.GestorNotificaciones

class FuncionalidadesAdministrativoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewBienvenida: TextView
    private lateinit var buttonGestionCitas: Button
    private lateinit var buttonRegistroPacientes: Button
    private lateinit var buttonCrearCuentaPaciente: Button
    private lateinit var buttonFacturacion: Button
    private lateinit var buttonDocumentosClinicos: Button
    private lateinit var buttonComunicacionInterna: Button
    private lateinit var buttonReportesControl: Button
    private lateinit var buttonGestionNotificaciones: Button
    private lateinit var buttonVolver: Button
    
    private val gestorNotificaciones = GestorNotificaciones.obtenerInstancia()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionalidades_administrativo)
        
        inicializarVistas()
        configurarEventos()
        mostrarInformacionAdministrativo()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewBienvenida = findViewById(R.id.text_view_bienvenida)
        buttonGestionCitas = findViewById(R.id.button_gestion_citas)
        buttonRegistroPacientes = findViewById(R.id.button_registro_pacientes)
        buttonCrearCuentaPaciente = findViewById(R.id.button_crear_cuenta_paciente)
        buttonFacturacion = findViewById(R.id.button_facturacion)
        buttonDocumentosClinicos = findViewById(R.id.button_documentos_clinicos)
        buttonComunicacionInterna = findViewById(R.id.button_comunicacion_interna)
        buttonReportesControl = findViewById(R.id.button_reportes_control)
        buttonGestionNotificaciones = findViewById(R.id.button_gestion_notificaciones)
        buttonVolver = findViewById(R.id.button_volver)
    }
    
    private fun configurarEventos() {
        buttonGestionCitas.setOnClickListener {
            abrirGestionCitas()
        }
        
        buttonRegistroPacientes.setOnClickListener {
            abrirRegistroPacientes()
        }
        
        buttonCrearCuentaPaciente.setOnClickListener {
            crearCuentaPaciente()
        }
        
        buttonFacturacion.setOnClickListener {
            abrirFacturacion()
        }
        
        buttonDocumentosClinicos.setOnClickListener {
            abrirDocumentosClinicos()
        }
        
        buttonComunicacionInterna.setOnClickListener {
            abrirComunicacionInterna()
        }
        
        buttonReportesControl.setOnClickListener {
            abrirReportes()
        }
        
        buttonGestionNotificaciones.setOnClickListener {
            abrirGestionNotificaciones()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun mostrarInformacionAdministrativo() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val administrativo = sesion.usuario
            
            // Obtener número de notificaciones pendientes
            val notificacionesPendientes = gestorNotificaciones.obtenerCantidadNotificacionesPendientes()
            
            textViewTitulo.text = "📋 Funcionalidades del Personal Administrativo"
            
            val mensajeBienvenida = if (notificacionesPendientes > 0) {
                "Bienvenido ${administrativo.obtenerNombre()}\n\n🔔 Tienes $notificacionesPendientes notificación(es) pendiente(s)\n\nSelecciona la funcionalidad administrativa que deseas utilizar:"
            } else {
                "Bienvenido ${administrativo.obtenerNombre()}\n\nSelecciona la funcionalidad administrativa que deseas utilizar:"
            }
            
            textViewBienvenida.text = mensajeBienvenida
            
            // Actualizar el texto del botón de notificaciones
            buttonGestionNotificaciones.text = if (notificacionesPendientes > 0) {
                "🔔 Gestión de Notificaciones ($notificacionesPendientes)"
            } else {
                "🔔 Gestión de Notificaciones"
            }
        } else {
            mostrarMensaje("Error: No hay sesión activa")
            finish()
        }
    }
    
    private fun abrirGestionCitas() {
        val intent = Intent(this, GestionCitasAdministrativoActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirRegistroPacientes() {
        val intent = Intent(this, RegistroPacientesActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirFacturacion() {
        val intent = Intent(this, FacturacionPagosActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirDocumentosClinicos() {
        val intent = Intent(this, GestionDocumentosActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirComunicacionInterna() {
        val intent = Intent(this, ChatUnificadoActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirReportes() {
        val intent = Intent(this, ReportesControlActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirGestionNotificaciones() {
        val intent = Intent(this, GestionNotificacionesActivity::class.java)
        startActivity(intent)
    }
    
    private fun crearCuentaPaciente() {
        val mensaje = """
            🆕 Crear Nueva Cuenta de Paciente
            
            Esta funcionalidad permite al personal administrativo crear nuevas cuentas de pacientes en el sistema.
            
            📋 Proceso:
            1. Ingresar datos personales básicos
            2. Configurar información de contacto
            3. Establecer datos médicos (seguro, etc.)
            4. Crear credenciales de acceso
            5. Notificar al paciente
            
            💡 Importante:
            • Los datos del paciente son cruciales para solicitar citas
            • Se debe verificar la información antes de crear la cuenta
            • El paciente recibirá instrucciones para completar su perfil
            
            ¿Deseas proceder con la creación de una nueva cuenta de paciente?
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🆕 Crear Cuenta de Paciente")
            .setMessage(mensaje)
            .setPositiveButton("Crear Cuenta") { _, _ ->
                mostrarFormularioCrearPaciente()
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarFormularioCrearPaciente() {
        val mensaje = """
            📝 Formulario de Creación de Paciente
            
            Para crear una nueva cuenta de paciente, necesitarás:
            
            📋 Datos Personales:
            • Nombre completo
            • Fecha de nacimiento
            • Documento de identidad
            • Género
            
            📞 Información de Contacto:
            • Teléfono
            • Email
            • Dirección
            • Ciudad
            
            🏥 Información Médica:
            • Tipo de seguro
            • Nombre del seguro
            • Número de afiliación
            
            🔐 Credenciales:
            • Email para acceso
            • Contraseña temporal
            
            ⚠️ Nota: El paciente deberá completar su perfil al primer acceso.
            
            ¿Continuar con el formulario de creación?
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📝 Formulario de Creación")
            .setMessage(mensaje)
            .setPositiveButton("Continuar") { _, _ ->
                // Aquí se abriría el formulario completo
                mostrarMensaje("Funcionalidad de formulario completo en desarrollo")
                mostrarMensaje("Por ahora, usa 'Registro y Actualización de Pacientes'")
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarDialogoInformacion(titulo: String, mensaje: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Entendido") { _, _ -> }
            .setNegativeButton("Más información") { _, _ ->
                mostrarMensaje("Para más información contacta al soporte técnico")
            }
            .show()
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        mostrarInformacionAdministrativo() // Actualizar contador de notificaciones
    }
} 