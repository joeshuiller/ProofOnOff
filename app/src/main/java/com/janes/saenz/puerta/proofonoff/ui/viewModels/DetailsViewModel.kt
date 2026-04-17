package com.janes.saenz.puerta.proofonoff.ui.viewModels




import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.useCase.GetPostByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DetailsViewModel - Gestor de estado para la vista detallada de publicaciones.
 * * Sigue el patrón MVVM y utiliza StateFlow para la entrega de estados a Compose.
 *
 * @property getPostByIdUseCase Interactor de dominio para obtener un post por su identificador.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase
) : ViewModel() {

    // Estado interno mutable: Representa el Single Source of Truth para esta pantalla
    private val _uiState = MutableStateFlow<Resource<Posts>>(Resource.Loading)

    // Estado público inmutable: Observado por la UI (Compose)
    val uiState: StateFlow<Resource<Posts>> = _uiState.asStateFlow()

    /**
     * Recupera la información detallada de una publicación.
     * * Implementa una recolección asíncrona que notifica cambios de estado
     * (Carga, Éxito, Error) de forma atómica.
     *
     * @param id Identificador único del Post a consultar.
     */
    fun getPostDetail(id: Int) {
        viewModelScope.launch {
            getPostByIdUseCase(id)
                // Asegura que la UI muestre el estado de carga antes de la recolección
                .onStart { _uiState.value = Resource.Loading }
                .collect { result ->
                    // Actualización reactiva del estado final
                    _uiState.value = result
                }
        }
    }
}