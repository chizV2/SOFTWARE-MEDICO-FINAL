package com.example.softmedv5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val mensajes: List<MensajeChat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    
    companion object {
        private const val VIEW_TYPE_MIO = 1
        private const val VIEW_TYPE_OTRO = 2
    }
    
    override fun getItemViewType(position: Int): Int {
        return if (mensajes[position].esMio) VIEW_TYPE_MIO else VIEW_TYPE_OTRO
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutRes = if (viewType == VIEW_TYPE_MIO) {
            R.layout.item_mensaje_chat_mio
        } else {
            R.layout.item_mensaje_chat_otro
        }
        
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ChatViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.bind(mensaje)
    }
    
    override fun getItemCount(): Int = mensajes.size
    
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewRemitente: TextView = itemView.findViewById(R.id.text_view_remitente)
        private val textViewMensaje: TextView = itemView.findViewById(R.id.text_view_mensaje)
        private val textViewHora: TextView = itemView.findViewById(R.id.text_view_hora)
        
        fun bind(mensaje: MensajeChat) {
            textViewRemitente.text = mensaje.remitente
            textViewMensaje.text = mensaje.contenido
            textViewHora.text = mensaje.hora
        }
    }
}

data class MensajeChat(
    val remitente: String,
    val contenido: String,
    val hora: String,
    val esMio: Boolean
) 