package de.stuermerbenjamin.ble

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import de.stuermerbenjamin.ble.ui.connected.ConnectedScreen
import de.stuermerbenjamin.ble.ui.scan.ScanScreen

sealed class AppDestination(val route: String) {
    object Scan : AppDestination("scan")
    object Connected : AppDestination("connected")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: AppDestination = AppDestination.Scan,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = AppDestination.Scan.route) {
            ScanScreen(navController = navController)
        }
        composable(route = AppDestination.Connected.route) {
            ConnectedScreen(navController = navController)
        }
    }
}
