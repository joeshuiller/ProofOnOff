package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import javax.inject.Inject

/**
 * ClearAndInsertPostsUseCase - Interactor de Sincronización Forzada.
 *
 * Implementa la estrategia de "Reemplazo Total" para la caché de publicaciones.
 * Este componente es vital para garantizar que no existan datos huérfanos o
 * inconsistentes en el almacenamiento local tras una actualización mayor.
 *
 * @property repository Contrato de persistencia encargado de la gestión de la tabla de posts.
 */
class ClearAndInsertPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    /**
     * Ejecuta de forma secuencial y atómica la limpieza de la tabla y la inserción masiva.
     * * Nota de Performance: Al ser una función de suspensión, se espera que la
     * implementación del repositorio gestione el Dispatcher adecuado (IO) para
     * evitar bloqueos en el Main Thread durante operaciones de escritura pesadas.
     *
     * @param posts Lista de entidades [Posts] que representarán el nuevo estado de la base de datos.
     */
    suspend operator fun invoke(posts: List<Posts>) {
        // Validación preventiva: Evita limpiar la DB si la lista entrante es nula
        if (posts.isNotEmpty()) {
            repository.clearAndInsertPosts(posts)
        }
    }
}
