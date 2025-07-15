# Base de Datos BioMonitor

## Descripción General

Se ha implementado una base de datos completa usando **SQLite nativo** para evitar problemas de compatibilidad con Room y KAPT/KSP. Esta implementación proporciona persistencia de datos real sin dependencias complejas.

## Estructura de la Base de Datos

### Tablas Principales

1. **usuarios** - Gestión de usuarios del sistema
   - Campos: id, email, contrasena, nombre_completo, rol, fecha_registro, activo
   - Clave primaria: id (AUTOINCREMENT)
   - Restricciones: email UNIQUE

2. **pacientes** - Información de pacientes
   - Campos: id, nombre, apellidos, fecha_nacimiento, genero, documento, telefono, email, direccion, fecha_registro, activo
   - Clave primaria: id (AUTOINCREMENT)
   - Restricciones: documento UNIQUE

## Implementación

### SimpleDatabaseHelper

Clase principal que extiende `SQLiteOpenHelper` y maneja:
- Creación de tablas
- Migración de esquemas
- Inserción de datos de demostración
- Operaciones CRUD para usuarios y pacientes

### Características Implementadas

#### ✅ Persistencia de Datos
- Los datos se almacenan en SQLite local
- Persistencia entre sesiones de la aplicación
- Migración automática de esquemas

#### ✅ Datos de Demostración Automáticos
- Usuarios predefinidos con diferentes roles
- Pacientes de ejemplo con información completa
- Se insertan automáticamente al crear la base de datos

#### ✅ Operaciones CRUD Completas
- **Usuarios**: autenticación, registro, verificación de existencia
- **Pacientes**: inserción, consulta, listado ordenado

#### ✅ Consultas Optimizadas
- Búsqueda por email y contraseña
- Filtrado por estado activo
- Ordenamiento por nombre

## Credenciales de Demostración

### Usuarios del Sistema
- **Administrador**: admin@biomonitor.com / admin123
- **Médico**: medico@biomonitor.com / medico123
- **Paciente**: paciente@biomonitor.com / paciente123
- **Gerente**: gerente@biomonitor.com / gerente123

### Pacientes de Demostración
- María González López (DNI: 12345678A)
- Carlos Rodríguez Martín (DNI: 87654321B)
- Ana Fernández Jiménez (DNI: 11223344C)

## Ventajas de la Implementación

1. **Compatibilidad Total**: No requiere plugins adicionales (KAPT/KSP)
2. **Persistencia Real**: Los datos se mantienen entre sesiones
3. **Simplicidad**: Código directo y fácil de entender
4. **Rendimiento**: Consultas SQL nativas optimizadas
5. **Mantenibilidad**: Fácil de extender y modificar
6. **Estabilidad**: Sin dependencias externas complejas

## Métodos Disponibles

### Para Usuarios
- `autenticarUsuario(email, contrasena)`: Autentica un usuario
- `existeUsuarioConEmail(email)`: Verifica si existe un email
- `insertarUsuario(email, contrasena, nombre, rol)`: Registra nuevo usuario

### Para Pacientes
- `obtenerTodosLosPacientes()`: Lista todos los pacientes activos
- `insertarPaciente(...)`: Registra nuevo paciente

## Inicialización

La base de datos se inicializa automáticamente en `MainActivity`:

```kotlin
private fun inicializarBaseDatos() {
    databaseHelper = SimpleDatabaseHelper(this)
    // La base de datos se crea automáticamente con datos de demostración
}
```

## Próximos Pasos

1. Migrar las actividades existentes para usar la base de datos
2. Agregar más tablas según necesidades (citas, historiales, etc.)
3. Implementar operaciones de actualización y eliminación
4. Agregar encriptación de contraseñas
5. Implementar sincronización con servidor remoto

## Migración desde el Sistema Anterior

El sistema anterior usaba:
- `mutableMapOf` y `mutableListOf` para datos temporales
- Gestores con datos hardcodeados
- Sin persistencia entre sesiones

El nuevo sistema:
- Base de datos SQLite nativa
- Persistencia completa
- Datos de demostración automáticos
- Sin dependencias problemáticas 