package cl.duoc.rightnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RightNowApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RightNowApp() {
    val rootNavController = rememberNavController()
    val context = LocalContext.current
    val userStore = remember { UserLocalDataStore(context) }
    val scope = rememberCoroutineScope()

    var startRoute by remember { mutableStateOf("login") }

    // al iniciar, ver si hay usuario guardado
    LaunchedEffect(Unit) {
        val current = userStore.getCurrentUserEmail()
        if (!current.isNullOrBlank()) {
            val encoded = java.net.URLEncoder.encode(
                current,
                java.nio.charset.StandardCharsets.UTF_8.toString()
            )
            startRoute = "main/$encoded"
        }
    }

    MaterialTheme {
        Scaffold { innerPadding ->
            NavHost(
                navController = rootNavController,
                startDestination = startRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") {
                    LoginScreen(navController = rootNavController)
                }
                composable("register") {
                    RegisterScreen(navController = rootNavController)
                }
                composable(
                    route = "main/{username}",
                    arguments = listOf(
                        navArgument("username") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val userArg = backStackEntry.arguments?.getString("username").orEmpty()

                    // 👇 AQUÍ pasamos el onLogout hacia abajo
                    MainScreen(
                        username = userArg,
                        onLogout = {
                            scope.launch {
                                userStore.logout()
                                rootNavController.navigate("login") {
                                    // limpiamos todo para que no pueda volver con back
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

