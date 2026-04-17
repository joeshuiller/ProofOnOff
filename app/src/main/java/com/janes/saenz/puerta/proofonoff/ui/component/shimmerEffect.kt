package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
/**
 * Aplica un efecto de brillo animado (shimmer) al fondo de un Composable.
 * * Este modificador es ideal para componentes de carga (Skeletons). Utiliza una
 * transición infinita que desplaza un gradiente lineal de forma diagonal,
 * creando la ilusión de movimiento y profundidad.
 *
 * ### Detalles de la Animación:
 * - **Duración:** 1200ms por ciclo.
 * - **Easing:** [FastOutSlowInEasing] para un movimiento fluido y natural.
 * - **Colores:** Alterna entre gris claro con alfas de 0.6f y 0.2f.
 *
 * @author Janes Saenz Puerta
 * @return Un [Modifier] con el fondo de gradiente animado aplicado.
 */
@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    return this.background(brush)
}