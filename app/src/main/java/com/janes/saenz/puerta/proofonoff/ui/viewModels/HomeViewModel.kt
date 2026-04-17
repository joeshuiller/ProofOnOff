package com.janes.saenz.puerta.proofonoff.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.useCase.GetPostsUseCase
import com.janes.saenz.puerta.proofonoff.domain.useCase.ObserveFilteredPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de la lógica de sincronización y gestión de la pantalla principal.
 * * Implementa una estrategia de "Sincronización en Cascada":
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val observeFiltered: ObserveFilteredPostsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<Resource<List<Posts>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<Posts>>> = _uiState.asStateFlow()
    var isSuccess by mutableStateOf(false)
        private set
    fun loadProducts() {
        viewModelScope.launch {
            getPostsUseCase().collect { result ->
                Log.e("MainViewModel", "loadProduct: $result")
                _uiState.value = result
                isSuccess = true
            }
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Transformamos el texto de búsqueda en el flujo de resultados
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val postsState: StateFlow<Resource<List<Posts>>> = _searchQuery
        .debounce(300) // Evita buscar con cada tecla, espera un respiro
        .distinctUntilChanged() // Solo busca si el texto cambió realmente
        .flatMapLatest { query ->
            // Si el query es un número, buscamos por ID, si no por Título
            val id = query.toIntOrNull()
            val title = if (id == null) query else null
            observeFiltered(id = id, title = title)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading
        )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}