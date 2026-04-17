package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.ui.viewModels.CommentsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
/**
 * PostDetailContent - Orquestador de la vista de detalle.
 *
 * Presenta la información exhaustiva de un Post, incluyendo metadatos temporales
 * y la sección reactiva de comentarios.
 *
 * @param post Entidad de dominio con la información base.
 * @param viewModel Manejador de estado para la lógica de comentarios (Inyectado via Hilt).
 * @param paddingValues Espaciado seguro proporcionado por el Scaffold superior.
 */
@Composable
fun PostDetailContent(
    post: Posts,
    viewModel: CommentsViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    // Observación de estado asíncrono con consciencia del ciclo de vida
    val commentsResource by viewModel.uiStateComments.collectAsState()

    // Estado local derivado para la visualización de la lista
    // Nota Senior: Se inicializa vacío para garantizar una UI consistente durante el Loading
    var uiStateComments: List<Comments> = emptyList()

    // Disparador de sincronización: Se ejecuta al montar el componente o cambiar de Post
    LaunchedEffect(post.id) {
        viewModel.getComments(post.id)
    }

    // Pattern Matching para la resolución del estado del Resource
    when (val it = commentsResource) {
        is Resource.Success -> uiStateComments = it.data
        is Resource.Loading -> { /* Opcional: Implementar Shimmer local para comentarios */ }
        is Resource.Error -> { /* Opcional: Logging de error de red */ }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- CABECERA Y CUERPO ---
        Text(
            text = post.title ?: "Sin título",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Metadata Chips: Información técnica del post
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = { },
                label = { Text("Usuario ${post.userId}") },
                leadingIcon = { Icon(Icons.Default.Person, null, Modifier.size(18.dp)) }
            )
            AssistChip(
                onClick = { },
                label = { Text("ID ${post.id}") }
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = post.body ?: "Sin contenido disponible",
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN DE FECHAS ---
        MetadataRow(label = "Creado el", timestamp = post.createdAt)

        if (post.updatedAt > post.createdAt) {
            MetadataRow(label = "Actualizado el", timestamp = post.updatedAt)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // --- SECCIÓN DE COMENTARIOS ---
        Text(
            text = "Comentarios (${uiStateComments.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiStateComments.isEmpty()) {
            Text(
                text = "No hay comentarios aún. ¡Sé el primero!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            // Renderizado de lista de comentarios mediante composición modular
            uiStateComments.forEach { comment ->
                CommentItem(comment)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- FORMULARIO DE INTERACCIÓN ---
        CommentForm(
            isSending = viewModel.isSuccess,
            onSendComment = { bodyText ->
                // Mapeo dinámico de datos para el envío del nuevo comentario
                viewModel.sendComment(
                    Comments(
                        postId = post.id,
                        name = post.title.toString(),
                        email = "${post.id}@example${post.id}.com",
                        body = bodyText
                    )
                )
            }
        )
    }
}

/**
 * MetadataRow - Componente utilitario para visualización de fechas.
 * Utiliza [remember] para evitar reformatear la fecha en cada recomposición innecesaria.
 */
@Composable
fun MetadataRow(label: String, timestamp: Long) {
    val date = remember(timestamp) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(timestamp))
    }

    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: $date",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}