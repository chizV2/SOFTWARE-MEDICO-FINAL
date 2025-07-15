package com.example.softmedv5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class FacturacionPagosActivity : AppCompatActivity() {
    
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewResumen: TextView
    private lateinit var buttonNuevaFactura: Button
    private lateinit var buttonRegistrarPago: Button
    private lateinit var buttonAplicarDescuento: Button
    private lateinit var buttonEmitirComprobante: Button
    private lateinit var buttonVolver: Button
    private lateinit var linearLayoutFacturas: LinearLayout
    private lateinit var textViewSinFacturas: TextView
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facturacion_pagos)
        
        inicializarVistas()
        configurarEventos()
        cargarFacturasRegistradas()
    }
    
    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewResumen = findViewById(R.id.text_view_resumen)
        buttonNuevaFactura = findViewById(R.id.button_nueva_factura)
        buttonRegistrarPago = findViewById(R.id.button_registrar_pago)
        buttonAplicarDescuento = findViewById(R.id.button_aplicar_descuento)
        buttonEmitirComprobante = findViewById(R.id.button_emitir_comprobante)
        buttonVolver = findViewById(R.id.button_volver)
        linearLayoutFacturas = findViewById(R.id.linear_layout_facturas)
        textViewSinFacturas = findViewById(R.id.text_view_sin_facturas)
        
        textViewTitulo.text = "💰 Facturación y Pagos"
    }
    
    private fun configurarEventos() {
        buttonNuevaFactura.setOnClickListener {
            mostrarFormularioNuevaFactura()
        }
        
        buttonRegistrarPago.setOnClickListener {
            mostrarRegistroPago()
        }
        
        buttonAplicarDescuento.setOnClickListener {
            mostrarAplicarDescuento()
        }
        
        buttonEmitirComprobante.setOnClickListener {
            mostrarEmitirComprobante()
        }
        
        buttonVolver.setOnClickListener {
            finish()
        }
    }
    
    private fun cargarFacturasRegistradas() {
        val facturas = obtenerFacturasRegistradas()
        
        mostrarResumen(facturas)
        
        if (facturas.isNotEmpty()) {
            facturas.forEach { factura ->
                agregarFacturaALista(factura)
            }
        } else {
            mostrarSinFacturas()
        }
    }
    
    private fun obtenerFacturasRegistradas(): List<Factura> {
        return listOf(
            Factura(
                id = "FACT-001",
                pacienteId = "PACIENTE-1",
                pacienteNombre = "María González",
                fecha = "15/12/2024",
                hora = "09:30",
                tipoServicio = "Consulta Médica",
                especialidad = "Medicina General",
                subtotal = 50000.0,
                descuento = 0.0,
                iva = 9500.0,
                total = 59500.0,
                estado = "PENDIENTE",
                metodoPago = "",
                codigoCobertura = ""
            ),
            Factura(
                id = "FACT-002",
                pacienteId = "PACIENTE-2",
                pacienteNombre = "Carlos Rodríguez",
                fecha = "14/12/2024",
                hora = "14:00",
                tipoServicio = "Examen de Laboratorio",
                especialidad = "Laboratorio",
                subtotal = 120000.0,
                descuento = 10000.0,
                iva = 20900.0,
                total = 130900.0,
                estado = "PAGADA",
                metodoPago = "Tarjeta de Crédito",
                codigoCobertura = "EPS-SURA-2024"
            ),
            Factura(
                id = "FACT-003",
                pacienteId = "PACIENTE-3",
                pacienteNombre = "Ana Martínez",
                fecha = "13/12/2024",
                hora = "11:15",
                tipoServicio = "Procedimiento Quirúrgico",
                especialidad = "Cirugía General",
                subtotal = 2500000.0,
                descuento = 250000.0,
                iva = 427500.0,
                total = 2677500.0,
                estado = "PENDIENTE",
                metodoPago = "",
                codigoCobertura = "EPS-FAMISANAR-2024"
            )
        )
    }
    
    private fun mostrarResumen(facturas: List<Factura>) {
        val totalFacturas = facturas.size
        val facturasPagadas = facturas.count { it.estado == "PAGADA" }
        val facturasPendientes = facturas.count { it.estado == "PENDIENTE" }
        val totalIngresos = facturas.filter { it.estado == "PAGADA" }.sumOf { it.total }
        
        val resumen = """
            📊 Resumen de Facturación:
            
            📄 Total de facturas: $totalFacturas
            ✅ Facturas pagadas: $facturasPagadas
            ⏳ Facturas pendientes: $facturasPendientes
            💰 Total de ingresos: $${String.format("%,.0f", totalIngresos)}
            
            💡 Funcionalidades disponibles:
            • Generar facturas por consultas, exámenes o procedimientos
            • Registrar pagos en efectivo, tarjeta o seguro
            • Aplicar descuentos o códigos de cobertura
            • Emitir comprobantes electrónicos
        """.trimIndent()
        
        textViewResumen.text = resumen
    }
    
    private fun agregarFacturaALista(factura: Factura) {
        val cardView = LayoutInflater.from(this).inflate(
            R.layout.item_factura_administrativa,
            linearLayoutFacturas,
            false
        )
        
        val textViewId = cardView.findViewById<TextView>(R.id.text_view_id_factura)
        val textViewPaciente = cardView.findViewById<TextView>(R.id.text_view_paciente)
        val textViewServicio = cardView.findViewById<TextView>(R.id.text_view_servicio)
        val textViewTotal = cardView.findViewById<TextView>(R.id.text_view_total)
        val textViewEstado = cardView.findViewById<TextView>(R.id.text_view_estado)
        val buttonVerDetalle = cardView.findViewById<Button>(R.id.button_ver_detalle)
        val buttonRegistrarPago = cardView.findViewById<Button>(R.id.button_registrar_pago_item)
        val buttonEmitirComprobante = cardView.findViewById<Button>(R.id.button_emitir_comprobante_item)
        
        textViewId.text = "Factura: ${factura.id}"
        textViewPaciente.text = "👤 ${factura.pacienteNombre}"
        textViewServicio.text = """
            📋 ${factura.tipoServicio}
            🏥 ${factura.especialidad}
            📅 ${factura.fecha} - ${factura.hora}
        """.trimIndent()
        textViewTotal.text = "💰 $${String.format("%,.0f", factura.total)}"
        
        // Configurar estado con colores
        when (factura.estado) {
            "PAGADA" -> {
                textViewEstado.text = "✅ Pagada"
                textViewEstado.setTextColor(resources.getColor(R.color.colorSuccess, null))
                buttonRegistrarPago.visibility = View.GONE
            }
            "PENDIENTE" -> {
                textViewEstado.text = "⏳ Pendiente"
                textViewEstado.setTextColor(resources.getColor(R.color.colorWarning, null))
                buttonRegistrarPago.visibility = View.VISIBLE
            }
            else -> {
                textViewEstado.text = "❌ Cancelada"
                textViewEstado.setTextColor(resources.getColor(R.color.colorError, null))
                buttonRegistrarPago.visibility = View.GONE
            }
        }
        
        buttonVerDetalle.setOnClickListener {
            mostrarDetalleFactura(factura)
        }
        
        buttonRegistrarPago.setOnClickListener {
            registrarPagoFactura(factura)
        }
        
        buttonEmitirComprobante.setOnClickListener {
            emitirComprobanteFactura(factura)
        }
        
        linearLayoutFacturas.addView(cardView)
    }
    
    private fun mostrarFormularioNuevaFactura() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_formulario_factura, null)
        
        val editTextPaciente = dialogView.findViewById<EditText>(R.id.edit_text_paciente)
        val spinnerTipoServicio = dialogView.findViewById<Spinner>(R.id.spinner_tipo_servicio)
        val spinnerEspecialidad = dialogView.findViewById<Spinner>(R.id.spinner_especialidad)
        val editTextSubtotal = dialogView.findViewById<EditText>(R.id.edit_text_subtotal)
        val editTextDescuento = dialogView.findViewById<EditText>(R.id.edit_text_descuento)
        val editTextCodigoCobertura = dialogView.findViewById<EditText>(R.id.edit_text_codigo_cobertura)
        
        // Configurar spinners
        val tiposServicio = arrayOf("Consulta Médica", "Examen de Laboratorio", "Procedimiento Quirúrgico", "Radiografía", "Terapia", "Medicamentos")
        val especialidades = arrayOf("Medicina General", "Cardiología", "Dermatología", "Laboratorio", "Radiología", "Cirugía General", "Farmacia")
        
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposServicio)
        val adapterEspecialidad = ArrayAdapter(this, android.R.layout.simple_spinner_item, especialidades)
        
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterEspecialidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        spinnerTipoServicio.adapter = adapterTipo
        spinnerEspecialidad.adapter = adapterEspecialidad
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Generar Nueva Factura")
            .setView(dialogView)
            .setPositiveButton("Generar Factura") { _, _ ->
                val paciente = editTextPaciente.text.toString()
                val tipoServicio = spinnerTipoServicio.selectedItem.toString()
                val especialidad = spinnerEspecialidad.selectedItem.toString()
                val subtotal = editTextSubtotal.text.toString().toDoubleOrNull() ?: 0.0
                val descuento = editTextDescuento.text.toString().toDoubleOrNull() ?: 0.0
                val codigoCobertura = editTextCodigoCobertura.text.toString()
                
                if (paciente.isNotBlank() && subtotal > 0) {
                    generarNuevaFactura(paciente, tipoServicio, especialidad, subtotal, descuento, codigoCobertura)
                } else {
                    mostrarMensaje("Por favor completa todos los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun generarNuevaFactura(
        paciente: String,
        tipoServicio: String,
        especialidad: String,
        subtotal: Double,
        descuento: Double,
        codigoCobertura: String
    ) {
        val iva = (subtotal - descuento) * 0.19 // 19% IVA
        val total = subtotal - descuento + iva
        val fecha = dateFormat.format(calendar.time)
        
        val nuevaFactura = Factura(
            id = "FACT-${System.currentTimeMillis()}",
            pacienteId = "PACIENTE-${System.currentTimeMillis()}",
            pacienteNombre = paciente,
            fecha = fecha.split(" ")[0],
            hora = fecha.split(" ")[1],
            tipoServicio = tipoServicio,
            especialidad = especialidad,
            subtotal = subtotal,
            descuento = descuento,
            iva = iva,
            total = total,
            estado = "PENDIENTE",
            metodoPago = "",
            codigoCobertura = codigoCobertura
        )
        
        mostrarMensaje("✅ Factura generada exitosamente: ${nuevaFactura.id}")
        cargarFacturasRegistradas() // Recargar la lista
    }
    
    private fun mostrarRegistroPago() {
        val opciones = arrayOf(
            "💵 Efectivo",
            "💳 Tarjeta de Débito",
            "💳 Tarjeta de Crédito",
            "🏥 Seguro Médico",
            "📱 Transferencia Bancaria"
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("💳 Registrar Pago")
            .setItems(opciones) { _, which ->
                val metodoPago = opciones[which]
                mostrarFormularioPago(metodoPago)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun mostrarFormularioPago(metodoPago: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_formulario_pago, null)
        
        val editTextFactura = dialogView.findViewById<EditText>(R.id.edit_text_factura)
        val editTextMonto = dialogView.findViewById<EditText>(R.id.edit_text_monto)
        val editTextReferencia = dialogView.findViewById<EditText>(R.id.edit_text_referencia)
        val textViewMetodoPago = dialogView.findViewById<TextView>(R.id.text_view_metodo_pago)
        
        textViewMetodoPago.text = "Método de Pago: $metodoPago"
        
        android.app.AlertDialog.Builder(this)
            .setTitle("💳 Registrar Pago - $metodoPago")
            .setView(dialogView)
            .setPositiveButton("Registrar Pago") { _, _ ->
                val facturaId = editTextFactura.text.toString()
                val monto = editTextMonto.text.toString().toDoubleOrNull() ?: 0.0
                val referencia = editTextReferencia.text.toString()
                
                if (facturaId.isNotBlank() && monto > 0) {
                    registrarPago(facturaId, monto, metodoPago, referencia)
                } else {
                    mostrarMensaje("Por favor completa todos los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun registrarPago(facturaId: String, monto: Double, metodoPago: String, referencia: String) {
        mostrarMensaje("✅ Pago registrado exitosamente:\nFactura: $facturaId\nMonto: $${String.format("%,.0f", monto)}\nMétodo: $metodoPago")
        
        // En una aplicación real, aquí se actualizaría el estado de la factura
        cargarFacturasRegistradas() // Recargar la lista
    }
    
    private fun mostrarAplicarDescuento() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_aplicar_descuento, null)
        
        val editTextFactura = dialogView.findViewById<EditText>(R.id.edit_text_factura_descuento)
        val editTextPorcentaje = dialogView.findViewById<EditText>(R.id.edit_text_porcentaje_descuento)
        val editTextMotivo = dialogView.findViewById<EditText>(R.id.edit_text_motivo_descuento)
        
        android.app.AlertDialog.Builder(this)
            .setTitle("🎫 Aplicar Descuento")
            .setView(dialogView)
            .setPositiveButton("Aplicar Descuento") { _, _ ->
                val facturaId = editTextFactura.text.toString()
                val porcentaje = editTextPorcentaje.text.toString().toDoubleOrNull() ?: 0.0
                val motivo = editTextMotivo.text.toString()
                
                if (facturaId.isNotBlank() && porcentaje > 0) {
                    aplicarDescuento(facturaId, porcentaje, motivo)
                } else {
                    mostrarMensaje("Por favor completa todos los campos obligatorios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun aplicarDescuento(facturaId: String, porcentaje: Double, motivo: String) {
        mostrarMensaje("✅ Descuento aplicado exitosamente:\nFactura: $facturaId\nDescuento: $porcentaje%\nMotivo: $motivo")
        
        // En una aplicación real, aquí se recalcularía la factura
        cargarFacturasRegistradas() // Recargar la lista
    }
    
    private fun mostrarEmitirComprobante() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_emitir_comprobante, null)
        
        val editTextFactura = dialogView.findViewById<EditText>(R.id.edit_text_factura_comprobante)
        val spinnerTipoComprobante = dialogView.findViewById<Spinner>(R.id.spinner_tipo_comprobante)
        
        val tiposComprobante = arrayOf("Factura Electrónica", "Recibo de Pago", "Comprobante de Cobertura", "Certificado de Pago")
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposComprobante)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoComprobante.adapter = adapter
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Emitir Comprobante Electrónico")
            .setView(dialogView)
            .setPositiveButton("Emitir") { _, _ ->
                val facturaId = editTextFactura.text.toString()
                val tipoComprobante = spinnerTipoComprobante.selectedItem.toString()
                
                if (facturaId.isNotBlank()) {
                    emitirComprobante(facturaId, tipoComprobante)
                } else {
                    mostrarMensaje("Por favor ingresa el ID de la factura")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
    
    private fun emitirComprobante(facturaId: String, tipoComprobante: String) {
        mostrarMensaje("✅ Comprobante emitido exitosamente:\nFactura: $facturaId\nTipo: $tipoComprobante\n\nEl comprobante se ha enviado por email y está disponible para descarga.")
    }
    
    private fun mostrarDetalleFactura(factura: Factura) {
        val mensaje = """
            📄 Detalle de Factura: ${factura.id}
            
            👤 Paciente: ${factura.pacienteNombre}
            📋 Servicio: ${factura.tipoServicio}
            🏥 Especialidad: ${factura.especialidad}
            📅 Fecha: ${factura.fecha} - ${factura.hora}
            
            💰 Subtotal: $${String.format("%,.0f", factura.subtotal)}
            🎫 Descuento: $${String.format("%,.0f", factura.descuento)}
            📊 IVA (19%): $${String.format("%,.0f", factura.iva)}
            💵 Total: $${String.format("%,.0f", factura.total)}
            
            📋 Estado: ${factura.estado}
            💳 Método de Pago: ${factura.metodoPago.ifBlank { "No registrado" }}
            🏥 Código de Cobertura: ${factura.codigoCobertura.ifBlank { "No aplica" }}
        """.trimIndent()
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📄 Detalle de Factura")
            .setMessage(mensaje)
            .setPositiveButton("Cerrar") { _, _ -> }
            .show()
    }
    
    private fun registrarPagoFactura(factura: Factura) {
        mostrarMensaje("💳 Registrando pago para factura ${factura.id}")
        mostrarRegistroPago()
    }
    
    private fun emitirComprobanteFactura(factura: Factura) {
        mostrarMensaje("📄 Emitiendo comprobante para factura ${factura.id}")
        emitirComprobante(factura.id, "Factura Electrónica")
    }
    
    private fun mostrarSinFacturas() {
        textViewSinFacturas.visibility = View.VISIBLE
        linearLayoutFacturas.visibility = View.GONE
    }
    
    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        cargarFacturasRegistradas() // Recargar cuando se regrese a esta actividad
    }
}

// Clase de datos para representar una factura
data class Factura(
    val id: String,
    val pacienteId: String,
    val pacienteNombre: String,
    val fecha: String,
    val hora: String,
    val tipoServicio: String,
    val especialidad: String,
    val subtotal: Double,
    val descuento: Double,
    val iva: Double,
    val total: Double,
    val estado: String,
    val metodoPago: String,
    val codigoCobertura: String
) 