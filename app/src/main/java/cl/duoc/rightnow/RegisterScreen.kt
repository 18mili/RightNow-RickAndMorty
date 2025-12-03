package cl.duoc.rightnow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { UserLocalDataStore(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // campos
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }

    // errores
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

    fun simpleValidate(): Boolean {
        nameError = if (name.isBlank()) "Escribe tu nombre" else null

        emailError = when {
            email.isBlank() -> "El correo es obligatorio"
            !email.contains("@") -> "Correo no válido"
            else -> null
        }

        passError = if (password.length < 4) "Mínimo 4 caracteres" else null

        confirmError = when {
            confirm.isBlank() -> "Confirma tu contraseña"
            confirm != password -> "No coincide con la contraseña"
            else -> null
        }

        return nameError == null &&
                emailError == null &&
                passError == null &&
                confirmError == null
    }

    // si quieres que se desactive, usa esta línea.
    // para probar rápido, más abajo lo dejamos enabled = true
    val isFormValid =
        name.isNotBlank() &&
                email.contains("@") &&
                password.length >= 4 &&
                confirm == password &&
                nameError == null &&
                emailError == null &&
                passError == null &&
                confirmError == null

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(20.dp))

            // NOMBRE
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (name.isNotBlank()) nameError = null
                },
                label = { Text("Nombre completo") },
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) Text(nameError!!)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (email.contains("@")) emailError = null
                },
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) Text(emailError!!)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (password.length >= 4) passError = null
                },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passError != null,
                supportingText = {
                    if (passError != null) Text(passError!!)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // CONFIRM
            OutlinedTextField(
                value = confirm,
                onValueChange = {
                    confirm = it
                    if (confirm == password) confirmError = null
                },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmError != null,
                supportingText = {
                    if (confirmError != null) Text(confirmError!!)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (!simpleValidate()) return@Button

                    scope.launch {
                        val result = repo.register(
                            User(
                                name = name.trim(),
                                email = email.trim(),
                                password = password
                            )
                        )

                        result.onSuccess {
                            // si se registró, vamos directo al main
                            val encoded = URLEncoder.encode(
                                name.trim(),
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate("main/$encoded") {
                                popUpTo("login") { inclusive = true }
                            }
                        }.onFailure { e ->
                            snackbarHostState.showSnackbar(e.message ?: "No se pudo registrar")
                        }
                    }
                },
                // si quieres que se bloquee, pon: enabled = isFormValid
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "¿Ya tienes cuenta? Inicia sesión",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }
    }
}
