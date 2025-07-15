package com.example.softmedv5

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RevisionResultadosActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonVerResultados: Button
    private lateinit var buttonResultadosPendientes: Button
    private lateinit var buttonResultadosAnormales: Button
    private lateinit var buttonCompararResultados: Button
    private lateinit var buttonGenerarReporte: Button
    private lateinit var buttonVolver: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revision_resultados)
        
        inicializarVistas()
        configurarEventos()
        mostrarResumen()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonVerResultados = findViewById(R.id.button_ver_resultados)
        buttonResultadosPendientes = findViewById(R.id.button_resultados_pendientes)
        buttonResultadosAnormales = findViewById(R.id.button_resultados_anormales)
        buttonCompararResultados = findViewById(R.id.button_comparar_resultados)
        buttonGenerarReporte = findViewById(R.id.button_generar_reporte)
        buttonVolver = findViewById(R.id.button_volver)
        
        textViewTitulo.text = "📊 Revisión de Resultados"
    }
    
    private fun configurarEventos() {
        buttonVerResultados.setOnClickListener {
            mostrarResultados()
        }
        
        buttonResultadosPendientes.setOnClickListener {
            mostrarResultadosPendientes()
        }
        
        buttonResultadosAnormales.setOnClickListener {
            mostrarResultadosAnormales()
        }
        
        buttonCompararResultados.setOnClickListener {
            mostrarComparacionResultados()
        }
        
        buttonGenerarReporte.setOnClickListener {
            generarReporteResultados()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun mostrarResumen() {
        val resumen = """
            📊 Resumen de Resultados Médicos:
            
            📋 Total de resultados: 25
            ⏳ Pendientes de revisión: 8
            ⚠️ Resultados anormales: 3
            ✅ Revisados hoy: 12
            📈 Comparaciones disponibles: 15
            
            💡 Funcionalidades disponibles:
            • Ver todos los resultados
            • Resultados pendientes de revisión
            • Resultados con valores anormales
            • Comparar resultados históricos
            • Generar reportes de resultados
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun mostrarResultados() {
        val mensaje = """
            📊 Resultados Médicos Disponibles:
            
            🩺 Hemograma Completo:
            • Paciente: María González
            • Fecha: 15/12/2024
            • Estado: ✅ Normal
            • Valores: Hemoglobina 14.2 g/dL, Leucocitos 7,500/μL
            
            🩺 Química Sanguínea:
            • Paciente: Carlos Rodríguez
            • Fecha: 14/12/2024
            • Estado: ⚠️ Anormal
            • Valores: Glucosa 180 mg/dL (Elevada)
            
            🩺 Radiografía de Tórax:
            • Paciente: Ana Martínez
            • Fecha: 13/12/2024
            • Estado: ✅ Normal
            • Hallazgos: Sin alteraciones significativas
            
            🩺 Electrocardiograma:
            • Paciente: Juan Pérez
            • Fecha: 12/12/2024
            • Estado: ⚠️ Anormal
            • Hallazgos: Arritmia sinusal
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Resultados Médicos")
            .setMessage(mensaje)
            .setPositiveButton("Ver Detalles") { _, _ ->
                mostrarMensaje("📊 Abriendo detalles de resultados...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarResultadosPendientes() {
        val mensaje = """
            ⏳ Resultados Pendientes de Revisión:
            
            🩺 Resonancia Magnética:
            • Paciente: Laura Sánchez
            • Fecha: 15/12/2024
            • Prioridad: Alta
            • Motivo: Evaluación de dolor lumbar
            
            🩺 Tomografía Computarizada:
            • Paciente: Pedro López
            • Fecha: 14/12/2024
            • Prioridad: Normal
            • Motivo: Control post-operatorio
            
            🩺 Biopsia:
            • Paciente: Carmen Ruiz
            • Fecha: 13/12/2024
            • Prioridad: Crítica
            • Motivo: Evaluación de masa mamaria
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("⏳ Resultados Pendientes")
            .setMessage(mensaje)
            .setPositiveButton("Revisar") { _, _ ->
                mostrarMensaje("🔍 Iniciando revisión de resultados...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarResultadosAnormales() {
        val mensaje = """
            ⚠️ Resultados con Valores Anormales:
            
            🩺 Química Sanguínea - Carlos Rodríguez:
            • Glucosa: 180 mg/dL (Normal: 70-100 mg/dL)
            • Colesterol Total: 280 mg/dL (Normal: <200 mg/dL)
            • Triglicéridos: 350 mg/dL (Normal: <150 mg/dL)
            • Recomendación: Evaluar diabetes y dislipidemia
            
            🩺 Electrocardiograma - Juan Pérez:
            • Frecuencia cardíaca: 110 lpm (Normal: 60-100 lpm)
            • Arritmia sinusal detectada
            • Recomendación: Evaluación cardiológica
            
            🩺 Hemograma - María González:
            • Hemoglobina: 10.5 g/dL (Normal: 12-16 g/dL)
            • Anemia leve detectada
            • Recomendación: Suplementación de hierro
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("⚠️ Resultados Anormales")
            .setMessage(mensaje)
            .setPositiveButton("Generar Alertas") { _, _ ->
                mostrarMensaje("🚨 Generando alertas para pacientes...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun mostrarComparacionResultados() {
        val mensaje = """
            📈 Comparación de Resultados Históricos:
            
            🩺 María González - Hemograma:
            • Actual (15/12/2024): Hemoglobina 10.5 g/dL
            • Anterior (01/12/2024): Hemoglobina 11.2 g/dL
            • Tendencia: ⬇️ Disminución
            • Recomendación: Continuar suplementación
            
            🩺 Carlos Rodríguez - Glucosa:
            • Actual (14/12/2024): 180 mg/dL
            • Anterior (01/12/2024): 165 mg/dL
            • Tendencia: ⬆️ Aumento
            • Recomendación: Ajustar medicación
            
            🩺 Juan Pérez - Presión Arterial:
            • Actual (12/12/2024): 150/95 mmHg
            • Anterior (01/12/2024): 145/90 mmHg
            • Tendencia: ⬆️ Aumento
            • Recomendación: Revisar tratamiento
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📈 Comparación de Resultados")
            .setMessage(mensaje)
            .setPositiveButton("Generar Gráfico") { _, _ ->
                mostrarMensaje("📊 Generando gráfico de tendencias...")
            }
            .setNegativeButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun generarReporteResultados() {
        val mensaje = """
            📋 Generar Reporte de Resultados:
            
            Selecciona el tipo de reporte:
            
            📊 Reporte General:
            • Todos los resultados del período
            • Estadísticas por especialidad
            • Tendencias temporales
            
            ⚠️ Reporte de Anomalías:
            • Solo resultados anormales
            • Alertas y recomendaciones
            • Seguimiento requerido
            
            📈 Reporte de Comparación:
            • Evolución de pacientes
            • Comparación con valores anteriores
            • Análisis de tendencias
            
            📅 Reporte por Fecha:
            • Resultados de fechas específicas
            • Filtros por especialidad
            • Resumen ejecutivo
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📋 Generar Reporte")
            .setMessage(mensaje)
            .setPositiveButton("Generar") { _, _ ->
                mostrarMensaje("📋 Generando reporte de resultados...")
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
} 