package com.example.softmedv5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.util.GestorNotificaciones
import com.example.softmedv5.util.NotificacionUsuario
import com.example.softmedv5.util.EstadoNotificacion
import java.time.format.DateTimeFormatter

class GestionNotificacionesActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonLimpiarProcesadas: Button
    private lateinit var buttonVolver: Button
    private lateinit var linearLayoutNotificaciones: LinearLayout
    private lateinit var textViewSinNotificaciones: TextView
    
    private val gestorNotificaciones = GestorNotificaciones.obtenerInstancia()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_notificaciones)
        
        inicializarVistas()
        configurarEventos()
        cargarNotificaciones()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonLimpiarProcesadas = findViewById(R.id.button_limpiar_procesadas)
        buttonVolver = findViewById(R.id.button_volver)
        linearLayoutNotificaciones = findViewById(R.id.linear_layout_notificaciones)
        textViewSinNotificaciones = findViewById(R.id.text_view_sin_notificaciones)
        
        textViewTitulo.text = "🔔 Gestión de Notificaciones"
    }
    
    private fun configurarEventos() {
        buttonVolver.setOnClickListener {
            finish()
        }
        
        buttonLimpiarProcesadas.setOnClickListener {
            limpiarNotificacionesProcesadas()
        }
    }
    
    private fun cargarNotificaciones() {
        // Limpiar la lista actual
        linearLayoutNotificaciones.removeAllViews()
        
        val todasLasNotificaciones = gestorNotificaciones.obtenerTodasLasNotificaciones()
        val notificacionesPendientes = gestorNotificaciones.obtenerNotificacionesPendientes()
        
        // Mostrar resumen
        mostrarResumen(notificacionesPendientes.size, todasLasNotificaciones.size)
        
        if (todasLasNotificaciones.isNotEmpty()) {
            // Agrupar por estado
            val pendientes = todasLasNotificaciones.filter { it.estado == EstadoNotificacion.PENDIENTE }
            val procesadas = todasLasNotificaciones.filter { it.estado == EstadoNotificacion.PROCESADA }
            
            // Mostrar notificaciones pendientes primero
            if (pendientes.isNotEmpty()) {
                agregarTituloSeccion("⏳ Notificaciones Pendientes")
                pendientes.forEach { notificacion ->
                    agregarNotificacionALista(notificacion, true)
                }
            }
            
            // Mostrar notificaciones procesadas
            if (procesadas.isNotEmpty()) {
                agregarTituloSeccion("✅ Notificaciones Procesadas")
                procesadas.forEach { notificacion ->
                    agregarNotificacionALista(notificacion, false)
                }
            }
            
            textViewSinNotificaciones.visibility = View.GONE
            linearLayoutNotificaciones.visibility = View.VISIBLE
        } else {
            mostrarSinNotificaciones()
        }
    }
    
    private fun mostrarResumen(pendientes: Int, total: Int) {
        val resumen = """
            📊 Resumen de Notificaciones:
            
            ⏳ Pendientes: $pendientes
            ✅ Procesadas: ${total - pendientes}
            📋 Total: $total
            
            💡 Acciones disponibles:
            • Ver detalles de cada notificación
            • Marcar como procesada
            • Gestionar información del usuario
            • Limpiar notificaciones antiguas
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun agregarTituloSeccion(titulo: String) {
        val textView = TextView(this)
        textView.text = titulo
        textView.textSize = 18f
        textView.setTypeface(null, android.graphics.Typeface.BOLD)
        textView.setTextColor(resources.getColor(R.color.colorPrimary, null))
        textView.setPadding(0, 24, 0, 12)
        textView.gravity = android.view.Gravity.CENTER
        
        linearLayoutNotificaciones.addView(textView)
    }
    
    private fun agregarNotificacionALista(notificacion: NotificacionUsuario, esPendiente: Boolean) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_notificacion_administrativa,
            linearLayoutNotificaciones,
            false
        )
        
        val textViewTitulo = cardView.findViewById<TextView>(R.id.text_view_titulo_notificacion)
        val textViewMensaje = cardView.findViewById<TextView>(R.id.text_view_mensaje_notificacion)
        val textViewFecha = cardView.findViewById<TextView>(R.id.text_view_fecha_notificacion)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado_notificacion)
        val buttonVerDetalles = cardView.findViewById<Button>(R.id.button_ver_detalles)
        val buttonProcesar = cardView.findViewById<Button>(R.id.button_procesar)
        val buttonGestionar = cardView.findViewById<Button>(R.id.button_gestionar)
        
        // Configurar información de la notificación
        textViewTitulo.text = "🔔 ${notificacion.tipo.name.replace("_", " ")}"
        textViewMensaje.text = notificacion.mensaje
        textViewFecha.text = "📅 ${notificacion.fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}"
        
        // Configurar estado
        when (notificacion.estado) {
            EstadoNotificacion.PENDIENTE -> {
                textViewEstado.text = "⏳ Pendiente"
                textViewEstado.setTextColor(resources.getColor(R.color.colorWarning, null))
                buttonProcesar.visibility = View.VISIBLE
                buttonGestionar.visibility = View.VISIBLE
            }
            EstadoNotificacion.PROCESADA -> {
                textViewEstado.text = "✅ Procesada"
                textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
                buttonProcesar.visibility = View.GONE
                buttonGestionar.visibility = View.GONE
                
                notificacion.fechaProcesamiento?.let { fecha ->
                    val fechaActual = textViewFecha.text.toString()
                    textViewFecha.text = "$fechaActual\n✅ Procesada: ${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}"
                }
            }
            EstadoNotificacion.CANCELADA -> {
                textViewEstado.text = "❌ Cancelada"
                textViewEstado.setTextColor(resources.getColor(R.color.colorError, null))
                buttonProcesar.visibility = View.GONE
                buttonGestionar.visibility = View.GONE
            }
        }
        
        // Configurar botones
        buttonVerDetalles.setOnClickListener {
            mostrarDetallesNotificacion(notificacion)
        }
        
        buttonProcesar.setOnClickListener {
            procesarNotificacion(notificacion)
        }
        
        buttonGestionar.setOnClickListener {
            gestionarUsuario(notificacion)
        }
        
        linearLayoutNotificaciones.addView(cardView)
    }
    
    private fun mostrarDetallesNotificacion(notificacion: NotificacionUsuario) {
        val detalles = """
            📋 Detalles de la Notificación
            
            🆔 ID: ${notificacion.id}
            📝 Tipo: ${notificacion.tipo.name.replace("_", " ")}
            📅 Fecha: ${notificacion.fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}
            📊 Estado: ${notificacion.estado.name}
            ⚠️ Requiere acción: ${if (notificacion.requiereAccion) "Sí" else "No"}
            
            👤 Información del Usuario:
            ${notificacion.mensaje}
            
            ${if (notificacion.fechaProcesamiento != null) "✅ Procesada: ${notificacion.fechaProcesamiento!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}" else ""}
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📋 Detalles de Notificación")
            .setMessage(detalles)
            .setPositiveButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun procesarNotificacion(notificacion: NotificacionUsuario) {
        android.app.AlertDialog.Builder(this)
            .setTitle("✅ Procesar Notificación")
            .setMessage("""
                ¿Marcar esta notificación como procesada?
                
                👤 Usuario: ${notificacion.usuario.obtenerNombre()}
                📧 Email: ${notificacion.usuario.obtenerEmail()}
                🏷️ Rol: ${notificacion.usuario.obtenerNombreRol()}
                
                Esta acción no se puede deshacer.
            """.trimIndent())
            .setPositiveButton("Procesar") { _, _ ->
                gestorNotificaciones.marcarComoProcesada(notificacion.id)
                cargarNotificaciones() // Recargar la lista
                mostrarMensaje("✅ Notificación procesada exitosamente")
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun gestionarUsuario(notificacion: NotificacionUsuario) {
        val opciones = arrayOf(
            "👤 Ver perfil completo del usuario",
            "✏️ Editar información personal",
            "📄 Gestionar documentos",
            "🔐 Asignar permisos específicos",
            "📧 Contactar al usuario",
            "📋 Ver historial de actividades"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🔧 Gestionar Usuario - ${notificacion.usuario.obtenerNombre()}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarPerfilUsuario(notificacion.usuario)
                    1 -> editarInformacionUsuario(notificacion.usuario)
                    2 -> gestionarDocumentosUsuario(notificacion.usuario)
                    3 -> asignarPermisosUsuario(notificacion.usuario)
                    4 -> contactarUsuario(notificacion.usuario)
                    5 -> mostrarHistorialUsuario(notificacion.usuario)
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarPerfilUsuario(usuario: com.example.softmedv5.modelo.Usuario) {
        val perfil = """
            👤 Perfil Completo del Usuario
            
            📋 Información Básica:
            👤 Nombre: ${usuario.obtenerNombre()}
            📧 Email: ${usuario.obtenerEmail()}
            🆔 ID: ${usuario.obtenerId()}
            🏷️ Rol: ${usuario.obtenerNombreRol()}
            
            🔐 Permisos:
            ${usuario.obtenerPermisos().joinToString("\n• ", "• ")}
            
            📅 Información de Sesión:
            • Último acceso: ${java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
            • Estado: Activo
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("👤 Perfil de ${usuario.obtenerNombre()}")
            .setMessage(perfil)
            .setPositiveButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun editarInformacionUsuario(usuario: com.example.softmedv5.modelo.Usuario) {
        mostrarMensaje("✏️ Funcionalidad de edición en desarrollo para ${usuario.obtenerNombre()}")
    }
    
    private fun gestionarDocumentosUsuario(usuario: com.example.softmedv5.modelo.Usuario) {
        mostrarMensaje("📄 Gestión de documentos en desarrollo para ${usuario.obtenerNombre()}")
    }
    
    private fun asignarPermisosUsuario(usuario: com.example.softmedv5.modelo.Usuario) {
        mostrarMensaje("🔐 Asignación de permisos en desarrollo para ${usuario.obtenerNombre()}")
    }
    
    private fun contactarUsuario(usuario: com.example.softmedv5.modelo.Usuario) {
        mostrarMensaje("📧 Funcionalidad de contacto en desarrollo para ${usuario.obtenerNombre()}")
    }
    
    private fun mostrarHistorialUsuario(usuario: com.example.softmedv5.modelo.Usuario) {
        mostrarMensaje("📋 Historial de actividades en desarrollo para ${usuario.obtenerNombre()}")
    }
    
    private fun limpiarNotificacionesProcesadas() {
        android.app.AlertDialog.Builder(this)
            .setTitle("🧹 Limpiar Notificaciones")
            .setMessage("""
                ¿Limpiar todas las notificaciones procesadas?
                
                ⚠️ Esta acción eliminará permanentemente todas las notificaciones que ya han sido procesadas.
                
                Las notificaciones pendientes no se verán afectadas.
            """.trimIndent())
            .setPositiveButton("Limpiar") { _, _ ->
                gestorNotificaciones.limpiarNotificacionesAntiguas(0) // Limpiar todas las procesadas
                cargarNotificaciones() // Recargar la lista
                mostrarMensaje("🧹 Notificaciones procesadas limpiadas exitosamente")
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarSinNotificaciones() {
        textViewSinNotificaciones.visibility = View.VISIBLE
        linearLayoutNotificaciones.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarNotificaciones() // Recargar cuando se regrese a esta actividad
    }
} 