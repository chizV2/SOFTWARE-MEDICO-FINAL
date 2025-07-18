package com.example.softmedv5.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

/**
 * Helper simple de base de datos SQLite sin Room
 * Para evitar problemas de compatibilidad con KAPT/KSP
 */
class SimpleDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "biomonitor.db"
        private const val DATABASE_VERSION = 12

        // Tabla Usuarios
        private const val TABLE_USUARIOS = "usuarios"
        private const val COLUMN_USUARIO_ID = "id"
        private const val COLUMN_USUARIO_EMAIL = "email"
        private const val COLUMN_USUARIO_CONTRASENA = "contrasena"
        private const val COLUMN_USUARIO_NOMBRE = "nombre_completo"
        private const val COLUMN_USUARIO_ROL = "rol"
        private const val COLUMN_USUARIO_FECHA_REGISTRO = "fecha_registro"
        private const val COLUMN_USUARIO_ACTIVO = "activo"

        // Tabla Información de Contacto
        private const val TABLE_INFORMACION_CONTACTO = "informacion_contacto"
        private const val COLUMN_CONTACTO_ID = "id"
        private const val COLUMN_CONTACTO_USUARIO_ID = "usuario_id"
        private const val COLUMN_CONTACTO_TELEFONO = "telefono"
        private const val COLUMN_CONTACTO_DIRECCION = "direccion"
        private const val COLUMN_CONTACTO_CIUDAD = "ciudad"
        private const val COLUMN_CONTACTO_CODIGO_POSTAL = "codigo_postal"
        private const val COLUMN_CONTACTO_FECHA_NACIMIENTO = "fecha_nacimiento"
        private const val COLUMN_CONTACTO_GENERO = "genero"
        private const val COLUMN_CONTACTO_DOCUMENTO_IDENTIDAD = "documento_identidad"
        private const val COLUMN_CONTACTO_TIPO_DOCUMENTO = "tipo_documento"
        private const val COLUMN_CONTACTO_EMERGENCIA_CONTACTO = "emergencia_contacto"
        private const val COLUMN_CONTACTO_EMERGENCIA_TELEFONO = "emergencia_telefono"
        private const val COLUMN_CONTACTO_FECHA_ACTUALIZACION = "fecha_actualizacion"

        // Tabla Pacientes
        private const val TABLE_PACIENTES = "pacientes"
        private const val COLUMN_PACIENTE_ID = "id"
        private const val COLUMN_PACIENTE_NOMBRE = "nombre"
        private const val COLUMN_PACIENTE_APELLIDOS = "apellidos"
        private const val COLUMN_PACIENTE_FECHA_NACIMIENTO = "fecha_nacimiento"
        private const val COLUMN_PACIENTE_GENERO = "genero"
        private const val COLUMN_PACIENTE_DOCUMENTO = "documento"
        private const val COLUMN_PACIENTE_TELEFONO = "telefono"
        private const val COLUMN_PACIENTE_EMAIL = "email"
        private const val COLUMN_PACIENTE_DIRECCION = "direccion"
        private const val COLUMN_PACIENTE_FECHA_REGISTRO = "fecha_registro"
        private const val COLUMN_PACIENTE_ACTIVO = "activo"

        // Tabla Mensajes
        private const val TABLE_MENSAJES = "mensajes"
        private const val COLUMN_MENSAJE_ID = "id"
        private const val COLUMN_MENSAJE_REMITENTE_ID = "remitente_id"
        private const val COLUMN_MENSAJE_REMITENTE_NOMBRE = "remitente_nombre"
        private const val COLUMN_MENSAJE_REMITENTE_ROL = "remitente_rol"
        private const val COLUMN_MENSAJE_DESTINATARIO_ID = "destinatario_id"
        private const val COLUMN_MENSAJE_DESTINATARIO_NOMBRE = "destinatario_nombre"
        private const val COLUMN_MENSAJE_DESTINATARIO_ROL = "destinatario_rol"
        private const val COLUMN_MENSAJE_ASUNTO = "asunto"
        private const val COLUMN_MENSAJE_CONTENIDO = "contenido"
        private const val COLUMN_MENSAJE_FECHA_ENVIO = "fecha_envio"
        private const val COLUMN_MENSAJE_LEIDO = "leido"
        private const val COLUMN_MENSAJE_ACTIVO = "activo"

        // Tabla Diagnósticos
        private const val TABLE_DIAGNOSTICOS = "diagnosticos"
        private const val COLUMN_DIAGNOSTICO_ID = "id"
        private const val COLUMN_DIAGNOSTICO_PACIENTE_ID = "paciente_id"
        private const val COLUMN_DIAGNOSTICO_MEDICO_ID = "medico_id"
        private const val COLUMN_DIAGNOSTICO_FECHA = "fecha"
        private const val COLUMN_DIAGNOSTICO_SINTOMAS = "sintomas"
        private const val COLUMN_DIAGNOSTICO_EXAMEN_FISICO = "examen_fisico"
        private const val COLUMN_DIAGNOSTICO_DIAGNOSTICO = "diagnostico"
        private const val COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS = "diagnosticos_secundarios"
        private const val COLUMN_DIAGNOSTICO_TRATAMIENTO = "tratamiento"
        private const val COLUMN_DIAGNOSTICO_MEDICAMENTOS = "medicamentos"
        private const val COLUMN_DIAGNOSTICO_RECOMENDACIONES = "recomendaciones"
        private const val COLUMN_DIAGNOSTICO_PROXIMA_CITA = "proxima_cita"
        private const val COLUMN_DIAGNOSTICO_ESTADO = "estado"

        // Tabla Datos de Pacientes
        private const val TABLE_DATOS_PACIENTES = "datos_pacientes"
        private const val COLUMN_DATOS_PACIENTE_ID = "id"
        private const val COLUMN_DATOS_PACIENTE_USUARIO_ID = "usuario_id"
        private const val COLUMN_DATOS_PACIENTE_SEGURO = "seguro"
        private const val COLUMN_DATOS_PACIENTE_NUMERO_SEGURO = "numero_seguro"
        private const val COLUMN_DATOS_PACIENTE_FECHA_ACTUALIZACION = "fecha_actualizacion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val createUsuariosTable = """
                CREATE TABLE $TABLE_USUARIOS (
                    $COLUMN_USUARIO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_USUARIO_EMAIL TEXT UNIQUE NOT NULL,
                    $COLUMN_USUARIO_CONTRASENA TEXT NOT NULL,
                    $COLUMN_USUARIO_NOMBRE TEXT NOT NULL,
                    $COLUMN_USUARIO_ROL TEXT NOT NULL,
                    $COLUMN_USUARIO_FECHA_REGISTRO INTEGER NOT NULL,
                    $COLUMN_USUARIO_ACTIVO INTEGER DEFAULT 1
                )
            """.trimIndent()

            val createPacientesTable = """
                CREATE TABLE $TABLE_PACIENTES (
                    $COLUMN_PACIENTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_PACIENTE_NOMBRE TEXT NOT NULL,
                    $COLUMN_PACIENTE_APELLIDOS TEXT NOT NULL,
                    $COLUMN_PACIENTE_FECHA_NACIMIENTO TEXT NOT NULL,
                    $COLUMN_PACIENTE_GENERO TEXT NOT NULL,
                    $COLUMN_PACIENTE_DOCUMENTO TEXT NOT NULL,
                    $COLUMN_PACIENTE_TELEFONO TEXT NOT NULL,
                    $COLUMN_PACIENTE_EMAIL TEXT NOT NULL,
                    $COLUMN_PACIENTE_DIRECCION TEXT NOT NULL,
                    $COLUMN_PACIENTE_FECHA_REGISTRO INTEGER NOT NULL,
                    $COLUMN_PACIENTE_ACTIVO INTEGER DEFAULT 1
                )
            """.trimIndent()

            val createMensajesTable = """
                CREATE TABLE $TABLE_MENSAJES (
                    $COLUMN_MENSAJE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_MENSAJE_REMITENTE_ID INTEGER NOT NULL,
                    $COLUMN_MENSAJE_REMITENTE_NOMBRE TEXT NOT NULL,
                    $COLUMN_MENSAJE_REMITENTE_ROL TEXT NOT NULL,
                    $COLUMN_MENSAJE_DESTINATARIO_ID INTEGER NOT NULL,
                    $COLUMN_MENSAJE_DESTINATARIO_NOMBRE TEXT NOT NULL,
                    $COLUMN_MENSAJE_DESTINATARIO_ROL TEXT NOT NULL,
                    $COLUMN_MENSAJE_ASUNTO TEXT NOT NULL,
                    $COLUMN_MENSAJE_CONTENIDO TEXT NOT NULL,
                    $COLUMN_MENSAJE_FECHA_ENVIO INTEGER NOT NULL,
                    $COLUMN_MENSAJE_LEIDO INTEGER DEFAULT 0,
                    $COLUMN_MENSAJE_ACTIVO INTEGER DEFAULT 1
                )
            """.trimIndent()

            val createDiagnosticosTable = """
                CREATE TABLE $TABLE_DIAGNOSTICOS (
                    $COLUMN_DIAGNOSTICO_ID TEXT PRIMARY KEY,
                    $COLUMN_DIAGNOSTICO_PACIENTE_ID TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_MEDICO_ID TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_FECHA TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_SINTOMAS TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_EXAMEN_FISICO TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_DIAGNOSTICO TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_TRATAMIENTO TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_MEDICAMENTOS TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_RECOMENDACIONES TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_PROXIMA_CITA TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_ESTADO TEXT NOT NULL
                )
            """.trimIndent()

            val createInformacionContactoTable = """
                CREATE TABLE $TABLE_INFORMACION_CONTACTO (
                    $COLUMN_CONTACTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_CONTACTO_USUARIO_ID INTEGER NOT NULL,
                    $COLUMN_CONTACTO_TELEFONO TEXT NOT NULL,
                    $COLUMN_CONTACTO_DIRECCION TEXT NOT NULL,
                    $COLUMN_CONTACTO_CIUDAD TEXT NOT NULL,
                    $COLUMN_CONTACTO_CODIGO_POSTAL TEXT NOT NULL,
                    $COLUMN_CONTACTO_FECHA_NACIMIENTO TEXT NOT NULL,
                    $COLUMN_CONTACTO_GENERO TEXT NOT NULL,
                    $COLUMN_CONTACTO_DOCUMENTO_IDENTIDAD TEXT NOT NULL,
                    $COLUMN_CONTACTO_TIPO_DOCUMENTO TEXT NOT NULL,
                    $COLUMN_CONTACTO_EMERGENCIA_CONTACTO TEXT NOT NULL,
                    $COLUMN_CONTACTO_EMERGENCIA_TELEFONO TEXT NOT NULL,
                    $COLUMN_CONTACTO_FECHA_ACTUALIZACION INTEGER NOT NULL
                )
            """.trimIndent()

            val createDatosPacientesTable = """
                CREATE TABLE $TABLE_DATOS_PACIENTES (
                    $COLUMN_DATOS_PACIENTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_DATOS_PACIENTE_USUARIO_ID INTEGER NOT NULL,
                    $COLUMN_DATOS_PACIENTE_SEGURO TEXT NOT NULL,
                    $COLUMN_DATOS_PACIENTE_NUMERO_SEGURO TEXT NOT NULL,
                    $COLUMN_DATOS_PACIENTE_FECHA_ACTUALIZACION INTEGER NOT NULL
                )
            """.trimIndent()

            db.execSQL(createUsuariosTable)
            db.execSQL(createPacientesTable)
            db.execSQL(createMensajesTable)
            db.execSQL(createDiagnosticosTable)
            db.execSQL(createInformacionContactoTable)
            db.execSQL(createDatosPacientesTable)
            
            // DATOS DE DEMOSTRACIÓN - DESCOMENTAR SOLO PARA PRUEBAS
            // insertarDatosDemostracion(db)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Agregar tabla de mensajes si no existe
            val createMensajesTable = """
                CREATE TABLE $TABLE_MENSAJES (
                    $COLUMN_MENSAJE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_MENSAJE_REMITENTE_ID INTEGER NOT NULL,
                    $COLUMN_MENSAJE_REMITENTE_NOMBRE TEXT NOT NULL,
                    $COLUMN_MENSAJE_REMITENTE_ROL TEXT NOT NULL,
                    $COLUMN_MENSAJE_DESTINATARIO_ID INTEGER NOT NULL,
                    $COLUMN_MENSAJE_DESTINATARIO_NOMBRE TEXT NOT NULL,
                    $COLUMN_MENSAJE_DESTINATARIO_ROL TEXT NOT NULL,
                    $COLUMN_MENSAJE_ASUNTO TEXT NOT NULL,
                    $COLUMN_MENSAJE_CONTENIDO TEXT NOT NULL,
                    $COLUMN_MENSAJE_FECHA_ENVIO INTEGER NOT NULL,
                    $COLUMN_MENSAJE_LEIDO INTEGER DEFAULT 0,
                    $COLUMN_MENSAJE_ACTIVO INTEGER DEFAULT 1
                )
            """.trimIndent()
            db.execSQL(createMensajesTable)
            
            // Insertar mensajes de demostración
            insertarMensajesDemostracion(db)
        }
        
        if (oldVersion < 3) {
            // Agregar mensajes adicionales de demostración para versiones anteriores
            insertarMensajesDemostracion(db)
        }
        
        if (oldVersion < 4) {
            // Agregar usuarios adicionales de demostración para versiones anteriores - COMENTADO PARA PRODUCCIÓN
            // insertarDatosDemostracion(db)
        }
        
        if (oldVersion < 5) {
            // Corregir roles de ADMINISTRADOR a PERSONAL ADMINISTRATIVO
            println("=== MIGRACIÓN: Corrigiendo roles de ADMINISTRADOR a PERSONAL ADMINISTRATIVO ===")
            
            val values = ContentValues().apply {
                put(COLUMN_USUARIO_ROL, "PERSONAL ADMINISTRATIVO")
            }
            
            val resultado = db.update(
                TABLE_USUARIOS,
                values,
                "$COLUMN_USUARIO_ROL = ?",
                arrayOf("ADMINISTRADOR")
            )
            
            println("Usuarios actualizados: $resultado")
            
            // Corregir roles en mensajes también
            val valuesMensajes = ContentValues().apply {
                put(COLUMN_MENSAJE_REMITENTE_ROL, "PERSONAL ADMINISTRATIVO")
            }
            
            val resultadoRemitente = db.update(
                TABLE_MENSAJES,
                valuesMensajes,
                "$COLUMN_MENSAJE_REMITENTE_ROL = ?",
                arrayOf("ADMINISTRADOR")
            )
            
            val valuesDestinatario = ContentValues().apply {
                put(COLUMN_MENSAJE_DESTINATARIO_ROL, "PERSONAL ADMINISTRATIVO")
            }
            
            val resultadoDestinatario = db.update(
                TABLE_MENSAJES,
                valuesDestinatario,
                "$COLUMN_MENSAJE_DESTINATARIO_ROL = ?",
                arrayOf("ADMINISTRADOR")
            )
            
            println("Mensajes actualizados (remitente): $resultadoRemitente")
            println("Mensajes actualizados (destinatario): $resultadoDestinatario")
        }
        
        if (oldVersion < 6) {
            // Crear tabla de diagnósticos con tipos de datos correctos
            val createDiagnosticosTable = """
                CREATE TABLE $TABLE_DIAGNOSTICOS (
                    $COLUMN_DIAGNOSTICO_ID TEXT PRIMARY KEY,
                    $COLUMN_DIAGNOSTICO_PACIENTE_ID TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_MEDICO_ID TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_FECHA TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_SINTOMAS TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_EXAMEN_FISICO TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_DIAGNOSTICO TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_TRATAMIENTO TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_MEDICAMENTOS TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_RECOMENDACIONES TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_PROXIMA_CITA TEXT NOT NULL,
                    $COLUMN_DIAGNOSTICO_ESTADO TEXT NOT NULL
                )
            """.trimIndent()
            db.execSQL(createDiagnosticosTable)
            
            println("Tabla de diagnósticos creada exitosamente en migración v6")
            
            // Insertar datos de diagnósticos de demostración - COMENTADO PARA PRODUCCIÓN
            // insertarDiagnosticosDemostracion(db)
        }
        
        if (oldVersion < 7) {
            // Esta versión no necesita crear tablas adicionales
            // Las tablas se crearán en versiones posteriores si es necesario
        }
        
        if (oldVersion < 8) {
            // Solo crear la tabla de información de contacto si no existe
            // La tabla de diagnósticos ya existe desde la versión 6
            try {
                val createInformacionContactoTable = """
                    CREATE TABLE $TABLE_INFORMACION_CONTACTO (
                        $COLUMN_CONTACTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        $COLUMN_CONTACTO_USUARIO_ID INTEGER NOT NULL,
                        $COLUMN_CONTACTO_TELEFONO TEXT NOT NULL,
                        $COLUMN_CONTACTO_DIRECCION TEXT NOT NULL,
                        $COLUMN_CONTACTO_CIUDAD TEXT NOT NULL,
                        $COLUMN_CONTACTO_CODIGO_POSTAL TEXT NOT NULL,
                        $COLUMN_CONTACTO_FECHA_NACIMIENTO TEXT NOT NULL,
                        $COLUMN_CONTACTO_GENERO TEXT NOT NULL,
                        $COLUMN_CONTACTO_DOCUMENTO_IDENTIDAD TEXT NOT NULL,
                        $COLUMN_CONTACTO_TIPO_DOCUMENTO TEXT NOT NULL,
                        $COLUMN_CONTACTO_EMERGENCIA_CONTACTO TEXT NOT NULL,
                        $COLUMN_CONTACTO_EMERGENCIA_TELEFONO TEXT NOT NULL,
                        $COLUMN_CONTACTO_FECHA_ACTUALIZACION INTEGER NOT NULL
                    )
                """.trimIndent()
                db.execSQL(createInformacionContactoTable)
            } catch (e: Exception) {
                // La tabla ya existe, ignorar el error
                println("Tabla de información de contacto ya existe: ${e.message}")
            }
        }
        
        if (oldVersion < 9) {
            // Limpiar cualquier tabla duplicada y asegurar que solo existe la tabla de información de contacto
            try {
                // Verificar si la tabla de información de contacto existe
                val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_INFORMACION_CONTACTO'", null)
                val tableExists = cursor.moveToFirst()
                cursor.close()
                
                if (!tableExists) {
                    val createInformacionContactoTable = """
                        CREATE TABLE $TABLE_INFORMACION_CONTACTO (
                            $COLUMN_CONTACTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                            $COLUMN_CONTACTO_USUARIO_ID INTEGER NOT NULL,
                            $COLUMN_CONTACTO_TELEFONO TEXT NOT NULL,
                            $COLUMN_CONTACTO_DIRECCION TEXT NOT NULL,
                            $COLUMN_CONTACTO_CIUDAD TEXT NOT NULL,
                            $COLUMN_CONTACTO_CODIGO_POSTAL TEXT NOT NULL,
                            $COLUMN_CONTACTO_FECHA_NACIMIENTO TEXT NOT NULL,
                            $COLUMN_CONTACTO_GENERO TEXT NOT NULL,
                            $COLUMN_CONTACTO_DOCUMENTO_IDENTIDAD TEXT NOT NULL,
                            $COLUMN_CONTACTO_TIPO_DOCUMENTO TEXT NOT NULL,
                            $COLUMN_CONTACTO_EMERGENCIA_CONTACTO TEXT NOT NULL,
                            $COLUMN_CONTACTO_EMERGENCIA_TELEFONO TEXT NOT NULL,
                            $COLUMN_CONTACTO_FECHA_ACTUALIZACION INTEGER NOT NULL
                        )
                    """.trimIndent()
                    db.execSQL(createInformacionContactoTable)
                    println("Tabla de información de contacto creada exitosamente")
                } else {
                    println("Tabla de información de contacto ya existe")
                }
            } catch (e: Exception) {
                println("Error al crear tabla de información de contacto: ${e.message}")
            }
        }
        
        if (oldVersion < 10) {
            // LIMPIAR TODOS LOS DATOS DE PRUEBA - VERSIÓN DE PRODUCCIÓN
            println("=== MIGRACIÓN V10: Limpiando datos de prueba ===")
            
            // Eliminar usuarios de prueba (emails que contengan biomonitor.com)
            val usuariosEliminados = db.delete(
                TABLE_USUARIOS,
                "$COLUMN_USUARIO_EMAIL LIKE '%biomonitor.com%'",
                null
            )
            println("Usuarios de prueba eliminados: $usuariosEliminados")
            
            // Eliminar pacientes de prueba
            val pacientesEliminados = db.delete(
                TABLE_PACIENTES,
                null, // Eliminar todos los pacientes de prueba
                null
            )
            println("Pacientes de prueba eliminados: $pacientesEliminados")
            
            // Eliminar mensajes de prueba
            val mensajesEliminados = db.delete(
                TABLE_MENSAJES,
                null, // Eliminar todos los mensajes de prueba
                null
            )
            println("Mensajes de prueba eliminados: $mensajesEliminados")
            
            // Eliminar diagnósticos de prueba
            val diagnosticosEliminados = db.delete(
                TABLE_DIAGNOSTICOS,
                null, // Eliminar todos los diagnósticos de prueba
                null
            )
            println("Diagnósticos de prueba eliminados: $diagnosticosEliminados")
            
            // Eliminar información de contacto de prueba
            val contactosEliminados = db.delete(
                TABLE_INFORMACION_CONTACTO,
                null, // Eliminar toda la información de contacto de prueba
                null
            )
            println("Información de contacto de prueba eliminada: $contactosEliminados")
            
            println("=== LIMPIEZA DE DATOS DE PRUEBA COMPLETADA ===")
        }
        
        if (oldVersion < 11) {
            // CORREGIR ESTRUCTURA DE LA TABLA DE DIAGNÓSTICOS
            println("=== MIGRACIÓN V11: Corrigiendo estructura de tabla de diagnósticos ===")
            
            try {
                // Eliminar la tabla de diagnósticos existente
                db.execSQL("DROP TABLE IF EXISTS $TABLE_DIAGNOSTICOS")
                println("Tabla de diagnósticos eliminada")
                
                // Recrear la tabla con la estructura correcta
                val createDiagnosticosTable = """
                    CREATE TABLE $TABLE_DIAGNOSTICOS (
                        $COLUMN_DIAGNOSTICO_ID TEXT PRIMARY KEY,
                        $COLUMN_DIAGNOSTICO_PACIENTE_ID TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_MEDICO_ID TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_FECHA TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_SINTOMAS TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_EXAMEN_FISICO TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_DIAGNOSTICO TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_TRATAMIENTO TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_MEDICAMENTOS TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_RECOMENDACIONES TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_PROXIMA_CITA TEXT NOT NULL,
                        $COLUMN_DIAGNOSTICO_ESTADO TEXT NOT NULL
                    )
                """.trimIndent()
                db.execSQL(createDiagnosticosTable)
                println("Tabla de diagnósticos recreada con estructura correcta")
                
            } catch (e: Exception) {
                println("Error al recrear tabla de diagnósticos: ${e.message}")
                e.printStackTrace()
            }
        }
        
        if (oldVersion < 12) {
            // AGREGAR TABLA DE DATOS ESPECÍFICOS DE PACIENTES
            println("=== MIGRACIÓN V12: Agregando tabla de datos específicos de pacientes ===")
            
            try {
                val createDatosPacientesTable = """
                    CREATE TABLE $TABLE_DATOS_PACIENTES (
                        $COLUMN_DATOS_PACIENTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        $COLUMN_DATOS_PACIENTE_USUARIO_ID INTEGER NOT NULL,
                        $COLUMN_DATOS_PACIENTE_SEGURO TEXT NOT NULL,
                        $COLUMN_DATOS_PACIENTE_NUMERO_SEGURO TEXT NOT NULL,
                        $COLUMN_DATOS_PACIENTE_FECHA_ACTUALIZACION INTEGER NOT NULL
                    )
                """.trimIndent()
                db.execSQL(createDatosPacientesTable)
                println("Tabla de datos de pacientes creada exitosamente")
                
            } catch (e: Exception) {
                println("Error al crear tabla de datos de pacientes: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun insertarDatosDemostracion(db: SQLiteDatabase) {
        // Insertar usuarios de demostración
        val usuarios = listOf(
            mapOf(
                COLUMN_USUARIO_EMAIL to "admin@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "admin123",
                COLUMN_USUARIO_NOMBRE to "Administrador del Sistema",
                COLUMN_USUARIO_ROL to "PERSONAL ADMINISTRATIVO",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "medico@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "medico123",
                COLUMN_USUARIO_NOMBRE to "Dr. Carlos Rodríguez",
                COLUMN_USUARIO_ROL to "MEDICO",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "paciente@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "paciente123",
                COLUMN_USUARIO_NOMBRE to "María González López",
                COLUMN_USUARIO_ROL to "PACIENTE",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "medico2@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "medico123",
                COLUMN_USUARIO_NOMBRE to "Dra. Ana Martínez",
                COLUMN_USUARIO_ROL to "MEDICO",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "medico3@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "medico123",
                COLUMN_USUARIO_NOMBRE to "Dr. Luis Pérez",
                COLUMN_USUARIO_ROL to "MEDICO",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "paciente2@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "paciente123",
                COLUMN_USUARIO_NOMBRE to "Juan Carlos Silva",
                COLUMN_USUARIO_ROL to "PACIENTE",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "paciente3@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "paciente123",
                COLUMN_USUARIO_NOMBRE to "Carmen Ruiz Díaz",
                COLUMN_USUARIO_ROL to "PACIENTE",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "admin2@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "admin123",
                COLUMN_USUARIO_NOMBRE to "Sofía Mendoza",
                COLUMN_USUARIO_ROL to "PERSONAL ADMINISTRATIVO",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            ),
            mapOf(
                COLUMN_USUARIO_EMAIL to "admin3@biomonitor.com",
                COLUMN_USUARIO_CONTRASENA to "admin123",
                COLUMN_USUARIO_NOMBRE to "Roberto Jiménez",
                COLUMN_USUARIO_ROL to "PERSONAL ADMINISTRATIVO",
                COLUMN_USUARIO_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_USUARIO_ACTIVO to 1
            )
        )

        usuarios.forEach { usuario ->
            val values = ContentValues()
            usuario.forEach { (key, value) ->
                values.put(key, value.toString())
            }
            db.insert(TABLE_USUARIOS, null, values)
        }

        // Insertar pacientes de demostración
        val pacientes = listOf(
            mapOf(
                COLUMN_PACIENTE_NOMBRE to "María",
                COLUMN_PACIENTE_APELLIDOS to "González López",
                COLUMN_PACIENTE_FECHA_NACIMIENTO to "15/03/1989",
                COLUMN_PACIENTE_GENERO to "Femenino",
                COLUMN_PACIENTE_DOCUMENTO to "DNI: 12345678A",
                COLUMN_PACIENTE_TELEFONO to "+34 612 345 678",
                COLUMN_PACIENTE_EMAIL to "maria.gonzalez@email.com",
                COLUMN_PACIENTE_DIRECCION to "Calle Mayor 123, Madrid",
                COLUMN_PACIENTE_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_PACIENTE_ACTIVO to 1
            ),
            mapOf(
                COLUMN_PACIENTE_NOMBRE to "Carlos",
                COLUMN_PACIENTE_APELLIDOS to "Rodríguez Martín",
                COLUMN_PACIENTE_FECHA_NACIMIENTO to "22/07/1972",
                COLUMN_PACIENTE_GENERO to "Masculino",
                COLUMN_PACIENTE_DOCUMENTO to "DNI: 87654321B",
                COLUMN_PACIENTE_TELEFONO to "+34 698 765 432",
                COLUMN_PACIENTE_EMAIL to "carlos.rodriguez@email.com",
                COLUMN_PACIENTE_DIRECCION to "Avenida Principal 456, Barcelona",
                COLUMN_PACIENTE_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_PACIENTE_ACTIVO to 1
            ),
            mapOf(
                COLUMN_PACIENTE_NOMBRE to "Ana",
                COLUMN_PACIENTE_APELLIDOS to "Fernández Jiménez",
                COLUMN_PACIENTE_FECHA_NACIMIENTO to "08/11/1996",
                COLUMN_PACIENTE_GENERO to "Femenino",
                COLUMN_PACIENTE_DOCUMENTO to "DNI: 11223344C",
                COLUMN_PACIENTE_TELEFONO to "+34 655 123 456",
                COLUMN_PACIENTE_EMAIL to "ana.fernandez@email.com",
                COLUMN_PACIENTE_DIRECCION to "Plaza Central 789, Valencia",
                COLUMN_PACIENTE_FECHA_REGISTRO to System.currentTimeMillis(),
                COLUMN_PACIENTE_ACTIVO to 1
            )
        )

        pacientes.forEach { paciente ->
            val values = ContentValues()
            paciente.forEach { (key, value) ->
                values.put(key, value.toString())
            }
            db.insert(TABLE_PACIENTES, null, values)
        }
    }

    // Métodos para usuarios
    fun autenticarUsuario(email: String, contrasena: String): Map<String, Any>? {
        val db = this.readableDatabase
        val contrasenaEncriptada = contrasena.hashCode().toString()
        
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_USUARIO_EMAIL = ? AND $COLUMN_USUARIO_CONTRASENA = ? AND $COLUMN_USUARIO_ACTIVO = 1",
            arrayOf(email, contrasenaEncriptada),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val usuario = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                usuario[columnName] = value
            }
            cursor.close()
            usuario
        } else {
            cursor.close()
            null
        }
    }

    fun existeUsuarioConEmail(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(COLUMN_USUARIO_ID),
            "$COLUMN_USUARIO_EMAIL = ? AND $COLUMN_USUARIO_ACTIVO = 1",
            arrayOf(email),
            null,
            null,
            null
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    fun insertarUsuario(
        email: String,
        contrasena: String,
        nombreCompleto: String,
        rol: String
    ): Long {
        try {
            println("=== DEBUG: insertarUsuario ===")
            println("Email: $email")
            println("Nombre: $nombreCompleto")
            println("Rol: $rol")
            
            // Convertir rol a mayúsculas para consistencia
            val rolNormalizado = rol.uppercase()
            
            val contrasenaEncriptada = contrasena.hashCode().toString()
            println("Contraseña encriptada: $contrasenaEncriptada")
            
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_USUARIO_EMAIL, email)
                put(COLUMN_USUARIO_CONTRASENA, contrasenaEncriptada)
                put(COLUMN_USUARIO_NOMBRE, nombreCompleto)
                put(COLUMN_USUARIO_ROL, rolNormalizado)
                put(COLUMN_USUARIO_FECHA_REGISTRO, System.currentTimeMillis())
                put(COLUMN_USUARIO_ACTIVO, 1L)
            }
            
            println("Valores a insertar: $values")
            
            val resultado = db.insert(TABLE_USUARIOS, null, values)
            
            println("Resultado de inserción: $resultado")
            
            if (resultado > 0) {
                println("✅ Usuario insertado exitosamente con ID: $resultado")
            } else {
                println("❌ Error al insertar usuario")
            }
            
            return resultado
        } catch (e: Exception) {
            println("❌ Excepción al insertar usuario: ${e.message}")
            e.printStackTrace()
            return -1L
        }
    }

    fun obtenerUsuarioPorEmail(email: String): Map<String, Any>? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_USUARIO_EMAIL = ? AND $COLUMN_USUARIO_ACTIVO = 1",
            arrayOf(email),
            null,
            null,
            null
        )

        val usuario = if (cursor.moveToFirst()) {
            val userMap = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                userMap[columnName] = value
            }
            userMap
        } else {
            null
        }
        
        cursor.close()
        return usuario
    }

    fun obtenerTodosLosUsuarios(): List<Map<String, Any>> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_USUARIO_ACTIVO = 1",
            null,
            null,
            null,
            "$COLUMN_USUARIO_NOMBRE ASC"
        )

        val usuarios = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val usuario = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                usuario[columnName] = value
            }
            usuarios.add(usuario)
        }
        cursor.close()
        return usuarios
    }

    // Métodos para pacientes
    fun obtenerTodosLosPacientes(): List<Map<String, Any>> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PACIENTES,
            null,
            "$COLUMN_PACIENTE_ACTIVO = 1",
            null,
            null,
            null,
            "$COLUMN_PACIENTE_NOMBRE ASC"
        )

        val pacientes = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val paciente = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                paciente[columnName] = value
            }
            pacientes.add(paciente)
        }
        cursor.close()
        return pacientes
    }

    fun insertarPaciente(
        nombre: String,
        apellidos: String,
        fechaNacimiento: String,
        genero: String,
        documento: String,
        telefono: String,
        email: String,
        direccion: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PACIENTE_NOMBRE, nombre)
            put(COLUMN_PACIENTE_APELLIDOS, apellidos)
            put(COLUMN_PACIENTE_FECHA_NACIMIENTO, fechaNacimiento)
            put(COLUMN_PACIENTE_GENERO, genero)
            put(COLUMN_PACIENTE_DOCUMENTO, documento)
            put(COLUMN_PACIENTE_TELEFONO, telefono)
            put(COLUMN_PACIENTE_EMAIL, email)
            put(COLUMN_PACIENTE_DIRECCION, direccion)
            put(COLUMN_PACIENTE_FECHA_REGISTRO, System.currentTimeMillis())
            put(COLUMN_PACIENTE_ACTIVO, 1L)
        }
        return db.insert(TABLE_PACIENTES, null, values)
    }

    // Métodos para mensajes
    fun insertarMensaje(
        remitenteId: Int,
        remitenteNombre: String,
        remitenteRol: String,
        destinatarioId: Int,
        destinatarioNombre: String,
        destinatarioRol: String,
        asunto: String,
        contenido: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MENSAJE_REMITENTE_ID, remitenteId)
            put(COLUMN_MENSAJE_REMITENTE_NOMBRE, remitenteNombre)
            put(COLUMN_MENSAJE_REMITENTE_ROL, remitenteRol)
            put(COLUMN_MENSAJE_DESTINATARIO_ID, destinatarioId)
            put(COLUMN_MENSAJE_DESTINATARIO_NOMBRE, destinatarioNombre)
            put(COLUMN_MENSAJE_DESTINATARIO_ROL, destinatarioRol)
            put(COLUMN_MENSAJE_ASUNTO, asunto)
            put(COLUMN_MENSAJE_CONTENIDO, contenido)
            put(COLUMN_MENSAJE_FECHA_ENVIO, System.currentTimeMillis())
            put(COLUMN_MENSAJE_LEIDO, 0L)
            put(COLUMN_MENSAJE_ACTIVO, 1L)
        }
        return db.insert(TABLE_MENSAJES, null, values)
    }

    fun guardarMensaje(
        remitenteId: Int,
        destinatarioId: Int,
        contenido: String,
        remitenteNombre: String
    ): Boolean {
        try {
            // Obtener información del destinatario
            val destinatario = obtenerUsuarioPorId(destinatarioId)
            val destinatarioNombre = destinatario?.get("nombre_completo") as? String ?: "Usuario"
            val destinatarioRol = destinatario?.get("rol") as? String ?: "DESCONOCIDO"
            
            // Obtener rol del remitente
            val remitente = obtenerUsuarioPorId(remitenteId)
            val remitenteRol = remitente?.get("rol") as? String ?: "DESCONOCIDO"
            
            // Crear asunto simple
            val asunto = "Mensaje de chat"
            
            // Insertar mensaje usando el método existente
            val resultado = insertarMensaje(
                remitenteId,
                remitenteNombre,
                remitenteRol,
                destinatarioId,
                destinatarioNombre,
                destinatarioRol,
                asunto,
                contenido
            )
            
            return resultado != -1L
        } catch (e: Exception) {
            println("Error al guardar mensaje: ${e.message}")
            e.printStackTrace()
            return false
        }
    }
    
    private fun obtenerUsuarioPorId(usuarioId: Int): Map<String, Any>? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_USUARIO_ID = ? AND $COLUMN_USUARIO_ACTIVO = 1",
            arrayOf(usuarioId.toString()),
            null,
            null,
            null
        )

        val usuario = if (cursor.moveToFirst()) {
            val userMap = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                userMap[columnName] = value
            }
            userMap
        } else {
            null
        }
        
        cursor.close()
        return usuario
    }

    fun obtenerMensajesRecibidos(destinatarioId: Int): List<Map<String, Any>> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_MENSAJES,
            null,
            "$COLUMN_MENSAJE_DESTINATARIO_ID = ? AND $COLUMN_MENSAJE_ACTIVO = 1",
            arrayOf(destinatarioId.toString()),
            null,
            null,
            "$COLUMN_MENSAJE_FECHA_ENVIO DESC"
        )

        val mensajes = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val mensaje = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                mensaje[columnName] = value
            }
            mensajes.add(mensaje)
        }
        cursor.close()
        return mensajes
    }

    fun obtenerMensajesEnviados(remitenteId: Int): List<Map<String, Any>> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_MENSAJES,
            null,
            "$COLUMN_MENSAJE_REMITENTE_ID = ? AND $COLUMN_MENSAJE_ACTIVO = 1",
            arrayOf(remitenteId.toString()),
            null,
            null,
            "$COLUMN_MENSAJE_FECHA_ENVIO DESC"
        )

        val mensajes = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val mensaje = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                mensaje[columnName] = value
            }
            mensajes.add(mensaje)
        }
        cursor.close()
        return mensajes
    }

    fun marcarMensajeComoLeido(mensajeId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MENSAJE_LEIDO, 1L)
        }
        db.update(TABLE_MENSAJES, values, "$COLUMN_MENSAJE_ID = ?", arrayOf(mensajeId.toString()))
    }

    fun obtenerUsuariosPorRol(rol: String): List<Map<String, Any>> {
        val db = this.readableDatabase
        
        // Normalizar el rol de búsqueda a mayúsculas
        val rolNormalizado = rol.uppercase()
        
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "UPPER($COLUMN_USUARIO_ROL) = ? AND $COLUMN_USUARIO_ACTIVO = 1",
            arrayOf(rolNormalizado),
            null,
            null,
            "$COLUMN_USUARIO_NOMBRE ASC"
        )

        val usuarios = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val usuario = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                usuario[columnName] = value
            }
            usuarios.add(usuario)
        }
        cursor.close()
        
        return usuarios
    }

    fun obtenerUsuarioPorNombreYRol(nombre: String, rol: String): Map<String, Any>? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_USUARIO_NOMBRE = ? AND $COLUMN_USUARIO_ROL = ? AND $COLUMN_USUARIO_ACTIVO = 1",
            arrayOf(nombre, rol),
            null,
            null,
            null
        )

        val usuario = if (cursor.moveToFirst()) {
            val userMap = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                userMap[columnName] = value
            }
            userMap
        } else {
            null
        }
        
        cursor.close()
        return usuario
    }

    fun obtenerMensajesPrivadosEntreUsuarios(usuario1Id: Int, usuario2Id: Int): List<Map<String, Any>> {
        val db = this.readableDatabase
        
        // Consulta para obtener mensajes privados entre dos usuarios específicos
        val query = """
            SELECT * FROM $TABLE_MENSAJES 
            WHERE $COLUMN_MENSAJE_ACTIVO = 1 
            AND (
                ($COLUMN_MENSAJE_REMITENTE_ID = ? AND $COLUMN_MENSAJE_DESTINATARIO_ID = ?)
                OR 
                ($COLUMN_MENSAJE_REMITENTE_ID = ? AND $COLUMN_MENSAJE_DESTINATARIO_ID = ?)
            )
            ORDER BY $COLUMN_MENSAJE_FECHA_ENVIO ASC
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(
            usuario1Id.toString(), usuario2Id.toString(),
            usuario2Id.toString(), usuario1Id.toString()
        ))

        val mensajes = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val mensaje = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                mensaje[columnName] = value
            }
            mensajes.add(mensaje)
        }
        cursor.close()
        
        return mensajes
    }

    private fun insertarMensajesDemostracion(db: SQLiteDatabase) {
        val mensajes = listOf(
            mapOf(
                COLUMN_MENSAJE_REMITENTE_ID to 3, // Paciente María González
                COLUMN_MENSAJE_REMITENTE_NOMBRE to "María González López",
                COLUMN_MENSAJE_REMITENTE_ROL to "PACIENTE",
                COLUMN_MENSAJE_DESTINATARIO_ID to 2, // Dr. Carlos Rodríguez
                COLUMN_MENSAJE_DESTINATARIO_NOMBRE to "Dr. Carlos Rodríguez",
                COLUMN_MENSAJE_DESTINATARIO_ROL to "MEDICO",
                COLUMN_MENSAJE_ASUNTO to "Consulta sobre medicación",
                COLUMN_MENSAJE_CONTENIDO to "Doctor, tengo dudas sobre la dosis de metformina que me recetó. ¿Puede aclararme si debo tomarla antes o después de las comidas?",
                COLUMN_MENSAJE_FECHA_ENVIO to System.currentTimeMillis() - 86400000, // Hace 1 día
                COLUMN_MENSAJE_LEIDO to 0L,
                COLUMN_MENSAJE_ACTIVO to 1L
            ),
            mapOf(
                COLUMN_MENSAJE_REMITENTE_ID to 3, // Paciente María González
                COLUMN_MENSAJE_REMITENTE_NOMBRE to "María González López",
                COLUMN_MENSAJE_REMITENTE_ROL to "PACIENTE",
                COLUMN_MENSAJE_DESTINATARIO_ID to 1, // Administrador
                COLUMN_MENSAJE_DESTINATARIO_NOMBRE to "Administrador del Sistema",
                COLUMN_MENSAJE_DESTINATARIO_ROL to "PERSONAL ADMINISTRATIVO",
                COLUMN_MENSAJE_ASUNTO to "Problema con mi cita",
                COLUMN_MENSAJE_CONTENIDO to "Hola, tengo un problema con mi cita programada para mañana. ¿Podrían ayudarme a reprogramarla?",
                COLUMN_MENSAJE_FECHA_ENVIO to System.currentTimeMillis() - 172800000, // Hace 2 días
                COLUMN_MENSAJE_LEIDO to 1L,
                COLUMN_MENSAJE_ACTIVO to 1L
            ),
            mapOf(
                COLUMN_MENSAJE_REMITENTE_ID to 2, // Dr. Carlos Rodríguez
                COLUMN_MENSAJE_REMITENTE_ROL to "MEDICO",
                COLUMN_MENSAJE_DESTINATARIO_ID to 3, // Paciente María González
                COLUMN_MENSAJE_DESTINATARIO_NOMBRE to "María González López",
                COLUMN_MENSAJE_DESTINATARIO_ROL to "PACIENTE",
                COLUMN_MENSAJE_ASUNTO to "Respuesta sobre medicación",
                COLUMN_MENSAJE_CONTENIDO to "Hola María, la metformina debe tomarse durante las comidas para reducir los efectos secundarios. Tómala con el desayuno, almuerzo y cena.",
                COLUMN_MENSAJE_FECHA_ENVIO to System.currentTimeMillis() - 43200000, // Hace 12 horas
                COLUMN_MENSAJE_LEIDO to 0L,
                COLUMN_MENSAJE_ACTIVO to 1L
            ),
            mapOf(
                COLUMN_MENSAJE_REMITENTE_ID to 1, // Administrador
                COLUMN_MENSAJE_REMITENTE_NOMBRE to "Administrador del Sistema",
                COLUMN_MENSAJE_REMITENTE_ROL to "PERSONAL ADMINISTRATIVO",
                COLUMN_MENSAJE_DESTINATARIO_ID to 3, // Paciente María González
                COLUMN_MENSAJE_DESTINATARIO_NOMBRE to "María González López",
                COLUMN_MENSAJE_DESTINATARIO_ROL to "PACIENTE",
                COLUMN_MENSAJE_ASUNTO to "Cita reprogramada",
                COLUMN_MENSAJE_CONTENIDO to "Hola María, hemos reprogramado tu cita para el próximo viernes a las 10:00 AM. ¿Te viene bien ese horario?",
                COLUMN_MENSAJE_FECHA_ENVIO to System.currentTimeMillis() - 86400000, // Hace 1 día
                COLUMN_MENSAJE_LEIDO to 1L,
                COLUMN_MENSAJE_ACTIVO to 1L
            ),
            mapOf(
                COLUMN_MENSAJE_REMITENTE_ID to 3, // Paciente María González
                COLUMN_MENSAJE_REMITENTE_ROL to "PACIENTE",
                COLUMN_MENSAJE_DESTINATARIO_ID to 2, // Dr. Carlos Rodríguez
                COLUMN_MENSAJE_DESTINATARIO_NOMBRE to "Dr. Carlos Rodríguez",
                COLUMN_MENSAJE_DESTINATARIO_ROL to "MEDICO",
                COLUMN_MENSAJE_ASUNTO to "Síntomas nuevos",
                COLUMN_MENSAJE_CONTENIDO to "Doctor, desde ayer tengo mareos y náuseas. ¿Puede ser un efecto secundario de la medicación?",
                COLUMN_MENSAJE_FECHA_ENVIO to System.currentTimeMillis() - 21600000, // Hace 6 horas
                COLUMN_MENSAJE_LEIDO to 0L,
                COLUMN_MENSAJE_ACTIVO to 1L
            ),
            mapOf(
                COLUMN_MENSAJE_REMITENTE_ID to 2, // Dr. Carlos Rodríguez
                COLUMN_MENSAJE_REMITENTE_ROL to "MEDICO",
                COLUMN_MENSAJE_DESTINATARIO_ID to 3, // Paciente María González
                COLUMN_MENSAJE_DESTINATARIO_NOMBRE to "María González López",
                COLUMN_MENSAJE_DESTINATARIO_ROL to "PACIENTE",
                COLUMN_MENSAJE_ASUNTO to "Re: Síntomas nuevos",
                COLUMN_MENSAJE_CONTENIDO to "María, esos síntomas pueden ser normales al inicio del tratamiento. Si persisten más de 3 días, suspende la medicación y ven a consulta. ¿Has comido bien hoy?",
                COLUMN_MENSAJE_FECHA_ENVIO to System.currentTimeMillis() - 10800000, // Hace 3 horas
                COLUMN_MENSAJE_LEIDO to 0L,
                COLUMN_MENSAJE_ACTIVO to 1L
            ),
            mapOf(
                COLUMN_MENSAJE_REMITENTE_ID to 3, // Paciente María González
                COLUMN_MENSAJE_REMITENTE_ROL to "PACIENTE",
                COLUMN_MENSAJE_DESTINATARIO_ID to 2, // Dr. Carlos Rodríguez
                COLUMN_MENSAJE_DESTINATARIO_NOMBRE to "Dr. Carlos Rodríguez",
                COLUMN_MENSAJE_DESTINATARIO_ROL to "MEDICO",
                COLUMN_MENSAJE_ASUNTO to "Re: Síntomas nuevos",
                COLUMN_MENSAJE_CONTENIDO to "Gracias doctor. Sí, he comido normalmente. Los síntomas han mejorado un poco. Seguiré tomando la medicación como me indicó.",
                COLUMN_MENSAJE_FECHA_ENVIO to System.currentTimeMillis() - 3600000, // Hace 1 hora
                COLUMN_MENSAJE_LEIDO to 0L,
                COLUMN_MENSAJE_ACTIVO to 1L
            )
        )

        mensajes.forEach { mensaje ->
            val values = ContentValues()
            mensaje.forEach { (key, value) ->
                values.put(key, value.toString())
            }
            db.insert(TABLE_MENSAJES, null, values)
        }
    }

    private fun insertarDiagnosticosDemostracion(db: SQLiteDatabase) {
        val diagnosticos = listOf(
            mapOf(
                COLUMN_DIAGNOSTICO_PACIENTE_ID to 1,
                COLUMN_DIAGNOSTICO_MEDICO_ID to 2,
                COLUMN_DIAGNOSTICO_FECHA to System.currentTimeMillis(),
                COLUMN_DIAGNOSTICO_SINTOMAS to "Dolor de cabeza, mareos",
                COLUMN_DIAGNOSTICO_EXAMEN_FISICO to "Examen físico normal",
                COLUMN_DIAGNOSTICO_DIAGNOSTICO to "Migraña",
                COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS to "Ninguno",
                COLUMN_DIAGNOSTICO_TRATAMIENTO to "Medicación para migraña",
                COLUMN_DIAGNOSTICO_MEDICAMENTOS to "Ibuprofeno",
                COLUMN_DIAGNOSTICO_RECOMENDACIONES to "Beber mucha agua",
                COLUMN_DIAGNOSTICO_PROXIMA_CITA to System.currentTimeMillis() + 86400000, // Hace 1 día
                COLUMN_DIAGNOSTICO_ESTADO to "Activo"
            ),
            mapOf(
                COLUMN_DIAGNOSTICO_PACIENTE_ID to 2,
                COLUMN_DIAGNOSTICO_MEDICO_ID to 1,
                COLUMN_DIAGNOSTICO_FECHA to System.currentTimeMillis(),
                COLUMN_DIAGNOSTICO_SINTOMAS to "Dolor abdominal, nauseas",
                COLUMN_DIAGNOSTICO_EXAMEN_FISICO to "Examen físico normal",
                COLUMN_DIAGNOSTICO_DIAGNOSTICO to "Gastroenteritis",
                COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS to "Ninguno",
                COLUMN_DIAGNOSTICO_TRATAMIENTO to "Reposo y reposicionamiento",
                COLUMN_DIAGNOSTICO_MEDICAMENTOS to "Loperamida",
                COLUMN_DIAGNOSTICO_RECOMENDACIONES to "Beber mucha agua",
                COLUMN_DIAGNOSTICO_PROXIMA_CITA to System.currentTimeMillis() + 86400000, // Hace 1 día
                COLUMN_DIAGNOSTICO_ESTADO to "Activo"
            ),
            mapOf(
                COLUMN_DIAGNOSTICO_PACIENTE_ID to 3,
                COLUMN_DIAGNOSTICO_MEDICO_ID to 1,
                COLUMN_DIAGNOSTICO_FECHA to System.currentTimeMillis(),
                COLUMN_DIAGNOSTICO_SINTOMAS to "Dolor de espalda, dificultad para moverse",
                COLUMN_DIAGNOSTICO_EXAMEN_FISICO to "Examen físico normal",
                COLUMN_DIAGNOSTICO_DIAGNOSTICO to "Esguince lumbar",
                COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS to "Ninguno",
                COLUMN_DIAGNOSTICO_TRATAMIENTO to "Reposo y reposicionamiento",
                COLUMN_DIAGNOSTICO_MEDICAMENTOS to "Paracetamol",
                COLUMN_DIAGNOSTICO_RECOMENDACIONES to "Evitar actividades físicas intensas",
                COLUMN_DIAGNOSTICO_PROXIMA_CITA to System.currentTimeMillis() + 86400000, // Hace 1 día
                COLUMN_DIAGNOSTICO_ESTADO to "Activo"
            )
        )

        diagnosticos.forEach { diagnostico ->
            val values = ContentValues()
            diagnostico.forEach { (key, value) ->
                values.put(key, value.toString())
            }
            db.insert(TABLE_DIAGNOSTICOS, null, values)
        }
    }

    /**
     * Inserta un nuevo diagnóstico médico en la base de datos
     */
    fun insertarDiagnostico(diagnostico: com.example.softmedv5.modelo.DiagnosticoMedico): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DIAGNOSTICO_PACIENTE_ID, diagnostico.pacienteId)
            put(COLUMN_DIAGNOSTICO_MEDICO_ID, diagnostico.medicoId)
            put(COLUMN_DIAGNOSTICO_FECHA, diagnostico.fecha)
            put(COLUMN_DIAGNOSTICO_SINTOMAS, diagnostico.sintomas)
            put(COLUMN_DIAGNOSTICO_EXAMEN_FISICO, diagnostico.examenFisico)
            put(COLUMN_DIAGNOSTICO_DIAGNOSTICO, diagnostico.diagnostico)
            put(COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS, diagnostico.diagnosticosSecundarios)
            put(COLUMN_DIAGNOSTICO_TRATAMIENTO, diagnostico.tratamiento)
            put(COLUMN_DIAGNOSTICO_MEDICAMENTOS, diagnostico.medicamentos)
            put(COLUMN_DIAGNOSTICO_RECOMENDACIONES, diagnostico.recomendaciones)
            put(COLUMN_DIAGNOSTICO_PROXIMA_CITA, diagnostico.proximaCita)
            put(COLUMN_DIAGNOSTICO_ESTADO, diagnostico.estado)
        }
        
        return db.insert(TABLE_DIAGNOSTICOS, null, values)
    }

    /**
     * Obtiene todos los diagnósticos de un paciente específico
     */
    fun obtenerDiagnosticosPorPaciente(pacienteId: String): List<com.example.softmedv5.modelo.DiagnosticoMedico> {
        println("=== DEBUG: obtenerDiagnosticosPorPaciente ===")
        println("Paciente ID buscado: '$pacienteId'")
        println("Tipo de Paciente ID: ${pacienteId::class.simpleName}")
        
        val db = this.readableDatabase
        
        // Primero, verificar si la tabla existe y tiene datos
        val cursorCheck = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_DIAGNOSTICOS'", null)
        val tablaExiste = cursorCheck.moveToFirst()
        cursorCheck.close()
        println("Tabla diagnósticos existe: $tablaExiste")
        
        if (!tablaExiste) {
            println("ERROR: La tabla de diagnósticos no existe")
            return emptyList()
        }
        
        // Verificar cuántos registros hay en total
        val cursorCount = db.rawQuery("SELECT COUNT(*) FROM $TABLE_DIAGNOSTICOS", null)
        val totalRegistros = if (cursorCount.moveToFirst()) {
            cursorCount.getInt(0)
        } else {
            0
        }
        cursorCount.close()
        println("Total de registros en tabla diagnósticos: $totalRegistros")
        
        // Mostrar todos los registros para debug
        val cursorAll = db.rawQuery("SELECT * FROM $TABLE_DIAGNOSTICOS", null)
        println("Todos los registros en la tabla:")
        while (cursorAll.moveToNext()) {
            val id = cursorAll.getString(cursorAll.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_ID))
            val pacienteIdRegistro = cursorAll.getString(cursorAll.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_PACIENTE_ID))
            val medicoId = cursorAll.getString(cursorAll.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_MEDICO_ID))
            val fecha = cursorAll.getString(cursorAll.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_FECHA))
            val diagnostico = cursorAll.getString(cursorAll.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_DIAGNOSTICO))
            println("  - ID: $id, PacienteID: '$pacienteIdRegistro', MédicoID: $medicoId, Fecha: $fecha, Diagnóstico: $diagnostico")
        }
        cursorAll.close()
        
        val cursor = db.query(
            TABLE_DIAGNOSTICOS,
            null,
            "$COLUMN_DIAGNOSTICO_PACIENTE_ID = ?",
            arrayOf(pacienteId),
            null,
            null,
            "$COLUMN_DIAGNOSTICO_FECHA DESC"
        )

        println("Consulta ejecutada con filtro: $COLUMN_DIAGNOSTICO_PACIENTE_ID = '$pacienteId'")
        println("Registros encontrados con filtro: ${cursor.count}")

        val diagnosticos = mutableListOf<com.example.softmedv5.modelo.DiagnosticoMedico>()
        while (cursor.moveToNext()) {
            val diagnostico = com.example.softmedv5.modelo.DiagnosticoMedico(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_ID)),
                pacienteId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_PACIENTE_ID)),
                medicoId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_MEDICO_ID)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_FECHA)),
                sintomas = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_SINTOMAS)),
                examenFisico = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_EXAMEN_FISICO)),
                diagnostico = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_DIAGNOSTICO)),
                diagnosticosSecundarios = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS)),
                tratamiento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_TRATAMIENTO)),
                medicamentos = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_MEDICAMENTOS)),
                recomendaciones = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_RECOMENDACIONES)),
                proximaCita = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_PROXIMA_CITA)),
                estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_ESTADO))
            )
            diagnosticos.add(diagnostico)
            println("Diagnóstico agregado: ${diagnostico.id} - ${diagnostico.diagnostico}")
        }
        cursor.close()
        
        println("Total de diagnósticos retornados: ${diagnosticos.size}")
        println("==========================================")
        
        return diagnosticos
    }

    /**
     * Obtiene todos los diagnósticos realizados por un médico específico
     */
    fun obtenerDiagnosticosPorMedico(medicoId: String): List<com.example.softmedv5.modelo.DiagnosticoMedico> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_DIAGNOSTICOS,
            null,
            "$COLUMN_DIAGNOSTICO_MEDICO_ID = ?",
            arrayOf(medicoId),
            null,
            null,
            "$COLUMN_DIAGNOSTICO_FECHA DESC"
        )

        val diagnosticos = mutableListOf<com.example.softmedv5.modelo.DiagnosticoMedico>()
        while (cursor.moveToNext()) {
            val diagnostico = com.example.softmedv5.modelo.DiagnosticoMedico(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_ID)),
                pacienteId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_PACIENTE_ID)),
                medicoId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_MEDICO_ID)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_FECHA)),
                sintomas = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_SINTOMAS)),
                examenFisico = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_EXAMEN_FISICO)),
                diagnostico = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_DIAGNOSTICO)),
                diagnosticosSecundarios = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS)),
                tratamiento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_TRATAMIENTO)),
                medicamentos = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_MEDICAMENTOS)),
                recomendaciones = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_RECOMENDACIONES)),
                proximaCita = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_PROXIMA_CITA)),
                estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_ESTADO))
            )
            diagnosticos.add(diagnostico)
        }
        cursor.close()
        
        return diagnosticos
    }

    /**
     * Obtiene un diagnóstico específico por su ID
     */
    fun obtenerDiagnosticoPorId(diagnosticoId: String): com.example.softmedv5.modelo.DiagnosticoMedico? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_DIAGNOSTICOS,
            null,
            "$COLUMN_DIAGNOSTICO_ID = ?",
            arrayOf(diagnosticoId),
            null,
            null,
            null
        )

        val diagnostico = if (cursor.moveToFirst()) {
            com.example.softmedv5.modelo.DiagnosticoMedico(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_ID)),
                pacienteId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_PACIENTE_ID)),
                medicoId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_MEDICO_ID)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_FECHA)),
                sintomas = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_SINTOMAS)),
                examenFisico = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_EXAMEN_FISICO)),
                diagnostico = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_DIAGNOSTICO)),
                diagnosticosSecundarios = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_DIAGNOSTICOS_SECUNDARIOS)),
                tratamiento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_TRATAMIENTO)),
                medicamentos = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_MEDICAMENTOS)),
                recomendaciones = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_RECOMENDACIONES)),
                proximaCita = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_PROXIMA_CITA)),
                estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSTICO_ESTADO))
            )
        } else {
            null
        }
        
        cursor.close()
        return diagnostico
    }
    
    /**
     * Actualiza la contraseña de un usuario por su email
     * @param email Email del usuario
     * @param nuevaContrasena Nueva contraseña encriptada
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun actualizarContrasena(email: String, nuevaContrasena: String): Boolean {
        return try {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_USUARIO_CONTRASENA, nuevaContrasena)
            }
            
            val filasActualizadas = db.update(
                TABLE_USUARIOS,
                values,
                "$COLUMN_USUARIO_EMAIL = ?",
                arrayOf(email)
            )
            
            filasActualizadas > 0
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Actualiza la información básica de un usuario
     * @param email Email del usuario
     * @param nombre Nombre completo del usuario
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun actualizarInformacionBasica(email: String, nombre: String): Boolean {
        return try {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_USUARIO_NOMBRE, nombre)
            }
            
            val filasActualizadas = db.update(
                TABLE_USUARIOS,
                values,
                "$COLUMN_USUARIO_EMAIL = ?",
                arrayOf(email)
            )
            
            filasActualizadas > 0
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Actualiza la información de contacto de un usuario
     * @param email Email del usuario
     * @param informacionContacto Información de contacto a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun actualizarInformacionContacto(email: String, informacionContacto: com.example.softmedv5.modelo.InformacionContacto): Boolean {
        return try {
            println("=== DEBUG: actualizarInformacionContacto ===")
            println("Email: $email")
            println("Teléfono: ${informacionContacto.telefono}")
            println("Dirección: ${informacionContacto.direccion}")
            
            val db = this.writableDatabase
            
            // Buscar el usuario por email para obtener su ID
            val cursor = db.query(
                TABLE_USUARIOS,
                arrayOf(COLUMN_USUARIO_ID),
                "$COLUMN_USUARIO_EMAIL = ?",
                arrayOf(email),
                null,
                null,
                null
            )
            
            if (cursor.moveToFirst()) {
                val usuarioId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USUARIO_ID))
                cursor.close()
                println("Usuario encontrado con ID: $usuarioId")
                
                // Verificar si ya existe información de contacto para este usuario
                val cursorContacto = db.query(
                    TABLE_INFORMACION_CONTACTO,
                    arrayOf(COLUMN_CONTACTO_ID),
                    "$COLUMN_CONTACTO_USUARIO_ID = ?",
                    arrayOf(usuarioId.toString()),
                    null,
                    null,
                    null
                )
                
                val values = ContentValues().apply {
                    put(COLUMN_CONTACTO_USUARIO_ID, usuarioId)
                    put(COLUMN_CONTACTO_TELEFONO, informacionContacto.telefono)
                    put(COLUMN_CONTACTO_DIRECCION, informacionContacto.direccion)
                    put(COLUMN_CONTACTO_CIUDAD, informacionContacto.ciudad)
                    put(COLUMN_CONTACTO_CODIGO_POSTAL, informacionContacto.codigoPostal)
                    put(COLUMN_CONTACTO_FECHA_NACIMIENTO, informacionContacto.fechaNacimiento)
                    put(COLUMN_CONTACTO_GENERO, informacionContacto.genero)
                    put(COLUMN_CONTACTO_DOCUMENTO_IDENTIDAD, informacionContacto.documentoIdentidad)
                    put(COLUMN_CONTACTO_TIPO_DOCUMENTO, informacionContacto.tipoDocumento)
                    put(COLUMN_CONTACTO_EMERGENCIA_CONTACTO, informacionContacto.emergenciaContacto)
                    put(COLUMN_CONTACTO_EMERGENCIA_TELEFONO, informacionContacto.emergenciaTelefono)
                    put(COLUMN_CONTACTO_FECHA_ACTUALIZACION, System.currentTimeMillis())
                }
                
                val resultado: Long = if (cursorContacto.moveToFirst()) {
                    // Actualizar información existente
                    val contactoId = cursorContacto.getLong(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_ID))
                    cursorContacto.close()
                    println("Actualizando información de contacto existente con ID: $contactoId")
                    db.update(
                        TABLE_INFORMACION_CONTACTO,
                        values,
                        "$COLUMN_CONTACTO_ID = ?",
                        arrayOf(contactoId.toString())
                    ).toLong()
                } else {
                    // Insertar nueva información
                    cursorContacto.close()
                    println("Insertando nueva información de contacto")
                    db.insert(TABLE_INFORMACION_CONTACTO, null, values)
                }
                
                println("Resultado de actualización/inserción: $resultado")
                val exito = resultado > 0
                if (exito) {
                    println("✅ Información de contacto guardada exitosamente")
                } else {
                    println("❌ Error al guardar información de contacto")
                }
                
                exito
            } else {
                cursor.close()
                println("❌ Usuario no encontrado con email: $email")
                false
            }
        } catch (e: Exception) {
            println("❌ Excepción al actualizar información de contacto: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Obtiene la información de contacto de un usuario por su email
     * @param email Email del usuario
     * @return InformacionContacto del usuario o null si no existe
     */
    fun obtenerInformacionContacto(email: String): com.example.softmedv5.modelo.InformacionContacto? {
        return try {
            val db = this.readableDatabase
            
            // Buscar el usuario por email para obtener su ID
            val cursorUsuario = db.query(
                TABLE_USUARIOS,
                arrayOf(COLUMN_USUARIO_ID),
                "$COLUMN_USUARIO_EMAIL = ?",
                arrayOf(email),
                null,
                null,
                null
            )
            
            if (cursorUsuario.moveToFirst()) {
                val usuarioId = cursorUsuario.getLong(cursorUsuario.getColumnIndexOrThrow(COLUMN_USUARIO_ID))
                cursorUsuario.close()
                
                // Buscar información de contacto del usuario
                val cursorContacto = db.query(
                    TABLE_INFORMACION_CONTACTO,
                    null,
                    "$COLUMN_CONTACTO_USUARIO_ID = ?",
                    arrayOf(usuarioId.toString()),
                    null,
                    null,
                    null
                )
                
                val informacionContacto = if (cursorContacto.moveToFirst()) {
                    com.example.softmedv5.modelo.InformacionContacto(
                        telefono = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_TELEFONO)),
                        direccion = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_DIRECCION)),
                        ciudad = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_CIUDAD)),
                        codigoPostal = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_CODIGO_POSTAL)),
                        fechaNacimiento = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_FECHA_NACIMIENTO)),
                        genero = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_GENERO)),
                        documentoIdentidad = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_DOCUMENTO_IDENTIDAD)),
                        tipoDocumento = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_TIPO_DOCUMENTO)),
                        emergenciaContacto = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_EMERGENCIA_CONTACTO)),
                        emergenciaTelefono = cursorContacto.getString(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_EMERGENCIA_TELEFONO)),
                        fechaActualizacion = cursorContacto.getLong(cursorContacto.getColumnIndexOrThrow(COLUMN_CONTACTO_FECHA_ACTUALIZACION))
                    )
                } else {
                    // Si no existe información de contacto, devolver una instancia vacía
                    com.example.softmedv5.modelo.InformacionContacto()
                }
                
                cursorContacto.close()
                informacionContacto
            } else {
                cursorUsuario.close()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Obtiene la información de contacto de un usuario por su ID
     * @param usuarioId ID del usuario
     * @return Map con la información de contacto o null si no existe
     */
    fun obtenerInformacionContacto(usuarioId: Int): Map<String, Any>? {
        return try {
            val db = this.readableDatabase
            
            // Buscar información de contacto del usuario
            val cursorContacto = db.query(
                TABLE_INFORMACION_CONTACTO,
                null,
                "$COLUMN_CONTACTO_USUARIO_ID = ?",
                arrayOf(usuarioId.toString()),
                null,
                null,
                null
            )
            
            val contacto = if (cursorContacto.moveToFirst()) {
                val contactoMap = mutableMapOf<String, Any>()
                for (i in 0 until cursorContacto.columnCount) {
                    val columnName = cursorContacto.getColumnName(i)
                    val value = when (cursorContacto.getType(i)) {
                        android.database.Cursor.FIELD_TYPE_INTEGER -> cursorContacto.getLong(i)
                        android.database.Cursor.FIELD_TYPE_STRING -> cursorContacto.getString(i)
                        else -> cursorContacto.getString(i)
                    }
                    contactoMap[columnName] = value
                }
                contactoMap
            } else {
                null
            }
            
            cursorContacto.close()
            contacto
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Elimina un usuario de la base de datos (marcado como inactivo)
     * @param usuarioId ID del usuario a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    fun eliminarUsuario(usuarioId: Int): Boolean {
        return try {
            val db = this.writableDatabase
            
            // Marcar usuario como inactivo en lugar de eliminarlo físicamente
            val values = ContentValues().apply {
                put(COLUMN_USUARIO_ACTIVO, 0L)
            }
            
            val filasActualizadas = db.update(
                TABLE_USUARIOS,
                values,
                "$COLUMN_USUARIO_ID = ?",
                arrayOf(usuarioId.toString())
            )
            
            // También eliminar información de contacto asociada
            db.delete(
                TABLE_INFORMACION_CONTACTO,
                "$COLUMN_CONTACTO_USUARIO_ID = ?",
                arrayOf(usuarioId.toString())
            )
            
            filasActualizadas > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Elimina un usuario por su email
     * @param email Email del usuario a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    fun eliminarUsuarioPorEmail(email: String): Boolean {
        return try {
            val db = this.writableDatabase
            
            // Buscar el usuario por email para obtener su ID
            val cursor = db.query(
                TABLE_USUARIOS,
                arrayOf(COLUMN_USUARIO_ID),
                "$COLUMN_USUARIO_EMAIL = ?",
                arrayOf(email),
                null,
                null,
                null
            )
            
            if (cursor.moveToFirst()) {
                val usuarioId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USUARIO_ID))
                cursor.close()
                eliminarUsuario(usuarioId.toInt())
            } else {
                cursor.close()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Método de debug: Obtiene todos los usuarios (activos e inactivos)
     * Solo para propósitos de depuración
     */
    fun obtenerTodosLosUsuariosDebug(): List<Map<String, Any>> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            null, // Sin filtro para obtener todos
            null,
            null,
            null,
            "$COLUMN_USUARIO_NOMBRE ASC"
        )

        val usuarios = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val usuario = mutableMapOf<String, Any>()
            for (i in 0 until cursor.columnCount) {
                val columnName = cursor.getColumnName(i)
                val value = when (cursor.getType(i)) {
                    android.database.Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(i)
                    android.database.Cursor.FIELD_TYPE_STRING -> cursor.getString(i)
                    else -> cursor.getString(i)
                }
                usuario[columnName] = value
            }
            usuarios.add(usuario)
        }
        cursor.close()
        return usuarios
    }

    /**
     * Obtiene los datos completos del paciente incluyendo información de contacto
     * @param email Email del paciente
     * @return Map con todos los datos del paciente o null si no existe
     */
    fun obtenerDatosCompletosPaciente(email: String): Map<String, Any>? {
        return try {
            // println("=== DEBUG: obtenerDatosCompletosPaciente ===")
            // println("Email: $email")
            
            // 1. Obtener datos básicos del usuario
            val usuarioDb = obtenerUsuarioPorEmail(email)
            if (usuarioDb == null) {
                // println("❌ Usuario no encontrado")
                return null
            }
            
            // println("Usuario encontrado: ${usuarioDb["nombre_completo"]}")
            
            // 2. Obtener información de contacto
            val informacionContacto = obtenerInformacionContacto(email)
            
            // 3. Crear mapa con todos los datos
            val datosCompletos = mutableMapOf<String, Any>()
            
            // Datos básicos del usuario
            datosCompletos.putAll(usuarioDb)
            
            // Información de contacto
            if (informacionContacto != null) {
                datosCompletos["telefono"] = informacionContacto.telefono
                datosCompletos["direccion"] = informacionContacto.direccion
                datosCompletos["ciudad"] = informacionContacto.ciudad
                datosCompletos["codigo_postal"] = informacionContacto.codigoPostal
                datosCompletos["fecha_nacimiento"] = informacionContacto.fechaNacimiento
                datosCompletos["genero"] = informacionContacto.genero
                datosCompletos["documento_identidad"] = informacionContacto.documentoIdentidad
                datosCompletos["tipo_documento"] = informacionContacto.tipoDocumento
                datosCompletos["emergencia_contacto"] = informacionContacto.emergenciaContacto
                datosCompletos["emergencia_telefono"] = informacionContacto.emergenciaTelefono
            } else {
                // Valores por defecto si no hay información de contacto
                datosCompletos["telefono"] = ""
                datosCompletos["direccion"] = ""
                datosCompletos["ciudad"] = ""
                datosCompletos["codigo_postal"] = ""
                datosCompletos["fecha_nacimiento"] = ""
                datosCompletos["genero"] = ""
                datosCompletos["documento_identidad"] = ""
                datosCompletos["tipo_documento"] = ""
                datosCompletos["emergencia_contacto"] = ""
                datosCompletos["emergencia_telefono"] = ""
            }
            
            // 4. Para pacientes, obtener datos específicos desde la tabla de datos de pacientes
            val rol = usuarioDb["rol"] as? String ?: ""
            if (rol == "PACIENTE") {
                val usuarioId = (usuarioDb["id"] as? Long)?.toInt() ?: 0
                val datosPaciente = obtenerDatosEspecificosPaciente(usuarioId)
                if (datosPaciente != null) {
                    datosCompletos["seguro"] = datosPaciente["seguro"] as? String ?: "No especificado"
                    datosCompletos["numero_seguro"] = datosPaciente["numero_seguro"] as? String ?: "No especificado"
                } else {
                    datosCompletos["seguro"] = "No especificado"
                    datosCompletos["numero_seguro"] = "No especificado"
                }
            }
            
            // println("✅ Datos completos obtenidos exitosamente")
            // println("Datos: $datosCompletos")
            
            datosCompletos
        } catch (e: Exception) {
            // println("❌ Error al obtener datos completos del paciente: ${e.message}")
            // e.printStackTrace()
            null
        }
    }
    
    /**
     * Obtiene los datos específicos de un paciente desde la tabla de datos de pacientes
     * @param usuarioId ID del usuario
     * @return Map con los datos específicos del paciente o null si no existe
     */
    fun obtenerDatosEspecificosPaciente(usuarioId: Int): Map<String, Any>? {
        return try {
            val db = this.readableDatabase
            val cursor = db.query(
                TABLE_DATOS_PACIENTES,
                null,
                "$COLUMN_DATOS_PACIENTE_USUARIO_ID = ?",
                arrayOf(usuarioId.toString()),
                null,
                null,
                null
            )
            
            val datos = if (cursor.moveToFirst()) {
                val datosMap = mutableMapOf<String, Any>()
                datosMap["seguro"] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATOS_PACIENTE_SEGURO))
                datosMap["numero_seguro"] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATOS_PACIENTE_NUMERO_SEGURO))
                datosMap["fecha_actualizacion"] = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATOS_PACIENTE_FECHA_ACTUALIZACION))
                datosMap
            } else {
                null
            }
            
            cursor.close()
            datos
        } catch (e: Exception) {
            println("Error al obtener datos específicos del paciente: ${e.message}")
            null
        }
    }
    
    /**
     * Inserta o actualiza los datos específicos de un paciente
     * @param usuarioId ID del usuario
     * @param seguro Nombre del seguro
     * @param numeroSeguro Número del seguro
     * @return true si se guardó correctamente, false en caso contrario
     */
    fun guardarDatosEspecificosPaciente(usuarioId: Int, seguro: String, numeroSeguro: String): Boolean {
        return try {
            val db = this.writableDatabase
            
            // Verificar si ya existen datos para este paciente
            val cursor = db.query(
                TABLE_DATOS_PACIENTES,
                arrayOf(COLUMN_DATOS_PACIENTE_ID),
                "$COLUMN_DATOS_PACIENTE_USUARIO_ID = ?",
                arrayOf(usuarioId.toString()),
                null,
                null,
                null
            )
            
            val values = ContentValues().apply {
                put(COLUMN_DATOS_PACIENTE_USUARIO_ID, usuarioId)
                put(COLUMN_DATOS_PACIENTE_SEGURO, seguro)
                put(COLUMN_DATOS_PACIENTE_NUMERO_SEGURO, numeroSeguro)
                put(COLUMN_DATOS_PACIENTE_FECHA_ACTUALIZACION, System.currentTimeMillis())
            }
            
            val resultado: Long = if (cursor.moveToFirst()) {
                // Actualizar datos existentes
                val datosId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATOS_PACIENTE_ID))
                cursor.close()
                db.update(
                    TABLE_DATOS_PACIENTES,
                    values,
                    "$COLUMN_DATOS_PACIENTE_ID = ?",
                    arrayOf(datosId.toString())
                ).toLong()
            } else {
                // Insertar nuevos datos
                cursor.close()
                db.insert(TABLE_DATOS_PACIENTES, null, values)
            }
            
            resultado > 0
        } catch (e: Exception) {
            println("Error al guardar datos específicos del paciente: ${e.message}")
            false
        }
    }
    
    /**
     * Elimina todos los usuarios y datos relacionados de la base de datos
     * SOLO USAR PARA LIMPIAR DATOS DE PRUEBA
     */
    fun limpiarTodosLosUsuarios(): Boolean {
        return try {
            val db = this.writableDatabase
            
            // Eliminar en orden para respetar las restricciones de clave foránea
            db.delete(TABLE_DATOS_PACIENTES, null, null)
            db.delete(TABLE_INFORMACION_CONTACTO, null, null)
            db.delete(TABLE_DIAGNOSTICOS, null, null)
            db.delete(TABLE_MENSAJES, null, null)
            db.delete(TABLE_PACIENTES, null, null)
            db.delete(TABLE_USUARIOS, null, null)
            
            true
        } catch (e: Exception) {
            println("Error al limpiar usuarios: ${e.message}")
            false
        }
    }
}
