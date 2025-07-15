package com.example.softmedv5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.util.GestorAutenticacion

class DescargarDocumentosActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewSubtitulo: TextView
    private lateinit var buttonDescargarRecetas: Button
    private lateinit var buttonDescargarResultados: Button
    private lateinit var buttonHistorialDescargas: Button
    private lateinit var buttonVolver: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descargar_documentos)
        
        inicializarVistas()
        configurarEventos()
        mostrarInformacionPaciente()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewSubtitulo = findViewById(R.id.text_view_subtitulo)
        buttonDescargarRecetas = findViewById(R.id.button_descargar_recetas)
        buttonDescargarResultados = findViewById(R.id.button_descargar_resultados)
        buttonHistorialDescargas = findViewById(R.id.button_historial_descargas)
        buttonVolver = findViewById(R.id.button_volver)
    }
    
    private fun configurarEventos() {
        buttonDescargarRecetas.setOnClickListener {
            abrirDescargaRecetas()
        }
        
        buttonDescargarResultados.setOnClickListener {
            abrirDescargaResultados()
        }
        
        buttonHistorialDescargas.setOnClickListener {
            abrirHistorialDescargas()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun mostrarInformacionPaciente() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val paciente = sesion.usuario
            
            textViewTitulo.text = "📄 Descargar Documentos Médicos"
            textViewSubtitulo.text = "Paciente: ${paciente.obtenerNombre()}\n\nSelecciona el tipo de documento que deseas descargar:"
        } else {
            mostrarMensaje("Error: No hay sesión activa")
            finish()
        }
    }
    
    private fun abrirDescargaRecetas() {
        mostrarDialogoRecetas()
    }
    
    private fun abrirDescargaResultados() {
        mostrarDialogoResultados()
    }
    
    private fun abrirHistorialDescargas() {
        mostrarDialogoHistorial()
    }
    
    private fun mostrarDialogoRecetas() {
        val opciones = arrayOf(
            "💊 Recetas Actuales (Últimos 3 meses)",
            "📋 Recetas Anteriores (Histórico completo)",
            "🔄 Recetas Pendientes de Renovación",
            "📧 Enviar todas las recetas por email"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("💊 Descargar Recetas Médicas")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> descargarRecetasActuales()
                    1 -> descargarRecetasAnteriores()
                    2 -> descargarRecetasPendientes()
                    3 -> enviarRecetasPorEmail()
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarDialogoResultados() {
        val opciones = arrayOf(
            "🩸 Análisis de Sangre",
            "📷 Radiografías",
            "💓 Electrocardiogramas",
            "🧬 Estudios Especializados",
            "📊 Todos los Resultados",
            "📧 Enviar todos los resultados por email"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🔬 Descargar Resultados de Exámenes")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> descargarAnalisisSangre()
                    1 -> descargarRadiografias()
                    2 -> descargarElectrocardiogramas()
                    3 -> descargarEstudiosEspecializados()
                    4 -> descargarTodosResultados()
                    5 -> enviarResultadosPorEmail()
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarDialogoHistorial() {
        android.app.AlertDialog.Builder(this)
            .setTitle("📋 Historial de Descargas")
            .setMessage("""
                📅 Últimas descargas realizadas:
                
                • 15/01/2025 - Receta médica (Dr. García)
                • 10/01/2025 - Análisis de sangre completo
                • 05/01/2025 - Radiografía de tórax
                • 28/12/2024 - Receta de antibióticos
                • 20/12/2024 - Resultados de ecografía
                
                📊 Total de documentos descargados: 23
                📧 Documentos enviados por email: 18
                💾 Espacio utilizado: 45.2 MB
            """.trimIndent())
            .setPositiveButton("Ver Detalles") { _, _ ->
                mostrarMensaje("Abriendo historial detallado...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun descargarRecetasActuales() {
        mostrarMensaje("📥 Descargando recetas actuales...")
        simularDescarga("recetas_actuales.pdf")
    }
    
    private fun descargarRecetasAnteriores() {
        mostrarMensaje("📥 Descargando historial de recetas...")
        simularDescarga("recetas_historial.pdf")
    }
    
    private fun descargarRecetasPendientes() {
        mostrarMensaje("📥 Descargando recetas pendientes de renovación...")
        simularDescarga("recetas_pendientes.pdf")
    }
    
    private fun enviarRecetasPorEmail() {
        mostrarMensaje("📧 Enviando recetas por email...")
        mostrarDialogoInformacion(
            "Email Enviado",
            "✅ Todas las recetas han sido enviadas a tu correo electrónico registrado.\n\n📧 Revisa tu bandeja de entrada y carpeta de spam."
        )
    }
    
    private fun descargarAnalisisSangre() {
        mostrarMensaje("📥 Descargando análisis de sangre...")
        simularDescarga("analisis_sangre.pdf")
    }
    
    private fun descargarRadiografias() {
        mostrarMensaje("📥 Descargando radiografías...")
        simularDescarga("radiografias.zip")
    }
    
    private fun descargarElectrocardiogramas() {
        mostrarMensaje("📥 Descargando electrocardiogramas...")
        simularDescarga("electrocardiogramas.pdf")
    }
    
    private fun descargarEstudiosEspecializados() {
        mostrarMensaje("📥 Descargando estudios especializados...")
        simularDescarga("estudios_especializados.pdf")
    }
    
    private fun descargarTodosResultados() {
        mostrarMensaje("📥 Descargando todos los resultados...")
        simularDescarga("todos_resultados.zip")
    }
    
    private fun enviarResultadosPorEmail() {
        mostrarMensaje("📧 Enviando resultados por email...")
        mostrarDialogoInformacion(
            "Email Enviado",
            "✅ Todos los resultados han sido enviados a tu correo electrónico registrado.\n\n📧 Revisa tu bandeja de entrada y carpeta de spam."
        )
    }
    
    private fun simularDescarga(nombreArchivo: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle("📥 Descarga Completada")
            .setMessage("""
                ✅ Archivo descargado exitosamente:
                
                📄 Nombre: $nombreArchivo
                📁 Ubicación: Descargas/BioMonitor/
                📊 Tamaño: 2.3 MB
                ⏰ Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}
                
                📧 También se ha enviado una copia por email
                💾 El archivo se guardó en tu dispositivo
            """.trimIndent())
            .setPositiveButton("Abrir Archivo") { _, _ ->
                mostrarMensaje("Abriendo archivo...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarDialogoInformacion(titulo: String, mensaje: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Entendido") { _, _ -> }
            .show()
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
} 