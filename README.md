# sOFTmED v5 - Sistema Médico

## Descripción

sOFTmED v5 es un sistema médico desarrollado en Android utilizando el paradigma de **Programación Orientada a Objetos (POO)**. El sistema está diseñado para ser escalable, mantenible y fácil de comprender, utilizando términos en español para mejorar la legibilidad del código.

## Características Principales

### 🏗️ Arquitectura POO
- **Herencia**: Clase base `Usuario` con clases específicas para cada rol
- **Polimorfismo**: Métodos abstractos implementados por cada clase hija
- **Encapsulamiento**: Propiedades protegidas y métodos públicos
- **Patrón Factory**: `GestorUsuarios` para crear instancias según el rol
- **Patrón Singleton**: `GestorAutenticacion` y `GestorCitas` para manejo de sesiones y citas

### 🔐 Sistema de Autenticación
- **Registro de usuarios** con validación de datos
- **Inicio de sesión** con credenciales seguras
- **Gestión de sesiones** con tokens únicos
- **Validación de permisos** por rol de usuario
- **Cierre de sesión** seguro

### 📅 Sistema Completo de Citas
- **Solicitud de citas** con fecha, hora, especialidad y medio
- **Gestión de citas** (ver, cancelar, reprogramar)
- **Validación de disponibilidad** en tiempo real
- **Estados de cita** (Pendiente, Confirmada, Cancelada, Reprogramada, Completada)
- **Medios de atención** (Presencial, Virtual)
- **Especialidades médicas** predefinidas
- **Horarios disponibles** configurados

### 👥 Sistema de Roles
El sistema incluye cuatro tipos de usuarios con diferentes niveles de acceso:

1. **Gerente**
   - Acceso total al sistema
   - Gestión de usuarios y médicos
   - Generación de reportes administrativos
   - Configuración del sistema

2. **Médico** (NUEVO - FUNCIONALIDADES COMPLETAS)
   - **Menú específico de funcionalidades médicas**:
     - 📅 **Gestión de Citas y Agenda**: Programar, reprogramar, cancelar citas, ver agenda completa
     - 🏥 **Atención Médica**: Iniciar consultas, ver pacientes en espera, gestionar urgencias
     - 💊 **Prescripción y Órdenes Médicas**: Crear prescripciones, órdenes de exámenes, renovar medicamentos
     - 📊 **Revisión de Resultados**: Ver resultados de exámenes, detectar anomalías, comparar históricos
     - 📋 **Historial Clínico**: Buscar pacientes, ver historiales completos, agregar notas médicas
     - 💬 **Comunicación y Seguimiento**: Enviar mensajes, seguimiento de pacientes, recordatorios
   - Gestión completa de pacientes
   - Creación y actualización de historiales médicos
   - Prescripción de medicamentos y órdenes médicas
   - Generación de diagnósticos y tratamientos

3. **Paciente**
   - **Sistema completo de citas**:
     - 📅 Solicitar cita médica con validación completa
     - 📋 Ver historial de citas (futuras y pasadas)
     - ❌ Cancelar citas confirmadas o pendientes
     - 🔄 Reprogramar citas con nueva fecha/hora
     - 💻 Seleccionar medio (presencial o virtual)
     - 👨‍⚕️ Elegir especialidad médica
   - **Menú específico de funcionalidades**:
     - 📊 Consultar historial clínico personal
     - 📄 Descargar recetas o resultados de exámenes
     - 💻 Acceder a teleconsultas o chat médico
   - Gestión de información de contacto
   - Actualización de información personal

4. **Personal Administrativo**
   - **Menú específico de funcionalidades**:
     - 📅 Gestión de citas
     - 👥 Registro y actualización de pacientes
     - 💰 Facturación y pagos
     - 📄 Gestión de documentos clínicos
     - 💬 Comunicación interna
     - 📊 Reportes y control
   - Gestión de citas y pacientes
   - Control de inventario
   - Generación de reportes básicos
   - Gestión de facturación

## Flujo de Usuario

### 1. **Selección de Rol**
- El usuario selecciona su rol desde un menú desplegable
- Roles disponibles: Gerente, Médico, Paciente, Personal Administrativo

### 2. **Autenticación**
- **Registro**: Para nuevos usuarios
  - Nombre completo
  - Email válido
  - Contraseña (mínimo 6 caracteres)
  - Confirmación de contraseña
- **Inicio de Sesión**: Para usuarios existentes
  - Email registrado
  - Contraseña correcta

### 3. **Pantalla Principal**
- Información detallada del usuario
- Lista de permisos disponibles
- **Funcionalidades específicas por rol**:
  - **Pacientes**: Acceso directo a menú de funcionalidades específicas
  - **Personal Administrativo**: Acceso directo a menú administrativo específico
  - **Médicos**: Acceso directo a menú médico específico (NUEVO)
  - **Otros roles**: Información de funcionalidades disponibles
- Opción de cerrar sesión

### 4. **Menú de Funcionalidades del Médico (NUEVO)**
Cuando un médico accede a "Ver Funcionalidades", se abre una nueva pantalla con:

#### **📅 Gestión de Citas y Agenda**
- **Resumen de agenda**: Total de citas, citas para hoy, confirmadas, pendientes
- **Ver agenda completa**: Vista detallada de todas las citas programadas
- **Citas para hoy**: Filtro de citas del día actual
- **Citas pendientes**: Citas que requieren confirmación
- **Programar nueva cita**: Formulario completo con paciente, fecha, especialidad, motivo
- **Reprogramar citas**: Selección de cita y nueva fecha/hora
- **Cancelar citas**: Confirmación antes de cancelar
- **Lista de citas**: Vista detallada con estado, prioridad, tipo de consulta

