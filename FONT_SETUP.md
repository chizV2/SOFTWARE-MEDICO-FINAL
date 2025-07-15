# Configuración de Fuentes - BioMonitor

## Estado Actual
El proyecto BioMonitor actualmente utiliza **fuentes del sistema Android** para garantizar compatibilidad y evitar errores de recursos no encontrados.

## Fuentes Utilizadas

### Fuentes del Sistema
- **sans-serif-light**: Para títulos principales con peso ligero
- **sans-serif-medium**: Para títulos y botones con peso medio
- **sans-serif**: Para texto regular y subtítulos

## Ventajas de Usar Fuentes del Sistema

1. **Compatibilidad garantizada**: No hay errores de recursos no encontrados
2. **Rendimiento optimizado**: Las fuentes ya están cargadas en el sistema
3. **Consistencia visual**: Se adaptan automáticamente al tema del dispositivo
4. **Menor tamaño de APK**: No se incluyen archivos de fuentes adicionales

## Características de las Fuentes

### Títulos Principales
- **Fuente**: `sans-serif-light`
- **Tamaño**: 42-48sp
- **Estilo**: Bold
- **Efectos**: Sombra, letter-spacing
- **Uso**: "BioMonitor" en pantallas principales

### Subtítulos
- **Fuente**: `sans-serif`
- **Tamaño**: 18-20sp
- **Estilo**: Normal
- **Efectos**: Letter-spacing, alpha
- **Uso**: "Software médico y de monitoreo"

### Títulos de Sección
- **Fuente**: `sans-serif-medium`
- **Tamaño**: 28sp
- **Estilo**: Bold
- **Uso**: Títulos de formularios y secciones

### Texto Regular
- **Fuente**: `sans-serif`
- **Tamaño**: 16-18sp
- **Estilo**: Normal
- **Uso**: Labels, campos de texto, enlaces

### Botones
- **Fuente**: `sans-serif-medium`
- **Tamaño**: 18sp
- **Estilo**: Bold
- **Efectos**: Letter-spacing
- **Uso**: Botones de acción principales

## Implementación en Layouts

Los layouts utilizan las siguientes referencias de fuentes:

```xml
<!-- Título principal -->
android:fontFamily="sans-serif-light"

<!-- Subtítulos -->
android:fontFamily="sans-serif"

<!-- Títulos de sección -->
android:fontFamily="sans-serif-medium"

<!-- Texto regular -->
android:fontFamily="sans-serif"
```

## Notas Importantes

- Las fuentes del sistema se adaptan automáticamente a diferentes densidades de pantalla
- El rendimiento es óptimo ya que no hay archivos adicionales que cargar
- La apariencia es consistente en todos los dispositivos Android
- No se requieren permisos especiales para usar fuentes del sistema

## Futuras Mejoras

Si en el futuro se desea implementar fuentes personalizadas como Inter:

1. Descargar los archivos TTF de Inter desde Google Fonts
2. Colocarlos en `app/src/main/res/font/`
3. Actualizar `fonts.xml` con las referencias correctas
4. Modificar los layouts para usar `@font/inter_*`

Por ahora, las fuentes del sistema proporcionan una experiencia visual moderna y profesional. 