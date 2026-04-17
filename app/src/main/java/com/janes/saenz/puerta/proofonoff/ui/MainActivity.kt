package com.janes.saenz.puerta.proofonoff.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.janes.saenz.puerta.proofonoff.BuildConfig
import com.janes.saenz.puerta.proofonoff.ui.theme.ProofOnOffTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Actividad principal de la aplicación que sirve como host para la interfaz de usuario.
 *
 * Esta clase utiliza [AndroidEntryPoint] para habilitar la inyección de dependencias con Hilt,
 * permitiendo que los ViewModels y otros componentes sean provistos automáticamente.
 * * ### Responsabilidades:
 * 1. Inicializar el árbol de logs mediante [Timber].
 * 2. Configurar la experiencia visual de inicio ([installSplashScreen]).
 * 3. Establecer el grafo de navegación principal ([AppNavigation]).
 *
 * @author Janes Sáenz Puerta
 * @see AppNavigation
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Punto de inicio del ciclo de vida de la actividad.
     * * Configura el entorno de Compose, aplica el tema global [ProofOnOffTheme]
     * y define la estructura base de la pantalla mediante un [Scaffold].
     *
     * @param savedInstanceState Estado previo de la actividad, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño de borde a borde para aprovechar toda la pantalla (Edge-to-Edge)
        enableEdgeToEdge()

        // Maneja la transición fluida desde el icono de la app hasta la UI principal
        installSplashScreen()

        // Inicializa Timber para el registro de eventos en modo Debug
        // Nota de Seguridad: En producción, se debe usar un Tree que no exponga PII (Vulnerabilidad 10062)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setContent {
            ProofOnOffTheme {
                // Scaffold provee la estructura visual básica (slots para barras, FAB, etc.)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Punto de entrada al sistema de rutas de la aplicación
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}