package com.example.softmedv5.util

import com.example.softmedv5.modelo.Cita
import com.example.softmedv5.modelo.EstadoCita
import com.example.softmedv5.modelo.MedioCita
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class GestorCitas private constructor() {
    
    companion object {
        @Volatile
        private var INSTANCIA: GestorCitas? = null
        
        fun obtenerInstancia(): GestorCitas {
            return INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: GestorCitas().also { INSTANCIA = it }
            }
        }
    }
    
    // Lista de citas (en una aplicación real esto sería una base de datos)
    private val citas = mutableListOf<Cita>()
    
    // Especialidades disponibles
    val especialidadesDisponibles = listOf(
        "Medicina General",
        "Cardiología",
        "Dermatología",
        "Endocrinología",
        "Gastroenterología",
        "Ginecología",
        "Neurología",
        "Oftalmología",
        "Ortopedia",
        "Pediatría",
        "Psiquiatría",
        "Radiología",
        "Traumatología",
        "Urología"
    )
    
    // Horarios disponibles
    val horariosDisponibles = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "14:00", "14:30", "15:00", "15:30",
        "16:00", "16:30", "17:00", "17:30"
    )
    
    fun crearCita(
        pacienteId: String,
        fecha: String,
        hora: String,
        especialidad: String,
        medio: MedioCita,
        opcion: String
    ): ResultadoCita {
        return crearCita(pacienteId, fecha, hora, especialidad, medio, opcion, "", "", "", "", 0)
    }
    
    fun crearCita(
        pacienteId: String,
        fecha: String,
        hora: String,
        especialidad: String,
        medio: MedioCita,
        opcion: String,
        nombrePaciente: String,
        emailPaciente: String,
        telefonoPaciente: String,
        documentoPaciente: String,
        edadPaciente: Int
    ): ResultadoCita {
        try {
            // Validar fecha y hora
            val fechaHora = parsearFechaHora(fecha, hora)
            if (fechaHora.isBefore(LocalDateTime.now())) {
                return ResultadoCita.Error("No se puede crear una cita en el pasado")
            }
            
            // Validar especialidad
            if (!especialidadesDisponibles.contains(especialidad)) {
                return ResultadoCita.Error("Especialidad no válida")
            }
            
            // Validar hora disponible
            if (!horariosDisponibles.contains(hora)) {
                return ResultadoCita.Error("Hora no disponible. Horarios válidos: ${horariosDisponibles.joinToString(", ")}")
            }
            
            // Verificar disponibilidad
            if (!verificarDisponibilidad(fechaHora)) {
                return ResultadoCita.Error("Fecha y hora no disponibles")
            }
            
            // Crear la cita
            val id = generarIdCita()
            val cita = Cita(
                id = id,
                pacienteId = pacienteId,
                fechaHora = fechaHora,
                especialidad = especialidad,
                medio = medio,
                opcion = opcion,
                nombrePaciente = nombrePaciente,
                emailPaciente = emailPaciente,
                telefonoPaciente = telefonoPaciente,
                documentoPaciente = documentoPaciente,
                edadPaciente = edadPaciente
            )
            
            citas.add(cita)
            return ResultadoCita.Exito(cita)
            
        } catch (e: Exception) {
            return ResultadoCita.Error("Error al crear la cita: ${e.message}")
        }
    }
    
    fun obtenerCitasPaciente(pacienteId: String): List<Cita> {
        return citas.filter { it.pacienteId == pacienteId }
            .sortedBy { it.fechaHora }
    }
    
    fun obtenerCitaPorId(id: String): Cita? {
        return citas.find { it.id == id }
    }
    
    fun cancelarCita(id: String): ResultadoCita {
        val cita = obtenerCitaPorId(id)
        if (cita == null) {
            return ResultadoCita.Error("Cita no encontrada")
        }
        
        if (!cita.puedeCancelar()) {
            return ResultadoCita.Error("No se puede cancelar esta cita")
        }
        
        if (!cita.esCitaFutura()) {
            return ResultadoCita.Error("No se puede cancelar una cita pasada")
        }
        
        val citaCancelada = cita.copy(
            estado = EstadoCita.CANCELADA,
            fechaModificacion = LocalDateTime.now()
        )
        
        val index = citas.indexOf(cita)
        citas[index] = citaCancelada
        
        return ResultadoCita.Exito(citaCancelada)
    }
    
    fun reprogramarCita(
        id: String,
        nuevaFecha: String,
        nuevaHora: String
    ): ResultadoCita {
        val cita = obtenerCitaPorId(id)
        if (cita == null) {
            return ResultadoCita.Error("Cita no encontrada")
        }
        
        if (!cita.puedeReprogramar()) {
            return ResultadoCita.Error("No se puede reprogramar esta cita")
        }
        
        try {
            val nuevaFechaHora = parsearFechaHora(nuevaFecha, nuevaHora)
            if (nuevaFechaHora.isBefore(LocalDateTime.now())) {
                return ResultadoCita.Error("No se puede reprogramar a una fecha pasada")
            }
            
            if (!verificarDisponibilidad(nuevaFechaHora)) {
                return ResultadoCita.Error("Nueva fecha y hora no disponibles")
            }
            
            val citaReprogramada = cita.copy(
                fechaHora = nuevaFechaHora,
                estado = EstadoCita.REPROGRAMADA,
                fechaModificacion = LocalDateTime.now()
            )
            
            val index = citas.indexOf(cita)
            citas[index] = citaReprogramada
            
            return ResultadoCita.Exito(citaReprogramada)
            
        } catch (e: Exception) {
            return ResultadoCita.Error("Error al reprogramar la cita: ${e.message}")
        }
    }
    
    fun reprogramarCita(
        id: String,
        nuevaFecha: Date
    ): ResultadoCita {
        val cita = obtenerCitaPorId(id)
        if (cita == null) {
            return ResultadoCita.Error("Cita no encontrada")
        }
        
        if (!cita.puedeReprogramar()) {
            return ResultadoCita.Error("No se puede reprogramar esta cita")
        }
        
        try {
            // Convertir Date a LocalDateTime
            val calendar = Calendar.getInstance()
            calendar.time = nuevaFecha
            
            val nuevaFechaHora = LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, // Calendar.MONTH es 0-based
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
            
            if (nuevaFechaHora.isBefore(LocalDateTime.now())) {
                return ResultadoCita.Error("No se puede reprogramar a una fecha pasada")
            }
            
            if (!verificarDisponibilidad(nuevaFechaHora)) {
                return ResultadoCita.Error("Nueva fecha y hora no disponibles")
            }
            
            val citaReprogramada = cita.copy(
                fechaHora = nuevaFechaHora,
                estado = EstadoCita.REPROGRAMADA,
                fechaModificacion = LocalDateTime.now()
            )
            
            val index = citas.indexOf(cita)
            citas[index] = citaReprogramada
            
            return ResultadoCita.Exito(citaReprogramada)
            
        } catch (e: Exception) {
            return ResultadoCita.Error("Error al reprogramar la cita: ${e.message}")
        }
    }
    
    fun confirmarCita(id: String): ResultadoCita {
        val cita = obtenerCitaPorId(id)
        if (cita == null) {
            return ResultadoCita.Error("Cita no encontrada")
        }
        
        if (cita.estado != EstadoCita.PENDIENTE) {
            return ResultadoCita.Error("La cita ya no está pendiente")
        }
        
        val citaConfirmada = cita.copy(
            estado = EstadoCita.CONFIRMADA,
            fechaModificacion = LocalDateTime.now()
        )
        
        val index = citas.indexOf(cita)
        citas[index] = citaConfirmada
        
        return ResultadoCita.Exito(citaConfirmada)
    }
    
    private fun parsearFechaHora(fecha: String, hora: String): LocalDateTime {
        val fechaParts = fecha.split("/")
        val horaParts = hora.split(":")
        
        val dia = fechaParts[0].toInt()
        val mes = fechaParts[1].toInt()
        val anio = fechaParts[2].toInt()
        val horas = horaParts[0].toInt()
        val minutos = horaParts[1].toInt()
        
        return LocalDateTime.of(anio, mes, dia, horas, minutos)
    }
    
    private fun verificarDisponibilidad(fechaHora: LocalDateTime): Boolean {
        // Verificar que no haya otra cita en el mismo horario
        return !citas.any { 
            it.fechaHora == fechaHora && 
            it.estado != EstadoCita.CANCELADA 
        }
    }
    
    private fun generarIdCita(): String {
        return "CITA-${System.currentTimeMillis()}"
    }
    
    fun obtenerCitasProximas(pacienteId: String): List<Cita> {
        val ahora = LocalDateTime.now()
        return obtenerCitasPaciente(pacienteId)
            .filter { it.fechaHora.isAfter(ahora) && it.estado != EstadoCita.CANCELADA }
            .sortedBy { it.fechaHora }
    }
    
    fun obtenerCitasPasadas(pacienteId: String): List<Cita> {
        val ahora = LocalDateTime.now()
        return obtenerCitasPaciente(pacienteId)
            .filter { it.fechaHora.isBefore(ahora) }
            .sortedByDescending { it.fechaHora }
    }
    
    // Métodos para gestión administrativa
    fun obtenerTodasLasCitas(): List<Cita> {
        return citas.sortedBy { it.fechaHora }
    }
    
    fun obtenerCitasPorEstado(estado: EstadoCita): List<Cita> {
        return citas.filter { it.estado == estado }
            .sortedBy { it.fechaHora }
    }
    
    fun obtenerCitasPendientes(): List<Cita> {
        return obtenerCitasPorEstado(EstadoCita.PENDIENTE)
    }
    
    fun obtenerCitasConfirmadas(): List<Cita> {
        return obtenerCitasPorEstado(EstadoCita.CONFIRMADA)
    }
    
    fun obtenerCitasCanceladas(): List<Cita> {
        return obtenerCitasPorEstado(EstadoCita.CANCELADA)
    }
    
    // Método para crear citas de demostración
    fun crearCitasDemostracion() {
        if (citas.isEmpty()) {
            // Crear algunas citas de ejemplo para demostración
            val fechaActual = LocalDateTime.now()
            
            // Cita pendiente para mañana
            val cita1 = Cita(
                id = "CITA-DEMO-1",
                pacienteId = "PACIENTE-1",
                fechaHora = fechaActual.plusDays(1).withHour(9).withMinute(0),
                especialidad = "Medicina General",
                medio = MedioCita.PRESENCIAL,
                opcion = "Consulta regular",
                estado = EstadoCita.PENDIENTE,
                nombrePaciente = "María González López",
                emailPaciente = "maria.gonzalez@email.com",
                telefonoPaciente = "+34 612 345 678",
                documentoPaciente = "DNI: 12345678A",
                edadPaciente = 35
            )
            
            // Cita confirmada para pasado mañana
            val cita2 = Cita(
                id = "CITA-DEMO-2",
                pacienteId = "PACIENTE-2",
                fechaHora = fechaActual.plusDays(2).withHour(14).withMinute(30),
                especialidad = "Cardiología",
                medio = MedioCita.VIRTUAL,
                opcion = "Consulta de seguimiento",
                estado = EstadoCita.CONFIRMADA,
                nombrePaciente = "Carlos Rodríguez Martín",
                emailPaciente = "carlos.rodriguez@email.com",
                telefonoPaciente = "+34 698 765 432",
                documentoPaciente = "DNI: 87654321B",
                edadPaciente = 52
            )
            
            // Cita pendiente para la próxima semana
            val cita3 = Cita(
                id = "CITA-DEMO-3",
                pacienteId = "PACIENTE-3",
                fechaHora = fechaActual.plusDays(7).withHour(11).withMinute(0),
                especialidad = "Dermatología",
                medio = MedioCita.PRESENCIAL,
                opcion = "Revisión de piel",
                estado = EstadoCita.PENDIENTE,
                nombrePaciente = "Ana Fernández Jiménez",
                emailPaciente = "ana.fernandez@email.com",
                telefonoPaciente = "+34 655 123 456",
                documentoPaciente = "DNI: 11223344C",
                edadPaciente = 28
            )
            
            // Cita pendiente para la próxima semana
            val cita4 = Cita(
                id = "CITA-DEMO-4",
                pacienteId = "PACIENTE-4",
                fechaHora = fechaActual.plusDays(3).withHour(16).withMinute(0),
                especialidad = "Pediatría",
                medio = MedioCita.PRESENCIAL,
                opcion = "Control de crecimiento",
                estado = EstadoCita.PENDIENTE,
                nombrePaciente = "Lucas Pérez García",
                emailPaciente = "lucas.perez@email.com",
                telefonoPaciente = "+34 677 888 999",
                documentoPaciente = "DNI: 99887766D",
                edadPaciente = 8
            )
            
            citas.addAll(listOf(cita1, cita2, cita3, cita4))
        }
    }
}

sealed class ResultadoCita {
    data class Exito(val cita: Cita) : ResultadoCita()
    data class Error(val mensaje: String) : ResultadoCita()
} 