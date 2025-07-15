package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.Paciente
import com.example.softmedv5.util.GestorAutenticacion

class FuncionalidadesPacienteActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewBienvenida: TextView
    private lateinit var buttonAlertasCitas: Button
    private lateinit var buttonSolicitarCita: Button
    private lateinit var buttonConsultarHistorial: Button
    private lateinit var buttonDescargarRecetas: Button
    private lateinit var buttonTeleconsultas: Button
    private lateinit var buttonVolver: Button
    
    private var paciente: Paciente? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionalidades_paciente)
        
        inicializarVistas()
        configurarEventos()
        validarDatosPaciente()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewBienvenida = findViewById(R.id.text_view_bienvenida)
        buttonAlertasCitas = findViewById(R.id.button_alertas_citas)
        buttonSolicitarCita = findViewById(R.id.button_solicitar_cita)
        buttonConsultarHistorial = findViewById(R.id.button_consultar_historial)
        buttonDescargarRecetas = findViewById(R.id.button_descargar_recetas)
        buttonTeleconsultas = findViewById(R.id.button_teleconsultas)
        buttonVolver = findViewById(R.id.button_volver)
    }
    
    private fun configurarEventos() {
        buttonAlertasCitas.setOnClickListener {
            if (validarAccesoFuncionalidad()) {
                abrirAlertasCitas()
            }
        }
        
        buttonSolicitarCita.setOnClickListener {
            if (validarAccesoFuncionalidad()) {
                abrirSolicitarCita()
            }
        }
        
        buttonConsultarHistorial.setOnClickListener {
            if (validarAccesoFuncionalidad()) {
                abrirConsultarHistorial()
            }
        }
        
        buttonDescargarRecetas.setOnClickListener {
            if (validarAccesoFuncionalidad()) {
                abrirDescargarRecetas()
            }
        }
        
        buttonTeleconsultas.setOnClickListener {
            if (validarAccesoFuncionalidad()) {
                abrirTeleconsultas()
            }
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun validarDatosPaciente() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null && sesion.usuario is Paciente) {
            paciente = sesion.usuario as Paciente
            
            if (paciente!!.tieneDatosPersonalesCompletos()) {
                mostrarInformacionPaciente()
            } else {
                mostrarAdvertenciaDatosIncompletos()
            }
        } else {
            mostrarMensaje("Error: No hay sesión activa de paciente")
            finish()
        }
    }
    
    private fun validarAccesoFuncionalidad(): Boolean {
        if (paciente == null) {
            mostrarMensaje("Error: No hay sesión activa de paciente")
            return false
        }
        
        // Validar información básica (nombre y email)
        if (!paciente!!.tieneInformacionBasicaCompleta()) {
            val camposBasicosFaltantes = paciente!!.obtenerCamposBasicosFaltantes()
            val mensaje = """
                ⚠️ Información Básica Incompleta
                
                Para acceder a las funcionalidades, es necesario que su información básica esté completa.
                
                📋 Campos faltantes:
                ${camposBasicosFaltantes.joinToString("\n• ", "• ")}
                
                🔧 Contacte al personal administrativo para completar su información básica.
                
                📞 Contacto: (555) 123-4567
                📧 Email: admin@softmed.com
            """.trimIndent()
            
            android.app.AlertDialog.Builder(this)
                .setTitle("⚠️ Información Básica Incompleta")
                .setMessage(mensaje)
                .setPositiveButton("Entendido") { _, _ -> }
                .setNegativeButton("Contactar Administrativo") { _, _ ->
                    mostrarOpcionesContacto()
                }
                .setCancelable(false)
                .show()
            return false
        }
        
        if (!paciente!!.tieneDatosPersonalesCompletos()) {
            mostrarAdvertenciaDatosIncompletos()
            return false
        }
        
        return true
    }
    
    private fun mostrarAdvertenciaDatosIncompletos() {
        val camposFaltantes = paciente?.obtenerCamposFaltantes() ?: emptyList()
        val porcentajeCompletitud = paciente?.obtenerPorcentajeCompletitud() ?: 0
        
        val mensaje = """
            ⚠️ Datos Personales Incompletos
            
            Para acceder a las funcionalidades del paciente, es necesario completar todos sus datos personales en el "Registro y Actualización de Pacientes".
            
            📊 Completitud actual: $porcentajeCompletitud%
            
            📋 Campos faltantes:
            ${camposFaltantes.joinToString("\n• ", "• ")}
            
            🔧 Para completar sus datos:
            1. Contacte al personal administrativo
            2. Solicite acceso al módulo de registro
            3. Complete todos los campos requeridos
            4. Una vez completado, podrá acceder a todas las funcionalidades
            
            📞 Contacto administrativo: (555) 123-4567
            📧 Email: admin@softmed.com
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("⚠️ Datos Incompletos")
            .setMessage(mensaje)
            .setPositiveButton("Entendido") { _, _ -> }
            .setNegativeButton("Contactar Administrativo") { _, _ ->
                mostrarOpcionesContacto()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun mostrarOpcionesContacto() {
        val opciones = arrayOf(
            "📞 Llamar al administrativo",
            "📧 Enviar email",
            "💬 Chat con soporte",
            "📋 Ver horarios de atención"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📞 Contactar Personal Administrativo")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarMensaje("📞 Llamando al administrativo... (555) 123-4567")
                    1 -> mostrarMensaje("📧 Redirigiendo a email: admin@softmed.com")
                    2 -> mostrarMensaje("💬 Abriendo chat de soporte...")
                    3 -> mostrarHorariosAtencion()
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarHorariosAtencion() {
        val horarios = """
            🕒 Horarios de Atención Administrativa:
            
            📅 Lunes a Viernes:
            ⏰ 8:00 AM - 6:00 PM
            
            📅 Sábados:
            ⏰ 8:00 AM - 12:00 PM
            
            📅 Domingos:
            ❌ Cerrado
            
            📞 Teléfono: (555) 123-4567
            📧 Email: admin@softmed.com
            🏥 Ubicación: Piso 1, Oficina 101
            
            💡 Recomendación:
            Para completar su registro, acérquese en horario de atención con su documento de identidad y información de seguro médico.
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🕒 Horarios de Atención")
            .setMessage(horarios)
            .setPositiveButton("Entendido") { _, _ -> }
            .show()
    }
    
    private fun mostrarInformacionPaciente() {
        paciente?.let { p ->
            textViewTitulo.text = "🏥 Funcionalidades del Paciente"
            textViewBienvenida.text = """
                ✅ Bienvenido ${p.obtenerNombre()}
                
                Su perfil está completo y puede acceder a todas las funcionalidades disponibles.
                
                Selecciona la funcionalidad que deseas utilizar:
            """.trimIndent()
        }
    }
    
    private fun abrirSolicitarCita() {
        val intent = Intent(this, SolicitarCitaActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirAlertasCitas() {
        val intent = Intent(this, AlertasCitasActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirConsultarHistorial() {
        val intent = Intent(this, HistorialClinicoActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirDescargarRecetas() {
        val intent = Intent(this, DescargarDocumentosActivity::class.java)
        startActivity(intent)
    }
    
    private fun abrirTeleconsultas() {
        val intent = Intent(this, ChatMedicoActivity::class.java)
        startActivity(intent)
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
} 