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
import com.janes.saenz.puerta.proofonoff.ui.utlis.UIConstants
import com.janes.saenz.puerta.proofonoff.ui.viewModels.CommentsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
/**
 * Orquestador de la vista de detalle de una publicación.
 *
 * Esta función coordina la visualización de la información exhaustiva de un [Posts],
 * gestiona la sincronización de comentarios mediante un [LaunchedEffect] y delega
 * la renderización a componentes modulares.
 *
 * @param post Entidad de dominio con la información base del post.
 * @param viewModel Manejador de estado para la lógica de comentarios (Inyectado via Hilt).
 * @param paddingValues Espaciado seguro proporcionado por el Scaffold superior.
 */
@Composable
fun PostDetailContent(
    post: Posts,
    viewModel: CommentsViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val commentsResource by viewModel.uiStateComments.collectAsState()

    // Disparador de sincronización
    LaunchedEffect(post.id) {
        viewModel.getComments(post.id)
    }

    // Extracción de datos de comentarios para simplificar la UI
    val uiStateComments = (commentsResource as? Resource.Success)?.data ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        PostHeaderSection(post)

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        PostBodySection(post)

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        CommentsSection(
            comments = uiStateComments,
            isSending = viewModel.isSuccess,
            onSendComment = { bodyText ->
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
 * Sección superior que presenta los metadatos de identificación del post.
 * * Muestra el título con énfasis visual y etiquetas (Chips) con el ID del usuario
 * y el ID del recurso para facilitar la trazabilidad técnica.
 * * @param post Objeto que contiene el título y los metadatos de ID.
 */
@Composable
private fun PostHeaderSection(post: Posts) {
    Text(
        text = post.title ?: "Sin título",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(16.dp))

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
}

/**
 * Sección central encargada de mostrar el cuerpo del contenido y la trazabilidad temporal.
 * * Aplica configuraciones de interlineado personalizado definidas en [UIConstants]
 * y renderiza las fechas de creación y actualización de forma formateada.
 * * @param post Objeto con el cuerpo del mensaje y los timestamps (createdAt/updatedAt).
 */
@Composable
private fun PostBodySection(post: Posts) {
    Text(
        text = post.body ?: "Sin contenido disponible",
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * UIConstants.LINE_SPACING_EXTRA
    )

    Spacer(modifier = Modifier.height(32.dp))

    MetadataRow(label = "Creado el", timestamp = post.createdAt)

    if (post.updatedAt > post.createdAt) {
        MetadataRow(label = "Actualizado el", timestamp = post.updatedAt)
    }
}

/**
 * Componente reactivo para la gestión de la comunidad y comentarios.
 * * Renderiza dinámicamente la lista de comentarios o un estado vacío de feedback.
 * Incluye un formulario de interacción al final de la sección.
 * * @param comments Lista de comentarios a visualizar.
 * @param isSending Estado de carga del envío de un nuevo comentario.
 * @param onSendComment Callback para procesar el envío de información al ViewModel.
 */
@Composable
private fun CommentsSection(
    comments: List<Comments>,
    isSending: Boolean,
    onSendComment: (String) -> Unit
) {
    Text(
        text = "Comentarios (${comments.size})",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (comments.isEmpty()) {
        Text(
            text = "No hay comentarios aún. ¡Sé el primero!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    } else {
        comments.forEach { comment ->
            CommentItem(comment)
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    CommentForm(
        isSending = isSending,
        onSendComment = onSendComment
    )
}

/**
 * MetadataRow - Componente utilitario para visualización de fechas formateadas.
 * * Utiliza [remember] vinculado al [timestamp] para asegurar que el formateo de la fecha
 * (operación costosa) solo se recalcule si el valor de la fecha cambia realmente,
 * optimizando el rendimiento durante el scroll.
 *
 * @param label Etiqueta descriptiva que precede a la fecha (ej. "Creado el").
 * @param timestamp Valor de tiempo en milisegundos a ser formateado.
 */
@Composable
fun MetadataRow(label: String, timestamp: Long) {
    // Memorizamos el formateo para evitar trabajo innecesario en recomposiciones
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
            contentDescription = null, // Decorativo
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
