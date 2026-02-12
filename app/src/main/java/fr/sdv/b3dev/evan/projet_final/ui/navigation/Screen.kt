package fr.sdv.b3dev.evan.projet_final.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Accueil")
    data object Releases : Screen("releases", "Sorties")
    data object Search : Screen("search", "Recherche")
    data object Favorites : Screen("favorites", "Favoris")
    data object Profile : Screen("profile", "Profil")
    data object Detail : Screen("detail/{sneakerId}", "Detail") {
        fun createRoute(sneakerId: Long): String = "detail/$sneakerId"
    }
}

