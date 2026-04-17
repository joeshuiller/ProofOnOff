package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * EmptyStateComponent - Interfaz de retroalimentación para estados sin datos.
 *
 * Este componente centraliza la experiencia de usuario cuando las consultas
 * no retornan resultados, proporcionando una estética coherente con Material Design 3.
 *
 * @param message Texto descriptivo que explica por qué la vista está vacía.
 * @param onFilterClear Acción opcional para resetear filtros o estados.
 * Si es null, el botón de acción se omitirá automáticamente.
 */
@Composable
fun EmptyStateComponent(
    message: String = "No encontramos lo que buscas",
    onFilterClear: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, // Centrado vertical para foco visual
        horizontalAlignment = Alignment.CenterHorizontally // Centrado horizontal
    ) {
        // Elemento visual primario: Indica la naturaleza del estado vacío
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = "Búsqueda sin resultados",
            modifier = Modifier.size(80.dp),
            // Tonalidad suavizada para no sobrecargar visualmente al usuario
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título de estado: Mensaje de alto nivel
        Text(
            text = "Sin resultados",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mensaje de soporte: Provee contexto adicional o instrucciones
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Estrategia de recuperación (Call to Action)
        if (onFilterClear != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onFilterClear,
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Limpiar búsqueda")
            }
        }
    }
}