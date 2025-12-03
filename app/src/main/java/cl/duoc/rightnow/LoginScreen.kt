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
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { UserLocalDataStore(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        emailError = if (!isNotBlank(email)) "El email no puede estar vacío" else null
        passError = if (!isMinLength(password, 6)) "La contraseña debe tener al menos 6 caracteres" else null
        return emailError == null && passError == null
    }

    val isFormValid = isNotBlank(email) &&
            isMinLength(password, 6) &&
            emailError == null &&
            passError == null

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {

            Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (!isNotBlank(email)) "El email no puede estar vacío" else null
                },
                label = { Text("Email (registrado)") },
                singleLine = true,
                isError = emailError != null,
                supportingText = { if (emailError != null) Text(emailError!!) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passError = if (!isMinLength(password, 6)) "Mínimo 6 caracteres" else null
                },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passError != null,
                supportingText = { if (passError != null) Text(passError!!) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!validate()) return@Button
                    scope.launch {
                        val ok = repo.login(email.trim(), password)
                        if (ok) {
                            val encoded = URLEncoder.encode(
                                email.trim(),
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate("main/$encoded") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            snackbarHostState.showSnackbar("Credenciales inválidas o usuario no registrado")
                        }
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}