#### **🏥 Atención Médica**
- **Resumen de consultas**: Total, en curso, pendientes, finalizadas, urgencias
- **Iniciar consulta**: Formulario para nueva consulta médica
- **Pacientes en espera**: Lista de pacientes pendientes de atención
- **Consultas activas**: Consultas en curso con detalles
- **Finalizar consulta**: Selección y cierre de consultas
- **Gestión de urgencias**: Casos críticos que requieren atención inmediata
- **Lista de consultas**: Vista detallada con síntomas, diagnóstico, tratamiento

#### **💊 Prescripción y Órdenes Médicas**
- **Resumen**: Total de prescripciones y órdenes, activas, pendientes, vencidas
- **Nueva prescripción**: Formulario completo con medicamento, dosis, duración, indicaciones
- **Nueva orden médica**: Formulario para exámenes y estudios
- **Ver prescripciones**: Lista de prescripciones activas
- **Ver órdenes**: Lista de órdenes pendientes
- **Renovar prescripción**: Selección y renovación de prescripciones vencidas
- **Lista detallada**: Estado, prioridad, tipo, información completa

#### **📊 Revisión de Resultados**
- **Resumen**: Total de resultados, pendientes, anormales, revisados hoy
- **Ver resultados**: Todos los resultados médicos disponibles
- **Resultados pendientes**: Resultados que requieren revisión médica
- **Resultados anormales**: Valores fuera de rango con alertas
- **Comparar resultados**: Análisis histórico y tendencias
- **Generar reportes**: Diferentes tipos de reportes de resultados

#### **📋 Historial Clínico**
- **Resumen**: Total de pacientes, historiales completos, notas agregadas
- **Buscar paciente**: Por nombre, ID, email o teléfono
- **Ver historial**: Información completa del paciente
- **Agregar nota**: Diferentes tipos de notas médicas
- **Actualizar historial**: Modificar información del paciente
- **Exportar historial**: En diferentes formatos (PDF, Excel, HTML)

#### **💬 Comunicación y Seguimiento**
- **Resumen**: Mensajes enviados/recibidos, pacientes en seguimiento, recordatorios
- **Enviar mensaje**: A pacientes, personal médico o administrativo
- **Ver mensajes**: Mensajes recibidos con estado de lectura
- **Seguimiento de pacientes**: Estado y evolución de pacientes
- **Recordatorios**: Programación y gestión de recordatorios
- **Notificaciones**: Alertas del sistema y gestión de notificaciones

### 5. **Menú de Funcionalidades del Paciente**
Cuando un paciente accede a "Ver Funcionalidades", se abre una nueva pantalla con:
- **🔔 Alertas y Mis Citas**: Vista rápida de alertas y resumen de citas
- **📅 Solicitar Cita**: Sistema completo de agendamiento de citas
- **📊 Consultar Historial Clínico**: Acceso a historial médico personal
- **📄 Descargar Recetas**: Descarga de documentos médicos
- **💻 Teleconsultas**: Acceso a atención médica remota
- **⬅️ Volver**: Regreso a la pantalla principal

### 6. **Sistema de Citas**

#### **Alertas y Resumen de Citas**
1. **Resumen general**: Total de citas, confirmadas, pendientes y citas de hoy
2. **Alertas inteligentes**:
   - Citas programadas para hoy
   - Citas programadas para mañana
   - Citas pendientes de confirmación
   - Citas en los próximos 3 días
3. **Vista rápida**: Próximas 3 citas con información esencial
4. **Navegación directa**: Botones para ver todas las citas o crear nueva
5. **Tiempo restante**: Cálculo automático del tiempo hasta cada cita

#### **Solicitar Cita**
1. **Selección de fecha**: DatePicker con validación (no fechas pasadas)
2. **Selección de hora**: TimePicker con horarios disponibles
3. **Especialidad médica**: Spinner con 14 especialidades predefinidas
4. **Medio de atención**: Presencial o Virtual
5. **Motivo de consulta**: Campo de texto multilínea
6. **Validación completa**: Disponibilidad, formato, campos obligatorios
7. **Confirmación**: Diálogo con detalles de la cita creada

#### **📅 Mis Citas**
1. **Vista de citas**: Separadas en "Próximas" e "Historial"
2. **Información detallada**: ID, especialidad, fecha/hora, medio, estado
3. **Estados visuales**: Colores según estado (Verde=Confirmada, Amarillo=Pendiente, Rojo=Cancelada)
4. **Acciones disponibles**:
   - **Cancelar**: Confirmación antes de cancelar
   - **Reprogramar**: Diálogo para nueva fecha/hora
5. **Navegación**: Botón para crear nueva cita

#### **📊 Consultar Historial Clínico**
1. **Consultas médicas anteriores**
2. **Diagnósticos realizados**
3. **Medicamentos prescritos**
4. **Resultados de exámenes**
5. **Vacunas aplicadas**
6. **Alergias registradas**

## Estructura del Proyecto

