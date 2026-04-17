package com.janes.saenz.puerta.proofonoff.domain.repository

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Favorite
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import kotlinx.coroutines.flow.Flow

/**
 * PostFavoriteRepository - Contrato para la gestión de marcadores (Favoritos).
 * * Define las operaciones necesarias para persistir y consultar el interés
 * del usuario sobre publicaciones específicas.
 */
interface PostFavoriteRepository {

    /**
     * Recupera una lista reactiva de publicaciones vinculadas a sus favoritos.
     * * Útil para la pantalla de "Mis Favoritos" o vistas filtradas.
     * * @param postId Identificador opcional para filtrar la relación.
     * @return [Flow] que emite la lista de posts marcados como favoritos.
     */
    fun getPostsWithFavorites(postId: Int): Flow<Resource<List<Posts>>>

    /**
     * Persiste un nuevo registro de favorito en el almacenamiento local.
     * * Esta es una función de suspensión para asegurar que la escritura
     * no bloquee el hilo de la interfaz de usuario.
     * * @param favorite Entidad [Favorite] que contiene la relación usuario-post.
     */
    suspend fun insertFavorite(favorite: Favorite)

    /**
     * Elimina el estado de favorito asociado a un post específico.
     * * Implementación clave para la acción de "unfavorite".
     * * @param postId ID del post cuya relación de favorito debe removerse.
     */
    suspend fun deleteFavoriteByPostId(postId: Int)

    /**
     * Observa en tiempo real si un post específico está marcado como favorito.
     * * Es el pilar para la reactividad de iconos de estado en la pantalla de detalle.
     * * @param postId ID del post a verificar.
     * @return [Flow] que emite 'true' si existe la relación, 'false' en caso contrario.
     */
    fun isFavorite(postId: Int): Flow<Resource<Boolean>>
}
