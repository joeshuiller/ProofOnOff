package com.janes.saenz.puerta.proofonoff.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.janes.saenz.puerta.proofonoff.ui.navigation.RouteNavigation

/**
 * Orquestador principal de la jerarquía de navegación de la aplicación.
 *
 * Esta función es la encargada de inicializar y mantener el estado de la navegación
 * durante el ciclo de vida de la composición. Actúa como el puente entre el [Scaffold]
 * de la Activity y el grafo de rutas detallado.
 * * ### Responsabilidades:
 * 1. **Gestión de Estado:** Crea y preserva el [navController] usando [rememberNavController].
 * 2. **Delegación de Rutas:** Delega la definición de los destinos a [RouteNavigation].
 * 3. **Adaptabilidad:** Recibe un [modifier] para respetar las restricciones de
 * diseño (como el padding de las barras de sistema).
 *
 * @author Janes Saenz Puerta
 * @param modifier Modificador para ajustar el diseño, típicamente proveniente de un Scaffold.
 * @see RouteNavigation
 */
@Composable
fun AppNavigation(
    modifier: Modifier
) {
    val navController = rememberNavController()
    RouteNavigation(navController, modifier)
}
