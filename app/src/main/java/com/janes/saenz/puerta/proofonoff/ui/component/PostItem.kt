package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts

/**
 * PostItem - Representación de tarjeta para el listado de publicaciones.
 *
 * Diseñado bajo los principios de Material Design 3, este componente optimiza
 * la visualización de datos masivos mediante el truncamiento de texto y
 * elementos visuales de soporte (Chips).
 *
 * @param post Entidad de datos [Posts] con la información a resumir.
 * @param onClick Callback que expone el objeto completo al ser seleccionado.
 * @param paddingValues Valores de espaciado (Precaución: revisar duplicidad con padding interno).
 */
@Composable
fun PostItem(
    post: Posts,
    onClick: (Posts) -> Unit,
    paddingValues: PaddingValues
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(post) } // Implementación nativa de click en Card (M3)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                // Nota Senior: paddingValues suele aplicarse al LazyColumn raíz,
                // no por cada ítem para evitar indentaciones excesivas.
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            // Título: Prioridad visual alta
            Text(
                text = post.title ?: "Sin título",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cuerpo resumido: Truncado a 3 líneas para homogeneidad en la lista
            Text(
                text = post.body ?: "Sin contenido",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer de la tarjeta: Metadatos técnicos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SuggestionChip(
                    onClick = { /* Acción de filtrado por usuario opcional */ },
                    label = { Text("User: ${post.userId}") }
                )
                Text(
                    text = "ID: ${post.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}