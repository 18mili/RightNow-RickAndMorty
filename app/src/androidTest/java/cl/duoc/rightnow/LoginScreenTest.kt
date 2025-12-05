package cl.duoc.rightnow

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun login_muestra_error_si_contrasena_es_corta() {
        composeRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController)
        }

        // Ajusta los textos según tu UI real
        composeRule.onNodeWithText("Contraseña").performTextInput("123")
        composeRule.onNodeWithText("Ingresar").performClick()
        composeRule.onNodeWithText("Mínimo 6 caracteres").assertExists()
    }
}
