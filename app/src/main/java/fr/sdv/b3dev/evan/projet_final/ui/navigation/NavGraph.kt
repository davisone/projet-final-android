package fr.sdv.b3dev.evan.projet_final.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import fr.sdv.b3dev.evan.projet_final.camera.CameraMode
import fr.sdv.b3dev.evan.projet_final.ui.screens.cart.CartScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.camera.CameraScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.checkout.CheckoutScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.detail.SneakerDetailScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.favorites.FavoritesScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.home.HomeScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.profile.ProfileScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.releases.ReleasesScreen
import fr.sdv.b3dev.evan.projet_final.ui.screens.search.SearchScreen

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
            HomeScreen(
                onSneakerClick = { sneakerId ->
                    navController.navigate(Screen.Detail.createRoute(sneakerId))
                },
                onGoToReleases = { navController.navigate(Screen.Releases.route) },
                onGoToCart = { navController.navigate(Screen.Cart.route) }
            )
        }
        composable(Screen.Releases.route) {
            ReleasesScreen(
                onSneakerClick = { sneakerId ->
                    navController.navigate(Screen.Detail.createRoute(sneakerId))
                }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                onSneakerClick = { sneakerId ->
                    navController.navigate(Screen.Detail.createRoute(sneakerId))
                },
                onOpenScanner = {
                    navController.navigate(Screen.Camera.createRoute(CameraMode.SCAN.value))
                }
            )
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onSneakerClick = { sneakerId ->
                    navController.navigate(Screen.Detail.createRoute(sneakerId))
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onGoToCart = { navController.navigate(Screen.Cart.route) },
                onOpenCamera = {
                    navController.navigate(Screen.Camera.createRoute(CameraMode.PROFILE.value))
                }
            )
        }
        composable(Screen.Cart.route) {
            CartScreen(
                onGoToCheckout = { navController.navigate(Screen.Checkout.route) },
                onSneakerClick = { sneakerId ->
                    navController.navigate(Screen.Detail.createRoute(sneakerId))
                }
            )
        }
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onPaymentSuccess = { _ ->
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Cart.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("sneakerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sneakerId = backStackEntry.arguments?.getLong("sneakerId") ?: -1L
            SneakerDetailScreen(
                sneakerId = sneakerId,
                onGoToCart = { navController.navigate(Screen.Cart.route) }
            )
        }
        composable(
            route = Screen.Camera.route,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val modeArg = backStackEntry.arguments?.getString("mode")
            CameraScreen(
                modeArg = modeArg,
                onClose = { navController.popBackStack() },
                onSneakerFound = { sneakerId ->
                    navController.popBackStack()
                    navController.navigate(Screen.Detail.createRoute(sneakerId))
                }
            )
        }
    }
}
