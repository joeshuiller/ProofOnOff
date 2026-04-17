package com.janes.saenz.puerta.proofonoff.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.useCase.AddCommentUseCase
import com.janes.saenz.puerta.proofonoff.domain.useCase.GetCommentsUseCase
import com.janes.saenz.puerta.proofonoff.ui.utlis.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de la lógica de negocio para la sección de comentarios.
 * * Implementa patrones de observación reactiva y gestión de estados mediante [Resource]
 * para comunicar de forma precisa el ciclo de vida de las peticiones a la UI.
 *
 * @property getCommentsUseCase Servicio de dominio para la obtención reactiva de comentarios.
 * @property addCommentUseCase Servicio de dominio para la persistencia de nuevos comentarios.
 */
@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase
) : ViewModel() {

    // Estado reactivo principal: Observado por la UI para renderizar Lista, Error o Loading.
    private val _uiStateComments = MutableStateFlow<Resource<List<Comments>>>(Resource.Loading)
    val uiStateComments: StateFlow<Resource<List<Comments>>> = _uiStateComments.asStateFlow()

    // Estado de carga específico para acciones de escritura (Envío de formulario).
    // Implementado con mutableStateOf para una integración directa y eficiente con Compose.
    var isSuccess by mutableStateOf(false)
        private set

    /**
     * Inicia la suscripción al flujo de comentarios de un post específico.
     * * Utiliza el operador [onStart] para garantizar que la UI entre en estado de carga
     * antes de procesar la recolección del flujo.
     *
     * @param id Identificador único del Post (Foreign Key).
     */
    fun getComments(id: Int) {
        viewModelScope.launch {
            getCommentsUseCase(id)
                .onStart { _uiStateComments.value = Resource.Loading }
                .collect { result ->
                    _uiStateComments.value = result
                }
        }
    }

    /**
     * Orquesta el envío de un nuevo comentario.
     * * Implementa una lógica de actualización tras inserción:
     * 1. Activa el estado de carga (isSuccess).
     * 2. Ejecuta la persistencia via Use Case.
     * 3. Aplica un delay de 2s para asegurar que el feedback visual sea perceptible (UX).
     * 4. Fuerza la actualización de la lista de comentarios.
     * * @param comment DTO con la información del comentario a persistir.
     */
    fun sendComment(comment: Comments) {
        viewModelScope.launch {
            try {
                isSuccess = true
                // Persistencia de datos
                addCommentUseCase(comment)

                // Delay controlado para UX: evita que el loader parpadee demasiado rápido
                delay(Constants.TIMEOUT_MS)

                // Re-sincronización del estado local
                getComments(comment.postId)
            } finally {
                // Garantiza que el estado de carga se desactive incluso si ocurre una excepción
                isSuccess = false
            }
        }
    }
}
