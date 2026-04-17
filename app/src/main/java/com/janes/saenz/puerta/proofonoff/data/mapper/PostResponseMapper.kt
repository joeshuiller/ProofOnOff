package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.network.dtos.response.PostsResponse
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import javax.inject.Inject

/**
 * PostResponseMapper - Transformador de modelos de red a modelos de dominio.
 *
 * Esta clase centraliza la lógica de conversión para asegurar que el dominio
 * trate exclusivamente con datos validados y tipados correctamente.
 */
class PostResponseMapper @Inject constructor() {

    /**
     * Mapea un [PostsResponse] individual a la entidad de dominio [Posts].
     * * Realiza el saneamiento de cadenas nulas mediante [orEmpty].
     * * Inyecta marcas de tiempo locales para gestión de caché.
     *
     * @param response Objeto DTO crudo proveniente de la red.
     * @return Entidad de dominio lista para ser procesada por Use Cases.
     */
    fun fromResponseToDomain(response: PostsResponse): Posts {
        return Posts(
            userId = response.userId,
            id = response.id,
            title = response.title.orEmpty(),
            body = response.body.orEmpty(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

    /**
     * Transforma una colección de respuestas de red en una lista de dominio.
     * * Filtra elementos nulos de la lista para prevenir inconsistencias.
     * * Si la entrada es nula, retorna una lista vacía de forma segura.
     *
     * @param responses Lista opcional de DTOs provenientes de la API.
     * @return Lista inmutable de entidades [Posts].
     */
    fun fromResponseListToDomain(responses: List<PostsResponse?>?): List<Posts> {
        return responses?.filterNotNull()?.map { entity ->
            Posts(
                id = entity.id,
                userId = entity.userId,
                title = entity.title.orEmpty(),
                body = entity.body.orEmpty(),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        } ?: emptyList()
    }
}
