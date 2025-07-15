package com.example.softmedv5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.softmedv5.modelo.*
import com.example.softmedv5.util.GestorAutenticacion

class AutenticacionActivity : AppCompatActivity() {
    
    private lateinit var editTextUsuario: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextNombreCompleto: EditText
    private lateinit var editTextConfirmarPassword: EditText
    private lateinit var buttonAccion: Button
    private lateinit var textViewCambiarModo: TextView
    private lateinit var textViewTitulo: TextView
    private lateinit var textViewNombreCompleto: TextView
    private lateinit var textViewConfirmarPassword: TextView
    
    private var modoRegistro = false
    private var rolSeleccionado = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autenticacion)
        
        // Inicializar el gestor de autenticación
        GestorAutenticacion.obtenerInstancia().inicializarBaseDatos(this)
        
        // Obtener el rol seleccionado de la actividad anterior
        rolSeleccionado = intent.getStringExtra("ROL_SELECCIONADO") ?: ""
        
        inicializarVistas()
        configurarEventos()
        mostrarModoInicioSesion()
    }
    
    private fun inicializarVistas() {
        editTextUsuario = findViewById(R.id.edit_text_usuario)
        editTextPassword = findViewById(R.id.edit_text_password)
        editTextNombreCompleto = findViewById(R.id.edit_text_nombre_completo)
        editTextConfirmarPassword = findViewById(R.id.edit_text_confirmar_password)
        buttonAccion = findViewById(R.id.boton_accion)
        textViewCambiarModo = findViewById(R.id.text_view_cambiar_modo)
        textViewTitulo = findViewById(R.id.text_view_titulo)
        textViewNombreCompleto = findViewById(R.id.text_view_nombre_completo)
        textViewConfirmarPassword = findViewById(R.id.text_view_confirmar_password)
    }
    
    private fun configurarEventos() {
        buttonAccion.setOnClickListener {
            if (modoRegistro) {
                registrarUsuario()
            } else {
                iniciarSesion()
            }
        }
        
        textViewCambiarModo.setOnClickListener {
            cambiarModo()
        }
    }
    
    private fun mostrarModoRegistro() {
        modoRegistro = true
        textViewTitulo.text = "Registro - $rolSeleccionado"
        buttonAccion.text = "REGISTRARSE"
        textViewCambiarModo.text = "¿Ya tienes cuenta? Inicia sesión"
        
        // Mostrar campos de registro
        textViewNombreCompleto.visibility = View.VISIBLE
        editTextNombreCompleto.visibility = View.VISIBLE
        textViewConfirmarPassword.visibility = View.VISIBLE
        editTextConfirmarPassword.visibility = View.VISIBLE
    }
    
    private fun mostrarModoInicioSesion() {
        modoRegistro = false
        textViewTitulo.text = "Inicio de Sesión - $rolSeleccionado"
        buttonAccion.text = "INICIAR SESIÓN"
        textViewCambiarModo.text = "¿No tienes cuenta? Regístrate aquí"
        
        // Ocultar campos de registro
        textViewNombreCompleto.visibility = View.GONE
        editTextNombreCompleto.visibility = View.GONE
        textViewConfirmarPassword.visibility = View.GONE
        editTextConfirmarPassword.visibility = View.GONE
    }
    
    private fun cambiarModo() {
        if (modoRegistro) {
            mostrarModoInicioSesion()
        } else {
            mostrarModoRegistro()
        }
        
        // Limpiar campos
        limpiarCampos()
    }
    
    private fun registrarUsuario() {
        val usuario = editTextUsuario.text.toString().trim()
        val password = editTextPassword.text.toString()
        val confirmarPassword = editTextConfirmarPassword.text.toString()
        val nombreCompleto = editTextNombreCompleto.text.toString().trim()
        
        // Validaciones
        if (!validarCamposRegistro(usuario, password, confirmarPassword, nombreCompleto)) {
            return
        }
        
        // Verificar que el rol no esté vacío
        if (rolSeleccionado.isEmpty()) {
            mostrarMensaje("Error: No se ha seleccionado un rol válido")
            return
        }
        
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val resultado = gestorAutenticacion.registrarUsuario(
            usuario, password, nombreCompleto, rolSeleccionado
        )
        
        if (resultado.exitoso) {
            mostrarMensaje("Usuario registrado exitosamente. Ahora puedes iniciar sesión.")
            // Cambiar a modo de inicio de sesión después del registro exitoso
            mostrarModoInicioSesion()
            // Pre-llenar el campo de usuario con el email registrado
            editTextUsuario.setText(usuario)
            editTextPassword.text.clear()
        } else {
            mostrarMensaje(resultado.mensaje)
        }
    }
    
    private fun iniciarSesion() {
        val usuario = editTextUsuario.text.toString().trim()
        val password = editTextPassword.text.toString()
        
        // Validaciones
        if (!validarCamposInicioSesion(usuario, password)) {
            return
        }
        
        // Intentar inicio de sesión
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val resultado = gestorAutenticacion.iniciarSesion(usuario, password)
        
        if (resultado.exitoso) {
            mostrarMensaje("Sesión iniciada exitosamente")
            abrirPantallaPrincipal(resultado.usuario!!)
        } else {
            mostrarMensaje(resultado.mensaje)
        }
    }
    
    private fun iniciarSesionAutomatica(usuario: String, password: String) {
        val gestorAutenticacion = GestorAutenticacion.obtenerInstancia()
        val resultado = gestorAutenticacion.iniciarSesion(usuario, password)
        
        if (resultado.exitoso) {
            abrirPantallaPrincipal(resultado.usuario!!)
        }
    }
    
    private fun validarCamposRegistro(
        usuario: String,
        password: String,
        confirmarPassword: String,
        nombreCompleto: String
    ): Boolean {
        
        if (nombreCompleto.isEmpty()) {
            mostrarMensaje("Por favor ingresa tu nombre completo")
            return false
        }
        
        if (usuario.isEmpty()) {
            mostrarMensaje("Por favor ingresa tu usuario o email")
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
            mostrarMensaje("Por favor ingresa un email válido")
            return false
        }
        
        if (password.isEmpty()) {
            mostrarMensaje("Por favor ingresa una contraseña")
            return false
        }
        
        if (password.length < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres")
            return false
        }
        
        if (password != confirmarPassword) {
            mostrarMensaje("Las contraseñas no coinciden")
            return false
        }
        
        return true
    }
    
    private fun validarCamposInicioSesion(usuario: String, password: String): Boolean {
        
        if (usuario.isEmpty()) {
            mostrarMensaje("Por favor ingresa tu usuario o email")
            return false
        }
        
        if (password.isEmpty()) {
            mostrarMensaje("Por favor ingresa tu contraseña")
            return false
        }
        
        return true
    }
    
    private fun abrirPantallaPrincipal(usuario: Usuario) {
        val intent = Intent(this, PantallaPrincipalActivity::class.java).apply {
            putExtra("USUARIO_ID", usuario.obtenerId())
            putExtra("USUARIO_NOMBRE", usuario.obtenerNombre())
            putExtra("USUARIO_ROL", usuario.obtenerNombreRol())
        }
        startActivity(intent)
        finish() // Cerrar esta actividad
    }
    
    private fun limpiarCampos() {
        editTextUsuario.text.clear()
        editTextPassword.text.clear()
        editTextNombreCompleto.text.clear()
        editTextConfirmarPassword.text.clear()
    }
    
    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
} 