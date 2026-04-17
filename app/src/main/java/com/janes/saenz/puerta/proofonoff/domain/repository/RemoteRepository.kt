package com.janes.saenz.puerta.proofonoff.domain.repository

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import kotlinx.coroutines.flow.Flow

/**
 * RemoteRepository - Contrato de acceso a servicios externos.
 * * Define la comunicación con el backend para la obtención de recursos globales.
 * Las implementaciones de esta interfaz deben gestionar el manejo de excepciones
 * de red (Timeouts, 4xx, 5xx) y transformarlas en estados [Resource].
 */
interface RemoteRepository {

    /**
     * Recupera el listado maestro de publicaciones desde el servidor remoto.
     * * Este flujo es fundamental para procesos de sincronización y mapeo de datos.
     * Se recomienda que la implementación utilice un Dispatcher de IO para no
     * bloquear el hilo que realiza la llamada.
     *
     * @return [Flow] que emite un estado [Resource] envolviendo la lista de [Posts].
     * Representa la fuente de verdad externa antes de la persistencia local.
     */
    suspend fun getPosts(): Flow<Resource<List<Posts>>>
}