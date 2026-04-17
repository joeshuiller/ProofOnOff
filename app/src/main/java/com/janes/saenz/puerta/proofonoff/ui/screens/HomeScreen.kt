package com.janes.saenz.puerta.proofonoff.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.ui.component.EmptyStateComponent
import com.janes.saenz.puerta.proofonoff.ui.component.PostDetailContent
import com.janes.saenz.puerta.proofonoff.ui.component.PostItem
import com.janes.saenz.puerta.proofonoff.ui.component.PostSearchTopAppBar
import com.janes.saenz.puerta.proofonoff.ui.component.ShowError
import com.janes.saenz.puerta.proofonoff.ui.component.ShowSkeleton
import com.janes.saenz.puerta.proofonoff.ui.viewModels.HomeViewModel

/**
 * HomeScreen - Dashboard principal de gestión de publicaciones.
 * * Implementa la especificación de Material Design 3 con un patrón de búsqueda
 * integrada en el TopAppBar. Utiliza recolección de estados con consciencia del
 * ciclo de vida para optimizar el consumo de recursos.
 *
 * @param viewModel Puente de comunicación con la capa de datos.
 * @param onClick Navegación hacia la vista de detalle, inyectando el ID del recurso seleccionado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClick: (id: Int) -> Unit
) {
    // Observación de estados mediante Flow con Lifecycle-awareness
    // Previene recolecciones innecesarias cuando la app está en segundo plano
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.postsState.collectAsStateWithLifecycle()

    // Estados efímeros de UI para la gestión del buscador
    var isSearchActive by remember { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Manejador de estado raíz: Sincronización Inicial / Caché
    when (val resource = state) {
        is Resource.Loading -> {
            ShowSkeleton()
            // Estrategia de carga única: evita re-disparar si ya hay éxito previo
            if (!viewModel.isSuccess) viewModel.loadProducts()
        }

        is Resource.Success -> {
            val list = resource.data

            // Caso de borde: Base de datos vacía tras sincronización
            if (list.isEmpty()) {
                EmptyStateComponent(message = "No hay posts disponibles")
            } else {
                Scaffold(
                    topBar = {
                        PostSearchTopAppBar(
                            isSearchActive = isSearchActive,
                            searchQuery = searchQuery,
                            onSearchToggle = { isSearchActive = it },
                            onQueryChanged = { viewModel.onSearchQueryChanged(it) }
                        )
                    }
                ) { paddingValues ->
                    // Contenedor reactivo para resultados filtrados
                    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

                        when (val filteredState = uiState) {
                            is Resource.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }

                            is Resource.Success -> {
                                if (filteredState.data.isEmpty()) {
                                    // Feedback cuando el filtro no coincide con ningún registro
                                    EmptyStateComponent(
                                        message = "Prueba con palabras diferentes o verifica el ID del post.",
                                        onFilterClear = { viewModel.onSearchQueryChanged("") }
                                    )
                                } else {
                                    // Lista de alto rendimiento con Lazy Loading
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(8.dp)
                                    ) {
                                        // Optamos por list del recurso raíz para consistencia
                                        items(filteredState.data) { post ->
                                            PostItem(
                                                post = post,
                                                onClick = { onClick(it.id) },
                                                paddingValues = paddingValues
                                            )
                                        }
                                    }
                                }
                            }

                            is Resource.Error -> {
                                ShowError(filteredState.message) {
                                    viewModel.onSearchQueryChanged(searchQuery)
                                }
                            }
                        }
                    }
                }
            }
        }

        is Resource.Error -> {
            ShowError(resource.message) { viewModel.loadProducts() }
        }
    }
}