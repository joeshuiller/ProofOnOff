package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * ObserveFilteredPostsUseCase - Interactor de búsqueda y filtrado dinámico.
 *
 * Actúa como el motor de consulta para componentes de búsqueda. Permite la
 * localización de publicaciones mediante criterios específicos, manteniendo
 * la reactividad con la fuente de datos local.
 *
 * @property repository Contrato de persistencia que implementa la lógica de filtrado.
 */
class ObserveFilteredPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    /**
     * Inicia la observación de un subconjunto de publicaciones filtradas.
     * * El filtrado es inclusivo: si se proveen ambos parámetros, el resultado
     * debe satisfacer ambas condiciones (Lógica AND).
     *
     * @param id (Opcional) Identificador numérico exacto del post.
     * @param title (Opcional) Cadena de texto para búsqueda parcial (Case-insensitive).
     * @return Un [Flow] observable con estados [Resource] que contiene la lista resultante.
     */
    operator fun invoke(
        id: Int? = null,
        title: String? = null
    ): Flow<Resource<List<Posts>>> {
        return repository.observeFilteredPosts(id, title)
    }
}