```
app/src/main/java/com/example/softmedv5/
├── MainActivity.kt                    # Actividad principal (selección de roles)
├── AutenticacionActivity.kt           # Actividad de registro/login
├── PantallaPrincipalActivity.kt       # Actividad principal del sistema
├── InformacionContactoActivity.kt     # Actividad de gestión de contacto
├── FuncionalidadesPacienteActivity.kt # Actividad específica para pacientes
├── FuncionalidadesAdministrativoActivity.kt # Actividad específica para personal administrativo
├── FuncionalidadesMedicoActivity.kt   # Actividad específica para médicos (NUEVO)
├── GestionCitasMedicoActivity.kt      # Gestión de citas del médico (NUEVO)
├── AtencionMedicaActivity.kt          # Atención médica (NUEVO)
├── PrescripcionOrdenesActivity.kt     # Prescripción y órdenes (NUEVO)
├── RevisionResultadosActivity.kt      # Revisión de resultados (NUEVO)
├── HistorialClinicoActivity.kt        # Historial clínico (NUEVO)
├── ComunicacionSeguimientoActivity.kt # Comunicación y seguimiento (NUEVO)
├── GestionCitasAdministrativoActivity.kt    # Actividad de gestión administrativa de citas
├── RegistroPacientesActivity.kt             # Actividad de registro y gestión de pacientes
├── SolicitarCitaActivity.kt           # Actividad para solicitar citas
├── MisCitasActivity.kt                # Actividad para gestionar citas
├── AlertasCitasActivity.kt            # Actividad de alertas y resumen
├── modelo/                           # Clases del modelo de datos
│   ├── Usuario.kt                    # Clase base abstracta
│   ├── SesionUsuario.kt              # Clase para gestión de sesiones
│   ├── InformacionContacto.kt        # Clase para información de contacto
│   ├── Cita.kt                       # Clase para gestión de citas
│   ├── PerfilPaciente.kt             # Clase para perfil completo de pacientes
│   ├── Gerente.kt                    # Clase específica para gerentes
│   ├── Medico.kt                     # Clase específica para médicos
│   ├── Paciente.kt                   # Clase específica para pacientes
│   └── PersonalAdministrativo.kt     # Clase específica para personal administrativo
└── util/                             # Utilidades del sistema
    ├── GestorUsuarios.kt             # Gestor de usuarios (Factory Pattern)
    ├── GestorAutenticacion.kt        # Gestor de autenticación (Singleton)
    └── GestorCitas.kt                # Gestor de citas (Singleton)
```

## Tecnologías Utilizadas

- **Kotlin**: Lenguaje principal de desarrollo
- **Android SDK**: Framework de desarrollo móvil
- **Material Design**: Componentes de UI modernos
- **CardView**: Componente para diseño de tarjetas
- **Spinner**: Componente para selección de roles y especialidades
- **EditText**: Campos de entrada con validación
- **ScrollView**: Navegación con scroll
- **AlertDialog**: Diálogos informativos para funcionalidades
- **DatePickerDialog**: Selector de fechas para citas
- **TimePickerDialog**: Selector de horas para citas

## Instalación y Uso

