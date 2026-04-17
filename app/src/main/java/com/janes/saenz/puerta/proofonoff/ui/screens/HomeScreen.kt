package com.janes.saenz.puerta.proofonoff.ui.screens
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.ui.component.EmptyStateComponent
import com.janes.saenz.puerta.proofonoff.ui.component.PostItem
import com.janes.saenz.puerta.proofonoff.ui.component.PostSearchTopAppBar
import com.janes.saenz.puerta.proofonoff.ui.component.ShowError
import com.janes.saenz.puerta.proofonoff.ui.component.ShowSkeleton
import com.janes.saenz.puerta.proofonoff.ui.viewModels.HomeViewModel

/**
 * Pantalla principal que orquesta el flujo de datos y estados de las publicaciones.
 *
 * Esta función actúa como un "Screen Level Composable" que:
 * 1. Gestiona la lógica de carga inicial (Sincronización/Caché).
 * 2. Observa el estado del buscador y los filtros aplicados.
 * 3. Delega la representación visual a componentes especializados según el estado del [Resource].
 *
 * @param viewModel Instancia del [HomeViewModel] inyectada mediante Hilt.
 * @param onClick Callback de navegación que se dispara al seleccionar un post,
 * proporcionando su ID único.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onClick: (id: Int) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.postsState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }

    when (val resource = state) {
        is Resource.Loading -> {
            ShowSkeleton()
            if (!viewModel.isSuccess) viewModel.loadProducts()
        }

        is Resource.Error -> ShowError(resource.message) { viewModel.loadProducts() }
        is Resource.Success -> {
            if (resource.data.isEmpty()) {
                EmptyStateComponent(message = "No hay posts disponibles")
            } else {
                HomeScaffoldContent(
                    uiState = uiState,
                    searchQuery = searchQuery,
                    isSearchActive = isSearchActive,
                    onSearchToggle = { isSearchActive = it },
                    onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                    onRetryFilter = { viewModel.onSearchQueryChanged(searchQuery) },
                    onClick = onClick
                )
            }
        }
    }
}

/**
 * Contenedor estructural que define la anatomía visual de la pantalla de inicio.
 *
 * Implementa un [Scaffold] que integra una barra de búsqueda reactiva y un
 * contenedor principal para los resultados filtrados.
 *
 * @param uiState Estado actual de la lista de posts filtrados.
 * @param searchQuery Texto actual presente en el campo de búsqueda.
 * @param isSearchActive Define si el buscador está en modo expandido o colapsado.
 * @param onSearchToggle Callback para cambiar la visibilidad del buscador.
 * @param onQueryChanged Callback que se dispara al escribir en el buscador.
 * @param onRetryFilter Acción para reintentar la búsqueda en caso de error.
 * @param onClick Navegación hacia el detalle del recurso.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScaffoldContent(
    uiState: Resource<List<Posts>>,
    searchQuery: String,
    isSearchActive: Boolean,
    onSearchToggle: (Boolean) -> Unit,
    onQueryChanged: (String) -> Unit,
    onRetryFilter: () -> Unit,
    onClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            PostSearchTopAppBar(
                isSearchActive = isSearchActive,
                searchQuery = searchQuery,
                onSearchToggle = onSearchToggle,
                onQueryChanged = onQueryChanged
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (uiState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Error -> ShowError(uiState.message) { onRetryFilter() }
                is Resource.Success -> {
                    if (uiState.data.isEmpty()) {
                        EmptyStateComponent(
                            message = "Prueba con palabras diferentes.",
                            onFilterClear = { onQueryChanged("") }
                        )
                    } else {
                        HomeList(
                            posts = uiState.data,
                            paddingValues = paddingValues,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}

/**
 * Representación optimizada de la lista de publicaciones.
 *
 * Utiliza un [LazyColumn] para garantizar un rendimiento fluido incluso con listas
 * extensas, manejando el reciclaje de vistas de forma eficiente.
 *
 * @param posts Colección de objetos [List<Posts>] a renderizar.
 * @param paddingValues Márgenes de seguridad proporcionados por el Scaffold.
 * @param onClick Acción a ejecutar al presionar un elemento de la lista.
 */
@Composable
private fun HomeList(
    posts: List<Posts>,
    paddingValues: PaddingValues,
    onClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(posts) { post ->
            PostItem(
                post = post,
                onClick = { onClick(it.id) },
                paddingValues = paddingValues
            )
        }
    }
}
