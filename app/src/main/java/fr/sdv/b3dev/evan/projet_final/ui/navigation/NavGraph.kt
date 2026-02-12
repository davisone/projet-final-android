package fr.sdv.b3dev.evan.projet_final.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SneakerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            PlaceholderScreen(title = "Accueil")
        }
        composable(Screen.Releases.route) {
            PlaceholderScreen(title = "Sorties")
        }
        composable(Screen.Search.route) {
            PlaceholderScreen(title = "Recherche")
        }
        composable(Screen.Favorites.route) {
            PlaceholderScreen(title = "Favoris")
        }
        composable(Screen.Profile.route) {
            PlaceholderScreen(title = "Profil")
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("sneakerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sneakerId = backStackEntry.arguments?.getLong("sneakerId") ?: -1L
            PlaceholderScreen(title = "Detail sneaker #$sneakerId")
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}

