package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * ShowError - Componente de retroalimentación y recuperación de fallos.
 *
 * Este componente centraliza la visualización de errores críticos en la interfaz,
 * asegurando que el usuario reciba feedback visual coherente y una vía de acción.
 *
 * @param message Descripción técnica o amigable del error ocurrido.
 * @param onRetry Callback ejecutado al presionar el botón de reintento para disparar
 * nuevamente la lógica de carga.
 */
@Composable
fun ShowError(
    message: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Indicador visual de alerta: Utiliza el esquema de color de error del sistema
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Icono de advertencia",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de Error: Fallback dinámico para evitar campos vacíos
        Text(
            text = message ?: "Ha ocurrido un error inesperado",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Acción de Recuperación (CTA): Estilizado con ErrorContainer para jerarquía visual
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Icono de reintento",
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(text = "Reintentar")
        }
    }
}