package com.janes.saenz.puerta.proofonoff.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel orquestador del flujo de arranque y control de versiones (Startup Flow).
 * * Este componente es responsable de garantizar que la aplicación esté sincronizada
 * con los parámetros del servidor antes de permitir el acceso al usuario.
 *
 * ### Responsabilidades Principales:
 * 1. **Enrutamiento Dinámico:** Define el [startDestination] evaluando la presencia de una sesión activa (Token).
 */
@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    var startDestination by mutableStateOf<String?>(null)
        private set

    init {
        startSplashScreenTimer()
    }

    private fun startSplashScreenTimer() {
        viewModelScope.launch {
            // Espera de 2 segundos (2000 milisegundos)
            delay(2000)

            // Aquí decides a dónde ir (puedes meter lógica de Login vs Home)
            startDestination = "main_tabs"
        }
    }
}