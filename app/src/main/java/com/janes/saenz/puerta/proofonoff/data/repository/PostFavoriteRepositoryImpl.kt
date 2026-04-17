package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostFavoriteDataSource
import com.janes.saenz.puerta.proofonoff.data.mapper.FavoriteDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.mapper.PostDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Favorite
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostFavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * PostFavoriteRepositoryImpl - Implementación de persistencia para favoritos.
 *
 * Gestiona el ciclo de vida de los marcadores del usuario, integrando modelos
 * de base de datos relacionales con el dominio de la aplicación.
 *
 * @property remoteData Fuente de datos local (DAO) para favoritos.
 * @property mapper Mapeador para la entidad de relación Favorite.
 * @property mapperPost Mapeador para la entidad de contenido Post.
 */
class PostFavoriteRepositoryImpl @Inject constructor(
    private val remoteData: PostFavoriteDataSource,
    private val mapper: FavoriteDataBaseMapper,
    private val mapperPost: PostDataBaseMapper
) : BaseRepository(), PostFavoriteRepository {

    /**
     * Recupera publicaciones marcadas como favoritas de forma reactiva.
     * * Transforma la relación de base de datos en una lista de dominio [Posts].
     */
    override fun getPostsWithFavorites(postId: Int): Flow<Resource<List<Posts>>> {
        return remoteData.getPostsWithFavorites(postId)
            .map { entities ->
                // Nota: Se mapea la propiedad 'post' de la relación al dominio
                mapperPost.fromEntityListToDomain(listOf(entities.post))
            }
            .asResource()
            .flowOn(Dispatchers.IO)
    }

    /**
     * Inserta un nuevo registro de favorito.
     * @param favorite Objeto de dominio a ser convertido en entidad de persistencia.
     */
    override suspend fun insertFavorite(favorite: Favorite) {
        withContext(Dispatchers.IO) {
            val entity = mapper.fromDomainToEntity(favorite)
            remoteData.insertFavorite(entity)
        }
    }

    /**
     * Elimina la relación de favorito para un post específico.
     * @param postId ID de la publicación.
     */
    override suspend fun deleteFavoriteByPostId(postId: Int) {
        withContext(Dispatchers.IO) {
            remoteData.deleteFavoriteByPostId(postId)
        }
    }

    /**
     * Verifica la existencia de un favorito.
     * Implementar usando un flujo que emita true/false basado en la DB.
     */
    override fun isFavorite(postId: Int): Flow<Resource<Boolean>> {
        TODO("Not yet implemented")
    }
}
