package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithFavorite
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostFavoriteDataSource
import com.janes.saenz.puerta.proofonoff.data.mapper.FavoriteDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.mapper.PostDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Favorite
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostFavoriteRepositoryImplTest {

    private lateinit var dataSource: PostFavoriteDataSource
    private lateinit var mapper: FavoriteDataBaseMapper
    private lateinit var mapperPost: PostDataBaseMapper
    private lateinit var repository: PostFavoriteRepositoryImpl

    @Before
    fun setUp() {
        dataSource = mockk()
        mapper = mockk()
        mapperPost = mockk()
        repository = PostFavoriteRepositoryImpl(dataSource, mapper, mapperPost)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getPostsWithFavorites should emit Loading and Success with mapped posts`() = runTest {
        // GIVEN
        val postId = 1
        val mockEntity = mockk<PostWithFavorite>()
        val mockPostEntity = mockk<PostEntity>()
        val mockDomainPost = mockk<Posts>()

        every { mockEntity.post } returns mockPostEntity
        every { dataSource.getPostsWithFavorites(postId) } returns flowOf(mockEntity)
        every { mapperPost.fromEntityListToDomain(any()) } returns listOf(mockDomainPost)

        // WHEN
        val results = repository.getPostsWithFavorites(postId).toList()

        // THEN
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
        assertEquals(listOf(mockDomainPost), (results[1] as Resource.Success).data)

        verify(exactly = 1) { mapperPost.fromEntityListToDomain(match { it.first() == mockPostEntity }) }
    }

    @Test
    fun `insertFavorite should map domain to entity and call dataSource`() = runTest {
        // GIVEN
        val domainFavorite = mockk<Favorite>()
        val entity = mockk<com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity>()

        every { mapper.fromDomainToEntity(domainFavorite) } returns entity
        coEvery { dataSource.insertFavorite(entity) } returns Unit

        // WHEN
        repository.insertFavorite(domainFavorite)

        // THEN
        coVerify(exactly = 1) { dataSource.insertFavorite(entity) }
    }

    @Test
    fun `deleteFavoriteByPostId should call dataSource`() = runTest {
        // GIVEN
        val postId = 10
        coEvery { dataSource.deleteFavoriteByPostId(postId) } returns Unit

        // WHEN
        repository.deleteFavoriteByPostId(postId)

        // THEN
        coVerify(exactly = 1) { dataSource.deleteFavoriteByPostId(postId) }
    }

    @Test(expected = NotImplementedError::class)
    fun `isFavorite should throw NotImplementedError when called`() {
        // Este test documenta el estado actual del TODO y asegura cobertura del método
        repository.isFavorite(1)
    }
}
