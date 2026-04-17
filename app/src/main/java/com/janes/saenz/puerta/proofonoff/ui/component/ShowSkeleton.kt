package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
/**
 * Genera una representación visual de carga (Skeleton Screen) para un componente de producto o perfil.
 *
 * Esta función utiliza una disposición vertical ([Column]) para mostrar placeholders animados
 * que imitan la estructura final del contenido, mejorando la experiencia de usuario (UX)
 * durante procesos asíncronos o fetch de datos.
 *
 * ### Estructura Visual:
 * 1. **Imagen/Icono:** Un recuadro de 120dp con bordes redondeados (8dp).
 * 2. **Título:** Una línea de texto simulada al 70% del ancho.
 * 3. **Detalle/Precio:** Una línea de texto corta al 30% del ancho.
 *
 * @author Janes Saenz Puerta
 * @see Modifier.shimmerEffect Requiere que esta extensión esté definida para aplicar la animación de brillo.
 * @sample ProductScreen Ejemplo de uso dentro de un estado de carga.
 */
@Composable
fun ShowSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder de Imagen
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder de Título
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Placeholder de Precio
        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(18.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
    }
}