package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * CommentForm - Componente de entrada para la creación de comentarios.
 *
 * Implementa un campo de texto estilizado y un botón de acción que soporta
 * estados de carga y validación de contenido.
 *
 * @param isSending Flag que indica si hay una operación de envío en curso.
 * Controla la habilitación de los controles y la visibilidad del loader.
 * @param onSendComment Callback disparado cuando el usuario confirma el envío.
 * Entrega el String limpio de la entrada.
 */
@Composable
fun CommentForm(
    isSending: Boolean,
    onSendComment: (String) -> Unit
) {
    // Estado interno: Maneja la entrada de texto de forma local para optimizar performance
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Entrada de texto principal con soporte para múltiples líneas
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Escribe un comentario...") },
            enabled = !isSending, // Bloquea la edición durante el envío
            maxLines = 4,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de acción con lógica de estado dinámico
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onSendComment(text)
                    text = "" // Limpieza del buffer local tras el éxito
                }
            },
            modifier = Modifier.align(Alignment.End),
            // Solo habilitado si hay texto y no se está enviando ya uno
            enabled = text.isNotBlank() && !isSending,
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isSending) {
                // Feedback visual de carga (Material Design 3)
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar comentario"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publicar")
            }
        }
    }
}