package cl.duoc.rightnow

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    data object News : BottomNavItem("news", "Noticias", Icons.Filled.Article)
    data object Profile : BottomNavItem("profile", "Perfil", Icons.Filled.Person)
}

val bottomItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.News,
    BottomNavItem.Profile
)
