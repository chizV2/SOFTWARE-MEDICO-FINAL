package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softmedv5.database.SimpleDatabaseHelper
import com.example.softmedv5.util.GestorAutenticacion
import java.text.SimpleDateFormat
import java.util.*

class ComunicacionInternaActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewSubtitulo: TextView
    private lateinit var spinnerConversacion: Spinner
    private lateinit var editTextMensaje: EditText
    private lateinit var buttonEnviar: Button
    private lateinit var buttonNuevoUsuario: Button
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var buttonVolver: Button
    
    private lateinit var chatAdapter: ChatAdapter
    private val mensajes = mutableListOf<MensajeChat>()
    private var conversacionActual = ""
    private var conversaciones = mutableListOf<String>()
    private var dbHelper: SimpleDatabaseHelper? = null
    private var administrativoId: Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comunicacion_interna)
        
        try {
        inicializarVistas()
        configurarEventos()
            configurarBaseDatos()
            mostrarInformacionAdministrativo()
            cargarConversaciones()
            configurarSpinners()
        } catch (e: Exception) {
            mostrarMensaje("Error al inicializar: ${e.message}")
            e.printStackTrace()
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Recargar conversaciones cuando se regresa a esta actividad
        // Esto asegura que nuevos usuarios registrados aparezcan en la lista
        try {
            cargarConversaciones()
        } catch (e: Exception) {
            mostrarMensaje("Error al recargar conversaciones: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewSubtitulo = findViewById(R.id.text_view_subtitulo)
        spinnerConversacion = findViewById(R.id.spinner_conversacion)
        editTextMensaje = findViewById(R.id.edit_text_mensaje)
        buttonEnviar = findViewById(R.id.button_enviar)
        buttonNuevoUsuario = findViewById(R.id.button_actualizar_lista)
        recyclerViewChat = findViewById(R.id.recycler_view_chat)
        buttonVolver = findViewById(R.id.button_volver)
    }
    
    private fun configurarEventos() {
        buttonEnviar.setOnClickListener {
            enviarMensaje()
        }
        
        buttonNuevoUsuario.setOnClickListener {
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
    
    private fun mostrarInformacionAdministrativo() {
        try {
            val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
            val sesion = gestorAutenticacion.obtenerSesionActual()
            
            if (sesion != null) {
                val administrativo = sesion.usuario
                
                textViewTitulo.text = "💬 Comunicación Interna"
                textViewSubtitulo.text = "Administrativo: ${administrativo.obtenerNombre()}\n\nSelecciona con quién quieres conversar:"
                
                // Obtener ID del administrativo de manera segura
                administrativoId = obtenerIdUsuario(administrativo.obtenerEmail())
            } else {
                mostrarMensaje("Error: No hay sesión activa")
                finish()
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al mostrar información del administrativo: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun obtenerIdUsuario(email: String): Int {
        return try {
            // Buscar usuario por email sin contraseña
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
                1 // Default para demo
            }
            
            cursor?.close()
            id
        } catch (e: Exception) {
            mostrarMensaje("Error al obtener ID de usuario: ${e.message}")
            1 // Default para demo
        }
    }
    
    private fun cargarConversaciones() {
        try {
            // Obtener todos los usuarios registrados (pacientes, médicos, administrativos)
            val pacientes = dbHelper?.obtenerUsuariosPorRol("PACIENTE") ?: emptyList()
            val medicos = dbHelper?.obtenerUsuariosPorRol("MEDICO") ?: emptyList()
            val administrativos = dbHelper?.obtenerUsuariosPorRol("ADMINISTRADOR") ?: emptyList()
            
            // Crear lista de todos los usuarios disponibles para chatear
            val todosUsuarios = mutableListOf<Map<String, Any>>()
            todosUsuarios.addAll(pacientes)
            todosUsuarios.addAll(medicos)
            todosUsuarios.addAll(administrativos)
            
            // Filtrar para no incluir al administrativo actual
            val usuariosDisponibles = todosUsuarios.filter { usuario ->
                val usuarioId = (usuario["id"] as? Long)?.toInt() ?: 0
                usuarioId != administrativoId
            }
            
            // Crear lista de conversaciones formateadas
            conversaciones.clear()
            usuariosDisponibles.forEach { usuario ->
                try {
                    val rol = usuario["rol"] as? String ?: "DESCONOCIDO"
                    val nombre = usuario["nombre_completo"] as? String ?: "Usuario"
                    val emoji = when (rol) {
                        "MEDICO" -> "👨‍⚕️"
                        "ADMINISTRADOR" -> "👩‍💼"
                        "PACIENTE" -> "👤"
                        else -> "👤"
                    }
                    conversaciones.add("$emoji $nombre ($rol)")
                } catch (e: Exception) {
                    // Si hay error con un usuario, lo saltamos
                    return@forEach
                }
            }
            
            // Configurar spinner
            val opciones = mutableListOf("Selecciona con quién chatear...")
            opciones.addAll(conversaciones)
            
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerConversacion.adapter = adapter
            
            // Mostrar información resumida
            if (usuariosDisponibles.isNotEmpty()) {
                mostrarMensaje("Usuarios disponibles: ${usuariosDisponibles.size}")
            } else {
                mostrarMensaje("No hay usuarios disponibles para chatear")
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al cargar usuarios: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun configurarSpinners() {
        try {
            // Evento del spinner para cambiar de conversación
            spinnerConversacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    try {
                        if (position > 0) { // Evitar el primer elemento que es el placeholder
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
            // Limpiar mensajes actuales
            mensajes.clear()
            conversacionActual = remitente
            
            // Cargar mensajes de la conversación seleccionada
            cargarMensajesConversacion(remitente)
            
            // Actualizar el adapter
            chatAdapter.notifyDataSetChanged()
            if (mensajes.isNotEmpty()) {
                recyclerViewChat.scrollToPosition(mensajes.size - 1)
            }
            
            // Mostrar mensaje de cambio de conversación
            mostrarMensaje("Cambiando a conversación con $remitente")
        } catch (e: Exception) {
            mostrarMensaje("Error al cambiar conversación: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun cargarMensajesConversacion(remitente: String) {
        try {
            // Limpiar mensajes actuales
            mensajes.clear()
            
            // Extraer información del remitente seleccionado
            val partes = remitente.split(" (")
            if (partes.size >= 2) {
                val nombreDestinatario = partes[0].substringAfter(" ").trim() // Remover emoji
                val rolDestinatario = partes[1].removeSuffix(")")
                
                // Buscar ID del destinatario
                val destinatario = dbHelper?.obtenerUsuarioPorNombreYRol(nombreDestinatario, rolDestinatario)
                val destinatarioId = destinatario?.get("id")?.toString()?.toIntOrNull() ?: 0
                
                if (destinatarioId > 0) {
                    val mensajesRecibidos = dbHelper?.obtenerMensajesRecibidos(administrativoId) ?: emptyList()
                    val mensajesEnviados = dbHelper?.obtenerMensajesEnviados(administrativoId) ?: emptyList()
                    
                    // Filtrar mensajes de esta conversación específica
                    val mensajesConversacion = mutableListOf<Map<String, Any>>()
                    
                    // Agregar mensajes recibidos de este remitente específico
                    mensajesRecibidos.forEach { mensaje ->
                        val remitenteId = (mensaje["remitente_id"] as? Long)?.toInt() ?: 0
                        if (remitenteId == destinatarioId) {
                            mensajesConversacion.add(mensaje)
                        }
                    }
                    
                    // Agregar mensajes enviados a este destinatario específico
                    mensajesEnviados.forEach { mensaje ->
                        val destinatarioIdMensaje = (mensaje["destinatario_id"] as? Long)?.toInt() ?: 0
                        if (destinatarioIdMensaje == destinatarioId) {
                            mensajesConversacion.add(mensaje)
                        }
                    }
                    
                    // Ordenar por fecha de envío
                    mensajesConversacion.sortBy { it["fecha_envio"] as? Long ?: 0L }
                    
                    // Convertir a MensajeChat
                    mensajesConversacion.forEach { mensajeDb ->
                        try {
                            val remitenteId = (mensajeDb["remitente_id"] as? Long)?.toInt() ?: 0
                            val esMio = remitenteId == administrativoId
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
                            // Si hay error con un mensaje, lo saltamos
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
            
            if (texto.isNotEmpty() && conversacionActual.isNotEmpty()) {
                // Obtener información del destinatario de manera más robusta
                val partes = conversacionActual.split(" (")
                if (partes.size >= 2) {
                    val nombreDestinatario = partes[0].substringAfter(" ").trim() // Remover emoji
                    val rolDestinatario = partes[1].removeSuffix(")")
                    
                    // Buscar ID del destinatario usando el nuevo método más eficiente
                    val destinatario = dbHelper?.obtenerUsuarioPorNombreYRol(nombreDestinatario, rolDestinatario)
                    val destinatarioId = destinatario?.get("id")?.toString()?.toIntOrNull() ?: 0
                    
                    if (destinatarioId > 0) {
                        // Insertar mensaje en la base de datos
                        val administrativo = GestorAutenticacion.obtenerInstancia().obtenerSesionActual()?.usuario
                        val mensajeId = dbHelper?.insertarMensaje(
                            administrativoId,
                            administrativo?.obtenerNombre() ?: "Administrativo",
                            "ADMINISTRADOR",
                            destinatarioId,
                            nombreDestinatario,
                            rolDestinatario,
                            "Mensaje administrativo",
                            texto
                        ) ?: 0
                        
                        if (mensajeId > 0) {
                            // Agregar mensaje a la lista local
                            val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                            mensajes.add(MensajeChat(
                                "Tú",
                                texto,
                                hora,
                                true
                            ))
                            
                            editTextMensaje.text.clear()
                            chatAdapter.notifyDataSetChanged()
                            recyclerViewChat.scrollToPosition(mensajes.size - 1)
                            
                            mostrarMensaje("Mensaje enviado a $nombreDestinatario")
                        } else {
                            mostrarMensaje("Error al enviar el mensaje")
                        }
                    } else {
                        mostrarMensaje("No se pudo encontrar al destinatario: $nombreDestinatario ($rolDestinatario)")
                    }
                } else {
                    mostrarMensaje("Formato de destinatario inválido")
                }
            } else {
                mostrarMensaje("Por favor escribe un mensaje y selecciona un destinatario")
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al enviar mensaje: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun actualizarListaUsuarios() {
        try {
            mostrarMensaje("Actualizando lista de usuarios...")
            
            // Recargar conversaciones
            cargarConversaciones()
            
            // Mostrar mensaje de confirmación
            val totalUsuarios = conversaciones.size
            mostrarMensaje("Lista actualizada. Usuarios disponibles: $totalUsuarios")
            
        } catch (e: Exception) {
            mostrarMensaje("Error al actualizar lista de usuarios: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun mostrarMensaje(mensaje: String) {
        try {
            android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        try {
            dbHelper?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Clase para representar un mensaje del chat
    data class MensajeChat(
        val remitente: String,
        val mensaje: String,
        val hora: String,
        val esMio: Boolean
    )
    
    // Adapter para el RecyclerView del chat
    inner class ChatAdapter(private val mensajes: List<MensajeChat>) : 
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
        
        inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textViewRemitente: TextView = itemView.findViewById(R.id.text_view_remitente)
            val textViewMensaje: TextView = itemView.findViewById(R.id.text_view_mensaje)
            val textViewHora: TextView = itemView.findViewById(R.id.text_view_hora)
            val cardView: CardView = itemView.findViewById(R.id.card_view_mensaje)
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val view = layoutInflater.inflate(R.layout.item_mensaje_chat, parent, false)
            return ChatViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            try {
                val mensaje = mensajes[position]
                
                holder.textViewRemitente.text = mensaje.remitente
                holder.textViewMensaje.text = mensaje.mensaje
                holder.textViewHora.text = mensaje.hora
                
                // Configurar el estilo según si es mío o no
                if (mensaje.esMio) {
                    holder.cardView.setCardBackgroundColor(resources.getColor(android.R.color.holo_blue_light, null))
                    holder.textViewRemitente.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                    holder.textViewMensaje.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                    holder.textViewHora.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                } else {
                    holder.cardView.setCardBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                    holder.textViewRemitente.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    holder.textViewMensaje.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    holder.textViewHora.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        override fun getItemCount(): Int = mensajes.size
    }
} 