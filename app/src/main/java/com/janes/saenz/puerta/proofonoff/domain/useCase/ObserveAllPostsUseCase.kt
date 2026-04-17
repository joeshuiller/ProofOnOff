package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ObserveAllPostsUseCase - Interactor para la observación reactiva del feed de publicaciones.
 *
 * Este componente es la pieza central para el listado principal de la aplicación.
 * Permite una sincronización bidireccional implícita: cuando la base de datos se
 * actualiza (vía API o cacheo), este flujo emite automáticamente los nuevos datos.
 *
 * @property repository Contrato de acceso a datos que gestiona la colección de publicaciones.
 */
class ObserveAllPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    /**
     * Inicia la suscripción al flujo de todas las publicaciones disponibles.
     * * El flujo resultante debe ser recolectado preferiblemente en el ViewModel
     * utilizando operadores que gestionen el ciclo de vida (ej. [stateIn]).
     *
     * @return Un [Flow] observable que emite estados [Resource] con la colección de [Posts].
     */
    operator fun invoke(): Flow<Resource<List<Posts>>> {
        return repository.observeAllPosts()
    }
}