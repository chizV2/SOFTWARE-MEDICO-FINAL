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

class ChatMedicoActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewSubtitulo: TextView
    private lateinit var spinnerDestinatario: Spinner
    private lateinit var editTextMensaje: EditText
    private lateinit var buttonEnviar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var buttonVolver: Button
    
    private lateinit var chatAdapter: ChatAdapter
    private val mensajes = mutableListOf<MensajeChat>()
    private var dbHelper: SimpleDatabaseHelper? = null
    private var pacienteId: Int = 0
    private var destinatarios = mutableListOf<Map<String, Any>>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_medico)
        
        try {
            inicializarVistas()
            configurarEventos()
            configurarBaseDatos()
            mostrarInformacionPaciente()
            cargarDestinatarios()
            configurarSpinners()
        } catch (e: Exception) {
            mostrarMensaje("Error al inicializar: ${e.message}")
            e.printStackTrace()
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Recargar destinatarios cuando se regresa a esta actividad
        // Esto asegura que nuevos usuarios registrados aparezcan en la lista
        try {
            cargarDestinatarios()
            configurarSpinners()
        } catch (e: Exception) {
            mostrarMensaje("Error al recargar destinatarios: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewSubtitulo = findViewById(R.id.text_view_subtitulo)
        spinnerDestinatario = findViewById(R.id.spinner_destinatario)
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
    
    private fun mostrarInformacionPaciente() {
        try {
            val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
            val sesion = gestorAutenticacion.obtenerSesionActual()
            
            if (sesion != null) {
                val paciente = sesion.usuario
                
                textViewTitulo.text = "💬 Chat Médico"
                textViewSubtitulo.text = "Paciente: ${paciente.obtenerNombre()}\n\nSelecciona con quién quieres chatear:"
                
                // Obtener ID del paciente de manera segura
                pacienteId = obtenerIdUsuario(paciente.obtenerEmail())
            } else {
                mostrarMensaje("Error: No hay sesión activa")
                finish()
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al mostrar información del paciente: ${e.message}")
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
                3 // Default para demo
            }
            
            cursor?.close()
            id
        } catch (e: Exception) {
            mostrarMensaje("Error al obtener ID de usuario: ${e.message}")
            3 // Default para demo
        }
    }
    
    private fun cargarDestinatarios() {
        try {
            // Obtener todos los usuarios registrados (pacientes, médicos, administrativos)
            val pacientes = dbHelper?.obtenerUsuariosPorRol("PACIENTE") ?: emptyList()
            val medicos = dbHelper?.obtenerUsuariosPorRol("MEDICO") ?: emptyList()
            val administrativos = dbHelper?.obtenerUsuariosPorRol("ADMINISTRADOR") ?: emptyList()
            
            destinatarios.clear()
            destinatarios.addAll(pacientes)
            destinatarios.addAll(medicos)
            destinatarios.addAll(administrativos)
            
            // Filtrar para no incluir al paciente actual
            val usuariosDisponibles = destinatarios.filter { usuario ->
                val usuarioId = (usuario["id"] as? Long)?.toInt() ?: 0
                usuarioId != pacienteId
            }
            
            destinatarios.clear()
            destinatarios.addAll(usuariosDisponibles)
            
            // Mostrar información de debug
            println("=== DEBUG: ChatMedicoActivity.cargarDestinatarios ===")
            println("Pacientes encontrados: ${pacientes.size}")
            println("Médicos encontrados: ${medicos.size}")
            println("Administrativos encontrados: ${administrativos.size}")
            println("Total de usuarios disponibles: ${destinatarios.size}")
            println("ID del paciente actual: $pacienteId")
            
            // Si no hay destinatarios, mostrar mensaje
            if (destinatarios.isEmpty()) {
                mostrarMensaje("No hay usuarios disponibles para chatear")
            } else {
                mostrarMensaje("Usuarios disponibles: ${destinatarios.size}")
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al cargar destinatarios: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun actualizarListaUsuarios() {
        try {
            mostrarMensaje("Actualizando lista de usuarios...")
            cargarDestinatarios()
            configurarSpinners()
            mostrarMensaje("Lista actualizada. Usuarios disponibles: ${destinatarios.size}")
        } catch (e: Exception) {
            mostrarMensaje("Error al actualizar lista: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun configurarSpinners() {
        try {
            // Crear lista de destinatarios formateados
            val destinatariosFormateados = mutableListOf<String>()
            destinatarios.forEach { usuario ->
                try {
                    val rol = usuario["rol"] as? String ?: "DESCONOCIDO"
                    val nombre = usuario["nombre_completo"] as? String ?: "Usuario"
                    val emoji = when (rol) {
                        "MEDICO" -> "👨‍⚕️"
                        "ADMINISTRADOR" -> "👩‍💼"
                        else -> "👤"
                    }
                    destinatariosFormateados.add("$emoji $nombre ($rol)")
                } catch (e: Exception) {
                    // Si hay error con un usuario, lo saltamos
                    return@forEach
                }
            }
            
            // Si no hay destinatarios formateados, agregar uno por defecto
            if (destinatariosFormateados.isEmpty()) {
                destinatariosFormateados.add("👨‍⚕️ Dr. Demo (MEDICO)")
            }
            
            val adapterDestinatario = ArrayAdapter(this, android.R.layout.simple_spinner_item, destinatariosFormateados)
            adapterDestinatario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDestinatario.adapter = adapterDestinatario
            
            // Evento del spinner para cambiar de conversación
            spinnerDestinatario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    try {
                        cambiarConversacion(position)
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
    
    private fun cambiarConversacion(position: Int) {
        try {
            if (position >= 0 && position < destinatarios.size) {
                // Limpiar mensajes actuales
                mensajes.clear()
                
                // Cargar mensajes de la nueva conversación
                cargarMensajesConversacion(position)
                
                // Actualizar el adapter
                chatAdapter.notifyDataSetChanged()
                if (mensajes.isNotEmpty()) {
                    recyclerViewChat.scrollToPosition(mensajes.size - 1)
                }
                
                // Mostrar mensaje de cambio de conversación
                val destinatario = destinatarios[position]
                val nombre = destinatario["nombre_completo"] as? String ?: "Usuario"
                mostrarMensaje("Cambiando a conversación con $nombre")
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al cambiar conversación: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun cargarMensajesConversacion(position: Int) {
        try {
            if (position >= 0 && position < destinatarios.size) {
                val destinatario = destinatarios[position]
                val destinatarioId = (destinatario["id"] as? Long)?.toInt() ?: 0
                
                if (destinatarioId > 0) {
                    // Obtener mensajes de la base de datos
                    val mensajesRecibidos = dbHelper?.obtenerMensajesRecibidos(pacienteId) ?: emptyList()
                    val mensajesEnviados = dbHelper?.obtenerMensajesEnviados(pacienteId) ?: emptyList()
                    
                    // Filtrar mensajes de esta conversación
                    val mensajesConversacion = mutableListOf<Map<String, Any>>()
                    
                    // Agregar mensajes recibidos de este destinatario
                    mensajesRecibidos.forEach { mensaje ->
                        val remitenteId = (mensaje["remitente_id"] as? Long)?.toInt() ?: 0
                        if (remitenteId == destinatarioId) {
                            mensajesConversacion.add(mensaje)
                        }
                    }
                    
                    // Agregar mensajes enviados a este destinatario
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
                            val esMio = remitenteId == pacienteId
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
            val position = spinnerDestinatario.selectedItemPosition
            
            if (texto.isNotEmpty() && position >= 0 && position < destinatarios.size) {
                val destinatario = destinatarios[position]
                val destinatarioId = (destinatario["id"] as? Long)?.toInt() ?: 0
                val destinatarioNombre = destinatario["nombre_completo"] as? String ?: "Usuario"
                val destinatarioRol = destinatario["rol"] as? String ?: "DESCONOCIDO"
                
                if (destinatarioId > 0) {
                    // Obtener información del paciente
                    val paciente = GestorAutenticacion.obtenerInstancia().obtenerSesionActual()?.usuario
                    
                    // Insertar mensaje en la base de datos
                    val mensajeId = dbHelper?.insertarMensaje(
                        pacienteId,
                        paciente?.obtenerNombre() ?: "Paciente",
                        "PACIENTE",
                        destinatarioId,
                        destinatarioNombre,
                        destinatarioRol,
                        "Consulta del paciente",
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
                        
                        mostrarMensaje("Mensaje enviado a $destinatarioNombre")
                    } else {
                        mostrarMensaje("Error al enviar el mensaje")
                    }
                } else {
                    mostrarMensaje("Destinatario no válido")
                }
            } else {
                mostrarMensaje("Por favor escribe un mensaje y selecciona un destinatario")
            }
        } catch (e: Exception) {
            mostrarMensaje("Error al enviar mensaje: ${e.message}")
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