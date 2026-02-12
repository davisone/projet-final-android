package fr.sdv.b3dev.evan.projet_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.sdv.b3dev.evan.projet_final.ui.navigation.BottomNavItems
import fr.sdv.b3dev.evan.projet_final.ui.navigation.SneakerNavGraph
import fr.sdv.b3dev.evan.projet_final.ui.theme.ProjetfinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetfinalTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AppBottomNavigation(
                            currentDestinationRoute = currentDestination?.route,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            isRouteSelected = { route ->
                                currentDestination?.hierarchy?.any { it.route == route } == true
                            }
                        )
                    }
                ) { innerPadding ->
                    SneakerNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun AppBottomNavigation(
    currentDestinationRoute: String?,
    onNavigate: (String) -> Unit,
    isRouteSelected: (String) -> Boolean
) {
    NavigationBar {
        BottomNavItems.items.forEach { item ->
            NavigationBarItem(
                selected = isRouteSelected(item.screen.route),
                onClick = {
                    if (currentDestinationRoute != item.screen.route) {
                        onNavigate(item.screen.route)
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = null
            )
        }
    }
}
