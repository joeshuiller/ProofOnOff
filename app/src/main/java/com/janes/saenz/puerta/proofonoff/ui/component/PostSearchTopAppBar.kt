package com.janes.saenz.puerta.proofonoff.ui.component
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Barra de navegación superior personalizada con funcionalidad de búsqueda integrada.
 *
 * Alterna dinámicamente entre un título estático y un campo de entrada de texto ([TextField]).
 * Gestiona automáticamente el cambio de iconos según el estado de la búsqueda.
 *
 * @param isSearchActive Define si el componente muestra el campo de búsqueda o el título estático.
 * @param searchQuery Estado actual del texto de búsqueda introducido por el usuario.
 * @param onSearchToggle Callback que notifica cambios en el modo de búsqueda (activo/inactivo).
 * @param onQueryChanged Callback que se dispara cada vez que el usuario modifica el texto de búsqueda.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSearchTopAppBar(
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchToggle: (Boolean) -> Unit,
    onQueryChanged: (String) -> Unit
) {
    TopAppBar(
        title = {
            if (isSearchActive) {
                SearchTextField(query = searchQuery, onQueryChanged = onQueryChanged)
            } else {
                Text(text = "Publicaciones", style = MaterialTheme.typography.titleLarge)
            }
        },
        navigationIcon = {
            if (isSearchActive) {
                BackButton(onClose = {
                    onSearchToggle(false)
                    onQueryChanged("")
                })
            }
        },
        actions = {
            SearchActions(
                isSearchActive = isSearchActive,
                searchQuery = searchQuery,
                onSearchToggle = onSearchToggle,
                onQueryChanged = onQueryChanged
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

/**
 * Campo de texto estilizado para la entrada de términos de búsqueda.
 *
 * Configurado sin indicadores de foco visibles para integrarse limpiamente en la [TopAppBar].
 * Utiliza los colores del esquema [MaterialTheme.colorScheme.onPrimaryContainer] para
 * garantizar el contraste.
 *
 * @param query Texto actual a mostrar en el campo.
 * @param onQueryChanged Notifica cambios de texto para actualizar el estado superior.
 */
@Composable
private fun SearchTextField(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(text = "Buscar por ID o título...", style = MaterialTheme.typography.bodyLarge)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

/**
 * Botón de navegación para salir del modo búsqueda.
 *
 * @param onClose Acción a ejecutar que normalmente incluye desactivar la búsqueda
 * y limpiar el filtro.
 */
@Composable
private fun BackButton(onClose: () -> Unit) {
    IconButton(onClick = onClose) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Cerrar búsqueda",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/**
 * Contenedor de acciones laterales para la barra de búsqueda.
 *
 * Muestra el icono de lupa para activar la búsqueda o el icono de cierre para
 * limpiar el texto actual cuando la búsqueda ya está activa.
 *
 * @param isSearchActive Estado actual del modo búsqueda.
 * @param searchQuery Texto de búsqueda para determinar si mostrar el botón de limpieza.
 * @param onSearchToggle Acción para activar el modo búsqueda.
 * @param onQueryChanged Acción para limpiar el texto introducido.
 */
@Composable
private fun SearchActions(
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchToggle: (Boolean) -> Unit,
    onQueryChanged: (String) -> Unit
) {
    if (!isSearchActive) {
        IconButton(onClick = { onSearchToggle(true) }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Activar búsqueda",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    } else if (searchQuery.isNotEmpty()) {
        IconButton(onClick = { onQueryChanged("") }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Limpiar texto",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
