package fr.sdv.b3dev.evan.projet_final.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

object BottomNavItems {
    val items = listOf(
        BottomNavItem(
            screen = Screen.Home,
            label = "Accueil",
            icon = Icons.Filled.Home
        ),
        BottomNavItem(
            screen = Screen.Releases,
            label = "Sorties",
            icon = Icons.Filled.NewReleases
        ),
        BottomNavItem(
            screen = Screen.Search,
            label = "Recherche",
            icon = Icons.Filled.Search
        ),
        BottomNavItem(
            screen = Screen.Favorites,
            label = "Favoris",
            icon = Icons.Filled.Favorite
        ),
        BottomNavItem(
            screen = Screen.Profile,
            label = "Profil",
            icon = Icons.Filled.Person
        )
    )
}