### Requisitos Previos
- Android Studio Arctic Fox o superior
- Android SDK API 24+
- Kotlin 1.8+

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd sOFTmEDv5
   ```

2. **Abrir en Android Studio**
   - Abrir Android Studio
   - Seleccionar "Open an existing project"
   - Navegar a la carpeta del proyecto y seleccionarla

3. **Sincronizar dependencias**
   - Esperar a que Gradle sincronice las dependencias
   - Resolver cualquier conflicto de dependencias si es necesario

4. **Ejecutar la aplicación**
   - Conectar un dispositivo Android o iniciar un emulador
   - Presionar el botón "Run" en Android Studio

### Uso de la Aplicación

1. **Selección de Rol**
   - Seleccionar el rol de usuario desde el menú desplegable
   - Presionar "INGRESAR AL SISTEMA"

2. **Autenticación**
   - **Nuevo usuario**: Completar formulario de registro
   - **Usuario existente**: Cambiar a modo "Inicio de Sesión"
   - Validar credenciales y acceder al sistema

3. **Navegación en el Sistema**
   - Ver información personal y permisos
   - **Para Pacientes**: Acceder al menú específico de funcionalidades
   - **Para otros roles**: Explorar funcionalidades disponibles
   - Gestionar información de contacto
   - Cerrar sesión cuando sea necesario

### Funcionalidades Específicas del Personal Administrativo

Al acceder como personal administrativo y presionar "Ver Funcionalidades":

1. **📅 Gestión de Citas (IMPLEMENTADO)**
   - **Vista completa de todas las citas del sistema**
   - **Confirmar citas pendientes** enviadas por pacientes
   - **Cancelar citas** por solicitud o conflictos
   - **Reprogramar citas** con nueva fecha y hora
   - **Resumen estadístico** en tiempo real
   - **Organización por estado**: Pendientes, Confirmadas, Canceladas
   - **Validación de disponibilidad** al reprogramar
   - **Interfaz intuitiva** con botones de acción específicos
   - **Citas de demostración** para pruebas del sistema

2. **👥 Registro y Actualización de Pacientes (IMPLEMENTADO)**
   - **Crear nuevo perfil de paciente** con datos personales completos
   - **Actualizar información** (teléfono, dirección, seguro, etc.)
   - **Escanear y adjuntar documentos** (carnet, referencias médicas)
   - **Validar cobertura médica** y documentos de identidad
   - **Búsqueda avanzada** de pacientes por múltiples criterios
   - **Gestión de documentos** con estado de validación
   - **Interfaz intuitiva** con formularios completos
   - **Validación en tiempo real** de campos obligatorios
   - **Pacientes de demostración** para pruebas del sistema

3. **💰 Facturación y Pagos (IMPLEMENTADO)**
   - **Generar facturas** por consultas, exámenes o procedimientos
   - **Registrar pagos** en efectivo, tarjeta o seguro
   - **Aplicar descuentos** o códigos de cobertura
   - **Emitir comprobantes** electrónicos automáticamente
   - **Cálculo automático** de IVA (19%)
   - **Gestión de estados** de facturas (Pendiente, Pagada)
   - **Resumen financiero** en tiempo real
   - **Múltiples métodos de pago** con validación
   - **Facturas de demostración** para pruebas del sistema

4. **📄 Gestión de Documentos Clínicos (IMPLEMENTADO)**
   - **Digitalización de documentos**: Escaneo automático con OCR y detección de bordes
   - **Subida de referencias externas**: Derivaciones y documentos de otros centros médicos
   - **Organización de archivos**: Por paciente, fecha, tipo de documento, especialidad, estado
   - **Estado de documentos**: Seguimiento de documentos solicitados y en procesamiento
   - **Búsqueda avanzada**: Por paciente, tipo, fecha, estado con múltiples criterios
   - **Descarga y compartición**: Acceso a documentos digitalizados
   - **Dashboard interactivo**: Resumen de documentos en tiempo real
   - **Sistema de prioridades**: Crítica, Alta, Normal para documentos urgentes
   - **Documentos de demostración**: Datos de prueba para validar funcionalidades
   - **Interfaz administrativa**: Diseñada específicamente para gestión de documentos

5. **💬 Comunicación Interna (IMPLEMENTADO)**
   - **Notificar al médico** cuando un paciente ha llegado
   - **Contactar al paciente** para recordatorios o cambios
   - **Ver mensajes** o alertas administrativas del sistema
   - **Múltiples métodos** de contacto (llamada, SMS, email, WhatsApp)
   - **Sistema de prioridades** para mensajes críticos
   - **Mensajes internos** entre personal administrativo
   - **Registro completo** de todas las comunicaciones
   - **Notificaciones automáticas** para mensajes urgentes

6. **📊 Reportes y Control (IMPLEMENTADO)**
   - **Generar reportes** de atención diaria, cancelaciones o ingresos
   - **Ver carga de trabajo** por médico o especialidad
   - **Reportar inconsistencias** o errores al administrador del sistema
   - **Análisis de tendencias** y estadísticas en tiempo real
   - **Exportación automática** a PDF con gráficos
   - **Sistema de prioridades** para errores críticos
   - **Reportes de demostración** para validar funcionalidades
   - **Dashboard interactivo** con resumen de métricas

### Funcionalidades Específicas del Paciente

Al acceder como paciente y presionar "Ver Funcionalidades":

1. **🔔 Alertas y Mis Citas**
   - Resumen general de todas las citas
   - Alertas inteligentes para citas próximas
   - Vista rápida de las próximas 3 citas
   - Cálculo de tiempo restante hasta cada cita
   - Navegación directa a gestión completa de citas
   - Acceso rápido para crear nueva cita

2. **📅 Solicitar Cita**
   - Selección de fecha y hora con validación
   - Elección de especialidad médica (14 opciones)
   - Selección de medio (Presencial/Virtual)
   - Descripción del motivo de consulta
   - Confirmación y creación de cita
   - Validación de disponibilidad en tiempo real

3. **📅 Mis Citas**
   - Vista de citas próximas y pasadas
   - Información detallada de cada cita
   - Cancelación de citas con confirmación
   - Reprogramación con nueva fecha/hora
   - Estados visuales con colores
   - Acceso directo a nueva cita

4. **📊 Consultar Historial Clínico**
   - Consultas médicas anteriores
   - Diagnósticos realizados
   - Medicamentos prescritos
   - Resultados de exámenes
   - Vacunas aplicadas
   - Alergias registradas

5. **📄 Descargar Recetas**
   - Recetas médicas actuales y anteriores
   - Resultados de análisis
   - Informes médicos
   - Certificados médicos
   - Descarga en formato PDF

6. **💻 Teleconsultas**
   - Videollamada con médico
   - Chat en tiempo real
   - Consulta por mensaje de voz
   - Envío de fotos para diagnóstico
   - Programación de seguimiento

## Sistema de Citas - Características Técnicas

### **Modelo de Datos**
```kotlin
data class Cita(
    val id: String,
    val pacienteId: String,
    val fechaHora: LocalDateTime,
    val especialidad: String,
    val medio: MedioCita,
    val opcion: String,
    val estado: EstadoCita,
    val fechaCreacion: LocalDateTime,
    val fechaModificacion: LocalDateTime
)
```

### **Estados de Cita**
- **PENDIENTE**: Cita creada, pendiente de confirmación
- **CONFIRMADA**: Cita confirmada por el sistema
- **CANCELADA**: Cita cancelada por el paciente
- **REPROGRAMADA**: Cita reprogramada a nueva fecha/hora
- **COMPLETADA**: Cita ya realizada

### **Medios de Atención**
- **PRESENCIAL**: Consulta en el consultorio médico
- **VIRTUAL**: Consulta por videollamada o chat

### **Especialidades Disponibles**
- Medicina General, Cardiología, Dermatología, Endocrinología
- Gastroenterología, Ginecología, Neurología, Oftalmología
- Ortopedia, Pediatría, Psiquiatría, Radiología
- Traumatología, Urología

### **Horarios Disponibles**
- Mañana: 08:00, 08:30, 09:00, 09:30, 10:00, 10:30, 11:00, 11:30
- Tarde: 14:00, 14:30, 15:00, 15:30, 16:00, 16:30, 17:00, 17:30

### **Validaciones Implementadas**
- ✅ Fecha no puede ser en el pasado
- ✅ Hora debe estar en horarios disponibles (validación automática)
- ✅ Especialidad debe ser válida
- ✅ No puede haber conflictos de horario
- ✅ Campos obligatorios completos
- ✅ Solo citas futuras se pueden cancelar/reprogramar
- ✅ Selector de hora personalizado con solo horarios válidos

## Gestión Administrativa de Citas - Características Técnicas

### **Funcionalidades Implementadas**
- ✅ **Vista completa del sistema**: Todas las citas organizadas por estado
- ✅ **Confirmación de citas**: Cambio de estado de PENDIENTE a CONFIRMADA
- ✅ **Cancelación administrativa**: Cancelación por parte del personal administrativo
- ✅ **Reprogramación**: Cambio de fecha y hora con validación
- ✅ **Resumen estadístico**: Conteo de citas por estado en tiempo real
- ✅ **Interfaz específica**: Diseñada exclusivamente para personal administrativo
- ✅ **Citas de demostración**: Datos de prueba para validar funcionalidades

### **Flujo de Gestión Administrativa**
1. **Acceso**: Personal administrativo → "Ver Funcionalidades" → "Gestión de Citas"
2. **Vista inicial**: Resumen estadístico y lista de citas organizadas
3. **Citas pendientes**: Sección prioritaria con botón "Confirmar"
4. **Citas confirmadas**: Sección con opciones de "Reprogramar" y "Cancelar"
5. **Acciones disponibles**:
   - **Confirmar**: Diálogo de confirmación → Cambio de estado
   - **Reprogramar**: Selector de fecha/hora → Validación → Actualización
   - **Cancelar**: Diálogo de confirmación → Cambio de estado

### **Características de Seguridad**
- ✅ **Solo personal administrativo** puede acceder a esta funcionalidad
- ✅ **Validación de permisos** antes de cada acción
- ✅ **Confirmación obligatoria** para acciones críticas
- ✅ **Registro de modificaciones** con fecha y hora
- ✅ **Prevención de conflictos** al reprogramar

### **Interfaz de Usuario**
- **Resumen estadístico**: Total de citas, pendientes, confirmadas, canceladas
- **Organización visual**: Separación clara por estado de cita
- **Botones de acción**: Específicos según el estado de cada cita
- **Colores informativos**: Verde (confirmada), Amarillo (pendiente), Rojo (cancelada)
- **Navegación intuitiva**: Botón volver y recarga automática de datos

### **Métodos del Gestor de Citas (Nuevos)**
```kotlin
// Métodos específicos para gestión administrativa
fun obtenerTodasLasCitas(): List<Cita>
fun obtenerCitasPorEstado(estado: EstadoCita): List<Cita>
fun obtenerCitasPendientes(): List<Cita>
fun obtenerCitasConfirmadas(): List<Cita>
fun obtenerCitasCanceladas(): List<Cita>
fun confirmarCita(id: String): ResultadoCita
fun crearCitasDemostracion(): Unit
```

## Gestión de Pacientes - Características Técnicas

### **Funcionalidades Implementadas**
- ✅ **Creación de perfiles**: Formulario completo con validación
- ✅ **Actualización de información**: Edición de todos los campos
- ✅ **Escaneo de documentos**: Simulación de captura de documentos
- ✅ **Validación de cobertura**: Verificación de seguros médicos
- ✅ **Búsqueda avanzada**: Por nombre, email, documento
- ✅ **Gestión de documentos**: Estado de validación visual
- ✅ **Interfaz administrativa**: Diseñada específicamente para personal administrativo
- ✅ **Pacientes de demostración**: Datos de prueba para validar funcionalidades

### **Modelo de Datos del Paciente**
```kotlin
data class PerfilPaciente(
    val id: String,
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val seguro: String,
    val numeroSeguro: String,
    val fechaNacimiento: String,
    val documentoIdentidad: String,
    val fechaRegistro: String,
    val estado: String,
    val documentosAdjuntos: List<String>,
    val validaciones: Map<String, Boolean>
)
```

### **Campos del Formulario**
- **Obligatorios**: Nombre, Email, Teléfono, Documento de Identidad
- **Opcionales**: Dirección, Seguro Médico, Número de Seguro, Fecha de Nacimiento
- **Validación**: Formato de email, teléfono, documento
- **Selector de fecha**: DatePicker para fecha de nacimiento

### **Funcionalidades de Documentos**
- **📷 Escanear Carnet de Identidad**: Captura y procesamiento automático
- **📄 Adjuntar Referencias Médicas**: Subida de archivos PDF/imágenes
- **🏥 Escanear Carnet de Seguro**: Validación de cobertura
- **📋 Ver Documentos Adjuntos**: Lista de documentos del paciente

### **Validaciones Implementadas**
- ✅ **Campos obligatorios**: Validación en tiempo real
- ✅ **Formato de email**: Validación de estructura
- ✅ **Formato de teléfono**: Validación de números
- ✅ **Documento de identidad**: Validación de formato
- ✅ **Cobertura médica**: Verificación de vigencia
- ✅ **Duplicados**: Prevención de registros duplicados

### **Búsqueda de Pacientes**
- **Criterios de búsqueda**:
  - Nombre completo o parcial
  - Dirección de email
  - Número de documento
  - Número de seguro
- **Resultados**: Lista filtrada con información resumida
- **Navegación**: Acceso directo a edición desde resultados

### **Interfaz de Usuario**
- **Resumen estadístico**: Total de pacientes, validados, pendientes
- **Lista de pacientes**: Cards con información esencial
- **Estados visuales**: Verde (validado), Amarillo (pendiente)
- **Botones de acción**: Editar, Documentos, Validar
- **Formularios modales**: Diálogos para crear/editar pacientes

### **Flujo de Gestión de Pacientes**
1. **Acceso**: Personal administrativo → "Ver Funcionalidades" → "Registro y Actualización de Pacientes"
2. **Vista inicial**: Resumen y lista de pacientes registrados
3. **Crear paciente**: Formulario completo con validación
4. **Editar paciente**: Modificación de información existente
5. **Gestionar documentos**: Escaneo y adjuntar documentos
6. **Validar información**: Verificación de datos y cobertura

### **Características de Seguridad**
- ✅ **Solo personal administrativo** puede acceder a esta funcionalidad
- ✅ **Validación de permisos** antes de cada acción
- ✅ **Protección de datos personales** con encriptación
- ✅ **Registro de cambios** con timestamp
- ✅ **Confirmación obligatoria** para acciones críticas

## Facturación y Pagos - Características Técnicas

### **Funcionalidades Implementadas**
- ✅ **Generación de facturas**: Formulario completo con cálculo automático
- ✅ **Registro de pagos**: Múltiples métodos de pago con validación
- ✅ **Aplicación de descuentos**: Porcentajes y códigos de cobertura
- ✅ **Emisión de comprobantes**: Electrónicos automáticos
- ✅ **Cálculo automático de IVA**: 19% sobre subtotal menos descuento
- ✅ **Gestión de estados**: Pendiente, Pagada, Cancelada
- ✅ **Resumen financiero**: Estadísticas en tiempo real
- ✅ **Interfaz administrativa**: Diseñada específicamente para facturación
- ✅ **Facturas de demostración**: Datos de prueba para validar funcionalidades

### **Modelo de Datos de Factura**
```kotlin
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
```

### **Tipos de Servicios Disponibles**
- **Consulta Médica**: Atención médica general y especializada
- **Examen de Laboratorio**: Análisis clínicos y pruebas
- **Procedimiento Quirúrgico**: Cirugías y procedimientos invasivos
- **Radiografía**: Estudios de imagen
- **Terapia**: Tratamientos terapéuticos
- **Medicamentos**: Farmacia y recetas

### **Métodos de Pago Soportados**
- **💵 Efectivo**: Pago en efectivo con recibo
- **💳 Tarjeta de Débito**: Pago con tarjeta de débito
- **💳 Tarjeta de Crédito**: Pago con tarjeta de crédito
- **🏥 Seguro Médico**: Facturación directa a EPS/IPS
- **📱 Transferencia Bancaria**: Pago por transferencia

### **Tipos de Descuentos**
- **Seguro médico**: 10-20% según cobertura
- **Paciente frecuente**: 5-15% por lealtad
- **Descuento por volumen**: 10-25% para múltiples servicios
- **Descuento especial**: Según autorización administrativa

### **Tipos de Comprobantes Electrónicos**
- **Factura Electrónica**: Para servicios médicos (obligatorio)
- **Recibo de Pago**: Confirmación de pago realizado
- **Comprobante de Cobertura**: Para seguros médicos
- **Certificado de Pago**: Para trámites oficiales

### **Cálculo Automático de Impuestos**
- **Base imponible**: Subtotal - Descuento
- **IVA (19%)**: Calculado automáticamente sobre la base imponible
- **Total**: Subtotal - Descuento + IVA
- **Validación**: Prevención de totales negativos

### **Estados de Factura**
- **PENDIENTE**: Factura generada, pendiente de pago
- **PAGADA**: Factura pagada completamente
- **CANCELADA**: Factura cancelada por solicitud

### **Interfaz de Usuario**
- **Resumen financiero**: Total facturas, pagadas, pendientes, ingresos
- **Lista de facturas**: Cards con información esencial
- **Estados visuales**: Verde (pagada), Amarillo (pendiente), Rojo (cancelada)
- **Botones de acción**: Ver detalle, Registrar pago, Emitir comprobante
- **Formularios modales**: Diálogos para crear facturas y registrar pagos

### **Flujo de Facturación**
1. **Acceso**: Personal administrativo → "Ver Funcionalidades" → "Facturación y Pagos"
2. **Vista inicial**: Resumen financiero y lista de facturas
3. **Generar factura**: Formulario completo con validación
4. **Registrar pago**: Selección de método y registro de transacción
5. **Aplicar descuento**: Porcentaje y motivo del descuento
6. **Emitir comprobante**: Generación automática de documentos

### **Características de Seguridad**
- ✅ **Solo personal administrativo** puede acceder a esta funcionalidad
- ✅ **Validación de montos** antes de registrar pagos
- ✅ **Registro de transacciones** con timestamp y referencia
- ✅ **Confirmación obligatoria** para acciones críticas
- ✅ **Cálculo automático** para prevenir errores manuales
- ✅ **Comprobantes electrónicos** con firma digital

## Comunicación Interna - Características Técnicas

### **Funcionalidades Implementadas**
- ✅ **Notificaciones a médicos**: Cuando un paciente ha llegado
- ✅ **Contacto a pacientes**: Recordatorios, cambios y confirmaciones
- ✅ **Mensajes internos**: Entre personal administrativo
- ✅ **Múltiples métodos**: Llamada, SMS, Email, WhatsApp, Recordatorio automático
- ✅ **Sistema de prioridades**: Crítica, Alta, Normal para mensajes
- ✅ **Registro completo**: Historial de todas las comunicaciones
- ✅ **Dashboard interactivo**: Resumen de mensajes en tiempo real
- ✅ **Mensajes de demostración**: Datos de prueba para validar funcionalidades

### **Modelo de Datos de Mensaje**
```kotlin
data class Mensaje(
    val id: String,
    val tipo: String,
    val remitente: String,
    val destinatario: String,
    val asunto: String,
    val contenido: String,
    val fecha: String,
    val estado: String,
    val prioridad: String
)
```

### **Tipos de Mensajes Disponibles**
- **👨‍⚕️ Notificación Médico**: Cuando un paciente ha llegado para su cita
- **👤 Contacto Paciente**: Recordatorios, cambios de horario, confirmaciones
- **📨 Mensaje Interno**: Comunicación entre personal administrativo
- **🚨 Alerta Administrativa**: Notificaciones del sistema
- **📋 Recordatorio Paciente**: Recordatorios automáticos de citas

### **Estados de Mensaje**
- **ENVIADO**: Mensaje enviado exitosamente
- **LEÍDO**: Mensaje leído por el destinatario
- **PENDIENTE**: Mensaje en cola de envío
- **CANCELADO**: Mensaje cancelado por solicitud

### **Sistema de Prioridades**
- **🚨 CRÍTICA**: Mensajes urgentes que requieren atención inmediata
- **⚠️ ALTA**: Mensajes importantes que afectan la operación
- **📋 NORMAL**: Mensajes informativos y recordatorios

### **Métodos de Contacto Disponibles**
- **📞 Llamada telefónica**: Contacto directo por teléfono
- **📱 Mensaje de texto (SMS)**: Envío de SMS automático
- **📧 Email**: Comunicación por correo electrónico
- **💬 WhatsApp**: Mensajería instantánea
- **📋 Recordatorio automático**: Notificaciones del sistema

### **Funcionalidades de Notificación**
- **Notificación inmediata**: Para mensajes críticos
- **Registro de entrega**: Confirmación de recepción
- **Seguimiento de estado**: Control de mensajes enviados
- **Historial completo**: Registro de todas las comunicaciones
- **Búsqueda y filtrado**: Por tipo, destinatario, fecha

### **Interfaz de Usuario**
- **Dashboard principal**: Resumen de todos los mensajes
- **Botones de acción**: Notificar médico, Contactar paciente, Ver mensajes, Nuevo mensaje
- **Estados visuales**: Verde (enviado), Azul (leído), Amarillo (pendiente)
- **Prioridades visuales**: Rojo (crítica), Naranja (alta), Verde (normal)
- **Formularios modales**: Diálogos para configurar mensajes específicos

### **Flujo de Comunicación**
1. **Acceso**: Personal administrativo → "Ver Funcionalidades" → "Comunicación Interna"
2. **Selección**: Elegir tipo de comunicación (Notificar médico, Contactar paciente, etc.)
3. **Configuración**: Definir destinatario, mensaje, prioridad
4. **Envío**: Procesamiento automático con registro
5. **Seguimiento**: Control de estado y confirmación de entrega
6. **Historial**: Registro completo para auditoría

### **Notificaciones a Médicos**
- **Selección de médico**: Lista de médicos disponibles
- **Información del paciente**: Nombre y hora de cita
- **Observaciones**: Detalles adicionales importantes
- **Prioridad configurable**: Según urgencia del caso
- **Confirmación automática**: Registro de notificación enviada

### **Contacto a Pacientes**
- **Múltiples métodos**: Llamada, SMS, Email, WhatsApp
- **Motivos predefinidos**: Recordatorio, cambio de horario, confirmación
- **Mensaje personalizable**: Contenido específico por caso
- **Información de contacto**: Teléfono y email del paciente
- **Registro de entrega**: Confirmación de comunicación exitosa

### **Mensajes Internos**
- **Tipos configurables**: Interno, Alerta administrativa, Recordatorio
- **Destinatarios flexibles**: Médicos, personal administrativo, grupos
- **Prioridades**: Para mensajes importantes
- **Contenido completo**: Asunto y cuerpo del mensaje
- **Historial de respuestas**: Conversaciones completas

### **Características de Seguridad**
- ✅ **Solo personal administrativo** puede acceder a esta funcionalidad
- ✅ **Validación de destinatarios** antes de enviar mensajes
- ✅ **Registro de comunicaciones** con timestamp y usuario
- ✅ **Priorización automática** de mensajes críticos
- ✅ **Confirmación de entrega** para mensajes importantes
- ✅ **Historial completo** para auditoría y seguimiento

## Reportes y Control - Características Técnicas

### **Funcionalidades Implementadas**
- ✅ **Reportes de atención diaria**: Estadísticas completas de pacientes atendidos
- ✅ **Reportes de cancelaciones**: Análisis de tendencias y motivos
- ✅ **Reportes de ingresos**: Financieros con comparaciones mensuales
- ✅ **Carga de trabajo**: Distribución por médico y especialidad
- ✅ **Reporte de errores**: Sistema de tickets con prioridades
- ✅ **Dashboard interactivo**: Resumen de métricas en tiempo real
- ✅ **Exportación automática**: PDF con gráficos y estadísticas
- ✅ **Sistema de prioridades**: Crítica, Alta, Normal, Baja
- ✅ **Reportes de demostración**: Datos de prueba para validar funcionalidades

### **Modelo de Datos de Reporte**
```kotlin
data class Reporte(
    val id: String,
    val tipo: String,
    val fecha: String,
    val descripcion: String,
    val estadisticas: String,
    val estado: String,
    val prioridad: String
)
```

### **Tipos de Reportes Disponibles**
- **📊 Atención Diaria**: Total pacientes, distribución por especialidad, tiempo promedio
- **❌ Cancelaciones**: Análisis de motivos, tendencias temporales, recomendaciones
- **💰 Ingresos**: Financieros mensuales, comparaciones, métodos de pago
- **👨‍⚕️ Carga de Trabajo**: Distribución por médico, especialidad, horarios
- **🚨 Error del Sistema**: Tickets de soporte técnico con prioridades

### **Estados de Reporte**
- **GENERADO**: Reporte creado exitosamente
- **ANALIZADO**: Reporte procesado y analizado
- **EN REVISIÓN**: Reporte siendo revisado por administrador
- **CANCELADO**: Reporte cancelado por solicitud

### **Sistema de Prioridades**
- **🚨 CRÍTICA**: Errores que impiden el funcionamiento del sistema
- **⚠️ ALTA**: Problemas importantes que afectan la operación
- **📋 NORMAL**: Reportes informativos y estadísticos
- **📋 BAJA**: Sugerencias y mejoras menores

### **Funcionalidades de Análisis**
- **Tendencias temporales**: Comparación con períodos anteriores
- **Distribución porcentual**: Por especialidad, médico, tipo de servicio
- **Gráficos automáticos**: Visualizaciones incluidas en reportes
- **Estadísticas detalladas**: Métricas específicas por tipo de reporte
- **Recomendaciones**: Sugerencias basadas en análisis de datos

### **Interfaz de Usuario**
- **Dashboard principal**: Resumen de todos los reportes generados
- **Botones de acción**: Generar, ver detalle, descargar, compartir
- **Estados visuales**: Verde (generado), Azul (analizado), Amarillo (en revisión)
- **Prioridades visuales**: Rojo (crítica), Naranja (alta), Verde (normal)
- **Formularios modales**: Diálogos para configurar reportes específicos

### **Flujo de Generación de Reportes**
1. **Acceso**: Personal administrativo → "Ver Funcionalidades" → "Reportes y Control"
2. **Selección**: Elegir tipo de reporte (Atención, Cancelaciones, Ingresos, etc.)
3. **Configuración**: Definir parámetros (fechas, especialidad, opciones)
4. **Generación**: Procesamiento automático con estadísticas
5. **Visualización**: Reporte en lista con opciones de descarga
6. **Exportación**: PDF con gráficos y análisis detallado

### **Sistema de Reporte de Errores**
- **Tipos de error**: Sistema, Rendimiento, Interfaz, Sincronización, Seguridad
- **Descripción detallada**: Campos para explicar el problema
- **Pasos para reproducir**: Instrucciones para replicar el error
- **Información de contacto**: Para seguimiento y resolución
- **Notificación automática**: Al administrador del sistema

### **Características de Exportación**
- **Formato PDF**: Reportes profesionales con gráficos
- **Gráficos incluidos**: Visualizaciones automáticas de datos
- **Estadísticas detalladas**: Métricas completas por tipo de reporte
- **Compartir por email**: Envío automático a destinatarios
- **Archivo descargable**: Guardado en carpeta de descargas

### **Características de Seguridad**
- ✅ **Solo personal administrativo** puede acceder a esta funcionalidad
- ✅ **Validación de parámetros** antes de generar reportes
- ✅ **Registro de generación** con timestamp y usuario
- ✅ **Priorización automática** de errores críticos
- ✅ **Notificación inmediata** al administrador para errores críticos
- ✅ **Exportación segura** con control de acceso

## Seguridad

### Características de Seguridad Implementadas
- ✅ **Validación de email** con formato correcto
- ✅ **Contraseñas seguras** (mínimo 6 caracteres)
- ✅ **Encriptación básica** de contraseñas
- ✅ **Tokens de sesión** únicos
- ✅ **Validación de permisos** por rol
- ✅ **Cierre de sesión** seguro
- ✅ **Acceso específico** por tipo de usuario
- ✅ **Validación de citas** en tiempo real

### Mejoras Futuras de Seguridad
- 🔒 **Encriptación avanzada** con bcrypt
- 🔒 **Autenticación biométrica**
- 🔒 **Tokens JWT** para sesiones
- 🔒 **Validación de fuerza de contraseñas**
- 🔒 **Límite de intentos de login**

## Escalabilidad

El sistema está diseñado para ser altamente escalable:

### ✅ Fácil Adición de Nuevos Roles
```kotlin
// Ejemplo: Agregar un nuevo rol "Enfermero"
class Enfermero : Usuario() {
    override fun obtenerNombreRol(): String = "Enfermero"
    override fun obtenerPermisos(): List<String> = listOf("ATENDER_PACIENTES", "ADMINISTRAR_MEDICAMENTOS")
}
```

### ✅ Extensión de Funcionalidades
- Nuevos métodos pueden agregarse a las clases existentes
- El patrón Factory permite agregar nuevos tipos de usuarios fácilmente
- La estructura modular facilita la adición de nuevas características
- Sistema de autenticación extensible
- **Menús específicos por rol** para mejor organización
- **Sistema de citas modular** para agregar nuevas funcionalidades

### ✅ Mantenibilidad
- Código bien documentado con comentarios en español
- Separación clara de responsabilidades
- Uso de patrones de diseño establecidos
- Validaciones robustas
- **Interfaces específicas** por tipo de usuario
- **Gestión de estado** centralizada para citas

## Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contacto

Para preguntas o soporte, contactar a:
- Email: [tu-email@ejemplo.com]
- GitHub: [tu-usuario-github]

---

**Desarrollado con ❤️ usando Programación Orientada a Objetos y Autenticación Segura** 