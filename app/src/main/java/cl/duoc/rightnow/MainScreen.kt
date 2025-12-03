package cl.duoc.rightnow

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.Job

fun NavDestination?.isOnDestination(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}

@Composable
fun MainScreen(
    username: String,
    onLogout: () -> Unit = {}   // 👈 nuevo
) {
    val tabsNavController = rememberNavController()
    val backStackEntry by tabsNavController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    val selected = currentDest.isOnDestination(item.route)
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            tabsNavController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(tabsNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabsNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }

            // 🔹 Reemplazamos NewsScreen por la lista de personajes
            composable(BottomNavItem.News.route) {
                CharactersListScreen(navController = tabsNavController)
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    username = username,
                    onLogout = onLogout
                )
            }

            // 🔹 Ruta para el detalle del personaje
            composable(
                route = "characterDetail/{characterId}",
                arguments = listOf(
                    navArgument("characterId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
                CharacterDetailScreen(
                    characterId = characterId,
                    navController = tabsNavController
                )
            }
        }
    }
}
