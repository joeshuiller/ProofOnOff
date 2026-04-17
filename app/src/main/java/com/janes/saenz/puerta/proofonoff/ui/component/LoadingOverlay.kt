package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
/**
 * Un componente de superposición (Overlay) que bloquea la interfaz de usuario y muestra un indicador de carga.
 *
 * Se utiliza para operaciones síncronas que impiden al usuario continuar hasta que finalicen.
 * El componente oscurece el fondo mediante una capa semi-transparente y captura todos los
 * eventos de entrada para evitar clics accidentales en los elementos subyacentes.
 *
 * ### Características Principales:
 * 1. **Bloqueo de UI:** El modificador `.clickable(enabled = false)` consume los eventos táctiles.
 * 2. **Indicador Visual:** Utiliza un [CircularProgressIndicator] centrado.
 * 3. **Feedback de Texto:** Permite mostrar un mensaje personalizado (ej: "Procesando pago...").
 *
 * @author Janes Saenz Puerta
 * @param text El mensaje descriptivo que se mostrará debajo del indicador de progreso.
 */
@Composable
fun LoadingOverlay(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // Oscurece el fondo
            .clickable(enabled = false) {}, // Bloquea clicks
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}