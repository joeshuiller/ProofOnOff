package com.janes.saenz.puerta.proofonoff.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.ui.component.PostDetailContent
import com.janes.saenz.puerta.proofonoff.ui.component.ShowError
import com.janes.saenz.puerta.proofonoff.ui.component.ShowSkeleton
import com.janes.saenz.puerta.proofonoff.ui.viewModels.DetailsViewModel

/**
 * DetailScreen - Punto de entrada para la visualización detallada de un recurso.
 * * Orquesta la transición de estados entre la carga de datos y la visualización final.
 * Utiliza Material Design 3 para proporcionar una experiencia inmersiva y consistente.
 *
 * @param postId Identificador único de la publicación a consultar.
 * @param viewModel Manejador de estado inyectado mediante Hilt, acotado al ciclo de vida de la ruta.
 * @param onBack Callback para delegar la navegación de regreso al orquestador principal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    postId: Int,
    viewModel: DetailsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    // Observación del estado asíncrono del post
    val postState by viewModel.uiState.collectAsState()

    // Gestión reactiva del estado de la pantalla
    when (val resource = postState) {
        is Resource.Loading -> {
            // Visualización de Shimmer/Skeleton durante el fetch de datos
            ShowSkeleton()

            // Efecto secundario: Dispara la carga solo al entrar o si postId cambia
            LaunchedEffect(postId) {
                viewModel.getPostDetail(postId)
            }
        }

        is Resource.Success -> {
            val postData = resource.data

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Detalle del Post") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Regresar a la lista"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            ) { paddingValues ->
                // Delegación del renderizado a componente especializado
                PostDetailContent(
                    post = postData,
                    paddingValues = paddingValues
                )
            }
        }

        is Resource.Error -> {
            // Manejo de excepciones con política de reintento
            ShowError(
                message = resource.message,
                onRetry = { viewModel.getPostDetail(postId) }
            )
        }
    }
}
