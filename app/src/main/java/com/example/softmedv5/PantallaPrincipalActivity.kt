package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.util.GestorAutenticacion

class PantallaPrincipalActivity : AppCompatActivity() {
    
    private lateinit var textViewBienvenida: TextView
    private lateinit var textViewUsuarioId: TextView
    private lateinit var textViewUsuarioNombre: TextView
    private lateinit var textViewUsuarioEmail: TextView
    private lateinit var textViewUsuarioRol: TextView
    private lateinit var textViewTokenSesion: TextView
    private lateinit var textViewDuracionSesion: TextView
    private lateinit var textViewEstadoContacto: TextView
    private lateinit var textViewEstadoSesion: TextView
    private lateinit var textViewCantidadPermisos: TextView
    private lateinit var textViewPermisosLista: TextView
    private lateinit var buttonCerrarSesion: Button
    private lateinit var buttonFuncionalidades: Button
    private lateinit var buttonInformacionContacto: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)
        
        inicializarVistas()
        configurarEventos()
        mostrarInformacionUsuario()
    }
    
    private fun inicializarVistas() {
        textViewBienvenida = findViewById(R.id.text_view_bienvenida)
        textViewUsuarioId = findViewById(R.id.text_view_usuario_id)
        textViewUsuarioNombre = findViewById(R.id.text_view_usuario_nombre)
        textViewUsuarioEmail = findViewById(R.id.text_view_usuario_email)
        textViewUsuarioRol = findViewById(R.id.text_view_usuario_rol)
        textViewTokenSesion = findViewById(R.id.text_view_token_sesion)
        textViewDuracionSesion = findViewById(R.id.text_view_duracion_sesion)
        textViewEstadoContacto = findViewById(R.id.text_view_estado_contacto)
        textViewEstadoSesion = findViewById(R.id.text_view_estado_sesion)
        textViewCantidadPermisos = findViewById(R.id.text_view_cantidad_permisos)
        textViewPermisosLista = findViewById(R.id.text_view_permisos_lista)
        buttonCerrarSesion = findViewById(R.id.button_cerrar_sesion)
        buttonFuncionalidades = findViewById(R.id.button_funcionalidades)
        buttonInformacionContacto = findViewById(R.id.button_informacion_contacto)
    }
    
    private fun configurarEventos() {
        buttonCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
        
        buttonFuncionalidades.setOnClickListener {
            mostrarFuncionalidades()
        }
        
        buttonInformacionContacto.setOnClickListener {
            abrirInformacionContacto()
        }
        
        // BOTÓN TEMPORAL PARA LIMPIAR USUARIOS - ELIMINAR EN PRODUCCIÓN
        val buttonLimpiarUsuarios = findViewById<Button>(R.id.button_limpiar_usuarios)
        buttonLimpiarUsuarios?.setOnClickListener {
            limpiarTodosLosUsuarios()
        }
    }
    
    private fun mostrarInformacionUsuario() {
        // Usar el método actualizado que maneja la información más reciente
        actualizarInformacionUsuario()
    }
    
    private fun abrirInformacionContacto() {
        val intent = Intent(this, InformacionContactoActivity::class.java)
        startActivityForResult(intent, REQUEST_INFORMACION_CONTACTO)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_INFORMACION_CONTACTO && resultCode == RESULT_OK) {
            // La información de contacto se actualizó, refrescar la información del usuario
            mostrarInformacionUsuario()
            mostrarMensaje("Perfil actualizado correctamente")
        }
    }
    
    private fun cerrarSesion() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        gestorAutenticacion.cerrarSesion()
        
        // Volver a la pantalla principal de selección de roles
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun mostrarFuncionalidades() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null) {
            val usuario = sesion.usuario
            val rol = usuario.obtenerNombreRol()
            
            // Redirigir según el rol del usuario
            when (rol) {
                "Médico" -> {
                    val intent = Intent(this, FuncionalidadesMedicoActivity::class.java)
                    startActivity(intent)
                }
                "Paciente" -> {
                    val intent = Intent(this, FuncionalidadesPacienteActivity::class.java)
                    startActivity(intent)
                }
                "Personal Administrativo" -> {
                    val intent = Intent(this, FuncionalidadesAdministrativoActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    // Rol no reconocido, mostrar mensaje de error
                    mostrarMensaje("Rol no válido: $rol")
                }
            }
        }
    }
    
    private fun volverALogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        
        // Actualizar la información del usuario cuando se regrese a esta pantalla
        // Esto asegura que los cambios realizados en otras pantallas se reflejen
        actualizarInformacionUsuario()
    }
    
    private fun actualizarInformacionUsuario() {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val sesion = gestorAutenticacion.obtenerSesionActual()
        
        if (sesion != null && sesion.esSesionActiva()) {
            val usuario = sesion.usuario
            
            // Mostrar bienvenida
            textViewBienvenida.text = "BioMonitor\nSoftware médico y de monitoreo"
            
            // Actualizar la información mostrada con los datos más recientes
            textViewUsuarioId.text = usuario.obtenerId()
            textViewUsuarioNombre.text = usuario.obtenerNombre()
            textViewUsuarioEmail.text = usuario.obtenerEmail()
            textViewUsuarioRol.text = usuario.obtenerNombreRol()
            textViewTokenSesion.text = "${sesion.tokenSesion.take(20)}..."
            textViewDuracionSesion.text = "${sesion.obtenerDuracionSesion()} minutos"
            
            // Estado de contacto con icono
            val estadoContacto = if (usuario.tieneInformacionContactoCompleta()) "✅ Completo" else "⚠️ Incompleto"
            textViewEstadoContacto.text = estadoContacto
            
            // Estado de sesión con icono
            val estadoSesion = if (sesion.esSesionActiva()) "🟢 Activo" else "🔴 Inactivo"
            textViewEstadoSesion.text = estadoSesion
            
            // Mostrar permisos del usuario
            val permisos = usuario.obtenerPermisos()
            textViewCantidadPermisos.text = "${permisos.size} permisos"
            textViewPermisosLista.text = permisos.joinToString("\n• ", "• ")
            
        } else {
            // Si no hay sesión activa, volver a la pantalla de autenticación
            volverALogin()
        }
    }
    
    private fun limpiarTodosLosUsuarios() {
        android.app.AlertDialog.Builder(this)
            .setTitle("🗑️ Limpiar Todos los Usuarios")
            .setMessage("¿Estás seguro de que quieres eliminar TODOS los usuarios y datos relacionados?\n\n⚠️ Esta acción NO se puede deshacer.")
            .setPositiveButton("SÍ, ELIMINAR TODO") { _, _ ->
                val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
                val resultado = gestorAutenticacion.limpiarTodosLosUsuarios()
                
                if (resultado) {
                    mostrarMensaje("✅ Todos los usuarios han sido eliminados")
                    // Volver a la pantalla principal de selección de roles
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    mostrarMensaje("❌ Error al eliminar usuarios")
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .setCancelable(false)
            .show()
    }
    
    companion object {
        private const val REQUEST_INFORMACION_CONTACTO = 1001
    }
} 