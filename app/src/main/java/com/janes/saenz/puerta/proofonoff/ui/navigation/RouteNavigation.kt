package com.janes.saenz.puerta.proofonoff.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.janes.saenz.puerta.proofonoff.ui.screens.DetailScreen
import com.janes.saenz.puerta.proofonoff.ui.screens.HomeScreen
import com.janes.saenz.puerta.proofonoff.ui.screens.SplashScreen
import com.janes.saenz.puerta.proofonoff.ui.utlis.Screen
import com.janes.saenz.puerta.proofonoff.ui.viewModels.DetailsViewModel
import com.janes.saenz.puerta.proofonoff.ui.viewModels.HomeViewModel

/**
 * RouteNavigation - Grafo de navegación principal de la aplicación.
 *
 * Coordina el flujo entre los diferentes módulos (Splash, Home, Details),
 * gestionando la entrega de argumentos y la limpieza de la pila de navegación.
 *
 * @param navController Controlador central que gestiona el backstack de Compose.
 * @param modifier Modificador de diseño para ajustar el contenedor del NavHost.
 */
@Composable
fun RouteNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Screen.Splash.route
    ) {
        // --- SECCIÓN: BIENVENIDA (SPLASH) ---
        composable(Screen.Splash.route) {
            SplashScreen(onTransition = { destino ->
                navController.navigate(destino) {
                    // Acción Senior: Eliminamos Splash del historial para flujo de UX limpio
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // --- SECCIÓN: DASHBOARD (HOME) ---
        composable(Screen.MainTabs.route) {
            // ViewModel inyectado y acotado al ciclo de vida de este destino
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                onClick = { id ->
                    // Uso de ruta dinámica generada por el contrato de la Screen
                    navController.navigate(Screen.Details.createRoute(id))
                }
            )
        }

        // --- SECCIÓN: DETALLES ---
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Extracción segura del argumento de navegación
            val postId = backStackEntry.arguments?.getInt("postId") ?: return@composable
            val viewModel = hiltViewModel<DetailsViewModel>()

            DetailScreen(
                postId = postId,
                viewModel = viewModel,
                onBack = {
                    // Retorno seguro al destino anterior en el stack
                    navController.popBackStack()
                }
            )
        }
    }
}