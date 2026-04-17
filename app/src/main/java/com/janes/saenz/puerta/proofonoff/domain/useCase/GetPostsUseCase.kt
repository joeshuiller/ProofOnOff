package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.NetworkRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository,
    private val remoteRepository: RemoteRepository,
    private val networkRepository: NetworkRepository
) {
    /**
     * GetPostsUseCase - Refactorización de alto rendimiento.
     * * Cambios realizados:
     * 1. Eliminación de lógica redundante: Un solo punto de salida para el flujo de la DB.
     * 2. Estructura 'Early Return' mental: Se valida la necesidad de red antes de emitir.
     * 3. Robustez: Manejo de estados de Resource sobre la data local.
     */
    operator fun invoke(): Flow<Resource<List<Posts>>> = flow {
        emit(Resource.Loading)

        try {
            // 1. Snapshot instantáneo de la base de datos local
            val localDataResource = repository.observeAllPosts().first()
            val isCacheEmpty = (localDataResource as? Resource.Success)?.data?.isEmpty() != false

            // 2. Lógica de Sincronización (Solo si hay internet)
            if (networkRepository.hasInternetConnection()) {
                if (isCacheEmpty) {
                    // Sincronización forzada: La caché está vacía, necesitamos datos.
                    // Usamos .first() para asegurar que la descarga termine antes de seguir.
                    remoteRepository.getPosts().first().let { result ->
                        if (result is Resource.Success) {
                            repository.clearAndInsertPosts(result.data)
                        }
                    }
                }
            } else {
                // 3. Validación de Error Offline
                if (isCacheEmpty) {
                    emit(Resource.Error("No hay internet y la base de datos local está vacía"))
                    return@flow // Salida prematura: No hay nada que observar
                }
            }

            // 4. Fuente de Verdad Única (SSOT)
            // Tanto si hubo red como si no (y hay datos), emitimos el flujo de la DB
            emitAll(repository.observeAllPosts())
        } catch (e: IOException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}
