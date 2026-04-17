package com.janes.saenz.puerta.proofonoff.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * TopAppBar especializado con soporte para búsqueda reactiva.
 * * @param isSearchActive Controla si se muestra el campo de texto o el título estático.
 * @param searchQuery El texto actual introducido por el usuario.
 * @param onSearchToggle Callback para activar/desactivar el modo búsqueda.
 * @param onQueryChanged Callback que se dispara al modificar el texto.
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
                // Campo de entrada de búsqueda
                TextField(
                    value = searchQuery,
                    onValueChange = onQueryChanged,
                    placeholder = {
                        Text(
                            text = "Buscar por ID o título...",
                            style = MaterialTheme.typography.bodyLarge
                        )
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
            } else {
                Text(
                    text = "Publicaciones",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            if (isSearchActive) {
                IconButton(onClick = {
                    onSearchToggle(false)
                    onQueryChanged("") // Limpiar estado al retroceder
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Cerrar búsqueda",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        actions = {
            if (!isSearchActive) {
                IconButton(onClick = { onSearchToggle(true) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Activar búsqueda",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar texto",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}