package com.janes.saenz.puerta.proofonoff.core

import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostCommentsDataSource
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostCommentsDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostDataSource
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostFavoriteDataSource
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostFavoriteDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.network.source.RemoteDataSource
import com.janes.saenz.puerta.proofonoff.data.network.source.RemoteDataSourceImpl
import com.janes.saenz.puerta.proofonoff.data.repository.AndroidNetworkRepositoryImpl
import com.janes.saenz.puerta.proofonoff.data.repository.PostCommentRepositoryImpl
import com.janes.saenz.puerta.proofonoff.data.repository.PostRepositoryImpl
import com.janes.saenz.puerta.proofonoff.data.repository.RemoteRepositoryImpl
import com.janes.saenz.puerta.proofonoff.domain.repository.NetworkRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.PostCommentRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import com.janes.saenz.puerta.proofonoff.domain.repository.RemoteRepository
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * DataModuleTest - Validación del contrato de Inyección de Dependencias.
 * * Nota: Dado que @Binds genera código en tiempo de compilación, este test
 * asegura que las interfaces y sus implementaciones están correctamente
 * referenciadas en el contrato del módulo.
 */
class DataModuleTest {

    // Simulamos las implementaciones concretas que Hilt inyectaría
    private val networkRepoImpl = mockk<AndroidNetworkRepositoryImpl>()
    private val remoteRepoImpl = mockk<RemoteRepositoryImpl>()
    private val postCommentRepoImpl = mockk<PostCommentRepositoryImpl>()
    private val postRepoImpl = mockk<PostRepositoryImpl>()

    private val remoteDSImpl = mockk<RemoteDataSourceImpl>()
    private val postDSImpl = mockk<PostDataSourceImpl>()
    private val postCommentsDSImpl = mockk<PostCommentsDataSourceImpl>()
    private val postFavoriteDSImpl = mockk<PostFavoriteDataSourceImpl>()

    @Test
    fun `validate repository interfaces can be represented by implementations`() {
        // Validamos que las implementaciones inyectadas cumplen con el contrato de la interfaz
        val networkRepo: NetworkRepository = networkRepoImpl
        val remoteRepo: RemoteRepository = remoteRepoImpl
        val postCommentRepo: PostCommentRepository = postCommentRepoImpl
        val postRepo: PostRepository = postRepoImpl

        assertNotNull(networkRepo)
        assertNotNull(remoteRepo)
        assertNotNull(postCommentRepo)
        assertNotNull(postRepo)
    }

    @Test
    fun `validate datasource interfaces can be represented by implementations`() {
        // Validamos la jerarquía de los DataSources
        val remoteDS: RemoteDataSource = remoteDSImpl
        val postDS: PostDataSource = postDSImpl
        val postCommentsDS: PostCommentsDataSource = postCommentsDSImpl
        val postFavoriteDS: PostFavoriteDataSource = postFavoriteDSImpl

        assertNotNull(remoteDS)
        assertNotNull(postDS)
        assertNotNull(postCommentsDS)
        assertNotNull(postFavoriteDS)
    }

    @Test
    fun `validate binding contracts`() {
        // Validamos que las implementaciones inyectables heredan de las interfaces correctas.
        // Esto garantiza que @Binds no fallará por incompatibilidad de tipos.

        val networkRepo: NetworkRepository = mockk<AndroidNetworkRepositoryImpl>()
        val remoteRepo: RemoteRepository = mockk<RemoteRepositoryImpl>()
        val postCommentRepo: PostCommentRepository = mockk<PostCommentRepositoryImpl>()
        val postRepo: PostRepository = mockk<PostRepositoryImpl>()

        val remoteDS: RemoteDataSource = mockk<RemoteDataSourceImpl>()
        val postDS: PostDataSource = mockk<PostDataSourceImpl>()
        val postCommentsDS: PostCommentsDataSource = mockk<PostCommentsDataSourceImpl>()
        val postFavoriteDS: PostFavoriteDataSource = mockk<PostFavoriteDataSourceImpl>()

        // Si llegamos aquí, las asignaciones son válidas (Liskov Substitution Principle)
        assertNotNull(networkRepo)
        assertNotNull(remoteRepo)
        assertNotNull(postCommentRepo)
        assertNotNull(postRepo)
        assertNotNull(remoteDS)
        assertNotNull(postDS)
        assertNotNull(postCommentsDS)
        assertNotNull(postFavoriteDS)
    }
}
