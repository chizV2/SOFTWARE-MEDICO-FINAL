package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softmedv5.database.SimpleDatabaseHelper
import com.example.softmedv5.util.GestorAutenticacion
import java.text.SimpleDateFormat
import java.util.*

class ChatUnificadoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewSubtitulo: TextView
    private lateinit var spinnerConversacion: Spinner
    private lateinit var editTextMensaje: EditText
    private lateinit var buttonEnviar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var buttonVolver: Button
    
    private lateinit var chatAdapter: ChatAdapter
    private val mensajes = mutableListOf<MensajeChat>()
    private var conversacionActual = ""
    private var conversaciones = mutableListOf<String>()
    private var usuariosDisponibles = mutableListOf<Map<String, Any>>()
    private var dbHelper: SimpleDatabaseHelper? = null
    private var usuarioActualId: Int = 0
    private var usuarioActualRol: String = ""
    private var usuarioActualNombre: String = ""
    
    // Para actualización en tiempo real
    private val handler = Handler(Looper.getMainLooper())
    private val actualizacionRunnable = object : Runnable {
        override fun run() {
            if (conversacionActual.isNotEmpty()) {
                cargarMensajesConversacion(conversacionActual)
            }
            handler.postDelayed(this, 3000) // Actualizar cada 3 segundos
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_unificado)
        
        try {
            inicializarVistas()
            configurarEventos()
            configurarBaseDatos()
            mostrarInformacionUsuario()
            cargarConversaciones()
            configurarSpinners()
            iniciarActualizacionTiempoReal()
        } catch (e: Exception) {
            mostrarMensaje("Error al inicializar: ${e.message}")
            e.printStackTrace()
        }
    }
    
    override fun onResume() {
        super.onResume()
        try {
            cargarConversaciones()
            if (conversacionActual.isNotEmpty()) {
                cargarMensajesConversacion(conversacionActual)
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al recargar: ${e.message}")
            e.printStackTrace()
        }
    }
    
    override fun onPause() {
        super.onPause()
        detenerActualizacionTiempoReal()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        detenerActualizacionTiempoReal()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewSubtitulo = findViewById(R.id.text_view_subtitulo)
        spinnerConversacion = findViewById(R.id.spinner_conversacion)
        editTextMensaje = findViewById(R.id.edit_text_mensaje)
        buttonEnviar = findViewById(R.id.button_enviar)
        buttonActualizar = findViewById(R.id.button_actualizar)
        recyclerViewChat = findViewById(R.id.recycler_view_chat)
        buttonVolver = findViewById(R.id.button_volver)
    }
    
    private fun configurarEventos() {
        buttonEnviar.setOnClickListener {
            enviarMensaje()
        }
        
        buttonActualizar.setOnClickListener {
            actualizarListaUsuarios()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
        
        // Configurar RecyclerView
        recyclerViewChat.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(mensajes)
        recyclerViewChat.adapter = chatAdapter
    }
    
    private fun configurarBaseDatos() {
        try {
            dbHelper = SimpleDatabaseHelper(this)
        } catch (e: Exception) {
            mostrarMensaje("Error al configurar base de datos: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun mostrarInformacionUsuario() {
        try {
            val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
            val sesion = gestorAutenticacion.obtenerSesionActual()
            
            if (sesion != null) {
                val usuario = sesion.usuario
                usuarioActualNombre = usuario.obtenerNombre()
                usuarioActualRol = usuario.obtenerNombreRol()
                usuarioActualId = obtenerIdUsuario(usuario.obtenerEmail())
                
                val emoji = obtenerEmojiPorRol(usuarioActualRol)
                
                textViewTitulo.text = "💬 Chat Unificado"
                textViewSubtitulo.text = "$emoji $usuarioActualNombre ($usuarioActualRol)\n\nSelecciona con quién quieres conversar:"
                
            } else {
                mostrarMensaje("Error: No hay sesión activa")
                finish()
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al mostrar información del usuario: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun obtenerEmojiPorRol(rol: String): String {
        return when (rol) {
            "Médico" -> "👨‍⚕️"
            "Personal Administrativo" -> "👩‍💼"
            "Paciente" -> "👤"
            else -> "👤"
        }
    }
    
    private fun obtenerIdUsuario(email: String): Int {
        return try {
            val db = dbHelper?.readableDatabase
            val cursor = db?.query(
                "usuarios",
                arrayOf("id"),
                "email = ? AND activo = 1",
                arrayOf(email),
                null,
                null,
                null
            )
            
            val id = if (cursor?.moveToFirst() == true) {
                cursor.getLong(0).toInt()
            } else {
                1 // Default
            }
            
            cursor?.close()
            id
        } catch (e: Exception) {
            mostrarMensaje("Error al obtener ID de usuario: ${e.message}")
            1
        }
    }
    
    private fun cargarConversaciones() {
        try {
            // Obtener todos los usuarios registrados
            val pacientes = dbHelper?.obtenerUsuariosPorRol("PACIENTE") ?: emptyList()
            val medicos = dbHelper?.obtenerUsuariosPorRol("MEDICO") ?: emptyList()
            val administrativos = dbHelper?.obtenerUsuariosPorRol("ADMINISTRATIVO") ?: emptyList()
            
            // Crear lista de todos los usuarios disponibles
            val todosUsuarios = mutableListOf<Map<String, Any>>()
            todosUsuarios.addAll(pacientes)
            todosUsuarios.addAll(medicos)
            todosUsuarios.addAll(administrativos)
            
            // Filtrar para no incluir al usuario actual
            usuariosDisponibles = todosUsuarios.filter { usuario ->
                val usuarioId = (usuario["id"] as? Long)?.toInt() ?: 0
                usuarioId != usuarioActualId
            }.toMutableList()
            
            // Crear lista de conversaciones formateadas
            conversaciones.clear()
            usuariosDisponibles.forEach { usuario ->
                try {
                    val rol = usuario["rol"] as? String ?: "DESCONOCIDO"
                    val nombre = usuario["nombre_completo"] as? String ?: "Usuario"
                    val emoji = when (rol) {
                        "MEDICO" -> "👨‍⚕️"
                        "ADMINISTRATIVO" -> "👩‍💼"
                        "PACIENTE" -> "👤"
                        else -> "👤"
                    }
                    conversaciones.add("$emoji $nombre ($rol)")
                } catch (e: Exception) {
                    return@forEach
                }
            }
            
            // Configurar spinner
            val opciones = mutableListOf("Selecciona con quién chatear...")
            opciones.addAll(conversaciones)
            
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerConversacion.adapter = adapter
            
            // Mostrar información
            if (usuariosDisponibles.isNotEmpty()) {
                mostrarMensaje("Usuarios disponibles: ${usuariosDisponibles.size}")
            } else {
                mostrarMensaje("No hay usuarios disponibles para chatear")
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al cargar conversaciones: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun configurarSpinners() {
        try {
            spinnerConversacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    try {
                        if (position > 0) {
                            cambiarConversacion(conversaciones[position - 1])
                        }
                    } catch (e: Exception) {
                        mostrarMensaje("Error al cambiar conversación: ${e.message}")
                        e.printStackTrace()
                    }
                }
                
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al configurar spinner: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun cambiarConversacion(remitente: String) {
        try {
            mensajes.clear()
            conversacionActual = remitente
            cargarMensajesConversacion(remitente)
            chatAdapter.notifyDataSetChanged()
            if (mensajes.isNotEmpty()) {
                recyclerViewChat.scrollToPosition(mensajes.size - 1)
            }
            mostrarMensaje("Cambiando a conversación con $remitente")
        } catch (e: Exception) {
            mostrarMensaje("Error al cambiar conversación: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun cargarMensajesConversacion(remitente: String) {
        try {
            mensajes.clear()
            
            val partes = remitente.split(" (")
            if (partes.size >= 2) {
                val nombreDestinatario = partes[0].substringAfter(" ").trim()
                val rolDestinatario = partes[1].removeSuffix(")")
                
                val destinatario = dbHelper?.obtenerUsuarioPorNombreYRol(nombreDestinatario, rolDestinatario)
                val destinatarioId = destinatario?.get("id")?.toString()?.toIntOrNull() ?: 0
                
                if (destinatarioId > 0) {
                    // Usar el nuevo método para obtener mensajes privados entre estos dos usuarios específicos
                    val mensajesConversacion = dbHelper?.obtenerMensajesPrivadosEntreUsuarios(usuarioActualId, destinatarioId) ?: emptyList()
                    
                    // Convertir a MensajeChat
                    mensajesConversacion.forEach { mensajeDb ->
                        try {
                            val remitenteId = (mensajeDb["remitente_id"] as? Long)?.toInt() ?: 0
                            val esMio = remitenteId == usuarioActualId
                            val remitenteNombre = if (esMio) "Tú" else (mensajeDb["remitente_nombre"] as? String ?: "Usuario")
                            val fecha = Date(mensajeDb["fecha_envio"] as? Long ?: System.currentTimeMillis())
                            val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(fecha)
                            
                            mensajes.add(MensajeChat(
                                remitenteNombre,
                                mensajeDb["contenido"] as? String ?: "",
                                hora,
                                esMio
                            ))
                        } catch (e: Exception) {
                            return@forEach
                        }
                    }
                }
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al cargar mensajes: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun enviarMensaje() {
        try {
            val texto = editTextMensaje.text.toString().trim()
            if (texto.isEmpty()) {
                mostrarMensaje("Por favor escribe un mensaje")
                return
            }
            
            if (conversacionActual.isEmpty()) {
                mostrarMensaje("Por favor selecciona con quién quieres chatear")
                return
            }
            
            // Extraer información del destinatario
            val partes = conversacionActual.split(" (")
            if (partes.size >= 2) {
                val nombreDestinatario = partes[0].substringAfter(" ").trim()
                val rolDestinatario = partes[1].removeSuffix(")")
                
                val destinatario = dbHelper?.obtenerUsuarioPorNombreYRol(nombreDestinatario, rolDestinatario)
                val destinatarioId = destinatario?.get("id")?.toString()?.toIntOrNull() ?: 0
                
                if (destinatarioId > 0) {
                    // Guardar mensaje en la base de datos
                    val mensajeGuardado = dbHelper?.guardarMensaje(
                        usuarioActualId,
                        destinatarioId,
                        texto,
                        usuarioActualNombre
                    )
                    
                    if (mensajeGuardado == true) {
                        // Limpiar campo de texto
                        editTextMensaje.text.clear()
                        
                        // Recargar mensajes de la conversación actual
                        cargarMensajesConversacion(conversacionActual)
                        chatAdapter.notifyDataSetChanged()
                        
                        if (mensajes.isNotEmpty()) {
                            recyclerViewChat.scrollToPosition(mensajes.size - 1)
                        }
                        
                        mostrarMensaje("Mensaje enviado")
                    } else {
                        mostrarMensaje("Error al enviar mensaje")
                    }
                } else {
                    mostrarMensaje("Error: No se pudo identificar al destinatario")
                }
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al enviar mensaje: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun actualizarListaUsuarios() {
        try {
            mostrarMensaje("Actualizando lista de usuarios...")
            cargarConversaciones()
            configurarSpinners()
            mostrarMensaje("Lista actualizada. Usuarios disponibles: ${usuariosDisponibles.size}")
        } catch (e: Exception) {
            mostrarMensaje("Error al actualizar lista: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun iniciarActualizacionTiempoReal() {
        handler.post(actualizacionRunnable)
    }
    
    private fun detenerActualizacionTiempoReal() {
        handler.removeCallbacks(actualizacionRunnable)
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
} 